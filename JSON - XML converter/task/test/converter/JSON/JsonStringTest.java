package converter.JSON;

import converter.JSON.JsonString;
import converter.JSON.StringIndex;
import junit.framework.TestCase;

public class JsonStringTest extends TestCase {

    public void testLoadFromString_escapes() {
        String baseString = "\"a\\nb\\tc\\rА\\\"Б\\/Ц \\b \\f\"";
        StringIndex index = new StringIndex(0);
        JsonString js = new JsonString(baseString, index);
        System.out.println(js);
        assert "\"a\\nb\\tc\\rА\\\"Б\\/Ц \\b \\f\"".equals(js.toString());
        assert index.getIndex() == baseString.length();
    }

    public void testLoadFromString_emptyString() {
        String baseString = "\"\"";
        StringIndex index = new StringIndex(0);
        JsonString js = new JsonString(baseString, index);
        assert "\"\"".equals(js.toString());
        assert index.getIndex() == baseString.length();
    }

    public void testLoadFromString_exception() {
        try {
            String baseString = "\"no end";
            StringIndex index = new StringIndex(0);
            new JsonString(baseString, index);
            assert false;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            String baseString = "\"no \\g\"";
            StringIndex index = new StringIndex(0);
            new JsonString(baseString, index);
            assert false;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}