package tv.superawesome.lib.savastparser.vastparser;

import android.content.Context;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import tv.superawesome.lib.samodelspace.vastad.SAVASTAd;
import tv.superawesome.lib.samodelspace.vastad.SAVASTEvent;
import tv.superawesome.lib.savastparser.SAVASTParser;
import tv.superawesome.lib.savastparser.SAVASTParserInterface;
import tv.superawesome.lib.savastparser.mocks.executors.MockExecutor;
import tv.superawesome.lib.savastparser.mocks.servers.MockAbstractWebServer;
import tv.superawesome.lib.savastparser.mocks.servers.vast.MockVASTServer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class SAVASTParser_Async_Test {

    private Executor executor;
    private MockAbstractWebServer webServer;

    @Before
    public void setUp () throws Throwable {
        executor = new MockExecutor();
        webServer = new MockVASTServer();
        webServer.start();
    }

    @After
    public void tearDown () throws Throwable {
        webServer.shutdown();
    }

    @Test
    public void test1 () throws Throwable {

        Context context = null;

        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = webServer.url() + "vast/vast1.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                String expected_mediaUrl = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 15;
                String expected_error = "https://ads.superawesome.tv/v2/video/error?placement=30479&amp;creative=-1&amp;line_item=-1&amp;sdkVersion=unknown&amp;rnd=3232269&amp;device=web&amp;country=GB&amp;code=[ERRORCODE]";
                String expected_impression = "https://ads.superawesome.tv/v2/video/impression?placement=30479&amp;creative=-1&amp;line_item=-1&amp;sdkVersion=unknown&amp;rnd=4538730&amp;device=web&amp;country=GB";
                String expected_click = "https://ads.superawesome.tv/v2/video/click?placement=30479&creative=-1&line_item=-1&sdkVersion=unknown&rnd=1809240&device=web&country=GB";

                assertNotNull(ad);
                assertNotNull(ad.url);
                assertEquals(expected_mediaUrl, ad.url);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());

                SAVASTEvent error = null;
                SAVASTEvent impression = null;
                SAVASTEvent click = null;
                for (SAVASTEvent evt : ad.events) {
                    if (evt.event.equals("vast_error")) error = evt;
                    if (evt.event.equals("vast_impression")) impression = evt;
                    if (evt.event.equals("vast_click_through")) click = evt;
                }

                assertNotNull(error);
                assertEquals(expected_error, error.URL);
                assertNotNull(impression);
                assertEquals(expected_impression, impression.URL);
                assertNotNull(click);
                assertEquals(expected_click, click.URL);
            }
        });
    }

    @Test
    public void test2 () throws Throwable {

        Context context = null;

        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = webServer.url() + "vast/vast2.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                assertNotNull(ad);
                assertNotNull(ad.url);

                String expected_mediaURL = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 40;
                int expected_errorL = 2;
                int expected_impressionL = 2;
                int expected_click_throughL = 1;
                int expected_click_trackingL = 3;

                assertNotNull(ad.url);
                assertEquals(expected_mediaURL, ad.url);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());

                List<SAVASTEvent> errors = new ArrayList<>();
                List<SAVASTEvent> impressions = new ArrayList<>();
                List<SAVASTEvent> clicks_tracking = new ArrayList<>();
                List<SAVASTEvent> click_through = new ArrayList<>();

                for (SAVASTEvent evt : ad.events) {
                    if (evt.event.equals("vast_error")) errors.add(evt);
                    if (evt.event.equals("vast_impression")) impressions.add(evt);
                    if (evt.event.equals("vast_click_tracking")) clicks_tracking.add(evt);
                    if (evt.event.equals("vast_click_through")) click_through.add(evt);
                }

                assertEquals(expected_errorL, errors.size());
                assertEquals(expected_impressionL, impressions.size());
                assertEquals(expected_click_trackingL, clicks_tracking.size());
                assertEquals(expected_click_throughL, click_through.size());
            }
        });
    }

    @Test
    public void test3 () throws Throwable {

        Context context = null;
        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = webServer.url() + "vast/vast3.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 21;

                assertNotNull(ad);
                assertNull(ad.url);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());
                assertNotNull(ad.redirect);

            }
        });
    }

    @Test
    public void test4 () throws Throwable {

        Context context = null;
        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = webServer.url() + "vast/vast4.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.url);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());
                assertNull(ad.redirect);
            }
        });
    }

    @Test
    public void test5 () throws Throwable {

        Context context = null;
        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = "hshsa/..saas";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.url);
                assertNull(ad.redirect);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());
            }
        });
    }

    @Test
    public void test6 () throws Throwable {

        Context context = null;
        final SAVASTParser parser = new SAVASTParser(null, executor, 1000);
        assertNotNull(parser);

        final String vast = null;

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                int expected_vastEventsL = 0;

                assertNotNull(ad);
                assertNull(ad.url);
                assertNull(ad.redirect);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());
            }
        });
    }

    @Test
    public void test7 () throws Throwable {

        Context context = null;
        final SAVASTParser parser = new SAVASTParser(context, executor, 1000);
        assertNotNull(parser);

        final String vast = webServer.url() + "vast/vast5.0.xml";

        parser.parseVAST(vast, new SAVASTParserInterface() {
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                assertNotNull(ad);
                assertNotNull(ad.url);

                String expected_mediaURL = "https://ads.superawesome.tv/v2/demo_images/video.mp4";
                int expected_vastEventsL = 30;
                int expected_errorL = 2;
                int expected_impressionL = 2;
                int expected_click_throughL = 0;
                int expected_click_trackingL = 4;

                assertNotNull(ad.url);
                assertEquals(expected_mediaURL, ad.url);
                assertNotNull(ad.events);
                assertEquals(expected_vastEventsL, ad.events.size());

                List<SAVASTEvent> errors = new ArrayList<>();
                List<SAVASTEvent> impressions = new ArrayList<>();
                List<SAVASTEvent> clicks_tracking = new ArrayList<>();
                List<SAVASTEvent> click_through = new ArrayList<>();

                for (SAVASTEvent evt : ad.events) {
                    if (evt.event.equals("vast_error")) errors.add(evt);
                    if (evt.event.equals("vast_impression")) impressions.add(evt);
                    if (evt.event.equals("vast_click_tracking")) clicks_tracking.add(evt);
                    if (evt.event.equals("vast_click_through")) click_through.add(evt);
                }

                assertEquals(expected_errorL, errors.size());
                assertEquals(expected_impressionL, impressions.size());
                assertEquals(expected_click_trackingL, clicks_tracking.size());
                assertEquals(expected_click_throughL, click_through.size());
            }
        });
    }
}
