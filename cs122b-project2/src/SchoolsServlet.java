import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// Declaring a WebServlet called SchoolsServlet, which maps to url "/api/school"
@WebServlet(name = "SchoolServlet", urlPatterns = "/api/school_list")
public class SchoolsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type
        String school = request.getParameter("school");
        String location = request.getParameter("location");
        String other = request.getParameter("other");
        String order = request.getParameter("order");
        String genre = request.getParameter("genre");
        String fulltext = request.getParameter("fulltext");
        String autocomplete = request.getParameter("autocomplete");

        System.out.println(school);
        System.out.println(location);
        System.out.println(other);
        System.out.println(order);
        System.out.println(genre);
        System.out.println(fulltext);
        System.out.println(autocomplete);


        int pagenum =  Integer.parseInt(request.getParameter("pagenum"));
        int whichpage =  Integer.parseInt(request.getParameter("whichpage"));


        // The log message can be found in localhost log
        request.getServletContext().log("getting query: " + school+location+other);
        String query = "SELECT * \n" +
                "FROM school AS s\n" +
                "JOIN schools_in_locations AS sil ON sil.id = s.id\n" +
                "JOIN genres_in_schools AS gis ON gis.school_id = s.id\n" +
                "JOIN genre AS g ON g.id = gis.genre_id\n" +
                "JOIN location AS l ON l.location_id = sil.location_id\n";
        String sub_query = "";
        System.out.println("1");
        if(school.length()>0&&!school.equals("null")){
            if (fulltext.equals("false")) {
                if (school.length() == 2 && school.substring(1, 2).equals("_")) {
                    sub_query += String.format("WHERE s.name like '%s' or s.name like '%s'", school.substring(0, 1) + "%", school.substring(0, 1).toLowerCase() + "%");
                } else {
                    sub_query += String.format("WHERE s.name LIKE '%s'", school);
                }
            }
            else {
                String[] arrOfStr = school.split(" ");
                if (arrOfStr.length == 1) {
                    sub_query += String.format("WHERE MATCH (name) AGAINST ('*%s*' IN BOOLEAN MODE)", school);
                }
                else {
                    sub_query += "WHERE MATCH (name) AGAINST (";
                    for (String str: arrOfStr) {
                        sub_query += "'+*" + str + "*'";
                    }
                    sub_query += "IN BOOLEAN MODE)";
                }
            }
        }

        System.out.println("2");
        if(other.length()>0&&!location.equals("null")){
            if(sub_query.length()==0){
                sub_query += "WHERE ";
            }
            else{
                sub_query += " and ";
            }
            sub_query += String.format("l.state_full LIKE '%s'",location);
        }
        System.out.println("3");
        if(other.length()>0&&!other.equals("null")){
            if(sub_query.length()==0){
                sub_query += "WHERE ";
            }
            else{
                sub_query += " and ";
            }
            sub_query += String.format("s.description LIKE '%s'",other);
        }
        System.out.println("4");
        if(genre.length()>0&&!genre.equals("null")){
            if(sub_query.length()==0){
                sub_query += "WHERE ";
            }
            else{
                sub_query += " and ";
            }
            sub_query += String.format("g.fullname = '%s'",genre);
        }
        System.out.println("5");
        if(order.length()>6&&!order.equals("null")) {
            sub_query += "\nGROUP BY name\n";
            sub_query += order;
        }
        else if (order.length()>0&&!order.equals("null")){

            sub_query += "\nGROUP BY name\n";
            sub_query += String.format("ORDER BY s.name %s",order);
        }
        else {
            sub_query += "\nGROUP BY name";
            if (autocomplete.length()>0&&!autocomplete.equals("null")) {
                sub_query += "\nLIMIT 10";
            }
        }
        System.out.println("6");
        sub_query += ";";
        query = query+sub_query;
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("7");
            JsonArray jsonArray = new JsonArray();
            JsonObject jsonNum = new JsonObject();
            jsonNum.addProperty("pagenum", pagenum);
            jsonNum.addProperty("whichpage", whichpage);

            jsonArray.add(jsonNum);

            // Iterate through each row of rs
            int counter = 0;
            while (rs.next() && counter<pagenum*whichpage+pagenum) {
                if(counter>=pagenum*whichpage) {
                    String school_id = rs.getString("id");
                    String school_name = rs.getString("name");
                    String school_dis = rs.getString("description");
                    String school_rating = rs.getString("rating");
                    String school_city = rs.getString("city");
                    String school_state = rs.getString("state_full");
                    String school_genre = rs.getString("fullname");
                    String link_to_website = rs.getString("link_to_website");
                    String safety = rs.getString("safety");
                    String telephone = rs.getString("telephone");
                    String location_id = rs.getString("location_id");
                    // Create a JsonObject based on the data we retrieve from rs
                    JsonObject jsonObject = new JsonObject();

                    if (autocomplete.equals("true")) {
                        jsonObject.addProperty("value", school_name);
                        jsonObject.addProperty("data", school_id);
                    }
                    else {
                        jsonObject.addProperty("school_id", school_id);
                        jsonObject.addProperty("school_name", school_name);
                        jsonObject.addProperty("school_dis", school_dis);
                        jsonObject.addProperty("school_rating", school_rating);
                        jsonObject.addProperty("school_city", school_city);
                        jsonObject.addProperty("school_state", school_state);
                        jsonObject.addProperty("school_genre", school_genre);
                        jsonObject.addProperty("link_to_website", link_to_website);
                        jsonObject.addProperty("safety", safety);
                        jsonObject.addProperty("telephone", telephone);
                        jsonObject.addProperty("location_id", location_id);
                    }
                    jsonArray.add(jsonObject);
                }
                counter += 1;

            }
            rs.close();
            statement.close();

            // Log to localhost log
            request.getServletContext().log("getting " + jsonArray.size() + " results");

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}