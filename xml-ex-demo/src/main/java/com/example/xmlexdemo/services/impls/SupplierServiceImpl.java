package com.example.xmlexdemo.services.impls;

import com.example.xmlexdemo.models.dtos.seeddtos.SupplierSeedDto;
import com.example.xmlexdemo.models.dtos.seeddtos.SupplierSeedRootDto;
import com.example.xmlexdemo.models.dtos.viewdtos.SupplierViewDto;
import com.example.xmlexdemo.models.dtos.viewdtos.SupplierViewRootDto;
import com.example.xmlexdemo.models.entities.Supplier;
import com.example.xmlexdemo.repositories.SupplierRepository;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.xmlexdemo.constants.GlobalConstants.QUERY_3_FILE_PATH;
import static com.example.xmlexdemo.constants.GlobalConstants.SUPPLIER_FILE_PATH;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               ModelMapper modelMapper,
                               XMLParser xmlParser,
                               ValidationUtil validationUtil,
                               Random random) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedSuppliers() throws JAXBException, FileNotFoundException {
        SupplierSeedRootDto supplierSeedRootDto =
                this.xmlParser.unmarshalFromXML(SUPPLIER_FILE_PATH, SupplierSeedRootDto.class);

        supplierSeedRootDto.getSuppliers()
                .forEach(supplierSeedDto -> {
                    if (this.supplierRepository.findByName(supplierSeedDto.getName()) != null) {
                        System.out.println(supplierSeedDto.getName() + " already exists in database!");
                        return;
                    }

                    if (this.validationUtil.isValid(supplierSeedDto)) {
                        this.save(supplierSeedDto);
                    } else {
                        this.validationUtil
                                .violations(supplierSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });

//        for (SupplierSeedDto supplierSeedDto : supplierSeedRootDto.getSuppliers()) {
//
//            if (this.validationUtil.isValid(supplierSeedDto)) {
//                if (this.supplierRepository.findByName(supplierSeedDto.getName()) != null) {
//                    System.out.println(supplierSeedDto.getName() + " already exists in database!");
//                } else {
//                    this.save(supplierSeedDto);
//                }
//            } else {
//                this.validationUtil
//                        .violations(supplierSeedDto)
//                        .stream()
//                        .map(ConstraintViolation::getMessage)
//                        .forEach(System.out::println);
//            }
//        }
    }

    @Override
    public void save(SupplierSeedDto supplierSeedDto) {
        this.supplierRepository
                .saveAndFlush(this.modelMapper.map(supplierSeedDto, Supplier.class));
    }

    @Override
    public Supplier getRandomSupplier() {
        long randomId = this.random.nextInt((int) this.supplierRepository.count()) + 1;
        return this.supplierRepository.getOne(randomId);
    }

    @Override
    public void visualizeSuppliersNotImporter() throws JAXBException {
        SupplierViewRootDto supplierViewRootDto = new SupplierViewRootDto();

        List<SupplierViewDto> supplierViewDtos =
                this.supplierRepository
                        .findAllByImporterIsFalse()
                        .stream()
                        .map(supplier -> {
                            SupplierViewDto supplierViewDto =
                                    this.modelMapper.map(supplier, SupplierViewDto.class);
                            supplierViewDto.setPartsCount(supplier.getParts().size());
                            return supplierViewDto;
                        })
                        .collect(Collectors.toList());

        supplierViewRootDto.setSuppliers(supplierViewDtos);
        this.xmlParser.marshalToXML(supplierViewRootDto, QUERY_3_FILE_PATH);
    }
}
