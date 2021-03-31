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
 * 性能配置页面【单个配置和批量配置走同一个流程】
 * 单个设备配置前先检查该性能表达式是否已经配置过，如果已经配置过则提示用户
 * 多个设备配置时不提示用户是否配置过，直接删除以前配置项。
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-9-25 11:44:14
 *
 * 版权：南京联创网络科技 网管产品部;
 *******************************************修改记录**********************************************
 * 序号     时间       修改人          需求&BUG单号     修改内容                              备注
 *-----------------------------------------------------------------------------------------------
 *  1  2008-10-17   BENYP(5260)     无          初始化页面增加查询模板配置的性能表达式  和北京酒店网管模板融合
 *-----------------------------------------------------------------------------------------------
 *  2  2008-10-17   BENYP(5260)     无          配置结果项增加采集时间间隔，并能够修改  贵州要求
 *-----------------------------------------------------------------------------------------------
 *  3  2008-10-23   BENYP(5260)     无          将原始数据默认为入库                   沈克俭要求
 ************************************************************************* **********************
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能配置</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<script language="javascript">
	$(function(){
		$.init("hide");//初始化
		var flg="<s:property value="ajax"/>";
		if(flg!=null && flg!=""){
			if(flg=="true"){
				alert("配置成功！");
				window.location="<s:url value="/performance/configPmee.action"/>?isbatch=<s:property value="isbatch"/>&device_id=<s:property value="device_id"/>";
			}else{
				alert("配置失败！");
			}
		}
		window.setInterval("getConfigExp();",1000*60*2);//刷新获取已经配置的性能表达式
		//双击选择性能表达式
		$("select[@name='ex_perssion']").dblclick(function(){
			$("select[@name='sel_expression']").append($("select[@name='ex_perssion'] option:selected"));
		});
		//双击移除性能表达式
		$("select[@name='sel_expression']").dblclick(function(){
			$("select[@name='ex_perssion']").append($("select[@name='sel_expression'] option:selected"));
		});
		//多选选择性能表达式
		$("button[@name='add']").click(function(){
			$("select[@name='sel_expression']").append($("select[@name='ex_perssion'] option:selected"));
		});
		//多选移除性能表达式
		$("button[@name='del']").click(function(){
			$("select[@name='ex_perssion']").append($("select[@name='sel_expression'] option:selected"));
		});
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
		//显示隐藏配置告警框
		$("td[@name='td_show']").toggle(function(){
			$(this).html("隐藏配置 <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='点击隐藏配置告警' >");
			$("tr[@name='tr_show']").show();
			$.showChage();
		},function(){
			$(this).html("配置告警 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='点击配置告警' >");
			$.init("hide");
		});
	});

	//配置告警
	function ConfigWarn(expression_id){
		var url="<s:url value="/performance/configPmee!ConfigWarn.action"/>?device_id=<s:property value="device_id"/>&expressionid="+expression_id;
		window.open(url);
	}

	//刷新获取已经配置的性能表达式
	function getConfigExp(){
		$.post(
			"<s:url value="/performance/configPmee!getConfigExp.action"/>",
			{
				device_id:"<s:property value="device_id"/>"
			},
			function(data){
				$("#tbody").html(data);
			}
		);
	}

	//显示配置详细信息
	function showDetail(device_id,name,ip,expressionid){
		var url="<s:url value="/performance/configPmee!showDetail.action"/>?expressionid="+expressionid+"&device_id="+device_id+"&t="+new Date();
		window.open(url);
	}
	//刷新性能表达式
	function RefreshExp(expressionid){
		$("input[@name='expressionid']").val(expressionid);
		$("select[@name='keep']").attr("selectedIndex",0);
		if($.CheckWarn()){
			$("form").submit();
		}
		return false;
	}
	//删除性能表达式
	function Del(device_id,expressionid,target){
		$.post(
			"<s:url value="/performance/configPmee!delExp.action"/>",
			{
				device_id:device_id,
				expressionid:expressionid,
				id:"",
				type:"all"
			},
			function(data){
				if(data=="true"){
					alert("成功删除配置！");
					target.parent().parent().remove();
				}else{
					alert("删除配置失败，请重试！");
				}
			}
		);
	}

	//CHECK FORM
	function CheckForm(){
		if($("select[@name='sel_expression']").html()==""){
			alert("请至少选择一个性能表达式!");
			return false;
		}else if($("input[@name='interval']").val()==""){
			alert("请输入采集时间间隔！");
			$("input[@name='interval']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='interval']").val(),'int')){
			alert("采集时间间隔只能为整数，请重新输入！");
			$("input[@name='interval']").focus();
			$("input[@name='interval']").select();
			return false;
		}else if($.CheckWarn()){
			var expressionid="";
			$("select[@name='sel_expression'] option").each(function(){
				expressionid+=","+$(this).val();
			});
			$("input[@name='expressionid']").val(expressionid.substring(1));
			if($("input[@name='isbatch']").val()=="false"){
				$.post(
					"<s:url value="/performance/configPmee!CheckConfig.action"/>",
					{
						device_id:"'"+$("input[@name='device_id']").val()+"'",
						expressionid:expressionid.substring(1)
					},
					function(data){
						if(data!=null && data!=""){
							if(window.confirm("以下性能表达式:\n"+data+"\n已经配置，是否重新配置？")){
								$("form").submit();
							}
						}else{
							$("form").submit();
						}
					}
				);
			}else{
				$("form").submit();
			}
			return false;
		}
		return false;
	}
