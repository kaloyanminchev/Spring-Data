package com.jsonexdemo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsonexdemo.utils.FileIOUtil;
import com.jsonexdemo.utils.FileIOUtilImpl;
import com.jsonexdemo.utils.ValidationUtil;
import com.jsonexdemo.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public FileIOUtil fileIOUtil() {
        return new FileIOUtilImpl();
    }
}
