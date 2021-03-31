package com.linkage.module.gtms.stb.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;

/**
 * 湖南联通机顶盒按EPG或APK版本统计
 */
public class StbCountReportBioServ 
{
	private static Logger logger = LoggerFactory.getLogger(StbCountReportBioServ.class);
	/**存储统计数据结果文件*/
	private static final String RESULTFILE=LipossGlobals.getLipossHome()
											+ "/WEB-INF/classes/countResultFile/countResult.txt";
	
	
	/**
	 * 拼装页面table
	 */
	public String toConversion(String type,List<Map<String,String>> data,String city_id,
			List<HashMap<String,String>> cityList)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<input type='hidden' name=\"queryTime\" value=\""+listToString(data,type)+"\" />");
		sb.append("<table class=\"listtable\">");
		sb.append("<caption>统计结果</caption>");
		sb.append("<thead><tr>");
		if("apk".equals(type)){
			sb.append("<th>APK版本</th>");
		}else if("epg".equals(type)){
			sb.append("<th>EPG版本</th>");
		}else if("category".equals(type)){
			sb.append("<th>类别</th>");
		}
		
		for(Map<String,String> cm:cityList){
			sb.append("<th>"+cm.get("city_name")+"</th>");
		}
		
		sb.append("<th>小计</th>");
		sb.append("</tr></thead>");
		
		sb.append("<tbody>");
		if(data==null || data.isEmpty()){
			sb.append("<tr><td colspan=\""+(cityList.size()+2)+"\">未统计到设备数据</td></tr>");
		}
		else
		{
			for(Map<String, String> map:data)
			{
				String version=map.get("version");
				String category = map.get("category");
				if(StringUtil.IsEmpty(version))
				{
					version = category;
				}
				sb.append("<tr>");
				sb.append("<td>"+version+"</td>");
				
				for(Map<String, String> mapCity:cityList)
				{
					for(String key:map.keySet())
					{
						if(!"version".equals(key) && !"category".equals(key) && !"total_num".equals(key)
								&& key.equals(mapCity.get("city_id")))
						{
							if("0".equals(map.get(key))){
								sb.append("<td>0</td>");
							}
							else if(city_id.equals(key)){
								if("小计".equals(version)){
									sb.append("<td><b><a href=\"javascript:openDev('"+key+"','"+version+"','0');\">");
									sb.append(map.get(key)+"</a></b></td>");
								}else{
									sb.append("<td><a href=\"javascript:openDev('"+key+"','"+version+"','0');\">");
									sb.append(map.get(key)+"</a></td>");
								}
							}
							else{
								if("小计".equals(version)){
									sb.append("<td><b><a href=\"javascript:openDev('"+key+"','"+version+"','1');\">");
									sb.append(map.get(key)+"</a></b></td>");
								}else{
									sb.append("<td><a href=\"javascript:openDev('"+key+"','"+version+"','1');\">");
									sb.append(map.get(key)+"</a></td>");
								}
							}
						}
					}
				}
				
				if("0".equals(map.get("total_num"))){
					sb.append("<td>0</td>");
				}else{
					sb.append("<td><b><a href=\"javascript:openDev('"+city_id+"','"+version+"','1');\">"+map.get("total_num")+"</a></b></td>");
				}
				
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			
			sb.append("<tfoot><tr><td colspan=\""+(cityList.size()+2)+"\">");
			sb.append("<img src='../../../images/excel.gif' border='0' alt='导出列表' ");
			sb.append("style='cursor: hand' onclick=\"ToExcel('"+city_id+"')\">");
			sb.append("</td></tr></tfoot>");
		}
		
		sb.append("<tr STYLE=\"display: none\">");
		sb.append("<td colspan=\""+(cityList.size()+2)+"\">");
		sb.append("<iframe id=\"childFrm\" src=\"\"></iframe></td>");
		sb.append("</tr></table>");
		
		logger.debug("resultTable:"+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 拼装页面table 拼接厂商用
	 */
	public String toConversion4Vendor(String type,List<Map<String,String>> data,String vendor_id,List<HashMap<String,String>> vendorList)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<input type='hidden' name=\"queryTime\" value=\""+listToString(data,type)+"\" />");
		sb.append("<table class=\"listtable\">");
		sb.append("<caption>统计结果</caption>");
		sb.append("<thead><tr>");
		if("apk".equals(type)){
			sb.append("<th>APK版本</th>");
		}else if("epg".equals(type)){
			sb.append("<th>EPG版本</th>");
		}else if("category".equals(type)){
			sb.append("<th>类别</th>");
		}
		
		for(Map<String,String> cm:vendorList){
			sb.append("<th>"+cm.get("vendor_add")+"</th>");
		}
		
		sb.append("<th>小计</th>");
		sb.append("</tr></thead>");
		
		sb.append("<tbody>");
		if(data==null || data.isEmpty()){
			sb.append("<tr><td colspan=\""+(vendorList.size()+2)+"\">未统计到设备数据</td></tr>");
		}
		else
		{
			for(Map<String, String> map:data)
			{
				String version=map.get("version");
				String category = map.get("category");
				if(StringUtil.IsEmpty(version))
				{
					version = category;
				}
				sb.append("<tr>");
				sb.append("<td>"+version+"</td>");
				
				for(Map<String, String> mapVendor:vendorList)
				{
					for(String key:map.keySet())
					{
						if(!"version".equals(key) && !"category".equals(key) && !"total_num".equals(key)
								&& key.equals(mapVendor.get("vendor_id")))
						{
							if("0".equals(map.get(key))){
								sb.append("<td>0</td>");
							}
							else if(vendor_id.equals(key)){
								if("小计".equals(version)){
									sb.append("<td><b><a href=\"javascript:openDev('"+key+"','"+version+"','0');\">");
									sb.append(map.get(key)+"</a></b></td>");
								}else{
									sb.append("<td><a href=\"javascript:openDev('"+key+"','"+version+"','0');\">");
									sb.append(map.get(key)+"</a></td>");
								}
							}
							else{
								if("小计".equals(version)){
									sb.append("<td><b><a href=\"javascript:openDev('"+key+"','"+version+"','1');\">");
									sb.append(map.get(key)+"</a></b></td>");
								}else{
									sb.append("<td><a href=\"javascript:openDev('"+key+"','"+version+"','1');\">");
									sb.append(map.get(key)+"</a></td>");
								}
							}
						}
					}
				}
				
				if("0".equals(map.get("total_num"))){
					sb.append("<td>0</td>");
				}else{
					sb.append("<td><b><a href=\"javascript:openDev('"+vendor_id+"','"+version+"','1');\">"+map.get("total_num")+"</a></b></td>");
				}
				
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			
			sb.append("<tfoot><tr><td colspan=\""+(vendorList.size()+2)+"\">");
			sb.append("<img src='../../../images/excel.gif' border='0' alt='导出列表' ");
			sb.append("style='cursor: hand' onclick=\"ToExcel('"+vendor_id+"')\">");
			sb.append("</td></tr></tfoot>");
		}
		
		sb.append("<tr STYLE=\"display: none\">");
		sb.append("<td colspan=\""+(vendorList.size()+2)+"\">");
		sb.append("<iframe id=\"childFrm\" src=\"\"></iframe></td>");
		sb.append("</tr></table>");
		
		logger.debug("resultTable:"+sb.toString());
		return sb.toString();
	}

	/**
	 * 结果数据写入文件，返回时间戳
	 */
	private String listToString(List<Map<String, String>> data,String dataType) 
	{
		if(data==null || data.isEmpty()){
			return null;
		}
	
		StringBuffer sb=new StringBuffer();
		for(Map<String, String> map:data)
		{
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) 
			{
				String key = iterator.next();
				sb.append(key).append("#X#").append(map.get(key)).append("#Y#");
			}
			sb.append("#AB#");
		}
		
		logger.debug("filePath:"+RESULTFILE);
		long time=System.currentTimeMillis();

		FileWriter fw = null;
        try 
        {
            fw = new FileWriter(RESULTFILE,true);
            fw.write(dataType+"-"+time+":"+sb.substring(0,sb.toString().length()-4)+"\n");
            fw.close();
        } catch (IOException e) {
        	logger.error("结果数据存入文件[{}-{}]失败！err:"+e,RESULTFILE,time);
        	return null;
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
            	logger.error("IO流关闭失败,err:"+e);
            }
        }
		
		return StringUtil.getStringValue(time);
	}
	
	/**
	 * 将String转成List
	 */
	public List<Map<String, String>> stringToList(String time,String dataType) 
	{
		logger.debug("stringToList({},{})",time,dataType);
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		if(StringUtil.IsEmpty(time)){
			return list;
		}
		String index=dataType+"-"+time+":";
		
		String dataString=null;
		BufferedReader br=null;
		try{
			File file=new File(RESULTFILE);
	         if(!file.exists()||file.isDirectory()){
	        	return null; 
	         }
	            
	         br=new BufferedReader(new FileReader(file));
	         String temp=null;
	         while((temp=br.readLine())!=null)
	         {
	        	 if(temp.indexOf(index)>-1){
	        		 dataString=temp.substring(index.length()).trim();
	        		 break;
	        	 }
	         }
		}catch(IOException e){
			logger.error("读取数据文件[{}-{}]失败，err:"+e,RESULTFILE,time);
			return list;
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(StringUtil.IsEmpty(dataString)){
			logger.warn("数据文件[{}-{}]无数据",RESULTFILE,time);
			return list;
		}
		
		String[] s1=dataString.split("#AB#");
		for(String sm:s1)
		{
			String[] sm1=sm.split("#Y#");
			Map<String,String> map=new HashMap<String,String>();
			for(String m:sm1){
				if(m.split("#X#").length==1){
					map.put(m.split("#X#")[0],"");
				}else{
					map.put(m.split("#X#")[0], m.split("#X#")[1]);
				}
			}
			
			list.add(map);
		}
		
		return list;
	}
	
}
