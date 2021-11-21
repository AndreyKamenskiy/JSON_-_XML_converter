package converter.JSON;

import junit.framework.TestCase;

public class JsonNumberTest extends TestCase {

    private double rnd(int from, int to) {
        double f = Math.random() / Math.nextDown(1.0);
        return from * (1.0 - f) + to * f;
    }

    public void test_toString() {
        testString("1", true, 1, 1.0d);
        testString("1.0", true, 1, 1.0d);
        testString("-1", true, -1, -1.0d);
        testString("-1.0", true, -1, -1.0d);
        testString(String.valueOf(Integer.MAX_VALUE), true, Integer.MAX_VALUE, Integer.MAX_VALUE);
        testString(String.valueOf(Integer.MIN_VALUE), true, Integer.MIN_VALUE, Integer.MIN_VALUE);
        long val = (long)Integer.MAX_VALUE + 1;
        testString(String.valueOf(val), false, 0, val);
        val = (long)Integer.MIN_VALUE - 1;
        testString(String.valueOf(val), false, 0, val);
        for (int i = 0; i < 100; ++i) {
            double curr = rnd(Integer.MIN_VALUE, Integer.MAX_VALUE);
            testString(String.valueOf(curr), false, (int) curr, curr);
            testString(String.valueOf((int)curr), true, (int) curr, (int)curr);
        }
        testString("50e105", false, 0, 50e105d);
        testString("-50e105", false, 0, -50e105d);
    }

    public void test_errorSearching() {
        error_test("+0");
        error_test("-");
        error_test("1.");
        error_test("1E");
        error_test("1E+");
        error_test("1e-");
        error_test("1e-.");
        error_test("1.e-.");
        error_test("1e5000");
        error_test("-1e5000");
    }

    private void testString(String str, boolean isInt, int asInt, double asDouble) {
        JsonElement element = new JsonElementLoader(str).getElement();
        assert element instanceof JsonNumber;
        JsonNumber num = (JsonNumber) element;
        assert isInt == num.isInt();
        if (isInt) {
            assert num.asInt() == asInt;
        }
        assert num.asDouble() == asDouble;
    }
    
    private void error_test(String bad_number){
        try {
            JsonElement element = new JsonElementLoader(bad_number).getElement();
            assert false;
        } catch (IllegalArgumentException e) {
            System.out.println("Caught error: " + e.getMessage());
        }
    }

}