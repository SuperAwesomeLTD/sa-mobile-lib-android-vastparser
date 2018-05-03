package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SAXMLParser_Tests1 {

    @Test
    public void testXMLParsing1 () throws Exception {

        String xml = ResourceReader.readResource("mock_xml_response_1.xml");

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);

        // assert that it's not null
        assertNotNull(document);
    }

    @Test
    public void testXMLParsing2 () throws Exception {

        String xml = ResourceReader.readResource("mock_xml_response_1.xml");

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);

        // assert that it's not null
        assertNotNull(document);

        // get a list of all elements of type "VAST"
        NodeList VASTList = document.getElementsByTagName("VAST");

        // assert it's not null and it has just one element
        assertNotNull(VASTList);
        assertEquals(VASTList.getLength(), 1);

        // get the only element and assert it's not null
        Element VAST = (Element) VASTList.item(0);
        assertNotNull(VAST);

        // get a list of all elements of type "Ad"
        NodeList AdList = VAST.getElementsByTagName("Ad");

        // assert it's not null and it has just one element
        assertNotNull(AdList);
        assertEquals(AdList.getLength(), 1);

        // get the only element and assert it's not null
        Element Ad = (Element) AdList.item(0);
        assertNotNull(Ad);
    }

}
