/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author guest-tdld1a
 */
public class PeerDAO { 
    /*PeerDAO is implemented as a singleton*/
    public static PeerDAO INSTANCE;
    public static ArrayList<ArrayList<String>> LIST_OF_PEERS;
    
    
    /*Object containing the files of this Peer*/
    public HashMapDAO myFileTable = new HashMapDAO();
    /*Object containing the files of other Peers*/
    public HashMapDAO networkFileTable = new HashMapDAO();
    
    public static PeerDAO getInstance(){
        if(INSTANCE == null){
            synchronized (PeerDAO.class){
                if(INSTANCE ==  null){
                    INSTANCE = new PeerDAO();
                    LIST_OF_PEERS = new ArrayList();
                }
            }

        }
        return INSTANCE;
    }
      
}

class HashMapDAO{
    /* The object below uses as key the concatanation of IP, port and name of the peer
    The FileTable has the following fields: IP, PORT, NAME, LAST_UPDATE_TIMESTAMP,
    LIST_OF_FILES */
    private Map <String, FileTable> HashMap = Collections.synchronizedMap(new HashMap<String, FileTable>());
    
    /*DAO CRUD (Create, Read (get), Update, Delete)*/
    public void create(String IP, String port, String name, ArrayList listOfFiles){
        String key = (IP+port+name);
        Date creationTimestamp = new Date(); //Now datetime
        FileTable filetable = new FileTable(IP, port, name, creationTimestamp, listOfFiles);
        
        HashMap.put(key, filetable);
    }
    public FileTable get(String key){
        return HashMap.get(key);
    }
    public void update(String key, ArrayList listOfFiles){
        FileTable currentPeerState = HashMap.get(key);
        FileTable newPeerState = currentPeerState;
        
        Date updateTimestamp = new Date();
        newPeerState.setFiles(listOfFiles);
        newPeerState.setUpdateDate(updateTimestamp);
        
        HashMap.replace(key, newPeerState);
    }
    public void delete(String key){
        HashMap.remove(key);
    }
    public ArrayList<String> getAllKeys(){
        return new ArrayList(Arrays.asList(HashMap.keySet().toArray()));
    }
    public int size(){
        return HashMap.size();
    }
}

class FileTable{
    private final String IP;
    private final String port;
    private final String name;
    private Date lastUpdateTimestamp;
    private ArrayList listOfFiles;
    
    public FileTable(String IP, String port, String name, Date lastUpdateTimestamp, ArrayList listOfFiles){
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.listOfFiles = listOfFiles;
    }
    
    /* GETTERS AND SETTERS */
    public String getIP(){
        return this.IP;
    }
    public String getPort(){
        return this.port;
    }
    public String getName(){
        return this.name;
    }
    public Date getUpdateDate(){
        return this.lastUpdateTimestamp;
    }
    public void setUpdateDate(Date timestamp){
        this.lastUpdateTimestamp = timestamp;
    }
    public ArrayList getFiles(){
        return this.listOfFiles;
    }    
    public void setFiles(ArrayList listOfFiles){
        this.listOfFiles = listOfFiles;
    }
    
    
}
