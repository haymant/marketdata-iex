package org.marketcetera.marketdata.iex;

import org.marketcetera.core.CoreException;
import org.marketcetera.marketdata.AbstractMarketDataFeedFactory;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Constructs {@link IEXFeed} objects.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
public class IEXFeedFactory
        extends AbstractMarketDataFeedFactory<IEXFeed,IEXFeedCredentials>
{
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.IMarketDataFeedFactory#getMarketDataFeed()
     */
    @Override
    public synchronized IEXFeed getMarketDataFeed()
            throws CoreException
    {
        if(feed == null) {
            feed = new IEXFeed(getProviderName(),
                                 new IEXClientFactory() {
                                    @Override
                                    public IEXClient getClient(IEXFeedServices inFeedServices)
                                    {
                                        return new IEXClientImpl(inFeedServices);
                                    }
            });
        }
        return feed;
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.IMarketDataFeedFactory#getProviderName()
     */
    @Override
    public String getProviderName()
    {
        return PROVIDER_NAME;
    }
    /**
     * feed instance
     */
    private static IEXFeed feed;
    /**
     * name of the iex provider
     */
    static final String PROVIDER_NAME = "iex"; //$NON-NLS-1$
}
