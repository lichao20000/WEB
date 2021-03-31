/**
 * 
 */
package com.linkage.module.gwms.dao.gw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-7-14
 * @category com.linkage.module.gwms.dao.gw
 * 
 */
public class LocalServParamDAO  extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(LocalServParamDAO.class);
	
	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String[]
	 */
	public String[] getServPvc(String servType) 
	{
		logger.debug("getServPvc({})",servType);
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select param_name, param_value from local_serv_param ");
		strSQL.append("where param_filed='servparamcheck' and param_name='");
		strSQL.append(servType);
		strSQL.append("'");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
    	psql.getSQL();
		List rList = jt.queryForList(strSQL.toString());
		String[] arryStr = null;
		if (null != rList && rList.size() > 0) {
			
			int lsize = rList.size();
			arryStr = new String[lsize];
			for (int i = 0; i < lsize; i++) {
				Map rMap = (Map) rList.get(i);
				if (null != rMap && false == rMap.isEmpty()) {
					arryStr[i] = String.valueOf(rMap.get("param_value"));
				}
			}
		}
		return arryStr;
	}
	
	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String[]
	 */
	public String[] getServPvc(String servType[]) 
	{
		logger.debug("getServPvc({})",servType);
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select param_name, param_value from local_serv_param ");
		strSQL.append("where param_filed='servparamcheck' and param_name in(");
		if(null==servType){
			logger.warn("getServPvc传入的servType为null！");
			return null;
		}
		boolean flag = false;
		for(int i=0;i<servType.length;i++){
			if(flag){
				strSQL.append(",");
			}
			strSQL.append("'");
			strSQL.append(servType[i]);
			strSQL.append("'");
			flag = true;
		}
		strSQL.append(")");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
    	psql.getSQL();
		List rList = jt.queryForList(strSQL.toString());
		String[] arryStr = null;
		if (null != rList && rList.size() > 0) {
			
			int lsize = rList.size();
			arryStr = new String[lsize];
			for (int i = 0; i < lsize; i++) {
				Map rMap = (Map) rList.get(i);
				if (null != rMap && false == rMap.isEmpty()) {
					arryStr[i] = String.valueOf(rMap.get("param_value"));
				}
			}
		}
		return arryStr;
	}
	
	/**
	 * 获得VOIP必需的参数
	 * @author gongsj
	 * @date 2009-8-12
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVoipInitParam(String deviceId) 
	{
		logger.debug("getVoipInitParam({})",deviceId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select prox_server,prox_port,prox_server2,prox_port2,regi_serv");
			psql.append("regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy,");
			psql.append("out_bound_port,stand_out_bound_proxy,stand_out_bound_port ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_voip_init_param where device_id=? ");
		psql.setString(1,deviceId);
		List<Map<String, String>> rList = jt.queryForList(psql.getSQL());
		
		Map<String, String> voipInitParamMap = new HashMap<String, String>();
		
		if (null != rList && !rList.isEmpty()) {
			
			voipInitParamMap = rList.get(0);
		}
		
		return voipInitParamMap;
	}

}

