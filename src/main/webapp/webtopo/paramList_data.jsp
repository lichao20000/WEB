<%@ page import="com.linkage.litms.webtopo.ParameterAct"%>
<%@ page import="java.util.*"%>
<%@ page import=" com.linkage.litms.system.dbimpl.LogItem"%>
<%@ page import=" com.linkage.litms.common.util.Encoder"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
  String type = request.getParameter("type");

  ParameterAct act = new ParameterAct(); 
  Map data = act.getDeviceParam(Integer.parseInt(type),request);    
  String device_id = request.getParameter("device_id");
  String content = "设备状态监视";
  String result = "";
  if(data.size()==0){
	  String divName = "";
	  result="失败";
	  if("1".equals(type))
	  {
		  divName = "DLS_";
		  //content="查看DLS参数";		  
	  }		 
	  else if("2".equals(type))
	  {
		  divName = "PPPOE_,IP_,ATM_";	
		  //content="查看PPPOE、IP、ATM参数";
	  }	  	  
	  else if("3".equals(type))
	  {
		  divName = "LAN_";
		  //content="查看LAN参数";
	  }		  
	  else if("4".equals(type))
	  {
		  divName = "WLAN_";
		  //content="查看WLAN参数";
	  }	  
	  for(int i=0;i<divName.split(",").length;i++){
		  out.println("<div id='"+divName.split(",")[i]+"'>");
		  out.println("<TABLE border=0 cellspacing=1 cellpadding=1 width='100%' height='100%'>");
		  out.println("<TR>");
		  out.println("<TD class=column colspan=3 align=center>没有对应的数据</TD>");
		  out.println("</TR>");
		  out.println("</TABLE></DIV>");
	  }
  }else{
	  String divName = "";
	  result="成功";
	  ArrayList tableArray = new ArrayList();
	  Iterator it = data.entrySet().iterator();
	  while(it.hasNext()){
		  Map.Entry entry = (Map.Entry)it.next();
		  divName = (String)entry.getKey();
		  tableArray = (ArrayList)entry.getValue();
		  //表头map
		  Map tableMap = (Map)tableArray.get(0);
		  //数据表头
		  Map dataMap = (Map)tableArray.get(1);
		  
		  out.println("<DIV id='"+divName+"_'>");
		  out.println("<TABLE border=0 cellspacing=1 cellpadding=1 width='100%' height='100%'>");
		  out.println("<tr>");
		  //------------------画表头
		  Iterator it1 = tableMap.entrySet().iterator();
		  while(it1.hasNext()){
			  Map.Entry entry1 = (Map.Entry)it1.next();
			  String _value = (String)entry1.getValue();
			  out.println("<th>"+_value+"</th>");
		  }
		  //非DLS 需显示结点名
		  if(!"1".equals(type))
			  out.println("<th>结点名字</th>");
		  //------------------表头结束
		  out.println("</tr>");
		  
		  //------------------表数据
		  Iterator it2 = dataMap.entrySet().iterator();
		  while(it2.hasNext()){
			  Map.Entry entry2 = (Map.Entry)it2.next();
			  //节点名字
			  String _nodeName = (String)entry2.getKey();
			  //行数据
			  Map rowMap = (Map)entry2.getValue();
			  
			  out.println("<tr>");
			  //根据表头map的key 遍历rowMap 获取数据
			  it1 = tableMap.keySet().iterator();
			  while(it1.hasNext()){
				  String _key = (String)it1.next();
				  String rowData = (String)rowMap.get(_key);
				  out.println("<td class=column>"+(rowData==null?"":rowData)+"</td>");
			  }
			  //非DLS 需显示结点名
			  if(!"1".equals(type))
				  out.println("<td class=column>"+_nodeName+"</td>");
			  out.println("</tr>");
		  }
		  //------------------表数据结束

		  out.println("</table>");
		  out.println("</DIV>");
	  }// end of data while
		  
	 
	  data = null;
	  act = null;
  }
  
  //记录日志
  try
  {
	  content = Encoder.toISO(content);
	  result= Encoder.toISO(result);
	  LogItem.getInstance().writeItemLog(request,1,device_id,content,result);
  }
  catch(Exception e)
  {
	  //发生异常不记录日志
  }
%>
