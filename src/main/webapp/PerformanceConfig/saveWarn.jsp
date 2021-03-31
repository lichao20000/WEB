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
 * 配置告警页面
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-13 AM 10:13:19
 * 
 * @版权 南京联创 网管产品部;
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>配置告警</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">	
<script type="text/javascript">
	var flg="<s:property value="ajax"/>";
	if(flg!=null && flg!=""){
		if(flg=="true"){
			alert("编辑成功！");
			window.close();
		}else{
			alert("编辑失败！");
		}
	}
	$(function(){
		$.init("show");
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
	
	//CheckForm
	function CheckForm(){
		if(!$.CheckWarn()){
			return false;
		}
		var url="<s:url value="/performance/configPmee!SaveWarn.action"/>";
		$("form[@name='frm']").attr("action",url);
		$("form[@name='frm']").submit();
		return true;
	}
</script>
</head>
<body>
	<form name="frm">
	<br>
	<table align="center" width="94%" class="querytable" id="tab_warn">
			<tr>
				<td colspan="5">
					<table width="450" class="tab" align="left">
						<tr>
							<td class="mouseon" name="td_fix" 
								onMouseOver="this.className='mouseon';" 
								onMouseOut="this.className='mouseon';"
                      		>固定阀值配置</td>
							<td class="mouseout" name="td_active"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';" 
                      		>动态阀值配置</td>
							<td class="mouseout" name="td_sudden"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';" 
                      		>突变阀值配置</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- *******************************************以下是固定阈值配置项************************************ -->
			<tr name="tr_fix">
				<td class="title_2">比较操作符一:</td>
				<td>
					<select name="mintype">
						<option value="0" selected>不使用</option>
						<option value="1">大于</option>
						<option value="2">大于等于</option>
						<option value="3">小于</option>
						<option value="4">小于等于</option>
						<option value="5">等于</option>
						<option value="6">不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">固定阀值一:</td>
				<td>
					<input type="text" name="minthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">固定阀值一描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="mindesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">连续超出阀值一(次)</td>
				<td>
					<input type="text" name="mincount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="minwarninglevel">
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
				<td>
					<!--<select name="minreinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
					<s:select list="#request.warnMap" listKey="key" listValue="value" name="minreinstatelevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">比较操作符二:</td>
				<td>
					<select name="maxtype">
						<option value="0" selected>不使用</option>
						<option value="1">大于</option>
						<option value="2">大于等于</option>
						<option value="3">小于</option>
						<option value="4">小于等于</option>
						<option value="5">等于</option>
						<option value="6">不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">固定阀值二:</td>
				<td>
					<input type="text" name="maxthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">固定阀值二描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="maxdesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">连续超出阀值二(次)</td>
				<td>
					<input type="text" name="maxcount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="maxwarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
					<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		
				</td>
				<td class="title_2"></td>
				<td class="title_2">恢复告警级别:</td>
				<td>
					<!--<select name="maxreinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxreinstatelevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		</td>
			</tr>
			<!-- *******************************************以下是动态阈值配置项************************************ -->
			<tr name="tr_active">
				<td class="title_2">动态阀值操作符:</td>
				<td>
					<select name="dynatype">
						<option value="0" selected>不使用</option>
						<option value="1">变化率大于</option>
						<option value="2">变化率大于等于</option>
						<option value="3">变化率小于</option>
						<option value="4">变化率小于等于</option>
						<option value="5">变化率等于</option>
						<option value="6">变化率不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">数据的基准值（前几天）:</td>
				<td>
					<input type="text" name="beforeday" value="1">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">阀值百分比（％）:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynathres">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">达到阀值百分比（次）</td>
				<td>
					<input type="text" name="dynacount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">动态阀值描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynadesc">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="dynawarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynawarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		        </td>
				<td class="title_2"></td>
				<td class="title_2">恢复告警级别:</td>
				<td>
					<!--<select name="dynareinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynareinstatelevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		        </td>
			</tr>
			<tr name="tr_active">
				<td colspan="5" class="foot">备注：  变化率 ＝ （（采到的值－基准值）/基准值） * 100</td>
			</tr>
			<!-- *******************************************以下是突变阈值配置项************************************ -->
			<tr name="tr_sudden">
				<td class="title_2">突变阀值操作符:</td>
				<td>
					<select name="mutationtype">
						<option value="0" selected>不使用</option>
						<option value="1">变化率绝对值大于</option>
						<option value="2">变化率绝对值大于等于</option>
						<option value="3">变化率绝对值小于</option>
						<option value="4">变化率绝对值小于等于</option>
						<option value="5">变化率绝对值等于</option>
						<option value="6">变化率绝对值不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">超出百分比（％）:</td>
				<td>
					<input type="text" name="mutationthres">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">达到阀值百分比（次）</td>
				<td>
					<input type="text" name="mutationcount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">突变阀值描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="mutationdesc">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">发出告警级别:</td>
				<td colspan="4">
					<!--<select name="mutationwarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="mutationwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
		        </td>
			</tr>
			<tr name="tr_sudden">
				<td colspan="5" class="foot">备注：  变化率绝对值 ＝ |（（采到的值－上次采集值）/上次采集值） * 100|</td>
			</tr>
			<!-- *************************************************END******************************************** -->
			<tr>
				<td colspan="5" class="foot" align="center">
					<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
					<input type="hidden" name="expressionid" value="<s:property value="expressionid"/>">
					<button type="submit" onclick="return CheckForm();">&nbsp;保&nbsp;存&nbsp;</button>&nbsp;&nbsp;
					<button onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>