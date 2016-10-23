package me.lipu.core;

public interface Filter {
	public void doFilter (Request req, Response res, FilterChain chain) throws Exception;
}
