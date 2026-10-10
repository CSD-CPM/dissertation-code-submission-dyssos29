package com.fooddelivery.order_service.grpc.client;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fooddelivery.restaurant.v1.QuoteOrderLine;
import com.fooddelivery.restaurant.v1.QuoteOrderRequest;
import com.fooddelivery.restaurant.v1.QuoteOrderResponse;
import com.fooddelivery.restaurant.v1.RestaurantMenuServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import jakarta.annotation.PreDestroy;

@Service
public class RestaurantQuotationGrpcClient {
    private static final Metadata.Key<String> TOKEN_HEADER =
            Metadata.Key.of(
                    "x-internal-quote-token",
                    Metadata.ASCII_STRING_MARSHALLER);
    private final ManagedChannel channel;
    private final RestaurantMenuServiceGrpc.RestaurantMenuServiceBlockingStub baseStub;
    private final String quoteToken;

    public RestaurantQuotationGrpcClient(
            @Value("${restaurant-service.grpc.host}")
            String host,
            @Value("${restaurant-service.grpc.port}")
            int port,
            @Value("${restaurant-service.grpc.quote-token:}")
            String quoteToken) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.baseStub = RestaurantMenuServiceGrpc.newBlockingStub(channel);
        this.quoteToken = quoteToken;
    }

    public Quote quote(String restaurantId, List<RequestedLine> lines) {
        QuoteOrderRequest.Builder request = QuoteOrderRequest.newBuilder()
                                                .setRestaurantId(restaurantId);

        for (RequestedLine line : lines) {
            request.addLines(QuoteOrderLine.newBuilder()
                                .setMenuItemId(line.menuItemId())
                                .setQuantity(line.quantity())
                                .build());
        }

        var stub = baseStub.withDeadlineAfter(3, TimeUnit.SECONDS);

        if (quoteToken != null && !quoteToken.isBlank()) {
            Metadata metadata = new Metadata();
            metadata.put(TOKEN_HEADER, quoteToken);

            stub = stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
        }

        QuoteOrderResponse response = stub.quoteOrder(request.build());

        List<QuotedLine> quotedLines = response.getItemsList()
                                            .stream()
                                            .map(item -> new QuotedLine(
                                                    item.getMenuItemId(),
                                                    item.getName(),
                                                    item.getQuantity(),
                                                    item.getUnitPriceMinorUnits(),
                                                    item.getLineTotalMinorUnits()))
                                            .toList();

        return new Quote(
                response.getRestaurantId(),
                response.getRestaurantOwnerSub(),
                quotedLines,
                response.getTotalMinorUnits());
    }

    @PreDestroy
    void shutdown() {
        channel.shutdown();
    }

    public record RequestedLine(
            String menuItemId,
            int quantity) {}

    public record QuotedLine(
            String menuItemId,
            String name,
            int quantity,
            long unitPriceMinorUnits,
            long lineTotalMinorUnits) {}

    public record Quote(
            String restaurantId,
            String restaurantOwnerSub,
            List<QuotedLine> items,
            long totalMinorUnits) {}
}
