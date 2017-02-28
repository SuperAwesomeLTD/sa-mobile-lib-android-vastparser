package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.samodelspace.vastad.SAVASTAd;
import tv.superawesome.lib.samodelspace.vastad.SAVASTAdType;
import tv.superawesome.lib.samodelspace.vastad.SAVASTMedia;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAVASTParser_Local_Tests1 extends ApplicationTestCase<Application> {

    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<MediaFiles>\n" +
            "   <MediaFile type=\"video/mp4\" width=\"600\" height=\"480\" delivery=\"progressive\" id=\"5728\" bitrate=\"720\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4]]></MediaFile>\n" +
            "   <MediaFile type=\"application/x-mpegURL\" width=\"600\" height=\"480\" delivery=\"streaming\" id=\"5728\" bitrate=\"1800\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.m3u8]]></MediaFile>\n" +
            "</MediaFiles>";

    public SAVASTParser_Local_Tests1() {
        super(Application.class);
    }

    @SmallTest
    public void testParseMediaXML1 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            // create and assert a new parser
            SAVASTParser parser = new SAVASTParser(getContext());
            assertNotNull(parser);

            Element firstMediaElement = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "MediaFile");
            assertNotNull(firstMediaElement);

            SAVASTMedia savastMedia = parser.parseMediaXML(firstMediaElement);
            assertNotNull(savastMedia);

            String expected_type = "video/mp4";
            int expected_width = 600;
            int expected_height = 480;
            int expected_bitrate = 720;
            String expected_url = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4";

            assertEquals(expected_type, savastMedia.type);
            assertEquals(expected_width, savastMedia.width);
            assertEquals(expected_height, savastMedia.height);
            assertEquals(expected_bitrate, savastMedia.bitrate);
            assertEquals(expected_url, savastMedia.url);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @SmallTest
    public void testParseMediaXML2 () {

        // parse the XML document
        Document document = null;
        String tag = "MediaFile";

        // create and assert a new parser
        SAVASTParser parser = new SAVASTParser(getContext());
        assertNotNull(parser);

        Element firstMediaElement = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstMediaElement);

        SAVASTMedia savastMedia = parser.parseMediaXML(firstMediaElement);
        assertNotNull(savastMedia);

        String expected_type = null;
        int expected_width = 0;
        int expected_height = 0;
        int expected_bitrate = 0;
        String expected_url = null;

        assertEquals(expected_type, savastMedia.type);
        assertEquals(expected_width, savastMedia.width);
        assertEquals(expected_height, savastMedia.height);
        assertEquals(expected_bitrate, savastMedia.bitrate);
        assertEquals(expected_url, savastMedia.url);
    }

    @SmallTest
    public void testParseMediaXML3 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        // create and assert a new parser
        SAVASTParser parser = new SAVASTParser(getContext());
        assertNotNull(parser);

        Element firstMediaElement = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(firstMediaElement);

        SAVASTMedia savastMedia = parser.parseMediaXML(firstMediaElement);
        assertNotNull(savastMedia);

        String expected_type = null;
        int expected_width = 0;
        int expected_height = 0;
        int expected_bitrate = 0;
        String expected_url = null;

        assertEquals(expected_type, savastMedia.type);
        assertEquals(expected_width, savastMedia.width);
        assertEquals(expected_height, savastMedia.height);
        assertEquals(expected_bitrate, savastMedia.bitrate);
        assertEquals(expected_url, savastMedia.url);
    }
}