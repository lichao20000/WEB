<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../timelater.jsp" %>
<html>
<style type="text/css">
	.querytable{
		border-top: solid 1px #999;
		border-right: solid 1px #999;
	}
	.querytable th, .querytable td{
	 	border-bottom: solid 1px #999;
	 	border-left: solid 1px #999;
	}
</style>
<head>
    <title>设备型号管理页面</title>
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <script language="javascript">

      function init () {
        frm.actionType.value = "";
      }

      // 保存
      function save(){
      	// 校验
      	if(!checkForm()){
      		return;
      	}
      	$.ajax({
      		type:"Post",
      		url:"deviceModelInfo!stbExecute.action",
      		data: $("#frm").serialize(),
      		dataType:'json',
      		success:function(data){
      			if(data.code == 1){
      				$("#add-div").hide();
                    frm.actionType.value="";
                    frm.submit();
      			}else{
      				alert(data.message);
      			}
      		},
      		error:function(e){
      			alert("服务器异常");
      			console.info("e",e);
      		}
      	});
      }


      function showChild (obj, event) {
        frm.oui.value = event.target.value;
      }

      function checkForm () {
        if(frm.actionType.value==""){
            return true;
        }
        frm.deviceModelName = $("[name='deviceModelName']");
        var oui = frm.oui;
        var deviceModel = frm.deviceModelName;
        if (oui.value == "") {
          alert("请选择厂商");
          oui.focus();
          return false;
        }
        if (deviceModel.value == "") {
          alert("请输入型号名称");
          deviceModel.focus();
          return false;
        }

        return true;
      }

      function doEdit (s) {
        document.getElementById('add-div').style.display = "";
        var str = s.split("|")
        var element = document.getElementsByName('<s:property value="vendorAlias"/>');
        var size = element["0"].length;
        // element.options[element.selectedIndex].selected = false;
        var i = 1;
        while (size > i) {
          if (str[2] == element["0"][i].value) {
            element["0"][i].selected = true;
            frm.oui.value = element["0"][i].value;
            break;
          }
          i++;
        }
        frm.deviceModelName.value = str[3];
        frm.deviceModelId.value = str[0];
        /*frm.ouiId.value = str[4];
        frm.id.value = str[5];*/
        frm.actionType.value = "edit";
        editTypeLabel.innerHTML = "编辑[型号ID：" + frm.deviceModelId.value + "]";
      }

      function doDel (s) {
        if (window.confirm("确实要删除该条纪录吗？ 型号ID：" + s)) {
          frm.deviceModelId.value = s;
          frm.actionType.value = "del";
          $.ajax({
      		type:"Post",
      		url:"deviceModelInfo!stbExecute.action",
      		data: $("#frm").serialize(),
      		dataType:'json',
      		success:function(data){
      			if(data.code == 1){
                    frm.actionType.value="";
                    frm.submit();
      			}else{
      				alert(data.message);
      			}
      		},
      		error:function(e){
      			alert("服务器异常");
      			console.info("e",e);
      		}
         });
        }
      }

      function add () {
        document.getElementById('add-div').style.display = "";
        var element = document.getElementsByName('<s:property value="vendorAlias"/>');
        // element.options[element.selectedIndex].selected = false;
        frm.oui.value = "";
        frm.deviceModelName.value = "";
        frm.actionType.value = "add";
        editTypeLabel.innerHTML = "添加";
      }

      //翻页方法调用
      function doQuerySubmit () {
        frm.actionType.value = "";
        frm.submit();
      }

    </script>

