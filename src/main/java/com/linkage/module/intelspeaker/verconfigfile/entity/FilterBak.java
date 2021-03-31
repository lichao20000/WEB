package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;

public class FilterBak {
	/**
	 * 本路由表所包含的目标地址，支持单个IP、采用半角连线符“-”连接的某IP地址范围，多个记录间采用半角都好“,”进行分割，且无空格连接；
	 */
	private String Destination;

	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public FilterBak(){
		
	}
	public FilterBak(String childText) {
		this.Destination=childText;
	}
	@Override
	public String toString() {
		return "Filter [Destination=" + Destination + "]";
	}

}
