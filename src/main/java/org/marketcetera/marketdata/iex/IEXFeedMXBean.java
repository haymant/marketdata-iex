package org.marketcetera.marketdata.iex;

import javax.management.MXBean;

import org.marketcetera.marketdata.MarketDataModuleMXBean;
import org.marketcetera.module.DisplayName;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Provides an MX interface for the IEX market data feed.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@MXBean(true)
@DisplayName("Management Interface for the IEX Marketdata Feed")
@ClassVersion("$Id$")
public interface IEXFeedMXBean
        extends MarketDataModuleMXBean
{
    /**
     * Returns the URL that describes the location of the IEX server.
     *
     * @return a <code>String</code> value
     */
    @DisplayName("The URL for the IEX server")
    public String getURL();
    /**
     * Sets the URL that describes the location of the Marketcetera Exchange server.
     *
     * @param inURL a <code>String</code> value
     */
    @DisplayName("The URL for the IEX server")
    public void setURL(@DisplayName("The URL for the IEX server")
                       String inURL);
    /**
     * Returns the IEX Token.
     *
     * @return a <code>String</code> value
     */
    @DisplayName("The IEX Token")
    public String getToken();
    /**
     * Sets the IEX Token.
     *
     * @param inURL a <code>String</code> value
     */
    @DisplayName("The IEX Token")
    public void setToken(@DisplayName("The IEX Token")
                       String inToken);    
    /**
     * Returns the IEX Sceret.
     *
     * @return a <code>String</code> value
     */
    @DisplayName("The IEX Secret")
    public String getSecret();
    /**
     * Sets the IEX Secret.
     *
     * @param inURL a <code>String</code> value
     */
    @DisplayName("The IEX Secret")
    public void setSecret(@DisplayName("The IEX Sceret")
                       String inSecret);      
    /**
     * Gets the interval at which to refresh market data in ms.
     *
     * @return a <code>String</code> value
     */
    @DisplayName("The rate at which to refresh market data")
    public String getRefreshInterval();
    /**
     * Sets the interval at which to refresh market data in ms.
     *
     * @param inRefreshInterval a <code>String</code> value
     */
    @DisplayName("The rate at which to refresh market data")
    public void setRefreshInterval(@DisplayName("The rate at which to refresh market data")
                                   String inRefreshInterval);
    /**
     * Gets the number of requests that have been made.
     *
     * @return a <code>long</code> value
     */
    @DisplayName("The number of requests made since start or reset")
    public long getRequestCounter();
    /**
     * Resets the request counter.
     */
    @DisplayName("Resets the request counter")
    public void resetRequestCounter();
}
