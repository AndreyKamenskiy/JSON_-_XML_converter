package converter.JSON;

/*
    Класс представляет собой элемент json
    Формат

 */
public abstract class JsonElement {

    public JsonElement(StringIndex stringIndex) {
        loadFromString(stringIndex);
    }

    protected abstract void loadFromString(StringIndex stringIndex);

    @Override
    public String toString() {
        return getString();
    }

    protected abstract String getString();

}
