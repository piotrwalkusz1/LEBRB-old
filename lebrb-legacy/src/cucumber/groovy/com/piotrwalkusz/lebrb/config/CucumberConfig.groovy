package com.piotrwalkusz.lebrb.config

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CucumberConfig {

    @Bean
    WebDriver webDriver() {
        new FirefoxDriver()
    }
}
