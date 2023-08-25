package com.charter.rewards.calculator.service.impl;

import com.charter.rewards.calculator.exception.CustomException;
import com.charter.rewards.calculator.model.RewardPointsResponse;
import com.charter.rewards.calculator.model.TransactionRequest;
import com.charter.rewards.calculator.repository.TransactionsRepository;
import com.charter.rewards.calculator.repository.entity.Transactions;
import com.charter.rewards.calculator.service.RewardsCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RewardsCalculatorServiceImpl implements RewardsCalculatorService {

    @Value("${rewardPointsThreshold.onePoint}")
    private Long onePointThreshold;

    @Value("${rewardPointsThreshold.twoPoints}")
    private Long twoPointThreshold;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Override
    public RewardPointsResponse getRewardPoints (Long userId ) throws CustomException {
        log.info("Rewards Calculator service method is invoked");

        try {

            //Fetching last 3 months of transactions from the database for this particular userId.
            Date dateOffset = Date.from(LocalDate.now().minusMonths(3).atStartOfDay(ZoneOffset.UTC).toInstant());
            List<Transactions> transactions = transactionsRepository.findAllByUserIdAndDateAfter(userId, dateOffset);

            log.info("Successfully fetched transactions from database");
            if (transactions.isEmpty()) {
                log.error("No transactions found for given userId in last three months");
                throw new CustomException("No transactions found for given userId in last three months", HttpStatus.NOT_FOUND);
            }

            //Assuming that we are calculating rewards for last three months, filtering transactions for last three months
            Long rewardPointsForMonth1 = getRewardPointsForMonth(transactions, 1);
            Long rewardPointsForMonth2 = getRewardPointsForMonth(transactions, 2);
            Long rewardPointsForMonth3 = getRewardPointsForMonth(transactions, 3);

            Long totalRewardPoints = rewardPointsForMonth1 + rewardPointsForMonth2 + rewardPointsForMonth3;

            log.info("Successfully calculated rewards for given user for last three months");
            return new RewardPointsResponse(rewardPointsForMonth1, rewardPointsForMonth2, rewardPointsForMonth3, totalRewardPoints);

        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Exception occurred in reward calculator service. Cause " + ex.getMessage());
            throw new CustomException("Internal Service Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    This method calculated rewards for past month for a given month number, so we are filtering records from past month (month -1) to given month number.
     */
    private Long getRewardPointsForMonth (List<Transactions> transactions, int month) {
        Date startDate = Date.from(LocalDate.now().minusMonths(month).atStartOfDay(ZoneOffset.UTC).toInstant());
        Date endDate = Date.from(LocalDate.now().minusMonths(month-1).atStartOfDay(ZoneOffset.UTC).toInstant());

        //Filtering transactions for last month including today
        List<Transactions> filteredTransactions = transactions.stream()
                .filter(tx -> tx.getDate().compareTo(startDate) >0
                        && tx.getDate().compareTo(endDate) <=0)
                .collect(Collectors.toList());
        Long rewardPoints = 0L;

        //Calculating reward points for filtered transaction
        for (Transactions tx : filteredTransactions) {
            rewardPoints += getRewardPointsPerTransaction(tx);
        }
        return rewardPoints;
    }

    /*
    Calculates reward points per transaction with given logic of 2 points for amount more than 100 and 1 point for amount more than 50
     */
    private long getRewardPointsPerTransaction (Transactions transaction) {
        long amount = Math.round(transaction.getAmount());
        long rewardPoints = 0L;
        if (amount > twoPointThreshold) {
            rewardPoints = onePointThreshold + 2*(amount-twoPointThreshold);
        } else if (amount > onePointThreshold) {
            rewardPoints = amount-onePointThreshold;
        }
        return rewardPoints;
    }

    @Override
    public String addTransaction (TransactionRequest request, Long userId) throws CustomException {
        log.info("Add Transaction service method is invoked");
        //Adding transaction with given details
        try{
            Transactions transaction = new Transactions();
            transaction.setUserId(userId);
            transaction.setAmount(request.getAmount());
            transaction.setDate(formatDate(request.getDate()));
            transactionsRepository.save(transaction);

            log.info("Successfully added transaction into Database");
            return "Success";
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Exception occurred in add transaction service. Cause " + ex.getMessage());
            throw new CustomException("Internal Service Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Converts date in string format to Date object
    private Date formatDate (String inputDate) throws CustomException {
        try {
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.parse(inputDate);
        } catch (ParseException ex) {
            log.error("Invalid date format in the request");
            throw new CustomException("Invalid Date Format", HttpStatus.BAD_REQUEST);
        }
    }

}
