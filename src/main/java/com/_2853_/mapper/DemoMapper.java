package com._2853_.mapper;

import com._2853_.bean.Demo;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoMapper {
	
	int add(Demo demo);

}
