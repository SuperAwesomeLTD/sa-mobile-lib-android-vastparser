package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.savastparser.SAXMLParser;


public class SAXMLParser_Tests6 extends ApplicationTestCase<Application> {

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

    public SAXMLParser_Tests6() {
        super(Application.class);
    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf1 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Error";

            // assert that it's not null
            assertNotNull(document);

            boolean errorExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
            assertTrue(errorExists);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf2 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Impression";

            // assert that it's not null
            assertNotNull(document);

            boolean impressionExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
            assertTrue(impressionExists);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf3 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);
            String tag = "Click";

            // assert that it's not null
            assertNotNull(document);

            boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
            assertFalse(clickExists);

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf4 () {

        // parse the XML document
        Document document = null;
        String tag = "Impression";

        boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertFalse(clickExists);
    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf5 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, tag);
        assertFalse(clickExists);
    }
}
