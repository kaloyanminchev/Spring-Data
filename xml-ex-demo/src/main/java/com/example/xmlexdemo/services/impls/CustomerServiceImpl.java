package com.example.xmlexdemo.services.impls;

import com.example.xmlexdemo.constants.GlobalConstants;
import com.example.xmlexdemo.models.dtos.seeddtos.CustomerSeedDto;
import com.example.xmlexdemo.models.dtos.seeddtos.CustomerSeedRootDto;
import com.example.xmlexdemo.models.dtos.viewdtos.CustomerSalesViewDto;
import com.example.xmlexdemo.models.dtos.viewdtos.CustomerSalesViewRootDto;
import com.example.xmlexdemo.models.dtos.viewdtos.CustomerViewDto;
import com.example.xmlexdemo.models.dtos.viewdtos.CustomerViewRootDto;
import com.example.xmlexdemo.models.entities.Customer;
import com.example.xmlexdemo.models.entities.Sale;
import com.example.xmlexdemo.repositories.CustomerRepository;
import com.example.xmlexdemo.services.CustomerService;
import com.example.xmlexdemo.utils.ValidationUtil;
import com.example.xmlexdemo.utils.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.xmlexdemo.constants.GlobalConstants.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ModelMapper modelMapper,
                               XMLParser xmlParser,
                               ValidationUtil validationUtil,
                               Random random) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedCustomers() throws JAXBException, FileNotFoundException {
        CustomerSeedRootDto customerSeedRootDto =
                this.xmlParser.unmarshalFromXML(CUSTOMERS_FILE_PATH, CustomerSeedRootDto.class);

        customerSeedRootDto.getCustomers()
                .forEach(customerSeedDto -> {

                    if (this.customerRepository.findByName(customerSeedDto.getName()) != null) {
                        System.out.println(customerSeedDto.getName() + " already exists in database!");
                        return;
                    }

                    if (this.validationUtil.isValid(customerSeedDto)) {
                        this.save(customerSeedDto);
                    } else {
                        this.validationUtil
                                .violations(customerSeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public void save(CustomerSeedDto customerSeedDto) {
        Customer customer = this.modelMapper.map(customerSeedDto, Customer.class);
        this.customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getRandomCustomer() {
        long randomId = this.random.nextInt((int) this.customerRepository.count()) + 1;
        return this.customerRepository.getOne(randomId);
    }

    @Override
    public void visualizeOrderedCustomers() throws JAXBException {
        CustomerViewRootDto customerViewRootDto = new CustomerViewRootDto();

        List<CustomerViewDto> customerViewDtos =
                this.customerRepository
                        .findAllCustomersOrderedByBirthDateAndYoungDriver()
                        .stream()
                        .map(customer -> this.modelMapper.map(customer, CustomerViewDto.class))
                        .collect(Collectors.toList());

        customerViewRootDto.setCustomers(customerViewDtos);

        this.xmlParser.marshalToXML(customerViewRootDto, QUERY_1_FILE_PATH);
    }

    @Override
    public void visualizeCustomerWithSales() throws JAXBException {
        CustomerSalesViewRootDto customerSalesViewRootDto = new CustomerSalesViewRootDto();

        List<CustomerSalesViewDto> customerSalesViewDtos = this.customerRepository
                .findAllCustomersBoughtAtLeastOneCar()
                .stream()
                .map(customer -> {
                    CustomerSalesViewDto customerSalesViewDto = new CustomerSalesViewDto();

                    customerSalesViewDto.setFullName(customer.getName());
                    customerSalesViewDto.setBoughtCars(customer.getSales().size());

                    BigDecimal spentMoney = new BigDecimal(0);
                    for (Sale sale : customer.getSales()) {
                        spentMoney = spentMoney.add(sale.getCar().getPrice());
                    }

                    customerSalesViewDto.setSpentMoney(spentMoney);

                    return customerSalesViewDto;
                })
                .collect(Collectors.toList());

        customerSalesViewDtos = customerSalesViewDtos
                .stream()
                .sorted((c1, c2) -> {
                    int sort = c2.getSpentMoney().compareTo(c1.getSpentMoney());
                    if (sort == 0) {
                        sort = Integer.compare(c2.getBoughtCars(), c1.getBoughtCars());
                    }
                    return sort;
                })
                .collect(Collectors.toList());

        customerSalesViewRootDto.setCustomers(customerSalesViewDtos);

        this.xmlParser.marshalToXML(customerSalesViewRootDto, QUERY_5_FILE_PATH);
    }
}
