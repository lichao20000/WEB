<%@page import="java.net.URLDecoder"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.system.Role"%>
<%@ page import="com.linkage.litms.system.SysManage"%>
<%@ page import="java.util.List"%>
<%@ page import="com.linkage.litms.system.dbimpl.RoleSyb"%>
<%@ page import="com.linkage.litms.system.UserMap"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="com.linkage.litms.system.UserMap"%>
<%@page import="java.lang.StringBuilder"%>
<%@page import="java.util.Random"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<jsp:useBean id="qryp" scope="page" class="com.linkage.litms.common.database.QueryPage" />
<!-- jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/-->


<%
	org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this
		.getClass());

	String psw = "123qwe!@#";
	
	if (LipossGlobals.inArea("ah_dx")) {
		psw = "system@517";
	}else if(LipossGlobals.inArea("hb_lt")){// 20200603
		psw = "Rms123456";
	}else if(LipossGlobals.inArea("jl_lt")){
		psw = "Rms@201912";
	}
	
	if(LipossGlobals.inArea("nx_dx") )
	{
		char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray(); 
		StringBuilder sb = new StringBuilder();        
		Random r = new Random();         
		for (int x = 0; x < 8; ++x) 
		{            
			sb.append(charr[r.nextInt(charr.length)]);        
		}       
		psw = sb.toString();	
	}

	String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
	request.setCharacterEncoding("GBK");
	String strSQL = "";
	String username = request.getParameter("username");
	if (username != null) {
		// ���ս����û���Ҫͬ���������ݿ�����ʱ��
		if (LipossGlobals.inArea("ah_dx")) {
			UserMap.getInstance().deleteLockOutdatedUser(username);
		}
		else {
			username = URLDecoder.decode(username,"UTF-8");
			UserMap.getInstance().deleteLockUserMap(username);
		}
	}
	String stroffset = request.getParameter("offset");
	int pagelen = 15;
	int offset;
	String query = request.getParameter("query");
	String content = request.getParameter("content");
	String hiddencontent = request.getParameter("hiddencontent");
	String s1 = "";
	String s2 = "";
	String s3 = "";
	String s4 = "";
	String s5 = "";
	String disabled = "disabled";
	String selName = "ѡ��";
	if (query == null || (query != null && query.equals("all")))
	{
		s1 = "selected";
		s2 = "";
		s3 = "";
		s4 = "";
		s5 = "";
		hiddencontent = "";
		content = "";
	}
	else if (query != null && query.equals("acc_loginname"))
	{
		s1 = "";
		s2 = "selected";
		s3 = "";
		s4 = "";
		s5 = "";
		hiddencontent = "";
	}
	else if (query != null && query.equals("per_name"))
	{
		s1 = "";
		s2 = "";
		s3 = "selected";
		s4 = "";
		s5 = "";
		hiddencontent = "";
	}
	else if (query != null && query.equals("role_id"))
	{
		s1 = "";
		s2 = "";
		s3 = "";
		s4 = "selected";
		s5 = "";
		disabled = "";
		selName = " ��ɫ" + selName + " ";
	}
	else if (query != null && query.equals("group_oid"))
	{
		s1 = "";
		s2 = "";
		s3 = "";
		s4 = "";
		s5 = "selected";
		disabled = "";
		selName = "�û���" + selName;
	}
	
	SysManage sm = new SysManage();
	StringBuffer sb = new StringBuffer();
	//StringBuffer sbRoleId = new StringBuffer();  // �洢��¼�û��Ľ�ɫID���ӽ�ɫID
	List list = sm.getAllCreators(String.valueOf(user.getId()));
	for (int i = 0; i < list.size(); i++)
	{
		sb.append(list.get(i).toString() + ",");
	}
	sb.append(String.valueOf(user.getId()));
	
	// ��ȡ��ǰ��¼�û��Ľ�ɫID���Լ��ӽ�ɫID add by zhangchy 2012-09-04
	//Cursor cursor= roleManage.getAllRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
	//int count = cursor.getRecordSize();
	//Map fields = cursor.getNext();
	//while(fields != null){
	//	sbRoleId.append(fields.get("role_id")).append(",");
	//	fields = cursor.getNext();
	//}
	//sbRoleId.append(String.valueOf(user.getRoleId()));
	
	Role userRole = new RoleSyb(Integer.parseInt(String.valueOf(user.getRoleId())));
	
	//�ж��Ƿ��ǵ�һ�ν���ҳ��
	if ((stroffset == null && query == null)
			|| (query != null && query.equals("all")) || (content == null)
			|| (content.equals("")))
	{
		strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
				 "  from tab_persons a,tab_accounts b "+
				 " where b.acc_loginname != 'admin' "+
				 "   and a.per_acc_oid = b.acc_oid "+
				 "   and b.acc_oid in("	+ sb.toString() + ") "+
				 " order by a.per_acc_oid";  
		
		//  add by zhangchy 2012-09-04  ע��by zhangchy 2012-09-17 
		//strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip "+
		//         "  from tab_persons a,tab_accounts b " + 
		//         " where 1=1 " + 
		//         "   and a.per_acc_oid = b.acc_oid " + 
		//         "   and a.per_acc_oid in (select acc_oid from tab_acc_role where role_id in(" + sbRoleId.toString() + ")) " + 
		//         " order by a.per_acc_oid ";
		
		//if (user.isAdmin())  // ע��  by zhangchy 2012-09-04 
		if("system".equals(userRole.getRoleName().toLowerCase()))
		{
			strSQL = "select b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip ,a.per_jobtitle,a.per_city"+
					 "  from tab_persons a,tab_accounts b "+
					 " where a.per_acc_oid = b.acc_oid "+
					 " order by a.per_acc_oid";
		}
		session.putValue("accountSQL", strSQL);
	}
	//�ж��ǲ�ѯ����ҳ��,�����ǽ�ɫ��ѯ�ֲ����û����ѯ
	else if (query != null && query.length() > 0 && !query.equals("role_id")
			&& !query.equals("group_oid"))
	{
		strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
				 "  from tab_persons a,tab_accounts b "+
				 " where b.acc_loginname != 'admin' "+
				 "   and a.per_acc_oid = b.acc_oid "+
				 "   and b.acc_oid in("	+ sb.toString()+ ") "+
				 "   and " + query + " like '%" + content + "%' "+
				 " order by a.per_acc_oid";
		
		//if (user.isAdmin())
		if("system".equals(userRole.getRoleName().toLowerCase()))
		{
			strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
			         "  from tab_persons a,tab_accounts b "+
			         " where b.acc_loginname != 'admin' "+
			         "   and a.per_acc_oid = b.acc_oid "+
			         "   and " + query + " like '%" + content + "%' "+
			         " order by a.per_acc_oid";
		}
		
		// add by zhangchy 2012-09-04  ע�� by zhangchy 2012-09-17
		//strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip " + 
		//         "  from tab_persons a,tab_accounts b " + 
		//         " where 1=1 " + 
		//         "   and a.per_acc_oid = b.acc_oid " + 
		//         "   and b.acc_oid in(select acc_oid from tab_acc_role where role_id in(" + sbRoleId.toString() + ")) " + 
		//         "   and  "	+ query + " like '%" + content + "%' " + 
		//         " order by a.per_acc_oid";
		
		session.putValue("accountSQL", strSQL);
	}
	//�ж��ǲ�ѯ����ҳ�棬�ǽ�ɫ��ѯ
	else if (query != null && query.length() > 0 && query.equals("role_id"))
	{
		strSQL = "select b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
		         "  from tab_persons a,tab_accounts b "+
		         " where b.acc_loginname!='admin' "+
		         "   and a.per_acc_oid = b.acc_oid "+
		         "   and b.acc_oid in("+ sb.toString()+ ") "+
		         "   and a.per_acc_oid in  ( select acc_oid from tab_acc_role where role_id=" + hiddencontent + ") "+
		         " order by a.per_acc_oid";
		
		//if (user.isAdmin())
		if("system".equals(userRole.getRoleName().toLowerCase()))
		{
			strSQL = "select b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
					 "  from tab_persons a,tab_accounts b "+
					 " where b.acc_loginname != 'admin' "+
					 "   and a.per_acc_oid = b.acc_oid "+
					 "   and a.per_acc_oid in ( select acc_oid from tab_acc_role where role_id=" + hiddencontent + ") "+
					 " order by a.per_acc_oid";
		}
		session.putValue("accountSQL", strSQL);
	}
	//�ж��ǲ�ѯ����ҳ�棬���û����ѯ
	else if (query != null && query.length() > 0 && query.equals("group_oid"))
	{
		strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city " + 
		         "  from tab_persons a,tab_accounts b " + 
		         " where b.acc_loginname != 'admin' "+
		         "   and a.per_acc_oid = b.acc_oid "+
		         "   and b.acc_oid in("	+ sb.toString()+ ") "+
		         "   and a.per_acc_oid in  ( select acc_oid from tab_acc_group where group_oid="
		+ hiddencontent + ") order by a.per_acc_oid";
		
		//if (user.isAdmin())
		if("system".equals(userRole.getRoleName().toLowerCase()))
		{
			strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip,a.per_jobtitle,a.per_city "+
			         "  from tab_persons a,tab_accounts b "+
			         " where b.acc_loginname != 'admin' "+
			         "   and a.per_acc_oid = b.acc_oid "+
			         "   and a.per_acc_oid in  ( select acc_oid from tab_acc_group where group_oid="
			+ hiddencontent + ") order by a.per_acc_oid";
		}
		// add by zhangchy 2012-09-04 ���ݽ�ɫ��ѯ�û���ֻ�ܲ鿴����¼�û�������ɫ�����ӽ�ɫ���ʺ���Ϣ
		//strSQL = "select b.creator,b.acc_loginname,a.per_acc_oid,a.per_name,a.per_email,a.per_dep_oid,b.acc_login_ip "+
		//	     "  from tab_persons a,tab_accounts b "+
		//	     " where b.acc_loginname != 'admin' "+
		//	     "   and a.per_acc_oid = b.acc_oid "+
		//	     "   and a.per_acc_oid in (select acc_oid from tab_acc_role where role_id in(" + sbRoleId.toString() + ")) " + 
		//	     "   and a.per_acc_oid in (select acc_oid from tab_acc_group where group_oid=" + hiddencontent + ") "+
		//	     " order by a.per_acc_oid";
		session.putValue("accountSQL", strSQL);
	}
	//ͨ�������һҳ����
	else if (stroffset != null && query == null)
	{
		strSQL = (String) session.getValue("accountSQL");
	}
	//out.println(strSQL);
	if (stroffset == null)
		offset = 1;
	else
		offset = Integer.parseInt(stroffset);
	qryp.initPage(strSQL, offset, pagelen);
	String search = "&query=" + query + "&content=" + content + "&hiddencontent="
			+ hiddencontent + "";
	String strBar = qryp.getPageBar(search);
