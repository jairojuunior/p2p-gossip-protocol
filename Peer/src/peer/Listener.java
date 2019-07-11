/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.JSONObject;

/**
 *
 * @author Lucas Ferraz Nicolau
 */
public class Listener extends Thread {
    private String IP;
    private String port;
    private MessageController messageController;
    
    public Listener(String ip, String port) {
    	this.IP = ip;
    	this.port = port;
    	messageController = new MessageController(IP);
    }
    
    /*Run method from Thread inheritance. Continuously loop receiving messages and treating them*/
    public void run() {
    	try {
    		DatagramSocket socket = new DatagramSocket(Integer.parseInt(port));
    		
    		while (true) {
    			// Verify buffer length
    			byte[] buffer = new byte[1024];
    			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    			
    			socket.receive(packet);
    			
    			String serializedData = packet.getData().toString();
                        System.out.println(serializedData);
    			
    			JSONObject message = messageController.deserializeMessage(serializedData);
    			if (messageController.validateMessage(message)) {
    				messageController.saveMessage(message);
    			}
    		}
    		
    		//socket.close();
    	} catch (Exception ex) {
    		// Exception from socket
    		System.out.println("Something went very wrong");
    	}
    }
    
}
