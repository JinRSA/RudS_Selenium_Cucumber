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

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StepDefs {
    private WebDriver driver;

    @Пусть("открыт ресурс авито")
    public void открытРесурсАвито() {
        String path2ChromeDriver = "C:\\My\\Projects\\Java\\Libraries\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", path2ChromeDriver);

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.get("https://avito.ru");
        driver.manage().window().maximize();
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
        Select categoryDropDownList = new Select(driver.findElement(By.id("category")));
        categoryDropDownList.selectByVisibleText(categ.getValue());
    }

    @И("в поле поиска введено значение {string}")
    public void вПолеПоискаВведеноЗначение(String value) {
        WebElement search = driver.findElement(By.xpath("//input[@data-marker=\"search-form/suggest\"]"));
        search.sendKeys(value);
    }

    @Тогда("кликнуть по выпадающему списку региона")
    public void кликнутьПоВыпадающемуСпискуРегиона() {
        driver.findElement(By.xpath("//div[@data-marker=\"search-form/region\"]")).click();
    }

    @Тогда("в поле регион введено значение {string}")
    public void вПолеРегионВведеноЗначение(String cityStr) {
        WebElement searchCity = driver.findElement(By.xpath("//input[@data-marker=\"popup-location/region/input\"]"));
        searchCity.sendKeys(cityStr);
        WebElement city = driver.findElement(By.xpath("//strong[text()=\"" + cityStr + "\"]"));
        city.click();
    }

    @И("нажата кнопка показать объявления")
    public void нажатаКнопкаПоказатьОбъявления() {
        driver.findElement(By.xpath("//button[@data-marker=\"popup-location/save-button\"]")).click();
    }

    @Тогда("открылась страница результаты по запросу {string}")
    public void открыласьСтраницаРезультатыПоЗапросу(String value) {
        String result = driver.findElement(By.xpath("//h1[@data-marker=\"page-title/text\"]")).getText();
        assert result.contains(value) : "Страница с результатами по запросу \"" + value + "\" не найдена.";
    }

    @И("активирован чекбокс только с фотографией")
    public void активированЧекбоксТолькоСФотографией() {
        WebElement checkBox = driver.findElement(By.xpath("//input[@data-marker=\"delivery-filter/input\"]"));
        if (!checkBox.isSelected())
            checkBox.sendKeys(Keys.SPACE);
        driver.findElement(By.xpath("//button[@data-marker=\"search-filters/submit-button\"]")).click();
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
        Select filterByDropDownList = new Select(driver.findElement(By.xpath("//select[option[./text()=\"По умолчанию\"]]")));
        filterByDropDownList.selectByVisibleText(sortBy.getBy());
    }

    @И("в консоль выведено значение названия и цены {int} первых товаров")
    public void вКонсольВыведеноЗначениеНазванияИЦеныПервыхТоваров(int count) {
        List<WebElement> resultsName = driver.findElements(By.xpath("//h3[@itemprop=\"name\"]"));
        List<WebElement> resultsPrice = driver.findElements(By.xpath("//span[@data-marker=\"item-price\"]"));
        assert count <= resultsName.size() : count + ">" + resultsName.size() + "! Значение должно быть в пределах количества выводимых на странице результатов";
        for (int i = 0; i < count; ++i) {
            System.out.println("Наименование: " + resultsName.get(i).getText() + "\nЦена: " + resultsPrice.get(i).getText());
        }
        driver.quit();
    }
}
