package com.example.xmlexdemo.services;

import com.example.xmlexdemo.models.dtos.seeddtos.PartSeedDto;
import com.example.xmlexdemo.models.entities.Part;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Set;

public interface PartService {

    void seedParts() throws JAXBException, FileNotFoundException;

    void save(PartSeedDto partSeedDto);

    Set<Part> getRandomParts();
}
