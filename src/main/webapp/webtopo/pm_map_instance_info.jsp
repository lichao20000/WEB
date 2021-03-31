<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.common.database.*" %>
<title>设备性能配置</title>
<%
	String device_id = request.getParameter("device_id");//设备ID
	String type = request.getParameter("type");
	String expressionid = request.getParameter("expressionid");//表达式ID
	//String name = new String(request.getParameter("name").getBytes("iso-8859-1"),"gbk");//表达式名称
	String name = request.getParameter("name");
	String sql = "select id,indexid,descr,collect,intodb from pm_map_instance where device_id='" + device_id 
				+ "'  and expressionid=" + expressionid;
	Cursor cursor = DataSetBean.getCursor(sql);
	Map myMap = cursor.getNext();
	String id = null;//唯一实例id   主键
	String indexid = null;//实例索引
	String descr = null;//实例描述
	String intodb = null;//原始数据是否入库
	String collect = null;//是否采集
	
%>
<form name="topfrm" target="hiddenFrm">
	<table width="100%"  border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="#000000"> 
		<tr>
			<th>表达式名称</th>
			<th>实例描述</th>
			<th>索引</th>
			<th>操作</th>
		</tr>
		<div di="instanceparent">
		<%
			while(myMap != null){
				id = (String)myMap.get("id");
				indexid = (String)myMap.get("indexid");
				descr = (String)myMap.get("descr");
				collect = (String)myMap.get("collect");
				intodb = (String)myMap.get("intodb");
		%>
			<tr id="instance<%=id %>">
				<td class=column1><%=name %></td>
				<td class=column1><%=descr %></td>
				<td class=column1><%=indexid %></td>
				<td class=column1 align="center"><a href="pm_map_instance_modify.jsp?id=<%=id %>" target="hiddenFrm">修改</a>&nbsp;&nbsp;&nbsp;<a href="pm_map_instance_delete.jsp?id=<%=id %>&device_id=<%=device_id %>&expressionid=<%=expressionid %>&deltype=one&name=<%=name %>&type=<%=type %>" target="hiddenFrm">删除</a></td>
			</tr>
		<%
				myMap = cursor.getNext();	
			}
		%>
		<div>
		<tr>
			<td colspan="4" class=green_foot align="center">&nbsp;</td>
		</tr>
		
	</table>
</form>
<IFRAME ID=hiddenFrm name="hiddenFrm" STYLE="display:none;width:500;height:500"></IFRAME>

<SCRIPT LANGUAGE="JavaScript">
<!--

function showpage(uniformid)
{
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="button_onblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";

			break;
		}
		case 2:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_onblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="";
			document.all("test3").style.display="none";

			break;
		}	
		case 3:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_onblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="";

			break;
		}	
	}
	
}

function IsNegative(strValue,strMsg) 
{
	var bz = true;
	var index = strValue.indexOf("-");

	if (index == 0)
		bz = false;
	
	if(!bz)
		alert(strMsg+"应为正数");

	return bz;
}

