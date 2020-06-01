package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.SellerSeedRootDto;
import softuni.exam.models.entities.Rating;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XMLParser xmlParser;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository,
                             ModelMapper modelMapper,
                             ValidationUtil validationUtil,
                             XMLParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        SellerSeedRootDto sellerSeedRootDto =
                this.xmlParser.unmarshalFromXML(SELLERS_FILE_PATH, SellerSeedRootDto.class);

        sellerSeedRootDto.getSellers()
                .forEach(sellerSeedDto -> {

                    if (this.validationUtil.isValid(sellerSeedDto)) {

                        if (this.getSellerByEmail(sellerSeedDto.getEmail()) == null) {
                            Seller seller =
                                    this.modelMapper.map(sellerSeedDto, Seller.class);

                                output.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "seller",
                                        String.format("%s - %s", seller.getLastName(), seller.getEmail())));

                                this.sellerRepository.saveAndFlush(seller);
                        } else {
                            output.append(IN_DB_MESSAGE);
                        }

                    } else {
                        output.append(INCORRECT_DATA_MESSAGE + "seller");
                    }
                    output.append(System.lineSeparator());
                });

        return output.toString().trim();
    }

    @Override
    public Seller getSellerByEmail(String email) {
        return this.sellerRepository.findByEmail(email);
    }

    @Override
    public Seller getSellerById(Long id) {
        return this.sellerRepository.findById(id).orElse(null);
    }
}
