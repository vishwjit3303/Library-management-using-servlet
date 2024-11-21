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


@WebServlet("/AddBorrowerServlet")
public class AddBorrowerServlet extends HttpServlet {
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
       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet bookRs = stmt.executeQuery("SELECT * FROM books");

            out.println("<!doctype html>");
            out.println("<html><head><title>Issue Book</title>");
	        out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");

            out.println("<script>");
            out.println("function checkBookAvailability(bookId) {");
            out.println("    const bookStatus = document.getElementById('book-' + bookId);");
            out.println("    const availability = bookStatus ? bookStatus.value : 'Not Available';");
            out.println("    const bookStatusDisplay = document.getElementById('bookStatusDisplay');");
            out.println("    if (availability === 'Available') {");
            out.println("        bookStatusDisplay.style.color = 'green';");
            out.println("        bookStatusDisplay.innerText = 'Available';");
            out.println("    } else {");
            out.println("        bookStatusDisplay.style.color = 'red';");
            out.println("        bookStatusDisplay.innerText = 'Not Available';");
            out.println("    }");
            out.println("}");
            out.println("function fillUserName(userId) {");
            out.println("    const userNameField = document.getElementById('user-' + userId);");
            out.println("    const userNameDisplay = document.getElementById('userNameDisplay');");
            out.println("    userNameDisplay.innerText = userNameField ? userNameField.value : 'Unknown User';");
            out.println("}");
            out.println("function fillBookId(bookId) {");
            out.println("    document.getElementsByName('bookId')[0].value = bookId;");
            out.println("    checkBookAvailability(bookId);");
            out.println("}");
            out.println("function fillUserId(userId) {");
            out.println("    document.getElementsByName('userId')[0].value = userId;");
            out.println("    fillUserName(userId);");
            out.println("}");
            out.println("</script>");
            out.println("</head><body>");
            out.println("<br>");
            out.println("<center>");
            out.println("<div class='bg-book'");
            out.println("<h2>Issue Book</h2>");
            out.println("<br><br>");
            out.println("<form action='AddBorrowerServlet' method='post' onsubmit='return validateIssueForm();'>");
            out.println("BookId: <input type='text' name='bookId' placeholder='Enter BookId' oninput='checkBookAvailability(this.value)'><span id='bookStatusDisplay'></span><br><br>");
            out.println("UserId: <input type='text' name='userId' placeholder='Enter UserId' oninput='fillUserName(this.value)'><span id='userNameDisplay'></span><br><br>");
            out.println("<input class='btn btn-warning' type='submit' name='issue' value='Issue Book'>");
            out.println("<input class='btn btn-warning' type='submit' name='return' value='Return Book'><br>");
            out.println("<a class='btn btn-warning' href='AddBookServlet'>Home </a>");
            out.println("</form>");
            out.println("</div>");
            out.println("</center>");
            
            out.println("<br>");
            
            String errorMessage = (String) request.getAttribute("errorMessage");
            if(errorMessage != null) {
            	out.println("<span style='color:red;font-weigth:600;font-size:20px'>"+errorMessage+"</span><br>");
            }
            
            out.println("<br>");
            
            int Deposit=100;
            String msg = Deposit < 50 ? "Low Deposit!":"";
            
            out.println("<span style='color:red;font-weigth:600;'>"+msg+"</span>");
            out.println("<br><br>");
            

            while (bookRs.next()) {
                String bookId = bookRs.getString("id");
                String availability = bookRs.getInt("available_quantity") > 0 ? "Available" : "Not Available";
                out.println("<input type='hidden' id='book-" + bookId + "' value='" + availability + "'>");
            }

           
            ResultSet userRs = stmt.executeQuery("SELECT * FROM users");
	        while (userRs.next()) {
	            String userId = String.valueOf(userRs.getInt("uid"));
	            String userName = userRs.getString("name");
	            out.println("<input type='hidden' id='user-" + userId + "' value='" + userName + "'>");
	        }
	        
	        
	        out.println("<div style='display:flex; justify-content: space-around;'>");
	        
