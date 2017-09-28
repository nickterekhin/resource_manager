import controls.Finder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Nick on 20.12.2014.
 */
public class ResourceManager {
    public static void usage(){
        System.err.println("java -jar ResourceManager.jar <pathTorResDir pathToCodeDir "+"-name \"<glob_pattern>\"> <\"langs_to_translate comma separate\">");
        System.out.println("Example: java -jar ResourceManager.jar \"d:/home/User/projectDir/GlobalResource\" \"d:/home/User/projectDir\" -name \"*.{aspx,aspx.vb}\" \"es\"");
        System.exit(-1);
    }
    public static void main(String[] args) throws IOException {

        if(args.length<5 || !args[2].equals("-name"))
            usage();

        Path startPath = Paths.get(args[1]);
        String pattern = args[3];
        long start = System.currentTimeMillis();
        System.out.println("Resource Manager: Started");
        Finder find = new Finder(pattern, args[0],args[4]);
        Files.walkFileTree(startPath,find);
        long end = System.currentTimeMillis();
        System.out.println("Resource Manager: Done");
        System.out.println("Elapsed time: [ "+((end-start)*0.001)+" sec.]");
    }
}
