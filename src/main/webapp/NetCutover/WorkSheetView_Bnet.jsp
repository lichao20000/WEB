<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%
            request.setCharacterEncoding("GBK");
            //----------------------���ع��� add by YYS 2006-9-24 ----------------
            // ��ȡ��������
            String city_id = curUser.getCityId();
            SelectCityFilter City = new SelectCityFilter(request);
            String city_name = City.getNameByCity_id(city_id);
            String strCityList = City.getSelfAndNextLayer(true, city_id, "");
            //-------------------------------------------------------------------
            Date dt = new Date();
            long lms = dt.getTime();
            
            String pageURL = "worksheet_xml_Bss.jsp";//bss����
            if(LipossGlobals.getLipossProperty("InstArea.ShortName").equals("gd_dx")){
            	pageURL = "worksheet_xml_Bnet.jsp";
            }
%>

<%@ include file="../head.jsp"%>

<%@page import="java.util.Date"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var lms_now = parseInt("<%=lms%>");	//��ǰʱ��
var node_number = 30;		//��ʾ�ڵ���
var subobj_cur=null;		//��ǰѡ�е��ӽڵ����;
var objcn_cur = null;		//��ǰ�ڵ��className
var IsPoll = true;//false; //�Ƿ���ѯ

// ----------------------������״���ڽṹ���� modify by YYS 2006-10-9 --------------
//��ԭ��״���ڽṹ�滻Ϊ��������ʽ

//���ɽڵ�HTML add by YYS 2006-10-9
//��ԭ��״���ڽṹ�滻Ϊ��������ʽ
// ------------------------------------------------------------------------------
function initOselect(){
	var sHtml = "";
	var dt,year,month,day;
	var lms_cur = parseInt(lms_now);
	//��ʼ�����нڵ�
	for(var i=0;i<node_number;i++){
		dt = new Date(lms_cur);
		year	= dt.getYear();
		month	= dt.getMonth()+1;
		day		= dt.getDate();
		addOption(year,month,day);
		lms_cur = lms_cur - 24*3600*1000;
	}
	
	//���߷�������ǰʱ���ʼ��������ѯҳ��
	dt = new Date(lms_now);
	year	= dt.getYear();
	month	= dt.getMonth()+1;
	day		= dt.getDate();
	dt = new Date(month+"/"+day+"/"+year);
	refreshData(dt.getTime()/1000);
}

function addOption(year,month,day){
	var nodeStr = year+"-"+month+"-"+day;
	var dt = new Date(month+"/"+day+"/"+year);
	var lms = dt.getTime()/1000;
	
	var oSselect = document.all("queryDate");
	var oOption = document.createElement("OPTION");
	oSselect.options.add(oOption);
	oOption.innerText = nodeStr;
	oOption.value = lms;
}

function refreshData(lms_now){
	
	var obj;
	var lms_start;
	var pageurl = "<%= pageURL%>";
	if(lms_now==null){
		//alert("null");
		obj = event.srcElement;
		lms_start = obj.options[obj.selectedIndex].value;
	}else{//ҳ���ʼ��ʱ����ʹ��
		//alert(lms_now);
		lms_start = lms_now;
	}
	
	var page = pageurl + "?start="+lms_start+"&refresh="+Math.random();
	if(IsPoll) page += "&polltime="+getPolltime();
	showMsgDlg();
	reSetCityFilter();
	document.all("childFrm").src = page;

}

function getPolltime(){
	var o = document.frm;
	for(var i=0;i<o.polltime.length;i++){
		if(o.polltime[i].checked)
			return o.polltime[i].value;
	}
	return 5;
}


function reloadtime(v){
	page = document.all("childFrm").src;
	pos = page.indexOf("&polltime=");
	tmp = page.substring(0,pos);
	page = tmp + "&polltime="+ v;
	//alert(page)
	showMsgDlg();
	document.all("childFrm").src = page;
}

//----------------------���ع��� add by YYS 2006-9-24 ----------------
var user_city_id = "<%=city_id%>";//�û��������ر��
var city_id = "<%=city_id%>";//�û���ѡ�е����ر��
function showChild(parname){
	var obj = event.srcElement;
        
	if(parname=='city_id'){
		city_id = obj.options[obj.selectedIndex].value;
		if(city_id!=-1){
			if(user_city_id != city_id){
				document.all("childFrm3").src = "cityFilter.jsp?city_id="+city_id;
			}else{
				document.all("my_city<%=city_id%>").innerHTML = "";
				document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
			}
			document.all("hid_city_id").value = city_id;
		}else
			alert("��ѡ��һ����Ч������");
	}
}

