package stepDefs;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;

public class StepDefs {
    @Пусть("открыт ресурс авито")
    public void открытРесурсАвито() {

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
        System.out.println(categ.getValue() + "\t" + categ.name());
    }

    @И("в поле поиска введено значение {string}")
    public void вПолеПоискаВведеноЗначение(String value) {

    }

    @Тогда("кликнуть по выпадающему списку региона")
    public void кликнутьПоВыпадающемуСпискуРегиона() {
    }

    @Тогда("в поле регион введено значение {string}")
    public void вПолеРегионВведеноЗначение(String city) {
    }

    @И("нажата кнопка показать объявления")
    public void нажатаКнопкаПоказатьОбъявления() {
    }

    @Тогда("открылась страница результаты по запросу {string}")
    public void открыласьСтраницаРезультатыПоЗапросу(String arg0) {
    }

    @И("активирован чекбокс только с фотографией")
    public void активированЧекбоксТолькоСФотографией() {
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
        System.out.println(sortBy.getBy() + "\t" + sortBy.name());
    }

    @И("в консоль выведено значение названия и цены {int} первых товаров")
    public void вКонсольВыведеноЗначениеНазванияИЦеныПервыхТоваров(int count) {
    }
}
