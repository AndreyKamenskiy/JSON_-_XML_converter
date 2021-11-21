package converter.JSON;

import converter.StringIndex;
import junit.framework.TestCase;

public class JsonMapTest extends TestCase {

    public void testLoadFromString_emptyMap() {
        String baseString = "{}";
        StringIndex index = new StringIndex(0, baseString);
        JsonMap jm = new JsonMap(index);
        System.out.println(jm);
        assert "{\n}".equals(jm.toString());
        assert index.getIndex() == baseString.length();
    }

    public void testLoadFromString_stringElement() {
        String baseString = "{\"name\" :  \t \"value\"}";
        StringIndex index = new StringIndex(0, baseString);
        JsonMap jm = new JsonMap(index);
        System.out.println(jm);
        assert "{\n  \"name\" : \"value\"\n}".equals(jm.toString());
        assert index.getIndex() == baseString.length();
    }

    public void testLoadFromString_mapElement() {
        String baseString = "{\"name\" :  \n {\n\n\n\"n1\":\"Q\", \"n2\":\"W\"}}";
        System.out.println(baseString);
        StringIndex index = new StringIndex(0, baseString);
        JsonMap jm = new JsonMap(index);
        System.out.println(jm);
        assert "{\n  \"name\" : {\n  \"n1\" : \"Q\",\n  \"n2\" : \"W\"\n}\n}"
                .equals(jm.toString());
        assert index.getIndex() == baseString.length();
    }

}