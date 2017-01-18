package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAXMLParser_Tests extends ApplicationTestCase<Application> {
    public SAXMLParser_Tests() {
        super(Application.class);
    }

    @SmallTest
    public void testXMLParsing1 () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<VAST version=\"3.0\">\n" +
                "   <Ad id=\"undefined\">\n" +
                "      \n" +
                "   </Ad>\n" +
                "</VAST>";

        try {

            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }
    }

    @SmallTest
    public void testXMLParsing2 () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<VAST version=\"3.0\">\n" +
                "   <Ad id=\"undefined\">\n" +
                "      \n" +
                "   </Ad>\n" +
                "</VAST>";

        try {
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

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf1 () {

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

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            List<Element> errors = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Error", errors);

            assertNotNull(errors);
            assertEquals(errors.size(), 1);

            List<Element> impressions = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Impression", impressions);

            assertNotNull(impressions);
            assertEquals(impressions.size(), 3);

            List<Element> clicks = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Clicks", clicks);

            assertNotNull(clicks);
            assertEquals(clicks.size(), 0);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf2 () {

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

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            List<Element> errors = SAXMLParser.searchSiblingsAndChildrenOf(document, "Error");

            assertNotNull(errors);
            assertEquals(errors.size(), 1);

            List<Element> impressions = SAXMLParser.searchSiblingsAndChildrenOf(document, "Impression");

            assertNotNull(impressions);
            assertEquals(impressions.size(), 3);

            List<Element> clicks = SAXMLParser.searchSiblingsAndChildrenOf(document, "Clicks");

            assertNotNull(clicks);
            assertEquals(clicks.size(), 0);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testSearchSiblingsAndChildrenOf3 () {

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

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            final List<Element> errors = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Error", new SAXMLParser.SAXMLIterator() {
                @Override
                public void saDidFindXMLElement(Element e) {
                    assertNotNull(e);
                    errors.add(e);
                }
            });

            assertNotNull(errors);
            assertEquals(errors.size(), 1);

            final List<Element> impressions = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Impression", new SAXMLParser.SAXMLIterator() {
                @Override
                public void saDidFindXMLElement(Element e) {
                    assertNotNull(e);
                    impressions.add(e);
                }
            });

            assertNotNull(impressions);
            assertEquals(impressions.size(), 3);

            final List<Element> clicks = new ArrayList<>();
            SAXMLParser.searchSiblingsAndChildrenOf(document, "Click", new SAXMLParser.SAXMLIterator() {
                @Override
                public void saDidFindXMLElement(Element e) {
                    assertNotNull(e);
                    clicks.add(e);
                }
            });

            assertNotNull(clicks);
            assertEquals(clicks.size(), 0);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testFindFirstInstanceInSiblingsAndChildrenOf () {

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

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            Element firstImpression = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "Impression");
            assertNotNull(firstImpression);

            String firstImpressionUrl = firstImpression.getTextContent();
            String expected_firstImpressionUrl = "https://ads.staging.superawesome.tv/v2/impr1/";

            assertNotNull(firstImpressionUrl);
            assertEquals(expected_firstImpressionUrl, firstImpressionUrl);

            Element firstClick = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "click");
            assertNull(firstClick);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testCheckSiblingsAndChildrenOf () {

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

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            boolean errorExists = SAXMLParser.checkSiblingsAndChildrenOf(document, "Error");
            assertTrue(errorExists);

            boolean impressionExists = SAXMLParser.checkSiblingsAndChildrenOf(document, "Impression");
            assertTrue(impressionExists);

            boolean clickExists = SAXMLParser.checkSiblingsAndChildrenOf(document, "Click");
            assertFalse(clickExists);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            fail();
        }

    }

    @SmallTest
    public void testInvalidXML () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<VAST version=\"3.0\">\n" +
                "   <Ad id=\"undefined\">\n" +
                "      <InLine>\n" +
                "         <AdSystem version=\"1.0\">AwesomeAds</AdSystem>\n" +
                "         <AdTitle><![CDATA[REX fake exchange creative]]></AdTitle>\n" +
                "         <Description />\n" +
                "         <Error><![CDATA[https://ads.\n";

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // technically it should not reach this point to assert the not-nullness of an
            // invalid Document object, because it'll be caught as an exception
            assertNotNull(document);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            // do nothing
        }
    }

}
