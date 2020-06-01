package mostwanted.service.impls;

import com.google.gson.Gson;
import mostwanted.domain.dtos.DistrictSeedDto;
import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Town;
import mostwanted.repository.DistrictRepository;
import mostwanted.service.DistrictService;
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
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository,
                               ModelMapper modelMapper,
                               Gson gson,
                               ValidationUtil validationUtil, TownService townService) {
        this.districtRepository = districtRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public Boolean districtsAreImported() {
        return this.districtRepository.count() > 0;
    }

    @Override
    public String readDistrictsJsonFile() throws IOException {
        return Files.readString(Path.of(DISTRICTS_FILE_PATH));
    }

    @Override
    public String importDistricts(String districtsFileContent) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        DistrictSeedDto[] districtSeedDtos =
                this.gson.fromJson(new FileReader(DISTRICTS_FILE_PATH), DistrictSeedDto[].class);

        Arrays.stream(districtSeedDtos)
                .forEach(districtSeedDto -> {

                    if (this.validationUtil.isValid(districtSeedDto)) {

                        if (this.districtRepository.findByName(districtSeedDto.getName()) == null) {

                            District district =
                                    this.modelMapper.map(districtSeedDto, District.class);

                            Town town = this.townService.getTownByName(districtSeedDto.getTownName());
                            if (town != null) {
                                district.setTown(town);
                                sb.append(String.format("Successfully imported District - %s.",
                                        districtSeedDto.getName()));
                                this.districtRepository.saveAndFlush(district);
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
}
