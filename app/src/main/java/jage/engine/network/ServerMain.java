/**
 * Copyright 2026 Anton Jantos
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.network;

import jage.engine.network.PPP.RequestHandler;
import jage.engine.network.PPP.SendHandler;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        PPP net = new PPP(null, 0);
        net.initServer(1227);
        
        RequestHandler RH = new RequestHandler() {
            @Override
            public void handleRequest(String request, SendHandler InteractionHandler) {
                InteractionHandler.sendMessage("Yre there?");
                System.out.println(request);
                String nrc = InteractionHandler.recieveMessage();
                if(nrc.equals("Lord Jane?")){
                    InteractionHandler.sendMessage("Lord Davis!");
                }else{
                    InteractionHandler.sendMessage("Lord Jane!");
                }
            }
        };
        net.runServer(RH);
    }
}
