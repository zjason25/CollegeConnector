import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/collegedbSlave");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String browse_way1 = request.getParameter("browse_way1");
        String browse_way2 = request.getParameter("browse_way2");

        System.out.println("Handling post request");
        String school = "";
        String location = "";
        String other = "";
        String order = "";
        String genre = "";

        String fulltext_school = "";

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        response.setContentType("application/json"); // Response mime type

        PrintWriter out = response.getWriter();
        int count = 0;
        try (Connection conn = dataSource.getConnection()) {

            String query = "SELECT COUNT(1) \n" +
                    "FROM school AS s\n" +
                    "JOIN schools_in_locations AS sil ON sil.id = s.id\n" +
                    "JOIN genres_in_schools AS gis ON gis.school_id = s.id\n" +
                    "JOIN genre AS g ON g.id = gis.genre_id\n" +
                    "JOIN location AS l ON l.location_id = sil.location_id\n";
            String sub_query = "";

            if (browse_way2 != null && browse_way2.length() == 1) {
                school = browse_way2 + "_";
            }
            if (browse_way1 != null) {
                genre = browse_way1;
            } else {
                school = request.getParameter("school_name");
                location = request.getParameter("location");
                other = request.getParameter("other");
                fulltext_school = request.getParameter("fulltext_school");
            }

            if (school.length() > 0) {
                if (school.length() == 2 && school.substring(1, 2).equals("_")) {
                    sub_query += String.format("WHERE s.name like '%s' and s.name like '%s'", school.substring(0, 1) + "%", school.substring(0, 1).toLowerCase() + "%");
                } else {
                    sub_query += String.format("WHERE s.name LIKE '%s'", school);
                }
            }
            if (location.length() > 0) {
                if (location.length() > 0 && !location.equals("null")) {
                    if (sub_query.length() == 0) {
                        sub_query += "WHERE ";
                    } else {
                        sub_query += " and ";
                    }
                    sub_query += String.format("l.state_full LIKE '%s'", location);
                }
            }
            if (other.length() > 0) {
                if (sub_query.length() == 0) {
                    sub_query += "WHERE ";
                } else {
                    sub_query += " and ";
                }
                sub_query += String.format("s.description LIKE '%s'", other);
            }
            if (other.length() > 0) {
                if (sub_query.length() == 0) {
                    sub_query += "WHERE ";
                } else {
                    sub_query += " and ";
                }
                sub_query += String.format("g.fullname = '%s'", genre);
            }

            if (fulltext_school.length() > 0) {
                String[] arrOfStr = fulltext_school.split(" ");
                if (arrOfStr.length == 1) {
                    sub_query += String.format("WHERE MATCH (name) AGAINST ('*%s*' IN BOOLEAN MODE)", fulltext_school);
                } else {
                    sub_query += "WHERE MATCH (name) AGAINST (";
                    for (String str : arrOfStr) {
                        sub_query += "'+*" + str + "*'";
                    }
                    sub_query += "IN BOOLEAN MODE) group by name";
                }
            }

            query = query + sub_query;
            query += ";";

            JsonObject responseJsonObject = new JsonObject();

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    count = rs.getInt(1);
                }

                if (count >= 1) {
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    responseJsonObject.addProperty("school", school);
                    responseJsonObject.addProperty("location", location);
                    responseJsonObject.addProperty("other", other);
                    responseJsonObject.addProperty("order", order);
                    responseJsonObject.addProperty("genre", genre);
                    responseJsonObject.addProperty("fulltext_school", fulltext_school);
                    System.out.println("page exist");
                } else {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "Your search did not match any documents.");
                    System.out.println("Your search did not match any documents.");
                }
                rs.close();
            } catch (Exception e) {
                // Write error message JSON object to output
                System.out.println("errorMessage" + e.getMessage());
            }
            response.getWriter().write(responseJsonObject.toString());
            response.setStatus(200);

        }
//      ENCODE EVERY POSSIBLE VARIABLE IN FRONT END LINK
        catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}