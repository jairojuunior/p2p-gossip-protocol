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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemReader{
    private final Stream<Path> walk;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public FileSystemReader(String fileSystem) throws IOException{
        this.walk = Files.walk(Paths.get(fileSystem));
        System.out.println(updateMetadata(this.walk));
    }
    
    private ArrayList<ArrayList<String>> updateMetadata(Stream<Path> walk){
        ArrayList<ArrayList<String>> result = null;
        
        //Get files
        List<String> files = walk.filter(Files::isRegularFile)
                .map(x -> x.toString()).collect(Collectors.toList());
        //Get files metadata
        ArrayList<String> line = null;
        files.forEach((file)-> {
            BasicFileAttributes attr = null;
            try {
                attr = Files.readAttributes(Paths.get(file), BasicFileAttributes.class);
            } catch (IOException ex) {
                Logger.getLogger(FileSystemReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            line.add(file);
            line.add(df.format(attr.lastModifiedTime().toMillis()));
            line.add(Long.toString(attr.size()));
            result.add(line);
        });
        
        return result;
    }
}