</script>
<style type="text/css">
.red {
	color: red;
}

.blue {
	color: blue;
}
</style>
</head>
<body>
<form action="<s:url value="/performance/configPmee!Config.action"/>">
<br>
<table width="94%" align="center" class="querytable">
	<tr>
		<s:if test="isbatch==false">
			<th colspan="5" class="title_1">性能配置(<s:property
				value="DevInfoMap.device_name" />【<s:property
				value="DevInfoMap.loopback_ip" />】)</th>
		</s:if>
		<s:else>
			<th colspan="5" class="title_1">性能配置</th>
		</s:else>
	</tr>
	<tr>
		<td colspan="2" class="title_1">可选择设备性能</td>
		<td class="title_1"></td>
		<td colspan="2" class="title_1">已选择设备性能</td>
	</tr>
	<tr>
		<td colspan="2"><select size="10" multiple="multiple" style="width: 100%;" name="ex_perssion">
			<s:iterator var="exp" value="ExpList">
				<option value="<s:property value="#exp.expressionid"/>"><s:property
					value="#exp.name" /></option>
			</s:iterator>
		</select></td>
		<td align="center" valign="middle" class="title_2">
		<button name="add">&nbsp;>>&nbsp;</button>
		<br>
		<br>
		<br>
		<br>
		<button name="del">&nbsp;<<&nbsp;</button>
		</td>
		<td colspan="2"><select size="10" multiple="multiple"
			style="width: 100%;" name="sel_expression">
			<s:iterator var="expu" value="ExpListUse">
				<option selected value="<s:property value="#expu.expressionid"/>"><s:property
					value="#expu.name" /></option>
			</s:iterator>
		</select></td>
	</tr>
	<tr>
		<td width="20%" class="title_2">采集时间间隔：</td>
		<td width="25%"><input type="text" size="8" name="interval"
			value="300">(秒)</td>
		<td width="10%" class="title_2"></td>
		<td width="20%" class="title_2">原始数据是否入库:</td>
		<td width="25%"><select name="intodb">
			<option value="1" selected>是</option>
			<option value="0">否</option>
		</select></td>
	</tr>
	<tr>
		<td class="title_2">是否保留原有配置</td>
		<td colspan="4"><select name="iskeep">
			<option value="true">保留原有配置</option>
			<option value="false">重新配置</option>
		</select></td>
	</tr>
	<tr>
		<td colspan="5" class="title_1" name="td_show">配置告警 <img
			src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12"
			align="center" border="0" alt="点击配置告警"></td>
	</tr>
	<tr>
		<td colspan="5">&nbsp;</td>
	</tr>
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
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"  value="-1"/>
			</td>
		<td class="title_2"></td>
		<td class="title_2">恢复告警级别:</td>
		<td><!--<select name="minreinstatelevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minreinstatelevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>
		</td>
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
		<td>
		<!--<select name="maxwarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxwarninglevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>
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
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxreinstatelevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>
		</td>
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
		<td>
		<!--<select name="dynawarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>
		-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynawarninglevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>
		</td>
		<td class="title_2"></td>
		<td class="title_2">恢复告警级别:</td>
		<td><!--<select name="dynareinstatelevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynareinstatelevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>

		</td>
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
		<td colspan="4"><!--<select name="mutationwarninglevel">
			<option value="0" selected>请选择</option>
			<option value="1">正常日志</option>
			<option value="2">提示告警</option>
			<option value="3">一般告警</option>
			<option value="4">严重告警</option>
			<option value="5">紧急告警</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="mutationwarninglevel" theme="simple" headerKey="-1" headerValue="请选择" value="-1"/>
		</td>
	</tr>
	<tr name="tr_sudden">
		<td colspan="5" class="foot">备注： 变化率绝对值 ＝ |（（采到的值－上次采集值）/上次采集值） *
		100|</td>
	</tr>
	<!-- *************************************************END******************************************** -->
	<tr>
		<td colspan="5" class="foot" align="center"><input type="hidden"
			name="device_id" value="<s:property value="device_id"/>"> <input
			type="hidden" name="expressionid"> <input type="hidden"
			name="isbatch" value="<s:property value="isbatch"/>">
		<button type="submit" onclick="return CheckForm();">&nbsp;保&nbsp;存&nbsp;</button>
		&nbsp;&nbsp; <s:if test="!ismodule">
			<button onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
		</s:if></td>
	</tr>
