package converter;

import java.util.Set;

public class XMLElement extends Element{
    //consist of:
    //  name
    //  attributes
    //  content


    XMLElement child;
    XMLElement neighborhood;
    // parent - ? needed?
    Set<Element> attributes;
}
