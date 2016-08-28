package com.folksam.policy2;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MyPolicyAggregationStrategyTest {

	@Test
	public void testAggregate() throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		String policys = getXmlFileAsString("/policys.xml");
		String expected = getXmlFileAsString("/policys_result.xml");
		String merged = new MyPolicyAggregationStrategy().mergeByTag(policys , policys, "ns1:Policy");
		Assert.assertEquals(StringUtils.countMatches(expected, "Policy"), StringUtils.countMatches(merged, "Policy"));
		Assert.assertEquals(StringUtils.deleteWhitespace(expected), StringUtils.deleteWhitespace(merged));
	}

	private String getXmlFileAsString(String filename) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(filename));
	}

}
