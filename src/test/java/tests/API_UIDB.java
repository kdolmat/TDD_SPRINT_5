package tests;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
import pages.LoginPOJO;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ApplicationFormPage;
import utilities.DBUtility;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.io.*;
import java.util.*;

public class API_UIDB {

static {
    baseURI = "http://qa-duobank.us-east-2.elasticbeanstalk.com/api";
}

    String key;
    String log = "You have successfully logged in.";
    String reg = "You have successfully registered.";



    //mvn test -DxmlFile=regression.xml // TDD(TESTNG)
    //mvn test -Dcucumber.options="--tags@api" //CUCUMBER
    //OR mvn verify

    @Test
    public void verifications(){

      given().
                header("Accept", "application/json").
                        body("{\n" +
                                "\"email\" : \"" + "duotechb5@gmail.com" + "\",\n" +
                                "\"password\" : \"" + "duotechb5" + "\"\n" +
                                "\n" +
                                "}"
                ).
        when().log().all().
                post("/login.php").
        then().log().all().

                assertThat().
                statusCode(equalTo(200)).//http status code
        contentType(containsString("application/json")).//headers
        body("message",equalTo(log)).//json response
            time(lessThan(3000L))//sanity
    ;
    }



//    @Test(groups = {"smoke"})
    @Test
    @BeforeClass
    public void grabApiKey() throws IOException {


        Map<String,Object>serialization=new HashMap<>();//serialization using maps
        serialization.put("email", "duotechb5@gmail.com");
        serialization.put("password", "duotechb5");

        JsonPath responseBody =  given().
                header("Accept", "application/json").
                body(serialization).
                when().log().all().
                post("/login.php").
                then().log().all().

                assertThat().
                statusCode(equalTo(200)).extract().jsonPath();
       key = responseBody.getString("token");
        System.out.println(key);



    }




//    public static void writeFile(String text) throws FileNotFoundException {
//
//            Scanner file = new Scanner(new File("config.properties"));
//            while(file.hasNext("key")){
//
//                if(file.containsKey("key")){
//                    file.put(text,1);
//                } else{
//                    int count = words.get(word);
//                    count++;
//                    words.put(word,count);
//                }
//
//            }
//
//            file.close();
//            System.out.println(words);
//    }



    @Test
    public void APItoUIincDBacctCreationUsingPOJO(){
    Faker faker = new Faker();
    String Fname = faker.name().firstName();
    String Lname = faker.name().lastName();
    String email = faker.internet().emailAddress();
    String psw = faker.internet().password();//serialization using POJO
        LoginPOJO loginPOJO = new LoginPOJO(Fname,Lname,email,psw);
    given().
    body(loginPOJO).
    when().log().all().
    post("/register.php").
    then().log().all().
    assertThat().
    statusCode(200).
            body("message", equalTo(reg));//hamcrest


        given().
                body("{\n" +
                        "\"email\" : \"" + email + "\",\n" +
                        "\"password\" : \"" + psw + "\"\n" +
                        "\n" +
                        "}").
                when().log().all().
                post("/login.php").
                then().log().all().
                assertThat().
                statusCode(200)
                ;
        DBUtility.createConnection();
        ApplicationFormPage.i_log_in(email,psw);//UI verification

        String query = "select * from tbl_user where email = '"+email+"';";

        List<Map<String, Object>> queryResultListOfMaps = DBUtility.getQueryResultListOfMaps(query);

        for (Map<String, Object> eachRow : queryResultListOfMaps) {
            System.out.println(eachRow);
        }

        Assert.assertTrue(queryResultListOfMaps.get(0).containsValue(email));//DB verification
        DBUtility.close();
}

    @Test
    public void UItoAPIacctCreation(){
        Faker faker = new Faker();
        String Fname = faker.name().firstName();
        String Lname = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String psw = faker.internet().password();

        ApplicationFormPage.i_press_the_button_sign_up(Fname,Lname,email,psw);//UI Acct creation

        Map map = given().//serialization
                body("{\n" +
                        "\"email\" : \"" + email + "\",\n" +
                        "\"password\" : \"" + psw + "\"\n" +
                        "\n" +
                        "}").
                when().log().all().
                post("/login.php").
                then().log().all().
                assertThat().
                statusCode(200).
                body("message",equalTo(log)).extract().as(Map.class)//API verification
        ;
        Assert.assertEquals(map.get("message"),log);//deserialization verification


    }



}
