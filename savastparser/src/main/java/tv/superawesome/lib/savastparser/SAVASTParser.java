/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.savastparser;

import android.content.Context;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.vastad.SAVASTEvent;
import tv.superawesome.lib.samodelspace.vastad.SAVASTAd;
import tv.superawesome.lib.samodelspace.vastad.SAVASTAdType;
import tv.superawesome.lib.samodelspace.vastad.SAVASTMedia;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that abstracts away the complexities of parsing a VAST XML response
 */
public class SAVASTParser {

    // private context
    private Context context = null;
    // the header & query
    private JSONObject header = null;
    private JSONObject query = null;

    private Executor executor;
    private int timeout;

    /**
     * Simple constructor with a context as a parameter
     *
     * @param context current context (activity or fragment)
     */
    public SAVASTParser (Context context) {

        executor = Executors.newSingleThreadExecutor();
        timeout = 15000;

        this.context = context;
        query = new JSONObject();
        header = SAJsonParser.newObject(
                "Content-Type", "application/json",
                "User-Agent", SAUtils.getUserAgent(context));
    }

    /**
     * Constructor mainly used for testing
     * @param context   current context (activity or fragment)
     * @param executor  a test executor
     * @param timeout   a timeout
     */
    public SAVASTParser (Context context, Executor executor, int timeout) {
        this(context);
        this.executor = executor;
        this.timeout = timeout;
    }

    /**
     * Method that starts the VAST parsing by calling the internal recursive parsing method
     *
     * @param url       the initial VAST url to call
     * @param listener  a copy of the SAVASTParserInterface listener that the class / method sends
     *                  callbacks to the library user
     */
    public void parseVAST (String url, final SAVASTParserInterface listener) {
        // make sure the local listener is never null so I don't have to do checks upon checks
        final SAVASTParserInterface localListener = listener != null ? listener : new SAVASTParserInterface() {@Override public void saDidParseVAST(SAVASTAd ad) {}};

        // start the recursive method
        recursiveParse(url, new SAVASTAd(), new SAVASTParserInterface() {
            /**
             * Overridden SAVASTParserInterface implementation in which finally the whole parser
             * has produced a SAVASTAd of sorts (might or might not be valid) and I have to get
             * a valid media out of it
             *
             * @param ad the returned ad
             */
            @Override
            public void saDidParseVAST(SAVASTAd ad) {

                SAVASTMedia minMedia = null;
                SAVASTMedia maxMedia = null;
                SAVASTMedia medMedia = null;

                // get the min media
                for (SAVASTMedia media : ad.media) {
                    if (minMedia == null || (media.bitrate < minMedia.bitrate)) {
                        minMedia = media;
                    }
                }
                // get the max media
                for (SAVASTMedia media : ad.media) {
                    if (maxMedia == null || (media.bitrate > maxMedia.bitrate)) {
                        maxMedia = media;
                    }
                }
                // get everything in between
                for (SAVASTMedia media : ad.media) {
                    if (media != minMedia && media != maxMedia) {
                        medMedia = media;
                    }
                }

                // get media Url based on connection type
                SAUtils.SAConnectionType connectionType = SAUtils.getNetworkConnectivity(context);
                switch (connectionType) {

                    // try to get the lowest media possible
                    case cellular_unknown:
                    case cellular_2g: {
                        ad.url = minMedia != null ? minMedia.url : null;
                        break;
                    }
                    // try to get one of the medium media possible
                    case cellular_3g: {
                        ad.url = medMedia != null ? medMedia.url : null;
                        break;
                    }
                    // try to get the best media possible
                    case unknown:
                    case ethernet:
                    case wifi:
                    case cellular_4g: {
                        ad.url = maxMedia != null ? maxMedia.url : null;
                        break;
                    }
                }

                // if somehow all of that has failed, just get the last element of the list
                if (ad.url == null && ad.media.size() >= 1) {
                    ad.url = ad.media.get(ad.media.size() - 1).url;
                }

                // pass this forward
                localListener.saDidParseVAST(ad);
            }
        });
    }