%>


<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/code.js"></SCRIPT>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/crypto-js.min.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
var instArea = "<%=InstArea%>";
/*���������ť*/    
function popBox() {        
	var popBox = document.getElementById("popBox");        
	var popLayer = document.getElementById("popLayer");        
	popBox.style.display = "";       
	 popLayer.style.display = "";    
 };     
 /*����رհ�ť*/   
function closeBox() {       
	var popBox = document.getElementById("popBox");      
	var popLayer = document.getElementById("popLayer");     
	popBox.style.display = "none";     
	popLayer.style.display = "none";   
}


function Init(){
	/*var strXSLSrc = "viewdata.xsl";  
	objXSLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXSLDoc.async = false;
	objXSLDoc.load( strXSLSrc );
	objXMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	objXMLDoc.async = false;
	var strXMLSrc = idList.XMLSrc;
	objXMLDoc.load( strXMLSrc );
	var sHtml = objXMLDoc.transformNode( objXSLDoc );
	idList.innerHTML = sHtml;*/

}

function ExecCMD(type){
	var oSel = document.all("user_list");
	var v    = getSelectValue(oSel);
	if(v != -1){
		switch(type){
			case 4:
				var username = getTdValue(v, 1);
				var status = getTdValue(v, 5);
				if (status.indexOf("����") >= 0)
				{
					alert("��ǰ�û�״̬����");

					return false;
				}
				
				alert(username + " �����ɹ�!");
				username = encodeURI(encodeURI(username));
				this.location = "UserList.jsp?username="+ username;
				break;
			case 3:
				<%
				if(InstArea.equals("nx_dx")||InstArea.equals("hn_lt") )
				{
				%>
					updatePWDNX(v);
				<%
				}
				else
				{
				%>
					updatePWD(v);
				<%
				}
				%>
				break;
			case 2:
				if(v==1) {
					alert("�޷�ɾ��ϵͳĬ�ϵĹ���Ա");
					break;
				}
                if(!confirm("��ȷʵҪ���õ�ɾ������������")){
                    return ;
                }
                this.location = "UserSave.jsp?action=delete&acc_oid="+ v;
				/*var page = "../Js/warning.htm";
				var vReturnValue = window.showModalDialog(page,"","dialogHeight: 217px; dialogWidth: 400px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
				if(vReturnValue){
					//To Add your code
					//alert("true");
					this.location = "UserSave.jsp?action=delete&acc_oid="+ v;
				}*/
				break;
			case 1:
				// 20200527 �ӱ���ͨҳ�汨�� CryptoJSδ���壬���ų��ӱ���ͨ
				<%
				if(!InstArea.equals("hb_lt") && !InstArea.equals("sx_lt") && !InstArea.equals("nx_dx"))
				{
				%>
					var key="itms2020";
					var keyHex = CryptoJS.enc.Utf8.parse(key);
					var encrypted = CryptoJS.DES.encrypt(v, keyHex, {
						mode: CryptoJS.mode.ECB,
						padding: CryptoJS.pad.Pkcs7
					});
					this.location = "EditUserForm.jsp?acc_oid="+ encrypted.ciphertext.toString();
				<%
				}
				else
				{
				%>
					this.location = "EditUserForm.jsp?acc_oid="+ v;
				<%
				}
				%>
				break;
			case 0:
				// To Add your code
				break;
		}
	}
	else{
		alert("��ѡ��Ҫ��������");
	}
}

