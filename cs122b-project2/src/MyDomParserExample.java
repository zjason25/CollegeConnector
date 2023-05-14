import jakarta.servlet.ServletConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyDomParserExample {

    List<location> locations = new ArrayList<>();
    Document dom;

    public void runExample() {

        // parse the xml file and get the dom object
        parseXmlFile();

        // get each employee element and create a Employee object
        parseDocument();

        // iterate through the list and print the data
        printData();
    }

    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom = documentBuilder.parse("locations.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        // get a nodelist of employee Elements, parse each into Employee object
        NodeList nodeList = documentElement.getElementsByTagName("location");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the employee element
            Element element = (Element) nodeList.item(i);

            // get the Employee object
            location location = parseLocation(element);

            // add it to list
            locations.add(location);
        }
    }

    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */
    private location parseLocation(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        String location_id = getTextValue(element, "location_id");
        String state_init = getTextValue(element, "state_init");
        String zipcode = getTextValue(element, "zipcode");
        String state_full = getTextValue(element, "state_full");
        String city = getTextValue(element, "city");
        int lci = getIntValue(element, "lci");
        int safety = getIntValue(element, "safety");
        String type = element.getAttribute("type");
//        NodeList schoolNodes = element.getElementsByTagName("school");

//        List<school> schools = new ArrayList<>();

        // parse each Hobby element and add it to the list
//        for (int i = 0; i < schoolNodes.getLength(); i++) {
//            Element schoolElement = (Element) schoolNodes.item(i);
//            school new_school = parseSchool(schoolElement);
//            schools.add(new_school);
//        }
//        // create a new Employee with the value read from the xml nodes
        return new location(location_id, state_init, zipcode, state_full, city,
                lci, safety, type);
    }

    /**
     * It takes an XML element and the tag name, look for the tag and get
     * the text content
     * i.e for <Employee><Name>John</Name></Employee> xml snippet if
     * the Element points to employee node and tagName is name it will return John
     */
    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            textVal = nodeList.item(0).getFirstChild().getNodeValue();
        }
        return textVal;
    }

    private float getFloatValue(Element ele, String tagName) {
        // in production application you would catch the exception
        return Float.parseFloat(getTextValue(ele, tagName));
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        System.out.println(getTextValue(ele, tagName));
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

        System.out.println("Total parsed " + locations.size() + " locations");
        for (location loc : locations) {
            System.out.println("\t" + loc.toString());
        }
    }

    public static void main(String[] args) {
        // create an instance
        MyDomParserExample domParserExample = new MyDomParserExample();
        // call run example
        domParserExample.runExample();
    }

}
