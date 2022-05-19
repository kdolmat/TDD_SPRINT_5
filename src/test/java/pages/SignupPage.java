package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class SignupPage {

    public SignupPage(){

        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy (partialLinkText = "Register - Create an Account")
    public WebElement title;

  //  @FindBy(id = "register")
    @FindBy (xpath = "//a[@href=\"register.php\"]")
    public WebElement SignupWindow;

    @FindBy (id="inputfirstname4")
    public  WebElement firstName;

    @FindBy (id = "inputlastname4")
    public  WebElement lastName;

    @FindBy (id = "email")
    public  WebElement email;

    @FindBy (id = "exampleInputPassword1")
    public WebElement password;

    @FindBy (id = "register")
    public  WebElement button;

   @FindBy (xpath = "//button[@type=\"submit\"]")
   public  WebElement login;


}