    /**
     * Recursive method that handles all the VAST parsing
     *
     * @param url       url to get the vast from
     * @param startAd   ad that gets passed down
     * @param listener  a copy of the listener, which is never null at this point, thanks to the
     *                  "parseVAST" public method
     */
    private void recursiveParse(String url, final SAVASTAd startAd, final SAVASTParserInterface listener) {

        final SANetwork network = new SANetwork(executor, timeout);
        network.sendGET(url, query, header, new SANetworkInterface() {
            /**
             * Overridden SANetworkInterface method that deals with the network result - in this
             * case parsing a String payload that should contain valid VAST XML
             *
             * @param status    current status of the operation
             * @param vast      VAST XML as a string
             * @param success   whether the network request was successful or not
             */
            @Override
            public void saDidGetResponse(int status, String vast, boolean success) {

                // if not successful just return the ad as it is 'because definetly something
                // "bad" happened
                if (!success) {
                    listener.saDidParseVAST(startAd);
                }
                // else try to parse the XML data
                else try {

                    // parse the Document using the XML parser
                    Document document = SAXMLParser.parseXML(vast);

                    // get only the first XML Ad found in the VAST tag. don't bother at the moment
                    // with VAST strings that have multiple ads in them
                    Element Ad = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "Ad");

                    if (Ad == null) {
                        listener.saDidParseVAST(startAd);
                        return;
                    }

                    // use the internal "parseAdXML" method to form an SAVASTAd object
                    SAVASTAd ad = parseAdXML(Ad);

                    switch (ad.type) {
                        // if it's invalid, return the start a
                        case Invalid: {
                            listener.saDidParseVAST(startAd);
                            break;
                        }
                        // if it's inline, then I'm at the end of the VAST chain, I sum up ads and return
                        case InLine: {
                            ad.sumAd(startAd);
                            listener.saDidParseVAST(ad);
                            break;
                        }
                        // if it's a wrapper, I sum up what I have and call the method recursively
                        case Wrapper: {
                            ad.sumAd(startAd);
                            recursiveParse(ad.redirect, ad, listener);
                            break;
                        }
                    }

                } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
                    // if there's an XML error, again assume it all went to shit and don't
                    // bother summing ads or anything, just pass the start ad as it is
                    listener.saDidParseVAST(startAd);
                }
            }
        });
    }

    /**
     * Method that parses an XML containing a VAST ad into a SAVASTAd object
     *
     * @param adElement the XML Element
     * @return          a SAVASTAd object
     */
    public SAVASTAd parseAdXML(Element adElement) {

        // create the new ad
        final SAVASTAd ad = new SAVASTAd();

        // check ad type
        boolean isInLine = SAXMLParser.checkSiblingsAndChildrenOf(adElement, "InLine");
        boolean isWrapper = SAXMLParser.checkSiblingsAndChildrenOf(adElement, "Wrapper");

        if (isInLine) ad.type = SAVASTAdType.InLine;
        if (isWrapper) ad.type= SAVASTAdType.Wrapper;

        // if it's a wrapper, assign the redirect URL
        Element vastUri = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "VASTAdTagURI");
        if (vastUri != null) {
            ad.redirect = vastUri.getTextContent();
        }

        // get errors
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Error", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTEvent tracking = new SAVASTEvent();
                tracking.event = "vast_error";
                tracking.URL = e.getTextContent();
                ad.events.add(tracking);
            }
        });

        // get impressions
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Impression", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTEvent tracking = new SAVASTEvent();
                tracking.event = "vast_impression";
                tracking.URL = e.getTextContent();
                ad.events.add(tracking);
            }
        });

        // get other events, located in the creative
        Element creativeXML = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "Creative");

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "ClickThrough", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTEvent tracking = new SAVASTEvent();
                tracking.event = "vast_click_through";
                tracking.URL = e.getTextContent().replace("&amp;", "&").replace("%3A", ":").replace("%2F", "/");
                ad.events.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "ClickTracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTEvent tracking = new SAVASTEvent();
                tracking.event = "vast_click_tracking";
                tracking.URL = e.getTextContent();
                ad.events.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "Tracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTEvent tracking = new SAVASTEvent();
                tracking.event = "vast_" + e.getAttribute("event");
                tracking.URL = e.getTextContent();
                ad.events.add(tracking);
            }
        });

        // append only valid, mp4 type ads

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "MediaFile", new SAXMLParser.SAXMLIterator() {
            @Override
            public void saDidFindXMLElement(Element e) {
                SAVASTMedia media = parseMediaXML(e);
                if ((media.type.contains("mp4") || media.type.contains(".mp4")) && media.isValid()) {
                    ad.media.add(media);
                }
            }
        });

        return ad;
    }

    /**
     * Method that parses a VAST XML media element and returns a SAVASTMedia object
     *
     * @param element   the source XML element
     * @return          a SAMedia object
     */
    public SAVASTMedia parseMediaXML(Element element) {

        // create the nea media element
        SAVASTMedia media = new SAVASTMedia();

        // return empty media
        if (element == null) return media;

        // assign the media URL
        media.url = element.getTextContent().replace(" ", "");

        // set the type attribute
        media.type = element.getAttribute("type");

        // get bitrate
        String bitrate = element.getAttribute("bitrate");
        if (bitrate != null) {
            try {
                media.bitrate = Integer.parseInt(bitrate);
            } catch (Exception e) {
                // ignored
            }
        }

        // get width
        String width = element.getAttribute("width");
        if (width != null) {
            try {
                media.width = Integer.parseInt(width);
            } catch (Exception e) {
                // ignored
            }
        }

        // get height
        String height = element.getAttribute("height");
        if (height != null) {
            try {
                media.height = Integer.parseInt(height);
            } catch (Exception e) {
                // ignored
            }
        }

        return media;
    }

}
