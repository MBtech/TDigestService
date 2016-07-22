package com.tdigestserver;

import com.tdigestserver.TDigestServer;

/**
 *
 * @author mb
 */
public class Main {
    public static void main(String [] args){
        TDigestServer ts = new TDigestServer(Integer.parseInt(args[1]));
    }
}