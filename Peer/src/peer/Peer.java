/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner; 

/**
 *
 * @author guest-tdld1a
 */
public class Peer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        PeerDAO DAO = PeerDAO.getInstance();
        setPeers(DAO);
        System.out.println(DAO.LIST_OF_PEERS);
        
        
        
        
        /*
        try {
            FileSystemReader fs = new FileSystemReader("/home/ufabc/Downloads");
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
    /*The code below allows the user to set-up the peers to which it'll connect*/
    private static void setPeers(PeerDAO DAO){
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("How many peers do you want to set-up?");
        int n_peers = Integer.parseInt(scanner.nextLine());
        
        for(int i=0; i<n_peers; i++){
            ArrayList<String> peer = new ArrayList();
            System.out.println("Now we'll setup peer #" + Integer.toString(i));
            System.out.println("IP:");
            read = scanner.nextLine();
            //scanner.nextLine();
            peer.add(read);
            System.out.println("Port:");
            read = scanner.nextLine();
            //scanner.nextLine();
            peer.add(read);
            System.out.println("Name:");
            read = scanner.nextLine();
            //scanner.nextLine();
            peer.add(read);
            PeerDAO.LIST_OF_PEERS.add(peer);
        }
    }
        
    
}
