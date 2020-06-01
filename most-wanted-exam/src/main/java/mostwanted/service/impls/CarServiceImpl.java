package mostwanted.service.impls;

import com.google.gson.Gson;
import mostwanted.domain.dtos.CarSeedDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.CarRepository;
import mostwanted.service.CarService;
import mostwanted.service.RacerService;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static mostwanted.common.Constants.*;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final RacerService racerService;

    @Autowired
    public CarServiceImpl(CarRepository carRepository,
                          ModelMapper modelMapper,
                          Gson gson,
                          ValidationUtil validationUtil, RacerService racerService) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.racerService = racerService;
    }

    @Override
    public Boolean carsAreImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsJsonFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars(String carsFileContent) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        CarSeedDto[] carSeedDtos =
                this.gson.fromJson(new FileReader(CARS_FILE_PATH), CarSeedDto[].class);

        Arrays.stream(carSeedDtos)
                .forEach(carSeedDto -> {

                    if (this.validationUtil.isValid(carSeedDto)) {

                        if (this.carRepository
                                .findByBrandAndModelAndYearOfProduction(
                                        carSeedDto.getBrand(),
                                        carSeedDto.getModel(),
                                        carSeedDto.getYearOfProduction()
                                ) == null) {

                            Car car = this.modelMapper.map(carSeedDto, Car.class);
                            Racer racer =
                                    this.racerService.getRacerByName(carSeedDto.getRacerName());

                            if (racer != null) {
                                car.setRacer(racer);

                                sb.append(String.format("Successfully imported Car - %s %s @ %d.",
                                        carSeedDto.getBrand(),
                                        carSeedDto.getModel(),
                                        carSeedDto.getYearOfProduction()));

                                this.carRepository.saveAndFlush(car);
                            } else {
                                sb.append(INCORRECT_DATA_MESSAGE);
                            }


                        } else {
                            sb.append(DUPLICATE_DATA_MESSAGE);
                        }

                    } else {
                        sb.append(INCORRECT_DATA_MESSAGE);
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString().trim();
    }

    @Override
    public Car getCarById(Long id) {
        return this.carRepository.findById(id).orElse(null);
    }
}
