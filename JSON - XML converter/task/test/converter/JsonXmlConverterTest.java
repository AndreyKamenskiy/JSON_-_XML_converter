package converter;

import converter.JSON.JsonElement;
import converter.JSON.JsonElementLoader;
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

    public void checkJSONtoXML(String jsonIn, String xmlOut) {
        JsonElement jsonElement = new JsonElementLoader(jsonIn).getElement();
        String toXml = JsonXmlConverter.toStringAsXML(jsonElement);
        System.out.println(toXml);
        assert xmlOut.equals(toXml);
    }

    public void twoDimensionConverting(String jsonOrXml) {
        StringIndex i = new StringIndex(0, jsonOrXml);
        if (i.getNextNotSpaceChar() == '{') {
            //json
            JsonElement jsonElement = new JsonElementLoader(jsonOrXml).getElement();
            String jsonString = jsonElement.toString();
            System.out.println(jsonString);
            String xmlString = JsonXmlConverter.toStringAsXML(jsonElement);
            twoDimensionConverting(xmlString);
        } else if (i.getCurrentChar() == '<') {
            //xml
            XmlElement xmlElement = new XmlElement(new StringIndex(0,jsonOrXml));
            String xmlString = xmlElement.toString();
            String jsonString = JsonXmlConverter.toStringAsJson(xmlElement);
            JsonElement jsonElement = new JsonElementLoader(jsonString).getElement();
            String jsonToXml = JsonXmlConverter.toStringAsXML(jsonElement);
            System.out.println();
            System.out.println(xmlString);
            System.out.println(jsonString);
            assert xmlString.equals(jsonToXml);
        }
    }

    public void test_toStringAsXml() {
        checkJSONtoXML("{\"jdk\" : \"1.8.9\"}", "<jdk>1.8.9</jdk>");
        checkJSONtoXML("{\n" + "    \"employee\" : {\n" + "        \"@department\" : \"manager\",\n" +
                        "        \"#employee\" : \"Garry Smith\"\n" + "    }\n" + "}",
                "<employee department = \"manager\">Garry Smith</employee>");
        checkJSONtoXML("{\n" +
                "    \"person\" : {\n" +
                "        \"@rate\" : 1,\n" +
                "        \"@name\" : \"Torvalds\",\n" +
                "        \"#person\" : null\n" +
                "    }\n" +
                "}", "<person rate = \"1\" name = \"Torvalds\" />");

        checkJSONtoXML("{\"name\" : null }", "<name />");
        checkJSONtoXML("{\"name\" : 1 }", "<name>1</name>");
        checkJSONtoXML("{\"name\" : \"\" }", "<name></name>");
    }

    public void test_twoDimensions() {
        twoDimensionConverting("<name/>");
        twoDimensionConverting("{\"qwe\":\tnull}");
        twoDimensionConverting("<qwerty at = \"1234\" name = \"kira\">KiraNa</qwerty>");
        twoDimensionConverting("<qwerty at = \"1234\" name = \"kira\">\"KiraNa\"</qwerty>");
        twoDimensionConverting("{\n" +
                "  \"qwerty\" : {\n" +
                "    \"@str\" : \"1234\",\n" +
                "    \"@null\" : null,\n" +
                "    \"@num\" : 123E55,\n" +
                "    \"#qwerty\" : \"\\\"KiraNa\\\"\"\n" +
                "  }\n" +
                "}");
        twoDimensionConverting("{\n" +
                "  \"qwerty\" : {\n" +
                "    \"@str\" : \"1234\",\n" +
                "    \"@null\" : null,\n" +
                "    \"@num\" : 123E55,\n" +
                "    \"#qwerty\" : 50e5\n" +
                "  }\n" +
                "}");

    }



}