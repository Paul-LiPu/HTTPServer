package me.lipu.core;

import java.util.ArrayList;

public class FilterChain {
	private int n = 0;
	private ArrayList<Filter> filters;
	private Servlet servlet;
	
	public FilterChain(ArrayList<Filter> filters, Servlet servlet) {
		this.filters = filters;
		this.servlet = servlet;
	}
	public void doFilter(Request req, Response res) throws Exception {
		if (n < filters.size()) {
			Filter filter = filters.get(n);
			n++;
			filter.doFilter(req, res, this);
		} else {
			servlet.service(req, res);
		}
	}
	
	public Servlet getServlet() {
		return servlet;
	}
}
