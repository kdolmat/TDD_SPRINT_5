package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Utility class that follows the singleton design pattern to prevent multiple instantiation of the WebDriver
 */

public class Driver {

    private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>(); // supports thread safe Webdrivers

    private Driver(){} // to prevent creating objects


    public static synchronized WebDriver getDriver(){

        if(drivers.get() == null) { // check if the WebDriver is not initialized

            String browser = System.getProperty("browser");

            if(browser == null) { // if the browser was not passed through command line
                browser = PropertyReader.getProperty("browser");
            }


            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    drivers.set(new ChromeDriver());
                    break;
                case "chromeheadless":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");
                    options.addArguments("start-maximized");
                    drivers.set(new ChromeDriver(options));
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    drivers.set(new EdgeDriver());
                    break;
                case "edgeheadless":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless");
                    drivers.set(new EdgeDriver(edgeOptions));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    drivers.set(new FirefoxDriver());
                    break;
                case "firefoxheadless":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions foptions = new FirefoxOptions();
                    foptions.addArguments("--headless");
                    drivers.set(new FirefoxDriver(foptions));
                    break;
                case "safari":
                    WebDriverManager.safaridriver().setup();
                    drivers.set(new SafariDriver());
                    break;
                default:
                    throw new RuntimeException("Invalid browser");

            }
        }

        return drivers.get();
    }


    public static synchronized void quitDriver(){
        if(drivers.get() != null){
            drivers.get().quit();
            drivers.remove();
        }

    }







}