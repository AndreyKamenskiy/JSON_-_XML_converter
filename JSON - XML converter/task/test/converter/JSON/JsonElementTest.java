package converter.JSON;

import junit.framework.TestCase;

public class JsonElementTest extends TestCase {

    public void testGetType_null() {
            JsonElement element = new JsonElementLoader("null").getElement();
            assert element.getType() == JsonElementType.NULL;
    }

    public void testGetType_number() {
        JsonElement element = new JsonElementLoader("-1523").getElement();
        assert element.getType() == JsonElementType.NUMBER;
    }

    public void testGetType_string() {
        JsonElement element = new JsonElementLoader("\"-1523\"").getElement();
        assert element.getType() == JsonElementType.STRING;
    }

    public void testGetType_map() {
        JsonElement element = new JsonElementLoader("{\"name\":\"val\", \"num\":1, \"empty\":null}").getElement();
        assert element.getType() == JsonElementType.MAP;
    }


}