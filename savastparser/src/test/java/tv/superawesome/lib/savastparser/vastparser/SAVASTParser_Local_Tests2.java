package tv.superawesome.lib.savastparser.vastparser;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
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
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SAVASTParser_Local_Tests2  {

    private String xml = null;

    @Before
    public void setUp () {
        xml = ResourceReader.readResource("mock_xml_response_2.xml");
    }

    @Test
    public void testParseAdXML1 () {

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            // create and assert a new parser
            Context context = null; // mock(Context.class);

            SAVASTParser parser = new SAVASTParser(context);
            assertNotNull(parser);

            Element Ad = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "Ad");
            assertNotNull(Ad);

            SAVASTAd ad = parser.parseAdXML(Ad);
            assertNotNull(ad);

            SAVASTAdType expected_vastType = SAVASTAdType.InLine;
            int expected_vastEventsSize = 6;
            int expected_mediaListSize = 1;
            String expected_mediaUrl = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4";
            int expected_bitrate = 720;
            int expected_width = 600;
            int expected_height = 480;

            String[] expected_types = {
                    "vast_error", "vast_impression", "vast_click_through", "vast_creativeView", "vast_start", "vast_firstQuartile"
            };
            String[] expected_urls = {
                    "https://ads.staging.superawesome.tv/v2/video/error?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=7062039&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB&amp;code=[ERRORCODE]",
                    "https://ads.staging.superawesome.tv/v2/video/impression?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=9788452&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/click?placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=9970101&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=creativeView&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=3266878&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=start&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=9640628&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=firstQuartile&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=2560539&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB"
            };

            assertEquals(expected_vastType, ad.type);
            assertNull(ad.redirect);
            assertNotNull(ad.events);
            assertNotNull(ad.media);
            assertEquals(expected_vastEventsSize, ad.events.size());
            assertEquals(expected_mediaListSize, ad.media.size());

            for (int i = 0; i < ad.events.size(); i++) {
                assertEquals(expected_types[i], ad.events.get(i).event);
                assertEquals(expected_urls[i], ad.events.get(i).URL);
            }

            SAVASTMedia savastMedia = ad.media.get(0);
            assertNotNull(savastMedia);
            assertTrue(savastMedia.isValid());
            assertEquals(expected_mediaUrl, savastMedia.url);
            assertEquals(expected_bitrate, savastMedia.bitrate);
            assertEquals(expected_width, savastMedia.width);
            assertEquals(expected_height, savastMedia.height);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseAdXML2 () {

        // parse the XML document
        Document document = null;
        String tag = "Ad";

        // create and assert a new parser
        Context context = null; // mock(Context.class);

        SAVASTParser parser = new SAVASTParser(context);
        assertNotNull(parser);

        Element Ad = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(Ad);

        SAVASTAd ad = parser.parseAdXML(Ad);
        assertNotNull(ad);

        SAVASTAdType expected_vastType = SAVASTAdType.Invalid;
        int expected_vastEventsSize = 0;
        int expected_mediaListSize = 0;

        assertEquals(expected_vastType, ad.type);
        assertNull(ad.redirect);
        assertNotNull(ad.events);
        assertNotNull(ad.media);
        assertEquals(expected_vastEventsSize, ad.events.size());
        assertEquals(expected_mediaListSize, ad.media.size());
    }

    @Test
    public void testParseAdXML3 () {

        // parse the XML document
        Document document = null;
        String tag = null;

        // create and assert a new parser
        Context context = null; // mock(Context.class);
        
        SAVASTParser parser = new SAVASTParser(context);
        assertNotNull(parser);

        Element Ad = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, tag);
        assertNull(Ad);

        SAVASTAd ad = parser.parseAdXML(Ad);
        assertNotNull(ad);

        SAVASTAdType expected_vastType = SAVASTAdType.Invalid;
        int expected_vastEventsSize = 0;
        int expected_mediaListSize = 0;

        assertEquals(expected_vastType, ad.type);
        assertNull(ad.redirect);
        assertNotNull(ad.events);
        assertNotNull(ad.media);
        assertEquals(expected_vastEventsSize, ad.events.size());
        assertEquals(expected_mediaListSize, ad.media.size());
    }
}
