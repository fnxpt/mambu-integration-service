package com.backbase.dbs.capabilities.extended.mambu.service;

import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.FilterConstraint;
import com.backbase.dbs.capabilities.extended.mambu.client.mambu.domain.Saving;
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
public class SavingService extends AbstractService<Saving> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavingService.class);

    public SavingService(RestTemplate restTemplate, @Value("${mambu.saving.url}") String url) {
        super(restTemplate, url);
    }

    /**
     * Retrieve list of Saving filtered by account ID
     *
     * @return savings list
     */
    public List<Saving> getSavingById(String accountID) {

        FilterConstraint filterConstraint = new FilterConstraint();
        filterConstraint.setFilterSelection(Constant.FILTER_SELECTION_ENCODED_KEY);
        filterConstraint.setFilterElement(Constant.FILTER_OPERATOR_EQUALS);
        filterConstraint.setValue(accountID);
        filterConstraint.setDataItemType(Constant.FILTER_DATATYPE_SAVINGS);

        Search search = new Search();
        search.setFilterConstraints(Arrays.asList(filterConstraint));
        List<Saving> savings = super.search(search);
        LOGGER.info("Saving found: {}", savings);
        return savings;
    }

    /**
     * Retrieve list of Loan filtered by multiple account IDs
     *
     * @return savings list
     */
    public List<Saving> getSavingByIdList(List<String> accountsIDS) {

        FilterConstraint filterConstraint = new FilterConstraint();
        filterConstraint.setFilterSelection(Constant.FILTER_SELECTION_ENCODED_KEY);
        filterConstraint.setFilterElement(Constant.FILTER_OPERATOR_IN);
        filterConstraint.setValues(accountsIDS);
        filterConstraint.setDataItemType(Constant.FILTER_DATATYPE_SAVINGS);

        Search search = new Search();
        search.setFilterConstraints(Arrays.asList(filterConstraint));
        List<Saving> savings = super.search(search);
        LOGGER.info("Saving found: {}", savings);
        return savings;
    }

    /**
     * Retrieve list of Loan filtered by customer ID
     *
     * @return loans list
     */
    public List<Saving> getSavingByCustomerId(String customerId) {
        return super.list(customerId, Constant.FILTER_DATATYPE_SAVINGS);
    }

    @Override
    protected ParameterizedTypeReference<List<Saving>> getParameter() {
        return new ParameterizedTypeReference<List<Saving>>() {
        };
    }
}
