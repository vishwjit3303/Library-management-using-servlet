package arc.nov.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddUserServlet
 */
@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost:3306/booklab";
	private static final String USER = "root";
	private static final String PASSWORD = "Archer@1234";
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Drivers loaded successfully");
		} catch(Exception e) {
			throw new ServletException("Unable to load drivers");
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		 	out.println("<!doctype html>");
	        out.println("<html><head><title>Add User</title>");
	        out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");
			//out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<center>");
	        out.println("<div class='bg-book'>");
	        out.println("<h1> Add New User </h1>");
	        out.println("<form action='AddUserServlet' method='post'>");
	        out.println("Name: <input type='text' name='name' placeholder='Enter Name'><br><br>");
	        out.println("Email: <input type='email' name='email' placeholder='Enter Email'><br><br>");
	        out.println("Contact: <input type='text' name='contact' placeholder='Enter Contact'><br><br>");
	        out.println("Charges: <input type='text' name='charges' placeholder='Enter charges for books'><br><br>");
	        
	        out.println("<input class='btn btn-warning' type='submit' value='Add User'><br><br>");
	        out.println("</form>");
	        out.println("</div>");
	        out.println("</center");
	        out.println("<br><br>");
	        
	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery("SELECT * FROM users"); 
	            	
	            out.println("<center>");
	            out.println("<div class='bg-book'>");
	            out.println("<h2>User List</h2>");
	            out.println("<table border='1'>");
	            out.println("<tr> <th>UserID</th> <th>Name</th> <th>Email</th> <th>Contact</th> <th>Charges</th> </tr>");

	            while (rs.next()) {
	                int userId = rs.getInt("uid");
	                out.println("<tr>");
	                out.println("<td>" + userId + "</td>");
	                out.println("<td>" + rs.getString("name") + "</td>");
	                out.println("<td>" + rs.getString("email") + "</td>");
	                out.println("<td>" + rs.getString("contact") + "</td>");
	                out.println("<td>" + rs.getString("charges") + "</td>");
	                
//	                out.println("<td><a class='btn btn-warning' href='IssuedServlet?studentId=" + studentId + "'>Issue Book</a></td>");
//	                out.println("<td><a class='btn btn-warning' href='ReturnBookServlet?studentId=" +studentId + "'>Return Book</a></td>");
	                
	                out.println("</tr>");
	            }
	            out.println("</table><br>");
	            out.println("<a class='btn btn-warning' href='AddBookServlet'>back to home </a>");
	            out.println("</center>");
	            out.println("</div>");
	            
	            out.println("</body></html>");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new ServletException("Error retrieving user data");
	        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String name = request.getParameter("name");
	        String email = request.getParameter("email");
	        String contact = request.getParameter("contact");
	        String charges = request.getParameter("charges");

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String sql = "INSERT INTO users (name, email, contact, charges) VALUES (?, ?, ?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, name);
	            stmt.setString(2, email);
	            stmt.setString(3, contact);
	            stmt.setString(4, charges);
	            
	            stmt.executeUpdate();

	            System.out.println("User added successfully");
	            response.sendRedirect("AddUserServlet");

	        } catch (SQLException e) {
	            e.printStackTrace();
	            response.setContentType("text/html");
	            PrintWriter out = response.getWriter();
	            out.println("<html><body>");
	            out.println("<h3>Error: Unable to add user</h3>");
	            out.println("<p>" + e.getMessage() + "</p>");
	            out.println("</body></html>");
	        }
	}

}
