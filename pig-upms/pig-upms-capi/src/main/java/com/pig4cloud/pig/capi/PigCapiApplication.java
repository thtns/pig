package com.pig4cloud.pig.capi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PigCapiApplication{

	public static void main(String[] args) {
		SpringApplication.run(PigCapiApplication.class, args);
	}

}
