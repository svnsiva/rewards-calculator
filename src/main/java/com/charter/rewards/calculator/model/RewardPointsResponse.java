package com.charter.rewards.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardPointsResponse {
    private long month1points;
    private long month2points;
    private long month3points;
    private long totalPoints;
}
