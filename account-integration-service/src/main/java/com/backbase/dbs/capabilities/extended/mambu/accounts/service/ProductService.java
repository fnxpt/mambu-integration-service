package com.backbase.dbs.capabilities.extended.mambu.accounts.service;

import com.backbase.integration.account.rest.spec.v2.arrangementdetails.ArrangementDetailsGetResponseBody;
import com.backbase.integration.account.rest.spec.v2.balances.BalanceGetResponseBody;
import com.backbase.dbs.capabilities.extended.mambu.accounts.mapper.MambuProductMapper;
import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.Loan;
import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.Saving;
import com.backbase.dbs.capabilities.extended.mambu.service.LoanService;
import com.backbase.dbs.capabilities.extended.mambu.service.SavingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for handling the business logic for products integration
 */
@Component
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final MambuProductMapper mambuProductMapper;
    private LoanService loanService;
    private SavingService savingService;

    @Autowired
    public ProductService(LoanService loanService, SavingService savingService, MambuProductMapper mambuProductMapper) {
        this.loanService = loanService;
        this.savingService = savingService;
        this.mambuProductMapper = mambuProductMapper;
    }

    /**
     * Get arrangement details by accountId, mapping Saving external model or Loan in case Saving is null
     *
     * @return ArrangementDetails
     */
    public ArrangementDetailsGetResponseBody getProductDetail(String accountId) {

        ArrangementDetailsGetResponseBody response = new ArrangementDetailsGetResponseBody();

        List<Saving> savings = savingService.getSavingById(accountId);

        if (!CollectionUtils.isEmpty(savings)) {
            LOGGER.info("Saving account found: {}", savings);
            response = mambuProductMapper.savingToArrangementDetails(savings.stream().findFirst().get());
        } else {
            List<Loan> loan = loanService.getLoanById(accountId);
            if (!CollectionUtils.isEmpty(loan)) {
                LOGGER.info("Loan account found: {}", savings);
                response = mambuProductMapper.loanToArrangementDetails(loan.stream().findFirst().get());
            }
        }

        return response;
    }

    /**
     * Get a list of the Balances for the given account IDs, containing both Saving and Loan mapped objects
     *
     * @return Balances list
     */
    public List<BalanceGetResponseBody> getProductBalance(List<String> accounts) {

        List<Saving> savings = savingService.getSavingByIdList(accounts);
        List<Loan> loans = loanService.getLoansByIdList(accounts);

        List<BalanceGetResponseBody> mappedSavings = new ArrayList<>();
        List<BalanceGetResponseBody> mappedLoans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(savings)) {
            LOGGER.info("Savings account found: {}", savings);
            mappedSavings = savings.stream()
                .map(mambuProductMapper::savingToBalance)
                .collect(Collectors.toList());
        }

        if (!CollectionUtils.isEmpty(loans)) {
            LOGGER.info("Loans account found: {}", loans);
            mappedLoans = loans.stream()
                .map(mambuProductMapper::loanToBalance)
                .collect(Collectors.toList());
        }

        return Stream.concat(mappedSavings.stream(), mappedLoans.stream())
            .collect(Collectors.toList());
    }

}