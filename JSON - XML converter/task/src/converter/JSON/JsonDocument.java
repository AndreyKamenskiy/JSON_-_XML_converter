package converter.JSON;

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
        //this.root = new JsonElementLoader(jsonString, ).getElement();
    }
}