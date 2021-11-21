package converter.JSON;

import converter.StringIndex;

public class JsonNumber extends JsonElement {
    private int intValue;
    private double doubleValue;
    private boolean isInt;

    public JsonNumber(StringIndex stringIndex) {
        super(stringIndex);
    }

    public boolean isInt() {
        return isInt;
    }

    public int asInt() throws IllegalArgumentException {
        if (!isInt) {
            throw new IllegalArgumentException("Double json number can not transformed to int");
        }
        return intValue;
    }

    public double asDouble() {
        return isInt ? intValue : doubleValue;
    }

    @Override
    protected void loadFromString(StringIndex i) throws IllegalArgumentException {
        // https://miro.medium.com/max/598/0*BO8czSbb5FbFuibD.gif
        // JSON number format
        //TODO: make smaller!!!
        boolean noDot = true;
        boolean noExp = true;
        StringBuilder res = new StringBuilder();
        if (i.getCurrentChar() == '-') {
            res.append('-');
            i.inc();
        }
        char ch = i.getCurrentChar();
        final String error_prefix = "JSON number element parsing error: ";
        if (ch == '0') {
            res.append(ch);
            i.inc();
        } else if (ch >= '1' && ch <= '9') {
            res.append(ch);
            i.inc();
            if (i.hasChar()) {
                addDigits(i, res);
            }
        } else {
            throw new IllegalArgumentException(error_prefix + "',' or '}' is absent");
        }
        if (i.hasChar() && i.getCurrentChar() == '.') {
            noDot = false;
            res.append('.');
            i.inc();
            if (Character.isDigit(i.getCurrentChar())) {
                addDigits(i, res);
            } else {
                throw new IllegalArgumentException(error_prefix + "there is need one or more digits after '.'");
            }
        }
        if (i.hasChar() && (i.getCurrentChar() == 'e' || i.getCurrentChar() == 'E')) {
            noExp = false;
            res.append('e');
            i.inc();
            if (i.getCurrentChar() == '+' || i.getCurrentChar() == '-') {
                res.append(i.getCurrentChar());
                i.inc();
            }
            if (Character.isDigit(i.getCurrentChar())) {
                addDigits(i, res);
            } else {
                throw new IllegalArgumentException(error_prefix + "there is need one or more digits after 'e'");
            }
        }
        try {
            doubleValue = Double.parseDouble(res.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(error_prefix + e.getMessage());
        }
        if (doubleValue == Double.POSITIVE_INFINITY || doubleValue == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException(error_prefix + "infinity detected");
        }
        if (doubleValue == Math.floor(doubleValue) &&
                doubleValue <= Integer.MAX_VALUE &&
                doubleValue >= Integer.MIN_VALUE) {
            isInt = true;
            if (noDot && noExp) {
                try {
                    intValue = Integer.parseInt(res.toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(error_prefix + e.getMessage());
                }
            } else {
                intValue = (int) doubleValue;
            }
        }
    }

    private void addDigits(StringIndex i, StringBuilder res) {
        while (i.hasChar() && Character.isDigit(i.getCurrentChar())) {
            res.append(i.getCurrentChar());
            i.inc();
        }
    }

    @Override
    protected String getString() {
        return isInt ? String.valueOf(intValue) : String.valueOf(doubleValue);
    }
}
