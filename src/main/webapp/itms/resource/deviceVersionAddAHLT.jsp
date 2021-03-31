<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>

<html xmlns:s="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>设备版本新增</title>
    <script type="text/javascript" src="../../Js/jquery.js"></script>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/Calendar.js"></SCRIPT>
    <link rel="stylesheet" href="../../css/liulu.css" type="text/css">
    <link rel="stylesheet" href="../../css/css_green.css" type="text/css">
    <link rel="stylesheet" href="../../css/tab.css" type="text/css">
    <link rel="stylesheet" href="../../css/listview.css" type="text/css">
    <link rel="stylesheet" href="../../css/css_ico.css" type="text/css">
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/toolbars.js"></SCRIPT>
    <link rel="stylesheet" href="../../css3/global.css" type="text/css">
    <script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="../../Js/json2.js"></script>
</head>

<body>
<form id="addForm" name="addForm" target="" method="post" action="">
    <input type='hidden' id="deviceTypeId" value="<s:property value="deviceTypeId" />" />
    <input type='hidden' id="devTypeInfoMap" value="<s:property value="devTypeInfoMap" />" />
    <TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="addTable">
        <TR>
            <TD>
                <TABLE  class="mytable" width="100%" id="allDatas">
                    <TR>
                        <TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
                                id="DeviceTypeLabel"></SPAN>设备类型</TH>
                    </TR>
                    <TR bgcolor="#FFFFFF" id="vendor_idID">
                        <TD class=column align="right">设备厂商</TD>
                        <TD colspan=3><select name="vendor_add" id="vendor_add" class="bk"
                                              onchange="change_model('-1')">
                        </select>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF" id="device_ModelID">
                        <TD class=column align="right">设备型号</TD>
                        <TD colspan=3><select name="device_model_add" id="device_model_add" class="bk">
                            <option value="-1">==请选择厂商==</option>
                        </select>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">特定版本</TD>
                        <TD colspan=3><INPUT TYPE="text" NAME="speversion" id="speversion"
                                             maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">硬件版本</TD>
                        <TD colspan=3><INPUT TYPE="text" NAME="hard_version_add" id="hard_version_add"
                                             maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">软件版本</TD>
                        <TD colspan=3><INPUT TYPE="text" NAME="soft_version_add" id="soft_version_add"
                                             maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">是否审核</TD>
                        <TD colspan=3><select name="is_check_add" class="bk">
                            <option value="-2" selected>==请选择==</option>
                            <option value="1">经过审核</option>
                            <option value="-1">未审核</option>
                        </select>&nbsp;<font color="#FF0000">*</font></TD>
                    </TR>
                    <tr style="background-color: white">
                        <TD class=column align="right">设备类型</TD>
                        <TD colspan=3>
                            <s:select list="devTypeMap" name="rela_dev_type_add" id="rela_dev_type_add" listKey="type_id"
                                      listValue="type_name" cssClass="bk"></s:select>&ensp;<font color="#FF0000">*</font>
                        </TD>
                    </tr>

                    <tr style="background-color: white">
                        <TD class=column align="right">设备版本类型</TD>
                        <TD colspan=3>
                            <s:select list="devVersionTypeMap" name="device_version_type" id="device_version_type"
                                      headerKey="-1" headerValue="请选择设备版本类型" listKey="value"
                                      listValue="text" cssClass="bk"></s:select>&nbsp;<font color="#FF0000">*</font>
                        </TD>
                    </tr>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">上行方式</TD>
                        <TD colspan=3>
                            <span id="typeNameList"></span>&nbsp;<font color="#FF0000">*</font>
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">设备支持的协议</TD>
                        <TD colspan=3><input type="checkbox" width="30%" id="2"
                                             name="protocol2" value="2">H248 <input type="checkbox"
                                                                                    width="30%" id="1" name="protocol1" value="1">软交换SIP <input
                                type="checkbox" width="30%" id="0" name="protocol0" value="0">IMS
                            SIP</TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">设备IP支持方式</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="ipType" value="0">IPV4
                            <input type="radio" width="23%" name="ipType" value="1">IPV4和IPV6
                            <input type="radio" width="22%" name="ipType" value="2">DS-Lite
                            <input type="radio" width="23%" name="ipType" value="3">LAFT6
                            <input type="radio" width="23%" name="ipType" value="4">纯IPV6
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">是否为最新版本</TD>
                        <TD colspan=3>
                            <input type="radio" width="45%" name="isNormal" value="1">是
                            <input type="radio" width="45%" name="isNormal" value="0" checked="checked">否
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column align="right">终端规格</TD>
                        <td width="35%">
                            <s:select list="specList" name="specId" headerKey="-1" id="specId"
                                      headerValue="请选择终端规格" listKey="id" listValue="spec_name"
                                      value="specId" cssClass="bk"></s:select>
                        </td>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">是否支持百兆宽带</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="mbBroadband_add" value="1">是
                            <input type="radio" width="23%" name="mbBroadband_add" value="2">否
                        </TD>
                    </TR>
                        <TR bgcolor="#FFFFFF">
                            <TD class=column width="10%" align="right" id="3">定版时间</TD>
                            <TD width="35%"><input type="text" name="startOpenDate_add" id="startOpenDate_add"
                                                   readonly class=bk> <img
                                    name="shortDateimg"
                                    onClick="WdatePicker({el:document.addForm.startOpenDate_add,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="../../images/dateButton.png" width="15" height="12"
                                    border="0" alt="选择"></TD>
                        </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">是否支持QOE功能</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="is_qoe_add" value="1">是
                            <input type="radio" width="23%" name="is_qoe_add" value="2">否
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF">
                        <TD class=column width="10%" align="right" id="3">是否支持机顶盒零配置</TD>
                        <TD colspan=3>
                            <input type="radio" width="22%" name="machineConfig_add" value="1">是
                            <input type="radio" width="23%" name="machineConfig_add" value="2">否
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TD>
                <TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
                    <TR bgcolor="#FFFFFF">

                        <TD align="right" CLASS=green_foot><INPUT TYPE="button"
                                                                  onclick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
                            <INPUT TYPE="hidden" name="action" value="add">&nbsp;
                            <INPUT
                                    TYPE="button" value=" 关 闭 " class=jianbian
                                    onclick="javascript:window.close()"></TD>
                    </TR>

                </TABLE>
            </TD>
        </TR>

    </TABLE>
