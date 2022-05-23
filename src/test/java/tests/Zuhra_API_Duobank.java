package tests;

import com.github.javafaker.Faker;
import io.restassured.common.mapper.TypeRef;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.Homepage;
import pages.SignupPage;
import utilities.Driver;
import utilities.PropertyReader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Zuhra_API_Duobank {

    String Femail = new Faker().internet().emailAddress();
    String Fpassword = new Faker().internet().password();
    String FirstName = new Faker().name().firstName();
    String LastName = new Faker().name().lastName();
    RequestSpecification requestSpecification;
    Response response;

//    static {
//        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
//    }





    // Verify correct HTTP status code and // Verify response payload.
    @Test
    public void verify_All_MultiLayer_API_to_UI() {
        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
        given().
                body("{\n" +
                        "\"first_name\" : \"" + FirstName + "\",\n" +
                        "\"last_name\" : \"" + LastName + "\",\n" +
                        "\"email\" : \"" + Femail + "\",\n" +
                        "\"password\" : \"" + Fpassword + "\"\n" +
                        "\n" +
                        "}").
                when().log().all().
                post("/register.php").
                then().log().all().
                assertThat().
                statusCode(equalTo(200)).
                body("status", equalTo(201)).
                body("message", equalTo("You have successfully registered."));
        Driver.getDriver().get(PropertyReader.getProperty("url"));
        Homepage login = new Homepage();
        login.email.sendKeys(Femail);
        login.password.sendKeys(Fpassword);
        Assert.assertTrue(Driver.getDriver().getTitle().contains("Loan Application"));
        Driver.getDriver().quit();

    }



    //Verify response headers.
    @Test
    public void verify_responce_headers() {
        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
        given().
                header("Accept", "application/json").
                when().log().all().
                get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php").
                then().log().all().
                statusCode(equalTo(200));

    }




    @Test
    public  void basiAuth(){
      given().
//              auth().
//              basic("postman", "password").
                header("Authorization","Basic cG9zdG1hbjpwYXNzd29yZA=="). //we can make enter here dynamic as well
                when().log().all().
                get("https://postman-echo.com/basic-auth").
                then().log().all().
                statusCode(200).
                body("authenticated", equalTo(true));

    }



    //Verify basic performance sanity testing .

    @Test
    public void verify_sanity_testing() {
        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
        given().body("{\n" +
                        "\"first_name\" : \"" + FirstName + "\",\n" +
                        "\"last_name\" : \"" + LastName + "\",\n" +
                        "\"email\" : \"" + Femail + "\",\n" +
                        "\"password\" : \"" + Fpassword + "\"\n" +
                        "\n" +
                        "}").
                header("Accept", "application/json").
                header("Content-Type", "text/plain; charset=ISO-8859-1").
                when().log().all().
                post("/register.php").
                then().log().all().
                body("\"email\"", equalTo(Femail)).
                body("\"password\"", equalTo(Fpassword)).
                assertThat().
                statusCode(equalTo(200)).
                header("Content-Type", "application/json; charset=UTF-8").
                body("\"success\"", containsString("0"));

    }





    @Test
    public void verify_postive_testing(){
        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
        given().
                body("{\"first_name\": \"Falikq\",\n" +
                        "\"last_name\": \"Falimkq\",\n" +
                        "\"email\": \"failkaskq@mail.com\",\n" +
                        "\"password\": \"falikaskq1234\"}").
                when().log().all().
                post("/register.php").
                then().log().all().
                body("\"first_name\"", equalTo("Falikq")).
                body("\"last_name\"", equalTo("Falimkq")).
                body("\"email\"", equalTo("failkaskq@mail.com")).
                body("\"password\"", equalTo("falikaskq1234")).
                assertThat().
                statusCode(equalTo(201)).
                body("\"message\"", containsString("You have successfully registered.")).
                body("\"success\"", containsString("1"));



        // it did not match due to expected was faker actual was null
        given().
                body("{\"first_name\": \"Falik\",\n" +
                        "\"last_name\": \"Falimk\",\n" +
                        "\"email\": \"failkask@mail.com\",\n" +
                        "\"password\": \"falikask1234\"}").
                when().log().all().
                get("http://qa-duobank.us-east-2.elasticbeanstalk.com/index.php").
                then().log().all().
                body("\"first_name\"", equalTo(FirstName)).
                body("\"last_name\"",equalTo(LastName)).
                body("\"email\"", equalTo(Femail)).
                body("\"password\"", equalTo(Fpassword)).
                assertThat().
                statusCode(equalTo(200)).
                header("Content-Type", "application/json; charset=UTF-8").
                body("\"success\"", containsString("0"));


    }




    // Serialization

    @Test
    public void DuoBank_Serialization() {

        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";

        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("first_name", FirstName);
        loginMap.put("last_name", LastName);
        loginMap.put("email", Femail);
        loginMap.put("password", Fpassword);

        given().
                header("Accept", "application/json").
                body(loginMap, ObjectMapperType.GSON).
                when().log().all().
                post("/register.php").
                then().log().all().
                assertThat().
                statusCode(equalTo(200)).
                body("\"success\"", equalTo(1)).
                body("\"status\"", equalTo(201)).
                body("\"message\"", equalTo("You have successfully registered."));


    }




    // Deserialization using POGO
    @Test
    public void DuoBank_Deserialization_Using_POGO() {

        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";

        Zukhra_Duobank serializeInfo= new Zukhra_Duobank(
                "Falik",
                "Falimk",
                "failkask@mail.com",
                "falikask1234");
        Map mapInfo= given().
                header("Accept", "*/*").
                body(serializeInfo).
                when().log().all().
                post("/register.php").
                then().log().all().
                statusCode(is(200)).
                header("Content-Type", "application/json; charset=UTF-8").extract().as(Map.class);
        System.out.println(mapInfo);
        System.out.println(mapInfo.get("first_name"));


    }


    // POGO

    @Test
    public void POGO() {
        baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";

        Zukhra_Duobank serializeInfo= new Zukhra_Duobank(
                FirstName,
                LastName,
                Femail,
                Fpassword);

        Zukhra_Duobank responseObj= given().
                header("Accept", "*/*").
                body(serializeInfo).
                when().log().all().
                post("/register.php").
                then().log().all().
                statusCode(is(200)).
                header("Content-Type","application/json; charset=UTF-8").extract().as(Zukhra_Duobank.class);
        System.out.println(responseObj);
        System.out.println(responseObj.getFirst_name());




        // List Pogo serialization
//        List<Zukhra_Duobank> list = given().
//                header("Accept", "application/json").
//                when().log().all().
//                get("/index.php").
//                then().log().all().
//                statusCode(is(201)).extract().as(new TypeRef<List<Zukhra_Duobank>>() {
//                });
//
//        for (Zukhra_Duobank responce : list) {
//            Assert.assertNotNull(responce.getClass());
//        }

    }
}

