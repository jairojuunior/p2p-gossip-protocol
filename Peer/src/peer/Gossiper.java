/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guest-tdld1a
 */

/*
PARAMETROS DO CONSTRUTOR
-TEMPO DE ENVIO
-TABELA DE ENVIO (AQUI SE FOR GOSSIP DO PROPRIO PEER PASSA A TABELA DELE, 
    SENAO A TABELA DA REDE QUE O PEER POSSUI)
-ENDERECO DOS PEERS DA REDE
-
*/
public class Gossiper extends Thread{
    private final String IP;
    private final String port;
    private final String name;
    private final int refreshTime;
    private final PeerDAO DAO;
    private final ArrayList LIST_OF_PEERS;
    private final HashMapDAO TABLE;
    private final DatagramSocket socket;
    
    public Gossiper(String IP, String port, String name, String mode, int refreshTime) throws SocketException{
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.refreshTime = refreshTime;
        this.DAO = PeerDAO.getInstance();
        this.LIST_OF_PEERS = PeerDAO.LIST_OF_PEERS;
        this.socket = new DatagramSocket();
        
        if("relay".equals(mode)){
            this.TABLE = PeerDAO.INSTANCE.networkFileTable;
            System.out.println("Gossiper on RELAY mode created");
        }else{ //mode == "self-gossip"
            this.TABLE = PeerDAO.INSTANCE.myFileTable;
            System.out.println("Gossiper on SELF-GOSSIP mode created");
        }
    }
    
    public void run(){
        while(true){
            try {
                while(this.TABLE.size()==0){
                    Thread.sleep(this.refreshTime);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Gossiper.class.getName()).log(Level.SEVERE, null, ex);
            }

            ArrayList destination = choosePeer();
            ArrayList tableToSend = chooseTableToSend();
            JSONObject tableJSON = new JSONObject(tableToSend);
            String tableString = tableJSON.toString();
            System.out.println(tableString);

            byte[] sendData = new byte[1024];
            sendData = tableString.getBytes();

            try {
                InetAddress destinationIP = InetAddress.getByName((String) destination.get(0));
                int destinationPort = Integer.parseInt((String) destination.get(1));
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, destinationPort);
                this.socket.send(sendPacket);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Gossiper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Gossiper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }         
    }
    
    private ArrayList choosePeer(){
        Random rand = new Random();
        int number_of_peers = this.LIST_OF_PEERS.size();
        int selected_peer = rand.nextInt(number_of_peers);
        return (ArrayList) this.LIST_OF_PEERS.get(selected_peer);
    }
    
    private ArrayList chooseTableToSend(){
        Random rand = new Random();
        int number_of_tables = this.TABLE.size();
        int selected_table = rand.nextInt(number_of_tables);
        return (ArrayList) this.LIST_OF_PEERS.get(selected_table);
    }
    
    private String serializeData(String IP, String port, String peerName, ArrayList listOfFiles, Date lastUpdateTimestamp) {
        JSONObject json = new JSONObject();
        
        json.put("ip", IP);
        json.put("port", port);
        json.put("peer", peerName);
        json.put("list of files", listOfFiles);
        json.put("last update timestamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(lastUpdateTimestamp));
        
        return json.toString();
    }
    
}