	        out.println("<div class='bg-book' style='display:flex; justify-content: center; flex-direction:column; text-align:center;'>");
	        out.println("<h2>Books List</h2>");
	        ResultSet rs = stmt.executeQuery("SELECT * FROM books");
	        out.println("<table border = '1'>");
			out.println("<tr>");
			out.println("<th>Id</th> <th>Book Name</th> <th>Author</th> <th>Publication</th> <th>Quantity</th> <th>Available Quantity</th>");
			out.println("</tr>");
			
			while (rs.next()) {
			    String bookId = rs.getString("id");
			    out.println("<tr onclick=\"fillBookId('" + bookId + "')\">");
			    out.println("<td>" + bookId + "</td>");
			    out.println("<td>" + rs.getString("bkname") + "</td>");
			    out.println("<td>" + rs.getString("author") + "</td>");
			    out.println("<td>" + rs.getString("publication") + "</td>");
			    out.println("<td>" + rs.getString("quantity") + "</td>");
			    out.println("<td>" + rs.getString("available_quantity") + "</td>");
			    out.println("</tr>");
			}
			
			out.println("</table> <br>");
	        out.println("</div>");
	        out.println("<br><br>");
	        
	        
	        out.println("<center>");

	        out.println("<div class='bg-book' style='display:flex; justify-content: center; flex-direction:column; text-align:center;'>");
	        out.println("<h2>User List</h2>");
			ResultSet rst = stmt.executeQuery("SELECT * FROM users"); 

	        out.println("<table border='1'>");
	        out.println("<tr> <th>UserID</th> <th>Name</th> <th>Email</th> <th>Contact</th> <th>Charges</th></tr>");

	        while (rst.next()) {
	            int userId = rst.getInt("uid");
	            out.println("<tr onclick=\"fillUserId('"+userId+"')\"> ");
	            out.println("<td>" + userId + "</td>");
	            out.println("<td>" + rst.getString("name") + "</td>");
	            out.println("<td>" + rst.getString("email") + "</td>");
	            out.println("<td>" + rst.getString("contact") + "</td>");
	            out.println("<td>" + rst.getString("charges") + "</td>");
	            out.println("</td>");
	            out.println("</tr>");
	        }
	        out.println("</table><br>");
	        out.println("</div>");
	        out.println("</center>");
	        out.println("</div>");
	        out.println("<br>");

	        out.println("<center>");
	        out.println("<div class='bg-book' style='display:flex; justify-content: center; flex-direction:column;'>");
	        out.println("<h2>Issue List</h2>");
			ResultSet result = stmt.executeQuery("SELECT * FROM issue");
			out.println("<table border='1'>");
	        out.println("<tr> <th>isID</th> <th>uid</th> <th>bkid</th> <th>issue date</th> <th>submit date</th> <th>Status</th> </tr>");
	        
	        while(result.next()) {
	        	out.println("<tr>");
				out.println("<td>"+result.getInt("isId")+"</td>");
				out.println("<td>"+result.getInt("uid")+"</td>");
				out.println("<td>"+result.getInt("bkid")+"</td>");
				out.println("<td>"+result.getDate("issue_dt")+"</td>");	
				out.println("<td>"+result.getDate("submit_dt")+"</td>");
				out.println("<td>"+result.getString("status")+"</td>");
				out.println("</td>");
				out.println("</tr>");
	        }
	        out.println("</table><br>");
			out.println("</div>");
			out.println("</center>");

