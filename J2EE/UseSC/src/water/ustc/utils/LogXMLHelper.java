package water.ustc.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class LogXMLHelper {
    public static final String ACTION_TAG = "action";
    public static final String LOG_TAG = "log";
    public static final String NAME_TAG = "name";
    public static final String START_TIME_TAG = "s-time";
    public static final String END_TIME_TAG = "e-time";
    public static final String RESULT_TAG = "result";
    public static final String INIT_XML_CONTEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<log>\n</log>";
    File file;

    public LogXMLHelper(String path) throws ParserConfigurationException, TransformerException, IOException {
        this.file = new File(path);
        if (!this.file.exists()) {
            this.createFile();
        }

    }

    private void createFile() throws IOException {
        this.file.createNewFile();
        FileWriter os = new FileWriter(this.file);
        os.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<log>\n</log>");
        os.flush();
        os.close();
    }

    public void appendLogData(String name, String startTime, String endTime, String result) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        if (!this.file.exists()) {
            this.createFile();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(this.file);
        Element root = (Element)document.getElementsByTagName("log").item(0);
        Element action = document.createElement("action");
        Element nameNode = document.createElement("name");
        nameNode.setTextContent(name);
        Element startTimeNode = document.createElement("s-time");
        startTimeNode.setTextContent(startTime);
        Element endTimeNode = document.createElement("e-time");
        endTimeNode.setTextContent(endTime);
        Element resultNode = document.createElement("result");
        resultNode.setTextContent(result);
        root.appendChild(action);
        action.appendChild(nameNode);
        action.appendChild(startTimeNode);
        action.appendChild(endTimeNode);
        action.appendChild(resultNode);
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty("encoding", "utf-8");
        DOMSource source = new DOMSource(document);
        Result target = new StreamResult(this.file);
        transformer.transform(source, target);
    }
}
