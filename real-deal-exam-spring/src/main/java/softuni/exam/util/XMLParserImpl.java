package softuni.exam.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Component
public class XMLParserImpl implements XMLParser {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unmarshalFromXML(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(filePath));
    }

    @Override
    public <T> void marshalToXML(T rootDto, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(rootDto.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(rootDto, new File(filePath));
    }
}
