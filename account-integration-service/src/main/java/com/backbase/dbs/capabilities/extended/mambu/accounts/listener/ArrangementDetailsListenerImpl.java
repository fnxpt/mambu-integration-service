package com.backbase.dbs.capabilities.extended.mambu.accounts.listener;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.dbs.capabilities.extended.mambu.accounts.service.ProductService;
import com.backbase.integration.account.listener.spec.v2.arrangementdetails.ArrangementDetailsListener;
import com.backbase.integration.account.rest.spec.v2.arrangementdetails.ArrangementDetailsGetResponseBody;
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
public class ArrangementDetailsListenerImpl extends AbstractListener implements ArrangementDetailsListener {

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

        ArrangementDetailsGetResponseBody arrangementDetail = productService.getProductDetail(accountId);
        LOGGER.info("Product Detail: {}", arrangementDetail);

        return buildResponse(requestProxyWrapper, arrangementDetail);

    }


}
