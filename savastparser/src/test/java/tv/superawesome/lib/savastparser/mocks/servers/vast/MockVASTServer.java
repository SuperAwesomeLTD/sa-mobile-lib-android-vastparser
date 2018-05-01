package tv.superawesome.lib.savastparser.mocks.servers.vast;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import tv.superawesome.lib.savastparser.mocks.servers.MockAbstractWebServer;
import tv.superawesome.lib.savastparser.mocks.servers.ResponseFactory;

/**
 * Created by gabriel.coman on 01/05/2018.
 */

public class MockVASTServer extends MockAbstractWebServer {

    @Override
    public MockResponse handleRequest(RecordedRequest request) {

        switch (request.getRequestLine()) {
            // non-chained
            case "GET /vast/vast1.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_1.0.xml");
            // 1st chained request
            case "GET /vast/vast2.0.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_2.0.xml");
            case "GET /vast/vast2.1.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_2.1.xml");
            // 2nd chained request
            case "GET /vast/vast3.0.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_3.0.xml");
            case "GET /vast/vast3.1.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_3.1.xml");
            // and empty request
            case "GET /vast/vast4.0.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_4.0.xml");
            // and empty request
            case "GET /vast/vast5.0.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_5.0.xml");
            case "GET /vast/vast5.1.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_5.1.xml");
            case "GET /vast/vast5.2.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_5.2.xml");
            case "GET /vast/vast5.3.xml HTTP/1.1":
                return ResponseFactory.responseFromResource("mock_vast_response_5.3.xml");
        }

        return null;
    }
}
