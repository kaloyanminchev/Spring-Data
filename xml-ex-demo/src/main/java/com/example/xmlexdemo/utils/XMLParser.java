package com.example.xmlexdemo.utils;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XMLParser {

    <T> T unmarshalFromXML(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException;

    <T> void marshalToXML(T rootDto, String path) throws JAXBException;
}
