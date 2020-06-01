package softuni.xmllab.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XMLParserImpl implements XMLParser {
    @Override
    public <T> void exportToXML(T object, String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, new File(path));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T importFromXML(Class<T> klass, String path) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(klass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(path));
    }
}
