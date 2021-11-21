package converter.JSON;

import junit.framework.TestCase;

public class JsonNullTest extends TestCase {

    public void test_null() {
        {
            StringIndex index = new StringIndex(0, "null");
            JsonNull jn = new JsonNull(index);
            System.out.println(jn);
            StringIndex index2 = new StringIndex(0, jn.toString());
            JsonNull jn1 = new JsonNull(index2);
            assert jn.toString().equals(jn1.toString());
        }
        {
            StringIndex index = new StringIndex(0, "{ \"null element \n \"  :    \tnull}");
            JsonElement jn = new JsonElementLoader(index).getElement();
            System.out.println(jn);
            StringIndex index2 = new StringIndex(0, jn.toString());
            JsonElement jn1 = new JsonElementLoader(index2).getElement();
            assert jn.toString().equals(jn1.toString());
        }
    }

    private void loaderExceptionTest(String badJsonString) throws IllegalArgumentException {
        try {
            StringIndex index = new StringIndex(0, badJsonString);
            JsonElement jn = new JsonElementLoader(index).getElement();
            assert false;
        } catch (IllegalArgumentException e) {
            System.out.print("Error Message: ");
            System.out.println(e.getMessage());
        }
    }

    public void test_exceptions() {
        loaderExceptionTest("nUll");
        loaderExceptionTest("nul");
        //loaderExceptionTest("nulll");
        loaderExceptionTest("{\"qwe\":nul}");
        loaderExceptionTest("{\"qwe\":nulll}");
    }

}
