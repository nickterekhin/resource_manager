package controls;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;


/**
 * Created by Nick on 22.12.2014.
 */
public class XMLManager {

    private Map<String, Set<Parser.Resources>> resList;
    private String resFolder;
    private final Path templatePath = Paths.get("tmp.resx");
    private Logger log;


    public XMLManager(Map<String, Set<Parser.Resources>> resList, String resFolder) {
        log = Logger.getLogger("ResourceX");
        this.resList = resList;
        this.resFolder = resFolder;

    }

    public boolean initXMLfiles(String fileName,String[] langs) throws IOException {

        for(int i = -1; i<langs.length;i++) {
            for (Map.Entry<String, Set<Parser.Resources>> item : resList.entrySet()) {
                String resPath = item.getKey();
                if(i>-1) {
                    resPath = resPath+"."+langs[i];
                }

                Path path = Paths.get(resFolder + "/" + resPath + ".resx");

                if (!Files.exists(path)) {
                    if(i>-1)
                    {
                        Files.copy(Paths.get(resFolder + "/" + item.getKey() + ".resx"), path);
                    }
                    else {
                        Files.copy(templatePath, path);
                    }
                }
                log.info("============================[ " + fileName + " -> " + (i>-1?item.getKey()+"."+langs[i]:item.getKey()) + ".resx ]===============================");
                SAXReader saxReader = new SAXReader();
                saxReader.setEncoding("UTF-8");
                Document doc;
                try {
                    doc = saxReader.read(path.toFile());
                    manageXMLResourceFile(doc, item.getValue(),i,langs);
                    OutputFormat outFormat = new OutputFormat();
                    outFormat.setEncoding("utf-8");
                    XMLWriter writer = new XMLWriter(new FileWriter(path.toString()),outFormat);
                    writer.write(doc);
                    writer.close();
                } catch (DocumentException | IOException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        return true;
    }

    public Document manageXMLResourceFile(Document doc, Set<Parser.Resources> resObj,int langFlag, String[] langs){
        Long i =1L;
        for(Parser.Resources itemRes : resObj){
            String langExt ="";
            String elementText = itemRes.getResValue();
            if(langFlag>-1)
            {
                elementText ="";
                langExt = "."+langs[langFlag];
            }
            if(doc.selectSingleNode("//data[@name='"+itemRes.getResKey()+"']")==null)
            {

                Element root = doc.getRootElement();
                Element dataElement = root.addElement("data");
                dataElement.add(DocumentHelper.createText("\n"));
                dataElement.addAttribute("name", itemRes.getResKey());
                dataElement.addAttribute("xml:space","preserve");
                Element valueData = dataElement.addElement("value");
                if(!elementText.isEmpty())
                valueData.setText(elementText);
                dataElement.add(DocumentHelper.createText("\n"));
                root.add(DocumentHelper.createText("\n"));

                log.info(" ("+i+") Resource Added: FileName = ["+itemRes.getClassName()+langExt+".resx] value = ["+itemRes.getResKey()+"] defaultLanguageValue = ["+elementText+"]");
            }
            else
            {
                log.info(" (" + i + ") Resource Exist: FileName = [" + itemRes.getClassName() + langExt + ".resx] value = [" + itemRes.getResKey() + "] defaultLanguageValue = [" + elementText + "]");
                log.info(" (" + i + ") Resource Exist: FileName = [" + itemRes.getClassName() + langExt + ".resx] value = [" + itemRes.getResKey() + "] defaultLanguageValue = [" + elementText + "]");
            }
            i++;

        }

        return doc;
    }
}
