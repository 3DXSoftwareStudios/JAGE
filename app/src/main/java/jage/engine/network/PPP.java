/**
 * Copyright 2026 Anton Jantos
 *
 * Licensed under the Apache License, Version 2.0.
 * See the LICENSE file in the project root for details.
 */
package jage.engine.network;

import jage.engine.security.AESUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class PPP { //Private Peer Protocol
    
    private String  defaulKey = "12345678901234561234567890123456";
    private AESUtil aesU; //AES GCM, NoPAD, 12 byte IV, 128 bit Tag
    private String  key;
    private int     serverTimeOutS;
    
    private ServerSocket     server;
    private Socket           client;
    
    public PPP(String key, int ServerTimeOutS){
        this.serverTimeOutS = ServerTimeOutS;
        aesU = new AESUtil();
        if(key != null){
            this.key = key;
        }else{
            this.key = defaulKey;
        }
    }
    
    public void initServer(int port){
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(serverTimeOutS);
        } catch (IOException ex) {
            System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void initClient(String ip, int port){
        try {
            client = new Socket(ip, port);
        } catch (IOException ex) {
            System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void runServer(RequestHandler SRH){
        while(true){
            try {
                System.out.println("Waiting for Connection at " + server.getInetAddress() + ":" + server.getLocalPort());
                Socket Sclient = server.accept();
                System.out.println("Connected to: " + Sclient.getRemoteSocketAddress());
                DataInputStream  input  = new DataInputStream(Sclient.getInputStream());
                DataOutputStream output = new DataOutputStream(Sclient.getOutputStream());
                SendHandler sh = new SendHandler(input, output, aesU, key);
                
                System.out.println("Starting Reciever");
                
                SRH.handleRequest(sh.recieveMessage(), sh);
                
                input.close();
                output.close();
                Sclient.close();
                
            } catch (IOException ex) {
                System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
    
    public void runClient(RequestHandler SRH){
        try {
            DataInputStream  input  = new DataInputStream(client.getInputStream());
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            SendHandler sh = new SendHandler(input, output, aesU, key);
            
            System.out.println("Handeling Connection");
            
            sh.sendMessage("Cliento Pupo!");
            SRH.handleRequest(sh.recieveMessage(), sh);

            input.close();
            output.close();
            client.close();

        } catch (IOException ex) {
            System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public static abstract class RequestHandler{
        public abstract void handleRequest(String request, SendHandler InteractionHandler);
    }
    
    public static class SendHandler{
        
        private DataInputStream  dIn;
        private DataOutputStream dOut;
        private AESUtil          aesU;
        private String           key;
        
        public SendHandler(DataInputStream dIn, DataOutputStream dOut, AESUtil aesU, String key){
            this.dOut = dOut;
            this.dIn  = dIn;
            this.aesU = aesU;
            this.key  = key;
        }
        
        public void sendMessage(String Message){
            try {
                dOut.writeUTF(aesU.encrypt(key, Message));
            } catch (IOException ex) {
                System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        
        public String recieveMessage(){
            try {
                String input = dIn.readUTF();
                return aesU.decrypt(key, input);
            } catch (IOException ex) {
                System.getLogger(PPP.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return null;
        }
        
    }
    
}