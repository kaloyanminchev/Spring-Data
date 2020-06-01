package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.CarSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CarServiceImpl(CarRepository carRepository,
                          ModelMapper modelMapper,
                          ValidationUtil validationUtil,
                          Gson gson) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder output = new StringBuilder();

        System.out.println();
        CarSeedDto[] carSeedDtos =
                this.gson.fromJson(new FileReader(CARS_FILE_PATH), CarSeedDto[].class);

        Arrays.stream(carSeedDtos)
                .forEach(carSeedDto -> {

                    if (this.validationUtil.isValid(carSeedDto)) {

                        if (this.getCarByMakeAndModelAndKilometers(carSeedDto.getMake(),
                                carSeedDto.getModel(),
                                carSeedDto.getKilometers()) == null) {

                            Car car = this.modelMapper.map(carSeedDto, Car.class);

                            LocalDate localDate =
                                    LocalDate.parse(carSeedDto.getRegisteredOn(),
                                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                            car.setRegisteredOn(localDate);

                            output.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "car",
                                    String.format("%s - %s", car.getMake(), car.getModel())));

                            this.carRepository.saveAndFlush(car);

                        } else {
                            output.append(IN_DB_MESSAGE);
                        }

                    } else {
                        output.append((INCORRECT_DATA_MESSAGE + "car"));
                    }
                    output.append(System.lineSeparator());
                });

        return output.toString().trim();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder output = new StringBuilder();

        this.carRepository.findAllOrderByPicturesCountDescOrderByMakeAsc()
//                .findAll()
//                .stream()
//                .sorted((c1, c2) -> {
//                    int sort = c2.getPictures().size() - c1.getPictures().size();
//                    if (sort == 0) {
//                        sort = c1.getMake().compareTo(c2.getMake());
//                    }
//                    return sort;
//                })
                .forEach(car -> {
                    output.append(String.format("Car make - %s, model - %s\n" +
                                    "\tKilometers - %d\n" +
                                    "\tRegistered on - %s\n" +
                                    "\tNumber of pictures - %d\n\n",
                            car.getMake(), car.getModel(),
                            car.getKilometers(),
                            car.getRegisteredOn(),
                            car.getPictures().size()));
                });

        return output.toString().trim();
    }

    @Override
    public Car getCarByMakeAndModelAndKilometers(String make, String model, Integer kilometers) {
        return this.carRepository.findByMakeAndModelAndKilometers(make, model, kilometers);
    }

    @Override
    public Car getCarById(Long id) {
        return this.carRepository.findById(id).orElse(null);
    }
}
