package com.folksam.policy2;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MyPolicyAggregationStrategy implements AggregationStrategy {

    private static final String CHILD_ELEMENT = "ns1:Policy";

	@Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }

        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);
        

        try {
            String xmlOutputStr = mergeByTag(oldBody, newBody, CHILD_ELEMENT);

            // Set the new xml string as new body to the exchange
            oldExchange.getIn().setBody(xmlOutputStr);

            System.out.println("AGGREGATED: \n" + xmlOutputStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return oldExchange;
    }

	/**
	 * Merge two xml documents by extracting child elements of oldBody 
	 * by tagname and inserting them into newBody
	 */
	protected String mergeByTag(String oldBody, String newBody, String tagname) throws ParserConfigurationException, SAXException,
			IOException, TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		// Instantiate a document builder
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();

		// Get the xml documents to be aggregated
		Document oldDoc = builder.parse(new InputSource(new StringReader(oldBody)));            
		Document newDoc = builder.parse(new InputSource(new StringReader(newBody)));

		// Get the list of elements to be aggregated from each xml document
		NodeList oldNodes = oldDoc.getElementsByTagName(tagname);
		NodeList newNodes = newDoc.getElementsByTagName(tagname);

		// Append the old nodes to the new list of nodes
		for (int i = 0; i < oldNodes.getLength(); i++) {
		    Node n = (Node) newDoc.importNode(oldNodes.item(i), true);
		    newNodes.item(i).getParentNode().appendChild(n);
		}

		// Instantiate a transformer
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		// Transform the new document
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(newDoc);
		transformer.transform(source, result);

		// Get the document as a string
		String xmlOutputStr = result.getWriter().toString();
		return xmlOutputStr;
	}

}
