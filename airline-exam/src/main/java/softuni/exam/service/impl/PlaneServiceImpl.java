package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.IN_DB_MESSAGE;
import static softuni.exam.constants.GlobalConstants.PLANES_FILE_PATH;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository,
                            ModelMapper modelMapper,
                            XMLParser xmlParser,
                            ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlaneSeedRootDto planeSeedRootDto =
                this.xmlParser.unmarshalFromXML(PLANES_FILE_PATH, PlaneSeedRootDto.class);

        planeSeedRootDto.getPlanes()
                .forEach(planeSeedDto -> {

                    if (this.validationUtil.isValid(planeSeedDto)) {

                        if (this.planeRepository
                                .findByRegisterNumber(planeSeedDto.getRegisterNumber()) == null) {

                            Plane plane = this.modelMapper.map(planeSeedDto, Plane.class);

                            sb.append(String.format("Successfully imported Plane %s",
                                    planeSeedDto.getRegisterNumber()));

                            this.planeRepository.saveAndFlush(plane);
                        } else {
                            sb.append(IN_DB_MESSAGE);
                        }

                    } else {
                        sb.append("Invalid Plane");
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public Plane getPlaneByRegisterNumber(String registerNumber) {
        return this.planeRepository.findByRegisterNumber(registerNumber);
    }
}
