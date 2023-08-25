package com.charter.rewards.calculator.service;

import com.charter.rewards.calculator.exception.CustomException;
import com.charter.rewards.calculator.model.RewardPointsResponse;
import com.charter.rewards.calculator.model.TransactionRequest;

public interface RewardsCalculatorService {
    public RewardPointsResponse getRewardPoints (Long userId ) throws CustomException;
    public String addTransaction (TransactionRequest request, Long userId) throws CustomException;
}
