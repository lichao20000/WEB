<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.GeneralFluxPerf,
				 com.linkage.litms.LipossGlobals,
				 com.linkage.litms.flux.*,
				 com.linkage.litms.common.database.Cursor,
				 com.linkage.litms.report.FluxUnit,
				 java.util.*,
				 java.sql.*,
				 java.lang.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
//��ȡ��������ͣ�1Ϊ�գ�2Ϊ�ܣ�3Ϊ�£�4��Ϊ�꣬5Ϊ�׶�
String str_SearchType=request.getParameter("SearchType");
//��ȡ��ѯʱ��
String str_day = request.getParameter("day");

//��ȡ��ѯʱ���ʱ����
String str_day_start = request.getParameter("hidday");

//��ȡ�豸ID
String str_device_id = request.getParameter("dev_id");

//��ȡ����
String [] arr_kind   = request.getParameterValues("kind");
//��ȡ�˿�
String [] arr_port   = request.getParameterValues("port");

long str_start=0;
long str_end=0;

str_start=Long.parseLong(str_day_start);

int SearchType=Integer.parseInt(str_SearchType);

String title = "";
switch(SearchType){
	case 1://��			
		str_end	= str_start + 24*60*60-1;
		title = "�˿�������ͳ�Ʊ���";
		break;
	case 2://��
        str_end= str_start + 7*24*60*60-1;
		title = " �˿�������ͳ�Ʊ���";
		break;
	case 3://��
		String strmonth = request.getParameter("day")+"-1";
		java.sql.Date date = java.sql.Date.valueOf(strmonth);
		str_start = date.getTime()/1000;

		java.util.Date mydate = new java.util.Date((long)(str_start)*1000);
		int year  = mydate.getYear();
		int month = mydate.getMonth();
		String str_month = new Integer(year+1900).toString() +"-"+ new Integer(month+1).toString();
		mydate = new java.util.Date(year,month+1,1);
		str_end = (long)(mydate.getTime()/1000);
		title = "�˿�������ͳ�Ʊ���";
		break;
	case 4://��
		String stryear = request.getParameter("day")+"-01-01";
		java.sql.Date dateyear = java.sql.Date.valueOf(stryear);
		str_start = dateyear.getTime()/1000;
		java.util.Date mydateyear = new java.util.Date(str_start*1000);
		int year1  = mydateyear.getYear();
		int str_year = year1+1900;
		mydateyear = new java.util.Date(year1+1,0,1);
		str_end = (long)(mydateyear.getTime()/1000);
		title = "�˿�������ͳ�Ʊ���";
		break;
}
GeneralFluxPerf flux = new GeneralFluxPerf(str_start,str_end,SearchType);
//���ýӿڣ�������cursor
Cursor cursor = flux.getGeneralTxtData(request,0,0);

//��ȡ��ѯ��¼
Map fields = cursor.getNext();

int colnum = arr_kind.length + 2;
//device_id��IP��Ӧ��ϵ
DbUserRes  dbUserRes = (DbUserRes) session.getAttribute("curUser");
List gather_id = dbUserRes.getUserProcesses();
Map ipMap = com.linkage.litms.common.util.CommonMap.getDeviceIPMap(gather_id);

