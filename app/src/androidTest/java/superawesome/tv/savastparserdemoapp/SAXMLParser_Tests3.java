package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAXMLParser_Tests3 extends ApplicationTestCase<Application> {

    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<VAST version=\"3.0\">\n" +
            "   <Ad id=\"undefined\">\n" +
            "      <InLine>\n" +
            "         <AdSystem version=\"1.0\">AwesomeAds</AdSystem>\n" +
            "         <AdTitle><![CDATA[REX fake exchange creative]]></AdTitle>\n" +
            "         <Description />\n" +
            "         <Error><![CDATA[https://ads.staging.superawesome.tv/]]></Error>\n" +
            "         <Impression id=\"23\"><![CDATA[https://ads.staging.superawesome.tv/v2/impr1/]]></Impression>\n" +
            "\t\t\t\t <Impression id=\"23\"><![CDATA[https://ads.staging.superawesome.tv/v2/impr2/]]></Impression>\n" +
            "\t\t\t\t <Impression id=\"23\"><![CDATA[https://ads.staging.superawesome.tv/v2/impr3/]]></Impression>\n" +
            "      </InLine>\n" +
            "   </Ad>\n" +
            "</VAST>\n";

    public SAXMLParser_Tests3() {
        super(Application.class);
    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf1 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Error";

            // assert that it's not null
            assertNotNull(document);

            List<Element> errors = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

            assertNotNull(errors);
            assertEquals(errors.size(), 1);


        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf2 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Impression";

            // assert that it's not null
            assertNotNull(document);

            List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

            assertNotNull(impressions);
            assertEquals(impressions.size(), 3);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf3 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Clicks";

            // assert that it's not null
            assertNotNull(document);

            List<Element> clicks = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

            assertNotNull(clicks);
            assertEquals(clicks.size(), 0);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, tag);

        assertNotNull(impressions);
        assertTrue(impressions.size() == 0);
        assertFalse(impressions.size() == 3);

    }

    @SmallTest
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
