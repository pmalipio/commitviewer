package com.pmalipio.commitviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages={"com.pmalipio"})
@SpringBootApplication
public class CommitviewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommitviewerApplication.class, args);
    }

}
