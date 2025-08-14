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
    protected WebDriver driver;
    protected Properties config;

    public void setUp() {
        loadConfig();
        String browser = config.getProperty("browser", "chrome");

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(config.getProperty("baseUrl"));
    }

    public void tearDown() {
        if (driver != null) {
           // driver.quit();
        }
    }

    private void loadConfig() {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
