package org.marketcetera.marketdata.iex;

import org.marketcetera.marketdata.AbstractMarketDataFeedToken;
import org.marketcetera.marketdata.MarketDataFeedTokenSpec;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Token which represents a market data request to the IEX market data feed.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
public class IEXFeedToken
        extends AbstractMarketDataFeedToken<IEXFeed>
{
    /**
     * Create a new IEXFeedToken instance.
     *
     * @param inTokenSpec a <code>MarketDataFeedTokenSpec</code> value
     * @param inFeed a <code>IEXFeed</code> value
     */
    IEXFeedToken(MarketDataFeedTokenSpec inTokenSpec,
                   IEXFeed inFeed)
    {
        super(inTokenSpec,
              inFeed);
    }
}
