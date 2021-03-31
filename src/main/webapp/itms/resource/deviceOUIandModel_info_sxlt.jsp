<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>OUI查询修改</title>
    <%
        /**
         * OUI查询修改页面
         *
         * @author gaoyi
         * @version 1.0
         * @since 2013-05-15
         * @category
         */
        String str_VendorList = deviceAct.getVendorList(true, "",
                "vendorName");
    %>
    <link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
          type="text/css">
    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
          type="text/css">
    <style type="text/css">
     #device_oui_input,#stb_device_oui_input{
     	/* display: inline-block; */
     	opacity:0;
     	width: 100px;
     	margin-left: 10px;
     	line-height: 15px;
     	size: 15px;
     }
    </style>
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
    <script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
    <script type="text/javascript">
      function query () {
        document.selectForm.submit();
      }

      function showChild (param) {

        if (param == "vendorName") {
          page = "showDeviceModel.jsp?vendor_id=" + document.getElementsByName("vendorName")["0"].value + "&flag=1";
          document.all("childFrm").src = page;
        }
      }
      var deviceGwType = "";
      function addeditOUI (id, vendorName, ouiId, ouiDesc, remark, type, deviceModel,deviceType) {
        deviceGwType = deviceType;
        $("table[@id='addedit']").show();
        $("#device_oui_input").css("opacity","0");
        $("#stb_device_oui_input").css("opacity","0");
        if (type == "1") {
          $('.change-device-type').show();
          if (deviceType == "1") {
            $('.stb-device').hide();
            $('.device').show();
            $("select[@name='vendorName']").find("option[value='-1']").attr("selected", true);
            $("select[@name='stb_vendor']").find("option[value='-1']").attr("selected", true);
            $("select[@name='deviceModel']").find("option[value='-1']").attr("selected", true);
            $("select[name='device_oui']").find("option[value='-1']").attr("selected", true);
            $("select[name='stb_device_oui']").find("option[value='-1']").attr("selected", true);
            $("select[@name='stb_device_model']").find("option[value='-1']").attr("selected", true);
          } else if(deviceType == "4") {
            $('.stb-device').show();
            $('.device').hide();
          }
          document.all("actLabel").innerHTML = "添加";
          $("input[@name='type']").val("1");
          $("input[@name='id']").val("");
          // $("select[@name='vendorName']").text();
          $("input[@name='ouiId']").val("");
          $("input[@name='ouiDesc']").val("");
          $("input[@name='remark']").val("");
          // $("select[@name='deviceModel']").text();
        } else {
          $('.change-device-type').hide();
          if (deviceType == "1") {
            $('.stb-device').hide();
            $('.device').show();
            //vendorName = '--'+vendorName+'--';
            //deviceModel = '--'+deviceModel+'--';
            gwShare_change_select('vendor', vendorName);
            gwShare_change_select('deviceModel', deviceModel);
            gwShare_change_select('deviceOui', ouiId);
            //$("select[name='vendorName'] option:contains('"+vendorName+"')").attr('selected', true);
            //$("select[name='deviceModel'] option:contains('"+deviceModel+"')").attr('selected', true);
            
            /*$("select[name='vendorName']").find("option[value='-1']").attr("selected", true);
            $("select[name='device_model_id']").find("option[value='-1']").attr("selected", true);
            $("select[name='device_oui']").find("option[value='-1']").attr("selected", true);*/
          } else if(deviceType == "4") {
            $('.stb-device').show();
            $('.device').hide();
            //vendorName = '==' +vendorName + '==';
            //deviceModel = '=='+deviceModel+'==';
            // $("select[name='stb_vendor'] option:contains('"+vendorName+"')").attr('selected', true);
            // $("select[name='stb_device_model'] option:contains('"+deviceModel+"')").attr('selected', true);
            /*$("select[name='stb_vendor']").find("option[value='-1']").attr("selected", true);
            $("select[name='stb_device_model']").find("option[value='-1']").attr("selected", true);
            $("select[name='stb_device_oui']").find("option[value='-1']").attr("selected", true);*/
            gwShare_change_select_stb('stbVendor', vendorName);
            gwShare_change_select_stb('stbDeviceModel', deviceModel);
            gwShare_change_select_stb('stbDeviceOui', ouiId);
          }
          document.all("actLabel").innerHTML = "编辑";
          $("input[@name='type']").val(type);
          console.log("id="+id);
          $("input[@name='id']").val(id);
          $("select[@name='vendorName']").val();
          // $("input[@name='ouiId']").val(ouiId);
          $("input[@name='ouiDesc']").val(ouiDesc);
          $("input[@name='remark']").val(remark);
          $("select[@name='div_device_model_id']").val();

        }
      }

      function ExecMod () {
        var vendorName = "";
        var vendorNameValue = "";
        var deviceModel = "";
        var deviceModelValue = "";
        $("#save").attr('disabled', true);
        var type = $("input[@name='type']").val();
        
        var id = $("input[@name='id']").val();
        console.log("id="+id);
        var ouiId = $("select[@name='device_oui']").val();
        var url = "";
        var deviceType = "";
        if ("1" == type) {
          url = "<s:url value='/itms/resource/deviceOUIInfoACT!addSXLTOUI.action'/>";
          deviceType = $("select[@name='device-type']").find("option:selected").val();
        } else if ("2" == type) {
          url = "<s:url value='/itms/resource/deviceOUIInfoACT!editOUI.action'/>";
          deviceType = deviceGwType;
        }
        console.log("deviceType="+deviceType);
        console.log("ouiId="+ouiId);
        if (deviceType == "1") {
          vendorName = $("select[@name='vendorName']").find("option:selected").text().slice(2, -2);
          vendorNameValue = $("select[@name='vendorName']").find("option:selected").val();
          deviceModel = $("select[@name='device_model_id']").find("option:selected").text().slice(2, -2);
          deviceModelValue = $("select[@name='device_model_id']").find("option:selected").val();
          if(ouiId=='operate'){
        	  ouiId = $("select[@name='device_oui']").next().val();
          }
          //id = $("select[@name='device_oui']").find("option:selected").val();
        } else {
          vendorName = $("select[@name='stb_vendor']").find("option:selected").text().slice(2, -2);
          vendorNameValue = $("select[@name='stb_vendor']").find("option:selected").val();
          deviceModel = $("select[@name='stb_device_model']").find("option:selected").text().slice(2, -2);
          deviceModelValue = $("select[@name='stb_device_model']").find("option:selected").val();
          ouiId = $("select[@name='stb_device_oui']").val();
          if(ouiId=='operate'){
        	  ouiId = $("select[@name='stb_device_oui']").next().val();
          }
          //id = $("select[@name='stb_device_oui']").find("option:selected").val();
        }
        if(ouiId==null || $.trim(ouiId)=="" || ouiId.length!=6){
      	  alert("OUI不正确！");
      	  $("#save").attr('disabled', false);
      	  return false;
        }
        
        if (vendorNameValue == null || $.trim(vendorNameValue) == "" || vendorNameValue == -1) {
          alert("厂商不可为空！");
          $("#save").attr('disabled', false);
          return false;
        }
        
        if (deviceModelValue == null || $.trim(deviceModelValue) == "" || deviceModelValue == -1) {
           alert("型号不可为空！");
           $("#save").attr('disabled', false);
           return false;
         }
        
        if (id == null || $.trim(id) == "-1") {
          alert("OUI不可为空！");
          $("#save").attr('disabled', false);
          return false;
        }
        
        var ouiDesc = $("input[@name='ouiDesc']").val();
        if (ouiDesc == null || $.trim(ouiDesc) == "") {
          alert("OUI名称不可为空！");
          $("#save").attr('disabled', false);
          return false;
        }

        var remark = $("input[@name='remark']").val();

        // noinspection JSAnnotator
        $.post(url, {
          id: id,
          vendorName: vendorName,
          ouiId: ouiId,
          ouiDesc: ouiDesc,
          remark: remark,
          deviceModel: deviceModel,
          deviceType: deviceType,
          deviceModelId: deviceModelValue,
          vendorId: vendorNameValue
        }, function (ajax) {
          alert(ajax);
          var form = window.document.getElementById("form");
          form.action = "<s:url value="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfoSxlt.action"/>";
          form.submit();
          $("#save").attr('disabled', false);
          $("table[@id='addedit']").hide();
        });
      }


      function gwShare_change_select(type,selectvalue){
    	console.log("type="+type+", selectvalue="+selectvalue);
        switch (type){
          case "vendor":
            var url ="<s:url value="/inmp/share/gwDeviceQuery!getVendor.action"/>";
            $.ajaxSettings.async = false; //关闭异步
            $.post(url,{
            },function(ajax){
              gwShare_parseMessageContain(ajax,$("select[@name='vendorName']"),selectvalue,"vendor");
            });
            break;
            $.ajaxSettings.async = true//打开异步
          case "deviceModel":
            var url = "<s:url value="/inmp/share/gwDeviceQuery!getDeviceModel.action"/>";
            var vendorId = $("select[@name='vendorName']").val();
            if("-1"==vendorId){
              $("select[@name='device_model_id']").html("<option value='-1'>==请先选择设备厂商==</option>");
              break;
            }
            $.ajaxSettings.async = false; //关闭异步
            $.post(url,{
              gwShare_vendorId:vendorId
            },function(ajax){
              gwShare_parseMessage(ajax,$("select[@name='device_model_id']"),selectvalue);
              // $("select[@name='device_oui']").html("<option value='-1'>==请先选择设备型号==</option>");
            });
            $.ajaxSettings.async = true//打开异步
            
            break;
          case "deviceOui":
            //查询tab_vendor_oui填充oui
            var url = "<s:url value="/inmp/share/gwDeviceQuery!getDeviceOui.action"/>";
            var modelId = $("select[@name='vendorName']").val();
            if ("-1" == modelId) {
              $("select[@name='device_oui']").html("<option value='-1'>==请先选择设备厂商==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
              break;
            }
            $.ajaxSettings.async = false; //关闭异步
            $.post(url, {
              gwShare_deviceModelId: modelId,
            }, function (ajax) {
              //$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
              gwShare_parseMessageContain(ajax, $("select[@name='device_oui']"), selectvalue, "oui");
            });
            $.ajaxSettings.async = true//打开异步
            break;
          default:
            alert("未知查询选项！");
            break;
        }
      }
      
      function changoui(obj){
    	  console.log(obj.value);
    	  if(obj.value=="operate"){
    		  $(obj).next().css("opacity","1");
    		  $(obj).next().val("请输入OUI");
    	  }
    	  else{
    		  $(obj).next().css("opacity","0");
    	  }
      }

      function changeVendor(){
          gwShare_change_select('deviceModel','-1');
          gwShare_change_select('deviceOui', '-1');
      }

      function changeStbVendor(){
          gwShare_change_select_stb('stbDeviceModel','-1');
          gwShare_change_select_stb('stbDeviceOui', '-1');
      }


      function gwShare_change_select_stb (type, selectvalue) {
        switch (type) {
          case "stbVendor":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
            $.ajaxSettings.async = false; //关闭异步

            $.post(url, {}, function (ajax) {
            	gwShare_parseMessageContain(ajax, $("select[@name='stb_vendor']"), selectvalue, "vendor");
              //$("select[@name='vendor']").html("<option value='-1'>==请先选择设备厂商==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
            });
            $.ajaxSettings.async = true//打开异步
            break;
          case "stbDeviceModel":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
            var vendorId = $("select[@name='stb_vendor']").val();
            if ("-1" == vendorId) {
              $("select[@name='stb_device_model']").html("<option value='-1'>==请先选择设备厂商==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
              break;
            }
            $.ajaxSettings.async = false; //关闭异步
            $.post(url, {
              gwShare_vendorId: vendorId
            }, function (ajax) {
              //$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
              gwShare_parseMessage(ajax, $("select[@name='stb_device_model']"), selectvalue);
            });
            $.ajaxSettings.async = true//打开异步
            
            break;
          case "stbDeviceOui":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceOui.action"/>";
            var modelId = $("select[@name='stb_vendor']").val();
            if ("-1" == modelId) {
              $("select[@name='stb_device_oui']").html("<option value='-1'>==请先选择设厂商==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
              break;
            }
            $.ajaxSettings.async = false; //关闭异步
            $.post(url, {
              gwShare_deviceModelId: modelId,
            }, function (ajax) {
              //$("select[@name='device_model']").html("<option value='-1'>==请先选择设备型号==</option>");
              gwShare_parseMessageContain(ajax, $("select[@name='stb_device_oui']"), selectvalue,"oui");
            });
            $.ajaxSettings.async = true//打开异步
            break;
          default:
            alert("未知查询选项！");
            break;
        }
      }

      function gwShare_parseMessage (ajax, field, selectvalue) {
        var flag = true;
        if ("" == ajax) {
          return;
        }
        var lineData = ajax.split("#");
        if (!typeof(lineData) || !typeof(lineData.length)) {
          return false;
        }
        field.html("");

        option = "<option value='-1' selected>==请选择==</option>";
        field.append(option);
        
        for (var i = 0; i < lineData.length; i++) {
          var oneElement = lineData[i].split("$");
          var xValue = oneElement[0];
          var xText = oneElement[1];
          if (selectvalue == xValue || selectvalue == xText) {
            flag = false;
            //根据每组value和text标记的值创建一个option对象
            option = "<option value='" + xValue + "' selected>==" + xText + "==</option>";
          } else {
            //根据每组value和text标记的值创建一个option对象
            option = "<option value='" + xValue + "'>==" + xText + "==</option>";
          }
          try {
            field.append(option);
          } catch (e) {
            alert("设备型号检索失败！");
          }
        }
        if (flag) {
          field.attr("value", "-1");
        }
      }
      
      function gwShare_parseMessageContain (ajax, field, selectvalue, type) {
          var flag = true;
          field.html("");

          option = "<option value='-1' selected>==请选择==</option>";
          
          field.append(option);
          
          if ("" == ajax) {
        	  if(type=="oui"){
            	  option = "<option value='operate'>==手动输入==</option>";
            	  field.append(option);
              }
            return;
          }
          var lineData = ajax.split("#");
          if (!typeof(lineData) || !typeof(lineData.length)) {
        	  if(type=="oui"){
            	  option = "<option value='operate'>==手动输入==</option>";
            	  field.append(option);
              }
            return false;
          }
          
          for (var i = 0; i < lineData.length; i++) {
            var oneElement = lineData[i].split("$");
            var xValue = oneElement[0];
            var xText = oneElement[1];
            console.log("xText="+xText+", selectvalue="+selectvalue);
            if (selectvalue == xValue || (type=='vendor' && xText.indexOf(selectvalue)>-1)) {
              flag = false;
              //根据每组value和text标记的值创建一个option对象
              option = "<option value='" + xValue + "' selected>==" + xText + "==</option>";
            } else {
              //根据每组value和text标记的值创建一个option对象
              option = "<option value='" + xValue + "'>==" + xText + "==</option>";
            }
            try {
              field.append(option);
            } catch (e) {
              alert("设备型号检索失败！");
            }
          }
          if(type=="oui"){
        	  option = "<option value='operate'>==手动输入==</option>";
        	  field.append(option);
          }
          if (flag) {
            field.attr("value", "-1");
          }
        }

      //** iframe自动适应页面 **//
      //输入你希望根据页面高度自动调整高度的iframe的名称的列表
      //用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
      //定义iframe的ID
      var iframeids = ["dataForm"]

      //如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
      var iframehide = "yes"

      function dyniframesize () {
        var dyniframe = new Array()
        for (i = 0; i < iframeids.length; i++) {
          if (document.getElementById) {
            //自动调整iframe高度
            dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
            if (dyniframe[i] && !window.opera) {
              dyniframe[i].style.display = "block"
              //如果用户的浏览器是NetScape
              if (dyniframe[i].contentDocument
                && dyniframe[i].contentDocument.body.offsetHeight)
                dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
              //如果用户的浏览器是IE
              else if (dyniframe[i].Document
                && dyniframe[i].Document.body.scrollHeight)
                dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
            }
          }
          //根据设定的参数来处理不支持iframe的浏览器的显示问题
          if ((document.all || document.getElementById) && iframehide == "no") {
            var tempobj = document.all ? document.all[iframeids[i]]
              : document.getElementById(iframeids[i])
            tempobj.style.display = "block"
          }
        }
      }

      $(function () {
        dyniframesize();
        query();
        gwShare_change_select('vendor', '-1');
        gwShare_change_select_stb('stbVendor', '-1');
        changeType();
        changeTypeQry();
      });

      $(window).resize(function () {
        dyniframesize();
      });

      function changeType () {
        var deviceType = $("select[@name='device-type']").find("option:selected").val();
        if (deviceType == "1") {
          $('.stb-device').hide();
          $('.device').show();
        } else {
          $('.stb-device').show();
          $('.device').hide();
        }
      }
      
      function changeTypeQry() {
          var deviceType = $("select[@name='device_type_qry']").find("option:selected").val();
          if (deviceType == "1") {
            $('.stb-device-qry').hide();
            $('.device-qry').show();
          } else {
            $('.stb-device-qry').show();
            $('.device-qry').hide();
          }
        }
    </script>
</head>
<style>
table.querytable tr td input[type="text"],
table.querytable tr td select{
	width: 200px
}
</style>
<body>
<form id="form" name="selectForm" method="post"
      action="<s:url value='/itms/resource/deviceOUIInfoACT!getDeviceOuiInfoSxlt.action'/>"
      target="dataForm">
    <table>
        <tr>
            <td HEIGHT=20>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <table class="green_gargtd">
                    <tr>
                        <td width="162" align="center" class="title_bigwhite">
                            OUI信息管理
                        </td>
                        <td><img src="<s:url value="/images/attention_2.gif"/>"
                                 width="15" height="12"/> 设备OUI信息情况
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table class="querytable">

                    <TR>
                        <th colspan="6">设备OUI查询</th>
                    </TR>
                    
                    <tr>
                        <td class="column" width="15%" align="right">
                            	设备类型
                        </td>
                        <td colspan="3">
                            <select name="device_type_qry" class="bk" onchange="changeTypeQry()">
                                <option value="1" selected="">==光猫设备==</option>
                                <option value="4">==机顶盒设备==</option>
                            </select>
                        </td>
                        
                    </tr>
                    <TR>
                        <TD class="column" width='15%' align="right">OUI</TD>
                        <TD width="35%">
                            <input type="text" name="oui" id="oui" value="" maxlength=50 class=bk>
                        </TD>
                        <%-- <TD class="column device" width="15%" align='right'>光猫厂商</TD>
                        <TD width="35%">
                            <s:select list="vendorMap" name="vendor_name" id="vendor_name"
                                      headerKey="0" headerValue="请选择厂商" listKey="vendor_name"
                                      listValue="vendor_name" cssClass="bk">
                            </s:select>
                        </TD> --%>
                        
                        
                        <TD class="column device-qry" width="15%" align='right'>光猫厂商</TD>
                        <TD width="35%" class="device-qry">
                          	<s:select list="vendorMap" name="vendor_name" id="vendor_name"
                                      headerKey="0" headerValue="请选择厂商" listKey="vendor_name"
                                      listValue="vendor_name" cssClass="bk">
                            </s:select>
                        </TD>
                        
                        <TD class="column stb-device-qry" width="15%" align='right'>机顶盒厂商</TD>
                        <TD width="35%" class="stb-device-qry">
                          	<s:select list="vendorMapStb" name="vendor_name_stb" id="vendor_name_stb"
                                      headerKey="0" headerValue="请选择厂商" listKey="vendor_name"
                                      listValue="vendor_name" cssClass="bk">
                            </s:select>
                        </TD>
                    </TR>

                    <TR>
                        <td colspan="6" align="right" class=foot>

                            <s:if test='%{userName=="admin" || userName=="szxadmin"}'>
                                <button onclick="addeditOUI('','','','','','1','')">&nbsp;新&nbsp;&nbsp;增&nbsp;</button>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                            </s:if>
                            <button onclick="query()">&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
                        </td>
                    </TR>
                </table>
            </td>
        </tr>
        <tr>
            <td height="25" id="resultStr"></td>
        </tr>
        <tr>
            <td>
                <iframe id="dataForm" name="dataForm" height="0"
                        frameborder="0" scrolling="no" width="100%" src=""></iframe>
            </td>
        </tr>
        <tr>
            <td height="25"></td>
        </tr>

        <tr>
            <td>
                <table class="querytable" width="98%" align="center"
                       style="display: none" id="addedit">
                    <tr>
                        <th colspan="6"><SPAN id="actLabel">添加</SPAN>OUI信息
                            <input type="hidden" name="type" id="type" value="1"/>
                            <input type="hidden" name="id" id="id" value=""/>
                        </th>
                    </tr>
                    <tr class = "change-device-type">
                        <td class="column" width="15%" align="right">
                            	设备类型
                        </td>
                        <td>
                            <select name="device-type" class="bk" onchange="changeType()">
                                <option value="1" selected="">==光猫设备==</option>
                                <option value="4">==机顶盒设备==</option>
                            </select>
                        </td>
                        
                       	<TD class="column device" width="15%" align="right">光猫厂商</TD>
                        <TD align="left" class="device">
                            <select name="vendorName" class="bk" onchange="changeVendor()">
                                <option value="-1">==请选择==</option>
                            </select>
                            <font color="red">*</font>
                        </TD>
                        
                       	<TD class="column stb-device" width="15%" align="right" >机顶盒厂商</TD>
                        <TD align="left" class="stb-device">
                            <select name="stb_vendor" class="bk" onchange="changeStbVendor()">
                        	</select>
                            <font color="red">*</font>
                        </TD>
                    </tr>
                    <TR class="device">
                        <TD  class="column" align="right" width="15%">光猫型号</TD>
                        <TD  align="left" width="35%">
                            <select name="device_model_id" class="bk" >
                                <option value="-1">请先选择厂商</option>
                            </select>
                            <font color="red">*</font>
                        </TD>
                        <TD class="column" align="right" width="15%">OUI</TD>
                        <TD  align="left" width="35%">
                            <select name="device_oui" id="device_oui" class="bk" onchange="changoui(this)">
                                <option value="-1">==请选择OUI==</option>
                            </select>
                            <input type="text" name="device_oui_input" id="device_oui_input" class="bk" value="请输入OUI"  maxlength="10" >
                            <!-- <font color="red">*</font> -->
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF" class="stb-device" >
                        <TD class="column" align="right" width="15%">机顶盒型号</TD>
                        <TD  align="left" width="35%">
                        	<select name="stb_device_model" class="bk">
                            	<option value="-1">==请选择设备型号==</option>
                        	</select>
                            <font color="red">*</font>
                        </TD>
                        <TD class="column" align="right" width="15%">OUI</TD>
                        <TD  align="left" width="35%">
                            <select name="stb_device_oui" id="stb_device_oui" class="bk" onchange="changoui(this)">
                                <option value="-1">==请选择OUI==</option>
                            </select>
                            <input type="text" name="stb_device_oui_input" id="stb_device_oui_input" class="bk" value="请输入OUI"  >
                        </TD>
                    </TR>
                    <TR>
                        <TD class="column" align="right" >名称</TD>
                        <TD  align="left" >
                        	<input type="text" name="ouiDesc" id="ouiDesc" class="bk" value=""  maxlength="20">
                            <font color="red">*</font>
                        </TD>
                        <TD class="column" align="right">备注</TD>
                        <TD align="left" colspan="3">
                        	<input type="text" name="remark" id="remark" class="bk" value=""  maxlength="20"/>
                        </TD>
                    </TR>
                    <tr>
                        <td colspan="6" class="foot" align="right">
                            <div class="right">
                                <button onclick="ExecMod()" id="save">保存</button>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td id="bssSheetInfo"></td>
        </tr>
        <TR>
            <TD HEIGHT=20>&nbsp;
                <IFRAME ID="childFrm" SRC="" STYLE="display:none">
                </IFRAME>
            </TD>
            <TD HEIGHT=20>&nbsp;
                <IFRAME ID="childFrm1" SRC="" STYLE="display:none">
                </IFRAME>
            </TD>
        </TR>
    </table>
    <br>
</form>
</body>
</html>
<%@ include file="../../foot.jsp" %>
</body>
</html>