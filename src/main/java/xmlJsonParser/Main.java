package xmlJsonParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import csvJsonParser.Employee;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseXML(String filename) {
        List<Employee> list = new ArrayList<>();
        long id = 0;
        String firstname = " ";
        String lastname = " ";
        String country = " ";
        int age = 0;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));
            Node root = doc.getDocumentElement();//получаю корневой узел документа
            NodeList nodeList = root.getChildNodes(); //извлекаю список узлов
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i); //пробегаюсь по списку элементов конкретного узла
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    try {
                    id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                    firstname = element.getElementsByTagName("firstName").item(0).getTextContent();
                    lastname = element.getElementsByTagName("lastName").item(0).getTextContent();
                    country = element.getElementsByTagName("country").item(0).getTextContent();
                    age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    list.add(new Employee(id, firstname, lastname, country, age));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json) {
        try (FileOutputStream fos = new FileOutputStream("data.json", true)) {
            byte[] bytes = json.getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
