package com.folksam.policy2;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;

public class MyListAggregator extends AbstractListAggregationStrategy<DefaultMessage> {

	@Override
	public DefaultMessage getValue(Exchange exchange) {
		return ((DefaultMessage)exchange.getIn());
	}

	@SuppressWarnings("unchecked")
	@Override
    public void onCompletion(Exchange exchange) {
        if (exchange != null && isStoreAsBodyOnCompletion()) {
            List<DefaultMessage> list = (List<DefaultMessage>) exchange.removeProperty(Exchange.GROUPED_EXCHANGE);
            if (list != null) {
            	StringBuffer buf = new StringBuffer();
            	for (DefaultMessage string : list) {
					buf.append(string.getBody(String.class));
				}
                exchange.getIn().setBody(buf.toString());
//            	exchange.setIn(list.get(0));
            }
        }
    }
}
