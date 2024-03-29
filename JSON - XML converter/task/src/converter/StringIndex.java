package converter;

public class StringIndex {
    private int index;
    private final String text;

    public String getString() {
        return text;
    }

    public char getCurrentChar() throws IllegalArgumentException{
        if (index >= text.length()) {
            throw new IllegalArgumentException("Unexpected end of string found");
        }
        return text.charAt(index);
    }

    public StringIndex(int inputIndex, String inputText) {
        if (inputIndex < 0 || inputIndex >= inputText.length()) {
            throw new IllegalArgumentException("StringIndex error: index must be in range [0..str.length]");
        }
        index = inputIndex;
        text = inputText;
    }

    public int getIndex() {
        return index;
    }

    public void inc(){
        ++index;
    }

    public void skipSpaces() throws IllegalArgumentException {
        while (true) {
            if (index == this.text.length()) {
                throw new IllegalArgumentException("JSON map element parsing error: unexpected end of the string");
            }
            if (!isSpaceChar(this.text.charAt(index))) {
                return;
            }
            ++index;
        }
    }

    public char getNextNotSpaceChar() throws IllegalArgumentException {
        skipSpaces();
        return this.text.charAt(index);
    }

    static boolean isSpaceChar(char ch) {
        return " \t\n\r".indexOf(ch) != -1;
    }

    public void checkCurrentChar(char ch, String errorPrefix) throws IllegalArgumentException {
        if (getCurrentChar() != ch) {
            throw new IllegalArgumentException(errorPrefix + "illegal character detected. Was needed '" + ch +
                    "' but received '" + getCurrentChar() + "'");
        }
    }

    public boolean hasChar() {
        return index < text.length();
    }
}
