/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemReader extends Thread{
    private final String IP;
    private final String port;
    private final String name;
    private final String key;
    private final String fileSystem;
    private ArrayList<ArrayList<String>> filesMetadata;
    private final int sleepTime;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public FileSystemReader(String IP, String port, String name, String fileSystem, int sleepTime) throws IOException{
        this.IP = IP;
        this.port = port;
        this.name = name;
        this.key = (IP+port+name);
        this.fileSystem = fileSystem;
        this.sleepTime = sleepTime;
        this.filesMetadata = updateMetadata(this.fileSystem);
        
        PeerDAO.INSTANCE.myFileTable.create(this.IP, this.port, this.name, 
                this.filesMetadata);
    }
    
    public void run(){
        while(true){
            try {
                Thread.sleep(this.sleepTime);
                this.filesMetadata = updateMetadata(this.fileSystem);
                PeerDAO.INSTANCE.myFileTable.update(this.key, this.filesMetadata);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            }                     
        }
    }
    
    private ArrayList<ArrayList<String>> updateMetadata(String fileSystem) throws IOException{
        Stream<Path> walk = Files.walk(Paths.get(fileSystem));
        ArrayList<ArrayList<String>> result = new ArrayList();
        Date now = new Date();
        
        //Get files
        List<String> files = walk.filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());
        //Get files metadata
        ArrayList<String> line = new ArrayList();
        files.forEach((String file) -> {
            BasicFileAttributes attr;
            try {
                attr = Files.readAttributes(Paths.get(file), BasicFileAttributes.class);
                line.add(file); //File path
                line.add(df.format(attr.lastModifiedTime().toMillis())); //File modified datetime
                line.add(Long.toString(attr.size())); //File size
                result.add(line);
            } catch (IOException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        System.out.println("File system table updated");
        //System.out.println(result);
        
        return result;
    }
}