</head>
<%@ include file="../head.jsp" %>
<%@ include file="../toolbar.jsp" %>
<body onload="init()">
    <table border=0 cellspacing=0 cellpadding=0 width="100%">
        <TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<tr>
			<td>
			<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备型号
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						带'<font color="#FF0000">*</font>'的表单必须填写或选择
					</td>
				</tr>
			</table>
			</td>
		</tr>
    </table>
	<%--<form name = "queryFrm" action="deviceModelInfo!stbExecute.action">--%>
    <form name="frm" id="frm" action="deviceModelInfo!stbExecute.action" method="post" onsubmit="return checkForm();">
		<table class="querytable"  width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<th colspan="4">型号查询</th>
			</TR>
			<TR>
				<TD class="column" width='15%' align="right">厂商名称</TD>
				<TD width="225px">
					<input type="text" name="vendor_add"  value="<s:property value='vendor_add'/>" maxlength=50 class=bk>
				</TD>
			 	<TD class=column width="15%" align='right'>型号名称</TD>
				<TD width="225px">
					<input type="text" name="deviceModelQry"  value="<s:property value='deviceModelQry'/>" maxlength=50 class=bk>
				</TD>
			</TR>
			<TR>
				<td colspan="4" align="right" class=green_foot>
					<button type="button" onclick="add()" >&nbsp;新&nbsp;&nbsp;增&nbsp;</button>&nbsp;&nbsp;
					<button type="submit" >&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
				</td>
			</TR>
		</table>
	<%--</form>--%>

    <TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="idTable">
        <TR>
            <TD bgcolor=#999999>
                <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                    <TR>
                        <TH colspan="5" align="center">设备型号</TH>
                    </TR>
                    <tr CLASS=green_title2>
                        <td width="15%">型号ID</td>
                        <td width="20%">厂商名称</td>
                        <td width="20%">型号名称</td>
                        <td width="25%">操作</td>
                    </tr>
                    <s:iterator value="stbDeviceModelList">
                        <TR bgcolor="#FFFFFF">
                            <td class=column1 nowrap><s:property value="modelid"/></td>
                            <td class=column1 nowrap><s:property value="vendorname"/></td>
                            <td class=column1 nowrap><s:property value="modelname"/></td>
                            <td align="center" class=column1 nowrap>
                                <a href="#" onclick="doEdit('<s:property value="modelid"/>|<s:property value="vendorname"/>|<s:property value="ouiname"/>|<s:property value="modelname"/>')">编辑</a>&nbsp;&nbsp;
                                <a href="#" onclick="doDel('<s:property value="modelid"/>',<s:property value="id"/>)">删除</a>&nbsp;&nbsp;
                            </td>
                        </TR>
                    </s:iterator>
                    <tr>
                        <td align="right" CLASS=green_foot colspan="5">
                            <%@ include file="/PageFoot.jsp" %>
                        </td>
                    </tr>
                </TABLE>
            </TD>
        </TR>

    </TABLE>
    <br>
    <TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="add-div" STYLE="display:none">
        <TR>
            <TD bgcolor=#999999>
                <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                    <TR>
                        <TH colspan="4" align="center"><SPAN id="editTypeLabel">添加</SPAN>设备型号</TH>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">设备供应商</TD>
                        <TD colspan=3><s:property value="strVendorList" escapeHtml="false"/>&nbsp;
                            <font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">设备型号</TD>
                        <TD colspan=3><INPUT TYPE="text" NAME="deviceModelName" maxlength=32 class='bk' value="">&nbsp;
                            <font color="#FF0000">*</font></TD>
                    </TR>
                    <TR>
                        <TD colspan="4" align="right" CLASS=green_foot>
                            <INPUT TYPE="hidden" name="actionType" value=""/>
                            <input type="hidden" name="deviceModelId" value=""/>
                            <input type="hidden" name="id" value=""/>
                            <input type="hidden" name="vendorAlias" value='<s:property value="vendorAlias"/>'/>
                            <input type="hidden" name="oui"/>
                            <INPUT TYPE="button" onclick="save()" value=" 保 存 " class=btn/>&nbsp;&nbsp;
                            <INPUT TYPE="reset" value=" 重 置 " class=btn/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
</form>
<%-- <%@ include file="../foot.jsp" %> --%>
</body>
</html>
