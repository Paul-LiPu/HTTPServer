package me.lipu.webapp;

import me.lipu.core.Request;
import me.lipu.core.Response;
import me.lipu.core.Servlet;

public class LoginServlet extends Servlet {
	@Override
	public void doGet(Request req,Response rep) throws Exception {
		String name = req.getParameter("uname");
		String pwd =req.getParameter("pwd");
		if(login(name,pwd)){
			rep.println("��¼�ɹ�");
		}else{
			rep.println("��¼ʧ��");
		}
	}
	
	public boolean login(String name,String pwd){
		return name.equals("test") && pwd.equals("test");
	}
	
	
	@Override
	public void doPost(Request req,Response rep) throws Exception {
		
	}

}
