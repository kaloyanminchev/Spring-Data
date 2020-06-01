package mostwanted.service.impls;

import com.google.gson.Gson;
import mostwanted.domain.dtos.RacerSeedDto;
import mostwanted.domain.entities.Racer;
import mostwanted.domain.entities.Town;
import mostwanted.repository.RacerRepository;
import mostwanted.service.RacerService;
import mostwanted.service.TownService;
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
public class RacerServiceImpl implements RacerService {

    private final RacerRepository racerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    @Autowired
    public RacerServiceImpl(RacerRepository racerRepository,
                            ModelMapper modelMapper,
                            Gson gson,
                            ValidationUtil validationUtil, TownService townService) {
        this.racerRepository = racerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public Boolean racersAreImported() {
        return this.racerRepository.count() > 0;
    }

    @Override
    public String readRacersJsonFile() throws IOException {
        return Files.readString(Path.of(RACERS_FILE_PATH));
    }

    @Override
    public String importRacers(String racersFileContent) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        RacerSeedDto[] racerSeedDtos =
                this.gson.fromJson(new FileReader(RACERS_FILE_PATH), RacerSeedDto[].class);

        Arrays.stream(racerSeedDtos)
                .forEach(racerSeedDto -> {

                    if (this.validationUtil.isValid(racerSeedDto)) {

                        if (this.racerRepository.findByName(racerSeedDto.getName()) == null) {

                            Racer racer = this.modelMapper.map(racerSeedDto, Racer.class);
                            Town town = this.townService.getTownByName(racerSeedDto.getTownName());

                            racer.setHomeTown(town);

                            sb.append(String.format("Successfully imported Racer - %s.",
                                    racerSeedDto.getName()));

                            this.racerRepository.saveAndFlush(racer);
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
    public String exportRacingCars() {
        return null;
    }

    @Override
    public Racer getRacerByName(String name) {
        return this.racerRepository.findByName(name);
    }
}
