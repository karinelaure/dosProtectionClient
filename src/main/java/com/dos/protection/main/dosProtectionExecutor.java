package com.dos.protection.main;

import com.dos.protection.client.DosClient;
import com.dos.protection.util.AppResources;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.resources.cldr.pl.CalendarData_pl_PL;

import java.io.IOException;

import java.net.URISyntaxException;

import java.util.Scanner;

import java.util.stream.IntStream;

/**
 * @auther Karine Camhy
 * @since 8/2/2020
 */
public class dosProtectionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(dosProtectionExecutor.class);

    public static void main(String[] args){
        AppResources app = new AppResources();
        String uri = (String) app.getProperties("client.url");
        String param = (String) app.getProperties("client.request.param");

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        HttpClientBuilder clientbuilder = HttpClients.custom().setConnectionManager(connManager);


        CloseableHttpClient httpclient = clientbuilder.build();
        System.out.println("Please enter number of clients to simulate :) ");
        try (Scanner cmdReader = new Scanner(System.in)) {
            int numOfClients=0;
            try {
                numOfClients = cmdReader.nextInt();
            }catch (Exception e){
                logger.error("Wrong input! number of client request must be numeric! Bye",e);
                System.exit(0);
            }
            DosClient[] clients = new DosClient[numOfClients];

            int finalNumOfClients = numOfClients;
            IntStream.range(0,numOfClients).forEach(i->{
                try {
                    clients[i] = new DosClient(httpclient, finalNumOfClients);
                } catch (URISyntaxException e) {
                    logger.error("Couldn't create Client. ",e);
                }
            });

            Thread[] threads = new Thread[clients.length];
            for (int i=0; i<clients.length; i++){
                 threads[i] = new Thread(clients[i]);
                 threads[i].start();
            }

            System.in.read();

            for(DosClient client: clients){
                client.shutDown();
            }
            for (Thread thread:threads)
                thread.join();


        } catch (Exception e) {
            logger.error("Something goes wrong!" ,e);
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connManager.close();
        }

    }
}
