package arc.nov.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FetchRecordsBook")
public class FetchRecordsBook extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/booklab";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@1234";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded successfully");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Unable to load JDBC driver", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT DISTINCT publication FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Publication Dropdown</title><body>");
            out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");
            out.println("</head>");
            
            out.println("<br><br>");
            out.println("<center>");
            out.println("<div class='bg-book' style='display:flex; align-items:center; flex-direction:column; padding:30px'>");
            out.println("<h2>Select Publication</h2>");
            out.println("<form action='FetchRecordsBook' method='get'>");
            out.println("<label for='publication'>Publication:</label>");
            out.println("<select id='publication' name='publication'>");
            out.println("<option value='' disabled selected>Select Publication</option>");

            while (rs.next()) {
                String publication = rs.getString("publication");
                out.println("<option value='" + publication + "'>" + publication + "</option>");
            }

            out.println("</select>");
            out.println("<br><br>");

            out.println("<label for='price'>Price:</label>");
            out.println("<select id='price' name='price'>");
            out.println("<option value='' selected>Select Price Range</option>");
            out.println("<option value='100'>Less than 100</option>");
            out.println("<option value='200'>Less than 200</option>");
            out.println("<option value='300'>Less than 300</option>");
            out.println("<option value='500'>Less than 500</option>");
            out.println("<option value='500+'>Greater than or equal to 500</option>");
            out.println("</select>");
            out.println("<br><br>");

            out.println("<input class='btn' type='submit' value='Search'>");
            out.println("</form>");

            String selectedPublication = request.getParameter("publication");
            String selectedPrice = request.getParameter("price");

            if (selectedPublication != null || selectedPrice != null) {
                out.println("<h3>Filter Results:</h3>");

                StringBuilder booksql = new StringBuilder("SELECT * FROM books WHERE 1=1");
                if (selectedPublication != null && !selectedPublication.isEmpty()) {
                    booksql.append(" AND publication=?");
                }
                if (selectedPrice != null && !selectedPrice.isEmpty()) {
                    if (selectedPrice.equals("500+")) {
                        booksql.append(" AND price >= 500");
                    } else {
                        booksql.append(" AND price < ?");
                    }
                }

                PreparedStatement bookstmt = conn.prepareStatement(booksql.toString());
                int paramIndex = 1;

                if (selectedPublication != null && !selectedPublication.isEmpty()) {
                    bookstmt.setString(paramIndex++, selectedPublication);
                }
                if (selectedPrice != null && !selectedPrice.isEmpty() && !selectedPrice.equals("500+")) {
                    bookstmt.setInt(paramIndex, Integer.parseInt(selectedPrice));
                }

                ResultSet bookRs = bookstmt.executeQuery();

                out.println("<table border='1'>");
                out.println("<tr>");
                out.println("<th>BookId</th>");
                out.println("<th>Book Name</th>");
                out.println("<th>Author</th>");
                out.println("<th>Publication</th>");
                out.println("<th>Quantity</th>");
                out.println("<th>Available Quantity</th>");
                out.println("<th>Price</th>");
                out.println("</tr>");

                boolean hasResults = false;

                while (bookRs.next()) {
                    hasResults = true;
                    out.println("<tr>");
                    out.println("<td>" + bookRs.getString("id") + "</td>");
                    out.println("<td>" + bookRs.getString("bkname") + "</td>");
                    out.println("<td>" + bookRs.getString("author") + "</td>");
                    out.println("<td>" + bookRs.getString("publication") + "</td>");
                    out.println("<td>" + bookRs.getInt("quantity") + "</td>");
                    out.println("<td>" + bookRs.getInt("available_quantity") + "</td>");
                    out.println("<td>" + bookRs.getInt("price") + "</td>");
                    out.println("</tr>");
                }

                if (!hasResults) {
                    out.println("<tr><td colspan='7'>No Books found for the selected filters.</td></tr>");
                }

                out.println("</table>");
            } 
            else {
                out.println("<h2>Please select at least one filter to see results!<h2>");
            }
            out.println("<a class='btn btn-warning' href='AddBookServlet'>Home</a>");
            out.println("</div>");
            out.println("</center>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error occurred while fetching records. Please try again later.</p>");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); 
    }
}
