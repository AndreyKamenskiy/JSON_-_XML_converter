package converter.JSON;

import converter.StringIndex;

public class JsonString extends JsonElement {
    private String value;

    public static String toJsonString(String text) {
        StringBuilder res = new StringBuilder();
        res.append("\"");
        for (char ch : text.toCharArray()) {
            convertCharToJSON(ch, res);
        }
        res.append("\"");
        return res.toString();

    }

    public JsonString(StringIndex stringIndex) {
        super(stringIndex);
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void loadFromString(StringIndex i) throws IllegalArgumentException {
        StringBuilder res = new StringBuilder();
        i.inc();
        int length = i.getString().length();
        while (true) {
            if (i.getIndex() == length) {
                throw new IllegalArgumentException("JSON string element parsing error: closing quote is absent");
            }
            char ch = i.getCurrentChar();
            if (ch == '"') {
                i.inc();
                break;
            }
            if (ch == '\\') {
                i.inc();
                if (i.getIndex() == length) {
                    throw new IllegalArgumentException("JSON string element parsing error: closing quote is absent");
                }
                ch = i.getCurrentChar();
                ch = convertJsonEscapeSymbol(ch);
            }
            res.append(ch);
            i.inc();
        }
        this.value = res.toString();
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

    private static void convertCharToJSON(char ch, StringBuilder res) {
        final String noConverting = "\"\\/\b\f\n\r\t";
        if (noConverting.indexOf(ch) == -1) {
            res.append(ch);
            return;
        }
        switch (ch) {
            case '\"':
                res.append("\\\"");
                break;
            case '\\':
                res.append("\\\\");
                break;
            case '/':
                res.append("\\/");
                break;
            case '\b':
                res.append("\\b");
                break;
            case '\f':
                res.append("\\f");
                break;
            case '\n':
                res.append("\\n");
                break;
            case '\r':
                res.append("\\r");
                break;
            case '\t':
                res.append("\\t");
                break;
            default:
                throw new IllegalArgumentException("Unknown json string escape sequence - \\" + ch);
        }
    }

    @Override
    protected String getString() {
        return toJsonString(value);
    }
}
