package superawesome.tv.savastparserdemoapp;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.ArrayList;
import java.util.List;

import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAVASTParserInterface;

public class SAVASTParser_Async_Test extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final int TIMEOUT = 2500;

    public SAVASTParser_Async_Test() {
        super("superawesome.tv.sanetworktester", MainActivity.class);
    }

    @UiThreadTest
    @LargeTest
    public void test1 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST1.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                String expected_mediaUrl = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 15;
                String expected_error = "https://ads.superawesome.tv/v2/video/error?placement=30479&amp;creative=-1&amp;line_item=-1&amp;sdkVersion=unknown&amp;rnd=3232269&amp;device=web&amp;country=GB&amp;code=[ERRORCODE]";
                String expected_impression = "https://ads.superawesome.tv/v2/video/impression?placement=30479&amp;creative=-1&amp;line_item=-1&amp;sdkVersion=unknown&amp;rnd=4538730&amp;device=web&amp;country=GB";
                String expected_click = "https://ads.superawesome.tv/v2/video/click?placement=30479&creative=-1&line_item=-1&sdkVersion=unknown&rnd=1809240&device=web&country=GB";

                assertNotNull(ad);
                assertNotNull(ad.mediaUrl);
                assertEquals(expected_mediaUrl, ad.mediaUrl);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());

                SATracking error = null;
                SATracking impression = null;
                SATracking click = null;
                for (SATracking tracking : ad.vastEvents) {
                    if (tracking.event.equals("error")) error = tracking;
                    if (tracking.event.equals("impression")) impression = tracking;
                    if (tracking.event.equals("click_through")) click = tracking;
                }

                assertNotNull(error);
                assertEquals(expected_error, error.URL);
                assertNotNull(impression);
                assertEquals(expected_impression, impression.URL);
                assertNotNull(click);
                assertEquals(expected_click, click.URL);
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test2 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST2.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                assertNotNull(ad);
                assertNotNull(ad.mediaUrl);

                String expected_mediaURL = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 40;
                int expected_errorL = 2;
                int expected_impressionL = 2;
                int expected_click_throughL = 1;
                int expected_click_trackingL = 3;

                assertNotNull(ad.mediaUrl);
                assertEquals(expected_mediaURL, ad.mediaUrl);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());

                List<SATracking> errors = new ArrayList<>();
                List<SATracking> impressions = new ArrayList<>();
                List<SATracking> clicks_tracking = new ArrayList<>();
                List<SATracking> click_through = new ArrayList<>();

                for (SATracking tracking : ad.vastEvents) {
                    if (tracking.event.equals("error")) errors.add(tracking);
                    if (tracking.event.equals("impression")) impressions.add(tracking);
                    if (tracking.event.equals("click_tracking")) clicks_tracking.add(tracking);
                    if (tracking.event.equals("click_through")) click_through.add(tracking);
                }

                assertEquals(expected_errorL, errors.size());
                assertEquals(expected_impressionL, impressions.size());
                assertEquals(expected_click_trackingL, clicks_tracking.size());
                assertEquals(expected_click_throughL, click_through.size());
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test3 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST3.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 21;

                assertNotNull(ad);
                assertNull(ad.mediaUrl);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());
                assertNotNull(ad.vastRedirect);
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test4 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST4.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.mediaUrl);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());
                assertNull(ad.vastRedirect);
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test5 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "hshsa/..saas";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.mediaUrl);
                assertNull(ad.vastRedirect);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test6 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = null;

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.mediaUrl);
                assertNull(ad.vastRedirect);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());
            }
        });

        sleep(TIMEOUT);
    }

    @UiThreadTest
    @LargeTest
    public void test7 () {

        SAVASTParser parser = new SAVASTParser(getActivity());
        assertNotNull(parser);

        String vast = "https://raw.githubusercontent.com/SuperAwesomeLTD/sa-mobile-lib-android-vastparser/master/samples/VAST5.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                assertNotNull(ad);
                assertNotNull(ad.mediaUrl);

                String expected_mediaURL = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 30;
                int expected_errorL = 2;
                int expected_impressionL = 2;
                int expected_click_throughL = 0;
                int expected_click_trackingL = 4;

                assertNotNull(ad.mediaUrl);
                assertEquals(expected_mediaURL, ad.mediaUrl);
                assertNotNull(ad.vastEvents);
                assertEquals(expected_vastEventsL, ad.vastEvents.size());

                List<SATracking> errors = new ArrayList<>();
                List<SATracking> impressions = new ArrayList<>();
                List<SATracking> clicks_tracking = new ArrayList<>();
                List<SATracking> click_through = new ArrayList<>();

                for (SATracking tracking : ad.vastEvents) {
                    if (tracking.event.equals("error")) errors.add(tracking);
                    if (tracking.event.equals("impression")) impressions.add(tracking);
                    if (tracking.event.equals("click_tracking")) clicks_tracking.add(tracking);
                    if (tracking.event.equals("click_through")) click_through.add(tracking);
                }

                assertEquals(expected_errorL, errors.size());
                assertEquals(expected_impressionL, impressions.size());
                assertEquals(expected_click_trackingL, clicks_tracking.size());
                assertEquals(expected_click_throughL, click_through.size());
            }
        });

        sleep(TIMEOUT);
    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            fail("Unexpected Timeout");
        }
    }
}
