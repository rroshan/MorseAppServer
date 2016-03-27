package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bo.Message;
import core.PostToGCM;
import dao.RegistrationDAO;

/**
 * Servlet implementation class MorsePushServlet
 */
public class MorsePushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MorsePushServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fromUser = request.getParameter("fromUserId");
		String toUserId = request.getParameter("toUserId");
		RegistrationDAO dao = new RegistrationDAO();
		
		if(request.getParameterMap().containsKey("message"))
		{
			String message = request.getParameter("message");
			
			String token = dao.fetchRegistrationToken(toUserId);
			
	        String apiKey = "AIzaSyDxei_Bq4XhUMCEQCUxC9PaV-ei1BQ92TY";
	        Message m = new Message();

	        m.addRegId(token);
	        m.createData(fromUser, message);

	        PostToGCM.post(apiKey, m);
		}
		else
		{
			if(!dao.checkIfExists(toUserId))
			{
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				response.setStatus(450);
				out.write("{\"message\": \"User Does not Exist\"}");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
