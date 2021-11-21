package converter.JSON;

import converter.StringIndex;

import java.util.HashMap;
import java.util.Map;

public class JsonMap extends JsonElement {
    private HashMap<String, JsonElement> map;

    public JsonMap(StringIndex stringIndex) {
        super(stringIndex);
    }

    @Override
    protected void loadFromString(StringIndex i) throws IllegalArgumentException {
        map = new HashMap<>();
        i.inc();
        //test for empty map
        i.skipSpaces();
        if (i.getNextNotSpaceChar() == '}') {
            i.inc();
            return;
        }
        int length = i.getString().length();
        while (true) {
            if (i.getIndex() == length) {
                throw new IllegalArgumentException("JSON map element parsing error: closing curly brace is absent");
            }
            if (i.getNextNotSpaceChar() != '"') {
                throw new IllegalArgumentException("JSON map element parsing error: '\"' symbol is absent");
            }
            JsonString name = new JsonString(i);
            if (i.getNextNotSpaceChar() != ':') {
                throw new IllegalArgumentException("JSON map element parsing error: ':' symbol is absent");
            }
            i.inc();
            JsonElement element = new JsonElementLoader(i).getElement();
            this.map.put(name.getValue(), element);
            //now i point to the next after element char
            char ch = i.getNextNotSpaceChar();
            if (ch == ',') {
                i.inc();
            } else if (ch == '}') {
                i.inc();
                break;
            } else {
                throw new IllegalArgumentException("JSON map element parsing error: ',' or '}' is absent");
            }
        }
    }

    @Override
    protected String getString() {
        //TODO: add formatted print
        StringBuilder res = new StringBuilder();
        res.append("{");
        boolean first = true;
        for (Map.Entry<String, JsonElement> currentEntry : this.map.entrySet()) {
            if (first) {
                res.append("\n");
                first = false;
            } else {
                res.append(",\n");
            }
            res.append("  \"");
            res.append(currentEntry.getKey());
            res.append("\" : ");
            res.append(currentEntry.getValue().toString());
        }
        res.append("\n}");
        return res.toString();
    }

}
