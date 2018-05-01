package tv.superawesome.lib.savastparser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tv.superawesome.lib.savastparser.vastparser.SAVASTParser_Async_Test;
import tv.superawesome.lib.savastparser.vastparser.SAVASTParser_Local_Tests1;
import tv.superawesome.lib.savastparser.vastparser.SAVASTParser_Local_Tests2;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests1;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests2;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests3;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests4;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests5;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests6;
import tv.superawesome.lib.savastparser.xmlparser.SAXMLParser_Tests7;

/**
 * Created by gabriel.coman on 01/05/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SAXMLParser_Tests1.class,
        SAXMLParser_Tests2.class,
        SAXMLParser_Tests3.class,
        SAXMLParser_Tests4.class,
        SAXMLParser_Tests5.class,
        SAXMLParser_Tests6.class,
        SAXMLParser_Tests7.class,
        SAVASTParser_Local_Tests1.class,
        SAVASTParser_Local_Tests2.class,
        SAVASTParser_Async_Test.class
})
public class TestSuite {
}