function updatePWD(v){
	if(confirm("���Ҫ��ʼ�����û����룿Ĭ�������ǣ�"+"<%=psw%>")){
		this.location = "InitUserPwd.jsp?acc_oid="+ v;
	}
	else{
		return;
	}	
}

 function updatePWDNX(v){
	if(confirm("���Ҫ��ʼ�����û����룿Ĭ�������ǣ�"+"<%=psw%>")){
		//���������ַ���Ҫ����
		var psw=encodeURIComponent('<%=psw%>');
		this.location = "InitUserPwd.jsp?acc_oid="+ v+"&psw="+psw;
	}
	else{
		return;
	}
} 
 function modifyKey(){
	 	var oSel = document.all("user_list");
	 	var v  = getSelectValue(oSel);
	 	if(confirm("���Ҫ��ʼ�����û����룿")){
			this.location = "InitUserPwd.jsp?acc_oid="+ v+"&psw="+"<%=psw%>";
		}
		else{
			return;
		}
} 
function getSelectValue(o){
	if(typeof(o[0]) != "object"){
		if(o.checked) return o.value;
		else return -1;
	}

	for(var i=0; i<o.length; i++){
		if(o[i].checked){
			return o[i].value;
		}
	}

	
	return -1;
}

function showCodeName(){
	$('[id=dataTr]').each(function(){
		var codename = $(this).children("td").eq(2).html();
		$(this).children("td").eq(2).html(getCodeName('DEPARTMENT', codename));
	});
}

