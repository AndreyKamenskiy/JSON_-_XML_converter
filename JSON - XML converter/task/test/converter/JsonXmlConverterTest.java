package converter;

import converter.XML.XmlElement;
import junit.framework.TestCase;

public class JsonXmlConverterTest extends TestCase {

    public void test_toStringAsJson() {
        {
            String json = JsonXmlConverter.toStringAsJson(null);
            assert "".equals(json);
        }
        {
            String xmlString = "<employee department = \"manager\">Garry Smith</employee>";
            StringIndex index = new StringIndex(0, xmlString);
            XmlElement xmlElement = new XmlElement(index);
            String json = JsonXmlConverter.toStringAsJson(xmlElement);
            System.out.println(json);
            String correctAnswer = "{\n" +
                    "  \"employee\" : {\n" +
                    "    \"@department\" : \"manager\",\n" +
                    "    \"#employee\" : \"Garry Smith\"\n" +
                    "  }\n" +
                    "}";
            assert correctAnswer.equals(json);
        }
        {
            String xmlString = "<person rate = \"1\" name = \"Torvalds\" />";
            StringIndex index = new StringIndex(0, xmlString);
            XmlElement xmlElement = new XmlElement(index);
            String json = JsonXmlConverter.toStringAsJson(xmlElement);
            System.out.println(json);
            String correctAnswer = "{\n" +
                    "  \"person\" : {\n" +
                    "    \"@rate\" : \"1\",\n" +
                    "    \"@name\" : \"Torvalds\",\n" +
                    "    \"#person\" : null\n" +
                    "  }\n" +
                    "}";
            assert correctAnswer.equals(json);
        }
        {
            String xmlString = "<host>127.0.0.1</host>";
            StringIndex index = new StringIndex(0, xmlString);
            XmlElement xmlElement = new XmlElement(index);
            String json = JsonXmlConverter.toStringAsJson(xmlElement);
            System.out.println(json);
            String correctAnswer = "{\n" +
                    "  \"host\" : \"127.0.0.1\"\n" +
                    "}";
            assert correctAnswer.equals(json);
        }


    }
}