package com.example.xmlexdemo.services.impls;

import com.example.xmlexdemo.models.dtos.seeddtos.PartSeedDto;
import com.example.xmlexdemo.models.dtos.seeddtos.PartSeedRootDto;
import com.example.xmlexdemo.models.entities.Part;
import com.example.xmlexdemo.repositories.PartRepository;
import com.example.xmlexdemo.services.PartService;
import com.example.xmlexdemo.services.SupplierService;
import com.example.xmlexdemo.utils.ValidationUtil;
import com.example.xmlexdemo.utils.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.example.xmlexdemo.constants.GlobalConstants.PARTS_FILE_PATH;

@Service
@Transactional
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XMLParser xmlParser;
    private final SupplierService supplierService;
    private final Random random;

    @Autowired
    public PartServiceImpl(PartRepository partRepository,
                           ModelMapper modelMapper,
                           ValidationUtil validationUtil,
                           XMLParser xmlParser,
                           SupplierService supplierService,
                           Random random) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.supplierService = supplierService;
        this.random = random;
    }

    @Override
    public void seedParts() throws JAXBException, FileNotFoundException {
        PartSeedRootDto partSeedRootDto =
                this.xmlParser.unmarshalFromXML(PARTS_FILE_PATH, PartSeedRootDto.class);

        partSeedRootDto.getParts()
                .forEach(partSeedDto -> {
                    if (this.validationUtil.isValid(partSeedDto)) {
                        this.save(partSeedDto);
                    } else {
                        this.validationUtil
                                .violations(partSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public void save(PartSeedDto partSeedDto) {
        Part part = this.modelMapper.map(partSeedDto, Part.class);
        part.setSupplier(this.supplierService.getRandomSupplier());
        this.partRepository.saveAndFlush(part);
    }

    @Override
    public Set<Part> getRandomParts() {
        Set<Part> resultSet = new HashSet<>();
        int randomCounter = this.random.nextInt(10) + 10;

        for (int i = 0; i < randomCounter; i++) {
            long randomId = this.random.nextInt((int) this.partRepository.count()) + 1;

            resultSet.add(this.partRepository.getOne(randomId));
        }

        return resultSet;
    }
}
