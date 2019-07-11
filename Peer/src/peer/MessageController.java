/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import java.security.Timestamp;
import org.json.*;

/**
 *
 * @author Lucas Ferraz Nicolau
 */
public class MessageController {
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	/*Name of the peer the message came from*/
	public String originPeer;
	
	public MessageController(String originPeer) {
		this.originPeer = originPeer;
	}
	
	/*Return a JSON from a string if is valid and not an array*/
	public JSONObject deserializeMessage(String serializedData) {
		try {
			JSONObject data = new JSONObject(serializedData);
			return data;
		} catch (JSONException e) {
			// it is possible that JSON is an array while in catch
			System.out.println("Invalid data. Cannot be deserialized. Null value is assigned.");
			return null;
		}
	}
	
	/*Check if a JSON contains all properties of the message*/
	/*Properties: ip, port, peer, list of files and last update timestamp*/
	/*Properties file path and size was removed*/
	public boolean validateMessage(JSONObject message) {
		// check all properties
		// check each value format???
		if (message == null) {
			System.out.println("Message is null");
			return false;
		} else if (!message.has("ip")) {
			System.out.println("Message doesn't hava IP property");
			return false;
		} else if (!message.has("port")) {
			System.out.println("Message doesn't hava Port property");
			return false;
		} else if (!message.has("peer")) {
			System.out.println("Message doesn't hava Peer property");
			return false;
		/*} else if (!message.has("file path")) {
			System.out.println("Message doesn't hava File Path property");
			return false;
		} else if (!message.has("size")) {
			System.out.println("Message doesn't hava Size property");
			return false;*/
		} else if (!message.has("list of files")) {
			System.out.println("Message doesn't hava List of Files property");
			return false;
		} else if (!message.has("last update timestamp")) {
			System.out.println("Message doesn't hava Last Update Timestamp property");
			return false;
		}
		return true;
	}
	
	/*Save JSON data as FileTable checking if not duplicate and not old*/
	public void saveMessage(JSONObject message) {
		String messageIP = message.getString("ip");
		String messagePort = message.getString("port");
		String messagePeer = message.getString("peer");
		// Key constructing
		String messageKey = messageIP + messagePort + messagePeer;
		ArrayList<String> messageFiles = new ArrayList<String>();
		
		try {
			JSONArray jsonListOfFiles = new JSONArray(message.get("list of files"));
			for (int i = 0; i < jsonListOfFiles.length(); i++) {
				messageFiles.add(jsonListOfFiles.getString(i));
			}
		} catch (Exception ex) {
			// Exception for JSONArray possible errors
			System.out.println("List of Files from message is invalid. Files cannot be retrieved from JSON.");
			return;
		}
		
		try {
			// Verify Date to String conversion format
			Date messageTimestamp = df.parse(message.getString("last update timestamp"));
			
			PeerDAO peerDAO = PeerDAO.getInstance();
			FileTable currentPeerData = peerDAO.networkFileTable.get(messageKey);
			
			if (currentPeerData == null) {
				// Message from that peer never came before
				System.out.println("Recebimento [estado de " + messagePeer + "] por gossip vindo do peer " + originPeer);
				peerDAO.networkFileTable.create(messageIP, messageIP, messagePeer, messageFiles);
			} else if (currentPeerData.getUpdateDate().before(messageTimestamp)) {
				// Local message is out of date and must be updated
				System.out.println("Recebimento [estado de " + messagePeer + "] por gossip vindo do peer " + originPeer);
				peerDAO.networkFileTable.update(messageKey, messageFiles);
			} else if (currentPeerData.getUpdateDate().equals(messageTimestamp)) {
				// Message is duplicated
				System.out.println("Recebimento DUPLICADO [estado de " + messagePeer + "] por gossip vindo do peer " + originPeer);
			} else {
				// Message that arrived is out of date and must be ignored
				System.out.println("Recebimento ANTIGO [estado de " + messagePeer + "] por gossip vindo do peer " + originPeer);
			}
		} catch(Exception ex) {
			// Exception for valid message is from Date parse from String
			System.out.println("Last Update Timestamp from message is an invalid date to conversion. Message not saved");
		}
	}
    
}

