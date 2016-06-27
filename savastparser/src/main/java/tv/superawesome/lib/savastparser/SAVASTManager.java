package tv.superawesome.lib.savastparser;

import android.util.Log;

import java.util.List;

import tv.superawesome.lib.saevents.SAEvents;
import tv.superawesome.lib.sautils.SAUtils;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.samodelspace.SAVASTCreative;
import tv.superawesome.lib.samodelspace.SAVASTTracking;
import tv.superawesome.lib.savideoplayer.SAVideoPlayer;
import tv.superawesome.lib.savideoplayer.SAVideoPlayerInterface;

/**
 * Created by gabriel.coman on 23/12/15.
 */
public class SAVASTManager implements SAVASTParserInterface, SAVideoPlayerInterface {

    /** private variables */
    private SAVASTParser parser;
    private SAVideoPlayer refPlayer;
    private SAVASTManagerInterface listener;

    /** references to current ad and current creative */
    private SAVASTAd _cAd;
    private SAVASTCreative _cCreative;

    /**
     * Constructor
     * @param refPlayer
     * @param listener
     */
    public SAVASTManager(SAVideoPlayer refPlayer, SAVASTManagerInterface listener) {

        this.refPlayer = refPlayer;
        this.listener = listener;

        /** create parser */
        parser = new SAVASTParser();

        /** set the listener */
        if (this.refPlayer != null) {
            this.refPlayer.listener = this;
        }
    }

    /**
     * Main Manager public function
     * @param url
     */
    public void parseVASTURL(String url) {
        parser.parseVASTAds(url, this);
    }

    public void manageWithAd(SAVASTAd ad) {
        /** error checking */
        if (ad == null || ad.creative == null) {
            if (listener != null) {
                listener.didNotFindAds();
            }
            return;
        }

        /** get current ad */
        _cAd = ad;
        _cCreative = ad.creative;

        /** call listener */
        if (listener != null){
            listener.didStartAd();
        }

        // play current data
        playCurrentAdWithCurrentCreative();
    }

    /** <SAVASTParserInterface> */

    @Override
    public void didParseVAST(SAVASTAd ad) {
        manageWithAd(ad);
    }

    /** <SAVideoPlayerInterface> */

    @Override
    public void didFindPlayerReady() {

        if (!_cAd.isImpressionSent) {
            for (String Impression : _cAd.impressions) {
                SAEvents.sendEventToURL(Impression);
            }
            _cAd.isImpressionSent = true;
        }
    }

    @Override
    public void didStartPlayer() {
        Log.d("SuperAwesome", "didStartPlayer");

        /** send events */
        sendCurrentCreativeTrackersFor("start");
        sendCurrentCreativeTrackersFor("creativeView");

        /** call listener  */
        if (listener != null){
            listener.didStartCreative();
        }
    }

    @Override
    public void didReachFirstQuartile() {
        Log.d("SuperAwesome", "didReachFirstQuartile");

        /** send events */
        sendCurrentCreativeTrackersFor("firstQuartile");

        /** call listener */
        if (listener != null) {
            listener.didReachFirstQuartileOfCreative();
        }
    }

    @Override
    public void didReachMidpoint() {
        Log.d("SuperAwesome", "didReachMidpoint");

        /** send events */
        sendCurrentCreativeTrackersFor("midpoint");

        /** call listener */
        if (listener != null){
            listener.didReachMidpointOfCreative();
        }
    }

    @Override
    public void didReachThirdQuartile() {
        Log.d("SuperAwesome", "didReachThirdQuartile");

        /** send events */
        sendCurrentCreativeTrackersFor("thirdQuartile");

        /** call listener */
        if (listener != null) {
            listener.didReachThirdQuartileOfCreative();
        }
    }

    @Override
    public void didReachEnd() {
        Log.d("SuperAwesome", "didReachEnd");

        /** send events */
        sendCurrentCreativeTrackersFor("complete");

        /** call listener */
        if (listener != null){
            listener.didEndOfCreative();
            listener.didEndAd();
            listener.didEndAllAds();
        }
    }

    @Override
    public void didPlayWithError() {
        Log.d("SuperAwesome", "didPlayWithError");

        /**
         * if a creative is played with error, send events to the error tag
         * and advance to the next ad
         */
        if (_cAd != null) {
            for (String error : _cAd.errors) {
                SAEvents.sendEventToURL(error);
            }
        }

        /** call to listener */
        if (listener != null){
            listener.didHaveErrorForCreative();
        }
    }

    @Override
    public void didGoToURL() {
        Log.d("SuperAwesome", "didGoToURL");

        /** setup the current click URL */
        String url = "";
        if (_cCreative.clickThrough != null && SAUtils.isValidURL(_cCreative.clickThrough)){
            url = _cCreative.clickThrough;
        }

        /** call listener */
        if (listener != null){
            listener.didGoToURL(url, _cCreative.clickTracking);
        }
    }

    @Override
    public void didClickOnClose() {
        if (listener != null) {
            listener.didClickOnClose();
        }
    }

    private void playCurrentAdWithCurrentCreative() {
        /** play */
        if (refPlayer != null) {
            refPlayer.listener = this;
            if (_cCreative.isOnDisk) {
                Log.d("SuperAwesome", "Taking file from disk " + _cCreative.playableDiskUrl);
                refPlayer.playWithDiskURL(_cCreative.playableDiskUrl);
            } else {
                Log.d("SuperAwesome", "Taking file from remote " + _cCreative.playableMediaUrl);
                refPlayer.playWithMediaURL(_cCreative.playableMediaUrl);
            }
        }
    }

    /** Aux functions */
    private void sendCurrentCreativeTrackersFor(String event) {
        for (SAVASTTracking tracking : _cCreative.trackingEvents) {
            if (tracking.event.equals(event)) {
                SAEvents.sendEventToURL(tracking.url);
            }
        }
    }
}
