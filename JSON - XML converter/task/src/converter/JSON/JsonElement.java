package converter.JSON;

import converter.StringIndex;

/*
    Класс представляет собой элемент json
    Формат

 */
public abstract class JsonElement {

    public JsonElement(StringIndex stringIndex) {
        loadFromString(stringIndex);
    }

    @Override
    public String toString() {
        return getString();
    }

    public JsonElementType getType() throws IllegalArgumentException {
        if (this instanceof JsonNumber) {
            return JsonElementType.NUMBER;
        }
        if (this instanceof JsonNull) {
            return JsonElementType.NULL;
        }
        if (this instanceof JsonString) {
            return JsonElementType.STRING;
        }
        if (this instanceof JsonMap) {
            return JsonElementType.MAP;
        }
        //TODO: write JsonBoolean and JsonArray to uncomment the following text
//        if (this instanceof JsonBoolean) {
//            return JsonElementType.BOOLEAN;
//        }
//        if (this instanceof JsonArray) {
//            return JsonElementType.ARRAY;
//        }
        throw new IllegalArgumentException("Unknown type of JsonElement");
    }

    protected abstract void loadFromString(StringIndex stringIndex);

    protected abstract String getString();



}
