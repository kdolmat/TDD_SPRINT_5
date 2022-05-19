package pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import utilities.Driver;
import utilities.PropertyReader;
import utilities.SeleniumUtils;

import java.sql.SQLException;
import java.util.List;


public class ApplicationFormPage {



//TDD methods Sprint 5
    public static void i_log_in(String email, String password) {
        Driver.getDriver().get(PropertyReader.getProperty("url"));
        Homepage homepage = new Homepage();
        homepage.email.sendKeys(email);
        homepage.password.sendKeys(password+Keys.ENTER);
        Driver.quitDriver();
    }

   public static void i_press_the_button_sign_up(String f, String l,String e,String p) {
       Driver.getDriver().get(PropertyReader.getProperty("url"));
        SignupPage signupPage = new SignupPage();
        signupPage.SignupWindow.click();
       signupPage.firstName.sendKeys(f);
       signupPage.lastName.sendKeys(l);
       signupPage.email.sendKeys(e);
       signupPage.password.sendKeys(p);
       signupPage.button.click();
       Driver.quitDriver();
    }


}
