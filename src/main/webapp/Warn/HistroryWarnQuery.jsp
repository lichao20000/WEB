<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>��ʷ�澯��ѯ</title>
    <%--
            /**
             * ��ʷ�澯��ѯҳ��
             *
             * @author ������(5260) email:benyp@lianchuang.com
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
            //��ʼ��ʱ��
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
            //��ѯ���ʹ����¼�
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
        //��ʼ��ʱ��
        function init(){
            $("input[@name='starttime']").val($.now("-",false)+" 00:00:00");
            $("input[@name='endtime']").val($.now("-",true));
        }
        //check form
        function CheckForm(){

			if($("select[@name='city_id']").val()==""){
				alert("��ѡ������!");
				$("select[@name='city_id']").focus();
				return false;
			}

        	if($("input[@name='searchType'][@checked]").val()==0){
        		document.frm.action="<s:url value="/Warn/NetWarnQuery!queryWarnInfoByDev.action" />";
        	}else{
        	 //check grade
               if($("input[@name='grade']").filter("[@checked]").val()==null ){
                   alert("������ѡ��һ���澯�ȼ�!");
                   $("input[@name='grade']").focus();
                   return false;
               }
        		document.frm.action="<s:url value="/Warn/NetWarnQuery!Query.action" />";
        	}


            //check time
            if($("input[@name='starttime']").val()==""){
                alert("�����뿪ʼʱ��!");
                $("input[@name='starttime']").focus();
                return false;
            }else if($("input[@name='endtime']").val()==""){
                alert("���������ʱ��!");
                $("input[@name='endtime']").focus();
                return false;
            }else if($.dtls($("input[@name='starttime']").val())>$.dtls($("input[@name='endtime']").val())){
                alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��!");
                $("input[@name='starttime']").select();
                $("input[@name='starttime']").focus();
                return false;
            }
            //check ip
            if($("input[@name='ip']").val()!="" && !$.checkIP($("input[@name='ip']").val())){
                alert("������Ϸ���IP��ַ");
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
						��ʷ�澯��ѯ
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<tr>
					<th colspan="4">��ѯ</th>
				</tr>
				<tr>
					<td class="column text" align="right">��ѯ����</td>
					<td colspan="3" class="text">
						<input type="radio" name="searchType" id="tj" value="0" checked><label for="tj">ͳ�Ʋ�ѯ</label>&nbsp;&nbsp;
						<input type="radio" name="searchType" id="xx" value="1"><label for="xx">��ϸ��ѯ</label>
					</td>
				</tr>
				<tr style="display:none;">
					<td class="column text" align="right" width="12%">�ɼ�����</td>
					<td colspan="3" class="text">
						<select name="gatherid">
							<option value="">==��ѡ��ɼ���==</option>
							<s:iterator var="gatherlist" value="GatherList">
								<option value="<s:property value="#gatherlist.gather_id"/>">==<s:property value="#gatherlist.descr"/>==</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr>
					<td class="column text" align="right" width="12%"><label style="color:  #FF0000">*</label>&nbsp;����</td>
					<td colspan="3" class="text">
						<select name="city_id">
							<option value="">==��ѡ������==</option>
							<s:iterator var="citylist" value="CityList">
								<option value="<s:property value="#citylist.city_id"/>">==<s:property value="#citylist.city_name"/>==</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr style="display:none;">
					<td class="column text" align="right" width="12%">�豸���</td>
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
					<td class="column text" align="right">�澯�ȼ�</td>
					<td colspan="3" class="text">
						<!-- <input type="checkbox" name="grade" value="5" id="g1" checked><label for="g1">�����澯</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="4" id="g2" checked><label for="g2">���ظ澯</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="3" id="g3" checked><label for="g3">һ��澯</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="2" id="g4"><label for="g4">��ʾ�澯</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="1" id="g5"><label for="g5">������־</label>&nbsp;&nbsp;
						<input type="checkbox" name="grade" value="0" id="g6"><label for="g6">�Զ����</label>&nbsp;&nbsp; -->
						<s:checkboxlist list="warnMap" listKey="key" listValue="value" name="grade" theme="simple" />
					</td>
				</tr>
				<tr id="warnstat" style="display:none">
					<td class="column text" align="right">�澯״̬</td>
					<td colspan="3" class="text">
						<input type="checkbox" name="actstatus" value="1" id="w_s_1"><label for="w_s_1">��ȷ��</label>
						<input type="checkbox" name="clearstatus" value="2" id="w_s_2"><label for="w_s_2">�����</label>
					</td>
				</tr>
				<tr id="warntype" style="display:none">
					<td class="column text" align="right">
					<input type="checkbox" id="chk_all"><label for="chk_all"></label>&nbsp;
					�澯����
					</td>
					<td colspan="3" class="text">
						<input type=checkbox name="creatortype" value=1>����&nbsp;
						<input type=checkbox name="creatortype" value=2>����&nbsp;
						<input type=checkbox name="creatortype" value=10>����&nbsp;
						<input type=checkbox name="creatortype" value=3>Syslog&nbsp;
						<input type=checkbox name="creatortype" value=4>Trap&nbsp;
						<input type=checkbox name="creatortype" value=5>��������&nbsp;
						<input type=checkbox name="creatortype" value=8>Ping���
					</td>
				</tr>
				<tr>
					<td class="column text" align="right"><label style="color:  #FF0000">*</label>&nbsp;��ʼʱ��</td>
					<td class="text">
						<input type="text" name="starttime" readonly>
						<img onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')" src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��">
					</td>
					<td class="column text" align="right"><label style="color:  #FF0000">*</label>&nbsp;����ʱ��</td>
					<td class="text">
						<input type="text" name="endtime" readonly>
						<img onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')" src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��">
					</td>
				</tr>
				<tr id="stype" style="display:none">
					<td class="column text" align="right">��ѯ����</td>
					<td colspan="3" class="text">
						<input type="radio" name="type" id="t0" value="0" checked><label for="t0">��ȷ��ѯ</label>&nbsp;&nbsp;
						<input type="radio" name="type" id="t1" value="1"><label for="t1">ģ����ѯ</label>
						<label id="showtype" style="color: red;display: none;">(ģ����ѯ��Ӱ���ѯ�ٶ�)</label>
					</td>
				</tr>
				<tr id="devip" style="display:none">
					<td class="column text" align="right">�豸IP��ַ</td>
					<td class="text">
						<input type="text" name="ip">
					</td>
					<td class="column text" align="right">�豸����</td>
					<td class="text">
						<input type="text" name="dev_name">
					</td>
				</tr>
				<tr id="showkey" style="display: none;">
					<td class="column text" align="right">�澯���ݹؼ���</td>
					<td colspan="3" class="text">
						<input type="text" name="keyword">
					</td>
				</tr>
				<tr>
					<td align="right" colspan="4" class="green_foot">
						<input type="button" value=" �� ѯ " onclick="CheckForm();">&nbsp;&nbsp;
						 <input type="reset" value=" ȡ �� " >
					</td>
				</tr>
			</table>
			</TD>
		</TR>
	</TABLE>
    </form>
  </body>
</html>
