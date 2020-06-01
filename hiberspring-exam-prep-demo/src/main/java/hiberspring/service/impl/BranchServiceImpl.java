package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.GlobalConstants.*;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository,
                             ModelMapper modelMapper,
                             Gson gson,
                             ValidationUtil validationUtil,
                             TownService townService) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files.readString(Path.of(BRANCHES_FILE_PATH));
    }

    @Override
    public String importBranches(String branchesFileContent) throws FileNotFoundException {
        StringBuilder resultInfo = new StringBuilder();

        BranchSeedDto[] branchSeedDtos =
                this.gson.fromJson(new FileReader(BRANCHES_FILE_PATH), BranchSeedDto[].class);

        Arrays.stream(branchSeedDtos)
                .forEach(branchSeedDto -> {

                    if (this.validationUtil.isValid(branchSeedDto)) {

                        if (this.getBranchByName(branchSeedDto.getName()) == null) {
                            Branch branch =
                                    this.modelMapper.map(branchSeedDto, Branch.class);

                            Town town = this.townService.getTownByName(
                                    branchSeedDto.getTown());

                            if (town == null) {
                                resultInfo.append(INCORRECT_DATA_MESSAGE);
                            } else {
                                branch.setTown(town);

                                resultInfo.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                                        branch.getClass().getSimpleName(),
                                        branch.getName()));

                                this.branchRepository.saveAndFlush(branch);
                            }
                        } else {
                            resultInfo.append(IN_DB_MESSAGE);
                        }

                    } else {
                        resultInfo.append(INCORRECT_DATA_MESSAGE);
                    }

                    resultInfo.append(System.lineSeparator());
                });

        return resultInfo.toString().trim();
    }

    @Override
    public Branch getBranchByName(String name) {
        return this.branchRepository.findByName(name);
    }
}
