package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.util.ValidatorUtil;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static softuni.exam.constants.GlobalConstants.PLAYERS_FILE_PATH;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final PictureService pictureService;
    private final TeamService teamService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             ModelMapper modelMapper,
                             Gson gson,
                             ValidatorUtil validatorUtil,
                             PictureService pictureService,
                             TeamService teamService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.pictureService = pictureService;
        this.teamService = teamService;
    }

    @Override
    public String importPlayers() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos =
                this.gson.fromJson(new FileReader(PLAYERS_FILE_PATH), PlayerSeedDto[].class);

        Arrays.stream(playerSeedDtos)
                .forEach(playerSeedDto -> {

                    if (this.validatorUtil.isValid(playerSeedDto)) {

                        if (this.playerRepository
                                .findByFirstNameAndLastNameAndNumber(
                                        playerSeedDto.getFirstName(),
                                        playerSeedDto.getLastName(),
                                        playerSeedDto.getNumber()
                                ) == null) {

                            Player player = this.modelMapper.map(playerSeedDto, Player.class);

                            Picture picture = this.pictureService
                                    .getPictureByUrl(playerSeedDto.getPicture().getUrl());

                            Team team = this.teamService
                                    .getTeamByName(playerSeedDto.getTeam().getName());

                            if (picture != null && team != null) {
                                player.setPicture(picture);
                                player.setTeam(team);

                                sb.append(String.format("Successfully imported player: %s %s",
                                        playerSeedDto.getFirstName(),
                                        playerSeedDto.getLastName()));

                                this.playerRepository.saveAndFlush(player);
                            } else {
                                sb.append("Invalid player");
                            }

                        } else {
                            sb.append("Already in DB!");
                        }

                    } else {
                        sb.append("Invalid player");
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString().trim();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Paths.get(PLAYERS_FILE_PATH));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();

        this.playerRepository
                .findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000))
                .forEach(p -> {
                    sb.append(String.format("Player name: %s %s\n" +
                                    "      Number: %d\n" +
                                    "      Salary: %s\n" +
                                    "      Team: %s\n",
                            p.getFirstName(),
                            p.getLastName(),
                            p.getNumber(),
                            p.getSalary(),
                            p.getTeam().getName()));
                });

        return sb.toString().trim();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();

        sb.append("Team: North Hub").append(System.lineSeparator());

        this.playerRepository
                .findAllByTeamNameOrderById("North Hub")
                .forEach(player -> {
                    sb.append(String.format("      Player name: %s %s - %s\n" +
                                    "      Number: %d\n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getPosition(),
                            player.getNumber()));
                });

        return sb.toString().trim();
    }
}
