package com.titans.ahs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.titans.ahs.formatter.CustomLocalDateDeserializer;
import com.titans.ahs.formatter.CustomLocalDateSerializer;
import com.titans.ahs.formatter.CustomLocalDateTimeDeserializer;
import com.titans.ahs.formatter.CustomLocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class AutomatedHealthcareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatedHealthcareSystemApplication.class, args);
	}


	@Bean
	public ObjectMapper objectMapper(){
		//return new ObjectMapper().registerModule(new JavaTimeModule());
		return new ObjectMapper().registerModule(new SimpleModule()
				.addSerializer(LocalDate.class, new CustomLocalDateSerializer())
						.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer())
				.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer())
				.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer()
				));
	}
}
