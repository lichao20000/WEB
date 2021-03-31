<%@ include file="../timelater.jsp"%>

<%@ page import="org.apache.log4j.Logger" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%-- <%@page import="com.linkage.litms.Global"%> --%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>

<%@ page contentType="text/html;charset=GBK"%>
<%

Logger log = Logger.getLogger(this.getClass());

request.setCharacterEncoding("GBK");

String action = request.getParameter("action");

String para_name = request.getParameter("param_name");

String device_id = request.getParameter("device_id");

String para_value = request.getParameter("para_value");

String leafNodeListStr = request.getParameter("leafNodeListStr");

if(para_value != null){
	para_value = para_value.replace("%26", "&");
	para_value = para_value.replace("%2B", "+");
}
String gw_type = request.getParameter("gw_type");

if(null==gw_type){
	gw_type = com.linkage.litms.LipossGlobals.getGw_Type(device_id);
}

// ����
String area = com.linkage.litms.LipossGlobals.getLipossProperty("InstArea.ShortName");

out.println("<script language=javascript>");
String paraContainKey = "";
paramTreeObject.setGwType(gw_type);
if(action.equals("addChild")){//����ӽڵ�
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	Map paraMap = paramTreeObject.getParaMap(para_name,device_id);
	
	int flag = 0;

	if(paraMap != null){
		int i=0;
		while(paraMap.get(i+"")!=null){
			String strTitle= (String)paraMap.get(i+""); // hint_param + "," + writable
			String strText =  strTitle.substring(para_name.length(),strTitle.indexOf(",")); // param
			
			out.println("parent.addsub('"+ strText +"','"+ strTitle +"');");
			paraContainKey += "|" + strText;
			flag = 1;
			i++;
		}
	}
	//out.println("parent.createObjectResult("+ flag +");");
	out.println("parent.closeMsgDlg();");
	
		
}else if(action.equals("newChild")){
	int flag = paramTreeObject.addPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.delNodeAll();");
		out.println("parent.getChild();");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('��Ӳ���ʵ��ʧ�ܣ���������ӣ�');");
	}
}else if(action.equals("setValue")){
	int flag = paramTreeObject.setParaValueFlag(para_name,device_id,para_value);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʵ�����óɹ���');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʵ������ʧ�ܣ����������ã�');");
	}
}else if(action.equals("delValue")){
	int flag = paramTreeObject.delPara(para_name,device_id);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("parent.delNode();");
		out.println("alert('����ʵ��ɾ���ɹ���');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʵ��ɾ��ʧ�ܣ����������ã�');");
	}
}else if(action.equals("writeableConfig")){
	
	int flag = paramTreeObject.configUserWritable(para_name,device_id,request);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('���óɹ���');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʧ�ܣ����������ã�');");
	}
}else if(action.equals("getValue")){
	
	Map paraValueMap = paramTreeObject.getParaValueMap(para_name,device_id);
	if(paraValueMap == null){
		out.println("parent.closeMsgDlg();");
		out.println("alert('��ȡ����ֵʧ�ܣ������»�ȡ��')");
	}else{
		String paraValue = paramTreeObject.getParaVlue(paraValueMap);
		if(paraValue.indexOf("XXX")== -1){	
			out.println("parent.document.frm.para_value.value='" + paraValue + "';");
			out.println("parent.closeMsgDlg();");
			out.println("alert('�ɹ���ȡ����ֵ��')");
		}else{
			out.println("parent.closeMsgDlg();");
			out.println("alert('��ȡ����ֵʧ�ܣ������»�ȡ��')");
		}
	}
}if(action.equals("addChild2")){//����ӽڵ�
	//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	Map paraMap = paramTreeObject.getParaMap(para_name,device_id);
	
	int flag = 0;
	List<Map<String,String>> leafNodeList = new ArrayList<Map<String,String>>();
	// Ҷ�ӽڵ�
	Map<String,String> map = null;
	if(paraMap != null){
		int i=0; 
		String nodeText = "";
		String vlanPath = "";
		int size = paraMap.size();
		String[] titleArr = new String[size];
		String[] textArr = new String[size];
		String[] vlanPathArr = new String[size];
		while(paraMap.get(i+"")!=null){
			String strTitle= (String)paraMap.get(i+""); // hint_param + "," + writable
			String strText =  strTitle.substring(para_name.length(),strTitle.indexOf(",")); // param
			
			titleArr[i] = strTitle;
			textArr[i] = strText;
			
			// ������� wan�ڵ�˿���ʾ ������vlan��Ϣ
			if(para_name.contains(".")) {
				
				nodeText = para_name.substring(0,para_name.lastIndexOf('.')); //ȥ�����һ��"."
				nodeText = nodeText.substring(nodeText.lastIndexOf('.')+1);
				
				// ����ڵ���  WANConnectionDevice���ͻ�ȡ�ӽڵ��·��� X_CT-COM_WANGponLinkConfig.VLANIDMark��ֵ
				// ���磺InternetGatewayDevice.WANDevice.1.WANConnectionDevice.5.X_CT-COM_WANGponLinkConfig.VLANIDMark
				if(nodeText.equals("WANConnectionDevice")){
					// vlan�ڵ�·��
					vlanPath = strTitle.split(",")[0] + "X_CT-COM_WANGponLinkConfig.VLANIDMark";
					vlanPathArr[i] = vlanPath;
				}
			}
			// ��Ҷ�ӽڵ����list
			if(!strText.contains(".")){
				map = new HashMap<String,String>();
				String[] pathWArr = strTitle.split(",");
				map.put("name", strText);
				map.put("path", pathWArr[0]);
				map.put("writable", pathWArr[1]);
				leafNodeList.add(map);
			}
			
			paraContainKey += "|" + strText;
			flag = 1;
			i++;
		}
		// ������ȡvlanֵ
		if(size > 0)
		{
			if(vlanPathArr[0] != null)
			{	
				Map vlanValueMap = paramTreeObject.getParaValue_multi(vlanPathArr, device_id);
				
				if (vlanValueMap != null)
				{
					Set set = vlanValueMap.keySet();
					Iterator iterator = set.iterator();
					String path = null;
					String value = null;
					while (iterator.hasNext())
					{
						path = (String) iterator.next();
						value = (String) vlanValueMap.get(path);
						for(int j = 0; j < vlanPathArr.length; j++)
						{
							if(vlanPathArr[j].equals(path))
							{// ������ƴ��ֵ
								textArr[j] = textArr[j] + "_vlanIdMark: "+value;
							}
						}
					}
					iterator = null;
					set = null;
				}
			}
		}
		
		// �������ӽڵ㴫����ҳ��
		for(int k = 0; k < titleArr.length; k++){
			out.println("parent.addsub('"+ textArr[k] +"','"+ titleArr[k] +"');");
		}
		
		// ��Ҷ�ӽڵ㴫����ҳ�� 
		if(leafNodeList.size() > 0){
			//leafNodeList = [{name,path,writable}]
			out.println("parent.setLeafNodes('"+ para_name +"','"+ leafNodeList +"');");
		}
	}
	//out.println("parent.createObjectResult("+ flag +");");
	out.println("parent.closeMsgDlg();");
	
		
}else if(action.equals("getAllValue")){// ������� ��ȡ��ǰĿ¼�µ�����Ҷ�ӽڵ��ֵ
	
	//leafNodeListStr��ʽ //name,writable|name,writable����
	String[] leafNodeListArr = leafNodeListStr.split("\\|");
	// Ҷ�ӽڵ�·�����飬���������ɼ��ڵ�ֵ
	String[] pathArr = new String[leafNodeListArr.length];
	
	List<Map<String,String>> leafNodeList = new ArrayList<Map<String,String>>();
	// Ҷ�ӽڵ�
	Map<String,String> map = null;
	
	for(int i = 0; i < leafNodeListArr.length ; i++)
	{
		String[] leafNodeArr = leafNodeListArr[i].split(",");
		String name = leafNodeArr[0];
		String writable = leafNodeArr[1];
		String path = para_name + name;
		
		pathArr[i] = path;
		
		map = new HashMap<String,String>();
		map.put("path", path);
		map.put("name", name);
		map.put("writable", writable);
		leafNodeList.add(map);
	}
	
	Map paraValueMap = paramTreeObject.getParaValue_multi(pathArr, device_id);
	
	if (paraValueMap == null)
	{
		out.println("alert('��ȡ����ֵʧ�ܣ������»�ȡ��')");
	}
	else
	{
		Set set = paraValueMap.keySet();
		Iterator iterator = set.iterator();
		String path = null;
		String value = null;
		while (iterator.hasNext())
		{
			path = (String) iterator.next();
			value = (String) paraValueMap.get(path);
			value = value.replace(",", "");
			value = value.replace("\"", "|");
			for(int j = 0; j < leafNodeList.size(); j++)
			{
				if(leafNodeList.get(j).get("path").equals(path))
				{
					leafNodeList.get(j).put("value", value);
				}
			}
		}
		iterator = null;
		set = null;
		
		if(leafNodeList.size() > 0){
			//leafNodesValueStr = "path,value,writable|path,value,writable"
			out.println("parent.setLeafNodesValue('"+ para_name +"','"+ leafNodeList +"');");
		}
		out.println("alert('�ɹ���ȡ����ֵ��')");
	}
	out.println("parent.closeMsgDlg();");
	
}else if(action.equals("setAllValue")){
	// ·��
	String path = para_name;
	// Ҫ�޸ĵĽڵ�·������
	String[] nodeNameArr = leafNodeListStr.split("___");
	for(int i = 0; i< nodeNameArr.length; i++){
		nodeNameArr[i] = path + nodeNameArr[i];
	}
	// Ҫ�޸ĵĽڵ�ֵ����
	String[] nodeValueArr = para_value.split("___");
	
	int flag = paramTreeObject.setParaValueFlag_multi(nodeNameArr,device_id,nodeValueArr);
	if(flag == 1){
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʵ�����óɹ���');");
	}else{
		out.println("parent.closeMsgDlg();");
		out.println("alert('����ʵ������ʧ�ܣ����������ã�');");
	}
}
out.println("</script>");

%>