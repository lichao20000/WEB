package com.linkage.module.liposs.performance.dao.module;

public class ModuleManagerRelateFactory {
	public static I_ModuleManageRelateTab factory(int configtype){
		switch(configtype){
			case 2:
				return new ModuleManageRelatePmee();
			default:
				return new ModuleManageRelateException();
		}
	}
}
