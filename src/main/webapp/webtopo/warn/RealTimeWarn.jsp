<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʵʱ�澯��</title>
<%--
    /**
         * ҵ��ƽ̨-ʵʱ�澯��
         *
         * @author ������(5260) email:benyp@lianchuang.com
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
	var issound=true;//�Ƿ���
	var alarmlevel=0;//�Ƿ��и澯����,0Ϊ���澯������Ϊ�澯
	var update_flg=true;//�Ƿ���¹�����(������)
	var alarmInfo = eval("("+"<s:property value="warnInfo"/>"+")");
	var debug=eval("<s:property value="debug"/>");//�Ƿ���ʾdebug

  $(function(){
	  if(debug) $("#test").show();
    //�õ���ʼ������
	getFirstData();
    //�Զ�ˢ�����ֶ�ˢ��
    $("input[@name='ref']").click(function(){
		ChangeAutoOrHandRef($(this).val(),$("input[@name='reftime'][@checked]").val()*60);
    });
    //���ˢ��
    $("#refbtn").click(function(){
		$.RefData();
    });
	//�Զ�ˢ���趨ʱ��
	$("input[@name='reftime']").click(function(){
		SetRefTime($(this).val()*60);
		ShowTimeRef();
	});
	//�澯�����Ƿ���
	$("#sndTg").toggle(function(){
		$(this).attr("src","<s:url value="/images/sound_voice2.gif"/>");
		issound=false;
	},function(){
		$(this).attr("src","<s:url value="/images/sound_voice.gif"/>");
		issound=true;
	});
	//�л�����
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
  //ȷ���˳�
  function ConfigRelad(){
	this.focus();
	if(window.confirm("ȡ�����ݳ�ʱ�������࣬��Ҫ���µ�½���Ƿ����µ�½?")){
		window.open("<s:url value="/login.jsp"/>");
	 }
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=off&loop=0");
	 timeout=0;
	 $("#timeout").html("");
  }
	//��ʱ�˳�
  function ReloadWEB(){
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=on&loop=1");
	 window.setTimeout("ConfigRelad()",1000);
  }
  //��ʱ����
  function OutTimeVoice(){
	 $.download("<s:url  value="/work/sound.jsp"/>","filename=TOReload.wav&state=on&loop=0");
  }

//��ȡ��һ������
 function getFirstData(){
	var t=new Date().getTime();
	if(debug) $("#test ul").prepend("<li>��һ��ȡ����************</li>");
	$.loading(0);
	$.InitModule();
	$.post(
		"<s:url value="/webtopo/RealTimeWarn!getALLWarnData.action"/>",
	    {
			ruleid:$("input[@name='rule']").val(),
			fetchCount:$("input[@name='max']").val()
		},
		function(data){
		if(debug) 	$("#test ul").prepend("<li>���ݻ�ȡ��ϣ�׼������************"+(new Date().getTime()-t)+"</li>");
			$.loading(2);
			$("#table").html(data);
		if(debug) 	$("#test ul").prepend("<li>��������ҳ��************"+(new Date().getTime()-t)+"</li>");
			$("#data").tablesorter();
		if(debug) 	$("#test ul").prepend("<li>��������׼������************"+(new Date().getTime()-t)+"</li>");
			$.StatWarnNum();
		if(debug) 	$("#test ul").prepend("<li>ͳ�Ƹ澯��Ŀ************"+(new Date().getTime()-t)+"</li>");
		if(debug) 	$("#test ul").prepend("<li>�澯����************"+(new Date().getTime()-t)+"</li>");
			$.showTime();
			SetRefTime($("input[@name='reftime']").val()*60);
			ShowTimeRef();
			//$.DisplayStr();
			$.MouseEvent($("#tbody tr"));
			$.loading(3);
			getRefWarn();
			//���TH����֮ǰ�����������ڴ�
			  $("#thead tr th").click(function(){
				 if(!update_flg){
					$("#data").trigger("update");
					update_flg=true;
				 }
			  });
			alarmlevel=$.getWarnLevel();
			play();
			$.RecoverMEM();

		if(debug) $("#test ul").prepend("<li>��һ�����ݴ������************"+(new Date().getTime()-t)+"</li>");

		}
	);
 }
 //��������
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
//**********************************���澯�����ṩ�ӿڷ���***********************************//
//����ȷ�ϸ澯(�澯���к�+�ɼ���)
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
				$("#"+id+" td[@name=lev]").html("�Ѿ�����");
				$("#"+id+" td[@name=astu]").html("�ֹ�ȷ��");
				$("#"+id+" td[@name=atime]").html($.getTime());
				$.StatWarnNum();//ͳ�Ƹ澯����
				update_flg=false;
				if($("input[@name='ref'][@checked]").val()==0){
					ReSetRefTime();
					ShowTimeRef();
				}
				$.RecoverMEM();
			}else{
				alert("ȷ��δ�ɹ���������");
			}
			getRefWarn();
		},
		error:function(e){
			alert("session��ʱ,�����µ�½!");
			getRefWarn();
		}
	});
}
//��������澯
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
				$.StatWarnNum();//ͳ�Ƹ澯����
				$.RecoverMEM();
			}else{
				alert("ɾ��δ�ɹ���������");
			}
			getRefWarn();
		},
		error:function(msg){
			getRefWarn();
			alert("session��ʱ,�����µ�½!");
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
/*���ڵ���excel*/
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
/*���ڱ�������ͼƬ*/
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
/*�澯�ȼ�*/
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
/*������ı����ʽ*/
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
/*�澯������ʽ*/
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
		<th colspan="4" align="center">ʵʱ�澯��ʾ</th>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="left" class="foot" width="30%">
			<input type="radio" name="ref" value="0" checked title="���ø澯�Զ�ˢ��" id="autoRefresh">
			<label for="autoRefresh">�Զ�</label>
			<span id="autoref">
				<input type="radio" name="reftime" value="0.5" id="ref_05" checked><label for="ref_05">30��</label>
				<input type="radio" name="reftime" value="1" id="ref_1"><label for="ref_1">1����</label>
				<input type="radio" name="reftime" value="3" id="ref_3"><label for="ref_3">3����</label>
				<input type="radio" name="reftime" value="5" id="ref_5"><label for="ref_5">5����</label>
			</span>
			<input type="radio" name="ref" value="1" title="���ø澯�ֶ�ˢ��" id="handRefresh" style="margin-left: 5px;">
			<label for="handRefresh">�ֶ�</label>
			<span id="handref" style="display:none;">
				<input type="button" value="ˢ��" id="refbtn">
			</span><img style="cursor:hand;" id="sndTg" src="<s:url value="/images/sound_voice.gif"/>"/>
		</td>
		<td align="left" class="foot" width="30%">
			�ϴ�ˢ��ʱ���ǣ�<label id="last_reftime"></label>
		</td>
		<td align="left" class="foot" width="30%">
			<label id="next_reftime"></label>
		</td>
		<td align="right" class="foot" width="10%">
			<label id="timeout"></label>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td class="foot" colspan=4>����ģ��:
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
		<td colspan=2 class="foot" >ģ����ط�ʽ:
			<input type="radio" name="usemodule" value="0" checked id="m_new"><label for="m_new">����ҳ�����ģ��</label>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="usemodule" value="1" id="m_old"><label for="m_old">��ҳ�����ģ��</label>
		</td>
	</tr>

</table>
<table width="98%" align="center" border=0 bgcolor="#999999" cellpadding="0" cellspacing="0" class="top_line">
	<tr bgcolor="#FFFFFF" style="margin-top: 15px;">
			<td><a href="#" onclick="$.RemoveWarn(-1)" title="�Ƴ����и澯">�澯����:<label name="total"></label></a></td>
			<td class="level_5"><a href="#" onclick="$.RemoveWarn(5)" title="�Ƴ����ظ澯">���ظ澯:<label name="lev5_num"></label></a></td>
			<td class="level_4"><a href="#" onclick="$.RemoveWarn(4)" title="�Ƴ���Ҫ�澯">��Ҫ�澯:<label name="lev4_num"></label></a></td>
			<td class="level_3"><a href="#" onclick="$.RemoveWarn(3)" title="�Ƴ���Ҫ�澯">��Ҫ�澯:<label name="lev3_num"></label></a></td>
			<td class="level_2"><a href="#" onclick="$.RemoveWarn(2)" title="�Ƴ�����澯">����澯:<label name="lev2_num"></label></a></td>
			<td class="level_1"><a href="#" onclick="$.RemoveWarn(1)" title="�Ƴ��¼��澯">�¼��澯:<label name="lev1_num"></label></a></td>
			<td class="level_0"><a href="#" onclick="$.RemoveWarn(0)" title="�Ƴ�����澯">����澯:<label name="lev0_num"></label></a></td>
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
