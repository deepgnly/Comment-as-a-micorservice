package com.zuul.filters;

import com.netflix.zuul.ZuulFilter;

public class PostFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	
	public boolean shouldFilter() {
		return true;
	}

	
	public Object run() {
		return null;
	}

}
