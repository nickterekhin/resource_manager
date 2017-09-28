package controls;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 22.12.2014.
 */
public class Parser {
    Path file;
    private final Charset charset = Charset.forName("UTF-8");
    String p = "GetGlobalResourceObject\\(\"(\\w+)\",\\s+\"(\\w+)\",\\s+\"(.*?)\"\\)";
    private final Pattern pattern = Pattern.compile(p);
    private final Matcher matcher = pattern.matcher("");

    private Map<String,Set<Resources>> resList = new HashMap<>();

    public Parser(Path file)
    {
        this.file = file;
    }

    public Map<String,Set<Resources>> parserFile()
    {
        try (BufferedReader reader = Files.newBufferedReader(file,charset)){

            String line;
            while((line = reader.readLine())!=null)
            {
                matcher.reset(line);
                while(matcher.find())
                {
                    if(!resList.containsKey(matcher.group(1))) {

                        Set<Resources> rTmp = new HashSet<>();
                        rTmp.add(new Resources(matcher.group(1),matcher.group(2),matcher.group(3)));
                        resList.put(matcher.group(1), rTmp);
                    }
                    else
                    {
                        resList.get(matcher.group(1)).add(new Resources(matcher.group(1),matcher.group(2),matcher.group(3)));
                    }


                }

            }
            return resList;
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
            return null;
    }

    public class Resources{
        private String className;
        private String resKey;
        private String resValue;

        public Resources(String className, String resKey, String resValue) {
            this.className = className;
            this.resKey = resKey;
            this.resValue = resValue;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getResKey() {
            return resKey;
        }

        public void setResKey(String resKey) {
            this.resKey = resKey;
        }

        public String getResValue() {
            return resValue;
        }

        public void setResValue(String resValue) {
            this.resValue = resValue;
        }

        @Override
        public String toString() {
            return "Resources{" +
                    "className='" + className + '\'' +
                    ", resKey='" + resKey + '\'' +
                    ", resValue='" + resValue + '\'' +
                    "}\n";
        }
    }
}
