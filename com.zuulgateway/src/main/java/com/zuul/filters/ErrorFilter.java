package com.zuul.filters;

import com.netflix.zuul.ZuulFilter;

public class ErrorFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	
	public Object run() {
		return null;
	}

	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return false;
	}

}