</table>
<br>
<!-- 配置结果展示【如果是批量配置则不显示这项】 --> <s:if
	test="ConfigResultList!=null && ConfigResultList.size()>0">
	<table width="94%" align="center" class="listtable">
		<thead>
			<tr>
				<th colspan="6">设备(<s:property value="DevInfoMap.device_name" />【<s:property
					value="DevInfoMap.loopback_ip" />】)配置结果</th>
			</tr>
			<tr>
				<th width="25%" nowrap>表达式名称</th>
				<th width="25%" nowrap>表达式描述</th>
				<th width="10%" nowrap>采集时间间隔</th>
				<th width="8%" nowrap>配置状态</th>
				<th width="12%" nowrap>失败原因</th>
				<th width="20%" nowrap>操作</th>
			</tr>
		</thead>
		<tbody id="tbody">
			<s:if test="ConfigResultList==null || ConfigResultList.size()==0">
				<tr>
					<td colspan="6">该设备暂时未配置任何性能表达式</td>
				</tr>
			</s:if>
			<s:else>
				<s:iterator var="crlist" value="ConfigResultList">
					<tr style="color:<s:property value="#crlist.isok<1 ?'red':''"/>">
						<td><s:property value="#crlist.name" /></td>
						<td><s:property value="#crlist.descr" /></td>
						<td><label onclick="$.showT($(this));"><s:property
							value="#crlist.interval" /></label> <input type="text" size="8"
							style="display: none;"
							onmouseout="$.MO('<s:property value="#crlist.interval"/>',$(this))"
							onchange="$.changeT('<s:property value="#crlist.interval"/>',
									       					   '<s:property value="#crlist.expressionid"/>',
									       					   '<s:property value="#crlist.device_id"/>',
									                           $(this));"
							value="<s:property value="#crlist.interval"/>"></td>
						<td><s:property value="#crlist.result" /></td>
						<td><s:property value="#crlist.remark" /></td>
						<td><a href="#"
							onclick="showDetail('<s:property value="#crlist.device_id"/>',
																	'<s:property value="#crlist.device_name"/>',
									                                '<s:property value="#crlist.loopback_ip"/>',
									                                '<s:property value="#crlist.expressionid"/>');">详细</a>&nbsp;
						<a href="#"
							onclick="RefreshExp('<s:property value="#crlist.expressionid"/>')">刷新</a>&nbsp;
						<a href="#"
							onclick="ConfigWarn('<s:property value="#crlist.expressionid"/>')">配置告警</a>&nbsp;
						<a href="#"
							onclick="Del('<s:property value="device_id"/>',
									                         '<s:property value="#crlist.expressionid"/>',
									                         $(this))">删除</a>&nbsp;
						</td>
					</tr>
				</s:iterator>
			</s:else>
		</tbody>
	</table>
</s:if> <br>
</form>
</body>
</html>
