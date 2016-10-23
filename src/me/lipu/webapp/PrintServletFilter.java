package me.lipu.webapp;

import me.lipu.core.Filter;
import me.lipu.core.FilterChain;
import me.lipu.core.Request;
import me.lipu.core.Response;

public class PrintServletFilter implements Filter{

	@Override
	public void doFilter(Request req, Response res, FilterChain chain) throws Exception {
		System.out.println(chain.getServlet().getClass().getName() + " is going to serve");
		chain.doFilter(req, res);
		System.out.println(chain.getServlet().getClass().getName() + " has served");
	}

}
