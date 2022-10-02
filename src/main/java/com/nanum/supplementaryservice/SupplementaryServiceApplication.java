package com.nanum.supplementaryservice;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@SpringBootApplication(scanBasePackages = "com.nanum")
//@EnableDis
@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
public class SupplementaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplementaryServiceApplication.class, args);
    }

    // 다국어 처리
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }
//231
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
//    @Bean
//    public FeignErrorDecoder getFeignErrorDecoder(){
//        return new FeignErrorDecoder();
//    }

}
