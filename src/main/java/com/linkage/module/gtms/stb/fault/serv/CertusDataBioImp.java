package com.linkage.module.gtms.stb.fault.serv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.linkage.system.utils.StringUtils;

public class CertusDataBioImp implements CertusDataBio
{
	private static final Logger LOG = Logger.getLogger(CertusDataBioImp.class);

	/**
	 * <pre>
	 * 请求机顶盒信息
	 *  说明：该方法与requestSTB方法实现相同，该方法虽传入mac地址参数，却未使用。参见requestSTB方法
	 * </pre>
	 * @param serviceUser
	 * @param sn
	 * @param mac
	 * @param cityId
	 * @return
	 * @see #requestSTB
	 */
	public Map<String,String> requestSTBInfo(String serviceUser,String sn,String mac,String cityId)
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		
        Map<String,String> data = new HashMap<String,String>();
        // 参数判断
        if(serviceUser==null || "".equals(serviceUser)
        	|| sn==null || "".equals(sn) || "-1".equals(sn)
        	|| cityId==null || "".equals(cityId))
        {
        	data.put("errcode", "-10");
        	String error = "请求参数格式不正确（缺少";
        	if(serviceUser==null || "".equals(serviceUser))
        	{
        		error += "用户账号、";
        	}
        	if(sn==null || "".equals(sn) || "-1".equals(sn))
        	{
        		error += "SN、";
        	}
        	if(cityId==null || "".equals(cityId))
        	{
        		error += "属地、";
        	}
        	
        	data.put("valuation", error.substring(0, error.length()-1)+"信息）");
        	return data;
        }

