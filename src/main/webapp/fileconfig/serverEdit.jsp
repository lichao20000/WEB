<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
	Map fields = (Map)serverManage.getFileMap(request);
	String gather_id = (String)fields.get("gather_id");
	String gatherList =  deviceAct.getGatherList(session,gather_id,"",false);
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
	function checkform(){
		
		if(!IsNull(document.frm.server_name.value,"服务器名")){
			document.frm.server_name.focus();
			document.frm.server_name.select();
			return false;
		}
		if(document.frm.gather_id.value==-1){
			alert("请选择采集点！");
			document.frm.gather_id.focus();
			return false;		
		}	
		if(!IsNull(document.frm.inner_url.value,"服务器IP")){
			document.frm.inner_url.focus();
			document.frm.inner_url.select();
			return false;
		}
		if(!IsNull(document.frm.server_dir.value,"服务器相对路径")){
			document.frm.server_dir.focus();
			document.frm.server_dir.select();
			return false;
		}
		if(!IsNull(document.frm.outter_url.value,"WEB服务器URL")){
			document.frm.outter_url.focus();
			document.frm.outter_url.select();
			return false;
		}	
		
		if(document.frm.file_type.value==-1){
			alert("请选择文件服务器类型！");
			document.frm.file_type.focus();
			return false;		
		}

		
		if(!IsNull(document.frm.access_user.value,"用户名")){
			document.frm.access_user.focus();
			document.frm.access_user.select();
			return false;
		}
		if(!IsNull(document.frm.access_passwd.value,"密码")){
			document.frm.access_passwd.focus();
			document.frm.access_passwd.select();
			return false;
		}
		
		if(document.frm.dir_status.value==-1){
			alert("请选择目录状态！");
			document.frm.dir_status.focus();
			return false;		
		}	
	}
</SCRIPT>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<FORM NAME="frm" METHOD="post" ACTION="serverSave.jsp" onsubmit="return checkform()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD HEIGHT=20>
				&nbsp;
			</TD>
		</TR>	
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						文件服务器
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						请先手工创建文件服务虚拟路径及文件路径，然后创建文件服务器！
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="4" align="center"><B>文件服务器编辑</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>名称</TD>
					<TD width="40%">
						<INPUT TYPE="text" NAME="server_name" class="bk" value="<%=fields.get("server_name")%>">&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="right" width="10%" nowrap>采集点</TD>
					<TD width="40%">
						<%=gatherList%>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >用户访问URL</TD>
					<TD colspan="3">
						<input type="text" name="inner_url" value="<%=fields.get("inner_url")%>" size="85" class="bk">&nbsp;<font color="#FF0000">*</font>(例：http://192.168.28.192/FileServer,多个url用 ; 隔开)			
					</TD>				
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >设备访问URL</TD>
					<TD colspan="3">
						<input type="text" name="outter_url" size="85" value="<%=fields.get("outter_url")%>" class="bk">&nbsp;<font color="#FF0000">*</font>(例：http://221.226.118.51:9090/FileServer,多个url用 ; 隔开)			
					</TD>	
				</TR>	
				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >相对路径</TD>
					<TD>
						<input type="text" name="server_dir" value="<%=fields.get("server_dir")%>" class="bk">&nbsp;<font color="#FF0000">*</font>(例：FILESERVER/CONFIG)	
					</TD>
					<TD class=column nowrap align="right">文件服务器类型</TD>
					<%
					
						String strfile_type = (String)fields.get("file_type");
						String strSelect1 = "";
						String strSelect2 = "";
						String strSelect3 = "";
						if(strfile_type.equals("1")){
							strSelect1="selected";
						}else if(strfile_type.equals("2")){
							strSelect2="selected";
						}else{
							strSelect3="selected";
						}
					%>					
					<TD>
						<select name="file_type" class="bk">
							<option value="-1">--请选择--</option>
							<option value="1" <%=strSelect1%>>版本文件</option>
							<option value="2" <%=strSelect2%>>配置文件</option>
							<option value="3" <%=strSelect3%>>日志文件</option>								
						</select>&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>
				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >用户名</TD>
					<TD>
						<input type="text" name="access_user" value="<%=fields.get("access_user")%>" class="bk">&nbsp;<font color="#FF0000">*</font>
					</TD>				
					<TD class=column align="right" >密码</TD>
					<TD>
						<input type="password" name="access_passwd" value="<%=fields.get("access_passwd")%>" class="bk">&nbsp;<font color="#FF0000">*</font>
					</TD>
				</TR>


				<TR >
					<TD colspan="4" align="right" class=green_foot>
						<INPUT TYPE="submit" name="save" value=" 保 存 " class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="update">
						<INPUT TYPE="hidden" name="dir_id" value="<%=fields.get("dir_id")%>">
						<INPUT TYPE="reset" name="reset" value=" 重 写 " class=btn >
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</FORM>
	</TD>
</TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>
