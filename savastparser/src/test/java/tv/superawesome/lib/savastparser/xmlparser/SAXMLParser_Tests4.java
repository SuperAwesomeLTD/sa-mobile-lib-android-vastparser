package tv.superawesome.lib.savastparser.xmlparser;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class SAXMLParser_Tests4 {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_xml_response_2.xml");
    }

    @Test
    public void testFindFirstInstanceInSiblingsAndChildrenOf1 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Impression";

        // assert that it's not null
        assertNotNull(document);

        Element firstImpression = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNotNull(firstImpression);

        String firstImpressionUrl = firstImpression.getTextContent();
        String expected_firstImpressionUrl = "https://ads.staging.superawesome.tv/v2/impr1/";

        assertNotNull(firstImpressionUrl);
        assertEquals(expected_firstImpressionUrl, firstImpressionUrl);
    }

    @Test
    public void testFindFirstInstanceInSiblingsAndChildrenOf2 () throws Exception {

        // parse the XML document
        Document document = SAXMLParser.parseXML(xml);
        String tag = "Click";

        // assert that it's not null
        assertNotNull(document);

        Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstClick);
    }

    @Test
    public void testFindFirstInstanceInSiblingsAndChildrenOf3 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstClick);
    }

    @Test
    public void testFindFirstInstanceInSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstClick);
    }
}