function CheckForm()
{
	
	if (frm.id.value == "")
	{
		window.alert("没有选择性能表达式实例，请选择！");
		return false;
	}

	if (frm.compSign_1.selectedIndex != 0) 
	{
		
		if (!IsNull(frm.fixedness_value1.value.trim(),"启用了固定阀值一，固定阀值一的值"))
		{
			return false;
		}

		if (!isFloatNumber(frm.fixedness_value1.value.trim(),"固定阀值一的值的正确格式")) 
		{
			return false;
		}

		if (!IsNegative(frm.fixedness_value1.value.trim(),"固定阀值一的值的正确格式")) {
			return false;	
		}
		
		if (!IsNull(frm.seriesOverstep_value1.value.trim(),"启用了固定阀值一，连续超出阀值一的次数")) 
		{
			return false;
		}
		
		if (!IsNumber2(frm.seriesOverstep_value1.value.trim(),"连续超出阀值一的次数的正确格式"))
		{
			return false;
		}

		if (frm.send_warn1.selectedIndex==0) 
		{
			window.alert("没有选择固定阈值一发出告警的级别，请选择！");
			return false;
		}

		if (frm.renew_warn1.selectedIndex==0)
		{
			window.alert("没有选择固定阈值一恢复告警的级别，请选择！");
			return false;
		}
	}
	else {
		frm.fixedness_value1.value = "0";
		frm.seriesOverstep_value1.value = "1";
	}

	
	if (frm.compSign_2.selectedIndex != 0) 
	{
		
		if (!IsNull(frm.fixedness_value2.value.trim(),"启用了固定阀值二，固定阀值二的值"))
		{
			return false;
		}

		if (!isFloatNumber(frm.fixedness_value2.value.trim(),"固定阀值二的值的正确格式"))
			{
			return false;
		}

		if (!IsNegative(frm.fixedness_value2.value.trim(),"固定阀值二的值的正确格式")) {
			return false;	
		}

		if (!IsNull(frm.seriesOverstep_value2.value.trim(),"启用了固定阀值二，连续超出阀值二的次数"))
		{
			return false;
		}

		if (!IsNumber2(frm.seriesOverstep_value2.value.trim(),"连续超出阀值二的次数正确格式"))
		{
			return false;
		}

		if (frm.send_warn2.selectedIndex==0) 
		{
			window.alert("没有选择固定阈值二发出告警的级别，请选择！");
			return false;
		}

		if (frm.renew_warn2.selectedIndex==0) 
		{
			window.alert("没有选择固定阈值二恢复告警的级别，请选择！");
			return false;
		}
	}
	else {
		frm.fixedness_value2.value = "0";
		frm.seriesOverstep_value2.value = "1";

	}

	if (frm.dynamic_OperateSign.selectedIndex != 0)
    {

		if (!IsNull(frm.benchmark_Value.value.trim(),"启用了动态阀值，前几天的数据为基准值的值"))
		{
			return false;
		}

		if (!IsNumber2(frm.benchmark_Value.value.trim(),"动态阀值前几天的数据为基准值的值的正确格式")) 
		{
			return false;
		}
		
		if (!IsNull(frm.valve_Percent.value.trim(),"启用了动态阀值，动态阀值的值"))
		{
			return false;
		}

		if (!isFloatNumber(frm.valve_Percent.value.trim(),"动态阀值的的值的正确格式"))
		{
			return false;
		}

		if (!IsNegative(frm.valve_Percent.value.trim(),"动态阀值的值的正确格式")) {
			return false;	
		}
		
		if (!IsNull(frm.achieve_Percent2.value.trim(),"启用了动态阀值，连续超出动态阀值的次数"))
		{
			return false;
		}

		if (!IsNumber2(frm.achieve_Percent2.value.trim(),"连续超出动态阀值的次数的正确格式"))
		{
			return false;
		}

		if (frm.sdynamic_send_warn.selectedIndex==0) 
		{
			window.alert("没有选择动态阀值发出告警的级别，请选择！");
			return false;
		}
 
		if (frm.sdynamic_renew_warn.selectedIndex==0)
		{
			window.alert("没有选择动态阀值恢复告警的级别，请选择！");
			return false;
		}
	}
	else {
		frm.benchmark_Value.value = "1";
		frm.valve_Percent.value = "0";
		frm.achieve_Percent2.value = "1";
	}

	if (frm.mutation_OperateSign.selectedIndex != 0)
	{
		
		if (!IsNull(frm.overstep_Percent.value.trim(),"启用了突变阀值，突变阀值的值"))
		{
			return false;
		}

		if (!isFloatNumber(frm.overstep_Percent.value.trim(),"突变阀值的值的正确格式"))
		{
			return false;
		}

		if (!IsNegative(frm.overstep_Percent.value.trim(),"突变阀值的值的正确格式")) {
			return false;	
		}

		if (!IsNull(frm.achieve_Percent3.value.trim(),"启用了突变阀值,连续超出突变阀值的次数"))
		{
			return false;
		}

		if (!IsNumber2(frm.achieve_Percent3.value.trim(),"连续超出突变阀值的次数的正确格式"))
		{
			return false;
		}
		
		if(frm.send_warn3.selectedIndex==0) {
			window.alert("没有选择突变阀值，发出告警级别！");
			return false;
		}
	}
	else {
		frm.overstep_Percent.value = "0";
		frm.achieve_Percent3.value = "1";

	}
	frm.action="pm_map_instance_update.jsp";
	frm.submit();
}

