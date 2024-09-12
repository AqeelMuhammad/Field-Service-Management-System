package com.example.field_service_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FieldServiceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FieldServiceManagementApplication.class, args);
	}

}
