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

public class SchoolParser {

    List<school> schools = new ArrayList<>();
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
            dom = documentBuilder.parse("schools.xml");

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        NodeList nodeList = documentElement.getElementsByTagName("school");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // get the employee element
            Element element = (Element) nodeList.item(i);

            // get the Employee object
            school school = parseSchool(element);

            // add it to list
            schools.add(school);
        }
    }

    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */

    private school parseSchool(Element element) {

        String school_id = getTextValue(element, "school_id");
        String school_name = getTextValue(element, "school_name");
        float rating = getFloatValue(element, "rating");
        int numVotes = getIntValue(element, "numVotes");
        int net_cost = getIntValue(element, "net_cost");

        String description = getTextValue(element, "description");
        int upper_SAT = getIntValue(element, "upper_SAT");
        int lower_SAT = getIntValue(element, "lower_SAT");
        String link_to_website = getTextValue(element, "link_to_website");

        String telephone = getTextValue(element, "telephone");
        String address = getTextValue(element, "address");
        String link_to_image = getTextValue(element, "link_to_image");
        String type = getTextValue(element, "type");

        NodeList celebritiesNodes = element.getElementsByTagName("celebrity");

        List<celebrity> celebrities = new ArrayList<>();

        // parse each Hobby element and add it to the list
        for (int i = 0; i < celebritiesNodes.getLength(); i++) {
            Element schoolElement = (Element) celebritiesNodes.item(i);
            celebrity new_celebrity = parseCelebrity(schoolElement);
            celebrities.add(new_celebrity);
        }

        // create a new Employee with the value read from the xml nodes
        return new school(school_id,  school_name , rating,  numVotes,
                net_cost,  description,  upper_SAT,  lower_SAT,
                link_to_website,  telephone,  address,  link_to_image,  type, celebrities);
    }

    private celebrity parseCelebrity(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        int id = getIntValue(element, "id");

        String name = getTextValue(element, "name");
        int net_worth = getIntValue(element, "net_worth");
        String industry = element.getAttribute("industry");
        String type = element.getAttribute("type");


        return new celebrity(id, name,industry, net_worth,type);
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

        System.out.println("Total parsed " + schools.size() + " schools");
        for (school sch : schools) {
            System.out.println("\t" + sch.toString());
        }
    }

    public static void main(String[] args) {
        // create an instance
        SchoolParser domParserExample = new SchoolParser();
        // call run example
        domParserExample.runExample();
    }

}
