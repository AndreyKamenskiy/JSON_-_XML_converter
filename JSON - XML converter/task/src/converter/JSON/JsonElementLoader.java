package converter.JSON;

import converter.StringIndex;

public class JsonElementLoader {
    private final JsonElement element;

    public JsonElementLoader(StringIndex index) throws IllegalArgumentException {
        element = loader(index);
    }

    public JsonElementLoader(String str) throws IllegalArgumentException {
        StringIndex index = new StringIndex(0, str);
        element = loader(index);
    }

    public JsonElement getElement() {
        return element;
    }

    private JsonElement loader (StringIndex index) throws IllegalArgumentException {
        JsonElement element;
        char ch = index.getNextNotSpaceChar();
        if (ch == '{') {
            element = new JsonMap(index);
        } else if (ch == '"') {
            element = new JsonString(index);
        } else if (ch == 'n') {
            element = new JsonNull(index);
        } else if (Character.isDigit(ch) || ch == '-') {
            element = new JsonNumber(index);
        } else {
            throw new IllegalArgumentException("Unknown JSON element format found");
        }
        return element;
    }

}