        BufferedReader reader=null;
        try 
        {
        	JSONObject js = new JSONObject();
        	// 参数
    		Map<String,String> param = new HashMap<String,String>();
    		param.put("serviceUser", serviceUser);
    		param.put("sn", sn);
    		// 属地编号与区号的对应关系
			param.put("cityId", ResTool.getCityExtMap().get(cityId).get("area_id"));
    			
    		JSONObject json = new JSONObject(param);
        	js.put("event", 31);
        	js.put("stbDev", json);
        	// 目标地址
        	HttpPost httppost = new HttpPost(LipossGlobals.getLipossProperty("stb.Certus.URL"));
        	LOG.info("请求: " + httppost.getRequestLine());
        	// 参数
        	StringEntity reqEntity = new StringEntity(js.toString());
        	// 设置类型
        	reqEntity.setContentType("application/x-www-form-urlencoded");
        	// 设置请求的数据
        	httppost.setEntity(reqEntity);
        	LOG.info("请求内容:" + EntityUtils.toString(httppost.getEntity()));
        	
        	// 执行
        	HttpResponse response = httpClient.execute(httppost);

        	HttpEntity entity = response.getEntity();
        	LOG.info("请求状态:"+response.getStatusLine());

        	String jsonStr = "";
        	if(entity!=null)
        	{
        		String line = null;
	        	// 显示结果
	        	reader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
	        	while ((line = reader.readLine()) != null)
	        	{
	        		jsonStr += line;
	        	}
	        	entity.consumeContent();
        	}
            //处理内容
            LOG.info("返回内容:"+jsonStr);
            js = new JSONObject(jsonStr);
            data.put("errcode", String.valueOf(js.get("errcode")));
            // 判断错误码
            boolean flag = judgeErrCode(data);
            // 返回数据提示不正常情况
            if(!flag)
            {
            	return data;
            }
            // 解析其他结果
            data.put("stbcount", String.valueOf(js.get("stbcount")));
            if(!js.has("stbInfoList")){
            	return data;
            }
            JSONObject value = js.getJSONArray("stbInfoList").getJSONObject(0);
            
            String [] names = JSONObject.getNames(value);
            for(String name : names)
            {
            	data.put(name, 
            		(value.getString(name)==null || "null".equals(value.getString(name)))?"":value.getString(name));
            }
            
			// cpu,内存*100
			try
			{
				if(data.get("cpuRate")!=null && !"".equals(data.get("cpuRate")))
				{
					data.put("cpuRate",
						StringUtils.formatNumber(Float.parseFloat(data.get("cpuRate"))*100,2));
				}
				else
				{
					data.put("cpuRate","0");
				}
				if(data.get("memUseRate")!=null && !"".equals(data.get("memUseRate")))
				{
					data.put("memUseRate",
						StringUtils.formatNumber(Float.parseFloat(data.get("memUseRate"))*100,2));
				}
				else
				{
					data.put("memUseRate","0");
				}
				if(data.get("lossRate")!=null && !"".equals(data.get("lossRate")))
				{
					data.put("lossRate",
						StringUtils.formatNumber(Float.parseFloat(data.get("lossRate"))*100,2));
				}
				else
				{
					data.put("lossRate","0");
				}
			}
			catch (Exception e) {
				data.put("cpuRate","0");
				data.put("memUseRate","0");
				data.put("lossRate","0");
			}
			
			return data;
		}
        catch (JSONException e)
        {
			LOG.info("解析JSON对象发生错误", e);
			return null;
		}
        catch (Exception e)
		{
			LOG.info("Exception:", e);
			return null;
		}finally{
			try {
				if(reader!=null){
					reader.close();
					reader=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <pre>
	 * 请求机顶盒信息
	 * 说明：该方法与requestSTBInfo方法实现一致。在解决用户视屏质量参数不符合赛特斯格式要求时，
	 *   requestSTBInfo方法未传递mac地址，而且该方法被很多方法调用，加上业务不熟，
	 *   故新增该方法，防止引起其他bug。如果其他地方调用同样需要mac地址，可直接调用该方法。
	 * </pre>
	 * @param serviceUser
	 * @param sn
	 * @param mac
	 * @param cityId
	 * @return 
	 */
	public Map<String,String> requestSTB(String serviceUser,String sn,String mac,String cityId)
	{
		Map<String, String> data = new HashMap<String, String>();
		// 参数判断
		if (serviceUser == null || "".equals(serviceUser) || sn == null || "".equals(sn)
				|| "-1".equals(sn) || mac == null || "".equals(mac) || cityId == null
				|| "".equals(cityId))
		{
			data.put("errcode", "-10");
			String error = "请求参数格式不正确（缺少";
			if (serviceUser == null || "".equals(serviceUser))
			{
				error += "用户账号、";
			}
			if (sn == null || "".equals(sn) || "-1".equals(sn))
			{
				error += "SN、";
			}
			if (mac == null || "".equals(mac))
			{
				error += "MAC、";
			}
			if (cityId == null || "".equals(cityId))
			{
				error += "属地、";
			}
			data.put("valuation", error.substring(0, error.length() - 1) + "信息）");
			return data;
		}
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				30000);
		
		BufferedReader reader=null;
		try
		{
			JSONObject js = new JSONObject();
			// 参数
			Map<String, String> param = new HashMap<String, String>();
			param.put("serviceUser", serviceUser);
			param.put("sn", sn);
			// 属地编号与区号的对应关系
			param.put("cityId", ResTool.getCityExtMap().get(cityId).get("area_id"));
			param.put("mac", mac);
			JSONObject json = new JSONObject(param);
			js.put("event", 31);
			js.put("stbDev", json);
			// 目标地址
			HttpPost httppost = new HttpPost(LipossGlobals.getLipossProperty("stb.Certus.URL"));
			LOG.info("请求: " + httppost.getRequestLine());
			// 参数
			StringEntity reqEntity = new StringEntity(js.toString());
			// 设置类型
			reqEntity.setContentType("application/x-www-form-urlencoded");
			// 设置请求的数据
			httppost.setEntity(reqEntity);
			LOG.info("请求内容:" + EntityUtils.toString(httppost.getEntity()));
			// 执行
			HttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			LOG.info("请求状态:" + response.getStatusLine());
			String jsonStr = "";
			if (entity != null)
			{
				String line = null;
				// 显示结果
				reader = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"));
				while ((line = reader.readLine()) != null)
				{
					jsonStr += line;
				}
				entity.consumeContent();
			}
			// 处理内容
			LOG.info("返回内容:" + jsonStr);
			js = new JSONObject(jsonStr);
			data.put("errcode", String.valueOf(js.get("errcode")));
			// 判断错误码
			boolean flag = judgeErrCode(data);
			// 返回数据提示不正常情况
			if (!flag)
			{
				return data;
			}
			// 解析其他结果
			data.put("stbcount", String.valueOf(js.get("stbcount")));
			if (!js.has("stbInfoList"))
			{
				return data;
			}
			JSONObject value = js.getJSONArray("stbInfoList").getJSONObject(0);
			String[] names = JSONObject.getNames(value);
			for (String name : names)
			{
				data.put(name, (value.getString(name) == null || "null".equals(value
						.getString(name))) ? "" : value.getString(name));
			}
			// cpu,内存*100
			try
			{
				if (data.get("cpuRate") != null && !"".equals(data.get("cpuRate")))
				{
					data.put(
							"cpuRate",
							StringUtils.formatNumber(
									Float.parseFloat(data.get("cpuRate")) * 100, 2));
				}
				else
				{
					data.put("cpuRate", "0");
				}
				if (data.get("memUseRate") != null && !"".equals(data.get("memUseRate")))
				{
					data.put(
							"memUseRate",
							StringUtils.formatNumber(
									Float.parseFloat(data.get("memUseRate")) * 100, 2));
				}
				else
				{
					data.put("memUseRate", "0");
				}
				if (data.get("lossRate") != null && !"".equals(data.get("lossRate")))
				{
					data.put(
							"lossRate",
							StringUtils.formatNumber(
									Float.parseFloat(data.get("lossRate")) * 100, 2));
				}
				else
				{
					data.put("lossRate", "0");
				}
			}
			catch (Exception e)
			{
				data.put("cpuRate", "0");
				data.put("memUseRate", "0");
				data.put("lossRate", "0");
			}
			return data;
		}
		catch (JSONException e)
		{
			LOG.info("解析JSON对象发生错误", e);
			return null;
		}
		catch (Exception e)
		{
			LOG.info("Exception:", e);
			return null;
		}finally{
			try {
				if(reader!=null){
					reader.close();
					reader=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 判断返回数据的故障码
	 * @param data
	 * @return
	 */
	private boolean judgeErrCode(Map<String, String> data) {
		int errcode = Integer.parseInt(data.get("errcode"));
		// 判断返回结果
		switch (errcode) 
		{
			case -1:
				LOG.info("用户不存在");
				data.put("valuation", "该用户未纳入服务质量监测");
				return false;
			case -2:
				LOG.info("频道不存在");
				data.put("valuation", "频道不存在");
				return false;
			case -3:
				LOG.info("机顶盒关机");
				//data.put("valuation", "机顶盒关机");
				return true;
			case -4:
				LOG.info("机顶盒无响应");
				data.put("valuation", "机顶盒无响应");
				return false;
			case -5:
				LOG.info("视频分析仪无响应");
				data.put("valuation", "视频分析仪无响应");
				return false;
			case -6:
				LOG.info("该城市未部署");
				data.put("valuation", "该城市未部署");
				return false;
			case -7:
				LOG.info("请求参数格式不正确");
				data.put("valuation", "请求参数格式不正确");
				return false;
			case -8:
				LOG.info("数据库连接错误");
				data.put("valuation", "远程数据库连接失败");
				return false;
			case -9:
				LOG.info("服务错误");
				return false;
			case -13:
				LOG.info("用户在数据库中，但是没有下载监控程序");
				data.put("valuation", "用户在数据库中，但是没有下载监控程序");
				return false;
			case -14:
				LOG.info("用户长时间未开机（超过一周）");
				data.put("valuation", "用户长时间未开机（超过一周）");
				return false;
			case 0:
				LOG.info("正常");
				return true;
			default:
				LOG.info("其他未知错误码");
				data.put("valuation", "获取数据失败，请稍候再试");
				return false;
		}
	}

	/**
	 * 请求频道信息
	 * @param cityId 属地
	 * @param channelAddr 频道地址
	 * @param port 频道对应端口号
	 * @return
	 */
	public Map<String,String> requestEPGInfo(String cityId,String channelAddr,String port)
	{
		DefaultHttpClient httpClient = new DefaultHttpClient ();
		// 超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        
		 Map<String,String> data = new HashMap<String,String>();
        // 参数判断
        if(channelAddr==null || "".equals(channelAddr)
        	|| port==null || "".equals(port) 
        	|| cityId==null || "".equals(cityId))
        {
        	data.put("errcode", "-10");
        	data.put("valuation", "请求参数格式不正确");
        	return data;
        }

        BufferedReader reader=null;
        try {
        	JSONObject js = new JSONObject();
            
        	// 参数
    		Map<String,String> param = new HashMap<String,String>();
    		param.put("channelAddr", channelAddr);
    		param.put("port", port);
    		// 属地编号与区号的对应关系
    		param.put("cityId", ResTool.getCityExtMap().get(cityId).get("area_id"));
    		
    		JSONObject json = new JSONObject(param);
        	js.put("event", 32);
        	js.put("vaDev", json);
        	
        	// 目标地址
        	HttpPost httppost = new HttpPost("http://58.223.251.93:9081/NBI/va");
        	LOG.info("请求: " + httppost.getRequestLine());
        	// 参数
        	StringEntity reqEntity = new StringEntity(js.toString());
        	// 设置类型
        	reqEntity.setContentType("application/x-www-form-urlencoded");
        	// 设置请求的数据
        	httppost.setEntity(reqEntity);
        	LOG.info("请求内容:" + EntityUtils.toString(httppost.getEntity()));
        	
        	// 执行
        	HttpResponse response = httpClient.execute(httppost);
        	HttpEntity entity = response.getEntity();
        	LOG.info("请求状态:"+response.getStatusLine());

        	String jsonStr = "";
        	if(entity!=null)
        	{
        		String line = null;
	        	// 显示结果
	        	reader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
	        	while ((line = reader.readLine()) != null)
	        	{
	        		jsonStr += line;
	        	}
	        	entity.consumeContent();
        	}
        	
            //处理内容
            js = new JSONObject(jsonStr);
            data.put("errcode", String.valueOf(js.get("errcode")));
            judgeErrCode(data);
            // 解析其他结果
            JSONArray value = (JSONArray)js.get("responsePairs");
            for(int i=0;i<value.length();i++)
            {
            	data.put(String.valueOf(value.getJSONObject(i).get("name")),
            			String.valueOf(value.getJSONObject(i).get("value")));
            }
			
			return data;
		}
        catch (JSONException e)
        {
			LOG.info("解析JSON对象发生错误", e);
			return null;
		}
        catch (Exception e)
		{
			LOG.info("Exception:", e);
			return null;
		}finally{
			try {
				if(reader!=null){
					reader.close();
					reader=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
