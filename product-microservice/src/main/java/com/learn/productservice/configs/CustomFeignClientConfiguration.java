package com.learn.productservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.productservice.dtos.GenericResponseDto;
import com.learn.productservice.exceptions.CustomFeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class CustomFeignClientConfiguration {

    /**
     * Provides a custom {@link ErrorDecoder} bean for Feign clients.
     *
     * @return a {@link ErrorDecoder} instance
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private static class CustomErrorDecoder implements ErrorDecoder {

        @Override
        @SuppressWarnings("unchecked")
        public Exception decode(String methodKey, Response response) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InputStream is = response.body().asInputStream();
                GenericResponseDto<Void> genericResponseDto = objectMapper.readValue(is,
                        GenericResponseDto.class);
                return new CustomFeignException(genericResponseDto.getMessage(),
                        String.valueOf(response.status()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
