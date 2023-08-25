package com.charter.rewards.calculator.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name="TRANSACTIONS")
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "DATE")
    private Date date;

}
