package org.marketcetera.marketdata.iex;

import org.marketcetera.util.misc.ClassVersion;
import org.springframework.context.Lifecycle;

/* $License$ */

/**
 * Provides access to the IEX data source.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
interface IEXClient
        extends Lifecycle
{
    /**
     * Logs in to the IEX data source with the given credentials.
     *
     * @param inCredentials a <code>IEXFeedCredentials</code> value
     * @return a <code>boolean</code> value
     */
    boolean login(IEXFeedCredentials inCredentials);
    /**
     * Logs out from the IEX data source.
     */
    void logout();
    /**
     * Indicates if the connection is currently logged in or not. 
     *
     * @return a <code>boolean</code> value
     */
    boolean isLoggedIn();
    /**
     * Executes the given request.
     *
     * @param inRequest a <code>IEXRequest</code> value
     */
    void request(IEXRequest inRequest);
    /**
     * Cancels th given request.
     *
     * @param inRequest a <code>IEXRequest</code> value
     */
    void cancel(IEXRequest inRequest);
    /**
     * Gets the current count of requests. 
     *
     * @return a <code>long</code> value
     */
    long getRequestCounter();
    /**
     * Resets the count of requests.
     */
    void resetRequestcounter();
}
