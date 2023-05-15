import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class SchoolParser {

    List<school> schools = new ArrayList<>();
    Document dom;
    Integer fileParsed = 0;
    Integer recordInserted = 0;
//    ArrayList<String> insertStatements = new ArrayList<String>();
//    String query_string = "INSERT IGNORE school(id,name,rating,numVotes,net_cost,description,upper_SAT,lower_SAT,link_to_website,telephone,address,link_to_image) VALUES\n";
//    String genres_in_schools = " INSERT IGNORE genres_in_schools (genre_id, school_id) VALUES\n";
//    String genre = "INSERT IGNORE genre(fullname)\n";
//    String celebrity = "INSERT IGNORE celebrity(name,net_worth,industry) VALUES\n";
//    String celebrity_in_school = "INSERT IGNORE celebrities_in_schools(celebrity_id,school_id) VALUES ((SELECT MAX(id) FROM celebrity)\n";
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    String query_school = "INSERT IGNORE school(id,name,rating,numVotes,net_cost,description,upper_SAT,lower_SAT,link_to_website,telephone,address,link_to_image) VALUES ";
    String query_genre = "INSERT IGNORE genre(fullname) values ";
    String query_genres_in_schools = "INSERT IGNORE genres_in_schools (genre_id, school_id) values ";
    String query_celebrity = "INSERT IGNORE celebrity(name,net_worth,industry) values ";
    String query_celebrities_in_schools = "INSERT IGNORE celebrities_in_schools(celebrity_id,school_id) VALUES ";
    int celebrity_counter = 1;
    int genre_id_counter = 0;

    public void runExample() {

        parseXmlFile();
        parseDocument();

        query_genre = query_genre.substring(0, query_genre.length() - 1);
        query_school = query_school.substring(0, query_school.length() - 1);
        query_genres_in_schools = query_genres_in_schools.substring(0, query_genres_in_schools.length() - 1);
        query_celebrity = query_celebrity.substring(0, query_celebrity.length() - 1);
        query_celebrities_in_schools = query_celebrities_in_schools.substring(0, query_celebrities_in_schools.length() - 1);


        query_school += ";";
        query_genre += ";";
        query_genres_in_schools += ";";
        query_celebrity += ";";
        query_celebrities_in_schools += ";";

        System.out.println("files parsed: " + fileParsed);

        executeQuery(query_school);
        executeQuery(query_genre);
        executeQuery(query_genres_in_schools);
        executeQuery(query_celebrity);
        executeQuery(query_celebrities_in_schools);

    }
    private void parseXmlFile() {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false); // Disable validation
        documentBuilderFactory.setExpandEntityReferences(false); // Disable entity expansion

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
            fileParsed++;
            updateQuery(school);
            // add it to list
            schools.add(school);
//            insertLocationIntoDatabase(school);
        }
    }

    private void updateQuery(school school) {
        query_school += " ('" + school.get_school_id() + "', '" + school.get_school_name() +"', " + school.get_rating() + ", "  + school.get_numVotes() + ", "+ school.get_net_cost() +", '"+
                school.get_description() + "', " + school.get_upper_SAT() +", " + school.get_lower_SAT() +", '" +school.get_link_to_website() + "', '" + school.get_telephone() + "', '" +
                school.get_address()+ "', '" + school.get_link_to_image()+"'),\n";

        ArrayList<String> temp = new ArrayList<String>();
        if (map.containsKey(school.get_genre())) {
            query_genres_in_schools += String.format(" (%s, '%s'),",map.get(school.get_genre()),school.get_school_id() );
        } else {
            map.put(school.get_genre(), genre_id_counter);
            query_genre += String.format("('%s'),", school.get_genre());
            query_genres_in_schools += String.format(" (%s, '%s'),",Integer.toString(genre_id_counter),school.get_school_id() );
            genre_id_counter++;
        }
        List<celebrity> celebrities = school.get_celebrities();
        for(celebrity celebrity:celebrities){
            query_celebrity += String.format(" ('%s',%s,'%s'),",celebrity.get_name(), celebrity.get_net_worth(),celebrity.get_industry() );
            query_celebrities_in_schools += String.format(" (%s, '%s'),", Integer.toString(celebrity_counter), school.get_school_id() );;
            celebrity_counter ++;
        };
    }

    private void executeQuery(String query) {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(query);
                System.out.println(query);
                statement.executeUpdate(query);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows inserted: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertLocationIntoDatabase(school school) {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/collegedb";

        try {
            // load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

                String query1 =  String.format("SELECT COUNT(*) FROM genre WHERE fullname ='%s';",school.get_school_id() );
                PreparedStatement statement1 = connection.prepareStatement(query1);

                ResultSet rs  = statement1.executeQuery(query1);
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt(1);
                }
                if(count>0){
                    String query2 = String.format("SELECT id FROM genre WHERE fullname ='%s';",school.get_school_id() );;
                    PreparedStatement statement2 = connection.prepareStatement(query2);
                    ResultSet rs2  = statement2.executeQuery(query2);
                    int id = 0;
                    while (rs2.next()) {
                        id = rs2.getInt("id");
                    }
                    String query3 = String.format(" INSERT IGNORE genres_in_schools (genre_id, school_id) VALUES (%s, '%s');",id,school.get_school_id() );
//                    insertStatements.add(query3);
                }
                else{
                    String query2 = String.format("INSERT IGNORE genre(fullname) values (\"%s\");",school.get_genre());
//                    insertStatements.add(query2);

                    String query3 = "SELECT id FROM genre WHERE fullname = \""+school.get_genre()+"\";";
                    Statement statement3 = connection.createStatement();
                    ResultSet rs3  = statement3.executeQuery(query3);
                    int id = 0;
                    while (rs3.next()) {
                        id = rs3.getInt("id");
                    }
                    String query4 = String.format(" INSERT IGNORE genres_in_schools (genre_id, school_id) VALUES (%s, '%s');",id,school.get_school_id() );
//                    insertStatements.add(query4);
                }

                List<celebrity> celebrities = school.get_celebrities();
                for(celebrity celebrity:celebrities){

                    String query5 = String.format("INSERT IGNORE celebrity(name,net_worth,industry) VALUES ('%s',%s,'%s');", celebrity.get_name(), celebrity.get_net_worth(),celebrity.get_industry() );;
//                    insertStatements.add(query5);

                    String query6= String.format("INSERT IGNORE celebrities_in_schools(celebrity_id,school_id) VALUES ((SELECT MAX(id) FROM celebrity), '%s');",school.get_school_id() );;
//                    insertStatements.add(query6);

                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
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
        String genre = getTextValue(element, "genre");


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
                link_to_website,  telephone,  address,  link_to_image,  type, celebrities,genre);
    }


    private celebrity parseCelebrity(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name

        int id = getIntValue(element, "id");

        String name = getTextValue(element, "name");
        int net_worth = getIntValue(element, "net_worth");
        String industry = getTextValue(element, "industry");
//        System.out.println("here");
//        System.out.println(industry);
        String type = element.getAttribute("type");

        return new celebrity(id, name,industry, net_worth,type);
    }


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


    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
//        System.out.println(getTextValue(ele, tagName));
        return Integer.parseInt(getTextValue(ele, tagName));
    }


    public static void main(String[] args) {
        // create an instance
        SchoolParser domParserExample = new SchoolParser();
        // call run example
        domParserExample.runExample();
    }

}
