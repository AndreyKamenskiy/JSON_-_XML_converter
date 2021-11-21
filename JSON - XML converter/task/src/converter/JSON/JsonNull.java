package converter.JSON;

import converter.StringIndex;

public class JsonNull extends JsonElement {

    public JsonNull(StringIndex stringIndex) {
        super(stringIndex);
    }

    @Override
    protected void loadFromString(StringIndex i) throws IllegalArgumentException {
        final char[] nullArray  = {'n', 'u', 'l', 'l'};
        // TODO: remove all toCharArray from circles, because it make new temp array.
        for (char ch : nullArray) {
            i.checkCurrentChar(ch, "Null json element parsing error: ");
            i.inc();
        }
    }

    private char convertJsonEscapeSymbol(char ch) throws IllegalArgumentException {
        final String noConverting = "\"\\/";
        if (noConverting.indexOf(ch) != -1) {
            return ch;
        }
        switch (ch) {
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
        }
        throw new IllegalArgumentException("Unknown json string escape sequence - \\" + ch);
    }

    @Override
    protected String getString() {
        return "null";
    }
}
