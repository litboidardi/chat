package sk.ukf;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addText
 */
@WebServlet("/addText")
public class addText extends mainServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addText() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        PrintWriter prtw = response.getWriter();
        String name = request.getParameter("name");
        String text = request.getParameter("text");
        
        prtw.println(name+":  "+text);
        try {
			PreparedStatement prps = con.prepareStatement("INSERT INTO prispevky (nickname, text) "
															+"VALUES('"+name+"', '"+text+"');");
			prps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			prtw.println(e.toString());
		}
		RequestDispatcher rd = request.getRequestDispatcher("mainServlet");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter prtw = response.getWriter();
		prtw.println("Ban this user: "+request.getParameter("banMail"));
		String banMail = request.getParameter("banMail");
		try {
			
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
			PreparedStatement insert = con.prepareStatement("INSERT INTO banned(email) VALUES('"+banMail+"')");
			insert.execute();
		} catch(Exception e) {
			prtw.print(e.toString());
		};
		
		RequestDispatcher rd = request.getRequestDispatcher("adminTools");
		rd.forward(request, response);
	}
}