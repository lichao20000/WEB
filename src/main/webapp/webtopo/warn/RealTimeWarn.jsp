<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>实时告警牌</title>
<%--
    /**
         * 业务平台-实时告警牌
         *
         * @author 贲友朋(5260) email:benyp@lianchuang.com
         * @version 1.0
         * @since 2008-03-07
         * @category WebTopo
         */
--%>
<script src="<s:url value="/webtopo/coolmenu.js"/>"></script>
<script src="<s:url value="/Js/jquery.js"/>"></script>
<script src="<s:url value="/Js/jquery.tablesorter_LINKAGE.js"/>"></script>
<script src="<s:url value="/webtopo/warn/jquery.bussTopo.RealTimeWarn.js"/>"></script>
<script src="<s:url value="/webtopo/warn/RealTimeWarn.js"/>"></script>

<script language="JavaScript">
	var issound=true;//是否发声
	var alarmlevel=0;//是否有告警发生,0为不告警，其他为告警
	var update_flg=true;//是否更新过数据(排序用)
	var alarmInfo = eval("("+"<s:property value="warnInfo"/>"+")");
	var debug=eval("<s:property value="debug"/>");//是否显示debug

  $(function(){
	  if(debug) $("#test").show();
    //得到初始化数据
	getFirstData();
    //自动刷新与手动刷新
    $("input[@name='ref']").click(function(){
		ChangeAutoOrHandRef($(this).val(),$("input[@name='reftime'][@checked]").val()*60);
    });
    //点击刷新
    $("#refbtn").click(function(){
		$.RefData();
    });
	//自动刷新设定时间
	$("input[@name='reftime']").click(function(){
		SetRefTime($(this).val()*60);
		ShowTimeRef();
	});
	//告警声音是否发声
	$("#sndTg").toggle(function(){
		$(this).attr("src","<s:url value="/images/sound_voice2.gif"/>");
		issound=false;
	},function(){
		$(this).attr("src","<s:url value="/images/sound_voice.gif"/>");
		issound=true;
	});
	//切换规则
	$("select[@name='ruleid']").change(function(){
		rid=($(this).val()).split("-/-");
		if($("input[@name='usemodule'][@checked]").val()==0){
			url="<s:url value="/webtopo/RealTimeWarn.action"/>"+"?rule="+rid[0]+"&max="+rid[1]+"&time="+(new Date().getTime());
			//window.open(url);
			window.location=url;
			$.InitModule();
		}else{
			$("input[@name='rule']").val(rid[0]);
			$("input[@name='max']").val(rid[1]);
		}

	});

  });
  //确认退出
  function ConfigRelad(){
	this.focus();
	if(window.confirm("取得数据超时次数过多，需要重新登陆，是否重新登陆?")){
		window.open("<s:url value="/login.jsp"/>");
	 }
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=off&loop=0");
	 timeout=0;
	 $("#timeout").html("");
  }
	//超时退出
  function ReloadWEB(){
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=on&loop=1");
	 window.setTimeout("ConfigRelad()",1000);
  }
  //超时发声
  function OutTimeVoice(){
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=on&loop=0");
  }

//获取第一次数据
 function getFirstData(){
	var t=new Date().getTime();
	if(debug) $("#test ul").prepend("<li>第一次取数据************</li>");
	$.loading(0);
	$.InitModule();
	$.post(
		"<s:url value="/webtopo/RealTimeWarn!getALLWarnData.action"/>",
	    {
			ruleid:$("input[@name='rule']").val(),
			fetchCount:$("input[@name='max']").val()
		},
		function(data){
		if(debug) 	$("#test ul").prepend("<li>数据获取完毕，准备载入************"+(new Date().getTime()-t)+"</li>");
			$.loading(2);
			$("#table").html(data);
		if(debug) 	$("#test ul").prepend("<li>数据载入页面************"+(new Date().getTime()-t)+"</li>");
			$("#data").tablesorter();
		if(debug) 	$("#test ul").prepend("<li>数据排序准备结束************"+(new Date().getTime()-t)+"</li>");
			$.StatWarnNum();
		if(debug) 	$("#test ul").prepend("<li>统计告警数目************"+(new Date().getTime()-t)+"</li>");
		if(debug) 	$("#test ul").prepend("<li>告警发声************"+(new Date().getTime()-t)+"</li>");
			$.showTime();
			SetRefTime($("input[@name='reftime']").val()*60);
			ShowTimeRef();
			//$.DisplayStr();
			$.MouseEvent($("#tbody tr"));
			$.loading(3);
			getRefWarn();
			//点击TH排序之前将数据载入内存
			  $("#thead tr th").click(function(){
				 if(!update_flg){
					$("#data").trigger("update");
					update_flg=true;
				 }
			  });
			alarmlevel=$.getWarnLevel();
			play();
			$.RecoverMEM();

		if(debug) $("#test ul").prepend("<li>第一次数据处理结束************"+(new Date().getTime()-t)+"</li>");

		}
	);
 }
 //播放声音