</form>

</body>

<script>
    // 初始化厂家
    getVendorList();
    if($("#deviceTypeId").val() == "-1"){
        queryTypeName("");
        //新增
        $("#devTypeInfoMap").val("");
        $("input[@name='protocol2']").attr("checked",false);
        $("input[@name='protocol1']").attr("checked",false);
        $("input[@name='protocol0']").attr("checked",false);
        clearData();
        disableLabel(false);
        document.getElementsByName("is_check_add")[0].value=-2;
        document.getElementsByName("rela_dev_type_add")[0].value=-1;
        document.getElementsByName("device_version_type")[0].value="0";

    }else {
        //修改 填充值
        var devTypeInfoMap = JSON.parse($("#devTypeInfoMap").val());
        change_model(devTypeInfoMap.vendor_id);
        $("#speversion").val(devTypeInfoMap.specversion);
        $("#hard_version_add").val(devTypeInfoMap.hardwareversion);
        $("#soft_version_add").val(devTypeInfoMap.softwareversion);
        document.getElementsByName("is_check_add")[0].value = devTypeInfoMap.is_check == "" ? "-2" : devTypeInfoMap.is_check;
        document.getElementsByName("rela_dev_type_add")[0].value = devTypeInfoMap.rela_dev_type_id == "" ? "-1" : devTypeInfoMap.rela_dev_type_id;
        $("#specId").val(devTypeInfoMap.spec_id);
        $("#deviceTypeId").val(devTypeInfoMap.devicetype_id);
        $("#device_version_type").val(devTypeInfoMap.device_version_type);
        document.getElementsByName("startOpenDate_add")[0].value=devTypeInfoMap.versionttime;

        if (devTypeInfoMap.ip_model_type == 0) {
            document.getElementsByName("ipType")[0].checked = "checked";
        } else if (devTypeInfoMap.ip_model_type == 1) {
            document.getElementsByName("ipType")[1].checked = "checked";
        } else if (devTypeInfoMap.ip_model_type == 2) {
            document.getElementsByName("ipType")[2].checked = "checked";
        } else  if (devTypeInfoMap.ip_model_type == 3){
            document.getElementsByName("ipType")[3].checked = "checked";
        }else{
           document.getElementsByName("ipType")[4].checked = "checked";
        }

        if(devTypeInfoMap.zeroconf == 1){
            document.getElementsByName("machineConfig_add")[0].checked = "checked";
        }else{
            document.getElementsByName("machineConfig_add")[1].checked = "checked";
        }

        if(devTypeInfoMap.mbbroadband == 1){
            document.getElementsByName("mbBroadband_add")[0].checked = "checked";
        }else{
            document.getElementsByName("mbBroadband_add")[1].checked = "checked";
        }

        if(devTypeInfoMap.is_normal==1){
            document.getElementsByName("isNormal")[0].checked="checked";
        }else {document.getElementsByName("isNormal")[1].checked="checked";}

        if(devTypeInfoMap.is_qoe==1){
            document.getElementsByName("is_qoe_add")[0].checked="checked";
        }else {document.getElementsByName("is_qoe_add")[1].checked="checked";}


        //默认设备类型不可修改
        $("select[@name='rela_dev_type_add']").attr("disabled",true);
        queryTypeName(devTypeInfoMap.access_style_relay_id);
        getPortAndType($("#deviceTypeId").val());
        //document.getElementsByName("type_id")[0].value = devTypeInfoMap.type_id;
        disableLabel(true);
    }
    function getVendorList() {
        var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
        $.post(url,{
        },function(ajax){
            var vendorIdSelect = "-1";
            if($("#devTypeInfoMap").val() != ""){
                var devTypeInfoMap = JSON.parse($("#devTypeInfoMap").val());
                vendorIdSelect = devTypeInfoMap.vendor_id;
            }

            gwShare_parseMessage(ajax,$("#vendor_add"),vendorIdSelect);
        });
    }

    function change_model(value){
        var vendorId = $("#vendor_add").val();
        if(value !== "-1"){
            vendorId = value;
        }
        var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
        if("-1" === vendorId){
            $("#device_model_add").html("<option value='-1'>==请先选择设备厂商==</option>");
            return;
        }
        $.post(url,{
            gwShare_vendorId : vendorId
        },function(ajax){
            var modelIdSelect = "-1";
            if($("#devTypeInfoMap").val() != ""){
                var devTypeInfoMap = JSON.parse($("#devTypeInfoMap").val());
                modelIdSelect = devTypeInfoMap.device_model_id;
            }
            gwShare_parseMessage(ajax,$("#device_model_add"),modelIdSelect);
        });
    }

    function gwShare_parseMessage(ajax,field,selectvalue){
        var flag = true;
        if("" === ajax){
            return;
        }
        var lineData = ajax.split("#");
        if(!typeof(lineData) || !typeof(lineData.length)){
            return false;
        }
        field.html("");

        var option = "<option value='-1' selected>==请选择==</option>";
        field.append(option);
        for(var i=0;i<lineData.length;i++){
            var oneElement = lineData[i].split("$");
            var xValue = oneElement[0];
            var xText = oneElement[1];
            if(selectvalue === xValue){
                flag = false;
                //根据每组value和text标记的值创建一个option对象
                option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
            }else{
                //根据每组value和text标记的值创建一个option对象
                option = "<option value='"+xValue+"'>=="+xText+"==</option>";
            }
            try{
                field.append(option);
            }catch(e){
                alert("设备型号检索失败！");
            }
        }
        if(flag){
            field.attr("value","-1");
        }
    }

    function queryTypeName(typeId){
        var url = "<s:url value='/itms/resource/deviceTypeInfo!getTypeNameList.action'/>";
        $.post(url,{
            typeId:typeId
        },function(mesg){
            document.getElementById("typeNameList").innerHTML = mesg;
            if(typeId != "" && typeId != null){
                document.getElementsByName("type_id")[0].value = typeId;
            }
        });
    }

    //全部trim
    function trimAll()
    {
        var inputs = document.getElementsByTagName("input");
        for(var i=0;i<inputs.length;i++)
        {
            var input = inputs[i];
            if(/text/gi.test(input.type))
            {
                input.value = trim(input.value);
            }
        }
    }

    //提交保存
    function save() {
        trimAll();
        if (!CheckForm())
            return;
        var url = "<s:url value='/itms/resource/deviceTypeInfo!addDevType.action'/>";
        var vendor = $("select[@name='vendor_add']").val();
        var device_model = $("select[@name='device_model_add']").val();
        var speversion = $("input[@name='speversion']").val();
        var hard_version = $("input[@name='hard_version_add']").val();
        var soft_version = $("input[@name='soft_version_add']").val();
        var is_check = $("select[@name='is_check_add']").val();
        var rela_dev_type = $("select[@name='rela_dev_type_add']").val();
        var deviceTypeId = $("#deviceTypeId").val();
        var typeId = $("select[@name='type_id']").val();
        var protocol2 = $("input[@name='protocol2']");
        var protocol1 = $("input[@name='protocol1']");
        var protocol0 = $("input[@name='protocol0']");
        var is_esurfing = $("input[@name='is_esurfing']:checked").val();
        is_esurfing = is_esurfing == null ? "" : is_esurfing;
        var ipType = $("input[@name='ipType']:checked").val() == null ? "" : $("input[@name='ipType']:checked").val();
        var isNormal = $("input[@name='isNormal']:checked").val();
        var gw_type = "1";
        var specId = $("select[@name='specId']").val();

        var servertype = "";

        if (protocol2.attr("checked") == true) {
            servertype = servertype + "2";
        }
        if (protocol1.attr("checked") == true) {
            if (servertype == "") {
                servertype = "1";
            } else {
                servertype = servertype + "@1";
            }

        }
        if (protocol0.attr("checked") == true) {
            if (servertype == "") {
                servertype = "0";
            } else {
                servertype = servertype + "@0";
            }

        }

        var machineConfig = $("input[@name='machineConfig_add']:checked").val();
        machineConfig = machineConfig == null ? "" : machineConfig;

        var is_QOE = $("input[@name='is_qoe_add']:checked").val();
        is_QOE = is_QOE == null ? "" : is_QOE;
        var startOpenDate = "2020-02-17 20:30:00";
        var mbBroadband = $("input[@name='mbBroadband_add']:checked").val();
        mbBroadband = mbBroadband == null ? "" : mbBroadband;
        //是否支持awifi
        var is_awifi = "";

        //是否支持组播
        var is_multicast = "";
        var is_speedtest = "";
        var device_version_type = $("select[@name='device_version_type']").val();
        var wifi = "";
        var wifi_frequency = "";
        var gigabit_port = "";
        var gigabit_port_type = "";
        var download_max_wifi = "";
        var download_max_lan = "";
        var power = "";

        $.post(url, {
            deviceTypeId: deviceTypeId,
            vendor: vendor,
            device_model: device_model,
            hard_version: encodeURIComponent(hard_version),
            speversion: encodeURIComponent(speversion),
            soft_version: encodeURIComponent(soft_version),
            is_check: is_check,
            typeId: typeId,
            rela_dev_type: rela_dev_type,
            servertype: servertype,
            portInfo: "",
            ipType: ipType,
            isNormal: isNormal,
            gw_type: gw_type,
            editDeviceType:"0",
            mbBroadband: mbBroadband,
            startOpenDate: startOpenDate,
            machineConfig: machineConfig,
            is_awifi: is_awifi,
            is_QOE: is_QOE,
            is_multicast: is_multicast,
            specId: specId,
            is_speedtest: is_speedtest,
            reason: "",
            is_esurfing: is_esurfing,
            gbBroadband: "",
            device_version_type: device_version_type,
            wifi: wifi,
            wifi_frequency: wifi_frequency,
            download_max_wifi: download_max_wifi,
            gigabit_port: gigabit_port,
            gigabit_port_type: gigabit_port_type,
            download_max_lan: download_max_lan,
            power: power
        }, function (ajax) {
            window.close();
            alert(ajax);
        });
    }


    function CheckForm(){
        if($("#vendor_add").val() === "-1" || $("#vendor_add").val() === "")
        {
            alert("请选择厂商！");
            return false;
        }
        if($("#device_model_add").val() == "-1" ||  $("#device_model_add").val() == "")
        {
            alert("请选择设备型号！");
            return false;
        }

        if($("#hard_version_add").val() == "")
        {
            alert("请填写硬件版本！");
            return false;
        }

        if($("#soft_version_add").val()=="")
        {
            alert("请填写软件版本！");
            return false;
        }
        var isCheckAdd = $("select[@name='is_check_add']").val();

        if(isCheckAdd == ""  || isCheckAdd == "-2")
        {
            alert("请选择是否审核！");
            return false;
        }
        var realDevType = $("select[@name='rela_dev_type_add']").val();
        if(realDevType == "-1" || realDevType == ""){
            alert("请选择设备类型！");
            return false;
        }

        var devVersionType = $("select[@name='device_version_type']").val();
        if(devVersionType == "-1" || devVersionType == ""){
            alert("请选择设备版本类型！");
            return false;
        }


        return true;
    }

    function getPortAndType (deviceTypeId){
        var url = "<s:url value='/itms/resource/deviceTypeInfo!getPortAndType.action'/>";
        $.post(url,{
            deviceTypeId:deviceTypeId
        },function(mesg){
            var temp=mesg.split("&");
            var servType=temp[1].split(",");

            var protocol2 = $("input[@name='protocol2']");
            var protocol1 = $("input[@name='protocol1']");
            var protocol0 = $("input[@name='protocol0']");

            protocol2.attr("checked",false);
            protocol1.attr("checked",false);
            protocol0.attr("checked",false);
            for(var j=0;j<servType.length;j++){
                if(protocol2.val()==servType[j]){
                    protocol2.attr("checked",true)
                }
                if(protocol1.val()==servType[j]){
                    protocol1.attr("checked",true)
                }
                if(protocol0.val()==servType[j]){
                    protocol0.attr("checked",true)
                }
            }
        });
    }

    // 某些字段不允许编辑
    function disableLabel(tag)
    {
        $("select[@name='vendor_add']").attr("disabled",tag);
        $("select[@name='device_model_add']").attr("disabled",tag);
        $("input[@name='speversion']").attr("disabled",false);
        $("input[@name='hard_version_add']").attr("disabled",tag);
        $("input[@name='soft_version_add']").attr("disabled",tag);
        $("input[@name='speversion']").attr("disabled",tag);
    }

    function clearData()
    {
        document.getElementsByName("vendor_add")[0].value=-1;
        change_model('deviceModel','-1');
        document.getElementsByName("speversion")[0].value="";
        document.getElementsByName("hard_version_add")[0].value="";
        document.getElementsByName("soft_version_add")[0].value="";
        document.getElementsByName("is_check_add")[0].value=0;
        document.getElementsByName("rela_dev_type_add")[0].value=1;
        document.getElementById("actLabel").innerHTML="添加";
    }

    function trim(str){
        return RTrim(LTrim(str)).toString();
    }
    function RTrim(str){
        var whitespace = new String("　 \t\n\r");
        var s = new String(str);

        if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
            var i = s.length - 1;
            while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
                i--;
            }
            s = s.substring(0, i+1);
        }
        return s;
    }

    function LTrim(str){
        var whitespace = new String("　 \t\n\r");
        var s = new String(str);

        if (whitespace.indexOf(s.charAt(0)) != -1){
            var j=0, i = s.length;
            while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
                j++;
            }
            s = s.substring(j, i);
        }
        return s;
    }
</script>

