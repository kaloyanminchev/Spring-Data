import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
//        Ex 2
//        this.removeObjectsEx();

//        Ex 3
//        try {
//            this.containsEmployeeEx();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Ex 4
//        this.employeesWithSalaryOver50000();

//        Ex 5
//        this.employeesFromDepartment();

//        Ex 6
//        try {
//            this.addAddressAndUpdateEmployee();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Ex 7
//        this.addressesWithEmployeeCount();

//        Ex 8
//        try {
//            this.getEmployeeWithProject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Ex 9
//        this.findLatestTenProjects();

//        Ex 10
//        this.increaseSalaries();

//        Ex 11
//        try {
//            this.removeTowns();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Ex 12
//        try {
//            this.findEmployeesByFirstName();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Ex 13
//        this.employeesMaxSalaries();
    }

    private void removeObjectsEx() {
        this.entityManager.getTransaction().begin();

        this.entityManager.createQuery("UPDATE Town AS t " +
                "SET t.name = LOWER(t.name) " +
                "WHERE LENGTH(t.name) <= 5")
                .executeUpdate();

        this.entityManager.getTransaction().commit();

        /* Another Solution
        List<Town> towns = this.entityManager
                .createQuery("SELECT t FROM Town AS t " +
                                "WHERE length(t.name) <= 5",
                        Town.class)
                .getResultList();

        this.entityManager.getTransaction().begin();
        towns
                .forEach(this.entityManager::detach);

        for (Town town : towns) {
            town.setName(town.getName().toLowerCase());
        }

        towns
                .forEach(this.entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
         */
    }

    private void containsEmployeeEx() throws IOException {
        System.out.println("Enter employee full name:");
        String fullName = this.reader.readLine();

        try {
            Employee employee = this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                    "WHERE CONCAT(e.firstName, ' ', e.lastName) = :name", Employee.class)
                    .setParameter("name", fullName)
                    .getSingleResult();
            System.out.println("Yes");
        } catch (NoResultException nre) {
            System.out.println("No");
        }
    }

    private void employeesWithSalaryOver50000() {
        this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                "WHERE e.salary > 50000", Employee.class)
                .getResultStream()
                .forEach(e -> System.out.println(e.getFirstName()));
    }

    private void employeesFromDepartment() {
        this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.name = 'Research and Development' ORDER BY e.salary, e.id",
                Employee.class)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s from %s - $%.2f%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getDepartment().getName(),
                        e.getSalary())
                );
    }

    private void addAddressAndUpdateEmployee() throws IOException {
        System.out.println("Enter employee last name:");
        String lastName = reader.readLine();

        try {
            Employee employee = getEmployeeByLastName(lastName);
            Address address = this.createNewAddress("Vitoshka 15");

            this.entityManager.getTransaction().begin();
            this.entityManager.detach(employee);
            employee.setAddress(address);
            this.entityManager.merge(employee);
            this.entityManager.flush();
            this.entityManager.getTransaction().commit();

        } catch (NoResultException nre) {
            System.out.println(
                    String.format("Person with last name %s does not exist! Please enter existing last name!",
                            lastName)
            );
        }
    }

    private Employee getEmployeeByLastName(String entity) {
        return this.entityManager.createQuery("SELECT e FROM Employee AS e WHERE e.lastName = :name",
                Employee.class)
                .setParameter("name", entity)
                .getSingleResult();
    }

    private Address createNewAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(address);
        this.entityManager.getTransaction().commit();
        return address;
    }

    private void addressesWithEmployeeCount() {
        this.entityManager.createQuery("SELECT a FROM Address AS a ORDER BY a.employees.size DESC",
                Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",
                        a.getText(),
                        a.getTown().getName(),
                        a.getEmployees().size()));
    }

    private void getEmployeeWithProject() throws IOException {
        System.out.println("Enter employee id:");
        int employeeId = Integer.parseInt(reader.readLine());

        try {
            Employee employee = getEmployeeById(employeeId);
            System.out.println(String.format("%s %s - %s",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getJobTitle()));

            employee.getProjects()
                    .stream()
                    .sorted(Comparator.comparing(Project::getName))
                    .forEach(p -> System.out.printf("   %s%n", p.getName()));
        } catch (NoResultException nre) {
            System.out.println("ID does not exist in database, please enter an existing ID!");
        }
    }

    private Employee getEmployeeById(int employeeId) {
        return this.entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.id = :id", Employee.class)
                .setParameter("id", employeeId)
                .getSingleResult();
    }

    private void findLatestTenProjects() {
        this.entityManager
                .createQuery("SELECT p FROM Project AS p ORDER BY p.startDate DESC",
                        Project.class)
                .setMaxResults(10)
                .getResultStream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("Project name: %s%n" +
                                "   Project Description: %s%n" +
                                "   Project Start Date:%s%n" +
                                "   Project End Date: %s%n",
                        p.getName(),
                        p.getDescription(),
                        p.getStartDate(),
                        p.getEndDate())
                );
    }

    private void increaseSalaries() {
        this.entityManager.getTransaction().begin();
//        this.entityManager.createQuery("UPDATE Employee AS e " +
//                "SET e.salary = e.salary * 1.12 " +
//                "WHERE e.department.name IN ('Tool Design', 'Marketing', 'Engineering', 'Information Services')")
//                .executeUpdate();
        this.entityManager.createQuery("UPDATE Employee AS e " +
                "SET e.salary = e.salary * 1.12 " +
                "WHERE e.department.id IN (1, 2, 4, 11)")
                .executeUpdate();

        this.entityManager.getTransaction().commit();

        /* Another Solution
        List<Employee> employees = getEmployeesInSpecificDepartments();

        this.entityManager.getTransaction().begin();
        employees.forEach(this.entityManager::detach);

        for (Employee employee : employees) {
            employee.setSalary(employee.getSalary().multiply(new BigDecimal("1.12")));
        }

        employees.forEach(this.entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
        */

        printEmployeesWithIncreasedSalaries();
    }

    private List<Employee> getEmployeesInSpecificDepartments() {
        return this.entityManager
                .createQuery("SELECT e FROM Employee AS e " +
                                "WHERE e.department.name IN " +
                                "('Engineering', 'Tool Design', 'Marketing', 'Information Services')",
                        Employee.class)
                .getResultList();
    }

    private void printEmployeesWithIncreasedSalaries() {
        List<Employee> employees = getEmployeesInSpecificDepartments();
        for (Employee employee : employees) {
            System.out.println(String.format("%s %s ($%.2f)",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getSalary())
            );
        }
    }

    private void removeTowns() throws IOException {
        System.out.println("Enter town:");
        String townName = reader.readLine();

        List<Address> addresses = this.entityManager
                .createQuery("SELECT a FROM Address AS a WHERE a.town.name = :name", Address.class)
                .setParameter("name", townName)
                .getResultList();

        this.entityManager.getTransaction().begin();

        for (Address address : addresses) {
            for (Employee employee : address.getEmployees()) {
                employee.setAddress(null);
            }
            this.entityManager.remove(address);
        }

        Town town = this.entityManager.createQuery("SELECT t FROM Town AS t WHERE t.name = :name", Town.class)
                .setParameter("name", townName)
                .getSingleResult();

        this.entityManager.remove(town);
        this.entityManager.getTransaction().commit();

        if (addresses.size() == 1) {
            System.out.printf("1 address in %s deleted", townName);
        } else {
            System.out.println(String.format("%d addresses in %s deleted", addresses.size(), townName));
        }
    }

    private void findEmployeesByFirstName() throws IOException {
        System.out.println("Enter first name pattern:");
        String pattern = reader.readLine();

        this.entityManager
                .createQuery("SELECT e FROM Employee AS e WHERE e.firstName LIKE CONCAT(:pattern, '%')"
                        , Employee.class)
                .setParameter("pattern", pattern)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getJobTitle(),
                        e.getSalary())
                );
    }

    private void employeesMaxSalaries() {
        Query query = this.entityManager
                .createQuery("SELECT e.department.name, MAX(e.salary) FROM Employee AS e " +
                        "GROUP BY e.department.name " +
                        "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000");

        List<Object[]> resultList = query.getResultList();
        resultList
                .forEach(r -> System.out.println(Arrays.toString(r).replaceAll("[\\[\\],]", "")));
    }
}

