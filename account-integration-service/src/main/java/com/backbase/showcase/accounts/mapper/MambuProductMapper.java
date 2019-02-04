package com.backbase.showcase.accounts.mapper;

import com.backbase.integration.account.rest.spec.v2.arrangementdetails.ArrangementDetailsGetResponseBody;
import com.backbase.integration.account.rest.spec.v2.balances.BalanceGetResponseBody;
import com.backbase.showcase.client.mambu.domain.Loan;
import com.backbase.showcase.client.mambu.domain.Saving;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper class for converting external models to Backbase models
 */
@Component
public class MambuProductMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MambuProductMapper.class);

    /**
     * Map Saving external model to ArrangementDetails
     *
     * @return ArrangementDetails
     */
    public ArrangementDetailsGetResponseBody savingToArrangementDetails(Saving saving) {

        return new ArrangementDetailsGetResponseBody()
            .withName(saving.getName())
            .withBBAN(saving.getId())
            .withCurrency(ArrangementDetailsGetResponseBody.Currency.fromValue(saving.getCurrencyCode()))
            .withAvailableBalance(new BigDecimal(saving.getBalance()))
            .withBookedBalance(new BigDecimal(saving.getBalance()))
            .withAccountInterestRate(new BigDecimal(saving.getAccruedInterest()))
            .withPrincipalAmount(new BigDecimal(saving.getBalance()));
    }

    /**
     * Map Loan external model to ArrangementDetails
     *
     * @return ArrangementDetails
     */
    public ArrangementDetailsGetResponseBody loanToArrangementDetails(Loan loan) {

        return new ArrangementDetailsGetResponseBody()
            .withName(loan.getLoanName())
            .withBBAN(loan.getId())
            .withCurrency(ArrangementDetailsGetResponseBody.Currency.EUR)
            .withAvailableBalance(new BigDecimal(loan.getPrincipalBalance()))
            .withAccountInterestRate(new BigDecimal(loan.getAccruedInterest()))
            .withPrincipalAmount(new BigDecimal(loan.getPrincipalBalance()));
    }

    /**
     * Map Saving external model to Balance
     *
     * @return Balance
     */
    public BalanceGetResponseBody savingToBalance(Saving saving) {

        BalanceGetResponseBody balanceGetResponseBody = new BalanceGetResponseBody();
        balanceGetResponseBody.setArrangementId(saving.getEncodedKey());
        balanceGetResponseBody.setBookedBalance(new BigDecimal(saving.getBalance()));
        balanceGetResponseBody.setAvailableBalance(new BigDecimal(saving.getBalance()));
        return balanceGetResponseBody;
    }

    /**
     * Map Loan external model to Balance
     *
     * @return Balance
     */
    public BalanceGetResponseBody loanToBalance(Loan loan) {

        BalanceGetResponseBody balanceGetResponseBody = new BalanceGetResponseBody();
        balanceGetResponseBody.setArrangementId(loan.getEncodedKey());
        balanceGetResponseBody.setAvailableBalance(new BigDecimal(loan.getPrincipalBalance()));
        balanceGetResponseBody.setBookedBalance(new BigDecimal(loan.getPrincipalBalance()));
        return balanceGetResponseBody;
    }
}