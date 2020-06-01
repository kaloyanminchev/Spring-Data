package mostwanted.service.impls;

import mostwanted.domain.dtos.RaceEntrySeedRootDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Race;
import mostwanted.domain.entities.RaceEntry;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.RaceEntryRepository;
import mostwanted.service.CarService;
import mostwanted.service.RaceEntryService;
import mostwanted.service.RaceService;
import mostwanted.service.RacerService;
import mostwanted.util.ValidationUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static mostwanted.common.Constants.*;

@Service
@Transactional
public class RaceEntryServiceImpl implements RaceEntryService {
    private final RaceEntryRepository raceEntryRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final CarService carService;
    private final RacerService racerService;
    private final RaceService raceService;

    @Autowired
    public RaceEntryServiceImpl(RaceEntryRepository raceEntryRepository,
                                ModelMapper modelMapper,
                                XmlParser xmlParser,
                                ValidationUtil validationUtil,
                                CarService carService,
                                RacerService racerService,
                                RaceService raceService) {
        this.raceEntryRepository = raceEntryRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.carService = carService;
        this.racerService = racerService;
        this.raceService = raceService;
    }

    @Override
    public Boolean raceEntriesAreImported() {
        return this.raceEntryRepository.count() > 0;
    }

    @Override
    public String readRaceEntriesXmlFile() throws IOException {
        return Files.readString(Path.of(ENTRIES_FILE_PATH));
    }

    @Override
    public String importRaceEntries() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        RaceEntrySeedRootDto raceEntrySeedRootDto =
                this.xmlParser.parseXml(RaceEntrySeedRootDto.class, ENTRIES_FILE_PATH);

        raceEntrySeedRootDto.getEntries()
                .forEach(raceEntrySeedDto -> {

                    if (this.validationUtil.isValid(raceEntrySeedDto)) {

                        if (this.raceEntryRepository.findByCar(
                                this.carService.getCarById(raceEntrySeedDto.getCarId())) == null) {

                            RaceEntry raceEntry =
                                    this.modelMapper.map(raceEntrySeedDto, RaceEntry.class);

                            Car car = this.carService
                                    .getCarById(raceEntrySeedDto.getCarId());

                            Racer racer = this.racerService
                                    .getRacerByName(raceEntrySeedDto.getRacer());

                            if (car != null && racer != null) {
                                raceEntry.setCar(car);
                                raceEntry.setRacer(racer);

                                this.raceEntryRepository.saveAndFlush(raceEntry);

                                sb.append(String.format("Successfully imported RaceEntry - %d.",
                                        raceEntry.getId()));
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
