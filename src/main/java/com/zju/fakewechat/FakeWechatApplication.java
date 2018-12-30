package com.zju.fakewechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableNeo4jRepositories("com.zju.fakewechat.repositories")
public class FakeWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeWechatApplication.class, args);
    }
}