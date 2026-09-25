package com.fooddelivery.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.grpc.server.port=0")
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
