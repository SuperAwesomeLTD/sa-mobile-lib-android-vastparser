package tv.superawesome.lib.savastparser.vastparser;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.samodelspace.vastad.SAVASTMedia;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAXMLParser;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class SAVASTParser_Local_Tests1  {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_media_files_response_1.xml");
    }

    @Test
    public void testParseMediaXML1 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            // create and assert a new parser
            Context context = null; // mock(Context.class);
            
            SAVASTParser parser = new SAVASTParser(context);
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

    @Test
    public void testParseMediaXML2 () {

        // parse the XML document
        Document document = null;
        String tag = "MediaFile";

        // create and assert a new parser
        Context context = null; // mock(Context.class);

        SAVASTParser parser = new SAVASTParser(context);
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

    @Test
    public void testParseMediaXML3 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        // create and assert a new parser
        Context context = null; // mock(Context.class);
        
        SAVASTParser parser = new SAVASTParser(context);
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