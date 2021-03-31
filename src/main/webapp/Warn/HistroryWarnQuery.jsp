<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>历史告警查询</title>
    <%--
            /**
             * 历史告警查询页面
             *
             * @author 贲友朋(5260) email:benyp@lianchuang.com
             * @version 1.0
             * @since 2008-1-3
             * @category
             */
        --%>
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
    <script type="text/javascript">
        $(function(){
            //初始化时间
            init();
            $("input[@name='searchType']").click(function(){
            	if($(this).val()==0){
            		$("#warn_level").hide();
            		$("#warnstat").hide();
            		$("#warntype").hide();
            		$("#devip").hide();
            		$("#stype").hide();
            	}else{
            		$("#warn_level").show();
            		$("#warnstat").show();
            		$("#warntype").show();
            		$("#devip").show();
            		$("#stype").show();
            	}
            });
            //查询类型触发事件
            $("input[@name='type']").click(function(){
                if($(this).val()==0){
                    $("#showkey").hide();
                    $("#showtype").hide();
                }else{
                    $("#showkey").show();
                    $("#showtype").show();
                }
            });
            $("#chk_all").click(function(){
            	var chk=$(this).attr("checked");
            	chk=typeof(chk)=="undefined"?false:chk;
            	$("input[@name='creatortype']").attr("checked",chk);
            });
        });
        //初始化时间
        function init(){
            $("input[@name='starttime']").val($.now("-",false)+" 00:00:00");
            $("input[@name='endtime']").val($.now("-",true));
        }
        //check form
        function CheckForm(){

			if($("select[@name='city_id']").val()==""){
				alert("请选择属地!");
				$("select[@name='city_id']").focus();
				return false;
			}

        	if($("input[@name='searchType'][@checked]").val()==0){
        		document.frm.action="<s:url value="/Warn/NetWarnQuery!queryWarnInfoByDev.action" />";
        	}else{
        	 //check grade
               if($("input[@name='grade']").filter("[@checked]").val()==null ){
                   alert("请至少选择一个告警等级!");
                   $("input[@name='grade']").focus();
                   return false;
               }
        		document.frm.action="<s:url value="/Warn/NetWarnQuery!Query.action" />";
        	}


            //check time
            if($("input[@name='starttime']").val()==""){
                alert("请输入开始时间!");
                $("input[@name='starttime']").focus();
                return false;
            }else if($("input[@name='endtime']").val()==""){
                alert("请输入结束时间!");
                $("input[@name='endtime']").focus();
                return false;
            }else if($.dtls($("input[@name='starttime']").val())>$.dtls($("input[@name='endtime']").val())){
                alert("开始时间不能大于结束时间!");
                $("input[@name='starttime']").select();
                $("input[@name='starttime']").focus();
                return false;
            }
            //check ip
            if($("input[@name='ip']").val()!="" && !$.checkIP($("input[@name='ip']").val())){
                alert("请输入合法的IP地址");
                $("input[@name='ip']").select();
                $("input[@name='ip']").focus();
                return false;
            }

            document.frm.submit();
        }
    </script>

<link rel="stylesheet" href="../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<link rel="stylesheet" href="../css/css_ico.css" type="text/css">
<link rel="stylesheet" href="../css/user-defined.css" type="text/css">

<style type="text/css">
	tr{
		background-color:#ffffff;
	}
	label{
		cursor:hand;
	}

