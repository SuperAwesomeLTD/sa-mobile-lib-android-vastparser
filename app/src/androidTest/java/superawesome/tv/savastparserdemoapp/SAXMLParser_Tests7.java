package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAXMLParser_Tests7 extends ApplicationTestCase<Application> {
    public SAXMLParser_Tests7() {
        super(Application.class);
    }

    @SmallTest
    public void testInvalidXML1 () {

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
        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            // do nothing
        }
    }

    @SmallTest
    public void testInvalidXML2 () {

        String xml = null;

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // technically it should not reach this point to assert the not-nullness of an
            // invalid Document object, because it'll be caught as an exception
            assertNotNull(document);
        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            // do nothing
        }
    }
}
