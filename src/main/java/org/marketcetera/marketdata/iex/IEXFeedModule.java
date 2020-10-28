package org.marketcetera.marketdata.iex;

import javax.management.AttributeChangeNotification;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.marketcetera.core.CoreException;
import org.marketcetera.marketdata.AbstractMarketDataModule;
import org.marketcetera.marketdata.MarketDataModuleMXBean;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Provides access to IEX market data.
 * 
 * <p>
 * Module Features
 * <table summary="IEXFeedModule capabilities">
 * <tr><th>Capabilities</th><td>Data Emitter</td></tr>
 * <tr><th>Stops data flows</th><td>No</td></tr>
 * <tr><th>Start Operation</th><td>Starts the feed, logs into it.</td></tr>
 * <tr><th>Stop Operation</th><td>Stops the data feed.</td></tr>
 * <tr><th>Management Interface</th><td>{@link MarketDataModuleMXBean}</td></tr>
 * <tr><th>MX Notification</th><td>{@link AttributeChangeNotification}
 * whenever {@link #getFeedStatus()} changes. </td></tr>
 * </table>
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
public class IEXFeedModule
        extends AbstractMarketDataModule<IEXFeedToken,IEXFeedCredentials>
        implements IEXFeedMXBean
{
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#getURL()
     */
    @Override
    public String getURL()
    {
        return url;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#setURL(java.lang.String)
     */
    @Override
    public void setURL(String inURL)
    {
        url = StringUtils.trimToNull(inURL);
        Validate.notNull(url,
                         Messages.MISSING_URL.getText());
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#getToken()
     */
    @Override
    public String getToken()
    {
        return token;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#setToken(java.lang.String)
     */
    @Override
    public void setToken(String inToken)
    {
        token = StringUtils.trimToNull(inToken);
        Validate.notNull(url,
                         Messages.MISSING_URL.getText());
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#getSecret()
     */
    @Override
    public String getSecret()
    {
        return secret;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#setSecret(java.lang.String)
     */
    @Override
    public void setSecret(String inSecret)
    {
        secret = StringUtils.trimToNull(inSecret);
        Validate.notNull(url,
                         Messages.MISSING_URL.getText());
    }    
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#getRefreshInterval()
     */
    @Override
    public String getRefreshInterval()
    {
        return Integer.toString(refreshInterval);
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#setRefreshInterval(int)
     */
    @Override
    public void setRefreshInterval(String inRefreshInterval)
    {
        String rawInterval = StringUtils.trimToNull(inRefreshInterval); 
        if(rawInterval == null) {
            refreshInterval = 0;
        } else {
            refreshInterval = Integer.parseInt(rawInterval);
        }
        feed.setRefreshInterval(refreshInterval);
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#getRequestCounter()
     */
    @Override
    public long getRequestCounter()
    {
        return feed.getRequestCounter();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedMXBean#resetRequestCounter()
     */
    @Override
    public void resetRequestCounter()
    {
        feed.resetCounter();
    }
    /**
     * Create a new IEXFeedModule instance.
     * 
     * @throws CoreException 
     */
    IEXFeedModule()
            throws CoreException
    {
        super(IEXFeedModuleFactory.INSTANCE_URN,
              new IEXFeedFactory().getMarketDataFeed());
        feed = new IEXFeedFactory().getMarketDataFeed();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.AbstractMarketDataModule#getCredentials()
     */
    @Override
    protected IEXFeedCredentials getCredentials()
            throws CoreException
    {
        return new IEXFeedCredentials(url, token, secret);
    }
    /**
     * the underlying feed
     */
    private final IEXFeed feed;
    /**
     * the URL at which IEX provides the data
     */
    private volatile String url = "https://sandbox.iexapis.com/stable/stock/market/batch?"; //$NON-NLS-1$
    /**
     * the IEX Token
     */
    private volatile String token = null; //$NON-NLS-1$
    /**
     * the IEX Secret
     */
    private volatile String secret = null; //$NON-NLS-1$
    /**
     * the interval at which to get a new quote
     */
    private volatile int refreshInterval = 2500;
}
