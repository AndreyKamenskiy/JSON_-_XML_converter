package converter.JSON;

import converter.StringIndex;

/*
    Класс хранит json документ.
 */
public class JsonDocument {

    private JsonElement root;

    public JsonElement getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return this.root.toString();
    }

    public void load(String jsonString) {
        StringIndex index = new StringIndex(0, jsonString);
        this.root = new JsonElementLoader(index).getElement();
    }
}