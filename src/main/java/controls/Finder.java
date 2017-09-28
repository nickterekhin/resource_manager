package controls;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nick on 22.12.2014.
 */
public class Finder extends SimpleFileVisitor<Path> {

    private final PathMatcher matcher;
    private String pathToResDir;
    private Logger log;
    private String[] langToTranlsate;


    public Finder(String match, String pathToresDir, String langToTranslate)
    {
        log = Logger.getLogger("Total");
        matcher = FileSystems.getDefault().getPathMatcher("glob:"+match);
        this.pathToResDir = pathToresDir;
        this.langToTranlsate = langToTranslate.split(",");
    }

    void find(Path file){
        Path name = file.getFileName();
        if(name!=null && matcher.matches(name)){

            Parser parser = new Parser(file);
            Map<String, Set<Parser.Resources>> resList = parser.parserFile();
            String resString = "";
            for(Map.Entry<String,Set<Parser.Resources>> item : resList.entrySet())
            {
                resString+="["+item.getKey()+".resx : {"+item.getValue().size()+"}]";

            }
            log.info("Parsed File: "+file.toFile().getName()+"  --  Resource Files: " + resString);
            XMLManager xmlManager = new XMLManager(resList, pathToResDir);
            try {
                xmlManager.initXMLfiles(file.toFile().getName(),langToTranlsate);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
        find(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr){
        find(dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException ex){
        System.out.println(ex);
        return FileVisitResult.CONTINUE;
    }

}
