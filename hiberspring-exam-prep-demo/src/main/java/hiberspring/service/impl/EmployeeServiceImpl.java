package hiberspring.service.impl;

import hiberspring.domain.dtos.EmployeeSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static hiberspring.common.GlobalConstants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final EmployeeCardService employeeCardService;
    private final BranchService branchService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ModelMapper modelMapper,
                               XmlParser xmlParser,
                               ValidationUtil validationUtil,
                               EmployeeCardService employeeCardService,
                               BranchService branchService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.employeeCardService = employeeCardService;
        this.branchService = branchService;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEES_FILE_PATH));
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder resultInfo = new StringBuilder();

        EmployeeSeedRootDto employeeSeedRootDto =
                this.xmlParser.unmarshalFromXML(EMPLOYEES_FILE_PATH, EmployeeSeedRootDto.class);

        employeeSeedRootDto.getEmployees()
                .forEach(employeeSeedDto -> {

                    if (this.validationUtil.isValid(employeeSeedDto)) {

                        if (this.employeeRepository
                                .findByFirstNameAndLastNameAndPosition(employeeSeedDto.getFirstName(),
                                        employeeSeedDto.getLastName(),
                                        employeeSeedDto.getPosition()) == null) {

                            Employee employee =
                                    this.modelMapper.map(employeeSeedDto, Employee.class);

                            EmployeeCard card = this.employeeCardService
                                    .getCardByNumber(employeeSeedDto.getCard());

                            Branch branch = this.branchService
                                    .getBranchByName(employeeSeedDto.getBranch());

                            if (card == null
                                    || branch == null
                                    || this.employeeRepository.findByCard(card) != null) {
                                resultInfo.append(INCORRECT_DATA_MESSAGE);

                            } else {
                                employee.setCard(card);
                                employee.setBranch(branch);

                                resultInfo.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                                        employee.getClass().getSimpleName(),
                                        String.format("%s %s",
                                                employee.getFirstName(),
                                                employee.getLastName())));

                                this.employeeRepository.saveAndFlush(employee);
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
    public String exportProductiveEmployees() {
        StringBuilder exportResult = new StringBuilder();

        this.employeeRepository
                .findAllByBranchWithAtLeastOneProduct(PRODUCTS_SIZE)
                .forEach(e -> {
                    exportResult.append(String.format("Name: %s %s\n" +
                                    "Position: %s\n" +
                                    "Card Number: %s\n" +
                                    "-------------------------\n",
                            e.getFirstName(), e.getLastName(),
                            e.getPosition(),
                            e.getCard().getNumber()));
                });

        return exportResult.toString().trim();
    }
}
