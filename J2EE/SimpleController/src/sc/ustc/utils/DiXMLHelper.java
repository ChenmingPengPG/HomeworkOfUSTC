package sc.ustc.utils;

import com.sun.xml.internal.bind.v2.model.core.ID;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import sc.ustc.items.DiItems.Bean;
import sc.ustc.items.DiItems.Field;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class DiXMLHelper {
    public static final String DI_XML_FILE_NAME = "di.xml";
    public static final String BEAN_TAG = "bean";
    public static final String ID_TAG = "id";
    public static final String CLASS_TAG = "class";
    public static final String NAME_TAG = "name";
    public static final String REF_TAG = "bean-ref";
    private Map<String, Bean> diData;
    private Document document;

    public DiXMLHelper() throws ParserConfigurationException, IOException, SAXException {
        String path = this.getClass().getClassLoader().getResource("").getPath().replaceFirst("/","");
        try{
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(path + DI_XML_FILE_NAME);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(file);

        diData = new HashMap<>();
        getBeans();
    }
    private void getBeans(){
        NodeList nodeList = document.getElementsByTagName(BEAN_TAG);
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node instanceof Element){
                Bean bean = getBeanByNode(node);
                diData.put(bean.getName(), bean);
            }
        }
    }
    private Bean getBeanByNode(Node node){
        Bean bean = new Bean();
        NamedNodeMap attrs = node.getAttributes();
        for(int i = 0; i < attrs.getLength(); i++){
            Node attr = attrs.item(i);
            String value = attr.getNodeValue();
            switch (attr.getNodeName()){
                case ID_TAG:
                    bean.setName(value);
                    break;
                case CLASS_TAG:
                    bean.setClassName(value);
            }
        }
        NodeList list = node.getChildNodes();
        for(int i = 0; i < list.getLength(); i++){
            Node child = list.item(i);
            if(child != null && child instanceof Element){
                Field field = new Field();
                NamedNodeMap childAttrs = child.getAttributes();
                for(int j = 0; j < childAttrs.getLength(); j++){
                    Node attr = childAttrs.item(j);
                    String value = attr.getNodeValue();
                    switch (attr.getNodeName()){
                        case NAME_TAG:
                            field.setName(value);
                            break;
                        case REF_TAG:
                            field.setRef(value);
                            break;
                    }
                }
                bean.setField(field);
            }
        }
        return bean;
    }

    public Map<String, Bean> getDiData(){
        return diData;
    }
}
