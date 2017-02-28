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

public class SAXMLParser_Tests1 extends ApplicationTestCase<Application> {
    public SAXMLParser_Tests1() {
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

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
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

        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }

    }

}
