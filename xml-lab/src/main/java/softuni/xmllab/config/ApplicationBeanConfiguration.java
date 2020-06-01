package softuni.xmllab.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.xmllab.utils.XMLParser;
import softuni.xmllab.utils.XMLParserImpl;

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
}
