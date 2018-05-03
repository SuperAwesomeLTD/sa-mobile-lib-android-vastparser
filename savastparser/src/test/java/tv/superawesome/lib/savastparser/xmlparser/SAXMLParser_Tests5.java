package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class SAXMLParser_Tests5 {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_xml_response_2.xml");
    }

    @Test
    public void testSearchSiblingsAndChildrenOf1 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Error";

        // assert that it's not null
        assertNotNull(document);

        final List<Element> errors = new ArrayList<>();
        SAXMLParser.searchSiblingsAndChildrenOf(document, tag, new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                assertNotNull(e);
                errors.add(e);
            }
        });

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

        final List<Element> impressions = new ArrayList<>();
        SAXMLParser.searchSiblingsAndChildrenOf(document, tag, new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                assertNotNull(e);
                impressions.add(e);
            }
        });

        assertNotNull(impressions);
        assertEquals(impressions.size(), 3);
    }

    @Test
    public void testSearchSiblingsAndChildrenOf3 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Click";

        // assert that it's not null
        assertNotNull(document);

        final List<Element> clicks = new ArrayList<>();
        SAXMLParser.searchSiblingsAndChildrenOf(document, tag, new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                assertNotNull(e);
                clicks.add(e);
            }
        });

        assertNotNull(clicks);
        assertEquals(clicks.size(), 0);
    }

    @Test
    public void testSearchSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        final List<Element> impressions = new ArrayList<>();
        SAXMLParser.searchSiblingsAndChildrenOf(document, tag, new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                assertNotNull(e);
                impressions.add(e);
            }
        });

        assertNotNull(impressions);
        assertTrue(impressions.size() == 0);
        assertFalse(impressions.size() == 3);

    }

    @Test
    public void testSearchSiblingsAndChildrenOf5 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        final List<Element> impressions = new ArrayList<>();
        SAXMLParser.searchSiblingsAndChildrenOf(document, tag, new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                assertNotNull(e);
                impressions.add(e);
            }
        });

        assertNotNull(impressions);
        assertTrue(impressions.size() == 0);
        assertFalse(impressions.size() == 3);

    }
}