function play()
{
	if(issound)
		{

			var warninfos = alarmInfo[alarmlevel];
			if(warninfos==undefined)
			{
			return;
			}

			$.download("<s:url  value="/webtopo/sound.jsp"/>",
						"filename="+warninfos['warnvoice']+"&state=on&loop="+warninfos['voicetype']+"&state=on");
			alarmlevel=0;
			warninfos=null;
		}
}
//**********************************给告警详情提供接口方法***********************************//
//调用确认告警(告警序列号+采集点)
function ackAlarm(AlarmNum,gather_id,create_time){
	sel_id="";
	sel_devid="";
	var id=AlarmNum+"-"+gather_id;
	var tmp=$("#"+id).attr("devid");
	tmp=(typeof(tmp)=="undefined"?"":tmp);
	ClearWarnInterval();
	$.ajax({
		type:"post",
		url:"<s:url value="/webtopo/RealTimeWarn!ConfigWarn.action"/>",
		data:"alarmid="+id+"-1/gw/"+tmp+"-"+create_time+"-/-",
		success:function(data){
			if(data==1){
				$("#"+id).attr("class","level_0");
				$("#"+id).attr("lev","0");
				$("#"+id+" td[@name=lev]").html("已经处理");
				$("#"+id+" td[@name=astu]").html("手工确认");
				$("#"+id+" td[@name=atime]").html($.getTime());
				$.StatWarnNum();//统计告警条数
				update_flg=false;
				if($("input[@name='ref'][@checked]").val()==0){
					ReSetRefTime();
					ShowTimeRef();
				}
				$.RecoverMEM();
			}else{
				alert("确认未成功，请重试");
			}
			getRefWarn();
		},
		error:function(e){
			alert("session超时,请重新登陆!");
			getRefWarn();
		}
	});
}
//调用清除告警
function clearAlarm(AlarmNum,gather_id,create_time){
	sel_id="";
	sel_devid="";
	var id=AlarmNum+"-"+gather_id;
	var tmp=$("#"+id).attr("devid");
	tmp=(typeof(tmp)=="undefined"?"":tmp);
	ClearWarnInterval();
	$.ajax({
		type:"post",
		url:"./RealTimeWarn!DelWarn.action",
		data:"alarmid="+id+"-1/hgw/"+tmp+"-"+create_time+"-/-",
		success:function(data){
			if(data==1){
				$("#"+id).remove();
				update_flg=false;
				if($("input[@name='ref'][@checked]").val()==0){
					ReSetRefTime();
					ShowTimeRef();
				}
				$.StatWarnNum();//统计告警条数
				$.RecoverMEM();
			}else{
				alert("删除未成功，请重试");
			}
			getRefWarn();
		},
		error:function(msg){
			getRefWarn();
			alert("session超时,请重新登陆!");
		}
	});
}
//*******************************************************************************************//
</script>
<LINK REL="stylesheet" HREF="<s:url value="/webtopo/css/coolmenu.css"/>" TYPE="text/css">
<LINK REL="stylesheet" HREF="<s:url value="/webtopo/css/listview.css"/>" TYPE="text/css">
<LINK REL="stylesheet" HREF="<s:url value="/webtopo/css/webtopo.css"/>" TYPE="text/css">
<LINK REL="stylesheet" HREF="<s:url value="/css/css_blue.css"/>" TYPE="text/css">
<style type="text/css">
/*用于导出excel*/
td.column {
	background-color: #F4F4FF;
	color: #000000;
}

td.head {
	background-color: buttonface;
	border-left: solid #ffffff 1.5px;
	border-top: solid #ffffff 1.5px;
	border-right: solid #808080 1.8px;
	border-bottom: solid #808080 1.8px;
	color: #000000;
}
/*用于表格排序的图片*/
table.tablesorter thead tr .header {
	background-image: url("<s:url value="/images/bg.gif "/>");
	background-color: #bdd4ff;
	background-repeat: no-repeat;
	background-position: right;
	cursor: pointer;
}
table.tablesorter thead tr .headerSortUp {
	background-image: url("<s:url value="/images/desc.gif "/>");
}

table.tablesorter thead tr .headerSortDown {
	background-image: url("<s:url value="/images/asc.gif "/>");
}
/*告警等级*/
.level_0 {
	background-color:#FFFFFF;
	color:#000000;
	cursor:default;
		size:14px;
}

