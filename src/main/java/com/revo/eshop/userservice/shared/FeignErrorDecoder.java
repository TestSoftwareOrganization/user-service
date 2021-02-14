package com.revo.eshop.userservice.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    Environment environment;

    @Autowired
    public FeignErrorDecoder(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                // do something
                // return new BadRequestException();
                break;
            case 404:
                // the name of the method in Feign Client getOrdersBy...
                if (methodKey.contains("getOrders")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), environment.getProperty("orders.exceptions.orders-not-found"));
                }
                break;
            case 500:
                if (methodKey.contains("getOrders")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "500 error !!!!!");
                }
            default:
                return new Exception(response.reason());
        }

        return null;
    }
}
