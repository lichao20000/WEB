
package com.linkage.module.itms.resource.bio;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.ids.util.WSClientUtil;
import com.linkage.module.itms.resource.dao.FileServerManageDAO;

public class FileServerManageBIO
{

	private FileServerManageDAO  dao;
	

	public List<Map<String, String>> queryServer()
	{
		List<Map<String, String>> list = dao.queryServer();
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("root");
		
		for (int i = 0; i < list.size(); i++) {
			Element paramElem = root.addElement("param");
			Map<String, String> map =list.get(i);
			for (Map.Entry<String, String> m : map.entrySet()) {
				if (!"username".equals(m.getKey().toLowerCase()) && !"password".equals(m.getKey().toLowerCase())) {
					paramElem.addElement(m.getKey().toLowerCase()).addText(String.valueOf(m.getValue()));
				}
			}
		}
		
		String asXML = document.asXML();
		//调接口
		String methodName = "fileServerPing_hb";
		String url="http://172.30.16.21:9268/NorthInterface/services/CallService";
		//回参
		String callBack =  WSClientUtil.callItmsService(url, asXML, methodName);
		
		List<Map<String, String>> backList=new ArrayList<Map<String, String>>();
		SAXReader reader = new SAXReader();
		Document document1 = null;
		try {
			document1 = reader.read(new StringReader(callBack));
			Element root1 = document1.getRootElement();
			List<Element> param = root1.elements("param");
			for (int i = 0; i < param.size(); i++) {
				Map<String, String> map=new HashMap<String, String>();
				String id = StringUtil.getStringValue(param.get(i).elementTextTrim("id"));
				String server_name = StringUtil.getStringValue(param.get(i).elementTextTrim("server_name"));
				String host = StringUtil.getStringValue(param.get(i).elementTextTrim("host"));
				String fileserverport = StringUtil.getStringValue(param.get(i).elementTextTrim("fileserverport"));
				String isOnline = StringUtil.getStringValue(param.get(i).elementTextTrim("isonline"));
				map.put("id", id);
				map.put("server_name", server_name);
				map.put("host", host);
				map.put("fileserverport", fileserverport);
				map.put("isonline", isOnline);
				backList.add(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return backList;
	}

	public List<Map> queryServer(String id)
	{
		 List list = dao.queryServer(id);
		 return list;
	}

	public FileServerManageDAO getDao() {
		return dao;
	}

	public void setDao(FileServerManageDAO dao) {
		this.dao = dao;
	}

	
}
