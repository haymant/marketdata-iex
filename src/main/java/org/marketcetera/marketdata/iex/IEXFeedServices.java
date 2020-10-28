package org.marketcetera.marketdata.iex;

import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Services provided by the IEX feed.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
interface IEXFeedServices
{
    /**
     * Indicates receipt of market data.
     *
     * @param inHandle a <code>String</code> value
     * @param inData an <code>Object</code> value
     */
    void doDataReceived(String inHandle,
                        Object inData);
    /**
     * Gets the interval at which to refresh market data requests.
     *
     * @return an <code>int</code> value in ms
     */
    int getRefreshInterval();
}
