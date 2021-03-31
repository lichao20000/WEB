package com.linkage.module.gtms.resource.dao;

import java.util.HashMap;
import java.util.List;


public interface QueryVoipPhoneByLoidDAO {
	
	public List<HashMap<String, String>> queryVoipPhoneByLoid(String loidStr);
	
}
