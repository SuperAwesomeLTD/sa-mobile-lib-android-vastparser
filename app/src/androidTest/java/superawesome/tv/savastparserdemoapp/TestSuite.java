package superawesome.tv.savastparserdemoapp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        SAXMLParser_Tests.class,
        SAVASTParser_Local_Tests.class,
        SAVASTParser_Async_Test.class
})
public class TestSuite {
}
