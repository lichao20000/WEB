/**
 * 
 */

package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.ServTemplateDAO;
import com.linkage.module.itms.resource.obj.TreeNode;

/**
 * 模板管理BIO
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2020-3-11
 * @category com.linkage.module.itms.resource.bio
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class ServTemplateBIO {

	private ServTemplateDAO dao;
	private int maxPage_splitPage;

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	
	public List<Map> queryDeviceList(String name, String vlan, String serv, int curPage_splitPage, int num_splitPage, String cm) {
		List<Map> list = dao.queryTemplateList(name, vlan, serv, curPage_splitPage, num_splitPage, cm);
		maxPage_splitPage = dao.queryTemplateListCount(name, vlan, serv, curPage_splitPage, num_splitPage, cm);
		return list;
	}
	
	/**
	 * 获取id$name#id2$name2的拼接字符串
	 * @param name
	 * @param vlan
	 * @param serv
	 * @param cm 
	 * @return
	 */
	public String queryTemplateStr(String name, String vlan, String serv, String cm) {
		List<HashMap<String, String>> list = dao.queryTemplateList(name, vlan, serv, cm);
		String rtn = "";
		if(null==list || list.size()==0){
			return "";
		}
		for(int i=0;i<list.size();i++){
			rtn = rtn + list.get(i).get("id") + "$" + list.get(i).get("name") + "#";
		}
		return rtn.substring(0, rtn.length()-1);
	}
	
	public Map<String, String> queryTemplate(int id){
		return dao.queryTemplate(id);
	}
	
	public ArrayList<HashMap<String, String>> queryTemplateParam(int id){
		ArrayList<HashMap<String, String>> list = dao.queryTemplateParam(id);
		if(null != list && list.size()!=0){
			for(int i=0;i<list.size();i++){
				String type = StringUtil.getStringValue(list.get(i), "type");
				list.get(i).put("typeName", getTypeName(type));
			}
		}
		
		return list;
	}
	
	String getTypeName(String type){
		if("1".equals(type)){
			return "string";
		}
		else if("2".equals(type)){
			return "int";
		}
		else if("3".equals(type)){
			return "unsignedInt";
		}
		else if("4".equals(type)){
			return "boolean";
		}
		else{
			return "other";
		}
	}
	
	
	public int saveTemplate(List<TreeNode> templateParams, int id, String serv, String vlan, String describe, String name, String nserv_svlan_del, String sserv_del, String sserv_svlan_del, String sport_del, String service_id){
		return dao.saveTemplate(templateParams, id, serv, vlan, describe, name, nserv_svlan_del, sserv_del, sserv_svlan_del, sport_del, service_id);
	}
	
	public int addTemplate(List<TreeNode> templateParams, int id, String serv, String vlan, String describe, String name, String nserv_svlan_del, String sserv_del, String sserv_svlan_del, String sport_del, String service_id){
		return dao.addTemplate(templateParams, id, serv, vlan, describe, name, nserv_svlan_del, sserv_del, sserv_svlan_del, sport_del, service_id);
	}
	
	public int getMaxId(){
		return dao.getMaxId();
	}


	/**
	 * 根据id删除记录
	 * 
	 * @param id
	 */
	public void deleteDevice(int id) {
		dao.deleteDevice(id);
	}


	/** getters and setters **/
	public ServTemplateDAO getDao() {
		return dao;
	}

	public void setDao(ServTemplateDAO dao) {
		this.dao = dao;
	}
}
