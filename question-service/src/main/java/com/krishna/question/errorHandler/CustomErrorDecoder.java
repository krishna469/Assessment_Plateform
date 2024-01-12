package com.krishna.question.errorHandler;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishna.question.exception.ResourceNotFoundException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;


public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404 || response.status() == 500) {
            String errorMessage = null;
            try {
                errorMessage = extractErrorMessage(response);
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong !! please try again.", e);
            }
            return new ResourceNotFoundException(errorMessage);
        }else {
            return new ResourceNotFoundException("The Quiz service is temporarily unavailable. Please try again later.");
        } 

    }

    private String extractErrorMessage(Response response) throws IOException {
        if (response.body() != null) {
            String responseBody = Util.toString(response.body().asReader());
            return parseErrorMessageFromJson(responseBody);
        }
        
        return "Error occurred !! please try again.";
    }

    private String parseErrorMessageFromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        if (jsonNode.has("message")) {
            return jsonNode.get("message").asText();
        }

        return "Error occurred!! please try again.";
    }


}
