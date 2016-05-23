import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleWeb {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        // create a server
        Server server = Server.createTcpServer("-baseDir", "./data").start();

        // created our connection
        String jdbcUrl = "jdbc:h2:" + server.getURL() + "/main";
        System.out.println(jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl, "", null);

        // configure service
        PeopleWebService peopleWebService = new PeopleWebService(connection);

        // insure the DB tables exist
        peopleWebService.createTables();

        // create a variable of type ArrayList that holds instances of People.
        peopleWebService.populateDatabase(connection, "people.csv");
        // Use the CSVParser to read and parse the people.csv into this ArrayList of People

        //Make the / route use selectPeople(conn, offsetNum) to get the ArrayList<Person>
        Spark.get(
                "/",
                (request, response) -> {
                    // Create a HashMap for your model
                    HashMap m = new HashMap();

                    // declare a variable named offset to hold your offset
                    // this needs to be an integer so we can do some arithmetic
                    int offset;

                    // because we're going to have to parse an integer from string data we need to open a try/catch block
                    try {
                        // Get the offset query parameter from the request.
                        String StrOffset = request.queryParams("offset");
                        // Set this into a string variable temporarily.


                        // Parse the string offset from a string to an integer.
                        offset = Integer.parseInt(StrOffset);
                        // Set the resulting integer into the variable you created to hold the offset.
                    }

                    // now we need a catch block in case the offset can't be parsed as an integer
                catch(NumberFormatException nfe) {
                    // When the number can't be parsed default the offset to 0
                    offset = 0;
                }

                    ArrayList<Person>people = peopleWebService.selectPeople(connection, offset);

                    // We need to know two things
                    // 1) can we go to a previous page
                    // 2) if so, what is the offset of that page
                    // To do so, create an Integer variable named "backOffset" to hold the offset for the previous page and set it to null.
                    Integer backOffset = null;

                    // Check if the offset is not 0. This tells us we're not on the first page.
                    if(offset != 0) {
                        // If so, set backOffset to the offset minus twenty
                        backOffset = offset - 20;
                    }

                    // put the offset for the previous page into the model.
                    m.put("backOffset", backOffset);

                    // Now we need to know if we can go to a next page from the current page.
                    // Create another Integer variable named nextOffset and set that to null.
                    Integer nextOffSet = null;

                    // check if the current offset is less than the size of the people array, minus twenty
                    if(people.size() == 20) {

                        // set nextOffset to the offset plus twenty
                        nextOffSet = offset + 20;
                    }

                    // put the offset for the next page into the model.
                    m.put("nextOffset", nextOffSet);


                    // put the variable containing the list of people into the model with a key named "people"
                    m.put("people", people);

                    // return a ModelAndView object for the people.mustache template.
                    return new ModelAndView(m, "people.mustache");
                },
                new MustacheTemplateEngine()
        );

        //Make the /person route use selectPerson(conn, idNum) to get the Person.
        Spark.get(
                "/person",
                (request, response) -> {
                    // create a HashMap for your model
                    HashMap m = new HashMap();

                    // Create a variable to hold the ID property and read it from the request's query params
                    String peopleId = request.queryParams("id");

                    Person person = peopleWebService.selectPerson(connection, Integer.parseInt(peopleId));

                    m.put("person", person);

                    // return a ModelAndView object for the person.mustache template.
                    return new ModelAndView(m, "person.mustache");
                },
                new MustacheTemplateEngine()
        );
    }
}
