/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.savastparser;

import tv.superawesome.lib.samodelspace.vastad.SAVASTAd;

/**
 * Interface used by the VAST parser to return a complete parsed VAST Ad
 */
public interface SAVASTParserInterface {

    /**
     * Main method that needs to be implemented by the library users in order to get back a valid
     * VAST ad
     *
     * @param ad the returned ad
     */
    void saDidParseVAST(SAVASTAd ad);
}

