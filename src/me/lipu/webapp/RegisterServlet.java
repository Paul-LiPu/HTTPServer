package me.lipu.webapp;

import me.lipu.core.Request;
import me.lipu.core.Response;
import me.lipu.core.Servlet;

public class RegisterServlet extends Servlet{
	@Override
	public void doGet(Request req,Response rep) throws Exception {
		rep.println("<html><head><title>����ע��</title>");
		rep.println("</head><body>");
		rep.println("����û���Ϊ:"+req.getParameter("uname"));
		rep.println("</body></html>");
	}
	
	@Override
	public void doPost(Request req,Response rep) throws Exception {
		rep.println("<html><head><title>����ע��</title>");
		rep.println("</head><body>");
		rep.println("����û���Ϊ:"+req.getParameter("uname"));
		rep.println("</body></html>");
	}

}
