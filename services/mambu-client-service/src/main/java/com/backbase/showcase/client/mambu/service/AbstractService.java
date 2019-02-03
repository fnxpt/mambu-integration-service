package com.backbase.showcase.client.mambu.service;

import com.backbase.showcase.client.mambu.domain.FilterConstraint;
import com.backbase.showcase.client.mambu.domain.Search;
import com.backbase.showcase.client.mambu.util.Constant;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Class to be implemented from the different integration services
 */
public abstract class AbstractService<T> {

    protected final RestTemplate restTemplate;

    protected final String url;

    @Value("${mambu.auth.password}")
    protected String password;

    public AbstractService(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    /**
     * Perform a search with the given params against the given endpoint
     *
     * @return list of results
     */
    protected List<T> search(Search search) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(Constant.HEADER_AUTH_NAME, getAuth());

        HttpEntity httpEntity = new HttpEntity(search, httpHeaders);

        ResponseEntity<List<T>> responseEntity = restTemplate
            .exchange(url, HttpMethod.POST, httpEntity, getParameter());
        return responseEntity.getBody();
    }


    /**
     * Get basic auth header with type and encoded password
     *
     * @return auth type and encoded password
     */
    private String getAuth() {

        byte[] encodedAuth = Base64.encodeBase64(
            password.getBytes(Charset.forName(Constant.PASSWORD_CHARSET)));
        return Constant.HEADER_AUTH_TYPE + " " + new String(encodedAuth);

    }

    /**
     * Retrieve a filtered list for the given customer and product type
     */
    protected List<T> list(String customerId, String productType) {

        FilterConstraint filterConstraint = new FilterConstraint();
        filterConstraint.setFilterSelection(Constant.FILTER_SELECTION_ACC_HOLDER_KEY);
        filterConstraint.setFilterElement(Constant.FILTER_OPERATOR_EQUALS);
        filterConstraint.setValue(customerId);
        filterConstraint.setDataItemType(productType);

        Search search = new Search();
        search.setFilterConstraints(Arrays.asList(filterConstraint));
        return search(search);
    }

    protected abstract ParameterizedTypeReference<List<T>> getParameter();

}
