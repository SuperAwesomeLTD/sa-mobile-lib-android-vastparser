package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class SAXMLParser_Tests6 {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_xml_response_2.xml");
    }

    @Test
    public void testCheckSiblingsAndChildrenOf1 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Error";

        // assert that it's not null
        assertNotNull(document);

        boolean errorExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertTrue(errorExists);
    }

    @Test
    public void testCheckSiblingsAndChildrenOf2 () throws Exception{

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Impression";

        // assert that it's not null
        assertNotNull(document);

        boolean impressionExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertTrue(impressionExists);
    }

    @Test
    public void testCheckSiblingsAndChildrenOf3 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Click";

        // assert that it's not null
        assertNotNull(document);

        boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertFalse(clickExists);
    }

    @Test
    public void testCheckSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertFalse(clickExists);
    }

    @Test
    public void testCheckSiblingsAndChildrenOf5 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertFalse(clickExists);
    }
}
