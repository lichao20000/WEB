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
  String content = "�豸״̬����";
  String result = "";
  if(data.size()==0){
	  String divName = "";
	  result="ʧ��";
	  if("1".equals(type))
	  {
		  divName = "DLS_";
		  //content="�鿴DLS����";		  
	  }		 
	  else if("2".equals(type))
	  {
		  divName = "PPPOE_,IP_,ATM_";	
		  //content="�鿴PPPOE��IP��ATM����";
	  }	  	  
	  else if("3".equals(type))
	  {
		  divName = "LAN_";
		  //content="�鿴LAN����";
	  }		  
	  else if("4".equals(type))
	  {
		  divName = "WLAN_";
		  //content="�鿴WLAN����";
	  }	  
	  for(int i=0;i<divName.split(",").length;i++){
		  out.println("<div id='"+divName.split(",")[i]+"'>");
		  out.println("<TABLE border=0 cellspacing=1 cellpadding=1 width='100%' height='100%'>");
		  out.println("<TR>");
		  out.println("<TD class=column colspan=3 align=center>û�ж�Ӧ������</TD>");
		  out.println("</TR>");
		  out.println("</TABLE></DIV>");
	  }
  }else{
	  String divName = "";
	  result="�ɹ�";
	  ArrayList tableArray = new ArrayList();
	  Iterator it = data.entrySet().iterator();
	  while(it.hasNext()){
		  Map.Entry entry = (Map.Entry)it.next();
		  divName = (String)entry.getKey();
		  tableArray = (ArrayList)entry.getValue();
		  //��ͷmap
		  Map tableMap = (Map)tableArray.get(0);
		  //���ݱ�ͷ
		  Map dataMap = (Map)tableArray.get(1);
		  
		  out.println("<DIV id='"+divName+"_'>");
		  out.println("<TABLE border=0 cellspacing=1 cellpadding=1 width='100%' height='100%'>");
		  out.println("<tr>");
		  //------------------����ͷ
		  Iterator it1 = tableMap.entrySet().iterator();
		  while(it1.hasNext()){
			  Map.Entry entry1 = (Map.Entry)it1.next();
			  String _value = (String)entry1.getValue();
			  out.println("<th>"+_value+"</th>");
		  }
		  //��DLS ����ʾ�����
		  if(!"1".equals(type))
			  out.println("<th>�������</th>");
		  //------------------��ͷ����
		  out.println("</tr>");
		  
		  //------------------������
		  Iterator it2 = dataMap.entrySet().iterator();
		  while(it2.hasNext()){
			  Map.Entry entry2 = (Map.Entry)it2.next();
			  //�ڵ�����
			  String _nodeName = (String)entry2.getKey();
			  //������
			  Map rowMap = (Map)entry2.getValue();
			  
			  out.println("<tr>");
			  //���ݱ�ͷmap��key ����rowMap ��ȡ����
			  it1 = tableMap.keySet().iterator();
			  while(it1.hasNext()){
				  String _key = (String)it1.next();
				  String rowData = (String)rowMap.get(_key);
				  out.println("<td class=column>"+(rowData==null?"":rowData)+"</td>");
			  }
			  //��DLS ����ʾ�����
			  if(!"1".equals(type))
				  out.println("<td class=column>"+_nodeName+"</td>");
			  out.println("</tr>");
		  }
		  //------------------�����ݽ���

		  out.println("</table>");
		  out.println("</DIV>");
	  }// end of data while
		  
	 
	  data = null;
	  act = null;
  }
  
  //��¼��־
  try
  {
	  content = Encoder.toISO(content);
	  result= Encoder.toISO(result);
	  LogItem.getInstance().writeItemLog(request,1,device_id,content,result);
  }
  catch(Exception e)
  {
	  //�����쳣����¼��־
  }
%>
