package hiberspring.service.impl;

import hiberspring.domain.dtos.ProductSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hiberspring.common.GlobalConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final BranchService branchService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ModelMapper modelMapper,
                              XmlParser xmlParser,
                              ValidationUtil validationUtil,
                              BranchService branchService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.branchService = branchService;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(Path.of(PRODUCTS_FILE_PATH));
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
        StringBuilder resultInfo = new StringBuilder();

        ProductSeedRootDto productSeedRootDto =
                this.xmlParser.unmarshalFromXML(PRODUCTS_FILE_PATH, ProductSeedRootDto.class);

        productSeedRootDto.getProducts()
                .forEach(productSeedDto -> {

                    if (this.validationUtil.isValid(productSeedDto)) {

                        if (this.productRepository.findByName(productSeedDto.getName()) == null) {

                            Product product =
                                    this.modelMapper.map(productSeedDto, Product.class);

                            Branch branch = this.branchService.
                                    getBranchByName(productSeedDto.getBranch());

                            if (branch == null) {
                                resultInfo.append(INCORRECT_DATA_MESSAGE);
                            } else {
                                product.setBranch(branch);

                                resultInfo.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                                        product.getClass().getSimpleName(),
                                        product.getName()));

                                this.productRepository.saveAndFlush(product);
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
}
