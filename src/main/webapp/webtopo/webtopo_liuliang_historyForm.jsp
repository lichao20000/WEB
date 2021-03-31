<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.linkage.litms.webtopo.Flux_Config" %>
<%
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	String device_name = request.getParameter("device_name");
	String device_ip = request.getParameter("device_ip");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
	SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-M");
	SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfall=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	Date d=new Date();    
	String nowstart=sdfall.format(new Date(d.getTime()-2*60*60*1000));
    String nowend=sdfall.format(new Date(d.getTime()-60*60*1000));
    
	String yestorday=sdf.format(new Date(d.getTime()-86400000));
	String lastweek=sdf.format(new Date(d.getTime()-86400000*7));
	String lastmonth=sdfm.format(new Date(d.getTime()-86400000*31));
	String lastYear = sdfy.format(new Date(d.getTime()-86400000*365));
	String []lastmonth_tmp = lastmonth.split("-");
	String lastmonth_tmp2 = lastmonth_tmp[0]+"-"+ String.valueOf(Integer.parseInt(lastmonth_tmp[1])-1);
	Flux_Config fluxCon = new Flux_Config();
	Cursor cursor_port_list = fluxCon.getPortList(device_id);
	Map map_port_list = cursor_port_list.getNext();
	
	String ifindex = null;
	String ifportip = null;
	String ifname = null;
	String ifdescr = null;
	String ifnamedefined = null;
	String getway = null;
	String key = null;
	String view = null;
	String if_real_speed = null;
	

%>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
function searchType() 
{
	var type=0;	
	
	for(var i=0;i<document.frm.ReportForms.length;i++){
		
		if(document.frm.ReportForms[i].checked){
            type=document.frm.ReportForms[i].value;
            break;
		}
  }
    document.getElementById("changeStart").style.display="block";
    document.getElementById("detaildate").style.display="none";
    if(type==0){
        document.getElementById("detaildate").style.display="block";
        document.getElementById("changeStart").style.display="none";
    }
	else if(type==1){
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" readonly=true class=bk value=\"<%=yestorday%>\">" 
					+ "<input TYPE=\"button\" value=\"��\" class=\"btn\" onclick=\"new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')\">" 
					+ "<input type=\"hidden\" name=\"hidday\"></span>";
	
	}
	else if(type==2){
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" readonly=true class=bk value=\"<%=lastweek%>\">" 
					+ "<input TYPE=\"button\" value=\"��\" class=\"btn\" onclick=\"new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')\">" 
					+ "<input type=\"hidden\" name=\"hidday\"></span>";
		

	}
	else if(type==3){
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" readonly=true class=bk value=\"<%=lastmonth_tmp2%>\">" 
					+ "<input TYPE=\"button\" value=\"��\" class=\"btn\" onclick=\"new WdatePicker(document.frm.day,'%Y-%M',false,'whyGreen')\">" 
					+ "<input type=\"hidden\" name=\"hidday\"></span>";		
		
	}
	else if(type==4){
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" readonly=true class=bk value=\"<%=lastYear%>\">" 
					+ "<input TYPE=\"button\" value=\"��\" class=\"btn\" onclick=\"new WdatePicker(document.frm.day,'%Y',false,'whyGreen')\">"  
					+ "<input type=\"hidden\" name=\"hidday\"></span>";	
	}
}

function selectAll(param)
{
	var len = frm.elements.length;
	var i;
	
	if (param == "select_fluxtype")
	{
		for (i=0;i<len;i++ )
		{
			if (frm.elements[i].name=='kind') {
				frm.elements[i].checked = frm.select_fluxtype.checked;
			}
		}
	}
	else if (param == "select_devport")
	{
		for (i=0;i<len;i++ )
		{
			if (frm.elements[i].name=='port') {
				frm.elements[i].checked = frm.select_devport.checked;
			}
		}	
	}
}

function SelectKind()
{
	var len = frm.elements.length;
	var i;
	var flag;

	for(i=0; i<len; i++){
		if(frm.elements[i].name=='kind')
		{
			if (frm.elements[i].checked)
			{
				flag=true;
				break;
			}
		}		
	}

	if(!flag)
	{	
		return false;	
	}
	else
	{
		return true;
	}
}


function SelectPort()
{
	var len = frm.elements.length;
	var i;
	var flag;

	for(i=0; i<len; i++){
		if(frm.elements[i].name=='port')
		{
			if (frm.elements[i].checked)
			{
				flag=true;
				break;
			}
		}		
	}

	if(!flag)
	{	
		return false;	
	}
	else
	{
		return true;
	}
}

