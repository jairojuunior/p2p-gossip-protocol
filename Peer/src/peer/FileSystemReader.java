/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer;

import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.Files.walk;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author guest-tdld1a
 */
public class FileSystemReader{
    private final Stream<Path> walk;
    
    public FileSystemReader(String fileSystem) throws IOException{
        this.walk = Files.walk(Paths.get(fileSystem));
        System.out.println(findFiles(this.walk));
    }
    
    private ArrayList<ArrayList<String>> findFiles(Stream<Path> walk){
        
        //Get files
        List<String> files = walk.filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());
        //Get files metadata
        List<String> mod_date = files.stream().map(file -> 
                Files.readAttributes(Paths.get(file), BasicFileAttributes.class).lastModifiedTime()).collect(Collectors.toList());
        
        /*
        List<String> attrs = files.forEach((file) -> 
                Files.readAttributes(Paths.get(file), BasicFileAttributes.class));
        */
        return result;
    }           
    
 //Files.readAttributes(Paths.get(file), BasicFileAttributes.class);
