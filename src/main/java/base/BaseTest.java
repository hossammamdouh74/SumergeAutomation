package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver; // WebDriver instance for interacting with the browser
    protected Properties config; // Properties object to hold configuration settings

    // Set up the browser and open the base URL
    public void setUp() {
        loadConfig(); // Load the configuration settings from the properties file
        String browser = config.getProperty("browser", "chrome"); // Get the browser name from config, default is chrome

        // Check which browser to use based on the configuration
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup(); // Set up ChromeDriver
            driver = new ChromeDriver(); // Initialize ChromeDriver
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup(); // Set up EdgeDriver
            driver = new EdgeDriver(); // Initialize EdgeDriver
        }

        driver.manage().window().maximize(); // Maximize the browser window
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Set an implicit wait of 10 seconds
        driver.get(config.getProperty("baseUrl")); // Navigate to the base URL from the config file
    }

    // Close the browser and clean up resources
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser and terminate the WebDriver session
        }
    }

    // Load configuration settings from the properties file
    private void loadConfig() {
        config = new Properties(); // Create a new Properties object to store config settings
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            config.load(fis); // Load the properties from the config file
        } catch (IOException e) {
            e.printStackTrace(); // Print any exception if loading the properties fails
        }
    }
}
