
package converter;

import converter.JSON.JsonElement;
import converter.JSON.JsonElementLoader;
import converter.XML.XmlElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File inputFile = new File("test.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String input = scanner.useDelimiter("\\z").next();
        scanner.close();
        StringIndex i = new StringIndex(0, input);
        String res = "";
        if (i.getNextNotSpaceChar() == '{') {
            //json
            JsonElement jsonElement = new JsonElementLoader(input).getElement();
            res = JsonXmlConverter.toStringAsXML(jsonElement);
        } else if (i.getCurrentChar() == '<') {
            //xml
            XmlElement xmlElement = new XmlElement(new StringIndex(0,input));
            res = JsonXmlConverter.toStringAsJson(xmlElement);
        }
        System.out.println(res);
    }
}
