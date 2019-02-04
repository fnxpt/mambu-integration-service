package com.backbase.dbs.capabilities.extended.mambu.service;

import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.FilterConstraint;
import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.Loan;
import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.Search;
import com.backbase.dbs.capabilities.extended.mambu.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Service implementation for Loan object
 */
@Service
public class LoanService extends AbstractService<Loan> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavingService.class);

    public LoanService(RestTemplate restTemplate, @Value("${mambu.loan.url}") String url) {
        super(restTemplate, url);
    }

    /**
     * Retrieve list of Loan filtered by account ID
     *
     * @return loans list
     */
    public List<Loan> getLoanById(String accountID) {
        LOGGER.info("Account Id: {}", accountID);
        FilterConstraint filterConstraint = new FilterConstraint();
        filterConstraint.setFilterSelection(Constant.FILTER_SELECTION_ENCODED_KEY);
        filterConstraint.setFilterElement(Constant.FILTER_OPERATOR_EQUALS);
        filterConstraint.setValue(accountID);
        filterConstraint.setDataItemType(Constant.FILTER_DATATYPE_LOAN);

        Search search = new Search();
        search.setFilterConstraints(Arrays.asList(filterConstraint));

        List<Loan> loans = super.search(search);

        LOGGER.info("Loans found: {}", loans);
        return loans;
    }

    /**
     * Retrieve list of Loan filtered by multiple account IDs
     *
     * @return loans list
     */
    public List<Loan> getLoansByIdList(List<String> accountIDs) {
        LOGGER.info("Account Ids: {}", accountIDs);
        FilterConstraint filterConstraint = new FilterConstraint();
        filterConstraint.setFilterSelection(Constant.FILTER_SELECTION_ENCODED_KEY);
        filterConstraint.setFilterElement(Constant.FILTER_OPERATOR_IN);
        filterConstraint.setValues(accountIDs);
        filterConstraint.setDataItemType(Constant.FILTER_DATATYPE_LOAN);

        Search search = new Search();
        search.setFilterConstraints(Arrays.asList(filterConstraint));

        List<Loan> loans = super.search(search);

        LOGGER.info("Loans found: {}", loans);
        return loans;
    }

    /**
     * Retrieve list of Loan filtered by customer ID
     *
     * @return loans list
     */
    public List<Loan> getLoanListByCustomerId(String customerId) {
        return super.list(customerId, Constant.FILTER_DATATYPE_LOAN);
    }

    @Override
    protected ParameterizedTypeReference<List<Loan>> getParameter() {
        return new ParameterizedTypeReference<List<Loan>>() {
        };
    }
}
