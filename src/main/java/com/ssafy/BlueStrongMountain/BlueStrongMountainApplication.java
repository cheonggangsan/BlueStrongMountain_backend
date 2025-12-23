package com.ssafy.BlueStrongMountain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
		org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration.class
})
@EnableScheduling
public class BlueStrongMountainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueStrongMountainApplication.class, args);
	}

}
