package com.titans.ahs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomatedHealthcareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatedHealthcareSystemApplication.class, args);
	}


	/*@Bean
	public ObjectMapper objectMapper(){
		//return new ObjectMapper().registerModule(new JavaTimeModule());
		//return new ObjectMapper().registerModule(new Jdk8Module());
		return new ObjectMapper().registerModule(new SimpleModule()
				.addSerializer(LocalDate.class, new CustomLocalDateSerializer())
						.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer())
				.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer())
				.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer()
				));
	}

	 */
}
