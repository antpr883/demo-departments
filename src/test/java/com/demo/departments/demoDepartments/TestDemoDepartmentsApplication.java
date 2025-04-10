package com.demo.departments.demoDepartments;

import org.springframework.boot.SpringApplication;

public class TestDemoDepartmentsApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoDepartmentsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
