/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guest-tdld1a
 */
public class Peer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Try to 
        try {
            FileSystemReader fs = new FileSystemReader("/home/ufabc/Downloads");
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
