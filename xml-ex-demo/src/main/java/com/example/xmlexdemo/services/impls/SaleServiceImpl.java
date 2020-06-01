package com.example.xmlexdemo.services.impls;

import com.example.xmlexdemo.constants.GlobalConstants;
import com.example.xmlexdemo.models.dtos.viewdtos.CarSalesViewDto;
import com.example.xmlexdemo.models.dtos.viewdtos.CarViewDto;
import com.example.xmlexdemo.models.dtos.viewdtos.SaleViewRootDto;
import com.example.xmlexdemo.models.dtos.viewdtos.SalesViewDto;
import com.example.xmlexdemo.models.entities.Sale;
import com.example.xmlexdemo.repositories.SaleRepository;
import com.example.xmlexdemo.services.CarService;
import com.example.xmlexdemo.services.CustomerService;
import com.example.xmlexdemo.services.SaleService;
import com.example.xmlexdemo.utils.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.xmlexdemo.constants.GlobalConstants.QUERY_6_FILE_PATH;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final Random random;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository,
                           CarService carService,
                           CustomerService customerService,
                           Random random,
                           ModelMapper modelMapper,
                           XMLParser xmlParser) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.customerService = customerService;
        this.random = random;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedSales() {
        for (int i = 0; i < 20; i++) {
            Sale sale = new Sale();

            sale.setDiscount(this.setRandomDiscount());
            sale.setCar(this.carService.getRandomCar());
            sale.setCustomer(this.customerService.getRandomCustomer());

            this.saleRepository.saveAndFlush(sale);
        }
    }

    private double setRandomDiscount() {
        double[] discounts = new double[]{0D, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5};

        return discounts[this.random.nextInt(discounts.length)];
    }

    @Override
    public void visualizeSalesWithDiscount() throws JAXBException {
        SaleViewRootDto saleViewRootDto = new SaleViewRootDto();

        List<SalesViewDto> salesViewDtos =
                this.saleRepository
                        .findAll()
                        .stream()
                        .map(sale -> {
                            SalesViewDto salesViewDto = new SalesViewDto();

                            CarSalesViewDto carSalesViewDto =
                                    this.modelMapper.map(sale.getCar(), CarSalesViewDto.class);

                            salesViewDto.setCarSalesViewDto(carSalesViewDto);
                            salesViewDto.setCustomerName(sale.getCustomer().getName());

                            double discount = sale.getDiscount();
                            salesViewDto.setDiscount(discount);

                            BigDecimal price = sale.getCar().getPrice();
                            salesViewDto.setPrice(price);

                            BigDecimal priceWithDiscount =
                                    price.multiply(BigDecimal.valueOf(1 - discount));
                            salesViewDto.setPriceWithDiscount(priceWithDiscount);

                            return salesViewDto;
                        }).collect(Collectors.toList());

        saleViewRootDto.setSales(salesViewDtos);
        this.xmlParser.marshalToXML(saleViewRootDto, QUERY_6_FILE_PATH);
    }
}
