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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.samodelspace.SATracking;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.samodelspace.SAVASTAdType;
import tv.superawesome.lib.samodelspace.SAVASTMedia;
import tv.superawesome.lib.sanetwork.request.SANetwork;
import tv.superawesome.lib.sanetwork.request.SANetworkInterface;
import tv.superawesome.lib.sautils.SAUtils;

public class SAVASTParser {

    // private context
    private Context context = null;

    /**
     * Simple constructor with a context as a parameter
     *
     * @param context current context (activity or fragment)
     */
    public SAVASTParser (Context context) {
        this.context = context;
    }

    /**
     * Main method of the parser, that will take a VAST URL and return a SAVASTAd object
     * with the help of a SAVASTParserInterface listener
     *
     * @param url       vast URL
     * @param listener  listener copy
     */
    public void parseVAST(String url, final SAVASTParserInterface listener) {

        // get a local copy of the listener and make sure it's not null
        final SAVASTParserInterface localListener = listener != null ? listener : new SAVASTParserInterface() {@Override public void didParseVAST(SAVASTAd ad) {}};

        // create the header
        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", SAUtils.getUserAgent(context)
        });

        final SANetwork network = new SANetwork();
        network.sendGET(context, url, new JSONObject(), header, new SANetworkInterface() {
            /**
             * Overridden SANetworkInterface method in which I try to parse the VAST response
             *
             * @param status        status of the GET request
             * @param VASTString    VAST string
             * @param success       success status
             */
            @Override
            public void response(int status, String VASTString, boolean success) {

                // in case of failure return a basic VAST Ad
                if (!success) {
                    localListener.didParseVAST(new SAVASTAd());
                }
                // in case of success try to parse the document
                else {
                    try {
                        // use the XML parser to parse this
                        Document document = SAXMLParser.parseXML(VASTString);

                        // get the VAST ad
                        Element Ad = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(document, "Ad");

                        // in case of error (could not find an Ad XML Element, for example)
                        if (Ad == null) {
                            localListener.didParseVAST(new SAVASTAd());
                            return;
                        }

                        // finally parse the ad XML into a SAVASTAd object
                        final SAVASTAd ad = parseAdXML(Ad);

                        // inline case
                        if (ad.vastType == SAVASTAdType.InLine) {
                            localListener.didParseVAST(ad);
                        }
                        // wrapper case
                        else if (ad.vastType == SAVASTAdType.Wrapper) {
                            parseVAST(ad.vastRedirect, new SAVASTParserInterface() {
                                /**
                                 * Overridden implementation of the SAVASTParserInterface method.
                                 * In this case we parse the Wrapper tag again and
                                 * we sum the two ads.
                                 *
                                 * @param wrapper the next ad in the chain
                                 */
                                @Override
                                public void didParseVAST(SAVASTAd wrapper) {
                                    // sum ads after their own internal logic
                                    ad.sumAd(wrapper);

                                    // respond with the summed ad
                                    localListener.didParseVAST(ad);
                                }
                            });
                        }
                        // some other invalid case
                        else {
                            localListener.didParseVAST(new SAVASTAd());
                        }
                    }
                    // in case of error just send the same empty ad
                    catch (ParserConfigurationException | IOException | SAXException e) {
                        localListener.didParseVAST(new SAVASTAd());
                    }
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

        if (isInLine) ad.vastType = SAVASTAdType.InLine;
        if (isWrapper) ad.vastType= SAVASTAdType.Wrapper;

        // if it's a wrapper, assign the redirect URL
        Element vastUri = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "VASTAdTagURI");
        if (vastUri != null) {
            ad.vastRedirect = vastUri.getTextContent();
        }

        // get errors
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Error", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "error";
                tracking.URL = e.getTextContent();
                ad.vastEvents.add(tracking);
            }
        });

        // get impressions
        SAXMLParser.searchSiblingsAndChildrenOf(adElement, "Impression", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "impression";
                tracking.URL = e.getTextContent();
                ad.vastEvents.add(tracking);
            }
        });

        // get other events, located in the creative
        Element creativeXML = SAXMLParser.findFirstInstanceInSiblingsAndChildrenOf(adElement, "Creative");

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "ClickThrough", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "click_through";
                tracking.URL = e.getTextContent().replace("&amp;", "&").replace("%3A", ":").replace("%2F", "/");
                ad.vastEvents.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "ClickTracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "click_tracking";
                tracking.URL = e.getTextContent();
                ad.vastEvents.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "CustomClicks", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = "custom_clicks";
                tracking.URL = e.getTextContent();
                ad.vastEvents.add(tracking);
            }
        });

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "Tracking", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SATracking tracking = new SATracking();
                tracking.event = e.getAttribute("event");
                tracking.URL = e.getTextContent();
                ad.vastEvents.add(tracking);
            }
        });

        // get media files

        final List<SAVASTMedia> mediaFiles = new ArrayList<>();
        final SAVASTMedia[] defaultMedia = {null};

        SAXMLParser.searchSiblingsAndChildrenOf(creativeXML, "MediaFile", new SAXMLParser.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                SAVASTMedia media = parseMediaXML(e);
                if (media.type.contains("mp4") || media.type.contains(".mp4")) {
                    mediaFiles.add(media);
                    defaultMedia[0] = media;
                }
            }
        });

        // depending on the type of connection, chose an appropriate media file
        if (mediaFiles.size() >= 1 && defaultMedia[0] != null) {
            // get the videos at different bit rates
            List<SAVASTMedia> bitrate360 = new ArrayList<>();
            for (SAVASTMedia m : mediaFiles) {
                if (m.bitrate == 360) {
                    bitrate360.add(m);
                }
            }
            List<SAVASTMedia> bitrate540 = new ArrayList<>();
            for (SAVASTMedia m : mediaFiles) {
                if (m.bitrate == 540) {
                    bitrate540.add(m);
                }
            }
            List<SAVASTMedia> bitrate720 = new ArrayList<>();
            for (SAVASTMedia m : mediaFiles) {
                if (m.bitrate == 720) {
                    bitrate720.add(m);
                }
            }

            SAVASTMedia media360 = bitrate360.size() >= 1 ? bitrate360.get(0) : new SAVASTMedia();
            SAVASTMedia media540 = bitrate540.size() >= 1 ? bitrate540.get(0) : new SAVASTMedia();
            SAVASTMedia media720 = bitrate720.size() >= 1 ? bitrate720.get(0) : new SAVASTMedia();

            SAUtils.SAConnectionType connectionType = SAUtils.getNetworkConnectivity(context);

            // when connection is:
            //  1) cellular unknown
            //  2) 2g
            // try to get the lowest media possible
            if (connectionType == SAUtils.SAConnectionType.cellular_unknown ||
                    connectionType == SAUtils.SAConnectionType.cellular_2g) {
                ad.mediaUrl = media360.mediaUrl;
            }
            // when connection is:
            //  1) 3g
            // try to get the medium media
            else if (connectionType == SAUtils.SAConnectionType.cellular_3g) {
                ad.mediaUrl = media540.mediaUrl;
            }
            // when connection is:
            //  1) unknown
            //  2) 4g
            //  3) wifi
            //  4) ethernet
            // try to get the best media available
            else {
                ad.mediaUrl = media720.mediaUrl;
            }
        }

        // if somehow no media was added (because of legacy VAST)
        // then just add the default media (which should be the 720 one)
        if (ad.mediaUrl == null && defaultMedia[0] != null) {
            ad.mediaUrl = defaultMedia[0].mediaUrl;
        }

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
        media.mediaUrl = element.getTextContent().replace(" ", "");

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
