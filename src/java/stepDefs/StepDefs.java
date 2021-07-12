package stepDefs;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class StepDefs {
    private WebDriver m_driver;
    private String m_object, m_city;

    @Пусть("открыт ресурс авито")
    public void открытРесурсАвито() {
        try (InputStream input = new FileInputStream("src/test/resources/resources.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            String path2ChromeDriver = properties.getProperty("path2ChromeDriver");
            System.setProperty("webdriver.chrome.driver", path2ChromeDriver);
        } catch (Exception ignored) {}

        m_driver = new ChromeDriver();
        m_driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        m_driver.get("https://avito.ru");
        m_driver.manage().window().maximize();
    }

    @ParameterType(".*")
    public Category category(String categ) {
        categ = categ.substring(0, 1).toUpperCase() + categ.substring(1).toLowerCase();
        categ = categ.replaceAll("\\p{Punct}", "");
        categ = categ.replaceAll(" ", "_");
        return Category.valueOf(categ);
    }

    @И("в выпадающем списке категорий выбрана {category}")
    public void вВыпадающемСпискеКатегорийВыбранаОргтехникаИРасходники(Category categ) {
        By by = By.id("category");
        Select categoryDropDownList = new Select(m_driver.findElement(by));
        categoryDropDownList.selectByVisibleText(categ.getValue());
    }

    @И("в поле поиска введено значение {string}")
    public void вПолеПоискаВведеноЗначение(String value) {
        m_object = value;
        By by = By.xpath("//input[@data-marker=\"search-form/suggest\"]");
        WebElement search = m_driver.findElement(by);
        search.sendKeys(value);
    }

    @Тогда("кликнуть по выпадающему списку региона")
    public void кликнутьПоВыпадающемуСпискуРегиона() {
        By by = By.xpath("//div[@data-marker=\"search-form/region\"]");
        m_driver.findElement(by).click();
    }

    @Тогда("в поле регион введено значение {string}")
    public void вПолеРегионВведеноЗначение(String cityStr) {
//        m_city = cityStr;
        By by = By.xpath("//input[@data-marker=\"popup-location/region/input\"]");
        WebElement searchCity = m_driver.findElement(by);
        searchCity.sendKeys(cityStr);
        by = By.xpath("//strong[text()=\"" + cityStr + "\"]");
        WebElement city = m_driver.findElement(by);
        by = By.xpath("//span[strong[text()=\""+ cityStr + "\"]]");
        m_city = city.findElement(by).getText();
        city.click();
    }

    @И("нажата кнопка показать объявления")
    public void нажатаКнопкаПоказатьОбъявления() {
        By by = By.xpath("//button[@data-marker=\"popup-location/save-button\"]");
        m_driver.findElement(by).click();
    }

    @Тогда("открылась страница результаты по запросу {string}")
    public void открыласьСтраницаРезультатыПоЗапросу(String value) {
        By by = By.xpath("//h1[@data-marker=\"page-title/text\"]");
        String result = m_driver.findElement(by).getText();
        assert result.contains(value) : "Страница с результатами по запросу \"" + value + "\" не найдена.";
    }

    @И("активирован чекбокс только с фотографией")
    public void активированЧекбоксТолькоСФотографией() {
        By by = By.xpath("//input[@data-marker=\"delivery-filter/input\"]");
        WebElement checkBox = m_driver.findElement(by);
        if (!checkBox.isSelected())
            checkBox.sendKeys(Keys.SPACE);
        by = By.xpath("//button[@data-marker=\"search-filters/submit-button\"]");
        m_driver.findElement(by).click();
    }

    @ParameterType(".*")
    public SortBy sortBy(String sortBy) {
        sortBy = sortBy.substring(0, 1).toUpperCase() + sortBy.substring(1).toLowerCase();
        sortBy = sortBy.replaceAll("\\p{Punct}", "");
        sortBy = sortBy.replaceAll(" ", "_");
        return SortBy.valueOf(sortBy);
    }

    @И("в выпадающем списке сортировка выбрано значение {sortBy}")
    public void вВыпадающемСпискеСортировкаВыбраноЗначениеДороже(SortBy sortBy) {
        By by = By.xpath("//select[option[./text()=\"По умолчанию\"]]");
        Select filterByDropDownList = new Select(m_driver.findElement(by));
        filterByDropDownList.selectByVisibleText(sortBy.getBy());
    }

    @И("в консоль выведено значение названия и цены {int} первых товаров")
    public void вКонсольВыведеноЗначениеНазванияИЦеныПервыхТоваров(int count) {
        By by = By.xpath("//h3[@itemprop=\"name\"]");
        List<WebElement> resultsName = m_driver.findElements(by);
        by = By.xpath("//span[@data-marker=\"item-price\"]");
        List<WebElement> resultsPrice = m_driver.findElements(by);
        assert count <= resultsName.size() : count + ">" + resultsName.size() + "! Значение должно быть в пределах количества выводимых на странице результатов";
        String output = m_city + "; " + m_object + '.';
        for (int i = 0; i < count; ++i)
            output += "\nНаименование: " + resultsName.get(i).getText() + "\nЦена: " + resultsPrice.get(i).getText();
        System.out.println(output);
        m_driver.quit();
    }
}
