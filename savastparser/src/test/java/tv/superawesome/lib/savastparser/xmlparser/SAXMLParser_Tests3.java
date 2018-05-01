package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class SAXMLParser_Tests3 {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_xml_response_4.xml");
    }

    @Test
    public void testSearchSiblingsAndChildrenOf1 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Error";

        // assert that it's not null
        assertNotNull(document);

        List<Element> errors = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(errors);
        assertEquals(errors.size(), 1);
    }


    @Test
    public void testSearchSiblingsAndChildrenOf2 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Impression";

        // assert that it's not null
        assertNotNull(document);

        List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(impressions);
        assertEquals(impressions.size(), 3);
    }

    @Test
    public void testSearchSiblingsAndChildrenOf3 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Clicks";

        // assert that it's not null
        assertNotNull(document);

        List<Element> clicks = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(clicks);
        assertEquals(clicks.size(), 0);
    }

    @Test
    public void testSearchSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(impressions);
        assertTrue(impressions.size() == 0);
        assertFalse(impressions.size() == 3);

    }

    @Test
    public void testSearchSiblingsAndChildrenOf5 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(impressions);
        assertTrue(impressions.size() == 0);
        assertFalse(impressions.size() == 3);

    }
}
