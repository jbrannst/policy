package com.folksam.policy2;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MyAggregationStrategyTest {

	@Test
	public void testAggregate() throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		String policys = getXmlFileAsString("/policys.xml");
		String expected = getXmlFileAsString("/policys_result.xml");
		Assert.assertEquals(expected, new MyAggregationStrategy().mergeByTag(policys , policys, "policys"));
	}

	private String getXmlFileAsString(String filename) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(filename));
	}

}
