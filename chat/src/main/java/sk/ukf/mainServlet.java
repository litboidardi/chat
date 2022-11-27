package sk.ukf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class mainServlet
 */
@WebServlet("/mainServlet")
public class mainServlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	 Connection con = null; 
	 HttpSession session;
	 String error = "";
	 
	 @Override
	 public void init() throws ServletException {
	   super.init();
	   try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
	  } catch (Exception e) {  
		  error = e.getMessage();   }
	  }

    /**
     * Default constructor. 
     */
    public mainServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        PrintWriter out = response.getWriter();
		printInfo(out, request);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (con == null) {
            	out.println(error);
            	return; 
            	}
            String operacia = request.getParameter("operacia");
            int user_id = getUserID(request);
            if (operacia.equals("login")) { 
            	getUserInfo(out, request); 
            	}
            if (operacia == null || user_id == 0) { 
            	out.println("Incorrect input"); 
            	return; 
            	}
            if (operacia.equals("logout")) {
            	logoutTrue(out, request);
            	return; 
            	}
            printInfo(out, request);
        } catch (Exception e) {  out.println(e); }

	}
	 
	 protected void getUserInfo(PrintWriter out, HttpServletRequest request) {
	        try {
	            String meno = request.getParameter("login");
	            String heslo = request.getParameter("pass");
	            Statement stmt = con.createStatement();
	            String sql = "SELECT MAX(id), COUNT(id) AS pocet FROM users WHERE email = '"+meno+"' AND passwd = '"+heslo+"'";
	            ResultSet rs = stmt.executeQuery(sql);
	            rs.next();
	            session = request.getSession();
	             
	            if (rs.getInt("pocet") == 1) {
	              sql = "SELECT * FROM users WHERE email = '"+meno+"'"; 
	              rs = stmt.executeQuery(sql);
	              rs.next();
	              
	              session.setAttribute("ID", rs.getInt("id")); 
	              session.setAttribute("meno", rs.getString("meno"));
	              session.setAttribute("priezvisko", rs.getString("priezvisko"));
	              session.setAttribute("nickname", rs.getString("nickname"));
	              session.setAttribute("email", rs.getString("email"));
	              session.setAttribute("passwd", rs.getString("passwd"));
	              session.setAttribute("admin", rs.getInt("admin"));
	              
	              sql = "SELECT * FROM banned WHERE email = '"+meno+"'";
	              rs = stmt.executeQuery(sql);
	      
	              if(rs.next() == true) {
		              session.setAttribute("banned", rs.getString("email"));
	              } 
	            } else {
	              out.println("Prihlasovacie údaje nie sú v poriadku.");
	              session.invalidate();
	            }
	            rs.close();
	            stmt.close();
	           } catch (Exception ex) {
	        	   out.println(ex.getMessage()); 
	        }
           }
	 
	 	protected int getUserID(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        Integer id = (Integer)(session.getAttribute("ID"));
	        if (id == null) id = 0;
	        return id;
	    }
	 	
	 	  protected void vypisHlavicka(PrintWriter out, HttpServletRequest request) {
	 	        session = request.getSession();
	 	        out.println("<!DOCTYPE html>"
		         		+ "<html>"
		         		+ "<head>"
		         		+ "<meta charset=\"UTF-8\">"
		         		+ "<title>Chat app</title>"
		         		+ "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
		         		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"chat-style.css\">"
		         		+ "</head>");
	 	 }
	 	  
	 	 protected void printInfo(PrintWriter out, HttpServletRequest request) {
	            vypisHlavicka(out, request);
	            String nickname=(String) session.getAttribute("nickname");
	            String name="";
	            String text = "";
	            String banned = "";
	            if(session.getAttribute("banned") == null) {} 
	            else {
		            if(session.getAttribute("banned").equals(session.getAttribute("email"))) {
			            banned = session.getAttribute("banned").toString();
		            }
	            }
	         try {
	 		 Statement stmt = con.createStatement();
	 		 String sql = "SELECT * FROM prispevky INNER JOIN users ON (users.nickname = prispevky.nickname) WHERE email NOT IN (SELECT email FROM banned) ";
	 		 ResultSet rs = stmt.executeQuery(sql);
	 		 
	 	        out.println("<nav class='navbar'>" + "<h5>"+"Logged in as: "+ session.getAttribute("meno")+"  "+session.getAttribute("priezvisko") + "</h5>");
	 	        out.println("<style> h5 { color: white }</style>");
	 	        out.println("<div class='d-flex flex-row'>");
	 	        
		 		 int isAdmin = (int) session.getAttribute("admin");
		 		 if(isAdmin == 1) {
		 			 out.println("<a class='btn btn-danger' target='_blank' href='adminTools'>Admin Tools</a>");
		 		 }
		 		 
	            out.println("<form method='post' action='index.html'>");
	            out.println("<input type='hidden' name='operacia' value='logout'>");
	            out.println("<input class='btn btn-dark' type='submit' value='Logout'>");
	            out.println("</form>");
	            out.print("</div>");
	            out.println("</nav>");
	 		 

	            out.println("<div class='chat-container'>");
	            out.println("<div class='chat'>");
	            out.println();
	 		 while (rs.next()) {
	 			name = rs.getString(1);
	 			text = rs.getString(2);
	 				if(name.equals(session.getAttribute("nickname").toString())) {
				         out.println("<p class='message right'>"+text+"</p>");
	 				} else {
	 					out.println("<p class='message left'>"+name+":  "+text+"</p>");
	 				}
	 		 }
	 		 out.println("</div>");

	 		 if(banned.equals("")) {
	            out.println("<form class='col' method='get' action='addText'>");
	            out.println("<input type='hidden' name='name' value='"+nickname+"'>");
	            out.println("<input type='text' name='text'>");
	            out.println("<input class='btn btn-primary' type='submit' value='Send'>");
	            out.println("</form>");
	            out.println("</div>");	      
	 		 } else {
	 			 out.print("<p class='text-center bg-danger'>YOU WERE BANNED!</p>");
	 		 }
	        } catch (Exception ex) {
	            out.println(ex.getMessage());
	        }
	    }
	 	 protected void logoutTrue(PrintWriter out, HttpServletRequest request) {
	         HttpSession session = request.getSession();
	         session.invalidate();
	        
	       
	     }
}
