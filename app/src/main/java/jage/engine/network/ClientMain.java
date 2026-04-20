/**
 * Copyright 2026 3DX Software Studios
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.network;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        PPP net = new PPP(null, 1000);
        net.initClient("localhost", 1227);
        
        PPP.RequestHandler RH = new PPP.RequestHandler() {
            @Override
            public void handleRequest(String request, PPP.SendHandler InteractionHandler) {
                InteractionHandler.sendMessage("Lord Jane?");
                System.out.println(InteractionHandler.recieveMessage());
            }
        };
        net.runClient(RH);
    }
}
