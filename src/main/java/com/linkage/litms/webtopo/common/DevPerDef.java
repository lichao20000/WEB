package com.linkage.litms.webtopo.common;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;





public class DevPerDef {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DevPerDef.class);
	private String expressionid = null;

	private String device_id = null;

	private String str_interval = null;
	
	private int interval = 0;
	
	private String pm_Name = null;
	
	private String mindesc = null;
	
	private String maxdesc = null;
	
	private String dynadesc = null;
	
	private String mutationdesc = null;
	
	private String str_dynatype = null;
	
	private int dynatype = 0;
	
	private String str_dynacount = null;
	
	private int dynacount = 0;
	
	private String str_beforeday = null;
	
	private int beforeday = 0;
	
	private String str_dynawarninglevel = null;
	
	private int dynawarninglevel = 0;
	
	private String str_dynareinstatelevel = null;
	
	private int dynareinstatelevel = 0;
	
	private String str_maxtype = null;
	
	private int maxtype = 0;
	
	private String str_maxcount = null;
	
	private int maxcount = 0;
	
	private String str_maxwarninglevel = null;
	
	private int maxwarninglevel = 0;
	
	private String str_maxreinstatelevel = null;
	
	private int maxreinstatelevel = 0;
	
	private String str_mintype = null;
	
	private int mintype = 0;
	
	private String str_mincount = null;
	
	private int mincount = 0;
	
	private String str_minwarninglevel = null;
	
	private int minwarninglevel = 0;
	
	private String str_minreinstatelevel = null;
	
	private int minreinstatelevel = 0;
	
	private String str_mutationtype = null;
	
	private int mutationtype = 0;
	
	private String str_mutationcount = null;
	
	private int mutationcount = 0;
	
	private String str_mutationwarninglevel = null;
	
	private int mutationwarninglevel = 0;
	
	private int mutationreinstatelevel = 0;
	
	private String str_intodb = null;
	
	private int intodb = 0;
	
	private String str_maxthres = null;
	
	private float maxthres = 0;
	
	private String str_minthres = null;
	
	private float minthres = 0;
	
	private String str_dynathres = null;
	
	private float dynathres = 0;
	
	private String str_mutationthres = null;
	
	private float mutationthres = 0;
	
	private String mySQL = null;

	private Cursor cursor = null;

	private Map feilds = null;

	private PrepareSQL pSQL = null;
	
	private HttpServletRequest request=null;

	public DevPerDef(HttpServletRequest request) {
		this.request=request;
		expressionid = request.getParameter("exp_name");
		device_id = request.getParameter("device_id");
		str_interval = request.getParameter("samp_distance");
		logger.debug("str_interval:"+str_interval);
		interval = Integer.parseInt(str_interval);
		str_intodb = request.getParameter("ruku");
		intodb = Integer.parseInt(str_intodb);
		pm_Name = request.getParameter("expression_Name");
		mindesc = request.getParameter("fixedness_value1desc");
		maxdesc = request.getParameter("fixedness_value2desc");
		dynadesc = request.getParameter("dynamic_Valve_desc");
		mutationdesc = request.getParameter("mutation_Valve_desc");
		
		str_mintype = request.getParameter("compSign_1");
		mintype = Integer.parseInt(str_mintype);
		str_mincount = request.getParameter("seriesOverstep_value1");
		mincount = Integer.parseInt(str_mincount);
		str_minwarninglevel = request.getParameter("send_warn1");
		minwarninglevel = Integer.parseInt(str_minwarninglevel);
		str_minreinstatelevel = request.getParameter("renew_warn1");
		minreinstatelevel = Integer.parseInt(str_minreinstatelevel);
		
		str_maxtype = request.getParameter("compSign_2");
		maxtype = Integer.parseInt(str_maxtype);
		str_maxcount = request.getParameter("seriesOverstep_value2");
		maxcount = Integer.parseInt(str_maxcount);
		str_maxwarninglevel=request.getParameter("send_warn2");
		maxwarninglevel = Integer.parseInt(str_maxwarninglevel);
		str_maxreinstatelevel = request.getParameter("renew_warn2");
		maxreinstatelevel = Integer.parseInt(str_maxreinstatelevel);
		
		str_dynatype = request.getParameter("dynamic_OperateSign");
		dynatype = Integer.parseInt(str_dynatype);
		str_dynacount = request.getParameter("achieve_Percent2");
		dynacount = Integer.parseInt(str_dynacount);
		str_beforeday = request.getParameter("benchmark_Value");
		beforeday = Integer.parseInt(str_beforeday);
		str_dynawarninglevel = request.getParameter("sdynamic_send_warn");
		dynawarninglevel = Integer.parseInt(str_dynawarninglevel);
		str_dynareinstatelevel = request.getParameter("sdynamic_renew_warn");
		dynareinstatelevel = Integer.parseInt(str_dynareinstatelevel);
		
		str_mutationtype = request.getParameter("mutation_OperateSign");
		mutationtype = Integer.parseInt(str_mutationtype);
		str_mutationcount = request.getParameter("achieve_Percent3");
		mutationcount = Integer.parseInt(str_mutationcount);
		str_mutationwarninglevel = request.getParameter("send_warn3");
		mutationwarninglevel = Integer.parseInt(str_mutationwarninglevel);
		
		str_minthres = request.getParameter("fixedness_value1");
		minthres = Float.parseFloat(str_minthres);
		str_maxthres = request.getParameter("fixedness_value2");
		maxthres = Float.parseFloat(str_maxthres);
		str_dynathres = request.getParameter("valve_Percent");
		dynathres = Float.parseFloat(str_dynathres);
		str_mutationthres = request.getParameter("overstep_Percent");
		mutationthres = Float.parseFloat(str_mutationthres);
	}

	public int actionPerformedOne() {
		String[] sqlStrs = new String[2];
		
		String sqlDel = "delete from pm_map_instance where expressionid=? and device_id=?";
		pSQL = new PrepareSQL(sqlDel);
		pSQL.setStringExt(1,expressionid,false);
		pSQL.setStringExt(2,device_id,true);
		sqlStrs[0] = pSQL.getSQL();
		logger.debug("pm_map_instanceSQL:"+sqlStrs[0]);
		
		mySQL = "select count(1) as num from pm_map where expressionid=? and device_id=?";
		pSQL = new PrepareSQL(mySQL);
		pSQL.setStringExt(1, expressionid, false);
		pSQL.setStringExt(2, device_id, true);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("pm_mapSQL:" + pSQL.getSQL());
		feilds = cursor.getNext();
		String sqlAlter = null;
		
		if (feilds != null) {
			int num = Integer.parseInt((String) feilds.get("num"));
			logger.debug("wp_pm_map+num:"+num);
			if (num > 0) {
				sqlAlter = "update pm_map set isok=-1, interval=?,remark=null where expressionid=? and device_id=?";
				pSQL = new PrepareSQL(sqlAlter);
				pSQL.setStringExt(1,str_interval,false);
				pSQL.setStringExt(2,expressionid,false);
				pSQL.setStringExt(3,device_id,true);
			} else {
				sqlAlter = "insert into pm_map(expressionid,device_id,interval,isok) values(? ,?,?,-1)";
				pSQL = new PrepareSQL(sqlAlter);
				pSQL.setStringExt(1,expressionid,false);
				pSQL.setStringExt(2,device_id,true);
				pSQL.setStringExt(3,str_interval,false);
			}
			sqlStrs[1] = pSQL.getSQL();
			logger.debug("sqlAlter:" + pSQL.getSQL());
		}
		int[] retflag = DataSetBean.doBatch(sqlStrs);
		
		return retflag!=null?0:-1; 
	}
	
	public void actionPerformedTwo() {
		logger.debug("begin actionPerformedTwo!");
		PM_instance pm = new PM_instance();
		pm.setName(pm_Name, expressionid);
		pm.setDesc(mindesc, maxdesc, dynadesc, mutationdesc);
		pm.setDyna(dynatype, dynacount, beforeday, dynawarninglevel,dynareinstatelevel);
		pm.setMax(maxtype, maxcount, maxwarninglevel, maxreinstatelevel);
		pm.setMin(mintype, mincount, minwarninglevel, minreinstatelevel);
		pm.setMutation(mutationtype, mutationcount, mutationwarninglevel,mutationreinstatelevel);
		pm.setPerf(interval, intodb);
		pm.setThres(maxthres, minthres, dynathres, mutationthres);
		ArrayList list = new ArrayList();
		list.add(device_id);
		pm.setDevList(list);
		
		logger.debug("begin PefDef_MasterTread");
		PefDef_MasterTread mt = new PefDef_MasterTread(2);		
		mt.setPM(pm);		
		HttpSession session=request.getSession();			
		UserRes curUser = (UserRes) session.getAttribute("curUser");		
		mt.setAccountInfo(curUser.getUser().getAccount(),curUser.getUser().getPasswd());		
		String srcType = request.getParameter("type");
		logger.debug("action6_srcType:"+srcType);
		if(null!=srcType&&!"".equals(srcType))
		{			
			int type = Integer.parseInt(srcType);			
			if(2==type)
			{				
				mt.setType(type);				
			}
			
		}		
		mt.start();		
		logger.debug("end actionPerformedTwo!");
		
	}

	public boolean is_Pmbeing() {
		boolean b = true;
		mySQL = "select count(1) as num from pm_map_instance where expressionid=? and device_id=?";

		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(mySQL);
		pSQL.setStringExt(1, expressionid, false);
		pSQL.setStringExt(2, device_id, true);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		logger.debug("pm_instance:" + pSQL.getSQL());

		feilds = cursor.getNext();
		

		if (feilds != null) {
			int num = Integer.parseInt((String) feilds.get("num"));

			if (num > 0)
				b = true;
			else
				b = false;
		}
		cursor.Reset();
		feilds.clear();
		return b;
	}
}
