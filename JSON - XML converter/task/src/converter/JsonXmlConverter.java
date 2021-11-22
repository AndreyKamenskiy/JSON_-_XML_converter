package converter;

import converter.JSON.JsonElement;
import converter.JSON.JsonElementType;
import converter.JSON.JsonMap;
import converter.JSON.JsonString;
import converter.XML.XmlElement;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static String toStringAsXML(JsonElement element) throws IllegalArgumentException {
        //todo: make smaller!!!
        final String error_prefix = "JSON to XML converting error: ";
        if (element == null) {
            return "";
        }
        if (element.getType() != JsonElementType.MAP) {
            throw new IllegalArgumentException(error_prefix + "element must be the MAP type");
        }
        Map<String, JsonElement> map = ((JsonMap) element).getMap();
        if (map.size() != 1) {
            throw new IllegalArgumentException(error_prefix + "more than one element in the map");
        }
        String xmlElementName = map.keySet().iterator().next();
        Map<String, String> attr = null;
        List<String> content = null;
        JsonElement innerElement = map.get(xmlElementName);
        JsonElementType innerType = innerElement.getType();
        boolean hasContent = false;
        boolean hasAttr = false;
        if (innerType == JsonElementType.STRING) {
            hasContent = true;
            content = new ArrayList<>();
            content.add(((JsonString)innerElement).getValue());
        } else if (innerType == JsonElementType.MAP) {
            Map<String, JsonElement> innerMap = ((JsonMap)innerElement).getMap();
            String contentKeyName = "#" + xmlElementName;
            for (Map.Entry<String, JsonElement> currentAttr : innerMap.entrySet()) {
                if (currentAttr.getKey().startsWith(contentKeyName)) {
                    if (currentAttr.getValue().getType() == JsonElementType.STRING) {
                        hasContent = true;
                        content = new ArrayList<>();
                        content.add(((JsonString)currentAttr.getValue()).getValue());
                    } else if (currentAttr.getValue().getType() == JsonElementType.NULL) {
                        //do nothing
                    } else if (currentAttr.getValue().getType() == JsonElementType.NUMBER) {
                        content = new ArrayList<>();
                        content.add(currentAttr.getValue().toString());
                    } else {
                        throw new IllegalArgumentException(error_prefix
                                + "now accepts only string, null or number type content");
                    }
                } else if (currentAttr.getKey().startsWith("@")) {
                    if (!hasAttr) {
                        hasAttr = true;
                        attr = new HashMap<>();
                    }
                    String attrName = currentAttr.getKey().substring(1);
                    String attrValue = "";
                    JsonElement attrValueElement = currentAttr.getValue();
                    JsonElementType attrValueType = attrValueElement.getType();
                    if (attrValueType == JsonElementType.STRING) {
                        attrValue = ((JsonString) attrValueElement).getValue();
                    } else if (attrValueType == JsonElementType.NULL ||
                            attrValueType == JsonElementType.NUMBER) {
                        attrValue = attrValueElement.toString();
                    } else {
                        throw new IllegalArgumentException(error_prefix + "illegal type of the attribute value");
                    }
                    attr.put(attrName, attrValue);
                } else {
                    throw new IllegalArgumentException(error_prefix + "unknown key type.");
                }
            }
        } else if (innerType == JsonElementType.NULL) {
        } else  if (innerType == JsonElementType.NUMBER) {
            hasContent = true;
            content = new ArrayList<>();
            content.add(innerElement.toString());
        } else {
            throw new IllegalArgumentException(error_prefix + "illegal inner element type");
        }
        return new XmlElement(xmlElementName, attr, content).toString();
    }

    private static void addJsonContent(StringBuilder res, XmlElement xmlElement) {
        if (xmlElement.hasContent() && !xmlElement.getContent().isEmpty()) {
            List<String> content = xmlElement.getContent();
            if (content.size() == 1) {
                res.append(JsonString.toJsonString(xmlElement.getContent().get(0)));
                res.append("\n");
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
