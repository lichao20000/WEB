<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%--
/**
 * 编辑端口;
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-29 AM11:27:21
 * 
 * @版权 南京联创网络科技 网管产品部;
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>编辑端口</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">	
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<script type="text/javascript">
	$(function(){
		var ajax="<s:property value="ajax"/>";
		if(null != ajax && ""!=ajax){
			if(ajax=="0"){
				alert("编辑成功！");
			}else if(ajax=="-1"){
				alert("编辑失败！");
			}else if(ajax=="-2"){
				alert("数据编辑成功，通知后台失败！");
			}else{
				alert("超时，请重试！");
			}
			window.close();
		}
		//显示隐藏配置告警框	
		$("td[@name='td_show']").toggle(function(){
			$(this).html("隐藏配置 <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='点击隐藏配置告警' >");
			$("#tr_show").show();
			getWarn();
		},function(){
			$(this).html("配置告警 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='点击配置告警' >");
			$("#tr_show").hide();
		});
		
	});
	//获取告警页面
	function getWarn(){
		if($("#td_warn").html()==""){
			$("#td_warn").html("正在获取告警配置项，请等待......");
			$.post(
				"<s:url value="/performance/configFlux!getWarnJSP.action"/>",
				function(data){
					$("#td_warn").html(data);
					setWarn();
				}
			);
		}
	}
	//setwarn
	function setWarn(){
		//*****************固定阈值一***********************************************************************
		var ifinoct_maxtype     ="<s:property value="ifinoct_maxtype"/>";		//端口流入利用率阈值一比较操作符
		var ifinoctetsbps_max   ="<s:property value="ifinoctetsbps_max"/>";		//端口流入利用率阈值一(%)
		var ifoutoct_maxtype	="<s:property value="ifoutoct_maxtype"/>";		//端口流出利用率阈值一比较操作符
		var ifoutoctetsbps_max 	="<s:property value="ifoutoctetsbps_max"/>";	//端口流出利用率阈值一(%)
		var ifindiscardspps_max ="<s:property value="ifindiscardspps_max"/>";	//端口流入丢包率阈值(%)
		var ifoutdiscardspps_max="<s:property value="ifoutdiscardspps_max"/>";	//端口流出丢包率阈值(%)
		var ifinerrorspps_max 	="<s:property value="ifinerrorspps_max"/>";		//端口流入错包率阈值(%)
		var ifouterrorspps_max 	="<s:property value="ifouterrorspps_max"/>";	//端口流出错包率阈值(%)
		var warningnum 			="<s:property value="warningnum"/>";			//超出阈值的次数（发告警）
		var warninglevel 		="<s:property value="warninglevel"/>";			//发出阈值告警时的告警级别
		var reinstatelevel 		="<s:property value="reinstatelevel"/>";		//恢复告警级别
		//*****************固定阈值二***********************************************************************
		var ifinoct_mintype 	="<s:property value="ifinoct_mintype"/>";		//端口流入利用率阈值二比较操作符
		var ifinoctetsbps_min 	="<s:property value="ifinoctetsbps_min"/>";		//端口流入利用率阈值二(%)
		var ifoutoct_mintype 	="<s:property value="ifoutoct_mintype"/>";		//端口流出利用率阈值二比较操作符
		var ifoutoctetsbps_min 	="<s:property value="ifoutoctetsbps_min"/>";	//端口流出利用率阈值二(%)
		var warningnum_min 		="<s:property value="warningnum_min"/>";		//超出阈值二的次数（发告警）
		var warninglevel_min 	="<s:property value="warninglevel_min"/>";		//发出阈值二告警时的告警级别
		var reinlevel_min 		="<s:property value="reinlevel_min"/>";			//阈值二恢复告警级别
		//*****************动态阈值一***********************************************************************
		var overmax 			="<s:property value="overmax"/>";				//动态阈值一操作符
		var overper 			="<s:property value="overper"/>";				//动态阈值一(%)
		var overnum 			="<s:property value="overnum"/>";				//超出动态阈值一的次数(发告警)
		var overlevel 			="<s:property value="overlevel"/>";				//发出动态阈值一告警时的告警级别
		var reinoverlevel		="<s:property value="reinoverlevel"/>";			//发出恢复告警时的级别
		//*****************动态阈值二************************************************************************
		var overmin 			="<s:property value="overmin"/>";				//动态阈值二操作符
		var overper_min 		="<s:property value="overper_min"/>";			//动态阈值二(%)
		var overnum_min 		="<s:property value="overnum_min"/>";			//超出动态阈值二次数(发告警)
		var overlevel_min 		="<s:property value="overlevel_min"/>";			//发出动态阈值二告警时的告警级别
		var reinoverlevel_min 	="<s:property value="reinoverlevel_min"/>";		//发出恢复告警时的级别
		var com_day 			="<s:property value="com_day"/>";				//生成动态阈值一的天数(天)
		//*****************突变阈值**************************************************************************
		var intbflag 			="<s:property value="intbflag"/>";				//判断是否配置流入突变告警操作
		var ifinoctets 			="<s:property value="ifinoctets"/>";			//流入速率变化率阈值(%)
		var inoperation 		="<s:property value="inoperation"/>";			//流入速率突变告警操作符
		var inwarninglevel 		="<s:property value="inwarninglevel"/>";		//流入速率突变告警级别
		var inreinstatelevel 	="<s:property value="inreinstatelevel"/>";		//流入速率恢复突变告警级别
		var outtbflag 			="<s:property value="outtbflag"/>";				//是否配置流出突变告警操作
		var ifoutoctets 		="<s:property value="ifoutoctets"/>";			//流出速率变化率阈值(%)
		var outoperation 		="<s:property value="outoperation"/>";			//流出速率突变告警操作符
		var outwarninglevel 	="<s:property value="outwarninglevel"/>";		//流出速率突变告警级别
		var outreinstatelevel 	="<s:property value="outreinstatelevel"/>";		//流出速率恢复突变告警级别
		//*************************************************************************************************
		//*********************************江苏项目组***********************************************
		if($("select[@name='ifinoct_maxtype']").length>0){
			//端口流入利用率阈值一比较操作符
			checkSelectDataOne(ifinoct_maxtype,ifinoctetsbps_max,"ifinoct_maxtype",$("input[@name='ifinoctetsbps_max']"));
			//端口流出利用率阈值一比较操作符
			checkSelectDataOne(ifoutoct_maxtype,ifoutoctetsbps_max,"ifoutoct_maxtype",$("input[@name='ifoutoctetsbps_max']"));
			checkTextData(ifindiscardspps_max,$("#c_disinmax"),$("input[@name='ifindiscardspps_max']"));//端口流入丢包率阈值(%)
			checkTextData(ifoutdiscardspps_max,$("#c_disoutmax"),$("input[@name='ifoutdiscardspps_max']"));//端口流出丢包率阈值(%)
			checkTextData(ifinerrorspps_max,$("#c_errinmax"),$("input[@name='ifinerrorspps_max']"));//端口流入错包率阈值(%)
			checkTextData(ifouterrorspps_max,$("#c_erroutmax"),$("input[@name='ifouterrorspps_max']"));//端口流出错包率阈值(%)
			$("input[@name='warningnum']").val(warningnum);//超出阈值的次数（发告警）
			//发出阈值告警时的告警级别
			$("select[@name='warninglevel'] option").each(function(){
				if($(this).val()==warninglevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//恢复告警级别
			$("select[@name='reinstatelevel'] option").each(function(){
				if($(this).val()==reinstatelevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//端口流入利用率阈值二比较操作符
			checkSelectDataOne(ifinoct_mintype,ifinoctetsbps_min,"ifinoct_mintype",$("input[@name='ifinoctetsbps_min']"));
			//端口流出利用率阈值二比较操作符
			checkSelectDataOne(ifoutoct_mintype,ifoutoctetsbps_min,"ifoutoct_mintype",$("input[@name='ifoutoctetsbps_min']"));
			$("input[@name='warningnum_min']").val(warningnum_min);//超出阈值二的次数（发告警）
			//发出阈值二告警时的告警级别
			$("select[@name='warninglevel_min'] option").each(function(){
				if($(this).val()==warninglevel_min){
					$(this).attr("selected",true);
					return;
				}
			});
			//阈值二恢复告警级别
			$("select[@name='reinlevel_min'] option").each(function(){
				if($(this).val()==reinlevel_min){
					$(this).attr("selected",true);
					return;
				}
			});
			//动态阈值－设置
			checkSelectDataTwo(overmax,overper,overnum,overlevel,reinoverlevel,"overmax",$("input[@name='overper']"),$("input[@name='overnum']"),"overlevel","reinoverlevel");
			//动态阈值二设置
			checkSelectDataTwo(overmin,overper_min,overnum_min,overlevel_min,reinoverlevel_min,"overmin",$("input[@name='overper_min']"),$("input[@name='overnum_min']"),"overlevel_min","reinoverlevel_min");
			//生成动态阈值一的天数(天)
			$("input[@name='com_day']").val(com_day);
			//启用流入流量突变告警配置  
			if(intbflag=="1"){
				$("#c_intbflag").attr("checked",true);
				//流入速率变化率阈值(%)
				$("input[@name='ifinoctets']").val(ifinoctets);
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				//流入速率突变告警操作符
				$("select[@name='inoperation']").attr("disabled",false);
				$("select[@name='inoperation'] option").each(function(){
					if($(this).val()==inoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//流入速率突变告警级别
				$("select[@name='inwarninglevel']").attr("disabled",false);
				$("select[@name='inwarninglevel'] option").each(function(){
					if($(this).val()==inwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//流入速率恢复突变告警级别
				$("select[@name='inreinstatelevel']").attr("disabled",false);
				$("select[@name='inreinstatelevel'] option").each(function(){
					if($(this).val()==inreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//启用流出流量突变告警配置
			if(outtbflag=="1"){
				$("#c_outtbflag").attr("checked",true);
				//流出速率变化率阈值(%)
				$("input[@name='ifoutoctets']").val(ifinoctets);
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				//流出速率突变告警操作符
				$("select[@name='outoperation']").attr("disabled",false);
				$("select[@name='outoperation'] option").each(function(){
					if($(this).val()==outoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//流出速率突变告警级别
				$("select[@name='outwarninglevel']").attr("disabled",false);
				$("select[@name='outwarninglevel'] option").each(function(){
					if($(this).val()==outwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//流出速率恢复突变告警级别
				$("select[@name='outreinstatelevel']").attr("disabled",false);
				$("select[@name='outreinstatelevel'] option").each(function(){
					if($(this).val()==outreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
		}
		//*********************************其他项目组***********************************************
		if($("#c_inmax").length>0){
			checkTextData(ifinoctetsbps_max,$("#c_inmax"),$("input[@name='ifinoctetsbps_max']"));//端口流入带宽利用率阈值(%)
			checkTextData(ifoutoctetsbps_max,$("#c_outmax"),$("input[@name='ifoutoctetsbps_max']"));//端口流出带宽利用率阈值(%)
			checkTextData(ifindiscardspps_max,$("#c_disinmax"),$("input[@name='ifindiscardspps_max']"));//端口流入丢包率阈值(%) 
			checkTextData(ifoutdiscardspps_max,$("#c_disoutmax"),$("input[@name='ifoutdiscardspps_max']"));//端口流出丢包率阈值(%)  
			checkTextData(ifinerrorspps_max,$("#c_errinmax"),$("input[@name='ifinerrorspps_max']"));//端口流入错包率阈值(%) 
			checkTextData(ifouterrorspps_max,$("#c_erroutmax"),$("input[@name='ifouterrorspps_max']"));//端口流出错包率阈值(%) 
			$("input[@name='warningnum']").val(warningnum);//超出阈值的次数（发告警）
			//发出阈值告警时的告警级别
			$("select[@name='warninglevel'] option").each(function(){
				if($(this).val()==warninglevel){
					$(this).attr("selected",true);
					return;
				}
			});
			//恢复告警级别
			$("select[@name='reinstatelevel'] option").each(function(){
				if($(this).val()==reinstatelevel){
					$(this).attr("selected",true);
					return;
				}
			});
			if($.trim(overper)!="" && eval(overper)>0){
				$("#c_usedynamic").attr("checked",true);//启用动态阈值告警
				$("input[@name='overper']").val(overper);//超出动态阈值的百分比(%)
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overnum']").val(overnum);//超出百分比次数(发告警)
				$("input[@name='overnum']").attr("class","");
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='com_day']").val(com_day);//生成动态阈值一的天数(天)
				$("input[@name='com_day']").attr("class","");
				$("input[@name='com_day']").attr("readonly",false);
				$("select[@name='overlevel']").attr("disabled",false);//发出动态阈值告警时的告警级别
				$("select[@name='overlevel'] option").each(function(){
					if($(this).val()==overlevel){
						$(this).attr("selected",true);
						return;
					}
				});
				$("select[@name='reinoverlevel']").attr("disabled",false);//发出恢复告警时的级别
				$("select[@name='reinoverlevel'] option").each(function(){
					if($(this).val()==reinoverlevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//启用流入流量突变告警配置  
			if(intbflag=="1"){
				$("#c_intbflag").attr("checked",true);
				//流入速率变化率阈值(%)
				$("input[@name='ifinoctets']").val(ifinoctets);
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				//流入速率突变告警操作符
				$("select[@name='inoperation']").attr("disabled",false);
				$("select[@name='inoperation'] option").each(function(){
					if($(this).val()==inoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//流入速率突变告警级别
				$("select[@name='inwarninglevel']").attr("disabled",false);
				$("select[@name='inwarninglevel'] option").each(function(){
					if($(this).val()==inwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//流入速率恢复突变告警级别
				$("select[@name='inreinstatelevel']").attr("disabled",false);
				$("select[@name='inreinstatelevel'] option").each(function(){
					if($(this).val()==inreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
			//启用流出流量突变告警配置
			if(outtbflag=="1"){
				$("#c_outtbflag").attr("checked",true);
				//流出速率变化率阈值(%)
				$("input[@name='ifoutoctets']").val(ifinoctets);
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				//流出速率突变告警操作符
				$("select[@name='outoperation']").attr("disabled",false);
				$("select[@name='outoperation'] option").each(function(){
					if($(this).val()==outoperation){
						$(this).attr("selected",true);
						return;
					}
				});
				//流出速率突变告警级别
				$("select[@name='outwarninglevel']").attr("disabled",false);
				$("select[@name='outwarninglevel'] option").each(function(){
					if($(this).val()==outwarninglevel){
						$(this).attr("selected",true);
						return;
					}
				});
				//流出速率恢复突变告警级别
				$("select[@name='outreinstatelevel']").attr("disabled",false);
				$("select[@name='outreinstatelevel'] option").each(function(){
					if($(this).val()==outreinstatelevel){
						$(this).attr("selected",true);
						return;
					}
				});
			}
		}
	}
	//检查Text数据是否有效
	function checkTextData(data,chk_target,text_target){
		if(data!=null && $.trim(data)!="" && eval(data)>0){
			chk_target.attr("checked",true);
			text_target.attr("readonly",false);
			text_target.attr("class","");
			text_target.val(data);
		}
	}
	//检查Select数据是否有效
	function checkSelectDataOne(sel_data,text_data,sel_name,text_target){
		if(eval(sel_data)>0){
			text_target.attr("readonly",false);
			text_target.attr("class","");
			text_target.val(text_data);
			$("select[@name="+sel_name+"] option").each(function(){
				if($(this).val()==sel_data){
					$(this).attr("selected",true);
					return;
				}
			});
		}
	}
	//检查Select数据是否有效
	function checkSelectDataTwo(sel_data,text1_data,text2_data,sel1_data,sel2_data,sel_name,text1_target,text2_target,sel1_name,sel2_name){
		if(eval(sel_data)>0){
			text1_target.attr("readonly",false);
			text1_target.attr("class","");
			text1_target.val(text1_data);
			text2_target.attr("readonly",false);
			text2_target.attr("class","");
			text2_target.val(text2_data);
			$("select[@name='"+sel1_name+"']").attr("disabled",false);
			$("select[@name='"+sel2_name+"']").attr("disabled",false);
			$("select[@name='"+sel_name+"'] option").each(function(){
				if($(this).val()==sel_data){
					$(this).attr("selected",true);
					return;
				}
			});
			$("select[@name='"+sel2_name+"'] option").each(function(){
				if($(this).val()==sel2_data){
					$(this).attr("selected",true);
					return;
				}
			});
			$("select[@name='"+sel1_name+"'] option").each(function(){
				if($(this).val()==sel1_data){
					$(this).attr("selected",true);
					return;
				}
			});
		}
	}
	//检查选中选择项
	function chkSelData(sel_name,sel_val){
		$("select[@name='"+sel_name+"' option]").each(function(){
			if($(this).val()==sel_val){
				$(this).attr("selected",true);
			}
		});
	}
	//保存端口
	function SavePort(){
		if($("#td_warn").html()!=""){
			if(!$.checkFluxWarn()){
				return false;
			}
		}
		$("form").submit();
		return false;
	}
</script>
<style type="text/css">
   td.no{
		color:red;
	}
</style>
</head>
<body>
	<form action="configFlux!SavePort.action">
		<br>
		<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
		<input type="hidden" name="port_info" value="<s:property value="port_info"/>"/>
		<table width="94%" class="querytable" align="center">
			<tr>
				<th colspan="4" class="title_1"><s:property value="device_name+'【'+loopback_ip+'】'"/>端口信息</th>
			</tr>
			<tr>
				<td class="title_2" width="20%">设备IP</td>
				<td width="30%">
					<input type="text" name="loopback_ip" readonly class="onread" value="<s:property value="loopback_ip"/>">
				</td>
				<td class="title_2" width="20%">端口索引</td>
				<td width="30%">
					<input type="text" name="ifindex" readonly class="onread" value="<s:property value="ifindex"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">端口描述</td>
				<td>
					<input type="text" name="ifdescr" readonly class="onread" value="<s:property value="ifdescr"/>">
				</td>
				<td class="title_2">端口名字</td>
				<td>
					<input type="text" name="ifname" readonly class="onread" value="<s:property value="ifname"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">端口别名</td>
				<td>
					<input type="text" name="ifnamedefined" readonly class="onread" value="<s:property value="ifnamedefined"/>">
				</td>
				<td class="title_2">端口类型</td>
				<td>
					<input type="text" name="iftype" readonly class="onread" value="<s:property value="iftype"/>"> 
				</td>
			</tr>
			<tr>
				<td class="title_2">端口速率(bps)</td>
				<td>
					<input type="text" name="ifspeed" readonly class="onread" value="<s:property value="ifspeed"/>">
				</td>
				<td class="title_2">端口最大传输单元</td>
				<td>
					<input type="text" name="ifmtu" readonly class="onread" value="<s:property value="ifmtu"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">高速端口速率(bps)</td>
				<td colspan="3">
					<input type="text" name="ifhighspeed" readonly class="onread" value="<s:property value="ifhighspeed"/>">
				</td>
			</tr>
			<tr>
				<td class="title_2">是否采集端口流量信息</td>
				<td>
					<select name="gatherflag">
						<option value="1" "<s:property value="gatherflag==1?'selected':''"/>">采集</option>
						<option value="0" "<s:property value="gatherflag==0?'selected':''"/>">不采集</option>
					</select>
				</td>
				<td class="title_2">原始数据是否入库</td>
				<td>
					<select name="intodb">
						<option value="1" "<s:property value="intodb==1?'selected':''"/>">入库</option>
						<option value="0" "<s:property value="intodb==0?'selected':''"/>">不入库</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="title_1" name="td_show">
					配置告警
					<img src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12" align="center" border="0" alt="点击配置告警" >
				</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<tr id="tr_show" style="display:none;">
				<td id="td_warn" colspan="4">
					
				</td>
			</tr>
			<tr>
				<td class="foot" colspan="4" align="right">
					<button onclick="SavePort()">保存</button>
					<button onclick="window.close();">关闭</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>