package org.chat.server;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLParser {

    Document doc;

    public XMLParser() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new File("resources.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPort() {
        return Integer.parseInt(doc.getElementsByTagName("port").item(0).getTextContent());
    }

    public Integer getMessagesLogCount() {
        return Integer.parseInt(doc.getElementsByTagName("messages-log-count").item(0).getTextContent());
    }

    public Integer getConnectionsCount() {
        return Integer.parseInt(doc.getElementsByTagName("connections-count").item(0).getTextContent());
    }
}
