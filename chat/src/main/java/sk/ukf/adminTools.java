package sk.ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class adminPanel
 */
@WebServlet("/adminTools")
public class adminTools extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public adminTools() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public String adminButton(String command, String action, String method, String data) {
    	String btn = "";
    	if(command.equals("banBtn")) {
    		btn = "<form action='"+action+"' method='"+method+"'>"
					+"<button class='btn btn-outline-danger' type='submit' name='operacia' value='ban"+data+"'>Ban people</button>"
			+ "</form>";
    	
    	} else if(command.equals("unbanBtn")) {
    		btn = "<form action='"+action+"' method='"+method+"'>"
					+"<button class='btn btn-outline-primary btn-sm' type='submit' name='operacia' value='unban"+data+"'>Unban</button>"
				+ "</form>";
    	}
    	
    	return btn;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        response.setContentType("text/html;charset=UTF-8");        
        PrintWriter out  = response.getWriter();
	        out.println("<!DOCTYPE html>"
	         		+ "<html>"
	         		+ "<head>"
	         		+ "<meta charset=\"UTF-8\">"
	         		+ "<title>Chat App</title>"
	         		+ "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
	         		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"chat-style.css\">"
	         		+ "</head>");
        String table = "";
        String command = "SELECT * FROM banned";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			table += "<table id=\"myTable\" class=\"chat-container\">";
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			table += "<tr>";
				for (int i = 0; i < columnCount; i++) {
					table += "<th scope=\"col\" style=\"color: white\">Banned</th>";
				}
				 table += "<th scope=\"col\">" + "</th>";
			table += "</tr>";
			while (rs.next()) {
				table += "<tr>";
				String data="";
				for (int i = 0; i < columnCount; i++) {
					table += "<td scope=\"row\" style=\"color: white\";>" + rs.getString(i + 1) + "</td>";
					data+= "-"+rs.getString(1);
				}	
				table +=  "<td scope=\"row\">" + adminButton("unbanBtn","adminTools","post", data) + "</td>";
			}
			table += "<tr>"
					+ "<td colspan=\"100\">"+adminButton("banBtn","adminTools","post", "-add")+"</td>"
					+ "</tr>";
		table += "</tr>";
			table += "</table>";
		} catch (Exception ex) {
				table = ex.toString();
		}
		out.println(table);
		out.println("<a class='btn btn-danger' href='mainServlet'>Go Back</a>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
        String buttonValue = request.getParameter("operacia");
        if(buttonValue==null) {
			doGet(request, response);
        }
        String buttonValues[] = buttonValue.split("-");
        String operacia = buttonValues[0];
        String data = buttonValues[1];
        
		if(operacia.equals("ban")) {
			doGet(request, response);			
			String options = null;
			try {
				String command = "SELECT * FROM users WHERE email NOT IN (SELECT email FROM banned) AND admin = 0"; // email = 5
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(command);
				while(rs.next()) {
						String mail = rs.getString(5);
						options+= "<option value='"+mail+"'>"+mail+"</option>";
				}
			} catch (Exception e) {
				out.print(e.toString());
			}
			
			out.println("<form class='container w-25 mt-5' action='addText' method='post' id='banform'>");
				if(options==null) {
					out.println("<p class='bg-danger'>All members banned</p>");
				} else {
				out.println("<select class='form-select' form='banform' name='banMail'>");
					out.println(options);
				out.println("</select>");
				out.println("<input class='btn btn-danger' type='submit' value='Ban'>");
				}
			out.println("</form>");
		} else if(operacia.equals("unban")) {
			try {
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
				PreparedStatement insert = con.prepareStatement("DELETE FROM banned WHERE email="+"'"+data+"';");
				insert.execute();
			} catch(Exception e) {
				out.println(e.toString());
			}
			doGet(request, response);
		}
		
	}

}
