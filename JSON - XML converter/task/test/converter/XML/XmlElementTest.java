package converter.XML;

import converter.JSON.StringIndex;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class XmlElementTest extends TestCase {

    public void testTestGetName() {
        {
            String xml = "<name>content and content</name >";
            StringIndex index = new StringIndex(0, xml);
            XmlElement element = new XmlElement(index);
            assert "name".equals(element.getName());
            assert element.hasContent();
            assert "content and content".equals(element.getContent().get(0));
            assert !element.hasAttributes();
        }
        {
            String xml = "<name1/>";
            StringIndex index = new StringIndex(0, xml);
            XmlElement element = new XmlElement(index);
            assert "name1".equals(element.getName());
            assert !element.hasContent();
            assert element.getContent() == null;
            assert !element.hasAttributes();
            assert element.getAttr() == null;
        }
        {
            String xml = "<:_⼒./>";
            StringIndex index = new StringIndex(0, xml);
            XmlElement element = new XmlElement(index);
            assert ":_⼒.".equals(element.getName());
            assert !element.hasContent();
            assert element.getContent() == null;
            assert !element.hasAttributes();
            assert element.getAttr() == null;
        }

    }

    public void testGetAttr() {
        {
            String xml = "<qwerty attr1= \"5\" attr2 =\"6#%!\" \n\t attr3\t=\n\"\n\t1\"/>";
            StringIndex index = new StringIndex(0, xml);
            XmlElement element = new XmlElement(index);
            assert "qwerty".equals(element.getName());
            assert !element.hasContent();
            assert element.getContent() == null;
            assert element.hasAttributes();
            assert element.getAttr().size() == 3;
            Map<String, String> map = element.getAttr();
            assert map.containsKey("attr1");
            assert "5".equals(map.get("attr1"));
            assert map.containsKey("attr2");
            assert "6#%!".equals(map.get("attr2"));
            assert map.containsKey("attr3");
            assert "\n\t1".equals(map.get("attr3"));

        }
    }

    public void test_toString() {
        {
            String xml = "<name:1 a  = \"qwe\" b \t\n=   \"___111\"/>";
            StringIndex index = new StringIndex(0, xml);
            XmlElement element = new XmlElement(index);
            assert "name:1".equals(element.getName());
            assert !element.hasContent();
            assert element.getContent() == null;
            assert element.hasAttributes();
            assert element.getAttr().size() == 2;
//            System.out.println(element.toString());
            assert "<name:1 a = \"qwe\" b = \"___111\" />".equals(element.toString());
        }

    }

    public void testHasContent() {
    }
}