package arc.nov.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUpPage")
public class SignUpPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/booklab";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@1234";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Sign Up Page</title>");
        out.println("<link rel='stylesheet' type='text/css' href='css/login.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='login-container'>");
        out.println("<h2>Sign Up</h2>");
        out.println("<form action='SignUpPage' method='post'>");
        out.println("<label for='username'>Username:</label>");
        out.println("<input type='text' id='username' name='username' placeholder='Enter your username' required>");
        out.println("<label for='password'>Password:</label>");
        out.println("<input type='password' id='password' name='password' placeholder='Enter your password' required>");
        out.println("<label for='confirm-password'>Confirm Password:</label>");
        out.println("<input type='password' id='confirm-password' name='confirm-password' placeholder='Confirm your password' required>");
        out.println("<button type='submit' class='btn'>Sign Up</button>");
        out.println("</form>");
        out.println("<p>Already have an account? <a href='LoginPage'>Login</a></p>"); // Added login link
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Sign Up Result</title>");
        out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");
        out.println("</head>");
        out.println("<body>");

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            out.println("<h2>All fields are required!</h2>");
            out.println("<a href='SignUpPage'>Go Back</a>");
        } else if (password.equals(confirmPassword)) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

                String sql = "INSERT INTO registers (username, password) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password); 

                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    response.sendRedirect("LoginPage");
                } else {
//                    out.println("<h2>There was an issue signing up. Please try again.</h2>");
                	out.println("<script type='text/javascript'>");
                    out.println("alert('There was an issue signing up. Please try again.');");
                    out.println("window.location.href = 'SignUpPage';");
                    out.println("</script>");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<h2>Error: Unable to add user</h2>");
                out.println("<p>" + e.getMessage() + "</p>");
            }
        } else {
            out.println("<h2>Passwords do not match!</h2>");
            out.println("<a href='SignUpPage'>Go Back</a>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
