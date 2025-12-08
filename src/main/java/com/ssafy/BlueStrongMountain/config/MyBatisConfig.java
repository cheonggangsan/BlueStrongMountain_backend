package com.ssafy.BlueStrongMountain.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ssafy.BlueStrongMountain.repository.mapper")
public class MyBatisConfig {
}
