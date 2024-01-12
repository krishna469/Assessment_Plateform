package com.krishna.report.errorHandler;


import com.krishna.report.exception.ResourceNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {

            return new ResourceNotFoundException("Resource not found with given id.");
        }
        
        if (response.status() == 500) {

            return new ResourceNotFoundException("Something went wrong.");
        }


        // If it's not a 404 error, delegate to default Feign error decoder
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
