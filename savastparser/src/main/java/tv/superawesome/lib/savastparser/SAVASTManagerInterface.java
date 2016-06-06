package tv.superawesome.lib.savastparser;

import java.util.List;

/**
 * Public interface
 */
public interface SAVASTManagerInterface {

    /**
     * parsed vast and found errors / could not parse vast
     */
    void didNotFindAds();

    /**
     * ad started
     */
    void didStartAd();

    /**
     * creative / video started
     */
    void didStartCreative();

    /**
     * video 1/4
     */
    void didReachFirstQuartileOfCreative();

    /**
     * video 1/2
     */
    void didReachMidpointOfCreative();

    /**
     * video 3/4
     */
    void didReachThirdQuartileOfCreative();

    /**
     * video full
     */
    void didEndOfCreative();

    /**
     * video played with errors
     */
    void didHaveErrorForCreative();

    /**
     * ad ended
     */
    void didEndAd();

    /**
     * all ads ended
     */
    void didEndAllAds();

    /**
     * ad was clicked
     * @param url the URL to go to
     * @param clickTracking additoonal click tracking URLs
     */
    void didGoToURL(String url, List<String> clickTracking);

    /**
     * ad was closed
     */
    void didClickOnClose();
}
