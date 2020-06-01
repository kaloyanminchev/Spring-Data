package demo;

import java.sql.*;
import java.util.*;

public class Main {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";

    private static Connection connection;
    private static String query;
    private static PreparedStatement stmt;
    private static Scanner scanner;

    public static void main(String[] args) throws SQLException {
        scanner = new Scanner(System.in);

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "bonnano1313.");

        connection = DriverManager.getConnection(CONNECTION_STRING + DATABASE_NAME, props);

//        2. Get Villain's Names
//        getVillainsNames();

//        3. Get Minion Names
//        getMinionNamesEx();

//        4. Add Minion
//        addMinionEx();

//        5. Change Town Names Casing
//        changeTownNamesByGivenCountry();

//        6. Remove Villain
//        removeVillain();

//        7. Print All Minion Names
//        printAllMinionNamesInSpecificOrder();

//        8. Increase Minions Age
//        increaseMinionAgeByGivenId();

//        9. Increase Age Stored Procedure
//        increaseAgeWithStoreProcedure();

    }

    private static void getVillainsNames() throws SQLException {
        query = "SELECT v.`name`, COUNT(mv.minion_id) AS 'count_minions'\n" +
                "FROM villains AS v\n" +
                "JOIN minions_villains AS mv\n" +
                "ON v.id = mv.villain_id\n" +
                "GROUP BY v.`name`\n" +
                "HAVING count_minions > 15\n" +
                "ORDER BY count_minions DESC";

        stmt = connection.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            System.out.printf("%s %d%n", resultSet.getString(1), resultSet.getInt(2));
        }
    }

    private static void getMinionNamesEx() throws SQLException {
        System.out.print("Enter villain id: ");
        int villainId = Integer.parseInt(scanner.nextLine());

        if (!checkIfEntityExists(villainId, "villains")) {
            System.out.println(String.format("No villain with ID %d exists in the database.", villainId));
            return;
        }

        System.out.printf("Villain: %s%n", getEntityById(villainId, "villains"));
        getMinionNameAndAgeByGivenVillainId(villainId);
    }

    private static boolean checkIfEntityExists(int villainId, String tableName) throws SQLException {
        query = "SELECT * FROM villains WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, villainId);

        ResultSet resultSet = stmt.executeQuery();
        return resultSet.next();
    }

    private static String getEntityById(int villainId, String tableName) throws SQLException {
        query = "SELECT name FROM " + tableName + " WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, villainId);
        ResultSet resultSet = stmt.executeQuery();
        return resultSet.next() ? resultSet.getString(1) : null;
    }

    private static void getMinionNameAndAgeByGivenVillainId(int villainId) throws SQLException {
        query = "SELECT m.`name`, m.age\n" +
                "FROM minions AS m\n" +
                "JOIN minions_villains AS mv\n" +
                "ON m.id = mv.minion_id\n" +
                "WHERE mv.villain_id = ?";

        stmt = connection.prepareStatement(query);
        stmt.setInt(1, villainId);
        ResultSet resultSet = stmt.executeQuery();
        int counter = 1;
        while (resultSet.next()) {
            System.out.printf("%d. %s %d%n", counter++, resultSet.getString(1), resultSet.getInt(2));
        }
    }

    private static void addMinionEx() throws SQLException {
        System.out.print("Enter minion parameters: ");
        String[] minionParameters = scanner.nextLine().split("\\s+");
        String nameMinion = minionParameters[0];
        int ageMinion = Integer.parseInt(minionParameters[1]);
        String townMinion = minionParameters[2];

        System.out.print("Enter villain name: ");
        String villainName = scanner.nextLine();

        if (!checkIfEntityExistsByName(townMinion, "towns")) {
            insertEntityIntoTowns(townMinion);
            System.out.printf("Town %s was added to the database.%n", townMinion);
        }

        if (!checkIfEntityExistsByName(nameMinion, "minions")) {
            insertEntityIntoMinions(nameMinion, ageMinion, townMinion);
        }

        if (!checkIfEntityExistsByName(villainName, "villains")) {
            insertEntityIntoVillains(villainName);
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        setMinionAsServantOfVillain(nameMinion, villainName);
    }

    private static boolean checkIfEntityExistsByName(String entity, String tableName) throws SQLException {
        query = "SELECT * FROM " + tableName + " WHERE name = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, entity);
        ResultSet resultSet = stmt.executeQuery();
        return resultSet.next();
    }

    private static void insertEntityIntoTowns(String entity) throws SQLException {
        query = "INSERT INTO towns (name, country) VALUE(?, NULL)";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, entity);
        stmt.execute();
    }

    private static void insertEntityIntoMinions(String name, int age, String town) throws SQLException {
        query = "INSERT INTO minions (name, age, town_id) VALUE(?, ?, (SELECT id FROM towns WHERE `name` = ?))";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setInt(2, age);
        stmt.setString(3, town);
        stmt.execute();
    }

    private static void insertEntityIntoVillains(String entity) throws SQLException {
        query = "INSERT INTO villains (name, evilness_factor) VALUE (?, ?)";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, entity);
        stmt.setString(2, "evil");
        stmt.execute();
    }

    private static void setMinionAsServantOfVillain(String nameMinion, String villainName) throws SQLException {
        query = "INSERT INTO minions_villains VALUE((SELECT id FROM minions WHERE `name` = ?), (SELECT id FROM villains WHERE `name` = ?))";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, nameMinion);
        stmt.setString(2, villainName);
        stmt.execute();
        System.out.printf("Successfully added %s to be minion of %s.%n", nameMinion, villainName);
    }

    private static void changeTownNamesByGivenCountry() throws SQLException {
        System.out.print("Enter country: ");
        String country = scanner.nextLine();

        if (!checkIfCountryExists(country)) {
            System.out.println("No town names were affected.");
            return;
        }

        getTownNamesToUpperInCountry(country);
    }

    private static boolean checkIfCountryExists(String country) throws SQLException {
        query = "SELECT * FROM towns WHERE country = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, country);
        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next();
    }

    private static void getTownNamesToUpperInCountry(String country) throws SQLException {
        query = "SELECT UPPER(`name`) FROM towns WHERE country = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, country);
        ResultSet resultSet = stmt.executeQuery();

        List<String> townsUpper = new ArrayList<>();
        while (resultSet.next()) {
            townsUpper.add(resultSet.getString(1));
        }

        if (townsUpper.size() > 1) {
            System.out.printf("%d town names were affected.%n", townsUpper.size());
        } else {
            System.out.printf("%d town name was affected.%n", townsUpper.size());
        }
        System.out.println(townsUpper);
    }

    private static void removeVillain() throws SQLException {
        System.out.print("Enter villain ID: ");
        int villainID = Integer.parseInt(scanner.nextLine());

        if (!checkIfEntityExists(villainID, "villains")) {
            System.out.println("No such villain was found");
            return;
        }

        String villainName = getEntityById(villainID, "villains");
        int countReleasedMinions = getCountOfMinionServingToVillain(villainID);

        deleteEntityByID(villainID);

        System.out.printf("%s was deleted%n", villainName);
        System.out.printf("%d minions released", countReleasedMinions);

    }

    private static int getCountOfMinionServingToVillain(int villainID) throws SQLException {
        query = "SELECT COUNT(minion_id) AS 'count_minions'\n" +
                "FROM minions_villains\n" +
                "WHERE villain_id = ?";

        stmt = connection.prepareStatement(query);
        stmt.setInt(1, villainID);
        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    private static void deleteEntityByID(int entityID) throws SQLException {
        query = "DELETE FROM minions_villains WHERE villain_id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, entityID);
        stmt.execute();

        query = "DELETE FROM villains WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, entityID);
        stmt.execute();
    }

    private static void printAllMinionNamesInSpecificOrder() throws SQLException {
        List<String> names = getAllMinionNames();
        for (int i = 0; i < names.size() / 2; i++) {
            System.out.println(names.get(i));
            System.out.println(names.get(names.size() - 1 - i));
        }

        if (names.size() % 2 != 0) {
            System.out.println(names.get(names.size() / 2));
        }
    }

    private static List<String> getAllMinionNames() throws SQLException {
        List<String> names = new ArrayList<>();
        query = "SELECT name FROM minions";
        stmt = connection.prepareStatement(query);

        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            names.add(resultSet.getString(1));
        }

        return names;
    }

    private static void increaseMinionAgeByGivenId() throws SQLException {
        System.out.print("Enter minions IDs: ");
        String[] minionIDs = scanner.nextLine().split("\\s+");

        for (int i = 0; i < minionIDs.length; i++) {
            int currentID = Integer.parseInt(minionIDs[i]);
            if (!checkIfIdExists(currentID)) {
                System.out.println(String.format("ID %d does not exist!", currentID));
            } else {
                increaseAgeAndLowerName(currentID);
            }
        }

        printAllNamesAndAges();

    }

    private static boolean checkIfIdExists(int minionId) throws SQLException {
        query = "SELECT * FROM minions WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, minionId);
        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next();
    }

    private static void increaseAgeAndLowerName(int minionID) throws SQLException {
        query = "UPDATE minions\n" +
                "SET  age = age + 1, `name` = LOWER(`name`)\n" +
                "WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, minionID);
        stmt.execute();
    }

    private static void printAllNamesAndAges() throws SQLException {
        query = "SELECT name, age FROM minions";
        stmt = connection.prepareStatement(query);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n", resultSet.getString(1), resultSet.getInt(2));
        }
    }

    private static void increaseAgeWithStoreProcedure() throws SQLException {
        System.out.print("Enter minion ID: ");
        int minionID = Integer.parseInt(scanner.nextLine());

        query = "CALL usp_get_older(?)";
        CallableStatement callableStatement = connection.prepareCall(query);
        callableStatement.setInt(1, minionID);
        callableStatement.execute();
    }
}
