package com.cndmnio.HabitaOps;

import com.cndmnio.HabitaOps.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
public class HabitaOpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitaOpsApplication.class, args);
	}

}
