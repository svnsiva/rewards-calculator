package com.charter.rewards.calculator.controller;

import com.charter.rewards.calculator.exception.CustomException;
import com.charter.rewards.calculator.model.RewardPointsResponse;
import com.charter.rewards.calculator.model.TransactionRequest;
import com.charter.rewards.calculator.service.RewardsCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RewardsCalculatorController {

    @Autowired
    private RewardsCalculatorService service;

    @GetMapping("/users/{userId}/rewards")
    public ResponseEntity<RewardPointsResponse> getRewardPoints(@PathVariable("userId") Long userId) throws CustomException {

        log.info("Rewards Calculator controller is invoked");
        RewardPointsResponse response = service.getRewardPoints(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/transactions")
    public ResponseEntity<String> addTransaction (
            @PathVariable("userId") Long userId,
            @RequestBody TransactionRequest request) throws CustomException {

        log.info("Add Transaction controller is invoked");
        if (null == request.getAmount() || null == request.getDate()) {
            log.error("Mandatory information is missing in the request");
            throw new CustomException("Mandatory information is missing in the request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.addTransaction(request,userId),HttpStatus.CREATED);
    }

}
