package me.lipu.core;

public abstract class Servlet {
	public void service(Request req,Response rep) throws Exception{
		if (req.getMethod().equalsIgnoreCase("GET")){
			this.doGet(req,rep);
		}
		if (req.getMethod().equalsIgnoreCase("POST")){
			this.doPost(req,rep);
		}
	}
	
	public abstract void doGet(Request req,Response rep) throws Exception;
	public abstract void doPost(Request req,Response rep) throws Exception;
}
