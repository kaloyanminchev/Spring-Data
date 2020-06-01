package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.constants.GlobalConstants.IN_DB_MESSAGE;
import static softuni.exam.constants.GlobalConstants.PASSENGERS_FILE_PATH;

@Service
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository,
                                ModelMapper modelMapper,
                                Gson gson,
                                ValidationUtil validationUtil,
                                TownService townService) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PassengerSeedDto[] passengerSeedDtos =
                this.gson.fromJson(new FileReader(PASSENGERS_FILE_PATH), PassengerSeedDto[].class);

        Arrays.stream(passengerSeedDtos)
                .forEach(passengerSeedDto -> {

                    if (this.validationUtil.isValid(passengerSeedDto)) {

                        if (this.passengerRepository
                                .findByEmail(passengerSeedDto.getEmail()) == null) {

                            Passenger passenger =
                                    this.modelMapper.map(passengerSeedDto, Passenger.class);

                            Town town = this.townService.getTownByName(passengerSeedDto.getTown());

                            passenger.setTown(town);

                            sb.append(String.format("Successfully imported Passenger %s - %s",
                                    passengerSeedDto.getLastName(),
                                    passengerSeedDto.getEmail()));

                            this.passengerRepository.saveAndFlush(passenger);
                        } else {
                            sb.append(IN_DB_MESSAGE);
                        }

                    } else {
                        sb.append("Invalid Passenger");
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder output = new StringBuilder();

        this.passengerRepository
                .findAllOrderByTicketsCountDescThenByEmail()
                .forEach(p -> output.append(String.format("Passenger %s %s\n" +
                                "\tEmail - %s\n" +
                                "\tPhone - %s\n" +
                                "\tNumber of tickets - %d\n\n",
                        p.getFirstName(),
                        p.getLastName(),
                        p.getEmail(),
                        p.getPhoneNumber(),
                        p.getTickets().size())));

        return output.toString();
    }

    @Override
    public Passenger getPassengerByEmail(String email) {
        return this.passengerRepository.findByEmail(email);
    }
}
