package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAXMLParser_Tests4 extends ApplicationTestCase<Application> {

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

    public SAXMLParser_Tests4() {
        super(Application.class);
    }

    @SmallTest
    public void testFindFirstInstanceInSiblingsAndChildrenOf1 () {

        try {
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

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testFindFirstInstanceInSiblingsAndChildrenOf2 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Click";

            // assert that it's not null
            assertNotNull(document);

            Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
            assertNull(firstClick);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }
    }

    @SmallTest
    public void testFindFirstInstanceInSiblingsAndChildrenOf3 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstClick);
    }

    @SmallTest
    public void testFindFirstInstanceInSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstClick);
    }
}
