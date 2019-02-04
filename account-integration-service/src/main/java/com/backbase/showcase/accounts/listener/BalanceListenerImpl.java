package com.backbase.showcase.accounts.listener;

import com.backbase.buildingblocks.backend.api.utils.ApiUtils;
import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.integration.account.listener.spec.v2.balances.BalanceListener;
import com.backbase.integration.account.rest.spec.v2.balances.BalanceGetResponseBody;
import com.backbase.showcase.accounts.service.ProductService;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of camel route listener interface to get balance data
 */
@Service
@RequestListener
public class BalanceListenerImpl implements BalanceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceListenerImpl.class);

    private ProductService productService;

    @Autowired
    public BalanceListenerImpl(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Consume the Camel route, add products balance data to the return object and copy request proxy wrapper values to
     * it
     *
     * @return RequestProxyWrapper<List<BalanceGetResponseBody>>
     */
    @Override
    public RequestProxyWrapper<List<BalanceGetResponseBody>> getBalance(RequestProxyWrapper<Void> request,
        Exchange exchange, String arrangementIds) throws BadRequestException, InternalServerErrorException {

        InternalRequest<List<BalanceGetResponseBody>> balanceGetResponseBodyInternalRequests = new InternalRequest<>();

        List<BalanceGetResponseBody> balance = productService
            .getProductBalance(Arrays.asList(arrangementIds.split(",")));
        LOGGER.info("Balance found: {}", balance);

        balanceGetResponseBodyInternalRequests.setData(balance);

        RequestProxyWrapper<List<BalanceGetResponseBody>> returnProxyWrapper = new RequestProxyWrapper<>();
        ApiUtils.copyRequestProxyWrapperValues(request, returnProxyWrapper);
        returnProxyWrapper.setRequest(balanceGetResponseBodyInternalRequests);

        return returnProxyWrapper;

    }
}
