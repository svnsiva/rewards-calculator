package com.charter.rewards.calculator.service;

import com.charter.rewards.calculator.exception.CustomException;
import com.charter.rewards.calculator.model.RewardPointsResponse;
import com.charter.rewards.calculator.model.TransactionRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
public class RewardsCalculatorServiceTest {

    @Autowired
    RewardsCalculatorService service;

    @Test
    public void getRewardPointsSuccessUser1 () throws CustomException {
        RewardPointsResponse response = service.getRewardPoints(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(622L, response.getTotalPoints());
        Assert.assertEquals(454L, response.getMonth1points());
        Assert.assertEquals(118L, response.getMonth2points());
        Assert.assertEquals(50L, response.getMonth3points());
    }

    @Test
    public void getRewardPointsSuccessUser2 () throws CustomException {
        RewardPointsResponse response = service.getRewardPoints(2L);
        Assert.assertNotNull(response);
        Assert.assertEquals(278L, response.getMonth1points());
        Assert.assertEquals(290L, response.getMonth2points());
        Assert.assertEquals(81L, response.getMonth3points());
        Assert.assertEquals(649L, response.getTotalPoints());
    }

    @Test (expected = CustomException.class)
    public void noTransactionsFoundException () throws CustomException {
        service.getRewardPoints(21L);
    }

    @Test
    public void addTransaction() throws CustomException {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(110.00);
        request.setDate("08-08-2023");
        String response = service.addTransaction(request, 3L);
        Assert.assertNotNull(response);
        Assert.assertEquals("Success", response);
    }

    @Test (expected = CustomException.class)
    public void invalidDateException() throws CustomException {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(110.00);
        request.setDate("08-082023");
        service.addTransaction(request, 3L);
    }

}
