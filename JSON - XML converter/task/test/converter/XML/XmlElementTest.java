package converter.XML;

import converter.JSON.StringIndex;
import junit.framework.TestCase;

public class XmlElementTest extends TestCase {

    public void testTestGetName() {
        String xml = "<name>content and content</name >";
        StringIndex index = new StringIndex(0, xml);
        XmlElement element = new XmlElement(index);
        assert "name".equals(element.getName());
        assert element.hasContent();
        assert "content and content".equals(element.getContent().get(1));
        assert !element.hasAttributes();
    }

    public void testGetAttr() {
    }

    public void testGetContent() {
    }

    public void testHasContent() {
    }
}