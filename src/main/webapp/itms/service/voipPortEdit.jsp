<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
 <%
 String gw_type = request.getParameter("gw_type");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音端口编辑</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">

function query(){
	document.frm.submit();	 
}

function modify(userId,phone,lineId, dbPort){
	$("#edit_phone").html(phone);
	$(".edit_voip").val(lineId);
	$(".edit_voip_tmp").val(lineId);
	$("input[name='voipPort']").val(dbPort);
	$("input[name='userId']").val(userId);
	$(".editHTML").show();
}

function close(){
	$(".editHTML").hide();
}

function changePort() {
	var userId = $("input[name='userId']").val();
	var dbPort = $("input[name='voipPort']").val();
	if(dbPort == "null" || dbPort == "NULL"){
		dbPort = "";
	}
	var lineId = $("input[name='lineId']").val();
	var lineIdTmp = $("input[name='lineId_tmp']").val();
	if(lineId == "" || lineId == undefined || lineId == null){
		alert("端口号输入不能为空");
		return;
	}
	if(lineId == lineIdTmp){
		alert("端口号输入不能和原来一致");
		return;
	}
	
	
	var gwType = $("input[name='gw_type']").val();
	
	var reg = /^[1-9][0-9]*$/; 
	if(!reg.test(lineId) || lineId > 32){
		alert("端口号输入不正确，应为1~32的数字");
		return;
	}
	var url = "<s:url value='/itms/service/voipPortEdit!changeVoipPort.action'/>";
	$.post(url,{
		userId : userId,
		voipPort : dbPort,
		lineId : lineId,
		oldLine : lineIdTmp,
		gw_type : gwType
	},function(ajax){
		var array = ajax.split(",");
		alert(array[1]);
		if(array[0] == 1){
			$(".editHTML").hide();
			query();
		} 
	});
}
</script>
</head>
<body> 
  <TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
  <tr>
	  <td HEIGHT=20>&nbsp;</td>
  </tr>
    <tr>
      <td>
        <!-- 查询开始 -->
         <form name="frm" action="<s:url value='/itms/service/voipPortEdit.action'/>" method='POST'>
          <table width="98%" class="green_gargtd" height="30">
			<tr>
				<td width="162">
				  <div align="center" class="title_bigwhite">语音端口修改</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15" height="12">修改的端口不能是正在被占用的端口</td>
			</tr>
		  </table>
	  	  <table width="98%" class="querytable">
		  	 	<tr>
		  	 		<TH colspan="4">语音端口修改</TH>
		  	 	</tr>
		  	    <tr>
	  	   	 		<TD class=column width="15%" align='right'>LOID</TD>
					<TD width="35%"><input type="text" name="loid" size="30" class=bk value='<s:property value="loid" />'/>
					</TD>
					<TD class=column width="15%" align='right'>设备序列号</TD>
					<TD width="35%"><input type="text" name="dev_sn"size="30" class=bk value='<s:property value="dev_sn" />'/>
					</TD>
		   	    </tr>
		   	    <TR align=right>
					<td colspan="4" align="right" class=foot>
					    <button onclick="query()">&nbsp;查 询&nbsp;</button>
						<input type="hidden" name="gw_type" value="2"/>
					</td>
				</TR>
	  	  </table>
  	    </form>
  	    <!-- 查询end -->
  	    <TABLE width="98%" class="listtable" id="listTable">
  	       <thead>
			<tr>
	 			<th>Loid</th>
				<th>设备序列号</th>
				<th>语音号码</th>
				<th>语音端口</th>
				<th>线路ID</th>
				<th>操作</th>
			</tr>
		  </thead>
		  <tbody>
		  <s:if test="data.size()>0">
		  <s:iterator value="data">
			<tr>
				<td><s:property value="loid" /></td>
				<td><s:property value="device_serialnumber" /></td>
				<td><s:property value="voip_phone" /></td>
				<td><s:property value="voip_port" /></td>
				<td><s:property value="line_id" /></td>
				<td><a href='javascript:modify("<s:property value="userId" />",
					   				"<s:property value="voip_phone" />",
					   				"<s:property value="line_id" />",
					   				"<s:property value="voip_port" />")' >编辑</a></td>
			 </tr>
		  </s:iterator>
		  </s:if>
		  <s:else>
		 <tr>
			<td colspan="6">
				系统没有相关的设备信息!
			</td>
		  </tr>
		 </s:else>
		 </tbody>
		 <tfoot>
			<tr>
				<td colspan="6">
					<span style="float: right;"> <lk:pages
							url="/itms/service/voipPortEdit.action" styleClass=""
							showType="" isGoTo="true" changeNum="true" /> </span>
				</td>
			</tr>
          </tfoot>
  	    </TABLE>
  	    <!-- 编辑开始 -->
  	    <FORM target="" method="post" action="" style="margin-top: 15px">
		  <TABLE width="98%" class="editHTML green_gargtd" style="display: none">
		    <tr>
		      <td>
		         <TABLE width="100%" class="listtable">
					<TR>
						<td colspan="4" text-align="center" bgcolor='#E8E8FF'>编辑语音口</td>
					</TR>
					<tr bgcolor="#FFFFFF">
				        <td class=column>电话号码:</td>
				        <td id='edit_phone'></td>
				        <td class=column>语音端口:</td>
				        <td><input type='text' class='edit_voip' class=bk name='lineId'>
				        </td>
		           </tr>
				 </TABLE>
		      </td>
		    </tr>
		    <tr>
		      <TD>
				<TABLE width="100%" class="listtable">
					<TR bgcolor="#FFFFFF">
						<TD align="right" >
						<button onclick="javascript:changePort()">&nbsp;保 存&nbsp;</button>
						<input type="hidden" name="userId" value=""/>
           	            <input type="hidden" name="voipPort" value=""/>
           	            <input type='hidden' class='edit_voip_tmp' name='lineId_tmp'>
					</TR>
				</TABLE>
				</TD>
		    </tr>
		  </TABLE>
		</FORM>
  	    <!-- 编辑结束 -->
      </td>
    </tr>
  </TABLE>
</body>
</html>
<%@ include file="../../foot.jsp"%>