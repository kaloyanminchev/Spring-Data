package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PictureSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static softuni.exam.constants.GlobalConstants.*;

@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CarService carService;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository,
                              ModelMapper modelMapper,
                              ValidationUtil validationUtil,
                              Gson gson,
                              CarService carService) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder output = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos =
                this.gson.fromJson(new FileReader(PICTURES_FILE_PATH), PictureSeedDto[].class);

        Arrays.stream(pictureSeedDtos)
                .forEach(pictureSeedDto -> {

                    if (this.validationUtil.isValid(pictureSeedDto)) {

                        if (this.getPictureByName(pictureSeedDto.getName()) == null) {
                            Picture picture =
                                    this.modelMapper.map(pictureSeedDto, Picture.class);

                            LocalDateTime localDateTime =
                                    LocalDateTime.parse(pictureSeedDto.getDateAndTime(),
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                            Car car = this.carService.getCarById(pictureSeedDto.getCar());

                            picture.setDateAndTime(localDateTime);
                            picture.setCar(car);

                            output.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "picture",
                                    picture.getName()));

                            this.pictureRepository.saveAndFlush(picture);
                        } else {
                            output.append(IN_DB_MESSAGE);
                        }
                    } else {
                        output.append(INCORRECT_DATA_MESSAGE + "picture");
                    }
                    output.append(System.lineSeparator());
                });

        return output.toString().trim();
    }

    @Override
    public Picture getPictureByName(String name) {
        return this.pictureRepository.findByName(name);
    }

    @Override
    public Set<Picture> getAllByCarId(Long id) {

        return this.pictureRepository.findAllByCar(this.carService.getCarById(id));
    }
}
