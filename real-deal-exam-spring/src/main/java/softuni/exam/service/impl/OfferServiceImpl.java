package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.OfferSeedRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.PictureService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static softuni.exam.constants.GlobalConstants.*;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XMLParser xmlParser;
    private final CarService carService;
    private final SellerService sellerService;
    private final PictureService pictureService;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository,
                            ModelMapper modelMapper,
                            ValidationUtil validationUtil,
                            XMLParser xmlParser,
                            CarService carService,
                            SellerService sellerService,
                            PictureService pictureService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.carService = carService;
        this.sellerService = sellerService;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto =
                this.xmlParser.unmarshalFromXML(OFFERS_FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto.getOffers()
                .forEach(offerSeedDto -> {

                    if (this.validationUtil.isValid(offerSeedDto)) {

                        if (this.getOfferByDescriptionAndAddedOn(offerSeedDto.getDescription(),
                                offerSeedDto.getAddedOn()) == null) {

                            Offer offer =
                                    this.modelMapper.map(offerSeedDto, Offer.class);

                            Long id = offerSeedDto.getCar().getId();
                            Car car = this.carService.getCarById(id);
                            Seller seller =
                                    this.sellerService.getSellerById(offerSeedDto.getSeller().getId());

                            offer.setCar(car);
                            offer.setSeller(seller);
                            offer.setPictures(this.pictureService.getAllByCarId(car.getId()));

                            output.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "offer",
                                    String.format("%s - %s", offer.getAddedOn(), offer.getHasGoldStatus())));

                            this.offerRepository.saveAndFlush(offer);
                        } else {
                            output.append(IN_DB_MESSAGE);
                        }

                    } else {
                        output.append(INCORRECT_DATA_MESSAGE + "offer");
                    }
                    output.append(System.lineSeparator());
                });

        return output.toString().trim();
    }

    @Override
    public Offer getOfferByDescriptionAndAddedOn(String description, LocalDateTime addedOn) {
        return this.offerRepository.findByDescriptionAndAddedOn(description, addedOn);
    }
}
