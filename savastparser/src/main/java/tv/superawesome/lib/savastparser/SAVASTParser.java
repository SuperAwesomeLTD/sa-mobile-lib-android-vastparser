package tv.superawesome.lib.savastparser;

import android.util.Log;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sanetwork.request.*;
import tv.superawesome.lib.sanetwork.file.*;
import tv.superawesome.lib.samodelspace.SAVASTAd;
import tv.superawesome.lib.samodelspace.SAVASTAdType;
import tv.superawesome.lib.samodelspace.SAVASTCreative;
import tv.superawesome.lib.samodelspace.SAVASTCreativeType;
import tv.superawesome.lib.samodelspace.SAVASTMediaFile;
import tv.superawesome.lib.samodelspace.SAVASTTracking;
import tv.superawesome.lib.sautils.SAApplication;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public class SAVASTParser {

    /**
     * The main parse function of the parser
     * @param url - URL where the VAST resides
     * @param listener - a SAVASTParserInterface listener object
     */
    public void parseVASTAds(final String url, final SAVASTParserInterface listener) {

        this.parseVAST(url, new SAVASTParserInterface() {
            @Override
            public void didParseVAST(final SAVASTAd ad) {

                // do a final check for wrapper ads that have null or incorrect data
                if (ad == null || ad.creative.playableMediaUrl == null || ad.creative.mediaFiles.size() == 0) {
                    listener.didParseVAST(null);
                }
                else {
                    SAFileDownloader.getInstance().setupDownloader(SAApplication.getSAApplicationContext());
                    SAFileDownloader.getInstance().downloadFile(ad.creative.playableMediaUrl, ad.creative.playableDiskUrl, new SAFileDownloaderInterface() {
                        @Override
                        public void response(boolean success) {
                            if (!success) {
                                ad.creative.isOnDisk = false;
                                listener.didParseVAST(null);
                            } else {
                                ad.creative.isOnDisk = true;
                                listener.didParseVAST(ad);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * The main parseing function
     * @param url - URL to the VAST
     * @return an array of VAST ads
     */
    private void parseVAST(String url, final SAVASTParserInterface listener) {
        /** step 1: get the XML */
        JSONObject header = SAJsonParser.newObject(new Object[]{
                "Content-Type", "application/json",
                "User-Agent", SAUtils.getUserAgent()
        });

        final SANetwork network = new SANetwork();
        network.sendGET(SAApplication.getSAApplicationContext(), url, new JSONObject(), header, new SANetworkInterface() {
            @Override
            public void response(int status, String VAST, boolean success) {
                // error case
                if (!success) {
                    listener.didParseVAST(null);
                    return;
                }

                // continue with success
                Document doc = null;

                try {
                    // get the XML doc and the root element
                    doc = SAXML.parseXML(VAST);
                    final Element root = (Element) doc.getElementsByTagName("VAST").item(0);

                    // do a check
                    if (root == null) {
                        listener.didParseVAST(null);
                        return;
                    }
                    if (!SAXML.checkSiblingsAndChildrenOf(root, "Ad")) {
                        listener.didParseVAST(null);
                        return;
                    }

                    // get the proper vast ad
                    Element adXML = SAXML.findFirstInstanceInSiblingsAndChildrenOf(root, "Ad");
                    final SAVASTAd ad = parseAdXML(adXML);

                    // inline case
                    if (ad.type == SAVASTAdType.InLine) {
                        listener.didParseVAST(ad);
                        return;
                    }
                    // wrapper case
                    else if (ad.type == SAVASTAdType.Wrapper) {
                        parseVAST(ad.redirectUri, new SAVASTParserInterface() {
                            @Override
                            public void didParseVAST(SAVASTAd wrapper) {
                                if (wrapper != null) {
                                    ad.sumAd(wrapper);
                                }
                                listener.didParseVAST(ad);
                                return;
                            }
                        });
                    }
                    // some other invalid case
                    else {
                        listener.didParseVAST(null);
                    }
                } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
                    Log.d("SuperAwesome", e.toString());
                    listener.didParseVAST(null);
                }
            }
        });
    }

    /**
     * Function that parses a VAST XML "Ad" element into a SAVASTAd model
     * @param adElement XML elemend
     * @return a SAVAST ad object
     */
    public static SAVASTAd parseAdXML(Element adElement) {
        final SAVASTAd ad = new SAVASTAd();

        /** get ID and sequence */
        ad.id = adElement.getAttribute("id");
        ad.sequence = adElement.getAttribute("sequence");

        /** get type */
        ad.type = SAVASTAdType.Invalid;
        boolean isInLine = SAXML.checkSiblingsAndChildrenOf(adElement, "InLine");
        boolean isWrapper = SAXML.checkSiblingsAndChildrenOf(adElement, "Wrapper");

        if (isInLine) ad.type = SAVASTAdType.InLine;
        if (isWrapper) ad.type = SAVASTAdType.Wrapper;

        /** init ad arrays */
        ad.errors = new ArrayList<>();
        ad.impressions = new ArrayList<>();

        Element vastUri = SAXML.findFirstInstanceInSiblingsAndChildrenOf(adElement, "VASTAdTagURI");
        if (vastUri != null) {
            ad.redirectUri = vastUri.getTextContent();
        }

        /** get errors */
        SAXML.searchSiblingsAndChildrenOf(adElement, "Error", new SAXML.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
                String error = e.getTextContent();
                ad.errors.add(error);
            }
        });

        /** get impressions */
        ad.isImpressionSent = false;
        SAXML.searchSiblingsAndChildrenOf(adElement, "Impression", new SAXML.SAXMLIterator() {
            @Override
            public void foundElement(Element e) {
               ad.impressions.add(e.getTextContent());
            }
        });

        Element creativeXML = SAXML.findFirstInstanceInSiblingsAndChildrenOf(adElement, "Creative");
        ad.creative = parseCreativeXML(creativeXML);

        return ad;
    }

    /**
     * Function that parses a XML "Linear" VAST element and returns a SAVASTLinearCreative model
     * @param element a XML element
     * @return a valid SAVASTLinearCreative model
     */
    public static SAVASTCreative parseCreativeXML(Element element){
        /**
         * first find out what kind of content this creative has
         * is it Linear, NonLinear or CompanionAds?
         */
        boolean isLinear = SAXML.checkSiblingsAndChildrenOf(element, "Linear");

        /** init as a linear Creative */
        if (isLinear) {
            final SAVASTCreative creative = new SAVASTCreative();

            /** get attributes */
            creative.type = SAVASTCreativeType.Linear;
            creative.id = element.getAttribute("id");
            creative.sequence = element.getAttribute("sequence");

            /** create arrays */
            creative.mediaFiles = new ArrayList<>();
            creative.trackingEvents = new ArrayList<>();
            creative.clickTracking = new ArrayList<>();
            creative.customClicks = new ArrayList<>();

            /** populate duration */
            SAXML.searchSiblingsAndChildrenOf(element, "duration", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    creative.duration = e.getTextContent();
                }
            });

            /** populate click through */
            SAXML.searchSiblingsAndChildrenOf(element, "ClickThrough", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    creative.clickThrough = e.getTextContent().replace("&amp;","&").replace("%3A",":").replace("%2F", "/");
                }
            });

            /** populate Click Tracking */
            SAXML.searchSiblingsAndChildrenOf(element, "ClickTracking", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    creative.clickTracking.add(e.getTextContent());
                }
            });

            /** populate Custom Clicks */
            SAXML.searchSiblingsAndChildrenOf(element, "CustomClicks", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    creative.customClicks.add(e.getTextContent());
                }
            });

            /** populate Tracking */
            SAXML.searchSiblingsAndChildrenOf(element, "Tracking", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    SAVASTTracking tracking = new SAVASTTracking();
                    tracking.event = e.getAttribute("event");
                    tracking.url = e.getTextContent();
                    creative.trackingEvents.add(tracking);
                }
            });

            /** populate Media Files */
            SAXML.searchSiblingsAndChildrenOf(element, "MediaFile", new SAXML.SAXMLIterator() {
                @Override
                public void foundElement(Element e) {
                    SAVASTMediaFile mediaFile = new SAVASTMediaFile();
                    mediaFile.width = e.getAttribute("width");
                    mediaFile.height = e.getAttribute("height");
                    mediaFile.type = e.getAttribute("type");
                    mediaFile.url = e.getTextContent();

                    if (mediaFile.type.contains("mp4") || mediaFile.type.contains(".mp4")) {
                        creative.mediaFiles.add(mediaFile);
                    }
                }
            });

            /** add the playable media file */
            if (creative.mediaFiles.size() > 0) {
                creative.playableMediaUrl = creative.mediaFiles.get(0).url;
                if (creative.playableMediaUrl != null) {
                    creative.playableDiskUrl = SAFileDownloader.getInstance().getDiskLocation();
                }
            }

            /** return creative */
            return creative;
        }

        return null;
    }
}
