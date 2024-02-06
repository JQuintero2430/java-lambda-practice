package com.jorge.practice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRequest {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal rate;
}
