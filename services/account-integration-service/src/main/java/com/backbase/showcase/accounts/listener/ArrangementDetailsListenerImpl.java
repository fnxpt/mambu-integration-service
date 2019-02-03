package com.backbase.showcase.accounts.listener;

import com.backbase.buildingblocks.backend.api.utils.ApiUtils;
import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.integration.account.listener.spec.v2.arrangementdetails.ArrangementDetailsListener;
import com.backbase.integration.account.rest.spec.v2.arrangementdetails.ArrangementDetailsGetResponseBody;
import com.backbase.showcase.accounts.service.ProductService;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of camel route listener interface to get arrangements details
 */
@Service
@RequestListener
public class ArrangementDetailsListenerImpl implements ArrangementDetailsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrangementDetailsListenerImpl.class);

    private ProductService productService;

    @Autowired
    public ArrangementDetailsListenerImpl(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Consume the Camel route, add arrangement details data to the return object and copy request proxy wrapper values
     * to it
     *
     * @return RequestProxyWrapper<ArrangementDetailsGetResponseBody>
     */
    @Override
    public RequestProxyWrapper<ArrangementDetailsGetResponseBody> getArrangementDetails(
        RequestProxyWrapper<Void> requestProxyWrapper, Exchange exchange, String accountId)
        throws BadRequestException, InternalServerErrorException {

        InternalRequest<ArrangementDetailsGetResponseBody> balanceGetResponseBodyInternalRequests = new InternalRequest<>();

        ArrangementDetailsGetResponseBody arrangementDetail = productService.getProductDetail(accountId);
        LOGGER.info("Product Detail: {}", arrangementDetail);

        balanceGetResponseBodyInternalRequests.setData(arrangementDetail);

        RequestProxyWrapper<ArrangementDetailsGetResponseBody> returnProxyWrapper = new RequestProxyWrapper<>();
        ApiUtils.copyRequestProxyWrapperValues(requestProxyWrapper, returnProxyWrapper);
        returnProxyWrapper.setRequest(balanceGetResponseBodyInternalRequests);

        return returnProxyWrapper;

    }

}
