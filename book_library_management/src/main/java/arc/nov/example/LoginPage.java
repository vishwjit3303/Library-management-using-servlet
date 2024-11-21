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
import javax.servlet.http.HttpSession;

@WebServlet("/LoginPage")
public class LoginPage extends HttpServlet {
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
        out.println("<title>Login Page</title>");
        out.println("<link rel='stylesheet' type='text/css' href='css/login.css'>");  
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='login-container'>");
        out.println("<h2>Login</h2>");
        out.println("<form action='LoginPage' method='post'>");
        out.println("<label for='username'>Username:</label>");
        out.println("<input type='text' id='username' name='username' placeholder='Enter your username' required>");
        out.println("<label for='password'>Password:</label>");
        out.println("<input type='password' id='password' name='password' placeholder='Enter your password' required>");
        out.println("<button type='submit' class='btn'>Login</button>");
        out.println("</form>");
        out.println("<a href='SignUpPage'>Don't have an account? Sign up</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String sql = "SELECT password FROM registers WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (password.equals(storedPassword)) {
                	
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    response.sendRedirect("AddBookServlet");  
                } else {
                	
                	out.println("<script type='text/javascript'>");
                    out.println("alert('Invalid Username or Password!');");
                    out.println("window.location.href = 'LoginPage';");
                    out.println("</script>");
                }
            } else {
            	
//                out.println("<h2>User not found!</h2>");
//                out.println("<a href='LoginPage'>Go Back</a>");
                
                out.println("<script type='text/javascript'>");
                out.println("alert('Invalid Username or Password!');");
                out.println("window.location.href = 'LoginPage';");
                out.println("</script>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error: Unable to log in</h2>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
