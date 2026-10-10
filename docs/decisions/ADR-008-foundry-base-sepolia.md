# ADR-008 — Foundry and Base Sepolia for Blockchain Development

## Context

The platform requires a Solidity smart contract for cryptocurrency escrow and multi-party settlement.

The contract must support development, automated testing, deployment, and interaction from the Payment Service without using real production funds.

A suitable Solidity development toolchain and test blockchain are therefore required.

## Decision

Use **Foundry** for Solidity development and testing, and **Base Sepolia** as the external Ethereum-compatible Layer-2 test network.

The Payment Service communicates with the deployed contract through Ethereum JSON-RPC over HTTPS.

## Rationale

### Foundry instead of Hardhat

Both **Foundry** and **Hardhat 3** are mature Ethereum development frameworks offering Solidity development, deployment, and testing capabilities, including fuzz and invariant testing.

Hardhat provides an extensive JavaScript/TypeScript ecosystem, plugins, and flexible scripting. However, its use would introduce a Node.js-based toolchain primarily for the blockchain component of the project.

Foundry was preferred because it provides:

- a comparable set of core smart-contract development, testing, and deployment capabilities to Hardhat 3;
- a compact, Solidity-oriented development workflow;
- no additional Node.js runtime requirement.

Fuzz and invariant testing are particularly valuable for the escrow contract because they allow its state transitions and settlement rules to be exercised across many automatically generated inputs.

Foundry therefore provides the required smart-contract development and testing capabilities while keeping the project's technology stack relatively limited.

### Layer-2 instead of Layer-1

Ethereum Layer-2 networks are designed to provide lower transaction fees, higher throughput, and faster transaction inclusion compared with Ethereum Layer 1, while retaining compatibility with Ethereum tooling and smart contracts.

These characteristics are particularly advantageous for a future production deployment of the food-delivery platform, where frequent, relatively low-value payments would make Ethereum Layer-1 transaction fees disproportionately expensive. Lower fees and improved throughput could make cryptocurrency payments more economically practical as the platform scales.

For development, both Layer-1 and Layer-2 test networks use test cryptocurrency without real monetary value. **Base Sepolia** was selected because it provides an Ethereum-compatible Layer-2 environment that reflects the intended production deployment model while allowing escrow and multi-party settlement functionality to be tested without production funds.

The trade-off is that Layer-2 systems introduce additional considerations, including sequencer dependence and the distinction between Layer-2 transaction confirmation and final settlement on Ethereum Layer 1. Bridging and withdrawal finalization would introduce further complexity if cross-chain transfers were incorporated into the platform.

## Consequences

Benefits include:

- Solidity-native development and testing;
- strong fuzz and invariant testing support;
- no additional Node.js runtime required for smart-contract development;
- Ethereum-compatible JSON-RPC integration;
- realistic testing of escrow and settlement behaviour without production funds;
- alignment with a potential production deployment benefiting from lower transaction fees and higher throughput than Ethereum Layer 1.

Trade-offs include:

- dependence on external blockchain RPC availability;
- test-network behaviour does not fully represent production conditions;
- blockchain transactions introduce confirmation and finality considerations;
- Layer-2 operation involves sequencer and Ethereum settlement dependencies;
- state-changing transaction retries require safeguards against duplicate effects;
- confirmed on-chain settlements cannot be reversed through ordinary database rollback or Saga compensation;
- RPC or network failures require retries, circuit breaking, and settlement-pending handling.
