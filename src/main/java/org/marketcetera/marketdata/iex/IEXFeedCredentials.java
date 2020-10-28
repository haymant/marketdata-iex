package org.marketcetera.marketdata.iex;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.marketcetera.marketdata.AbstractMarketDataFeedURLCredentials;
import org.marketcetera.marketdata.FeedException;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Credentials for the IEX market data feed.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
public class IEXFeedCredentials
        extends AbstractMarketDataFeedURLCredentials
{
    /**
     * Create a new IEXFeedCredentials instance.
     *
     * @param inURL a <code>String</code> value
     * @throws FeedException if the credentials cannot be constructed
     */
    IEXFeedCredentials(String inURL, String inToken, String inSecret)
            throws FeedException
    {
        super(inURL);
        token = inToken;
        secret = inSecret;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedCredentials#getToken()
     */
    public String getToken()
    {
        return token;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXFeedCredentials#getSecret()
     */
    public String getSecret()
    {
        return secret;
    }
    /**
     * the IEX Token
     */
    private volatile String token = null; //$NON-NLS-1$
    /**
     * the IEX Secret
     */
    private volatile String secret = null; //$NON-NLS-1$    
}
