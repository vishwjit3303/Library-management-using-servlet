package arc.nov.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RequiredBookData")
@MultipartConfig(location = "/tmp", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10, fileSizeThreshold = 1024)

public class RequiredBookData extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/booklab";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@1234";
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        String[] selectedOptions = request.getParameterValues("selectedOptions");
        
        out.println("<!doctype= 'html'>");
		out.println("<html> <head> <title> Library Management System </title>");
        out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");

		out.println("</head>");
		out.println("<br>");
        out.println("<a class='btn btn-warning' href='AddBookServlet'>back to home </a>");

		out.println("<center><h1 style='color:white; font-family: 'Lobster', sans-serif;'>Library Management System!!</h1></center>");
		out.println("<br>");
		out.println("<br>");
		out.println("</center>");
		
		out.println("<center>"
				+ "<h2 style='color:white;'>Available Books</h2>"
				
				+"<form action='RequiredBookData' method='GET'>"
				
				+" <label>"
				+ "            <input type='checkbox' name='selectedOptions' value='id'>"
				+"              <span style='color:white;'>Id"
				
				
				+ "            <input type='checkbox' name='selectedOptions' value='bkname'>"
				+"              <span style='color:white;'>Title"
				
				
				+ "            <input type='checkbox' name='selectedOptions' value='price'>"
				+"              <span style='color:white;'>Price"
				
				+ "            <input type='checkbox' name='selectedOptions' value='img'>"
				+ "            <span style='color:white;'>Img"
				
				
				+ "        </label>"
				
				
				+"			<input class='btn btn-warning' type='submit' value='submit' onclick='getSelectedCheckboxes()'>"
				
				
				+"</form>"
				
				
				
				+ "</center>");
		
		if (selectedOptions != null && selectedOptions.length > 0) {
			 try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
				 
	                String selectedOptionsString = String.join(", ", selectedOptions);
	                String sql = "SELECT " + selectedOptionsString + " FROM books;";
	                PreparedStatement stmt = conn.prepareStatement(sql);
	                ResultSet rs = stmt.executeQuery();

	                out.println("<br><center><table  border='1'; style='width:fit-content; height:400px;'>");
	                out.println("<thead><tr>");
	                for (String option : selectedOptions) {
	                    out.println("<th>" + option.toUpperCase() + "</th>");
	                }
	                out.println("</tr></thead><tbody>");
	                
	                while (rs.next()) {
	                    out.println("<tr>");
	                    for (String option : selectedOptions) {
	                    	
	                    	if(option.equals("img")){
	                    	    String imgPath = rs.getString(option);
	                    	    if (imgPath != null && !imgPath.trim().isEmpty()) {
	                    	        out.println("<td><img src='" + imgPath + "' width='70' height='70'> </td>");
	                    	    } else {
	                    	        out.println("<td>No Image</td>");
	                    	    }
	                    	}else
	                    	{
	                        out.println("<td>" + rs.getString(option) + "</td>");
	                    	}
	                    }
	                    out.println("</tr>");
	                }
	                out.println("</tbody></table></center>");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                out.println("<h3>Error accessing the database</h3>");
	            }
	            
		}else {
		   out.println("<center>");
           out.println("<h3 style='color:red; background-color:black; width:fit-content;'>Please select at least one option to display data.</h3>");
           out.println("</center>");

		}
		
		out.println("</body></html>");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
