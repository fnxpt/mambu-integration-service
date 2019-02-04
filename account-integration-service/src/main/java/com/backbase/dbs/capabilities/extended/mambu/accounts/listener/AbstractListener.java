package com.backbase.dbs.capabilities.extended.mambu.accounts.listener;

import com.backbase.buildingblocks.backend.api.utils.ApiUtils;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.buildingblocks.backend.internalrequest.InternalRequest;

public abstract class AbstractListener {

    protected <T> RequestProxyWrapper<T> buildResponse(RequestProxyWrapper<Void> requestProxyWrapper, T data) {
        InternalRequest<T> request = new InternalRequest<>();
        request.setData(data);

        RequestProxyWrapper<T> returnProxyWrapper = new RequestProxyWrapper<>();
        ApiUtils.copyRequestProxyWrapperValues(requestProxyWrapper, returnProxyWrapper);
        returnProxyWrapper.setRequest(request);

        return returnProxyWrapper;
    }
}
