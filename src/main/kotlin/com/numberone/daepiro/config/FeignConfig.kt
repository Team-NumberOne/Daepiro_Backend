package com.numberone.daepiro.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.numberone.daepiro"])
class FeignConfig
