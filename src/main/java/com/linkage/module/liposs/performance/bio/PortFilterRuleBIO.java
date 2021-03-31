package com.linkage.module.liposs.performance.bio;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.liposs.performance.dao.PortFilterRuleDAO;

/**
 * 定义端口过滤规则的业务逻辑类
 *
 * @author 张松（3704）
 * @version:1.0
 * @since:2008-09-05 10:50
 * @catage:performance
 */
public class PortFilterRuleBIO {

	PortFilterRuleDAO portFilterRuleDao;
	/**
	 * 获得最大页数
	 * @param size
	 * 		记录数
	 * @param maxPage_splitPage
	 * 		最大分页数
	 * @param num_splitPage
	 * 		每页记录数
	 * @return
	 */
	public int getPageNumber(int size,int maxPage_splitPage,int  num_splitPage){
//		int size = portFilterRuleDao.getFilterNumber();
		if( size%num_splitPage!=0){
			maxPage_splitPage = size/num_splitPage+1;
		}else{
			maxPage_splitPage = size/num_splitPage;
		}
		return maxPage_splitPage;
	}

	/**
	 * 保存过滤规则
	 * @param deviceModel
	 * 			设备型号
	 * @param filterType
	 * 			过滤类型
	 * @param filterValue
	 * 			过滤值
	 * @return	字符串
	 */
	public String saveFilterRule(String deviceModel, int filterType,String filterValue){
		List list = portFilterRuleDao.getFilterRule(deviceModel, filterType, filterValue);
		if(list!=null&&!list.isEmpty()){
			return "已经存在相同的过滤规则！";
		}else{
			int res = portFilterRuleDao.saveFilterRule(deviceModel, filterType, filterValue);
			if(res>0){
				return "添加规则成功！";
			}else{
				return "添加规则失败！";
			}
		}

	}

	/**
	 * 删除规则
	 * @param delDeviceModel
	 * 			设备型号
	 * @param filterType
	 * 			过滤类型
	 * @return 字符串
	 */
	public String delFilterRule(String delDeviceModel, int filterType){
		int res = portFilterRuleDao.delFilterRule(delDeviceModel, filterType);
		if(res>0){
			return "删除规则成功！";
		}else{
			return "删除规则失败！";
		}
	}

	/**
	 * 获得记录数
	 * @return  int
	 */
	public int getFilterNumber(){
		return portFilterRuleDao.getFilterNumber();
	}

	/**
	 * 获得所有设备厂商
	 * @return List 存放Map
	 */
	public List getAllCompany(){
		return portFilterRuleDao.getAllCompany();
	}

	/**
	 * 获得分页数据
	 * @param curPage_splitPage
	 * 			目前页码
	 * @param num_splitPage
	 * 			每页记录数
	 * @return List 存放map
	 */
	public List getAllFilter(int curPage_splitPage, int num_splitPage){
		return portFilterRuleDao.getAllFilter(curPage_splitPage, num_splitPage);
	}

	/**
	 * 根据设备厂商获得设备型号
	 * @param companyName
	 * 			设备厂商
	 * @return List 存放Map
	 */
	public List getAllDeviceModelByCompany(int companyId){
		return portFilterRuleDao.getAllDeviceModelByCompany(companyId);
	}

	public List searchFilterRules(String deviceModel, String filterType){
		String sql = "select b.device_model, a.device_model as dm, b.device_model_id, a.type, a.value, a.blank " +
				"from tab_model_port_filter a,gw_device_model b " +
				"where a.device_model=b.device_model_id and a.device_model='"+deviceModel+"'";
		if(!"None".equals(filterType)){
			sql = sql+" and a.type="+Integer.parseInt(filterType);
		}
		PrepareSQL psql = new PrepareSQL(sql);
        List<Map> filterRuleBySqlList = portFilterRuleDao.getFilterRuleBySql(psql.getSQL());
        for (Map map : filterRuleBySqlList) {
            String device_model = StringUtil.getStringValue(map,"device_model");
            String dm = StringUtil.getStringValue(map,"dm");
            map.put("device_model", device_model + "(" + dm + ")");
        }
        return filterRuleBySqlList;
	}

	public PortFilterRuleDAO getPortFilterRuleDao() {
		return portFilterRuleDao;
	}
	public void setPortFilterRuleDao(PortFilterRuleDAO portFilterRuleDao) {
		this.portFilterRuleDao = portFilterRuleDao;
	}
}