//-->
</SCRIPT>
<form name="frm" target="hiddenFrm">
<input type="hidden" name="id" value="">
<input type="hidden" name="device_id" value="<%=device_id %>">
<input type="hidden" name="type" value="<%=type %>">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align=center>
  <tr>
    <TD class="column1"> 
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
        <tr> 
          <TH width="159" height="25" class="button_onblue" id="td1" onClick="location.href='javascript:showpage(1);'">固定阀值配置</TH>
          <TH width="159" height="25" class="button_outblue" id="td2" onClick="location.href='javascript:showpage(2);'">动态阀值配置</TH>
          <TH width="159" height="25" class="button_outblue" id="td3" onClick="location.href='javascript:showpage(3);'">突变阀值配置</TH>
          <td align="left"></td>
        </tr>
        <tr> 
          <td height="3" colspan="4" align="center" class="blue_tag_line"></td>
        </tr>
      </table>
    </TD>
  </tr>
  
  <TR>
    <TD id="test1" style="display:" bgcolor=#000000>
		<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1" valign="middle">
		  <tr>
			<td class=column1 align=center>比较操作符一</td>
			<td class=column2 align=left>
			 <select name="compSign_1">
              <option value="0" selected>不使用</option>
              <option value="1">大于</option>
              <option value="2">大于等于</option>
              <option value="3">小于</option>
              <option value="4">小于等于</option>
              <option value="5">等于</option>
              <option value="6">不等于</option>
            </select></td>
			<td class=column1 align=center>固定阀值一</td>
			<td class=column2 align=left><input name="fixedness_value1" type="text"  style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>固定阀值一描述</td>
			<td colspan="3" class=column1 align=left><input name="fixedness_value1desc" type="text" style="width:503"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>连续超出阀值一（次）</td>
			<td class=column2 align=left><input name="seriesOverstep_value1" type="text"  style="width:180" value=1></td>
			<td class=column1 align=left colspan="2">（发出告警）</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>发出告警级别</td>
			<td class=column2 align=left><select name="send_warn1">
			  <option value="0" selected>请选择</option>
			  <option value="1">正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select></td>
			<td class=column1 align=center>恢复告警级别</td>
			<td class=column2 align=left><select name="renew_warn1">
			  <option value="0" selected>请选择</option>
			  <option value="1">正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>比较操作符二</td>
			<td class=column2 align=left>
			<select name="compSign_2">
              <option value="0" selected>不使用</option>
              <option value="1">大于</option>
              <option value="2">大于等于</option>
              <option value="3">小于</option>
              <option value="4">小于等于</option>
              <option value="5">等于</option>
              <option value="6">不等于</option>
            </select>
			</td>
			<td class=column1 align=center>固定阀值二</td>
			<td class=column2 align=left><input name="fixedness_value2" type="text" style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>固定阀值二描述</td>
			<td colspan="3" class=column2 align=left><input name="fixedness_value2desc" type="text" style="width:180"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>连续超出阀值二（次）</td>
			<td class=column2 align=left><input name="seriesOverstep_value2" type="text" style="width:180" value=1></td>
			<td class=column1 align=left colspan="2">（发出告警）</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>发出告警级别</td>
			<td class=column2 align=left>
			<select name="send_warn2">
			  <option value="0" selected>请选择</option>
			  <option value="1" >正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select>
			</td>
			<td class=column1 align=center>恢复告警级别</td>
			<td class=column2 align=left>
			<select name="renew_warn2">
			  <option value="0" selected>请选择</option>
			  <option value="1">正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=green_foot align="center"><input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" 关 闭 " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>
	</TD>
 </TR>

  <TR>
    <TD id="test2" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>动态阀值操作符</td>
			<td class=column2 align=left>
			 <select name="dynamic_OperateSign">
              <option value="0" selected>不使用</option>
              <option value="1">变化率大于</option>
              <option value="2">变化率大于等于</option>
              <option value="3">变化率小于</option>
              <option value="4">变化率小于等于</option>
              <option value="5">变化率等于</option>
              <option value="6">变化率不等于</option>
            </select>			
			</td>
			<td class=column1 align=center>数据的基准值（前几天）</td>
			<td class=column2 align=left><input name="benchmark_Value" type="text" style="width:150" value=1></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>阀值百分比（％）</td>
			<td colspan="3" class=column2 align=left><input name="valve_Percent" type="text" style="width:502"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>达到阀值百分比（次）</td>
			<td class=column2 align=left><input name="achieve_Percent2" type="text" style="width:150" value=1></td>
			<td colspan="2" class=column2 align=left>（发出告警）</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>动态阀值描述</td>
			<td colspan="3" class=column2 align=left><input name="dynamic_Valve_desc" type="text" style="width:502"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>发出告警级别</td>
			<td class=column2 align=left>
			<select name="sdynamic_send_warn">
			  <option value="0" selected>请选择</option>
			  <option value="1" >正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select>
			</td>
			<td class=column1 align=center>恢复告警级别</td>
			<td class=column2 align=left>
			<select name="sdynamic_renew_warn">
			  <option value="0" selected>请选择</option>
			  <option value="1" >正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=column1 align=left>备注：&nbsp;&nbsp;变化率 ＝ （（采到的值－基准值）/基准值） * 100</td>
		  </tr>
		   <tr>
			<td colspan="4" class=green_foot align="center"><input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" 关 闭 " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>

	</TD>
 </TR>

  <TR>
    <TD id="test3" style="display:none" bgcolor=#000000>
		<table width="100%"  border="0" cellspacing="1" cellpadding="2" valign="middle" align="center">
		  <tr>
			<td class=column1 align=center>突变阀值操作符</td>
			<td class=column2 align=left>
			<select name="mutation_OperateSign">
              <option value="0" selected>不使用</option>
              <option value="1">变化率绝对值大于</option>
              <option value="2">变化率绝对值大于等于</option>
              <option value="3">变化率绝对值小于</option>
              <option value="4">变化率绝对值小于等于</option>
              <option value="5">变化率绝对值等于</option>
              <option value="6">变化率绝对值不等于</option>
            </select>			
			</td>
			<td class=column1 align=center>超出百分比（％）</td>
			<td class=column2 align=left><input name="overstep_Percent" type="text" style="width:165"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>达到阀值百分比（次）</td>
			<td class=column2 align=left><input name="achieve_Percent3" type="text" style="width:165" value=1></td>
			<td colspan="2" class=column2 align=left>（发出告警）</td>
		  </tr>
		  <tr>
			<td class=column1 align=center>突变阀值描述</td>
			<td colspan="3" class=column2 align=left><input name="mutation_Valve_desc" type="text" style="width:500"></td>
		  </tr>
		  <tr>
			<td class=column1 align=center>发出告警级别</td>
			<td colspan="3" class=column2 align=left>
			<select name="send_warn3">
			  <option value="0" selected>请选择</option>
			  <option value="1" >正常日志</option>
			  <option value="2">提示告警</option>
			  <option value="3">一般告警</option>
			  <option value="4">严重告警</option>
			  <option value="5">紧急告警</option>
			</select>
			</td>
		  </tr>
		  <tr>
			<td colspan="4" class=column1 align=left>备注：&nbsp;&nbsp;变化率绝对值 ＝ |（（采到的值－上次采集值）/上次采集值） * 100|</td>
		  </tr>
		   <tr>
			<td colspan="4" class=green_foot align="center"><input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;&nbsp;<input type="button" value=" 关 闭 " onClick="javascript:window.close();" class="jianbian">
			</td>
		  </tr>
		</table>

	</TD>
 </TR> 
</TABLE>
</form>

<%@ include file="../foot.jsp"%>