.level_1 {
	background-color:#E1ECFB;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_2 {
	background-color:#FFEBB5;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_3 {
	background-color:#FFC351;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_4 {
	background-color:#FFB4B2;
	color:#000000;
	cursor:default;
	size:14px;
}
.level_5{
	background-color:#FF0000;
	color:#000000;
	cursor:default;
	size:14px;
}
/*单击后改变的样式*/
.click{
	background-color:steelblue;
	color: white;
	font-size:14px;
}
.content{
	overflow: hidden;
	text-overflow: ellipsis;
	height: 14px;
	cursor: hand;
	width:400px;
}
.td_r{
	white-space: nowrap;
}
th{
	white-space: nowrap;
}
/*告警内容样式*/
.e_c{
	width:23%;
	white-space: normal;
}
.tr_b{
	font-weight:bold;
}
.tr_italic{
	font-style :italic;

}
#msg{
	position:absolute;
	z-index:100;
	top:65px;
	right:15px;
	background-color:white;

}
#test{
	position:absolute;
	z-index:50000;
	top:10px;
	right:1px;
	color:red;
	font-style:bold;
	background-color:blue;
	height:300px;
	overflow: auto;
}
#timeout{
	color: red;
	size:14px;
}
</style>
</head>
<body onselectstart="return false" ondragstart="return false" onclick="$.RightMenuHide()">
<div id="test" style="display:;"><ul></ul></div>
<div id="msg"></div>
<br>
<table width="98%" align="center" border=0 bgcolor="#999999" cellpadding="0" cellspacing="0" class="top_line">
	<tr bgcolor="#FFFFFF">
		<th colspan="4" align="center">实时告警显示</th>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="left" class="foot" width="30%">
			<input type="radio" name="ref" value="0" checked title="设置告警自动刷新" id="autoRefresh">
			<label for="autoRefresh">自动</label>
			<span id="autoref">
				<input type="radio" name="reftime" value="0.5" id="ref_05" checked><label for="ref_05">30秒</label>
				<input type="radio" name="reftime" value="1" id="ref_1"><label for="ref_1">1分钟</label>
				<input type="radio" name="reftime" value="3" id="ref_3"><label for="ref_3">3分钟</label>
				<input type="radio" name="reftime" value="5" id="ref_5"><label for="ref_5">5分钟</label>
			</span>
			<input type="radio" name="ref" value="1" title="设置告警手动刷新" id="handRefresh" style="margin-left: 5px;">
			<label for="handRefresh">手动</label>
			<span id="handref" style="display:none;">
				<input type="button" value="刷新" id="refbtn">
			</span><img style="cursor:hand;" id="sndTg" src="<s:url value="/images/sound_voice.gif"/>"/>
		</td>
		<td align="left" class="foot" width="30%">
			上次刷新时间是：<label id="last_reftime"></label>
		</td>
		<td align="left" class="foot" width="30%">
			<label id="next_reftime"></label>
		</td>
		<td align="right" class="foot" width="10%">
			<label id="timeout"></label>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td class="foot" colspan=4>规则模版:
			<select name="ruleid" value="<s:property value="ruleid"/>">
				<s:iterator var="modulelist" value="ModuleList">
					<option value="<s:property value="#modulelist.rule_id+'-/-'+#modulelist.maxnum"/>" sel="<s:property value="#modulelist.selected"/>">
						==<s:property value="#modulelist.rule_name"/>==
					</option>
				</s:iterator>
			</select>
		</td>
	</tr>
	<tr style="display:none;">
		<td colspan=2 class="foot" >模版加载方式:
			<input type="radio" name="usemodule" value="0" checked id="m_new"><label for="m_new">打开新页面加载模版</label>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="usemodule" value="1" id="m_old"><label for="m_old">本页面加载模版</label>
		</td>
	</tr>

</table>
<table width="98%" align="center" border=0 bgcolor="#999999" cellpadding="0" cellspacing="0" class="top_line">
	<tr bgcolor="#FFFFFF" style="margin-top: 15px;">
			<td><a href="#" onclick="$.RemoveWarn(-1)" title="移除所有告警">告警总数:<label name="total"></label></a></td>
			<td class="level_5"><a href="#" onclick="$.RemoveWarn(5)" title="移除严重告警">严重告警:<label name="lev5_num"></label></a></td>
			<td class="level_4"><a href="#" onclick="$.RemoveWarn(4)" title="移除主要告警">主要告警:<label name="lev4_num"></label></a></td>
			<td class="level_3"><a href="#" onclick="$.RemoveWarn(3)" title="移除次要告警">次要告警:<label name="lev3_num"></label></a></td>
			<td class="level_2"><a href="#" onclick="$.RemoveWarn(2)" title="移除警告告警">警告告警:<label name="lev2_num"></label></a></td>
			<td class="level_1"><a href="#" onclick="$.RemoveWarn(1)" title="移除事件告警">事件告警:<label name="lev1_num"></label></a></td>
			<td class="level_0"><a href="#" onclick="$.RemoveWarn(0)" title="移除清除告警">清除告警:<label name="lev0_num"></label></a></td>
	</tr>
</table>
	<br>
	<div id="table" style="text-align: center;" width="98%">
	</div>
	<br><br>
	<input type="hidden" name="rule" value="<s:property value="rule"/>">
	<input type="hidden" name="max" value="<s:property value="max"/>">

<form action="<s:url value="/webtopo/warn/outExcel.jsp"/>" id="excelFrm" method=post target="childFrm">
	<TEXTAREA id="rptdataExcel" NAME=rptdata ROWS=5 COLS=80 style='display:none'></TEXTAREA>
</form>
<iframe name="childFrm" style="display:none;"></iframe>
</body>
</html>