function getTdValue(row, cell) {
	var oTR = idList.getElementsByTagName("TR");
	for (var i = 2; i < oTR.length; i++ )
	{
		var oTD = oTR[i].childNodes;
		var td_value = oTD[0].innerHTML;
		if (td_value.indexOf("value=" + row + " ") >= 0)
		{
			return oTD[cell].innerText;
		}
	}

	return;
}


function ChangeSelect()
{
	var sel=frm.query.selectedIndex;
	frm.offset.value=1;
	switch(parseInt(sel,10))
	{
		case 3:
		{
			frm.sel.value=" ѡ���ɫ ";
			frm.sel.disabled=false;
			frm.content.value="";
			frm.hiddencontent.value="";
			break;
		}
		case 4:
		{
			frm.sel.value="ѡ���û���";
			frm.sel.disabled=false;
			frm.content.value="";
			frm.hiddencontent.value="";
			break;
		}
		default :
		{
			frm.sel.disabled=true;
			frm.content.value="";
			frm.hiddencontent.value="";
			break;
		}
	}
}

function selValue()
{
	var sel=frm.query.selectedIndex;
	var page="";
	frm.offset.value=1;
	switch(parseInt(sel,10))
	{
		case 3:
		{
			page="selRole.jsp";
			break;
		}
		case 4:
		{
			page="selGroup.jsp";
			break;
		}	
	}
	window.open(page,"","width=500,height=500,top=100,left=200")
}

