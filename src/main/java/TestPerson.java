import org.h2.tools.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestPerson {

    Connection connection;
    PeopleWebService peopleWebService;

    @Before
    public void before() throws SQLException {


        Server server = Server.createTcpServer("-baseDir", "./data").start();

        // created our connection
        String jdbcUrl = "jdbc:h2:" + server.getURL() + "/main";

        connection = DriverManager.getConnection(jdbcUrl, "", null);

        // configure service
        peopleWebService = new PeopleWebService(connection);

        // insure the DB tables exist
        peopleWebService.createTables();
    }

    @Test
    public void whenInsertingPersonIntoTable() throws SQLException {

        //Set up your test directory and write a test called testPerson which tests both of the aforementioned methods.
        /**Given a Person object
         * When inserting a Person into table
         * Then Person object gets an id
         */

        //arrange
        Person personTest = new Person();

        personTest.setFirstName("firstName");
        personTest.setLastName("lastName");
        personTest.setEmail("email");
        personTest.setCountry("country");
        personTest.setIpAddress("ipAddress");

        //act
        //call insertPerson method
        peopleWebService.insertPerson(connection, personTest);

        connection.commit();

        //assert
        assertThat(personTest.getId(), not(0));

        //arrange

        //act
        //call insertPerson method
        Person person = peopleWebService.selectPerson(connection, Integer.parseInt(personTest.getId()));

        //assert
        assertThat(person.getId(), is(personTest.getId()));

    }

    //Write a test called testPeople that tests selectPeople
    @Test
    public void testPeople() throws SQLException, FileNotFoundException {
        //arrange
        peopleWebService.populateDatabase(connection, "people.csv");

        //act
        ArrayList<Person> persons = peopleWebService.selectPeople(connection, 40);

        //assert
        assertThat(persons.size(), not(0));
    }

}
