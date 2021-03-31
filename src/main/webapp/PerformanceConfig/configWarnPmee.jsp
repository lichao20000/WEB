<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.Map,com.linkage.module.gwms.dao.gw.EventLevelLefDAO"%>
<%
EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
Map warnMap = eventLevelLefDAO.getWarnLevel();
request.setAttribute("warnMap",warnMap);
%>
<%--
/**
 * 配置告警选择项
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-6 PM 03:42:45
 * 
 * @版权所有.南京联创 网管产品部;
 * 
 */
 --%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>"     rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>"  rel="stylesheet" type="text/css">
<script type="text/javascript">
	$(function(){
		$("tr[@name='tr_fix']").show();
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
		//点击固定阈值配置
		$("td[@name='td_fix']").click(function(){
			$.showHide(1);
		});
		//点击动态阈值配置
		$("td[@name='td_active']").click(function(){
			$.showHide(2);
		});
		//点击突变阈值配置
		$("td[@name='td_sudden']").click(function(){
			$.showHide(3);
		});
	});

	
	//Check Form
	function CheckForm(){
		if($("select[@name='mintype']").val()>0 && $("input[@name='minthres']").val()==""){
			alert("启用了固定阀值一，固定阀值一不能为空，请输入!");
			$("input[@name='minthres']").focus();
			return false;
		}else if($("input[@name='minthres']").val()!="" && !$.checkNum($("input[@name='minthres']").val(),'int')){
			alert("固定阀值只能为数字，请重新输入！");
			$("input[@name='minthres']").focus();
			$("input[@name='minthres']").select();
			return false;
		}else if($("input[@name='mincount']").val()==""){
			alert("连续超出阀值的次数不能为空，请重新输入！");
			$("input[@name='mincount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mincount']").val(),'int')){
			alert("连续超出阀值的次数只能为数字格式，请重新输入！");
			$("input[@name='mincount']").focus();
			$("input[@name='mincount']").select();
			return false;
		}else if($("select[@name='maxtype']").val()>0 && $("input[@name='maxthres']").val()==""){
			alert("固定阀值不能为空，请输入！");
			$("input[@name='maxthres']").focus();
			return false;
		}else if($("input[@name='maxthres']").val()!="" && !$.checkNum($("input[@name='maxthres']").val(),'int')){
			alert("固定阀值只能为数字格式，请重新输入！");
			$("input[@name='maxthres']").focus();
			$("input[@name='maxthres']").select();
			return false;
		}else if($("input[@name='maxcount']").val()==""){
			alert("连续超出阀值的次数不能为空，请输入！");
			$("input[@name='maxcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='maxcount']").val(),'int')){
			alert("连续超出阀值的次数只能为数字格式，请重新输入！");
			$("input[@name='maxcount']").focus();
			$("input[@name='maxcount']").select();
			return false;
		}else if($("input[@name='beforeday']").val()==""){
			alert("请输入数据基准值！");
			$("input[@name='beforeday']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='beforeday']").val(),'int')){
			alert("数据基准值只能为数字格式，请重新输入！");
			$("input[@name='beforeday']").focus();
			$("input[@name='beforeday']").select();
			return false;
		}else if($("select[@name='dynatype']").val()>0 && $("input[@name='dynathres']").val()==""){
			alert("阀值百分比不能为空，请输入！");
			$("input[@name='dynathres']").focus();
			return false;
		}else if($("input[@name='dynathres']").val()!="" && !$.checkNum($("input[@name='dynathres']").val(),'int')){
			alert("阈值百分比只能为数字格式，请重新输入");
			$("input[@name='dynathres']").focus();
			$("input[@name='dynathres']").select();
			return false;
		}else if($("select[@name='mutationtype']").val()>0 && $("input[@name='mutationthres']").val()==""){
			alert("突变阀值超出百分比不能为空，请输入!");
			$("input[@name='mutationthres']").focus();
			return false;
		}else if($("input[@name='mutationthres']").val()!="" && !$.checkNum($("input[@name='mutationthres']").val(),'int')){
			alert("百分比只能为数字格式，请重新输入!");
			$("input[@name='mutationthres']").focus();
			$("input[@name='mutationthres']").select();
			return false;
		}else if($("input[@name='mutationcount']").val()==""){
			alert("达到阀值百分比次数不能为空，请输入!");
			$("input[@name='mutationcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mutationcount']").val(),'int')){
			alert("次数只能为数字格式，请重新输入");
			$("input[@name='mutationcount']").focus();
			$("input[@name='mutationcount']").select();
			return false;
		}else{
			return true;
		}
	}
