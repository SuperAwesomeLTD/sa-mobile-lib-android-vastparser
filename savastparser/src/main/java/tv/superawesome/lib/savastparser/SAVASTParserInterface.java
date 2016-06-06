package tv.superawesome.lib.savastparser;

import java.util.List;
import tv.superawesome.lib.savastparser.models.SAVASTAd;

/**
 * Public interface
 */
public interface SAVASTParserInterface {

    /**
     * Called when the parser has successfully parsed a VAST tag
     * @param ad - returns (as a callback parameter) a
     */
    void didParseVAST(SAVASTAd ad);
}
