package com.sparta.msa.project_part_3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableRetry
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class ProjectPart3Application {

  public static void main(String[] args) {
    SpringApplication.run(ProjectPart3Application.class, args);
  }

}
