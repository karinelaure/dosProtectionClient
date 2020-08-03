package com.dos.protection.client;


import com.dos.protection.util.AppResources;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @auther Karine Camhy
 * @since 8/2/2020
 */
public class DosClient implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DosClient.class);

    private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    private AtomicInteger clientId;
    private static volatile boolean terminate=false;
    private static final int MAX_WAIT =  AppResources.getMaxWait();
    private static final int MIN_WAIT = AppResources.getMMinWait();
    private static final String URL = AppResources.getUrl();
    private static final String QUERY_PARAM = AppResources.getParamQuery();


    public static int getRandomValue(int Min, int Max)
    {
        return ThreadLocalRandom
                .current()
                .nextInt(Min, Max + 1);
    }


    public DosClient(CloseableHttpClient httpClient,int numberOfClients) throws URISyntaxException {
        this.clientId = new AtomicInteger(getRandomValue(1,numberOfClients));
        URIBuilder builder = new URIBuilder(URL);
        builder.setParameter(QUERY_PARAM, String.valueOf(clientId));
        this.httpGet = new HttpGet(builder.build());
        this.httpClient = httpClient;
    }

    public void run() {
        CloseableHttpResponse response = null;
        try {

            while (!terminate) {

                response  = httpClient.execute(httpGet);
                logger.info("client id : {} status: {}", clientId, response.getStatusLine().getStatusCode());
                Thread.sleep(getRandomValue(MIN_WAIT,MAX_WAIT));

            }
        } catch (InterruptedException e) {
            logger.error("Thread interrupted. ", e);
        } catch (IOException e) {
            logger.error("Client request couldn't be send. clientid :{} ",clientId, e);
        }finally {
            try {
                if(response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("{} thread with clientId :{} ended!",Thread.currentThread().getName() ,clientId);

    }

    public void shutDown(){
        terminate = true;
    }


}
