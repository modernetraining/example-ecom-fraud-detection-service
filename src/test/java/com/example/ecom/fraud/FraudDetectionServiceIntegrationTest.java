package com.example.ecom.fraud;

import com.example.ecom.common.dto.ApiResponse;
import com.example.ecom.fraud.client.KycClient;
import com.example.ecom.fraud.client.RiskScoreClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "KYC_SERVICE_URL=http://localhost:8081",
        "RISK_SCORE_SERVICE_URL=http://localhost:8082"
})
@AutoConfigureMockMvc(addFilters = false)
public class FraudDetectionServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KycClient kycClient;

    @MockBean
    private RiskScoreClient riskScoreClient;

    @Test
    public void testCheckFraudAllowed() throws Exception {
        KycClient.KycResponse kycResponse = new KycClient.KycResponse();
        kycResponse.setStatus("ALLOW");
        when(kycClient.checkCustomer(any(KycClient.KycRequest.class)))
                .thenReturn(ApiResponse.success(kycResponse));

        RiskScoreClient.RiskCheckRequest riskResponse = new RiskScoreClient.RiskCheckRequest();
        riskResponse.setRiskScore("HIGH"); // Assuming HIGH is good based on logic
        when(riskScoreClient.assessRisk(any(RiskScoreClient.RiskCheckRequest.class)))
                .thenReturn(ApiResponse.success(riskResponse));

        mockMvc.perform(post("/api/fraud/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":123,\"amount\":100.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ALLOWED"));
    }
}
