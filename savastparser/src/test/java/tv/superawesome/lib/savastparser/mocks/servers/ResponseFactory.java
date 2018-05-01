package tv.superawesome.lib.savastparser.mocks.servers;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.SocketPolicy;
import tv.superawesome.lib.savastparser.testutils.ResourceReader;

/**
 * Created by gabriel.coman on 01/05/2018.
 */
public class ResponseFactory {

    public static MockResponse responseFromResource(String file) {
        String responseBody = ResourceReader.readResource(file);
        return new MockResponse().setBody(responseBody);
    }
    public static MockResponse badXMLResponse () {
        return responseFromResource("mock_error_xml_response.xml")
                .setResponseCode(200);
    }

    public static MockResponse emptyResponse () {
        return new MockResponse().setBody("").setResponseCode(200);
    }

    public static MockResponse timeoutResponse () {
        return new MockResponse()
                .setBody("")
                .setSocketPolicy(SocketPolicy.NO_RESPONSE);
    }
}