function SetValue(ids,contents)
{
	frm.content.value=contents;
	frm.hiddencontent.value=ids;

}

function ToExcel() {
	var page='<s:url value='/gwms/system/systemUser!getExcel.action'/>';
	document.all("childFrm").src=page;
}
//-->
</SCRIPT>
<style type="text/css">
 table .contentTable th{background-image : none !important;}
 table .contentTable .left_th{text-align:left !important;}
</style>
<%@ include file="../toolbar.jsp"%>
<BR>
<FORM NAME="frm">
	<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
		<TR>
			<TD>
				<table width="98%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							����Ա����
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							&nbsp;&nbsp;�û�һ����ɾ����ϵͳ�������û���ͽ�ɫ���޳����û�
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="2" align="center">
										<B>��ѯ</B>
									</TH>
								</TR>
								<TR>
									<td bgcolor="#ffffff" border="1" width ="50%">
										��ѯ��ʽ
										<select name="query" class="bk"
											onchange="javascript:ChangeSelect();">
											<option value="all" <%=s1%>>
												�����û�
											</option>
											<option value="acc_loginname" <%=s2%>>
												��¼��
											</option>
											<option value="per_name" <%=s3%>>
												����
											</option>
											<option value="role_id" <%=s4%>>
												�û���ɫ
											</option>
											<option value="group_oid" <%=s5%>>
												�û���
											</option>
										</select>
										&nbsp;&nbsp;&nbsp;��ѯ����
										<input type="text" name="content" value="<%=content%>"
											class="bk">
										&nbsp;&nbsp;&nbsp;
										<input type="hidden" name="offset" value="<%=offset%>">
										<input type="hidden" name="hiddencontent"
											value="<%=hiddencontent%>">
										<input type="button" name="sel" value="<%=selName%>"
											class="btn" onclick="javascript:selValue();" <%=disabled%>>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="button" name="ls" value=" �� ѯ " class=jianbian
											onclick="javascript:frm.submit();">
									</td>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD valign=top>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0>
					<TR height="10" nowrap>
						<TD></TD>
					</TR>
					<TR>
						 <%-- <TD bgcolor="#999999" id="idList"
							XMLSrc="user_xml.jsp?offset=<%=offset%>&pagelen=<%=pagelen%>"></TD>  --%>
						 <td bgcolor=#999999>
						 <table border=0 cellspacing=1 cellpadding=2 width="100%" class="contentTable" id="idList">
						  <thead >
						  <tr bgcolor='#ffffff' >
						     <th colspan="8" class="left_th">�û��б�</th>
						   <tr>
						   <tr bgcolor="#e1eeee">
						    <th></th>
						    <th>��¼��</th>
						    <ms:inArea areaCode="sx_lt" notInMode="false">
						    <th>����</th>
						    </ms:inArea>
						    <ms:inArea areaCode="sx_lt" notInMode="true">
						    <th>����</th>
						    </ms:inArea>
						    <th>����</th>
						    <th>Email</th>
						    <th>����״̬</th>
						    <th>��½IP</th>
						    <ms:inArea areaCode="sx_lt" notInMode="false">
						  		<th>��ɫ</th>
							</ms:inArea>
							<ms:inArea areaCode="sx_lt" notInMode="true">
							  	<th>�ֹ�˾</th>
							</ms:inArea>
						   </tr>
						  </thead>
						  <tbody>
						    <%
						    if (LipossGlobals.inArea("ah_dx")) {
						    	strSQL = "select atl.*, dep.depart_name from (" + strSQL + ") atl left join tab_department dep on atl.per_dep_oid = dep.depart_id";
						    }else if(LipossGlobals.inArea("sx_lt")){
						    	strSQL = "select a.*, c.role_name,city.city_name from (" + strSQL + ") a, tab_acc_role b, tab_role c,tab_city city where a.per_city=city.city_id and a.per_acc_oid = b.acc_oid and b.role_id = c.role_id";
						    }
							Cursor cursor= DataSetBean.getCursor(strSQL,offset,pagelen);
							int count = cursor.getRecordSize();
							Map fields = cursor.getNext();
							if(count>0){
								int i = 0;
								String bgClr = "#FFFFFF";
								String accountName="";
								String departname = "δ֪";
								while(fields != null){
									if((i % 2) == 0)
										bgClr = "#FFFFFF";
									else
										bgClr = "#F6F6F6";
									accountName = (String)fields.get("acc_loginname");
									String statusNew = "����";
									if(UserMap.getInstance().checkIsLockUser(accountName)) {
										statusNew =  "����";
									} else {
										statusNew = "����";
									}
									if (LipossGlobals.inArea("ah_dx")) {
										if (!"".equals(fields.get("depart_name")) && null != fields.get("depart_name")) {
											departname = (String)fields.get("depart_name");
										}
									}
									else {
										departname = (String)fields.get("per_dep_oid");
									}
								%>
								 <tr bgcolor="<%=bgClr%>" id="dataTr">
								  <td><input type="radio" name="user_list" value="<%=fields.get("per_acc_oid")%>"></td>
								  <td><%=accountName %></td>
								  <ms:inArea areaCode="sx_lt" notInMode="false">
								  <td><%=fields.get("city_name") %></td>
								  </ms:inArea>
								  <ms:inArea areaCode="sx_lt" notInMode="true">
								  <td><%=departname %></td>
								  </ms:inArea>
								  <td><%=fields.get("per_name") %></td>
								  <td><%=fields.get("per_email") %></td>
								  <td><%=statusNew %></td>
								  <td><%=fields.get("acc_login_ip") %></td>
								  <ms:inArea areaCode="sx_lt" notInMode="false">
								  	<td><%=fields.get("role_name") %></td>
								  </ms:inArea>
								  <ms:inArea areaCode="sx_lt" notInMode="true">
								  	<td><%=fields.get("per_jobtitle") %></td>
								  </ms:inArea>
								  
								  </tr>
								<% 
								i++;
								fields = cursor.getNext();
							}
						}else{
						%>
						 <tr><td colspan='8'>û�и�������</td></tr>
						<%
						}
						%>
						  </tbody>
						 </table>
						</td> 
					</TR>
					<TR height="22" nowrap>
						<TD align=right><%=strBar%></TD>
					</TR>
					<TR height="20" nowrap>
						<TD></TD>
					</TR>
					<TR>
						<TD>
							<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
								<TR>
									<TD align='right'>
										<INPUT TYPE="button" NAME="cmdExcel" value=" ���������û� " class=btn
											onclick="ToExcel()">&nbsp;&nbsp;
										<INPUT TYPE="button" NAME="cmdEdit" value=" �� �� "
											onclick="ExecCMD(1)" class="btn">
										&nbsp;&nbsp;
										<INPUT TYPE="button" NAME="cmdDel" value=" ɾ �� "
											onclick="ExecCMD(2)" class="btn">
										&nbsp;&nbsp;
										<INPUT TYPE="button" NAME="cmdUnlock" value=" �� �� "
											onclick="ExecCMD(4)" class="btn">
										&nbsp;&nbsp;
										<%
											//��ȡ��¼�û�������ɫ
											//String role_name = (String)session.getAttribute("role_name");
											Role role = new RoleSyb(Integer.parseInt(String.valueOf(user.getRoleId())));
											//if(role.getRoleName().toLowerCase().equals("system")){
											if (true)
											{
										%>
										<INPUT TYPE="button" NAME="cmdInit" value="��������"
											onclick="ExecCMD(3)" class="btn">
										<%
											}
											role = null;
										%>
										<!-- <INPUT TYPE="button" NAME="cmdEdit" value=" �鿴��ϸ�嵥 " onclick="ExecCMD(0)" class="btn"> -->

									</TD>
								</TR>
							</TABLE>

						</TD>
					</TR>
					
					<tr STYLE="display: none">
						<td>
							<iframe id="childFrm" src=""></iframe>
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>
<BR>

<BR>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
//Init();


$(function(){
	if ("ah_dx" != instArea && "sx_lt" != instArea) {
		showCodeName();
	}
});
</SCRIPT>