	        out.println("</body></html>");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new ServletException("Data display error");
	    }
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String bookIdParam = request.getParameter("bookId");
	    String userId = request.getParameter("userId");
	    String issueAction = request.getParameter("issue");
	    String returnAction = request.getParameter("return");

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();

	        if (issueAction != null) { 
	            int bookId = Integer.parseInt(bookIdParam);
	            int UserId = Integer.parseInt(userId);
	           // int userIdInt = Integer.parseInt(userId);
	            
	            // less than 50
	            String checkChargesSql = "SELECT charges FROM  users WHERE uid=?";
	            PreparedStatement chargesStmt = conn.prepareStatement(checkChargesSql);
	            chargesStmt.setInt(1,UserId);
	            ResultSet chargesRs = chargesStmt.executeQuery();
	            if (chargesRs.next() && chargesRs.getInt("charges") < 50) {
//	                out.println("<h3>You cannot issue books as your charges are below 50.</h3>");
	            	request.setAttribute("errorMessage", "Low Charges Amount!");
	            	doGet(request,response);
	            	 return;
	                
	            }
	            
	            //only two books
	            String checkIssuedBooksSql = "SELECT COUNT(*) AS issued_count FROM issue WHERE uid=? AND status = 'issued'";
	            PreparedStatement issuedBooksStmt = conn.prepareStatement(checkIssuedBooksSql);
	            issuedBooksStmt.setInt(1, UserId);
	            ResultSet issuedBooksRs = issuedBooksStmt.executeQuery();
	            if (issuedBooksRs.next() && issuedBooksRs.getInt("issued_count") >= 2) {
	            	request.setAttribute("errorMessage", "You cannot borrow a new book. please return previously books");
	            	doGet(request,response);
	                return;
	            }
	            

	            String checkAvailabilitySql = "SELECT available_quantity FROM books WHERE id = ?";
	            PreparedStatement checkStmt = conn.prepareStatement(checkAvailabilitySql);
	            checkStmt.setInt(1, bookId);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next() && rs.getInt("available_quantity") > 0) {
	                // Update book availability
	                String updateBookSql = "UPDATE books SET available_quantity = available_quantity - 1 WHERE id = ?";
	                PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
	                updateBookStmt.setInt(1, bookId);
	                updateBookStmt.executeUpdate();
	                
	                String updateUserSql = "UPDATE users SET charges = charges - 10 WHERE uid = ?";
	                PreparedStatement updateUserStmt = conn.prepareStatement(updateUserSql);
	                updateUserStmt.setInt(1, UserId);
	                updateUserStmt.executeUpdate();

	                String issueSql = "INSERT INTO issue (uid, bkid, issue_dt, status) VALUES (?, ?, NOW(), 'Issued')";
	                PreparedStatement issueStmt = conn.prepareStatement(issueSql);
	                issueStmt.setString(1, userId);
	                issueStmt.setInt(2, bookId);
	                issueStmt.executeUpdate();
	                

	                out.println("<h3>Book issued successfully!</h3>");
	            } else {
	                out.println("<h3>Book issue failed. Book may not be available.</h3>");
	            }
	        } 
	        
	        if(returnAction != null) {
	        
	        	int bookId = Integer.parseInt(bookIdParam);

            String checkQuantitySql = "SELECT available_quantity, quantity FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuantitySql);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int availableQuantity = rs.getInt("available_quantity");
                int totalQuantity = rs.getInt("quantity");

                if (availableQuantity < totalQuantity) {
                    // Update book availability
                    String updateBookSql = "UPDATE books SET available_quantity = available_quantity + 1 WHERE id = ?";
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
                    updateBookStmt.setInt(1, bookId);
                    updateBookStmt.executeUpdate();

                    // Update issue record
                    String updateIssueSql = "UPDATE issue SET submit_dt = NOW(), status='Returned' WHERE uid = ? AND bkid = ? AND submit_dt IS NULL";
                    PreparedStatement issueStmt = conn.prepareStatement(updateIssueSql);
                    issueStmt.setString(1, userId);
                    issueStmt.setInt(2, bookId);
                    issueStmt.executeUpdate();

                    out.println("<h3>Book returned successfully!</h3>");
                    
                } else {
                    out.println("<h3>Book return failed. Available quantity exceeds total quantity.</h3>");
                    System.out.println("book not returned due to insufficient quantity");
                }
            }
	        }
	        response.sendRedirect("AddBorrowerServlet");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new ServletException("Error processing request");
	    }
	}
}
