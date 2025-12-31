package com.example.ecom.fraud.controller;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class FraudCheckControllerTest {

    @Test
    public void testFraudCheckRequest() {
        FraudCheckController.FraudCheckRequest request = new FraudCheckController.FraudCheckRequest();
        request.setCustomerId(1L);
        request.setAmount(new BigDecimal("100.00"));

        assertEquals(Long.valueOf(1L), request.getCustomerId());
        assertEquals(new BigDecimal("100.00"), request.getAmount());
    }

    @Test
    public void testFraudCheckResponse() {
        FraudCheckController.FraudCheckResponse response = new FraudCheckController.FraudCheckResponse("SAFE",
                "No risk");

        assertEquals("SAFE", response.getStatus());
        assertEquals("No risk", response.getReason());
    }
}