//��ȡ�����ļ��еĵ�λ
/*
String FluxBase=LipossGlobals.getLipossProperty("report.FluxBase");
String UnitName=LipossGlobals.getLipossProperty("report.UnitName");
String confirm=LipossGlobals.getLipossProperty("report.confirm");
String name="M";
double   l_unit=1000*1000;
double   unit=1000*1000;
if(UnitName!=null)
{
	l_unit=Double.parseDouble(FluxBase)*Double.parseDouble(FluxBase)*Double.parseDouble(FluxBase);
	unit =unit*Double.parseDouble(confirm);
	name=UnitName;
}
*/
FluxUnit fu=FluxUnit.getFluxUnit(session);
String name=fu.getUnitName();
double l_unit=0.0;
double unit=fu.getUnit()*fu.getUnit()*fu.getConfirm();
if(name.equals("M"))
{
	l_unit=fu.getFluxBase()*fu.getFluxBase();	
}
else
{
	l_unit=fu.getFluxBase()*fu.getFluxBase()*fu.getFluxBase();	

}
//ӳ�����������ַ�
Map map = FluxMap.getFluxMap(name);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function setDivStyle(){
	var maxScreenX = window.screen.width;
	var maxScreenY = window.screen.height;

	var w = maxScreenX * 0.9;
	var h = maxScreenY * 0.9 - 350; 
	idLayer.style.width = w;
	idLayer.style.height = h;
}
window.onload=setDivStyle;
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckForm.js"></SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM method=POST action="" NAME="frm1">
<DIV id="idLayer" style="overflow:auto;width:'800px';height:'400px'">
  <TABLE  width="100%" border=0 align="center" cellpadding=0 cellspacing=0 bgcolor="#999999">
    <tr>
					<td>
						<Table width="100%" border=0 cellpadding=2 cellspacing=1 id="topTable">
							<TR>
								<td bgcolor="#FFFFFF" class="blue_td">
									��ѡ��ͼ����ʾ����������
								</td>
								<td width="88%"  nowrap bgcolor=#FFFFFF>
									<table width="100%" height="30"  border="0" cellpadding="0" cellspacing="0" class="blue_gargtd">
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap>										  
										<input name="kind_radio" type="radio"   value="ifinoctetsbps" checked>
										  ��������(bps)&nbsp; </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="ifindiscardspps">
										  ���붪����&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"    value="ifinerrors">
										  ����������&nbsp; </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"    value="ifinoctetsbpsmax">
										  �����ֵ</td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="ifinucastpktspps">
										  ÿ�����뵥������ </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="u1">
										  �������������</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifoutoctetsbps">
										  ��������(bps)&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutdiscardspps">
										  ����������&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifouterrors">
										  �����������</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutoctetsbpsmax">
										  ������ֵ</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifinnucastpktspps">
										  ÿ������ǵ�������</td>
										<td bgcolor=#FFFFFF  nowrap><input type="radio" name="kind_radio" value="u2" >
										  ��������������</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifinerrorspps">
										  ��������</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifinoctets">
										  �����ֽ���</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifindiscards">
										  ���붪������&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="u3">
										  �������������</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutucastpktspps">
										  ÿ��������������&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifouterrorspps">
										  ���������</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"  value="ifoutoctets">
										  �����ֽ���&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifoutdiscards">
										  ������������&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="u4">
										  �������������</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutnucastpktspps">
										  ÿ�������ǵ�������</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  </tr>
									  <td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifinunknownprotospps">
										  ÿ������δ֪Э�����</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutqlenpps">
										  ÿ���������д�С</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  <tr>
									  </tr>
									</table>
								</td>
							</tr>
							<TR bgcolor="#FFFFFF"  onmouseout="className='blue_trOut'"> 
								<TD colspan=2 align=right class="blue_foot"> <div align="right"> 
									<input type="hidden" name="queryTime" >
									<input type="hidden" name="queryMonth">
					  
									<INPUT TYPE="button" name="cmdOK" value=" ͼ����ʾ"  class="jianbian" onclick="javascript:viewGraphic();">
									  &nbsp;&nbsp;&nbsp;&nbsp; </div></TD>
							</TR>
					  </table>
		</td>
	  </tr>
	
	<TR>
      <TD bgcolor="#FFFFFF" colspan="<%=colnum%>" ><table width="100%" height="30"  border="0" cellpadding="0" cellspacing="0" class="blue_gargtd">
          <tr> 
            <td width="162" align="center" class="title_bigwhite"> ͳ����Ϣ</td>
            <td align="left"><span class="text">&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> 
              �������豸<font color=red><%=ipMap.get(str_device_id)%></font> <%=str_day%> �˿�����ͳ�Ʊ��� 
              </span></td>
          </tr>
        </table></TD>
    </TR>
    <TR> 
      <TD> <TABLE width="100%" border=0 cellpadding=2 cellspacing=1 id="topTable">
          <TR bgcolor="#FFFFFF"> 
            <td bgcolor="#FFFFFF" class="blue_td">�� ��</td>
            <td bgcolor="#FFFFFF" class="blue_td">�˿�����</td>
            <%
			String tmp;
			for(int i=0; i<colnum-2; i++){
				out.println("<td class=blue_td  nowrap>" + map.get(arr_kind[i]) + "</td>");
			}
			map.clear();
		  %>
          </TR>
          <%
			if(fields == null){
		  %>
          <TR bgcolor="#FFFFFF"> 
            <TD height="22" colspan=<%=colnum%>  bgcolor="#FFFFFF"> �豸&nbsp;<font color=red><%=ipMap.get(str_device_id)%></font>&nbsp;<%=str_day%>&nbsp;��ѯ���ݲ����� 
              <input type="hidden" id="url" name="url" value="./template/visualman_sys_day_avg.jsp">	
            </TD>
         </TR>
          <%
		    }
			else{
				String show_kind;
				double dbluse = 0.0D;
				int j = 0;				
				while(fields != null){
					out.println("<TR><TD bgcolor=#FFFFFF nowrap>"+ fields.get("ifindex") +"</TD>");
					//out.println("<TD class=column>"+ arr_ifnamedefined[j] +"</TD>");
					out.println("<TD bgcolor=#FFFFFF  >"+ fields.get("ifdescr")+"/"+fields.get("ifname")+"/"+fields.get("ifnamedefined") +"</TD>");
					tmp = "1";
					String ifnamedifined=(String)fields.get("ifnamedefined");
					if(ifnamedifined!=null)
					{
						ifnamedifined=ifnamedifined.replaceAll("#","%23");
					}
					for(int i=0; i<colnum-2; i++){
						show_kind = arr_kind[i];
						//show_kind = show_kind.toUpperCase();
						if(Double.parseDouble((String)fields.get("if_real_speed")) != 0){
							tmp = (String)fields.get("if_real_speed");
						}
						if(show_kind.equals("u1")){
							dbluse = Double.parseDouble((String)fields.get("ifinoctetsbps"))/Double.parseDouble(tmp);
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u2")){
							dbluse = Double.parseDouble((String)fields.get("ifoutoctetsbps"))/Double.parseDouble(tmp);
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u3")){
							dbluse = Double.parseDouble((String)fields.get("ifinoctetsbpsmax"))/Double.parseDouble(tmp);							
							out.println("<TD  bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u4")){
							dbluse = Double.parseDouble((String)fields.get("ifoutoctetsbpsmax"))/Double.parseDouble(tmp);							
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else{
							String value=(String)fields.get(show_kind);
							//1����M;2����G
							String unitValue="0";
							if(value.length()>12)
							{
								unitValue="3";
							}
							else if(value.length()>9)
							{
								unitValue="2";
							}
							else if(value.length()>6)
							{
								unitValue="1";
							}
							if(show_kind.equals("ifinoctetsbps") || show_kind.equals("ifinoctets")|| show_kind.equals("ifoutoctetsbps")|| show_kind.equals("ifoutoctets") || show_kind.equals("ifinoctetsbpsmax") || show_kind.equals("ifoutoctetsbpsmax"))
							{
								out.println("<TD bgcolor=#FFFFFF  nowrap><A HREF='portDetail_hour.jsp?dev_id="+ str_device_id.trim() +"&port="+ ((String)fields.get("ifindex")).trim() +"&kind="+ show_kind +"&time="+ str_day_start +"&desc="+ifnamedifined+"&unit="+unitValue+"' onclick='return showopen()' target='_blank'>"+  StringUtils.formatString(Double.valueOf((String)fields.get(show_kind)).doubleValue()/l_unit,6) +"</A></TD>");					
							}
							else
								out.println("<TD  bgcolor=#FFFFFF   nowrap><A HREF='portDetail_hour.jsp?dev_id="+ str_device_id.trim() +"&port="+ ((String)fields.get("ifindex")).trim() +"&kind="+ show_kind +"&time="+ str_day_start +"&desc="+ifnamedifined+"&unit=-1' onclick='return showopen()' target='_blank'>"+ (String)fields.get(show_kind) +"</A></TD>");
						}
					}					
					j++;
					out.println("</TR>");
					fields = cursor.getNext();
				} 
				
			}
				map.clear();

		  %>
        </TABLE>
		</TD>
    </TR>
  </TABLE>
  </DIV>
</FORM>

<SCRIPT LANGUAGE="JavaScript" id="idChildFun">
<!--
function showit(){
	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	
	var height = 200;
	var width = 400;
	var w = window.open(page,"ss","width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
	w.moveTo((screen.width-width)/2,(screen.height-height)/2);	
}

function showopen(){
	page = event.srcElement.href;
	window.open(page,"","left=20,top=20,width=500,height=400,resizable=yes,scrollbars=yes,status=yes");
	return false;
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView1) == "object"){
	//alert(idLayer.innerHTML);
	parent.idLayerView1.innerHTML = idLayer.innerHTML;
	//parent.idParentFun.text = idChildFun.innerHTML;
}
//-->
</SCRIPT>