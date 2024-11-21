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

@WebServlet("/UserDetailsServlet")
public class UserDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/booklab";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@1234";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int userId = 1; // Default to the first user
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            userId = Integer.parseInt(userIdParam);
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Fetch user details
            PreparedStatement userStmt = conn.prepareStatement("SELECT * FROM users WHERE uid = ?");
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                out.println("<!doctype html>");
                out.println("<html><head>");
                out.println("<title>User Details</title>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; background-color: #f0f0f0; }");
                out.println(".container { text-align: center; margin-top: 50px; }");
                out.println(".card { display: inline-block; border: 2px solid #ccc; padding: 20px; background-color: #C0C0C0; border-radius: 10px; width: 350px; }");
                out.println(".card p { margin: 10px 0; }");
                out.println(".navigation { margin-top: 20px; }");
                out.println(".button { background-color: #007bff; color: white; border: none; padding: 10px 20px; margin: 5px; border-radius: 5px; text-decoration: none; font-size: 16px; }");
                out.println(".button:hover { background-color: #0056b3; }");
                out.println(".home-button { margin-top: 20px; display: inline-block; }");
                out.println("</style>");
    	        out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");

                out.println("</head><body>");
                out.println("<center>");
                out.println("<div class='container bg-book'>");
                out.println("<h1>User Details</h1>");
                out.println("<div class='card'>");

                int uid = userRs.getInt("uid");
                String name = userRs.getString("name");
                String email = userRs.getString("email");
                String contact = userRs.getString("contact");
                int charges = userRs.getInt("charges");

                out.println("<p><strong>ID:</strong> " + uid + "</p>");
                out.println("<p><strong>Name:</strong> " + name + "</p>");
                out.println("<p><strong>Email:</strong> " + email + "</p>");
                out.println("<p><strong>Contact:</strong> " + contact + "</p>");
                out.println("<p><strong>Deposit:</strong> RS:" + charges + "</p>");

                // Fetch books issued to the user
                PreparedStatement booksStmt = conn.prepareStatement(
                        "SELECT b.bkname FROM issue i JOIN books b ON i.bkid = b.id WHERE i.uid = ? AND i.status = 'Issued'");
                booksStmt.setInt(1, uid);
                ResultSet booksRs = booksStmt.executeQuery();

                out.println("<p><strong>Books Issued:</strong></p>");
                out.println("<ul>");
                boolean hasBooks = false;
                while (booksRs.next()) {
                    hasBooks = true;
                    out.println("<li>" + booksRs.getString("bkname") + "</li>");
                }
                if (!hasBooks) {
                    out.println("<li>No books issued</li>");
                }
                out.println("</ul>");
                out.println("</div>");

                // Navigation buttons
                out.println("<div class='navigation'>");
                // First user
                out.println("<a href='UserDetailsServlet?userId=1' class='btn'>|&lt;</a>");

                // Fetch the previous user ID
                PreparedStatement prevStmt = conn.prepareStatement("SELECT MAX(uid) AS prevId FROM users WHERE uid < ?");
                prevStmt.setInt(1, uid);
                ResultSet prevRs = prevStmt.executeQuery();
                if (prevRs.next() && prevRs.getInt("prevId") != 0) {
                    int prevId = prevRs.getInt("prevId");
                    out.println("<a href='UserDetailsServlet?userId=" + prevId + "' class='btn'>&lt;</a>");
                }

                // Fetch the next user ID
                PreparedStatement nextStmt = conn.prepareStatement("SELECT MIN(uid) AS nextId FROM users WHERE uid > ?");
                nextStmt.setInt(1, uid);
                ResultSet nextRs = nextStmt.executeQuery();
                if (nextRs.next() && nextRs.getInt("nextId") != 0) {
                    int nextId = nextRs.getInt("nextId");
                    out.println("<a href='UserDetailsServlet?userId=" + nextId + "' class='btn'>&gt;</a>");
                }

                // Last user
                PreparedStatement lastStmt = conn.prepareStatement("SELECT MAX(uid) AS lastId FROM users");
                ResultSet lastRs = lastStmt.executeQuery();
                if (lastRs.next()) {
                    int lastId = lastRs.getInt("lastId");
                    out.println("<a href='UserDetailsServlet?userId=" + lastId + "' class='btn'>&gt;|</a>");
                }
                out.println("</div>");

                out.println("<a href='AddBookServlet' class='btn'>Home</a>");
                out.println("</div>");
                out.println("</center>");
                out.println("</body></html>");
            } else {
                out.println("<h3>No user found</h3>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3>Error retrieving user details</h3>");
        }
    }
}
