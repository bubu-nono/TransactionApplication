package com._2853_;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

@SpringBootApplication(scanBasePackages={"com._2853_"})
@MapperScan(basePackages="com._2853_.mapper",annotationClass= Repository.class)
public class App {

	public static void main(String[] args) {
		//启动运行
		SpringApplication.run(App.class, args);
	}

}
