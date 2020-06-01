package com.example.xmlexdemo.services.impls;

import com.example.xmlexdemo.constants.GlobalConstants;
import com.example.xmlexdemo.models.dtos.seeddtos.CarSeedDto;
import com.example.xmlexdemo.models.dtos.seeddtos.CarSeedRootDto;
import com.example.xmlexdemo.models.dtos.viewdtos.*;
import com.example.xmlexdemo.models.entities.Car;
import com.example.xmlexdemo.models.entities.Part;
import com.example.xmlexdemo.repositories.CarRepository;
import com.example.xmlexdemo.services.CarService;
import com.example.xmlexdemo.services.PartService;
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

import static com.example.xmlexdemo.constants.GlobalConstants.*;

@Service
@Transactional
public class CarServiceImpl implements CarService {
    public static final String MAKE = "Toyota";

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final PartService partService;
    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public CarServiceImpl(CarRepository carRepository,
                          ModelMapper modelMapper,
                          XMLParser xmlParser,
                          PartService partService,
                          ValidationUtil validationUtil,
                          Random random) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.partService = partService;
        this.validationUtil = validationUtil;
        this.random = random;
    }


    @Override
    public void seedCars() throws JAXBException, FileNotFoundException {
        CarSeedRootDto carSeedRootDto =
                this.xmlParser.unmarshalFromXML(CARS_FILE_PATH, CarSeedRootDto.class);

        carSeedRootDto.getCars()
                .forEach(carSeedDto -> {
                    if (this.carRepository.findByMakeAndModelAndTravelledDistance(
                            carSeedDto.getMake(),
                            carSeedDto.getModel(),
                            carSeedDto.getTravelledDistance()) != null) {
                        System.out.printf("%s %s with travelled distance %d already exists in database!%n",
                                carSeedDto.getMake(),
                                carSeedDto.getModel(),
                                carSeedDto.getTravelledDistance());
                        return;
                    }

                    if (this.validationUtil.isValid(carSeedDto)) {
                        this.save(carSeedDto);
                    } else {
                        this.validationUtil.violations(carSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });

//        for (CarSeedDto carSeedDto : carSeedRootDto.getCars()) {
//
//            if (this.validationUtil.isValid(carSeedDto)) {
//                if (this.carRepository.findByMakeAndModelAndTravelledDistance(
//                        carSeedDto.getMake(),
//                        carSeedDto.getModel(),
//                        carSeedDto.getTravelledDistance()) != null) {
//                    System.out.printf("%s %s with travelled distance %d already exists in database!%n",
//                            carSeedDto.getMake(),
//                            carSeedDto.getModel(),
//                            carSeedDto.getTravelledDistance());
//                } else {
//                    this.save(carSeedDto);
//                }
//            } else {
//                this.validationUtil
//                        .violations(carSeedDto)
//                        .stream()
//                        .map(ConstraintViolation::getMessage)
//                        .forEach(System.out::println);
//            }
//        }
    }

    @Override
    public void save(CarSeedDto carSeedDto) {
        Car car = this.modelMapper.map(carSeedDto, Car.class);
        car.setParts(this.partService.getRandomParts());
        this.carRepository.saveAndFlush(car);
    }

    @Override
    public Car getRandomCar() {
        long randomId = this.random.nextInt((int) this.carRepository.count()) + 1;
        return this.carRepository.getOne(randomId);
    }

    @Override
    public void visualizeCarsByMake() throws JAXBException {
        CarViewRootDto carViewRootDto = new CarViewRootDto();

        List<CarViewDto> carViewDtos = this.carRepository
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(MAKE)
                .stream()
                .map(car -> this.modelMapper.map(car, CarViewDto.class))
                .collect(Collectors.toList());

        carViewRootDto.setCars(carViewDtos);
        this.xmlParser.marshalToXML(carViewRootDto, QUERY_2_FILE_PATH);
    }

    @Override
    public void visualizeAllCarsWithTheirParts() throws JAXBException {
        CarPartsViewRootDto carPartsViewRootDto = new CarPartsViewRootDto();

        List<CarPartsViewDto> carPartsViewDtos = this.carRepository
                .findAll()
                .stream()
                .map(car -> {
                    CarPartsViewDto carPartsViewDto = new CarPartsViewDto();
                    this.modelMapper.map(car, carPartsViewDto);

                    PartViewRootDto partViewRootDto = new PartViewRootDto();
                    List<PartViewDto> partViewDtos = car
                            .getParts()
                            .stream()
                            .map(part -> this.modelMapper.map(part, PartViewDto.class))
                            .collect(Collectors.toList());

                    partViewRootDto.setParts(partViewDtos);

                    carPartsViewDto.setPartViewRootDto(partViewRootDto);

                    return carPartsViewDto;
                }).collect(Collectors.toList());

        carPartsViewRootDto.setCars(carPartsViewDtos);

        this.xmlParser.marshalToXML(carPartsViewRootDto, QUERY_4_FILE_PATH);
    }
}
