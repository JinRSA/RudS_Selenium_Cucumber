import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"stepDefs"},
        tags = "@1")
public class Main extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    public static void main(String[] args) {
        String path2ChromeDriver = "C:\\My\\Projects\\Java\\Libraries\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", path2ChromeDriver);

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        // 1.
        driver.get("https://avito.ru");
        driver.manage().window().maximize();
        // 2.
        Select categoryDropDownList = new Select(driver.findElement(By.id("category")));
        //categoryDropDownList.getOptions().forEach(opt -> System.out.println(opt.getAttribute("value") + "\t" + opt.getText()));
        categoryDropDownList.selectByVisibleText("Оргтехника и расходники");
        // 3.
        WebElement search = driver.findElement(By.xpath("//input[@data-marker=\"search-form/suggest\"]"));
        search.sendKeys("Принтер");
        // 4.
        driver.findElement(By.xpath("//div[@data-marker=\"search-form/region\"]")).click();
        // 5.
        WebElement searchCity = driver.findElement(By.xpath("//input[@data-marker=\"popup-location/region/input\"]"));
        searchCity.sendKeys("Владивосток");
//        (new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//strong[\"Владивосток\"]")));//.click();
        WebElement city = driver.findElement(By.xpath("//strong[text()=\"Владивосток\"]"));
        city.click();
        driver.findElement(By.xpath("//button[@data-marker=\"popup-location/save-button\"]")).click();
        // 6.
        WebElement checkBox = driver.findElement(By.xpath("//input[@data-marker=\"delivery-filter/input\"]"));
        if (!checkBox.isSelected())
            checkBox.sendKeys(Keys.SPACE);
        driver.findElement(By.xpath("//button[@data-marker=\"search-filters/submit-button\"]")).click();
        // 7.
        Select filterByDropDownList = new Select(driver.findElement(By.xpath("//select[option[./text()=\"По умолчанию\"]]")));
        filterByDropDownList.selectByVisibleText("Дороже");
        // 8.
        List<WebElement> resultsName = driver.findElements(By.xpath("//h3[@itemprop=\"name\"]"));
        List<WebElement> resultsPrice = driver.findElements(By.xpath("//span[@data-marker=\"item-price\"]"));
        for (int i = 0; i < 3; ++i) {
            System.out.println("Наименование: " + resultsName.get(i).getText() + "\nЦена: " + resultsPrice.get(i).getText());
        }

//        driver.close();
        driver.quit();
    }
}