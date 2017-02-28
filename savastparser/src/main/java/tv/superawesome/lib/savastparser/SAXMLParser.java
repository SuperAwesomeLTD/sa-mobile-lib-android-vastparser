/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.savastparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Class that abstracts away the complexities of XML parsing into a series of utility methods
 */
public class SAXMLParser {

    /**
     * Function that parses a XML document
     *
     * @param xml   xml string
     * @return      a Document object
     * @throws      ParserConfigurationException
     * @throws      IOException
     * @throws      SAXException
     */
    public static Document parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, NullPointerException {
        if (xml == null) {
            throw new NullPointerException();
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Method that makes a search in the current node's siblings and children by a
     * given "name" string parameter.
     * It will return the result into the list of XML elements given as paramter.
     *
     * @param node  the XML parent node
     * @param name  the XML name ot search for
     * @param list  a list of returned Elements
     */
    public static void searchSiblingsAndChildrenOf(Node node, String name, List<Element> list) {

        // guard against null node & name
        if (node == null || name == null) return;

        // get the sub-nodes
        NodeList subnodes = new NodeList() {
            @Override
            public Node item(int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            subnodes = ((Element) node).getElementsByTagName(name);
        }
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            subnodes = ((Document) node).getElementsByTagName(name);
        }

        for (int i = 0; i < subnodes.getLength(); i++){
            if (subnodes.item(i).getNodeType() == Node.ELEMENT_NODE){
                // get element
                Element e = (Element) subnodes.item(i);

                // add elements
                if (e.getTagName().equals(name)){
                    list.add(e);
                }

                // search recursively
                if (e.getFirstChild() != null){
                    searchSiblingsAndChildrenOf(e.getFirstChild(), name, list);
                }
            }
        }
    }

    /**
     * Method that returns a list of XML elements after performing a thorough search of all
     * the node parameter's siblings and children, by a given "name".
     *
     * @param node  the parent node
     * @param name  the name to search for
     * @return      a List of XML elements
     */
    public static List<Element> searchSiblingsAndChildrenOf(Node node, String name) {
        List<Element> list = new ArrayList<>();
        searchSiblingsAndChildrenOf(node, name, list);
        return list;
    }

    /**
     * Method that iterates over each children or sibling of the given XML node paramter.
     * The block is a method with one XML element parameter.
     *
     * @param node  the XML parent node
     * @param name  the name of the XML element
     * @param block a block of type SAXMLIterator
     */
    public static void  searchSiblingsAndChildrenOf(Node node, String name, SAXMLIterator block) {
        List<Element> list = searchSiblingsAndChildrenOf(node, name);
        for (Element aList : list) {
            block.saDidFindXMLElement(aList);
        }
    }

    /**
     * Finds only the first instance of a XML element with given name by searching in all of
     * the node parameter's siblings and children.
     *
     * @param node  the parent node
     * @param name  the name to search for
     * @return      the first element found
     */
    public static Element findFirstInstanceInSiblingsAndChildrenOf(Node node, String name){
        List<Element> list = searchSiblingsAndChildrenOf(node, name);
        if (list.size() >= 1){
            return list.get(0);
        }

        return null;
    }

    /**
     * Method that checks if in all children and siblings of a XML node, there exists
     * at least one element with given name
     *
     * @param node  parent XML node
     * @param name  name to search for
     * @return      true if found any, false otherwise
     */
    public static boolean checkSiblingsAndChildrenOf(Node node, String name){
        List<Element> list = searchSiblingsAndChildrenOf(node, name);
        return list.size() > 0;
    }

    /**
     * Internal interface used by the parser
     */
    public interface SAXMLIterator {

        /**
         * Function that needs to be implemented by the XML Parser
         * @param e XML element as callback return parameter
         */
        void saDidFindXMLElement (Element e);
    }
}