function CheckForm() 
{
	if (frm.day.value.trim().length<=0)
	{
		alert("���ڲ���Ϊ�գ�");
		return false;
	}
	
	if (!SelectKind())
	{
		alert("��ѡ���������ͣ�");
		return false;
	}

	if (!SelectPort())
	{
		alert("��ѡ���豸�˿ڣ�");
		return false;
	}

	var type=0;	
	
	for(var i=0;i<document.frm.ReportForms.length;i++){
		
		if(document.frm.ReportForms[i].checked){
            type=document.frm.ReportForms[i].value;
            break;
		}
	}
	frm.action = "webtopo_liuliang_history.jsp?type=" + type;
	frm.all("reportTabelView").innerHTML="�����������ݣ���ȴ�......<img src=../images/loading.gif>";
	frm.all("reportGraphicslView").innerHTML="";
	frm.submit();

}
function CheckFormPRT() 
{
	if (frm.day.value.trim().length<=0)
	{
		alert("���ڲ���Ϊ�գ�");
		return false;
	}
	
	if (!SelectKind())
	{
		alert("��ѡ���������ͣ�");
		return false;
	}

	if (!SelectPort())
	{
		alert("��ѡ���豸�˿ڣ�");
		return false;
	}

	var type=0;	
	
	for(var i=0;i<document.frm.ReportForms.length;i++){
		
		if(document.frm.ReportForms[i].checked){
            type=document.frm.ReportForms[i].value;
            break;
		}
	}
	var prtPage = "webtopo_liuliang_history_prt.jsp?type=" + type+"&deviceName="+"<%=device_name%>"+"&deviceIp="+"<%=device_ip%>";
	//alert(prtPage);
	frm.action = prtPage;
	frm.submit();

}

