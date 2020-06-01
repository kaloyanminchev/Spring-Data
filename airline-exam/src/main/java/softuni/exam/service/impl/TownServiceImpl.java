package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TownSeedDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.constants.GlobalConstants.IN_DB_MESSAGE;
import static softuni.exam.constants.GlobalConstants.TOWNS_FILE_PATH;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(TownRepository townRepository,
                           ModelMapper modelMapper,
                           Gson gson,
                           ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        TownSeedDto[] townSeedDtos =
                this.gson.fromJson(new FileReader(TOWNS_FILE_PATH), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .forEach(townSeedDto -> {

                    if (this.validationUtil.isValid(townSeedDto)) {

                        if (this.townRepository.findByName(townSeedDto.getName()) == null) {

                            Town town = this.modelMapper.map(townSeedDto, Town.class);

                            sb.append(String.format("Successfully imported Town %s - %d",
                                    townSeedDto.getName(),
                                    townSeedDto.getPopulation()));

                            this.townRepository.saveAndFlush(town);

                        } else {
                            sb.append(IN_DB_MESSAGE);
                        }

                    } else {
                        sb.append("Invalid Town");
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public Town getTownByName(String name) {
        return this.townRepository.findByName(name);
    }
}
