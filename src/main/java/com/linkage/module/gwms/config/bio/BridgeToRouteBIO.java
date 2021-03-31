
package com.linkage.module.gwms.config.bio;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.StaticTypeCommon;

@SuppressWarnings("unchecked")
public class BridgeToRouteBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BridgeToRouteBIO.class);
	private String message = null;
	String ServiceURL = "http://218.80.254.148:8181/ItmsService/services/CtService";

	/**
	 * 桥改路由，调WebService接口，查询接口
	 * 
	 * @author wangsenbo
	 * @date Jul 20, 2010
	 * @param
	 * @return void
	 */
	public List infoQuery(String username, String devSn)
	{
		Service service = null;
		Call call = null;
		String ret = null;
//		try
//		{
//			service = new Service();
//			call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(ServiceURL));
//			call.setOperationName(new QName("ctInfoQuery"));
//			call.addParameter("inXml", org.apache.axis.Constants.XSD_STRING,
//					javax.xml.rpc.ParameterMode.IN);
//			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
//			Object objRet = call.invoke(new Object[] { infoQueryXml(username, devSn) });
//			// Object objRet = call.invoke(new Object[] {});
//			if (null == objRet)
//			{
//			}
//			else
//			{
//				ret = StringUtil.getStringValue(objRet);
				ret = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID><RstCode>0</RstCode><RstMsg>成功</RstMsg><Param><DevFactory>华为</DevFactory><DevModel>v1.1</DevModel><DevSn>12345678</DevSn><RouteSupported>1</RouteSupported><NetType>1</NetType></Param></root>";
//			}
//		}
//		catch (ServiceException e2)
//		{
//			ret = null;
//			e2.printStackTrace();
//		}
//		catch (MalformedURLException e1)
//		{
//			ret = null;
//			e1.printStackTrace();
//		}
//		catch (RemoteException e)
//		{
//			ret = null;
//			e.printStackTrace();
//		}
		List list = new ArrayList();
		if (ret != null && !"".equals(ret))
		{
			list = readInfoQueryXml(ret);
		}
		else
		{
			message = "调用查询接口失败!";
		}
		return list;
	}

	/**
	 * 桥改路由，WebService接口，查询接口,返回xml解析
	 * 
	 * @author wangsenbo
	 * @date Jul 20, 2010
	 * @param
	 * @return void
	 */
	private List readInfoQueryXml(String xml)
	{
		List list = new ArrayList();
		try
		{
			StringReader read = new StringReader(xml);
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(read);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			if (!"root".equals(root.getName()))
			{
				message = "返回xml格式错误！";
				return list;
			}
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1); // RstCode
			Element RstMsg = elements.get(2);
			Element Param = elements.get(3);
			if (!"CmdID".equals(CmdID.getName()) || !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName())
					|| !"Param".equals(Param.getName()))
			{
				message = "返回xml格式错误！";
				return list;
			}
			if (!"0".equals(RstCode.getTextTrim()))
			{
				message = RstMsg.getTextTrim();
				return list;
			}
			else
			{
				List<Element> Params = Param.elements();
				Element DevFactory = Params.get(0);
				Element DevModel = Params.get(1); // RstCode
				Element DevSn = Params.get(2);
				Element RouteSupported = Params.get(3);
				Element NetType = Params.get(4);
				if (!"DevFactory".equals(DevFactory.getName())
						|| !"DevModel".equals(DevModel.getName())
						|| !"DevSn".equals(DevSn.getName())
						|| !"RouteSupported".equals(RouteSupported.getName())
						|| !"NetType".equals(NetType.getName()))
				{
					message = "返回xml格式错误！";
					return list;
				}
				Map map = new HashMap();
				map.put("DevFactory", DevFactory.getTextTrim());
				map.put("DevModel", DevModel.getTextTrim());
				map.put("DevSn", DevSn.getTextTrim());
				map.put("RouteSupported", RouteSupported.getTextTrim());
				map.put("NetType", NetType.getTextTrim());
				list.add(map);
				return list;
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
			message = "返回xml格式错误！";
			return list;
		}
	}

	/**
	 * 生成WebService接口的查询接口的入参xml
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	private String infoQueryXml(String username, String devSn)
	{
		logger.debug("infoQueryXml({},{})", username, devSn);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("root");
		root.addElement("CmdID").addText(StaticTypeCommon.generateId());
		root.addElement("CmdType").addText("CX_01");
		root.addElement("ClientType").addText("5");
		Element param = root.addElement("Param");
		param.addElement("UserName").addText(username);
		param.addElement("DevSN").addText(devSn);
		strXml = doc.asXML();
		strXml = strXml.replace("UTF-8", "GBK");
		return strXml;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	public static void main(String[] args)
	{
		BridgeToRouteBIO aaa = new BridgeToRouteBIO();
		String xml = aaa.bridge2RoutedXml("wangsenbo", "12345678", "1432");
		System.out.println(xml);
		// StringReader read = new StringReader(xml);
		//        
		// SAXReader reader = new SAXReader();
		// Document doc;
		// try {
		// doc = reader.read(read);
		// Element root = doc.getRootElement();
		// System.out.println(root.getName());
		// List<Element> elements = root.elements();
		// for(Iterator<Element> it = elements.iterator();it.hasNext();){
		// Element element = it.next();
		// System.out.println(element.getName()+" : "+element.getTextTrim());
		// }
		//               
		// }catch(DocumentException e){
		// e.printStackTrace();
		// }
//		 String xml1 = "<?xml version=\"1.0\"
//		 encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID><RstCode>0</RstCode><RstMsg>成功</RstMsg><Param><DevFactory>123</DevFactory><DevModel>321</DevModel><DevSn>456</DevSn><RouteSupported>1</RouteSupported><NetType>1</NetType></Param></root>";
		// List list = aaa.readInfoQueryXml(xml1);
		// for (int i = 0; i < list.size(); i++)
		// {
		// Map map = (Map) list.get(i);
		// System.out.println("DevFactory:" + map.get("DevFactory"));
		// System.out.println("DevModel:" + map.get("DevModel"));
		// System.out.println("DevSn:" + map.get("DevSn"));
		// System.out.println("RouteSupported:" + map.get("RouteSupported"));
		// System.out.println("NetType:" + map.get("NetType"));
		// }
		// System.out.println(aaa.getMessage());
	}

	/**
	 * 桥改路由，调WebService接口，业务下发接口
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return void
	 */
	public void bridge2Routed(String username, String devSn, String password)
	{
		Service service = null;
		Call call = null;
		String ret = null;
//		try
//		{
//			service = new Service();
//			call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(ServiceURL));
//			call.setOperationName(new QName("ctBridge2Routed"));
//			call.addParameter("inXml", org.apache.axis.Constants.XSD_STRING,
//					javax.xml.rpc.ParameterMode.IN);
//			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
//			Object objRet = call.invoke(new Object[] { bridge2RoutedXml(username, devSn,
//					password) });
//			// Object objRet = call.invoke(new Object[] {});
//			if (null == objRet)
//			{
//			}
//			else
//			{
//				ret = StringUtil.getStringValue(objRet);
		ret = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID><RstCode>0</RstCode><RstMsg>成功</RstMsg></root>";
//			}
//		}
//		catch (ServiceException e2)
//		{
//			ret = null;
//			e2.printStackTrace();
//		}
//		catch (MalformedURLException e1)
//		{
//			ret = null;
//			e1.printStackTrace();
//		}
//		catch (RemoteException e)
//		{
//			ret = null;
//			e.printStackTrace();
//		}
		if (ret != null && !"".equals(ret))
		{
			readBridge2RoutedXml(ret);
		}
		else
		{
			message = "调用业务下发接口失败";
		}
	}

	/**
	 * 生成WebService接口的业务下发接口的入参xml
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	private String bridge2RoutedXml(String username, String devSn, String password)
	{
		logger.debug("bridge2RoutedXml({},{},{})", new Object[] { username, devSn,
				password });
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("root");
		root.addElement("CmdID").addText(StaticTypeCommon.generateId());
		root.addElement("CmdType").addText("CX_02");
		root.addElement("ClientType").addText("5");
		Element param = root.addElement("Param");
		param.addElement("UserName").addText(username);
		param.addElement("Passwd").addText(password);
		param.addElement("DevSN").addText(devSn);
		strXml = doc.asXML();
		strXml = strXml.replace("UTF-8", "GBK");
		return strXml;
	}

	/**
	 * 桥改路由，WebService接口，业务下发,返回xml解析
	 * 
	 * @author wangsenbo
	 * @date Jul 20, 2010
	 * @param
	 * @return void
	 */
	private void readBridge2RoutedXml(String xml)
	{
		try
		{
			StringReader read = new StringReader(xml);
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(read);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			if (!"root".equals(root.getName()))
			{
				message = "返回xml格式错误！";
			}
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1); // RstCode
			Element RstMsg = elements.get(2);
			if (!"CmdID".equals(CmdID.getName()) || !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName()))
			{
				message = "返回xml格式错误！";
			}
			else
			{
				message = RstMsg.getTextTrim();
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
			message = "返回xml格式错误！";
		}
	}

	/**
	 * 桥改路由，调WebService接口，桥改路由下发情况查看接口
	 *
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param 
	 * @return void
	 */
	public void routedQuery(String username, String devSn)
	{
		Service service = null;
		Call call = null;
		String ret = null;
//		try
//		{
//			service = new Service();
//			call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(ServiceURL));
//			call.setOperationName(new QName("ctRoutedQuery"));
//			call.addParameter("inXml", org.apache.axis.Constants.XSD_STRING,
//					javax.xml.rpc.ParameterMode.IN);
//			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
//			Object objRet = call.invoke(new Object[] { routedQueryXml(username, devSn) });
//			// Object objRet = call.invoke(new Object[] {});
//			if (null == objRet)
//			{
//			}
//			else
//			{
//				ret = StringUtil.getStringValue(objRet);
		ret = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><CmdID>123456789012345</CmdID><RstCode>0</RstCode><RstMsg>成功</RstMsg><Param><Result>1</Result><ResultDesc>1</ResultDesc></Param></root>";
//			}
//		}
//		catch (ServiceException e2)
//		{
//			ret = null;
//			e2.printStackTrace();
//		}
//		catch (MalformedURLException e1)
//		{
//			ret = null;
//			e1.printStackTrace();
//		}
//		catch (RemoteException e)
//		{
//			ret = null;
//			e.printStackTrace();
//		}
		if (ret != null && !"".equals(ret))
		{
			readRoutedQueryXml(ret);
		}
		else
		{
			message = "调用业务下发接口失败";
		}
		
	}
	
	/**
	 * 生成WebService接口的桥改路由下发情况查看接口的入参xml
	 * 
	 * @author wangsenbo
	 * @date Jul 21, 2010
	 * @param
	 * @return String
	 */
	private String routedQueryXml(String username, String devSn)
	{
		logger.debug("routedQueryXml({},{})", new Object[] { username, devSn});
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("root");
		root.addElement("CmdID").addText(StaticTypeCommon.generateId());
		root.addElement("CmdType").addText("CX_03");
		root.addElement("ClientType").addText("3");
		Element param = root.addElement("Param");
		param.addElement("UserName").addText(username);
		param.addElement("DevSN").addText(devSn);
		strXml = doc.asXML();
		strXml = strXml.replace("UTF-8", "GBK");
		return strXml;
	}
	
	/**
	 * 桥改路由，WebService接口，业务下发,返回xml解析
	 * 
	 * @author wangsenbo
	 * @date Jul 20, 2010
	 * @param
	 * @return void
	 */
	private void readRoutedQueryXml(String xml)
	{
		try
		{
			StringReader read = new StringReader(xml);
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(read);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			if (!"root".equals(root.getName()))
			{
				message = "返回xml格式错误！";
			}
			Element CmdID = elements.get(0);
			Element RstCode = elements.get(1); // RstCode
			Element RstMsg = elements.get(2);
			Element Param = elements.get(3);
			if (!"CmdID".equals(CmdID.getName()) || !"RstCode".equals(RstCode.getName())
					|| !"RstMsg".equals(RstMsg.getName())
					|| !"Param".equals(Param.getName()))
			{
				message = "返回xml格式错误！";
			}
			if (!"0".equals(RstCode.getTextTrim()))
			{
				message = RstMsg.getTextTrim();
			}
			else
			{
				List<Element> Params = Param.elements();
				Element Result = Params.get(0);
				Element ResultDesc = Params.get(1); 
				if (!"Result".equals(Result.getName())
						|| !"ResultDesc".equals(ResultDesc.getName()))
				{
					message = "返回xml格式错误！";
				}else{
					message = ResultDesc.getTextTrim();
				}
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
			message = "返回xml格式错误！";
		}
	}
}