function reSetCityFilter(){
	document.all("my_city<%=city_id%>").innerHTML = "";
	document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
	document.all("initCityFilter").innerHTML = "<%=strCityList%><span id='my_city<%=city_id%>'></span>";
}
//--------------------------------------------------------------------



function reloadfilter(){
	
	//----------------------���ع��� add by YYS 2006-9-24 ----------------
	var ifcontainChild;//�Ƿ������������
	for(var i=0;i<document.all("ifcontainChild").length;i++){
     	if(document.all("ifcontainChild")[i].checked){
            ifcontainChild=document.all("ifcontainChild")[i].value;
           break;
      	}
   	}
   	//--------------------------------------------------------------------
	var s1,s2;
	page = document.all("childFrm").src;
	//�滻ˢ�������
	pos1 = page.indexOf("&refresh=");
	pos2 = page.indexOf("&polltime=");
	s1 = page.substring(0,pos1);
	
	if(pos2==-1) s2="";
	else s2=page.substring(pos2,page.length);
	
	//----------------------���ع��� modify by YYS 2006-9-24 --------------
	page = s1+"&refresh="+Math.random()+"&ifcontainChild="+ ifcontainChild +"&city_id="+city_id+s2;
	
	//--------------------------------------------------------------------

	document.all("childFrm").src = page;
}

function initialize(){
	initOselect();
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

function wsState(){
	var rows = idTable.rows;
	var total = 0,success =0,error=0;
	for(var i=1;i<rows.length;i++){
		if(rows[i].cells.length<2) continue;
		total += 1;
		if(rows[i].value != null && rows[i].value == 0){	
			success += 1;
		}else{
			error +=1;
		}
	}
	idDesc.innerHTML = "�ܹ�������<b>"+ total +"</b>  �ɹ���������<b><font color=green>"+ success +"</font></b>  ʧ�ܹ�������<b><font color=red>"+ error +"</font></b>";
}
//-->
</SCRIPT>

<link rel="stylesheet" href="../css/tree.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: ����">���Եȡ�����������</span></td>
	</tr>
</table>
</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD align="center">
		<TABLE border=0 cellspacing=2 cellpadding=4 width="100%">
			<TR>
				<TD align=center valign=top>
				<TABLE border=0 cellpadding=0 cellspacing=0 width="100%">
					<TR bgcolor="">
						<TD id="idDesc">&nbsp;</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD><FORM name="frm2" style="margin:0px">��ѡ��ɸѡ���أ�
						<span id="initCityFilter"><%=strCityList%> <span id='my_city<%=city_id%>'></span></span>
						&nbsp;��ǰ��ѡ����Ϊ:<span id=city_name><font color='red'><%=city_name%></font></span>
						&nbsp;���������¼�����: 
						<input type="radio" name='ifcontainChild' value='1' checked>�� 
						<input type="radio" name='ifcontainChild' value='0'>��
						<input type="hidden" name = 'hid_city_id' value="<%=city_id%>">
						<INPUT TYPE="button" NAME="btnFilter" value=" �� �� "
							onclick="reloadfilter()" class=jianbian></FORM>
						</TD>
					</TR>
					<TR>
						<TD valign=top>
						<TABLE>
							<TR WIDTH="" HEIGHT="">
								<!-- ----------------------������״���ڽṹ����������� add by YYS 2006-10-9 -------------- -->
								<td>��ѡ�񹤵����ڣ�&nbsp;
								<select name="queryDate" onchange="refreshData()"></select>&nbsp;&nbsp;
								</td>
								<!-- -------------------------------------------------------------------------------------- -->
								<TD>
								<TABLE id="idHelp">
									<TR>
										<TD>
										<FORM name="frm" style="margin:0px">ˢ��Ƶ��ѡ��&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="3"
											onclick="reloadtime(this.value)">3����&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="5" checked
											onclick="reloadtime(this.value)">5����&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="10"
											onclick="reloadtime(this.value)">10����&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="15"
											onclick="reloadtime(this.value)">15����&nbsp;&nbsp; <INPUT
											TYPE="radio" NAME="polltime" value="30"
											onclick="reloadtime(this.value)">30����</FORM>
										</TD>
									</TR>
								</TABLE>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR>
						<TD valign=top>
						<div id="idList" style="overflow:auto;width:800px;height:300px;"></div>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript" src="Js/rightMenu.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
if (document.all&&window.print){
    ie5menu.className="cMenu"
    document.body.onclick=hidemenuie5
}
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
initialize();
//-->
</SCRIPT>
