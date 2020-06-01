package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.domain.dtos.PictureSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XMLParser;

import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.PICTURES_FILE_PATH;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository,
                              ModelMapper modelMapper,
                              XMLParser xmlParser,
                              ValidatorUtil validatorUtil) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String importPictures() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PictureSeedRootDto pictureSeedRootDto =
                this.xmlParser.unmarshalFromXML(PICTURES_FILE_PATH, PictureSeedRootDto.class);

        pictureSeedRootDto.getPictures()
                .forEach(pictureSeedDto -> {
                    if (this.validatorUtil.isValid(pictureSeedDto)) {

                        if (this.pictureRepository.findByUrl(pictureSeedDto.getUrl()) == null) {
                            Picture picture =
                                    this.modelMapper.map(pictureSeedDto, Picture.class);

                            sb.append(String.format("Successfully imported picture - %s",
                                    pictureSeedDto.getUrl()));

                            this.pictureRepository.saveAndFlush(picture);
                        } else {
                            sb.append("Already in DB!");
                        }
                    } else {
                        sb.append("Invalid picture");
                    }
                    sb.append(System.lineSeparator());
                });

        return sb.toString().trim();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public Picture getPictureByUrl(String url) {
        return this.pictureRepository.findByUrl(url);
    }
}
