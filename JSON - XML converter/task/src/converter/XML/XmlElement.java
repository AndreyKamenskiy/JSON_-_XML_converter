package converter.XML;

import converter.JSON.StringIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlElement {
    //consist of:
    //  name
    //  attributes
    //  content
    final private String ERROR_PREFIX = "XML element parsing error: ";

    private String name;
    private Map<String, String> attr = null;
    //TODO: change String to Object or something like ContentType
    //      to take ability to wrap another XmlElement to content
    private List<String> content = null;
    private boolean hasEndTag;

    public XmlElement(StringIndex index) throws IllegalArgumentException {
        if (index == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "null received instead StringIndex");
        }
        loadStartTagAndAttributes(index);
        if (!hasEndTag) {
            return;
        }
        //todo: make content loading more complex. https://www.w3.org/TR/xml/#NT-content
        content = new ArrayList<>();
        this.content.add(loadCharData(index)); //now just CharData content is available
        loadEndTag(index);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttr() {
        return attr;
    }

    public List<String> getContent() {
        return content;
    }

    public boolean hasContent() {
        return hasEndTag && content != null && !content.isEmpty();
    }

    public boolean hasAttributes() {
        return attr != null && !attr.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('<');
        res.append(name);
        if (hasAttributes()) {
            for (Map.Entry<String, String> attribute : attr.entrySet()) {
                res.append(' ');
                res.append(attribute.getKey());
                res.append(' ');
                res.append('=');
                res.append(' ');
                res.append('"');
                res.append(attribute.getValue());
                res.append('"');
            }
            res.append(' ');
        }
        if (hasContent()) {
            res.append(">");
            for (Object s : content) {
                res.append(s.toString());
            }
            res.append(">/");
            res.append(name);
            res.append('>');

        } else {
            res.append("/>");
        }

        return res.toString();
    }

    private void loadStartTagAndAttributes(StringIndex index) throws IllegalArgumentException {
        // https://www.w3.org/TR/xml/#NT-STag
        // 	STag ::= '<' Name (S Attribute)* S? '>'
        checkCurrentChar(index, '<', ERROR_PREFIX);
        index.inc();
        name = loadName(index);
        skipSChars(index);
        char ch = index.getCurrentChar();
        if (ch != '/' && ch != '>') {
            // load attributes
            loadAttributes(index);
            ch = index.getCurrentChar();
        }
        if (ch == '>') {
            hasEndTag = true;
            index.inc();
        } else if (ch == '/') {
            //load '>'
            index.inc();
            checkCurrentChar(index, '>', ERROR_PREFIX);
            index.inc();
            hasEndTag = false;
        } else {
            throw new IllegalArgumentException(ERROR_PREFIX
                    + "expected '/' or '>' but received '"
                    + ch + "'");
        }
    }

    private void loadEndTag(StringIndex index) throws IllegalArgumentException {
        // end tag format ETag ::= '</' Name S? '>'
        // https://www.w3.org/TR/xml/#sec-starttags
        //index must point to '<'
        checkCurrentChar(index, '<', ERROR_PREFIX);
        index.inc();
        checkCurrentChar(index, '/', ERROR_PREFIX);
        index.inc();
        for (char ch : name.toCharArray()) {
            checkCurrentChar(index, ch, ERROR_PREFIX + "Start tag and End tag names does not equals. ");
            index.inc();
        }
        skipSChars(index);
        checkCurrentChar(index, '>', ERROR_PREFIX);
    }

    private void loadAttributes(StringIndex index) throws IllegalArgumentException {
        // https://www.w3.org/TR/xml/#NT-Attribute
        // 	Attribute	   ::=   	Name Eq AttValue
        // index point to first char of Name
        attr = new HashMap<>();
        while ("/>".indexOf(index.getCurrentChar()) == -1) {
            String attrName = loadName(index);
            // https://www.w3.org/TR/xml/#NT-Eq
            // Eq	   ::=   	S? '=' S?
            skipSChars(index);
            checkCurrentChar(index, '=', ERROR_PREFIX);
            index.inc();
            skipSChars(index);
            String attrValue = loadAttrValue(index);
            attr.put(attrName, attrValue);
        }
    }

    private String loadAttrValue(StringIndex index) throws IllegalArgumentException {
        // https://www.w3.org/TR/xml/#NT-AttValue
        // AttValue	   ::=   	'"' ([^<&"] | Reference)* '"'
        // the index must point to the first AttrValue char
        //TODO: add Reference providing.
        checkCurrentChar(index, '"', ERROR_PREFIX);
        index.inc();
        StringBuilder attrValue = new StringBuilder();
        while ("<&\"".indexOf(index.getCurrentChar()) == -1) {
            attrValue.append(index.getCurrentChar());
            index.inc();
        }
        checkCurrentChar(index, '"', ERROR_PREFIX);
        index.inc();
        skipSChars(index);
        return attrValue.toString();
    }

    private String loadCharData(StringIndex index) {
        //https://www.w3.org/TR/xml/#NT-CharData
        // CharData	   ::=   	[^<&]* - ([^<&]* ']]>' [^<&]*)
        StringBuilder charData = new StringBuilder();
        while ("<&".indexOf(index.getCurrentChar()) == -1) {
            charData.append(index.getCurrentChar());
            index.inc();
        }
        checkCurrentChar(index, '<', ERROR_PREFIX);
        return charData.toString();
    }

    private void skipSChars(StringIndex index) {
        while (isSpaceChar(index.getCurrentChar())) {
            index.inc();
        }
    }

    private String loadName(StringIndex index) throws IllegalArgumentException {
        //https://www.w3.org/TR/xml/#NT-Name
        // 	Name	   ::=   	NameStartChar (NameChar)*
        char ch = index.getNextNotSpaceChar();
        if (!isNameStartChar(ch)) {
            throw new IllegalArgumentException(ERROR_PREFIX
                    + "element's name can not starts with '" + ch + "'");
        }
        StringBuilder name = new StringBuilder();
        name.append(ch);
        index.inc();
        while (true) {
            ch = index.getCurrentChar();
            if (isNameChar(ch)) {
                name.append(ch);
                index.inc();
            } else {
                break;
            }
        }
        return name.toString();
    }

    private boolean isSpaceChar(char ch) {
        //from here https://www.w3.org/TR/xml/#NT-S
        // S ::=  	(#x20 | #x9 | #xD | #xA)+
        // 0x20=' '   0x9='\t'  0xD='\r'  0xA='\n'
        return ch == 0x20 || ch == 0x9 || ch == 0xD || ch == 0xA;
    }

    private boolean isNameStartChar(char ch) {
        // from here https://www.w3.org/TR/xml/#charencoding
        //NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF]
        // | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F]
        // | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD]
        // | [#x10000-#xEFFFF]
        return (ch == ':') || (ch == '_') ||
                (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') ||
                (ch >= 0xC0 && ch <= 0xD6) || (ch >= 0xD8 && ch <= 0xF6) ||
                (ch >= 0xF8 && ch <= 0x2FF) || (ch >= 0x2C00 && ch <= 0x2FEF) ||
                (ch >= 0x3001 && ch <= 0xD7FF) || (ch >= 0xF900 && ch <= 0xFDCF) ||
                (ch >= 0xFDF0 && ch <= 0xFFFD);
    }

    private boolean isNameChar(char ch) {
        // from here https://www.w3.org/TR/xml/#charencoding
        // NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7
        // | [#x0300-#x036F] | [#x203F-#x2040]
        return (isNameStartChar(ch) || (ch == '-') || (ch == '.') || (ch == 0xB7) ||
                (ch >= '0' && ch <= '9') || (ch >= 0x0300 && ch <= 0x036F) ||
                (ch >= 0x203F && ch <= 0x2040));
    }

    private void checkCurrentChar(StringIndex index, char ch, String errorPrefix) throws IllegalArgumentException {
        if (index.getCurrentChar() != ch) {
            throw new IllegalArgumentException(errorPrefix + "illegal character detected. Was needed '" + ch +
                    "' but received '" + index.getCurrentChar() + "'");
        }
    }
}