</script>
</head>
<body>
<br>
<table width="100%" class="querytable">
	<tr name="tr_show">
		<td colspan="5">
		<table width="450" class="tab" align="left">
			<tr>
				<td class="mouseon" name="td_fix"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseon';">固定阀值配置</td>
				<td class="mouseout" name="td_active"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseout';">动态阀值配置</td>
				<td class="mouseout" name="td_sudden"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseout';">突变阀值配置</td>
			</tr>
		</table>
		</td>
	</tr>
	<!-- *******************************************以下是固定阈值配置项************************************ -->
	<tr name="tr_fix">
		<td class="title_2">比较操作符一:</td>
		<td><select name="mintype">
			<option value="0" selected>不使用</option>
			<option value="1">大于</option>
			<option value="2">大于等于</option>
			<option value="3">小于</option>
			<option value="4">小于等于</option>
			<option value="5">等于</option>
			<option value="6">不等于</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">固定阀值一:</td>
		<td><input type="text" name="minthres"></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">固定阀值一描述:</td>
		<td colspan="4"><input type="text" size="58" name="mindesc">
		</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">连续超出阀值一(次)</td>
		<td><input type="text" name="mincount" value="1"></td>
		<td colspan="3">(发出告警)</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">发出告警级别:</td>
		<td><!--<select name="minwarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			    </td>
		<td class="title_2"></td>
		<td class="title_2">恢复告警级别:</td>
		<td><select name="minreinstatelevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">比较操作符二:</td>
		<td><select name="maxtype">
			<option value="0" selected>不使用</option>
			<option value="1">大于</option>
			<option value="2">大于等于</option>
			<option value="3">小于</option>
			<option value="4">小于等于</option>
			<option value="5">等于</option>
			<option value="6">不等于</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">固定阀值二:</td>
		<td><input type="text" name="maxthres"></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">固定阀值二描述:</td>
		<td colspan="4"><input type="text" size="58" name="maxdesc">
		</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">连续超出阀值二(次)</td>
		<td><input type="text" name="maxcount" value="1"></td>
		<td colspan="3">(发出告警)</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">发出告警级别:</td>
		<td><select name="maxwarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">恢复告警级别:</td>
		<td><select name="maxreinstatelevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
	</tr>
	<!-- *******************************************以下是动态阈值配置项************************************ -->
	<tr name="tr_active">
		<td class="title_2">动态阀值操作符:</td>
		<td><select name="dynatype">
			<option value="0" selected>不使用</option>
			<option value="1">变化率大于</option>
			<option value="2">变化率大于等于</option>
			<option value="3">变化率小于</option>
			<option value="4">变化率小于等于</option>
			<option value="5">变化率等于</option>
			<option value="6">变化率不等于</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">数据的基准值（前几天）:</td>
		<td><input type="text" name="beforeday" value="1"></td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">阀值百分比（％）:</td>
		<td colspan="4"><input type="text" size="58" name="dynathres">
		</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">达到阀值百分比（次）</td>
		<td><input type="text" name="dynacount" value="1"></td>
		<td colspan="3">(发出告警)</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">动态阀值描述:</td>
		<td colspan="4"><input type="text" size="58" name="dynadesc">
		</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">发出告警级别:</td>
		<td><select name="dynawarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">恢复告警级别:</td>
		<td><select name="dynareinstatelevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
	</tr>
	<tr name="tr_active">
		<td colspan="5" class="foot">备注： 变化率 ＝ （（采到的值－基准值）/基准值） * 100</td>
	</tr>
	<!-- *******************************************以下是突变阈值配置项************************************ -->
	<tr name="tr_sudden">
		<td class="title_2">突变阀值操作符:</td>
		<td><select name="mutationtype">
			<option value="0" selected>不使用</option>
			<option value="1">变化率绝对值大于</option>
			<option value="2">变化率绝对值大于等于</option>
			<option value="3">变化率绝对值小于</option>
			<option value="4">变化率绝对值小于等于</option>
			<option value="5">变化率绝对值等于</option>
			<option value="6">变化率绝对值不等于</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">超出百分比（％）:</td>
		<td><input type="text" name="mutationthres"></td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">达到阀值百分比（次）</td>
		<td><input type="text" name="mutationcount" value="1"></td>
		<td colspan="3">(发出告警)</td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">突变阀值描述:</td>
		<td colspan="4"><input type="text" size="58" name="mutationdesc">
		</td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">发出告警级别:</td>
		<td colspan="4"><select name="mutationwarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select></td>
	</tr>
	<tr name="tr_sudden">
		<td colspan="5" class="foot">备注： 变化率绝对值 ＝ |（（采到的值－上次采集值）/上次采集值） * 100|</td>
	</tr>
	<tr>
		<td colspan="5" class="foot" align="center">
			<button type="submit" onclick="return CheckForm();">&nbsp;保&nbsp;存&nbsp;</button>&nbsp;&nbsp;
			<button onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
		</td>
	</tr>
</table>
</body>
</html>