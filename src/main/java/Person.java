/**
 * Created by doug on 5/8/16.
 */
public class Person {

    // create a public id property. Use String type to keep things simple
    String id;

    // create a public property for first name
    String firstName;

    // create a public property for last name
    String lastName;

    // create a public property for email
    String email;

    // create a public property for country
    String country;

    // create a public property for ipAddress
    String ipAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
