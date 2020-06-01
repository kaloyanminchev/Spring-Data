package com.example.xmlexdemo.config;

import com.example.xmlexdemo.utils.ValidationUtil;
import com.example.xmlexdemo.utils.ValidationUtilImpl;
import com.example.xmlexdemo.utils.XMLParser;
import com.example.xmlexdemo.utils.XMLParserImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public XMLParser xmlParser() {
        return new XMLParserImpl();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
