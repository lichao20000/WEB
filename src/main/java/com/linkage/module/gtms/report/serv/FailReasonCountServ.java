package com.linkage.module.gtms.report.serv;

import java.util.List;
import java.util.Map;

public interface FailReasonCountServ {

	List<Map<String,String>> failReasonCount(String cityId, String starttime, String endtime);


}
