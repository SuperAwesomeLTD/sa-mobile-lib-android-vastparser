package superawesome.tv.savastparserdemoapp;

import android.app.Application;
import android.os.Looper;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.samodelspace.SAVASTAdType;
import tv.superawesome.lib.samodelspace.SAVASTMedia;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAVASTParserInterface;
import tv.superawesome.lib.savastparser.SAXMLParser;

public class SAVASTParser_Local_Tests extends ApplicationTestCase<Application> {
    public SAVASTParser_Local_Tests() {
        super(Application.class);
    }


    @SmallTest
    public void testParseMediaXML1 () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<MediaFiles>\n" +
                "   <MediaFile type=\"video/mp4\" width=\"600\" height=\"480\" delivery=\"progressive\" id=\"5728\" bitrate=\"720\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4]]></MediaFile>\n" +
                "   <MediaFile type=\"application/x-mpegURL\" width=\"600\" height=\"480\" delivery=\"streaming\" id=\"5728\" bitrate=\"1800\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.m3u8]]></MediaFile>\n" +
                "</MediaFiles>";

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
            assertEquals(expected_url, savastMedia.mediaUrl);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @SmallTest
    public void testParseMediaXML2 () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<MediaFiles>\n" +
                "   <MediaFile type=\"video/mp4\" width=\"600\" height=\"480\" delivery=\"progressive\" id=\"5728\" bitrate=\"720\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4]]></MediaFile>\n" +
                "   <MediaFile type=\"application/x-mpegURL\" width=\"600\" height=\"480\" delivery=\"streaming\" id=\"5728\" bitrate=\"1800\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.m3u8]]></MediaFile>\n" +
                "</MediaFiles>";

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            // create and assert a new parser
            SAVASTParser parser = new SAVASTParser(getContext());
            assertNotNull(parser);

            Element firstMediaElement = null;
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
            assertEquals(expected_url, savastMedia.mediaUrl);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @SmallTest
    public void testParseAdXML () {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<VAST version=\"3.0\">\n" +
                "   <Ad id=\"undefined\">\n" +
                "      <InLine>\n" +
                "         <AdSystem version=\"1.0\">AwesomeAds</AdSystem>\n" +
                "         <AdTitle><![CDATA[REX fake exchange creative]]></AdTitle>\n" +
                "         <Description />\n" +
                "         <Error><![CDATA[https://ads.staging.superawesome.tv/v2/video/error?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=7062039&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB&amp;code=[ERRORCODE]]]></Error>\n" +
                "         <Impression id=\"23\"><![CDATA[https://ads.staging.superawesome.tv/v2/video/impression?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=9788452&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB]]></Impression>\n" +
                "         <Creatives>\n" +
                "            <Creative id=\"5728\">\n" +
                "               <Linear>\n" +
                "                  <Duration>00:00:05</Duration>\n" +
                "                  <TrackingEvents>\n" +
                "                     <Tracking event=\"creativeView\">https://ads.staging.superawesome.tv/v2/video/tracking?event=creativeView&amp;placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=3266878&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB</Tracking>\n" +
                "                     <Tracking event=\"start\">https://ads.staging.superawesome.tv/v2/video/tracking?event=start&amp;placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=9640628&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB</Tracking>\n" +
                "                     <Tracking event=\"firstQuartile\">https://ads.staging.superawesome.tv/v2/video/tracking?event=firstQuartile&amp;placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=2560539&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB</Tracking>\n" +
                "                     </TrackingEvents>\n" +
                "                  <VideoClicks>\n" +
                "                     <ClickThrough id=\"\">https://ads.staging.superawesome.tv/v2/video/click?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=9970101&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB</ClickThrough>\n" +
                "                  </VideoClicks>\n" +
                "                  <MediaFiles>\n" +
                "                     <MediaFile type=\"video/mp4\" width=\"600\" height=\"480\" delivery=\"progressive\" id=\"5728\" bitrate=\"720\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.mp4]]></MediaFile>\n" +
                "                     <MediaFile type=\"application/x-mpegURL\" width=\"600\" height=\"480\" delivery=\"streaming\" id=\"5728\" bitrate=\"1800\"><![CDATA[https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/c0sKSRTuPu8dDkok2HQTnLS1k3A6vL6c.m3u8]]></MediaFile>\n" +
                "                  </MediaFiles>\n" +
                "               </Linear>\n" +
                "            </Creative>\n" +
                "         </Creatives>\n" +
                "      </InLine>\n" +
                "   </Ad>\n" +
                "</VAST>";

        try {
            // parse the XML document
            Document document = SAXMLParser.parseXML(xml);

            // assert that it's not null
            assertNotNull(document);

            // create and assert a new parser
            SAVASTParser parser = new SAVASTParser(getContext());
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
                "error", "impression", "click_through", "creativeView", "start", "firstQuartile"
            };
            String[] expected_urls = {
                    "https://ads.staging.superawesome.tv/v2/video/error?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=7062039&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB&amp;code=[ERRORCODE]",
                    "https://ads.staging.superawesome.tv/v2/video/impression?placement=544&amp;creative=5728&amp;line_item=1022&amp;sdkVersion=unknown&amp;rnd=9788452&amp;prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&amp;device=web&amp;country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/click?placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=9970101&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=creativeView&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=3266878&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=start&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=9640628&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB",
                    "https://ads.staging.superawesome.tv/v2/video/tracking?event=firstQuartile&placement=544&creative=5728&line_item=1022&sdkVersion=unknown&rnd=2560539&prog=a35a7dab-86f1-437f-b3d9-3b58ef069390&device=web&country=GB"
            };

            assertEquals(expected_vastType, ad.vastType);
            assertNull(ad.vastRedirect);
            assertNotNull(ad.vastEvents);
            assertNotNull(ad.mediaList);
            assertEquals(expected_vastEventsSize, ad.vastEvents.size());
            assertEquals(expected_mediaListSize, ad.mediaList.size());

            for (int i = 0; i < ad.vastEvents.size(); i++) {
                assertEquals(expected_types[i], ad.vastEvents.get(i).event);
                assertEquals(expected_urls[i], ad.vastEvents.get(i).URL);
            }

            SAVASTMedia savastMedia = ad.mediaList.get(0);
            assertNotNull(savastMedia);
            assertTrue(savastMedia.isValid());
            assertEquals(expected_mediaUrl, savastMedia.mediaUrl);
            assertEquals(expected_bitrate, savastMedia.bitrate);
            assertEquals(expected_width, savastMedia.width);
            assertEquals(expected_height, savastMedia.height);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}