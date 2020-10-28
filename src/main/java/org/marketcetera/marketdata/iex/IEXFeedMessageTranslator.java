package org.marketcetera.marketdata.iex;

import java.util.ArrayList;
import java.util.List;

import org.marketcetera.marketdata.DataRequestTranslator;
import org.marketcetera.marketdata.MarketDataRequest;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Translates market data requests to a format IEX can understand.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
public enum IEXFeedMessageTranslator
        implements DataRequestTranslator<List<IEXRequest>>
{
    INSTANCE;
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.DataRequestTranslator#fromDataRequest(org.marketcetera.marketdata.MarketDataRequest)
     */
    @Override
    public List<IEXRequest> fromDataRequest(MarketDataRequest inRequest)
    {
        List<IEXRequest> requests = new ArrayList<IEXRequest>();
        for(String symbol : inRequest.getSymbols()) {
            requests.add(new IEXRequest(inRequest,
                                          symbol));
        }
        return requests;
    }
}
