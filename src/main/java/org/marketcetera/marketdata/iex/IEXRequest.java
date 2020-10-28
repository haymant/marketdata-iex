package org.marketcetera.marketdata.iex;

import static org.marketcetera.marketdata.Content.DIVIDEND;
import static org.marketcetera.marketdata.Content.LATEST_TICK;
import static org.marketcetera.marketdata.Content.MARKET_STAT;
import static org.marketcetera.marketdata.Content.TOP_OF_BOOK;
import static org.marketcetera.marketdata.iex.IEXField.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.lang.StringUtils;
import org.marketcetera.marketdata.Content;
import org.marketcetera.marketdata.MarketDataRequest;
import org.marketcetera.util.misc.ClassVersion;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/* $License$ */

/**
 * Represents a market data request to the IEX market data provider.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ThreadSafe
@ClassVersion("$Id$")
class IEXRequest
{
	private static final IEXField DELIMITER_FIELD = IEXField.SYMBOL;
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IEXRequest)) {
            return false;
        }
        IEXRequest other = (IEXRequest) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
    /**
     * Create a new IEXRequest instance.
     *
     * @param inRequest a <code>MarketDataRequest</code> value
     * @param inSymbol a <code>String</code> value
     */
    IEXRequest(MarketDataRequest inRequest,
                 String inSymbol)
    {
        id = counter.incrementAndGet();
        request = inRequest;
        symbol = StringUtils.trimToNull(inSymbol);
    }
    /**
     * Get the handle value.
     *
     * @return a <code>String</code> value
     */
    String getHandle()
    {
        return handle;
    }
    /**
     * Sets the handle value.
     *
     * @param inHandle a <code>String</code> value
     */
    void setHandle(String inHandle)
    {
        handle = inHandle;
    }
    /**
     * Gets the query associated with this request.
     *
     * @return a <code>String</code> value
     */
    String getQuery()
    {
        StringBuilder query = new StringBuilder();
        query.append("symbols="); //$NON-NLS-1$
        query.append(symbol);
        query.append("&types=quote&token=");
        
        return query.toString();
    }
    /**
     * Get the request value.
     *
     * @return a <code>MarketDataRequest</code> value
     */
    MarketDataRequest getRequest()
    {
        return request;
    }
    /**
     * Gets the fields for the given content. 
     *
     * @param inContent a <code>Content</code> value
     * @return a <code>String</code> value
     */
    private String getFieldsFor(Content inContent)
    {
        synchronized(fields) {
            if(fields.isEmpty()) {
                fields.putAll(DIVIDEND,
                              Arrays.asList(new IEXField[] { EXPECTED_DIVIDEND_DATE,DIVIDEND_PAY_DATE,DIVIDEND_YIELD } ));
                fields.putAll(LATEST_TICK,
                              Arrays.asList(new IEXField[] { LAST_TRADE_DATE,LAST_TRADE_SIZE,LAST_TRADE_PRICE_ONLY,LAST_TRADE_TIME } ));
                fields.putAll(MARKET_STAT,
                              Arrays.asList(new IEXField[] { DAY_LOW,DAY_HIGH,HIGH_LIMIT,LOW_LIMIT,DAY_RANGE,REAL_TIME_DAY_RANGE,OPEN,PREVIOUS_CLOSE,VOLUME } ));
                fields.putAll(TOP_OF_BOOK,
                              Arrays.asList(new IEXField[] { REAL_TIME_BID,REAL_TIME_ASK,BID_SIZE,ASK_SIZE } ));
            }
        }
        StringBuilder builder = new StringBuilder();
        for(IEXField field : fields.get(inContent)) {
        	//add s (symbol) for each field as delimiter field, used to ease the issue with , as part of response value for a field.
        	builder.append(DELIMITER_FIELD.getCode());
            builder.append(field.getCode()).append(","); //$NON-NLS-1$
        }
        return builder.toString();
    }
    /**
     * fields in all requests
     */
    private static final List<IEXField> commonFields = Arrays.asList(new IEXField[] { STOCK_EXCHANGE,ERROR_INDICATION,SYMBOL } );
    /**
     * fields by content type
     */
    @GuardedBy("fields")
    private static final Multimap<Content,IEXField> fields = HashMultimap.create();
    /**
     * underlying request
     */
    private final MarketDataRequest request;
    /**
     * symbol of the request
     */
    private final String symbol;
    /**
     * identifier assigned to the request
     */
    private final int id;
    /**
     * handle value corresponding to the request
     */
    private volatile String handle;
    /**
     * counter used to allocate unique identifiers
     */
    private static final AtomicInteger counter = new AtomicInteger(0);
}
