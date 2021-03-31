<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<SCRIPT LANGUAGE="JavaScript">

function CheckForm(){
	
	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}
}

//测试连接
function testConn(e){
	
	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}
	
	var device_id ;

	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				device_id = oSelect[i].value;
			}
		}
	}
	if(oSelect.checked){
		device_id=oSelect.value;
	}
	
	var url = "testConnectionSubmit.jsp";
	$.post(url,{
		device_id:device_id
	},function(ajax){
		ajax = ajax.replace(/(^\s*)|(\s*$)/g, ""); 
		alert(ajax)
	});
}

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="testConnectionSubmit.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									配置查询
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									对选择的设备进行连接测试。
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">												
													<INPUT TYPE="button" value=" 获 取 " class=btn onclick="testConn(this)">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>