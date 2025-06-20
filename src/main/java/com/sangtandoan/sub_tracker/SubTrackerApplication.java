package com.sangtandoan.sub_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// Add this to make Page return stable construct
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SubTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SubTrackerApplication.class, args);
  }
}
