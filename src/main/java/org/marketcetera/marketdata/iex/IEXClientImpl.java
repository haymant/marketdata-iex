package org.marketcetera.marketdata.iex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.GuardedBy;

import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksType;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

import org.marketcetera.util.log.SLF4JLoggerProxy;
import org.marketcetera.util.misc.ClassVersion;

/* $License$ */

/**
 * Provides a <code>IEXClient</code> implementation.
 *
 * @author <a href="mailto:colin@marketcetera.com">Colin DuPlantis</a>
 * @version $Id$
 * @since 2.1.4
 */
@ClassVersion("$Id$")
class IEXClientImpl
        implements Runnable, IEXClient
{
    /* (non-Javadoc)
     * @see org.springframework.context.Lifecycle#isRunning()
     */
    @Override
    public boolean isRunning()
    {
        return isRunning.get();
    }
    /* (non-Javadoc)
     * @see org.springframework.context.Lifecycle#start()
     */
    @Override
    public synchronized void start()
    {
        if(isRunning.get()) {
            return;
        }
        thread = new Thread(this,
                            "IEX Client Thread"); //$NON-NLS-1$
        thread.start();
        isRunning.set(true);
    }
    /* (non-Javadoc)
     * @see org.springframework.context.Lifecycle#stop()
     */
    @Override
    public synchronized void stop()
    {
        if(!isRunning.get()) {
            return;
        }
        try {
            if(thread != null) {
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException ignored) {}
            }
        } finally {
            thread = null;
            isRunning.set(false);
        }
    }
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        try {
            while(isRunning.get()) {
                synchronized(requests) {
                    for(IEXRequest request : requests) {
                        try {
                            feedServices.doDataReceived(request.getHandle(),
                                                        submit(request));
                        } catch (Exception e) {
                            SLF4JLoggerProxy.debug(IEXClientImpl.class,
                                                   e,
                                                   "Retrying...");
                        } 
                    }
                }
                Thread.sleep(feedServices.getRefreshInterval());
            }
        } catch (InterruptedException e) {
        }
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#login(org.marketcetera.marketdata.iex.IEXFeedCredentials)
     */
    @Override
    public boolean login(IEXFeedCredentials inCredentials)
    {
        credentials = inCredentials;
        cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1_SANDBOX, //.IEX_CLOUD_V1,
                new IEXCloudTokenBuilder()
                .withPublishableToken(inCredentials.getToken())
                .withSecretToken(inCredentials.getSecret())
                .build());
        start();
        return isRunning();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#logout()
     */
    @Override
    public void logout()
    {
        stop();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#isLoggedIn()
     */
    @Override
    public boolean isLoggedIn()
    {
        return isRunning();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#request(org.marketcetera.marketdata.iex.IEXRequest)
     */
    @Override
    public void request(IEXRequest inRequest)
    {
        synchronized(requests) {
            requests.add(inRequest);
        }
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#cancel(org.marketcetera.marketdata.iex.IEXRequest)
     */
    @Override
    public void cancel(IEXRequest inRequest)
    {
        synchronized(requests) {
            requests.remove(inRequest);
        }
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#getRequestCounter()
     */
    @Override
    public long getRequestCounter()
    {
        return requestCounter.get();
    }
    /* (non-Javadoc)
     * @see org.marketcetera.marketdata.iex.IEXClient#resetRequestcounter()
     */
    @Override
    public void resetRequestcounter()
    {
        requestCounter.set(0);
    }
    /**
     * Create a new IEXClient instance.
     *
     * @param inFeedServices a <code>IEXFeedServices</code> value
     */
    IEXClientImpl(IEXFeedServices inFeedServices)
    {
        feedServices = inFeedServices;
    }
    /**
     * Submits the given request and returns the response from IEX.
     *
     * @param inRequest a <code>IEXRequest</code> value
     * @return a <code>BatchStocks</code> value
     * @throws IOException if an error occurs submitting the request
     */
    private Map<String, BatchStocks> submit(IEXRequest inRequest)
            throws IOException, IllegalArgumentException
    {
    	BatchMarketStocksRequestBuilder builder = new BatchMarketStocksRequestBuilder()
    			.withSymbols(new ArrayList<String>(inRequest.getRequest().getSymbols()))
    	        .addType(BatchStocksType.QUOTE);
    	if (inRequest.getRequest().getParameters() != null) {
    		for (Map.Entry<String, String> param: inRequest.getRequest().getParameters().entrySet()) {
    			if (param.getKey().equals("range")) {
    				ChartRange range = ChartRange.getValueFromCode(param.getValue());
    				builder.withChartRange(range);
    			} else if (param.getKey().equals("types")) {
    				for (String type : param.getValue().split(",")) {
    					builder.addType(BatchStocksType.valueOf(type));
    				}
    			}
    		}
    	}
    	Map<String, BatchStocks> result = cloudClient.executeRequest(builder.build());
    	return result;
    }
    /**
     * iextrading4j client
     */
    private volatile IEXCloudClient cloudClient;

    /**
     * IEX feed services value
     */
    private final IEXFeedServices feedServices;
    /**
     * thread used for submitting requests
     */
    private volatile Thread thread;
    /**
     * credentials used for the IEX connection
     */
    private volatile IEXFeedCredentials credentials;
    /**
     * indicates if the client is running
     */
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    /**
     * the active IEX requests
     */
    @GuardedBy("requests")
    private final Set<IEXRequest> requests = new HashSet<IEXRequest>();
    /**
     * the counter used to keep track of the number of requests
     */
    private final AtomicLong requestCounter = new AtomicLong(0);
}
