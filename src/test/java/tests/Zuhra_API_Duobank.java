package tests;

import com.github.javafaker.Faker;
//import org.junit.Assert;
//import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import utilities.Driver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Zuhra_API_Duobank {

    @Test
    public void DuoBank_POST() {



        // Verify correct HTTP status code.

        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";

        String email = new Faker().internet().emailAddress();
        String pass = new Faker().internet().password();
        given().
                body("{\n" +
                        "\"first_name\" : \"Max\",\n" +
                        "\"last_name\" : \"Milan\",\n" +
                        "\"email\" : \"" + email + "\",\n" +
                        "\"password\" : \"" + pass + "\"\n" +
                        "\n" +
                        "}").
                when().log().all().
                post("/register.php").
                then().log().all().
                assertThat().
                statusCode(200).



                // Verify response payload.
                        body("status", equalTo(201)).
                body("message", equalTo("You have successfully registered."));






        // Verify the user creation through logging in with the same creadentials in the UI;

        Driver.getDriver().get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php");

        Driver.getDriver().findElement(By.id("exampleInputEmail1")).sendKeys(email, Keys.TAB, pass, Keys.ENTER);

        Assert.assertTrue(Driver.getDriver().getTitle().contains("Loan Application"));


        Driver.getDriver().quit();



        //Verify response headers.

        given().
                header("Accept", "application/json").
                when().log().all().
                get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php").
                then().log().all().
                statusCode(200);




        //Verify basic performance sanity.


        given().
                header("Accept", "application/json").
                when().log().all().
                get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php").
                then().log().all().
                body("\"email\"", equalTo(email)).
                body("\"password\"", equalTo(pass)).
                assertThat().
                statusCode(equalTo(200)).
                header("Content-Type","application/json; charset=UTF-8").
                body("\"success\"", containsString("0")).
                body("\"status\"", containsString("0"));





        //  basic positive tests









        //  negative tests with valid input that attempt illegal operations
        given().
                header("Accept", "application/json").
                when().log().all().
                get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php").
                then().log().all().
                body("\"first_name\"", is("\"Max\"")).
                body("\"last_name\"", is("\"Milan\"")).
                body("\"email\"", equalTo(email)).
                body("\"password\"", equalTo(pass)).
                assertThat().
                statusCode(equalTo(200)).
                header("Content-Type","application/json; charset=UTF-8").
                body("\"success\"", containsString("0")).
                body("\"status\"", containsString("0"));











        //. negative tests with invalid input
















        // destructive tests that intentionally attempt to fail the API to














        //check its robustness.





    }

















    //Verify basic performance sanity.









}

