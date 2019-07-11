/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Lucas Ferraz Nicolau
 */
public class GarbageCollector extends Thread {
    private long refreshTime;
    private long TTLMessage;
    
    public GarbageCollector(long refreshTime, long TTLMessage) {
    	this.refreshTime = refreshTime;
    	this.TTLMessage = TTLMessage;
    }
    
    /*Run method from Thread inheritance. Loop in intervals deleting old data*/
    public void run() {
    	while (true) {
    		PeerDAO peerDAO = PeerDAO.getInstance();
    		ArrayList<String> keys = peerDAO.networkFileTable.getAllKeys();
    		
    		for (String key : keys) {
				if (fileIsOutOfDate(peerDAO, key)) {
					deleteData(peerDAO, key);
				}
			}
    		
    		try {
    			TimeUnit.SECONDS.sleep(refreshTime);
    		} catch (InterruptedException e) {
    			System.out.println("Error ocurred while executing sleep method");
    		}
    	}
    }
    
    private boolean fileIsOutOfDate(PeerDAO peerDAO, String key) {
    	Date fileDate = peerDAO.networkFileTable.get(key).getUpdateDate();
    	Date nowDate = new Date();
    	
    	if (nowDate.getTime() - fileDate.getTime() >= TTLMessage) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private void deleteData(PeerDAO peerDAO, String key) {
    	peerDAO.networkFileTable.delete(key);
    }
    
}