function Grphis()
{	
	var type = 0;
	
	if (document.frm.curvekind.length == null)
	{
		if(document.frm.curvekind.checked){
            type = document.frm.curvekind.value;
		}
	}
	else
	{
			for(var i=0;i<document.frm.curvekind.length;i++){
		
				if(document.frm.curvekind[i].checked){
								type = document.frm.curvekind[i].value;
								break;
				}
			}
	}
	
	if (type == 0)
	{
		alert("��ѡ���������ͣ�");
		return false;
	}
	frm.action = "webtopo_liuliang_historyGrap.jsp?type=" + type;
	frm.all("reportGraphicslView").innerHTML="�����������ݣ���ȴ�......<img src=../images/loading.gif>";
	frm.submit();
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../Js/edittable.js"></SCRIPT>

<%@ include file="../toolbar.jsp"%>

<form name="frm" method="post" action="" target="childFrm">
<table width="98%"  border="0" cellspacing="0" cellpadding="0" align="center">
  <tr><td bgcolor=#999999>
	<table width="100%"  border="0" cellspacing="1" cellpadding="2">
	  <TH colspan="2">   <%=device_name%><font color="red">[</font><%=device_ip%><font color="red">]</font>�˿�����ͳ��</TH>
	  <tr>
		<td width="20%" class=column1 align="right">��������</td>
		<td width="80%" class=column1 align="left">
		  <input name="ReportForms" type="radio" value="0" onClick="javascript:searchType();"> ��ϸ
		  <input name="ReportForms" type="radio" value="1" checked onClick="javascript:searchType();"> �ձ��� 
		  <input name="ReportForms" type="radio" value="2" onClick="javascript:searchType();"> �ܱ��� 
		  <input name="ReportForms" type="radio" value="3" onClick="javascript:searchType();"> �±��� 
		  <input name="ReportForms" type="radio" value="4" onClick="javascript:searchType();"> �걨�� 	
		</td>
	  </tr>
	  <tr>
		<td class="column" align="right">&nbsp;&nbsp;����ʱ��</td>
		<td class="column" align="left">
		  <span id="changeStart">
		  <INPUT TYPE="text" NAME="day" class=bk readonly=true value="<%=yestorday%>"> 
		  <input TYPE="button" value="��" class="btn"
            onclick="new WdatePicker(document.frm.day,'%Y-%M-%D',false,'whyGreen')">
          <INPUT TYPE="hidden" name="hidday">
		  </span>
		  <span id="detaildate" style="display: none;">
		      <input type="text" name="starttime" class=bk readonly=true value="<%=nowstart%>">
		      <input TYPE="button" value="��" class="btn"
            onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')">
            <input type="text" name="endtime" class=bk readonly=true value="<%=nowend%>">
              <input TYPE="button" value="��" class="btn"
            onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')">
		  </span>
		</td>
	  </tr>
	  <tr>
			<td class="column" align="right"><input name="select_fluxtype" type="checkbox" onclick="javascript:selectAll('select_fluxtype')">&nbsp;&nbsp;��������
				
			</td>
			<td class="column">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr> 
						<td>
							<input name="kind" type="checkbox" value="ifinoctetsbps" checked>
							��������(bps)&nbsp;</td>
						<td> <input name="kind" type="checkbox" value="ifindiscardspps">
							���붪����&nbsp;</td>
						<td> <input name="kind" type="checkbox" value="ifinerrors">
							����������&nbsp; </td>
						<td> <input name="kind" type="checkbox" value="ifinoctetsbpsmax">
							�����ֵ</td>
						<td> <input name="kind" type="checkbox" value="ifinucastpktspps">
							ÿ�����뵥������ </td>
					</tr>
					<tr> 
						<td><input name="kind" type="checkbox" value="ifoutoctetsbps" checked>
							��������(bps)&nbsp;</td>
						<td><input name="kind" type="checkbox" value="ifoutdiscardspps">
							����������&nbsp;</td>
						<td><input name="kind" type="checkbox" value="ifouterrors">
							�����������</td>
						<td><input name="kind" type="checkbox" value="ifoutoctetsbpsmax">
							������ֵ</td>
						<td><input name="kind" type="checkbox" value="ifinnucastpktspps">
							ÿ������ǵ�������</td>
					</tr>
					<tr> 
						<td><input name="kind" type="checkbox" value="ifinerrorspps">
							��������</td>
						<td><input name="kind" type="checkbox" value="ifinoctets">
							�����ֽ���</td>
						<td><input name="kind" type="checkbox" value="ifindiscards">
							���붪������&nbsp;</td>
						<td><input name="kind" type="checkbox" value="ifoutucastpktspps">
							ÿ��������������&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr> 
						<td><input name="kind" type="checkbox" value="ifouterrorspps">
							���������</td>
						<td><input name="kind" type="checkbox" value="ifoutoctets">
							�����ֽ���&nbsp;</td>
						<td><input name="kind" type="checkbox" value="ifoutdiscards">
							������������&nbsp;</td>
						<td><input name="kind" type="checkbox" value="ifoutnucastpktspps">
							ÿ�������ǵ�������</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
					  <td><input name="kind" type="checkbox" value="ifinunknownprotospps">
							ÿ��������δ֪Э�����</td>
						<td><input name="kind" type="checkbox" value="ifoutqlenpps">
							ÿ���������д�С</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</td>
	  </tr>
		<tr>
			<td class="column" align="left"><input name="select_devport" type="checkbox" onclick="javascript:selectAll('select_devport')" checked>&nbsp;&nbsp;�豸�˿�
				
			</td>
			<td class="column">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<%
					while (map_port_list != null) {
						ifindex = (String)map_port_list.get("ifindex");
						ifportip = (String)map_port_list.get("ifportip");
						ifname = (String)map_port_list.get("ifname");
						ifdescr = (String)map_port_list.get("ifdescr");
						ifnamedefined = (String)map_port_list.get("ifnamedefined");
						getway = (String)map_port_list.get("getway");
						if_real_speed = (String)map_port_list.get("if_real_speed");
						switch(Integer.parseInt(getway)) {
							case 1: {
								key = device_id + "##" + getway + "##" + ifindex;
								break;
							}
							case 2: {
								key = device_id + "##" + getway + "##" + ifdescr;
								break;
							}
							case 3:	{
								key = device_id + "##" + getway + "##" + ifname;
								break;
							}
							case 4:	{
								key = device_id + "##" + getway + "##" + ifnamedefined;
								break;
							}
							case 5:	{
								key = device_id + "##" + getway + "##" + ifportip;
							}
						}
					
						view = ifindex + " | " + ifnamedefined + " | " + ifdescr + " | " + ifname;
						out.println("<tr>");
						out.println("<td><input name=\"port\" type=\"checkbox\" value=\"" + key + "##" + ifindex + "##" + ifdescr + "##" + ifname + "##" + ifportip + "##" + if_real_speed + "##" + ifnamedefined + "\" checked>" + view + "</td>");
						out.println("</tr>");
						map_port_list = cursor_port_list.getNext();
					}
				%>
				</table>
			</td>
		</tr>
	  <tr>
	  	<td class='green_foot' align="right" colspan="2">
			<IMG SRC="../images/excel.gif" WIDTH="16" HEIGHT="16" BORDER="0" ALT="����EXCEL"  onclick="javascript:CheckFormPRT()">&nbsp;&nbsp;&nbsp;&nbsp;
	  		<input type="button" value=" �� ѯ " onclick="javascript:CheckForm()" class="jianbian">
			&nbsp;&nbsp;
			<input type="reset" value=" ȡ �� " class="jianbian">
	  	</td>
	  </tr>
  	</table>
	</td>
  </tr>
  <tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none;width:500;height:500"></IFRAME></td></tr>
</table>
<DIV id="reportTabelView" align="center"></div>
<DIV id="reportGraphicslView" align="center"></div>
<br><br>
</form>

