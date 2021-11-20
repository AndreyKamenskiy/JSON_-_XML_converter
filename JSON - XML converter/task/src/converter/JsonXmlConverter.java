package converter;

import converter.XML.XmlElement;

import java.util.List;
import java.util.Map;

public class JsonXmlConverter {

    public static String toStringAsJson(XmlElement xmlElement) {
        if (xmlElement == null) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        res.append("{\n  \"");
        res.append(xmlElement.getName());
        res.append("\" : ");
        if (xmlElement.hasAttributes()) {
            res.append("{\n");
            for (Map.Entry<String, String> attribute : xmlElement.getAttr().entrySet()) {
                res.append("    \"@");
                res.append(attribute.getKey());
                res.append("\" : \"");
                res.append(attribute.getValue());
                res.append("\",\n");
            }
            res.append("    \"#");
            res.append(xmlElement.getName());
            res.append("\" : ");
            addJsonContent(res, xmlElement);
            res.append("  }\n");
        } else {
            addJsonContent(res, xmlElement);
        }
        res.append("}");
        return res.toString();
    }

    private static void addJsonContent(StringBuilder res, XmlElement xmlElement) {
        if (xmlElement.hasContent() && !xmlElement.getContent().isEmpty()) {
            List<String> content = xmlElement.getContent();
            if (content.size() == 1) {
                res.append("\"");
                res.append(xmlElement.getContent().get(0));
                res.append("\"\n");
            } else {
                res.append("[");
                boolean first = true;
                for (Object currentContent : content.toArray()) {
                    if (first) {
                        first = false;
                    } else {
                        res.append(',');
                    }
                    res.append("\n    ");
                    res.append(currentContent.toString());
                    res.append("\"");
                }
                res.append("\n");
                res.append("  ]");
            }

        } else {
            res.append("null\n");
        }
    }

}
