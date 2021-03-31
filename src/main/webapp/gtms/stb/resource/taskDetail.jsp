<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
function resultConvert(res)
{
	switch(res)
	{
		case 0:
			document.write("失效");
			break;
		case 1:
			document.write("生效");
			break;
		case 2:
            document.write("生效");
            break;
		default:
			document.write("");
	}
}

function dateConvert(dat)
{
	var d = new Date(dat*1000);
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var h = d.getHours();
	var m = d.getMinutes();
	var s = d.getSeconds();
	
	document.write(year+"/"+month+"/"+day+" "+h+":" + m+":"+s);
}
</script>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：升级策略查询
		</TD>
	</TR>
</TABLE>
<br>
<table class="querytable" width="98%" align="center">
	<tr>
		<td colspan="4" class="title_1">批量策略版本升级任务定制</td>
	</tr>

	<ms:inArea areaCode="hn_lt" notInMode="false">
    	<tr>
    		<TD class="title_2" align="center" width="15%">任务简要描述</td>
    		<TD width="85%" colspan="3">
    			<s:property value="taskResultMap.task_desc" />
    		</td>
    	</tr>
    </ms:inArea>

	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.vendor_add" />
		</TD>
	</TR>
	<%if(LipossGlobals.inArea("sx_lt")){ %>
	<s:if test="taskType != 1">
	<TR>
		<TD class="title_2" align="center" width="15%">属地</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.city_name" />
		</TD>
	</TR>
	</s:if>
	<%}else{ %>
	<TR>
		<TD class="title_2" align="center" width="15%">属地</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.city_name" />
		</TD>
	</TR>
	<%} %>
	<TR>
		<TD class="title_2" align="center" width="15%">目标版本</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.version_path" />
		</TD>
	</TR>
	<tr>
		<td colspan="1" width="15%" align="center" class="title_2">
			已选择版本
		</td>
		<td width="85%" colspan="3">
			<s:if test="upRecordList!=null && upRecordList.size()>0">
				<s:iterator value="upRecordList">
					<s:property value="hardsoftv" />;
				</s:iterator>
			</s:if>
		</td>
	</tr>
	<ms:inArea areaCode="hn_lt" notInMode="true">
        <tr>
            <td class="title_2" align="center" width="15%">
                启用IP地址段
            </td>
            <td width="85%" colspan="3">
                <input type="radio" checked name="ipcheck" value="0" disabled/>否
                <input type="radio" name="ipcheck" value="1" disabled/>是
            </td>
        </tr>
	</ms:inArea>
	<s:if test="checkIPList!=null && checkIPList.size()>0">
		<s:iterator value="checkIPList">
			<tr name="ipduanti" style="display:none">
				<td class="title_2" align="center" width="15%">起始IP</td>
				<td width="35%" >
					<input type="text" name="start_ip" maxlength="15" readOnly value="<s:property value='start_ip' />"/>
				</td>
				<td class="title_2" align="center" width="15%">终止IP</td>
				<td width="35%">
					<input type="text" name="end_ip" maxlength="15" readOnly value="<s:property value='end_ip' />"/>
				</td> 
			</tr>
		</s:iterator>
	</s:if>
	<%if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
	<tr>
		<td class="title_2" align="center" width="15%">启用MAC地址段</td>
		<td width="85%" colspan="3">
			<s:if test="checkIPList.size()>0">
				<input type="radio" name="maccheck" value="0" disabled/>否
				<input type="radio" checked name="maccheck" value="1" disabled/>是
			</s:if>
			<s:else>
				<input type="radio" checked name="maccheck" value="0" disabled/>否
				<input type="radio" name="maccheck" value="1" disabled/>是
			</s:else>			
		</td>
	</tr>
	<s:if test="checkMACList!=null && checkMACList.size()>0">
		<s:iterator value="checkMACList">
			<tr name="macti" >
				<td class="title_2" align="center" width="15%">起始MAC</td>
				<td width="35%" >
					<input type="text" name="start_mac" maxlength="15" readOnly value="<s:property value='start_mac' />"/>
				</td>
				<td class="title_2" align="center" width="15%">终止MAC</td>
				<td width="35%">
					<input type="text" name="end_mac" maxlength="15" readOnly value="<s:property value='end_mac' />"/>
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<tr>
		<TD class="title_2" align="center" width="15%">启用业务帐号列表</td>
		<TD width="85%" colspan="3" >
			<s:if test='taskResultMap.check_account=="0"'>否</s:if>
			<s:else>
				<a target='_blank' href='<s:property value="taskResultMap.file_path" />'>业务帐号列表 </a>
			</s:else>
		</td>
	</tr>
	<%} %>
	<script type="text/javascript">
		initIpCheck('<s:property value="taskResultMap.check_ip" />');
		function initIpCheck(isCheck)
		{
			if('1'==isCheck)
			{
				document.getElementsByName("ipcheck")[1].checked = true;
				$("tr[name=ipduanti]").show();
				//document.getElementById("ipduanti").style.display="inline";
			}
		}
	</script>		
	<tr>
		<TD class="title_2" align="center" width="15%">策略方式</td>
		<TD width="85%" colspan="3">
			<SELECT name="strategyType" class="bk" disabled>
				<option value="4">机顶盒下次上连</option>
			</SELECT>
		</td>
	</tr>
	<tr>
		<TD class="title_2" align="center" width="15%">是否生效</td>
		<TD width="85%" colspan="3">
			<script type="text/javascript">resultConvert(<s:property value='taskResultMap.valid'/>)</script>
		</td>
	</tr>
	
	<ms:inArea areaCode="hn_lt" notInMode="false">
	<tr>
		<TD class="title_2" align="center" width="15%">是否校验网络类型</td>
		<TD width="85%" colspan="3">
			<s:if test="taskResultMap.check_net==1">是</s:if>
			<s:else>否</s:else>
		</td>
	</tr>
	<tr>
	    <TD class="title_2" align="center" width="15%">任务详细描述</TD>
        <TD width="85%" colspan="3">
           <s:property value="taskResultMap.task_detail_desc" />
        </TD>
	</tr>
	</ms:inArea>
	
	<tr>
		<TD class="title_2" align="center" width="15%">录入时间</td>
		<TD width="85%" colspan="3">
			<script type="text/javascript">dateConvert(<s:property value='taskResultMap.record_time'/>)</script>
		</td>
	</tr>
</table>
<br>
<br>
</body>