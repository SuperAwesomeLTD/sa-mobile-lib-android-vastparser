package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

public class SAXMLParser_Tests7  {

    @Test(expected = SAXParseException.class)
    public void testInvalidXML1 () throws Exception {

        String xml = ResourceReader.readResource("mock_error_xml_response.xml");

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidXML2 () throws Exception {

        String xml = null;

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
    }
}
