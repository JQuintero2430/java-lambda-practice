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
public class BankResponse {
    private BigDecimal quote;
    private BigDecimal rate;
    private Integer term;
    private BigDecimal quoteWithAccount;
    private BigDecimal rateWithAccount;
    private Integer termWithAccount;

}
