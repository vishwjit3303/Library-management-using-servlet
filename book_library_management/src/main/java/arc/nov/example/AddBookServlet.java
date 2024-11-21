package arc.nov.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet("/AddBookServlet")
@MultipartConfig(location = "/tmp", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 10, fileSizeThreshold = 1024)

public class AddBookServlet extends HttpServlet {
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
		
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM books");
			
			out.println("<!doctype html>");
			out.println("<html><head><title>Add Book</title>");
			out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");
//			out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH' crossorigin='anonymous'>");
			out.println("</head>");
			out.println("<body>");

			// data display
			

			out.println();
			out.println("<br><br>");
			out.println("<center>");
			out.println("<a class='btn' href='AddUserServlet' style='display: inline; margin: 15px;'>Add User</a>");
			out.println("<a class='btn' href='AddBorrowerServlet' style='display: inline; margin: 15px;'>Issue Book</a>");
			out.println("<a class='btn' href='FetchRecordsBook' style='display: inline; margin: 15px;'>Fetch Records</a> ");
			out.println("<a class='btn' href='UserDetailsServlet' style='display: inline; margin: 15px;'>User Details</a>");
			out.println("<a class='btn' href='RequiredBookData' style='display: inline; margin: 15px;'>Required Book Data</a>");
			out.println("<a class='btn' href='LoginPage' style='display: inline; margin: 15px;'>Logout</a> <br><br>");

			out.println("</center>");
			
			
			out.println("<center>");
			out.println("<div class='bg-book'>");
			out.println("<h2>Enter Book details</h2>");
			out.println("<form action='AddBookServlet' method='post' enctype='multipart/form-data'>");
			
			out.println("<span class='color-white'> Book Name: </span> <input type='text' name='name' placeholder='Enter Book Name'> <br><br>");
			out.println("<span class='color-white'> Author:  </span> <input type='text' name='author' placeholder='Enter Author Name'> <br><br>");
			out.println("<span class='color-white'> Publication: </span><input type='text' name='publication' placeholder='Enter Publication Name'> <br><br>");
			out.println("<span class='color-white'>Ouantity: </span><input type='text' name='quantity' placeholder='Enter Quantity'> <br><br>");
			out.println("<span class='color-white'> Price: </span><input type='text' name='price' placeholder='Enter Price'> <br><br>");
			out.println("<span class='color-white'> Upload Book Image: </span> <input class='color-white' type='file' name='file' id='book_image' accept='image/*' required> <br><br>");

			out.println("<input class='btn btn-warning' type='submit' value='Add Book'> ");
			out.println("</form>");
			out.println("</div>");
			out.println("</center>");
			
			
			out.println("<center>");
			out.println("<table border = '1'>");
			out.println("<tr>");
			out.println("<th>Id</th> <th>Book Name</th> <th>Author</th> <th>Publication</th> <th>Quantity</th> <th>Available Quantity</th> <th>Price</th>");
			out.println("</tr>");
			
			while(rs.next()) {
				out.println("<tr>");
				out.println("<td>"+rs.getString("id")+"</td>");
				out.println("<td>"+rs.getString("bkname")+"</td>");
				out.println("<td>"+rs.getString("author")+"</td>");
				out.println("<td>"+rs.getString("publication")+"</td>");	
				out.println("<td>"+rs.getString("quantity")+"</td>");
				out.println("<td>"+rs.getString("available_quantity")+"</td>");
				out.println("<td>"+rs.getString("price")+"</td>");
				
				out.println("</td>");
				out.println("</tr>");
			}
			
			out.println("</table> <br>");

			out.println("</center>");
			out.println("</body></html");
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException("Data display error");
			
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final String UPLOAD_DIRECTORY = "C:/Users/vishw/eclipse-workspace/book_library_management/src/main/webapp/images/";
	       String uploadedImagePath="";
	       File uploadDir = new File(UPLOAD_DIRECTORY);
	       if (!uploadDir.exists()) {
	           uploadDir.mkdir();
	       }
	       
	       Part filePart = request.getPart("file");
	       String fileName = getFileName(filePart);
	       
	       
	       if (filePart != null && fileName != null && !fileName.isEmpty()) {
	           // Save the file to the upload directory
	    	   String filePath = UPLOAD_DIRECTORY + fileName;
	    	   filePart.write(filePath);
	    	   
	    	    uploadedImagePath = "images/" + fileName;
	    	    
	    	         } else {
	           throw new ServletException("File upload failed. Ensure a file is selected.");
	       }
	       

		
		String name = request.getParameter("name");
		String author = request.getParameter("author");
		String publication = request.getParameter("publication");
		String quantity = request.getParameter("quantity");
		String price = request.getParameter("price");
		
		
		System.out.println("In Post(): "+name+", "+author+", "+publication+", "+quantity+", "+price); 
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connection established");
			
			
			String sql = "INSERT INTO books (bkname,author,publication,quantity,available_quantity,price,img)VALUES(?,?,?,?,?,?,?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, name);
			statement.setString(2, author);
			statement.setString(3, publication);
			statement.setInt(4, Integer.parseInt(quantity));
			statement.setInt(5, Integer.parseInt(quantity));
			statement.setInt(6, Integer.parseInt(price));
			statement.setString(7, uploadedImagePath);

			
			
			statement.executeUpdate();
			System.out.println("Record loaded successfully");
			response.sendRedirect("AddBookServlet");
			
			
			
		} catch (SQLException e) {

			e.printStackTrace();
			 response.setContentType("text/html");
		     PrintWriter out = response.getWriter();
		     out.println("<html><body>");
		     out.println("<h3>Error: Id not present</h3>");
		     out.println("<p>" + e.getMessage() + "</p>"); 
		     out.println("</body></html>");		
			
		}
	}


	private String getFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "";
	}

}
