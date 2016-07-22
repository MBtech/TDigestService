package com.tdigestclient;

import com.tdigestclient.TDigestClient;

/**
 *
 * @author mb
 */
public class Main {
    public static void main(String[] args){
        TDigestClient tc =  new TDigestClient(args[0],Integer.parseInt(args[1]));
        tc.connection();
        tc.getPercentile(Double.parseDouble(args[2]));
        tc.quitConnection();
    }
}