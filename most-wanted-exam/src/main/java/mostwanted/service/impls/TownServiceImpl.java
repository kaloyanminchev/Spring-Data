package mostwanted.service.impls;

import com.google.gson.Gson;
import mostwanted.common.Constants;
import mostwanted.domain.dtos.TownSeedDto;
import mostwanted.domain.entities.Town;
import mostwanted.repository.TownRepository;
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
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns(String townsFileContent) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        TownSeedDto[] townSeedDtos =
                this.gson.fromJson(new FileReader(TOWNS_FILE_PATH), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .forEach(townSeedDto -> {

                    if (this.validationUtil.isValid(townSeedDto)) {

                        if (this.townRepository.findByName(townSeedDto.getName()) == null) {
                            Town town = this.modelMapper.map(townSeedDto, Town.class);

                            sb.append(String.format("Successfully imported Town - %s.",
                                    townSeedDto.getName()));
                            this.townRepository.saveAndFlush(town);
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
    public String exportRacingTowns() {
        return null;
    }

    @Override
    public Town getTownByName(String name) {
        return this.townRepository.findByName(name);
    }
}
