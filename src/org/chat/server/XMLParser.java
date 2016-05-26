package org.chat.server;

import org.chat.Account;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    Document doc;
    List<Account> accounts = new ArrayList<>();

    public XMLParser() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new File("resources.xml"));
            this.packAccounts();
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

    public List<Account> getAccounts() {
        return this.accounts;
    }

    private void packAccounts() {
        NodeList accounts = doc.getElementsByTagName("account");
        for (int i = 0; i < accounts.getLength(); i++) {
            NodeList account = accounts.item(i).getChildNodes();
            String login = account.item(1).getTextContent();
            String password = account.item(3).getTextContent();
            this.accounts.add(new Account(login, password));
        }
    }
}
