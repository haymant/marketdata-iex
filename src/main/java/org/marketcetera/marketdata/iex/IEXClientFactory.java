package org.marketcetera.marketdata.iex;

import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Constructs {@link IEXClient} objects.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
interface IEXClientFactory
{
    /**
     * Constructs a <code>IEXClient</code> object. 
     *
     * @return a <code>IEXClient</code> value
     */
    IEXClient getClient(IEXFeedServices inFeedServices);
}
