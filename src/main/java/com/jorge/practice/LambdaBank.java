package com.jorge.practice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LambdaBank implements RequestHandler<BankRequest, BankResponse> {
    MathContext mathContext = MathContext.DECIMAL128;
    /**
     * P = Monto del préstamo
     * i = Tasa de interés mensual
     * n = Plazo del crédito en meses
     * Cuota mensual = (P * i) / (1 - (1 + i) ^ (-n))
     */
    @Override
    public BankResponse handleRequest(BankRequest bankRequest, Context context) {


        BigDecimal amount = bankRequest.getAmount()
                            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal monthlyRate = bankRequest.getRate()
                .setScale(2, RoundingMode.HALF_UP)
                .divide(new BigDecimal(100), mathContext);

        BigDecimal monthlyRateWithAccount = bankRequest.getRate()
                .subtract(BigDecimal.valueOf(0.2), mathContext)
                .setScale(2, RoundingMode.HALF_UP)
                .divide(new BigDecimal(100), mathContext);

        Integer term = bankRequest.getTerm();

        BigDecimal monthlyQuote = this.calculateMonthlyQuote(amount, monthlyRate, term);
        BigDecimal monthlyQuoteWithAccount = this.calculateMonthlyQuote(amount, monthlyRateWithAccount, term);

        return BankResponse.builder()
                .quote(monthlyQuote)
                .rate(monthlyRate.multiply(new BigDecimal(100), mathContext))
                .term(term)
                .quoteWithAccount(monthlyQuoteWithAccount)
                .rateWithAccount(monthlyRateWithAccount.multiply(new BigDecimal(100), mathContext))
                .termWithAccount(term)
                .build();
    }

    public BigDecimal calculateMonthlyQuote(BigDecimal amount, BigDecimal monthlyRate, Integer term) {
        // Calcular (1 + i)
        BigDecimal onePlusRate = monthlyRate.add(BigDecimal.ONE, mathContext);

        // Calcular (1 + i) ^ n y luego tomar el recíproco para obtener (1 + i) ^ -n
        BigDecimal onePlusRateToN = onePlusRate.pow(term, mathContext);
        BigDecimal onePlusRateToNegativeN = BigDecimal.ONE.divide(onePlusRateToN, mathContext);

        // Calcular la cuota mensual
        BigDecimal numerator = amount.multiply(monthlyRate, mathContext);
        BigDecimal denominator = BigDecimal.ONE.subtract(onePlusRateToNegativeN, mathContext);
        BigDecimal monthlyPayment = numerator.divide(denominator, mathContext);

        // Establecer el resultado a 2 decimales
        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_UP);
        return monthlyPayment;
    }

}