package com.charter.rewards.calculator.controller;

import com.charter.rewards.calculator.exception.CustomException;
import com.charter.rewards.calculator.model.RewardPointsResponse;
import com.charter.rewards.calculator.model.TransactionRequest;
import com.charter.rewards.calculator.service.RewardsCalculatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(MockitoJUnitRunner.class)
public class RewardsCalculatorControllerTest {

    @InjectMocks
    private RewardsCalculatorController rewardsCalculatorController;

    @Mock
    private RewardsCalculatorService service;

    @Test
    public void getRewardsSuccessTest () throws CustomException {
        Mockito.when(service.getRewardPoints(Mockito.anyLong())).thenReturn(getResponse());
        ResponseEntity<RewardPointsResponse> response = rewardsCalculatorController.getRewardPoints(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assert.assertEquals(749L, response.getBody().getTotalPoints());
        Assert.assertEquals(348L, response.getBody().getMonth1points());
        Assert.assertEquals(101L, response.getBody().getMonth2points());
        Assert.assertEquals(300L, response.getBody().getMonth3points());
    }

    @Test (expected = CustomException.class)
    public void userNotFoundException () throws CustomException {
        Mockito.when(service.getRewardPoints(Mockito.anyLong())).thenThrow(new CustomException("No transactions found for given userId in last three months",HttpStatus.NOT_FOUND));
        rewardsCalculatorController.getRewardPoints(1L);
    }

    @Test
    public void addTransactionsSuccessTest () throws CustomException {
        Mockito.when(service.addTransaction(Mockito.any(),Mockito.anyLong())).thenReturn("Success");

        ResponseEntity response = rewardsCalculatorController.addTransaction(1001L,new TransactionRequest(100.0,"08-15-2023"));
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test(expected = CustomException.class)
    public void addTransactionsErrorTest () throws CustomException {
        rewardsCalculatorController.addTransaction(1001L,new TransactionRequest(null,"08-15-2023"));
    }

    private RewardPointsResponse getResponse () {
        RewardPointsResponse response = new RewardPointsResponse();
        response.setMonth1points(348L);
        response.setMonth2points(101L);
        response.setMonth3points(300L);
        response.setTotalPoints(749L);
        return response;
    }
}
