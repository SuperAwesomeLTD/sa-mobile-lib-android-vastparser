package tv.superawesome.lib.savastparser.mocks.servers;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Created by gabriel.coman on 01/05/2018.
 */
public abstract class MockAbstractWebServer {

    private MockWebServer server;

    public MockAbstractWebServer () {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        server = new MockWebServer();
    }

    public String url() {
        return server.url("").toString();
    }

    public void start() throws Throwable {
        // start the server instance
        server.start(64000);

        // create a dispatcher
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch (RecordedRequest request) throws InterruptedException {
                return handleRequest(request);
            }
        };

        // set dispatcher
        server.setDispatcher(dispatcher);
    }

    // to be implemented
    public abstract MockResponse handleRequest(RecordedRequest request);

    public void shutdown() throws Throwable {
        // Shut down the server. Instances cannot be reused.
        server.shutdown();
    }
}
