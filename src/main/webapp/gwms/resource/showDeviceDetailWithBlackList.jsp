<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
		
		<script language="JavaScript">
		function removeBlackList(){
			var rm_deviceIds = new Array();
			var groupCheckbox=$("input[name='blacklistboxs']");
			for(i=0;i<groupCheckbox.length;i++){
		        if(groupCheckbox[i].checked){  
		            rm_deviceIds[i] =  groupCheckbox[i].deviceId;
		        }		
			}
			if( 0==rm_deviceIds.length)
			{
				alert("请先选择设备！");
				return ;
			}
			if(!confirm("确认移除黑名单吗？"))
			{
				return ;
			}
			//选中的deviceid集合传入后台
			var url="<s:url value="/gwms/resource/batchHttpTestBlackList!removeBlackList.action"/>";
			$.post(url,{
				rm_deviceIds:rm_deviceIds
			},function(){	
				alert("移除黑名单完成！");
				window.close();
			});
			$("input[@name='blacklistboxs']").val("0");
			
		}
		
		//全选，反选
		 function allcheck() {
		        var all=document.getElementById('allboxs');//获取到点击全选的那个复选框的id
		        var one=document.getElementsByName('blacklistboxs');//获取到复选框的名称
		        if(all.checked==true){//因为获得的是数组，所以要循环 为每一个checked赋值
		            for(var i=0;i<one.length;i++){
		                one[i].checked=true;
		            }
		 
		        }else{
		            for(var j=0;j<one.length;j++){
		                one[j].checked=false;
		            }
		        }
		    }

		</script>
		

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：测速黑名单设备详细
				</TD>
			</TR>
		</TABLE>
		<br>
		<form id="selectForm" name="selectForm" action="" target="childFrm">
		<input type="hidden" name="upResult" value='<s:property value="upResult"/>'/>
		<input type="hidden" name="taskId" value='<s:property value="taskId"/>'/>
		<table width="98%" class="listtable" align="center">
		<%	String area= LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
		<thead>
				<tr>
					<th align="center" width="8%">
						<input id="allboxs" onclick="allcheck()" type="checkbox"/>
					</th>
					<th align="center" <%if("hb_lt".equals(area)){%>width="6%"<%}else{%>width="8%"<%}%> >
						属地
					</th>
					<th align="center" width="8%">
						厂商
					</th>
					<th align="center" width="8%">
						型号
					</th>
					<th align="center" width="8%">
						软件版本
					</th>
					<th align="center" width="8%">
						设备序列号
					</th>
					<th align="center" width="8%">
						宽带账号
					</th>
					<th align="center" <%if("hb_lt".equals(area)){%>width="10%"<%}else{%>width="12%"<%}%> >
						添加时间
					</th>
					<th align="center" width="8%">
						是否在黑名单
					</th>
				</tr>
		</thead>
			<s:if test="taskResultList != null && taskResultList.size()>0">
				<tbody>
					<s:iterator value="taskResultList" >
						<tr>
							<td align="center"><input  name="blacklistboxs" type="checkbox" deviceId=<s:property value="deviceId" />/></td>
							<td align="center"><s:property value="cityName" /></td>
							<td align="center"><s:property value="vendorName" /></td>
							<td align="center"><s:property value="deviceModel" /></td>
							<td align="center"><s:property value="deviceTypeName" /></td>
							<td align="center"><s:property value="deviceSerialNumber" /></td>
							<td align="center"><s:property value="pppoe_name" /></td>
							<td align="center"><s:property value="add_time" /></td>
							<td align="center"><s:property value="is_blacklist" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</s:if>
			<s:else>
			<tbody>
				<tr>
					<td colspan="10">没有查询到相关设备！</td>
				</tr>
            </tbody>
		</s:else>
		<tfoot>
		<tr bgcolor="#FFFFFF">
		<td colspan="9" align="right" class="green_foot" width="100%">
			<input type="button" onclick="javascript:removeBlackList();" class=jianbian name="deviceRemove" value="移除黑名单" />
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" 取 消 " />
		</td>
		</tr>
		</tfoot>
		</table>		
</form>
</body>