</style>
  </head>

  <body>
    <form action="" name="frm">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
		<tr bgcolor=#ffffff>
			<td align=right>&nbsp;
				<div id="operResult" style='width:20%;display:none;background-color:#33CC00'>&nbsp;</div>
			</td>
		</tr>
		<tr  style="display:none">
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						历史告警查询
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						带'<font color="#FF0000">*</font>'的表单必须填写或选择
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<tr>
					<th colspan="4">查询</th>
				</tr>
				<tr>
					<td class="column text" align="right">查询类型</td>
					<td colspan="3" class="text">
						<input type="radio" name="searchType" id="tj" value="0" checked><label for="tj">统计查询</label>&nbsp;&nbsp;
						<input type="radio" name="searchType" id="xx" value="1"><label for="xx">详细查询</label>
					</td>
				</tr>
				<tr style="display:none;">
					<td class="column text" align="right" width="12%">采集区域</td>
					<td colspan="3" class="text">
						<select name="gatherid">
							<option value="">==请选择采集点==</option>
							<s:iterator var="gatherlist" value="GatherList">
								<option value="<s:property value="#gatherlist.gather_id"/>">==<s:property value="#gatherlist.descr"/>==</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr>
					<td class="column text" align="right" width="12%"><label style="color:  #FF0000">*</label>&nbsp;属地</td>
					<td colspan="3" class="text">
						<select name="city_id">
							<option value="">==请选择属地==</option>
							<s:iterator var="citylist" value="CityList">
								<option value="<s:property value="#citylist.city_id"/>">==<s:property value="#citylist.city_name"/>==</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr style="display:none;">
					<td class="column text" align="right" width="12%">设备层次</td>
					<td colspan="3" class="text">
						<select name="restype_id">
							<option value="">==<s:text name="PLEASE_SELECT"></s:text><s:text name="DEV_LAY"></s:text>==</option>
							<s:iterator var="LayList" value="LayList">
								<option value="<s:property value="#LayList.resource_type_id"/>">==<s:property value="#LayList.resource_name"/>==</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr id="warn_level" style="display:none">
					<td class="column text" align="right">告警等级</td>
					<td colspan="3" class="text">
						<!-- <input type="checkbox" name="grade" value="5" id="g1" checked><label for="g1">紧急告警</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="4" id="g2" checked><label for="g2">严重告警</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="3" id="g3" checked><label for="g3">一般告警</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="2" id="g4"><label for="g4">提示告警</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="1" id="g5"><label for="g5">正常日志</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="0" id="g6"><label for="g6">自动清除</label>&nbsp;&nbsp; -->
						<s:checkboxlist list="warnMap" listKey="key" listValue="value" name="grade" theme="simple" />
					</td>
				</tr>
				<tr id="warnstat" style="display:none">
					<td class="column text" align="right">告警状态</td>
					<td colspan="3" class="text">
						<input type="checkbox" name="actstatus" value="1" id="w_s_1"><label for="w_s_1">已确认</label>
						<input type="checkbox" name="clearstatus" value="2" id="w_s_2"><label for="w_s_2">已清除</label>
					</td>
				</tr>
				<tr id="warntype" style="display:none">
					<td class="column text" align="right">
					<input type="checkbox" id="chk_all"><label for="chk_all"></label>&nbsp;
					告警类型
					</td>
					<td colspan="3" class="text">
						<input type=checkbox name="creatortype" value=1>主机&nbsp;
						<input type=checkbox name="creatortype" value=2>性能&nbsp;
						<input type=checkbox name="creatortype" value=10>流量&nbsp;
						<input type=checkbox name="creatortype" value=3>Syslog&nbsp;
						<input type=checkbox name="creatortype" value=4>Trap&nbsp;
						<input type=checkbox name="creatortype" value=5>规则引擎&nbsp;
						<input type=checkbox name="creatortype" value=8>Ping检测
					</td>
				</tr>
				<tr>
					<td class="column text" align="right"><label style="color:  #FF0000">*</label>&nbsp;开始时间</td>
					<td class="text">
						<input type="text" name="starttime" readonly>
						<img onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')" src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="选择">
					</td>
					<td class="column text" align="right"><label style="color:  #FF0000">*</label>&nbsp;结束时间</td>
					<td class="text">
						<input type="text" name="endtime" readonly>
						<img onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')" src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="选择">
					</td>
				</tr>
				<tr id="stype" style="display:none">
					<td class="column text" align="right">查询类型</td>
					<td colspan="3" class="text">
						<input type="radio" name="type" id="t0" value="0" checked><label for="t0">精确查询</label>&nbsp;&nbsp;
						<input type="radio" name="type" id="t1" value="1"><label for="t1">模糊查询</label>
						<label id="showtype" style="color: red;display: none;">(模糊查询会影响查询速度)</label>
					</td>
				</tr>
				<tr id="devip" style="display:none">
					<td class="column text" align="right">设备IP地址</td>
					<td class="text">
						<input type="text" name="ip">
					</td>
					<td class="column text" align="right">设备名称</td>
					<td class="text">
						<input type="text" name="dev_name">
					</td>
				</tr>
				<tr id="showkey" style="display: none;">
					<td class="column text" align="right">告警内容关键字</td>
					<td colspan="3" class="text">
						<input type="text" name="keyword">
					</td>
				</tr>
				<tr>
					<td align="right" colspan="4" class="green_foot">
						<input type="button" value=" 查 询 " onclick="CheckForm();">&nbsp;&nbsp;
						 <input type="reset" value=" 取 消 " >
					</td>
				</tr>
			</table>
			</TD>
		</TR>
	</TABLE>
    </form>
  </body>
</html>
