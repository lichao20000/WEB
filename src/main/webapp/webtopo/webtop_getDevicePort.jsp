<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%@ page import ="com.linkage.litms.vipms.flux.FluxPortInfo"%>
<%@ page import ="com.linkage.litms.vipms.flux.PortJudgeAttr"%>
<%
request.setCharacterEncoding("GBK");
ManagerFluxConfig mfc=new ManagerFluxConfig(request);
//mfc.setSerial();
//ArrayList list=mfc.getPortBaseInfo();
//ArrayList attrList=mfc.getoidList("1");

//�Ƿ��Զ����ã�1���Զ����ã�2���ֹ�����
String isauto = request.getParameter("isauto");
if(null==isauto||"".equals(isauto))
{
	isauto="2";
}
//�ֹ����òɼ���ʽ
String gatherflag = request.getParameter("gatherflag");
if(null==gatherflag||"".equals(gatherflag))
{
	gatherflag="3";
}
String serial = request.getParameter("serial");

ArrayList list = null;
ArrayList attrList = null;
//�Զ����õ����
if("1".equals(isauto))
{
   //��Ϊ��ͬ�汾���������Ķ˿���ϢOID����ͬ����Ĭ��ȡv2�汾��64λ������
   ArrayList oiList =mfc.getOIDList(serial,"2","64","2");
   list= mfc.getPortBaseInfo(oiList);
   //���Ĭ�ϸ�v2�汾64λ������������OID�����Ǻ���ȷ
   attrList=mfc.getOIDList(serial,"2","64","1");
}
else
{
   String snmpversion = gatherflag;
   String counternum = "32";
   ArrayList oiList =mfc.getOIDList(serial,snmpversion,counternum,"2");
   list= mfc.getPortBaseInfo(oiList);
   attrList =mfc.getOIDList(serial,snmpversion,counternum,"1");   
}
%>



<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<TR><TD>	
        
	   <div id="child">    
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="blue_title"> 
						<td nowrap width=10%>
							<input type="checkbox" name="checkPort" onclick="javascript:CheckAllPorts();">ȫѡ
						</td>
						<td width=60%>
							�ɼ���ʽ
						</td>
						<td nowrap width=30%>
							�ɼ�����
						</td>
					</TR>		
					<%
						FluxPortInfo fpi=null;
						PortJudgeAttr pja=null;
						int len=list.size();
						int attrLen=attrList.size();
						//String radio="";
						String checkbox="";
						String check_value="";
						int getway=1;
						for(int i=0;i<len;i++)
						{	
							fpi=(FluxPortInfo)list.get(i);
							//radio="radio"+fpi.getIfindex();
							getway = mfc.getGetway(fpi,list);
							checkbox="checkbox"+fpi.getIfindex();
							out.println("<tr>");
							out.println("<td  class=column1 width=10% align=center >");
							out.println("<input type='checkbox' name='ifindex' value="+fpi.getIfindex()+"|||"+getway+">");	
							out.println("</td>");
							out.println("<td  class=column1 width=60% >");
							out.println("�˿�IP:"+fpi.getIfportip()+"<br>");
							out.println("�˿�����:"+fpi.getIfdescr()+"<br>");
							out.println("�˿�����:"+fpi.getIfname()+"<br>");
							out.println("�˿ڱ���:"+fpi.getIfnamedefined()+"<br>");							
							out.println("�˿�����:"+fpi.getIfindex()+"<br>");
							out.println("</td>");
							out.println("<td  class=column1 width=30% >");
							out.println("<table border=0 cellpadding=0 cellspacing=0 >");
							out.println("<tr>");
							for(int j=0;j<attrLen;j++)
							{
								
								pja=(PortJudgeAttr) attrList.get(j);
								check_value=pja.getValue()+"|||"+pja.getOrder();	
								out.println("<td><input type='checkbox' name="+checkbox+" value=\""+check_value+"\" checked>"+pja.getDesc()+"</td>");
								if((j+1)%2==0)
								{
									out.println("</tr><tr>");
								}
							}
							out.println("</tr>");
							out.println("</table>");
							out.println("</td>");
							out.println("</tr>");
						
						}
					
					%>
			</TABLE>	
		</div>
</TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.document.all("td1").style.background="#999999";
	parent.document.all("span1").innerHTML=child.innerHTML;
	//���ΰ����豸IP���豸�������豸���ơ��豸�������豸�����ķ����Զ˿ڽ���Ĭ�ϵ�����
	parent.isCall=1;
	





//-->
</SCRIPT>
