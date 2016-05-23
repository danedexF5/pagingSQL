import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by danedexheimer on 5/15/16.
 */
public class PeopleWebService {
    private final Connection connection;

    public PeopleWebService(Connection connection) {
        this.connection = connection;
    }

    //Write a method createTables which takes the database connection.
    //Run it right after you create your connection. It should execute two things:
    //Calls DROP TABLE IF EXISTS people
    //Calls CREATE TABLE people
    //(id IDENTITY, first_name VARCHAR, last_name VARCHAR, email VARCHAR, country VARCHAR, ip VARCHAR)

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS people");
        statement.execute("CREATE TABLE people (id IDENTITY, first_name VARCHAR, last_name VARCHAR, email VARCHAR, country VARCHAR, ip VARCHAR)");

    }

    //Write a method called insertPerson which takes the database connection and the columns.
    public void insertPerson(Connection connection, Person person) throws SQLException {
        // insert the new person
        PreparedStatement statement = connection.prepareStatement("INSERT INTO people VALUES (NULL, ?, ?, ?, ?, ?)");
        statement.setString(1, person.getFirstName());
        statement.setString(2, person.getLastName());
        statement.setString(3, person.getEmail());
        statement.setString(4, person.getCountry());
        statement.setString(5, person.getIpAddress());
        statement.executeUpdate();

        // get the generated id
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next(); // read the first line of results

        // set the generated id into my person
        person.setId(resultSet.getString(1));
    }
    //Write a method called selectPerson which takes the database connection and an id and returns a Person
    public Person selectPerson(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM people WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        Person person = new Person();

        person.setId(Integer.toString(resultSet.getInt(1)));
        person.setFirstName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setEmail(resultSet.getString(4));
        person.setCountry(resultSet.getString(5));
        person.setIpAddress(resultSet.getString(6));

        return person;
    }

    //Write a method called populateDatabase which takes the database connection,
    //then parses the CSV file and inserts each row into the database.
    public void populateDatabase(Connection connection, String filename) throws FileNotFoundException, SQLException {

        ArrayList<Person> people = CSVParser.parse(filename);

        for(Person p : people){
            insertPerson(connection, p);
        }
    }
    //Write a method called selectPeople which takes the database connection and returns an ArrayList<Person>
    //of everything from the database.

    //Modify selectPeople to accept an int offset as an argument.
    //Use LIMIT 20 OFFSET ? in your SQL query and pass the offset to your PreparedStatement.
    public ArrayList<Person> selectPeople(Connection connection, int offset) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM people LIMIT 20 OFFSET ?;");
        //noinspection JpaQueryApiInspection
        preparedStatement.setInt(1, offset);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Person>persons = new ArrayList<>();

        while (resultSet.next()) {

            Person person = new Person();

            person.setId(Integer.toString(resultSet.getInt(1)));
            person.setFirstName(resultSet.getString(2));
            person.setLastName(resultSet.getString(3));
            person.setEmail(resultSet.getString(4));
            person.setCountry(resultSet.getString(5));
            person.setIpAddress(resultSet.getString(6));

            persons.add(person);

        }

            return persons;
    }

}
