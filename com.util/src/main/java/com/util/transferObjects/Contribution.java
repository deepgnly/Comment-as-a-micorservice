package com.util.transferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contribution {
	@JsonProperty("componentId")
	private String componentId;
	@JsonProperty("serviceName")
	private String serviceName;
	@JsonProperty("componentOrder")
	private int componentOrder;
	
	@JsonProperty("tabName")
	private String tabName;
	
	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTabResource() {
		return tabResource;
	}

	public void setTabResource(String tabResource) {
		this.tabResource = tabResource;
	}

	@JsonProperty("tabResource")
	private String tabResource;

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getComponentOrder() {
		return componentOrder;
	}

	public void setComponentOrder(int componentOrder) {
		this.componentOrder = componentOrder;
	}

}
