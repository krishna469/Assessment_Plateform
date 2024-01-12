package com.krishna.quiz;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class QuizServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizServiceApplication.class, args);
	}

	 /**
     * Creates and returns an instance of ModelMapper, which is used for object
     * mapping.
     *
     * @return An instance of ModelMapper.
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
