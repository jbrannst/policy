package com.folksam.policy2;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyAggregator implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange != null){
			String newBody = newExchange.getIn().getBody(String.class);
			String oldBody = oldExchange.getIn().getBody(String.class);
			String resultBody = newBody + oldBody;
			newExchange.getIn().setBody(resultBody);
		}
		return newExchange;
	}

}
