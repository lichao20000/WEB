package com.linkage.module.ids.util;

import java.io.StringReader;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * WebService 工具类
 * @author zhangsm
 * @version 1.0
 * @since 2011-9-26 下午04:18:49
 * @category com.linkage.common<br>
 * @copyright 亚信联创 网管产品部
 */
public class WSClientUtil
{
	private static Logger logger = LoggerFactory.getLogger(WSClientUtil.class);
	public static void main(String[] args)
	{
			// 入参：xml字符串
			StringBuffer inParam = new StringBuffer();
			inParam.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n");
			inParam.append("<root><RstCode>1002</RstCode><RstMsg>机顶盒Mac地址对应的家庭网关不存在</RstMsg><mac>00:22:93:1D:F8:0E</mac><accounts/></root>");
//			inParam.append("    <CmdID>123456789012345</CmdID>       \n");
//			inParam.append("	<CmdType>CX_01</CmdType>           \n");
//			inParam.append("	<ClientType>3</ClientType>         \n");
//			inParam.append("	<Param>                            \n");
//			inParam.append("		<mac>00:22:93:1D:F8:0E</mac>      \n");
//			inParam.append("	</Param>                           \n");
//			inParam.append("</root>                              \n");
//			System.out.println(inParam.toString());
			final String endPointReference = "http://202.102.39.141:7070/ItmsService/services/ItmsService";
			System.out.println(callItmsService(endPointReference, inParam.toString(), "getItvAccount"));
			//解析回参
			SAXReader reader = new SAXReader();
			Document document = null;
			try {
				document = reader.read(new StringReader(inParam.toString()));
				Element root2 = document.getRootElement();
				Element account = root2.element("accounts").element("account");
				if(null != account)
				{
				   System.out.println(account.elementTextTrim("username"));
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
	}
	/**
	 * 调用WebService接口方法
	 * @param url   WebService接口地址
	 * @param inParam  入参
	 * @param method   方法名
	 * @return
	 */
	public static String callItmsService(String url, String inParam, String method)
	{
		logger.debug("callItmsService({})",new Object[] {url,inParam,method});
		String returnParam = "";
		try
		{
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(url));
			QName qn = new QName(url, method);
			call.setOperationName(qn);
			// 调用的服务器端方法
			// 回参：xml字符串
			returnParam = (String) call.invoke(new Object[] { inParam.toString() });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return returnParam;
	}
}
