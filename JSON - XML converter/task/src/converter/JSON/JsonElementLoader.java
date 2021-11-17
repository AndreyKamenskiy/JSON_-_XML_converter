package converter.JSON;

public class JsonElementLoader {
    private JsonElement element;

    public JsonElementLoader(StringIndex index) {
        //необходимо определить какой тип элемента нам подали на вход
        // варианты : Json объект - словарь
        // массив
        // целое или вещественное число
        // true или false
        // null
        // строка
        // на первых порах мы рассмотрим парсинг только словаря и строки.
        switch (index.getNextNotSpaceChar()) {
            case '{':
                element = new JsonMap(index);
                break;
            case '"':
                element = new JsonString(index);
                break;
            //TODO: добавить загрзку других типов элементов
            default:
                throw new IllegalArgumentException("Неизвестный формат JSON елемента");
        }
    }

    public JsonElement getElement() {
        return element;
    }

}
