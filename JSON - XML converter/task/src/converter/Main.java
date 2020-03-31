
package converter;

import java.util.Scanner;

public class Main {

    static String convertJSONToXML(String input) {
        StringBuilder result = new StringBuilder();

        final String HAS_NULL = "\\s*\\{\\s*\".+\"\\s*:\\s*null\\s*}\\s*";
        final String KEY_VALUE_JSON_ELEMENT = "\\s*\\{\\s*\"[^\"]+\"\\s*:\\s*\"[^\"]+\"\\s*}\\s*";

        String value;
        String key;
        if (input.matches(HAS_NULL)) {
            key = input.replaceFirst("\\s*\\{\\s*\"","")
                    .replaceFirst("\\s*\".+","").trim();
            result.append("<");
            result.append(key);
            result.append("/>");
        } else if (input.matches(KEY_VALUE_JSON_ELEMENT)) {
            key = input.replaceFirst("\\s*\\{\\s*\"","")
                    .replaceFirst("\\s*\".+","").trim();
            value = input.replaceFirst("\\s*\\{\\s*\"[^\"]+\"\\s*:\\s*\"","")
                    .replaceFirst("\".+","");

            result.append("<");
            result.append(key);
            result.append(">");
            result.append(value);
            result.append("</");
            result.append(key);
            result.append(">");
        }
        return result.toString();
    }

    static String convertXMLToJSON(String input) {
        StringBuilder result = new StringBuilder();

        final String SINGLE_ELEMENT = "\\s*<.*/>\\s*";
        final String DOUBLE_ELEMENT = "\\s*<.+>.*<\\s*/.+>\\s*";

        String value = "";
        String key = "";
        if (input.matches(SINGLE_ELEMENT)) {
            key = input.replace("<","").replaceAll("/\\s*>\\s*","").trim();
            value = "null";
        } else if (input.matches(DOUBLE_ELEMENT)) {
            key = input.replaceAll(">.*<\\s*/.+>\\s*","").replaceFirst("<\\s*","");
            value = '"' + input.replaceAll("<[^>]*>", "") + '"';
        }

        result.append("{ \"");
        result.append(key);
        result.append("\" : ");
        result.append(value);
        result.append("}");
/*
        String lookFor = "<? ?>";
        int pointer = 0;
        boolean parse = true;

        while (parse) {
            switch (lookFor) {
                case "<? ?>":
                    if (input.matches("\\s*<\\s*?.+?\\s*>\\s*<.+>\\s*")){
                        while (input.charAt(pointer) != '<'){
                            if (input.charAt(pointer) != ' ') {
                                //TODO Change to " \n\t".indexOf(char) >=0
                                System.out.printf("\n input=%s pointer=%d, char=%s \n"
                                        ,input, pointer, input.charAt(pointer));
                                System.exit(1); // error
                            }
                            pointer++;
                        }
                        while (input.charAt(pointer) != '?'){
                            if (input.charAt(pointer) != ' ') {
                                //TODO Change to " \n\t".indexOf(char) >=0
                                System.out.printf("\n input=%s pointer=%d, char=%s \n"
                                        ,input, pointer, input.charAt(pointer));
                                System.exit(1); // error
                            }
                            pointer++;
                        }


                    } else {
                        lookFor = "<";
                    }                case "<":
                    if (ch)
                        break;


            }


        }
        for (char ch : input.toCharArray()) {



        }
        */
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Check for format JSON or XML
        final String JSON_FORMAT = "\\s*\\{.+}\\s*";
        //final String XML_FORMAT = "\\s*\".+\"\\s*";///????
        final String XML_FORMAT = "\\s*<.+>\\s*";

        String result = "";

        if (input.matches(JSON_FORMAT)) {
            result = convertJSONToXML(input);
        } else if (input.matches(XML_FORMAT)) {
            result = convertXMLToJSON(input);
        }
        System.out.println(result);

    }
}
