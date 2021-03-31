<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<html xmlns:s="http://www.w3.org/1999/xhtml">
<head>
    <title>版本管理</title>
    <script type="text/javascript" src="../../Js/CheckForm.js"></script>
    <script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
    <style>
        table tr td input[type="text"],
        table tr td select{
            width: 225px
        }
        .mytable{
            border-top: solid 1px #999;
            border-right: solid 1px #999;
        }
        .mytable th, .mytable td{
            border-bottom: solid 1px #999;
            border-left: solid 1px #999;
        }

    </style>
</head>

<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../../Js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="../../zTree_v3/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="../../Js/json2.js"></script>
<%@ include file="/toolbar.jsp"%>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
    <tr>
        <td HEIGHT=20>&nbsp;</td>
    </tr>
    <tr>
        <td>
            <form NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="devVersionForm">
                <input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
                <input type='hidden' id="updateId" value="-1" />
                <input type='hidden' id="vendorId" value="-1" />
                <input type='hidden' id="modelId" value="-1" />
                <input type='hidden' id="total" value="0" />
                <table width="98%" height="30" border="0" align="center"
                       cellpadding="0" cellspacing="0" class="green_gargtd">
                    <tr>
                        <td width="162">
                            <div align="center" class="title_bigwhite">设备版本</div>
                        </td>
                        <td><img src="/itms/images/attention_2.gif" width="15"
                                 height="12">查询时间为设备版本的添加时间</td>
                        <td align="right"><input type='button' onclick='Add()'
                                                 value=' 增 加 ' class="jianbian" id='idAdd' /></td>
                    </tr>
                </table>
                <!-- 高级查询part -->
                <TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
                    <tr>
                        <td>
                            <table class="mytable" width="100%"
                                   align="center">
                                <tr>
                                    <th colspan="4" id="gwShare_thTitle">设备版本查询</th>
                                </tr>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">设备厂商</TD>
                                    <TD align="left" width="35%"><select name="vendor" id="vendor" class="bk"
                                                                         onchange="getModelListByVendor('vendor','deviceModel')">
                                    </select></TD>
                                    <TD align="right" class=column width="15%">设备型号</TD>
                                    <TD width="35%"><select name="device_model" id="deviceModel" class="bk">
                                        <option value="-1">==请选择厂商==</option>
                                    </select></TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">硬件版本</TD>
                                    <TD align="left" width="35%"><INPUT TYPE="text"
                                                                        NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
                                            color="#FF0000"></font></TD>
                                    <TD align="right" class=column width="15%">软件版本</TD>

                                    <TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
                                                                  maxlength=30 class=bk size=20>&nbsp<font color="#FF0000">支持后匹配</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">是否审核</TD>
                                    <TD align="left" width="35%"><select name="is_check"
                                                                         class="bk">
                                        <option value="-2">==请选择==</option>
                                        <option value="1">经过审核</option>
                                        <option value="-1">未审核</option>
                                    </select></TD>
                                    <TD align="right" class=column width="15%">设备版本类型</TD>
                                    <TD width="35%">
                                        <s:select list="devVersionTypeMap" name="deviceVersionType" listKey="value"
                                                  listValue="text" cssClass="bk"></s:select>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">上行方式</TD>
                                    <TD align="left" width="35%"><select
                                            name="access_style_relay_id" class="bk">
                                        <option value="-1">==请选择==</option>
                                        <option value="1">ADSL</option>
                                        <option value="2">LAN</option>
                                        <option value="3">EPON光纤</option>
                                        <option value="4">GPON光纤</option>
                                    </select></TD>
                                    <TD align="right" class=column width="15%">终端规格</TD>
                                    <td width="35%">
                                        <s:select list="specList" name="spec_id" headerKey="-1"
                                                  headerValue="请选择终端规格" listKey="id" listValue="spec_name"
                                                  value="spec_id" cssClass="bk"></s:select>
                                    </td>
                                </TR>

                                    <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                        <TD align="right" class=column width="15%">是否支持机顶盒零配置</TD>
                                        <TD align="left" width="35%">
                                            <select name="machineConfig" class="bk">
                                                <option value="-1">==请选择==</option>
                                                <option value="1">是</option>
                                                <option value="2">否</option>
                                            </select>
                                        </TD>
                                        <TD align="right" class=column width="15%">是否支持IPV6</TD>
                                        <TD align="left" width="35%">
                                            <select name="ipvsix" class="bk">
                                                <option value="-1">==请选择==</option>
                                                <option value="1">是</option>
                                                <option value="2">否</option>
                                            </select>
                                        </TD>
                                    </TR>
                                    <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                        <TD class=column width="15%" align='right'>版本定版开始时间</TD>
                                        <TD width="35%">
                                            <input type="text" name="startOpenDate" readonly class=bk >
                                            <img name="shortDateimg"
                                                 onClick="WdatePicker({el:document.mainForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                                 src="../../images/dateButton.png" width="15" height="12"
                                                 border="0" alt="选择">
                                        </TD>
                                        <TD class=column width="15%" align='right'>版本定版结束时间</TD>
                                        <TD width="35%">
                                            <input type="text" name="endOpenDate" readonly class=bk >
                                            <img name="shortDateimg"
                                                 onClick="WdatePicker({el:document.mainForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                                 src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择">
                                        </TD>
                                    </TR>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">是否支持百兆宽带</TD>
                                    <TD align="left" width="35%">
                                        <select name="mbBroadband" class="bk">
                                            <option value="-1">==请选择==</option>
                                            <option value="1">是</option>
                                            <option value="2">否</option>
                                        </select>
                                    </TD>
                                    <TD align="right" class=column width="15%"></TD>
                                    <TD align="left" width="35%"></TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                                    <TD align="right" class=column width="15%">开始时间</TD>
                                    <TD align="left" width="35%"><lk:date id="startTime"
                                                                          name="startTime" type="all" /></TD>
                                    <TD align="right" class=column width="15%">结束时间</TD>
                                    <TD align="left" width="35%"><lk:date id="endTime"
                                                                          name="endTime" type="all" /></TD>
                                </TR>
                                <tr bgcolor="#FFFFFF">
                                    <td colspan="4" align="right" class="green_foot" width="100%">
                                        <input type="button"
                                               onclick="javascript:queryDevice()" class=jianbian
                                               name="gwShare_queryButton" value=" 查 询 " />
                                        <input type="button"
                                               class=jianbian name="gwShare_reButto" value=" 重 置 "
                                               onclick="javascript:queryReset();" />
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
            <!-- 展示结果part -->
            <table class="mytable" style="margin-left:10px">
                <thead>
                <tr>
                    <th>设备厂商选择</th>
                    <th>设备版本详情</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td id="devTreeTD" width="200px" valign="top" rowspan="2">
                        <div>
                            <ul id="devVersionTree" class="ztree"></ul>
                        </div>
                    </td>
                    <td id="devVersionTd" valign="top" width="1100px">
                        <iframe id="devVersionForm" name="devVersionForm"
                                height="300px" frameborder="0" scrolling="no" width="100%" src=""></iframe>
                    </td>
                </tr>
                <!--编辑设备类型-->
                <tr>
                    <TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="editDeviceTypeTable" style="display: none">
                        <TR>
                            <TD bgcolor=#999999>
                                <TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="allDatas">
                                    <TR>
                                        <TH colspan="4" align="center">编辑设备类型</TH>
                                    </TR>
                                    <TR bgcolor="#FFFFFF">
                                        <TD class=column align="right">设备类型</TD>
                                        <TD colspan=3>
                                            <s:select list="devTypeMap" id="rela_dev_type_edit" name="rela_dev_type_edit"
                                                      headerKey="-1" headerValue="请选择设备类型" listKey="type_id"
                                                      listValue="type_name" cssClass="bk">
                                            </s:select>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                            <TR>
                            <TD bgcolor=#999999>
                                <TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
                                    <TR bgcolor="#FFFFFF">
                                        <TD align="right" CLASS=green_foot>
                                            <INPUT TYPE="button" onclick="javascript:saveEditDeviceType()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
                                            <INPUT TYPE="button" onclick="javascript:editDeviceTypeClose()" value="关 闭 " class=jianbian>&nbsp;&nbsp;
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                    </TABLE>
                </tr>
                </tbody>

            </table>

        </td>
    </tr>
    <TR>
        <TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
    </TR>

</table>
</body>
<%@ include file="/foot.jsp"%>

<script>

    function init(){
        // 初始化厂家
        getVendorList();
        setValue();
        queryDevice();
    }
    init();

    function getVendorList() {
        var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
        $.post(url,{
        },function(ajax){
            gwShare_parseMessage(ajax,$("#vendor"),"-1");
            gwShare_parseMessage(ajax,$("#vendor_add"),"-1");
        });
    }

    function getModelListByVendor(object,descObject) {
        var vendorId = $("#" + object).val();
        var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
        if("-1" === vendorId){
            $("#" + descObject).html("<option value='-1'>==请先选择设备厂商==</option>");
            return;
        }
        $.post(url,{
            gwShare_vendorId : vendorId
        },function(ajax){
            gwShare_parseMessage(ajax,$("#" + descObject),"-1");
        });
    }

    //查询
    function queryDevice()
    {
        trimAll();
        var url = "<s:url value='/itms/resource/deviceTypeInfo!queryDevVersionTreeList.action'/>";
        $.post(url,{
            vendor : document.getElementsByName("vendor")[0].value,
            device_model : document.getElementsByName("device_model")[0].value,
            hard_version : document.getElementsByName("hard_version")[0].value,
            soft_version : document.getElementsByName("soft_version")[0].value,
            is_check : document.getElementsByName("is_check")[0].value,
            startTime : document.getElementsByName("startTime")[0].value,
            endTime : document.getElementsByName("endTime")[0].value,
            access_style_relay_id : document.getElementsByName("access_style_relay_id")[0].value,
            spec_id : document.getElementsByName("spec_id")[0].value,
            machineConfig : document.getElementsByName("machineConfig")[0].value,
            ipvsix : document.getElementsByName("ipvsix")[0].value,
            startOpenDate : document.getElementsByName("startOpenDate")[0].value,
            endOpenDate : document.getElementsByName("endOpenDate")[0].value,
            deviceVersionType : document.getElementsByName("deviceVersionType")[0].value
        },function(ajax){
            var treeData = "";
            if(ajax !== ""){
                treeData = JSON.parse(ajax);
            }
            $.fn.zTree.init($("#devVersionTree"), setting, treeData);

        });
        //showAddPart(false);
    }

    function showIconForTree(treeId, treeNode) {
        return !treeNode.isParent;
    }
    var setting = {
        view: {
            showIcon: showIconForTree
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: onNodeClick
        }
    };

    //树节点点击 查询厂商型号对应的设备版本列表
    function onNodeClick(event, treeId, treeNode) {
        if(treeNode.children == null){
            var vendorId = treeNode.data[0].vendor_id;
            var devVersionTotal = treeNode.data[0].length;
            var isCheck = treeNode.data[0].is_check;
            var form = document.getElementById("mainForm");
            form.action = "<s:url value='/itms/resource/deviceTypeInfo!getDevVersionList.action'/>" +
                "?vendorId=" + vendorId + "&modelId=" + treeNode.id + "&devVersionTotal=" + devVersionTotal + "&isCheck=" + isCheck;
            form.submit();

            /*var url = "<s:url value='/itms/resource/deviceTypeInfo!getDevVersionList.action'/>";
            $.post(url,{
                vendor : treeNode.pId,
                device_model : treeNode.id,
                hard_version : document.getElementsByName("hard_version")[0].value,
                soft_version : document.getElementsByName("soft_version")[0].value,
                is_check : document.getElementsByName("is_check")[0].value,
                startTime : document.getElementsByName("startTime")[0].value,
                endTime : document.getElementsByName("endTime")[0].value,
                access_style_relay_id : document.getElementsByName("access_style_relay_id")[0].value,
                spec_id : document.getElementsByName("spec_id")[0].value,
                machineConfig : document.getElementsByName("machineConfig")[0].value,
                ipvsix : document.getElementsByName("ipvsix")[0].value,
                startOpenDate : document.getElementsByName("startOpenDate")[0].value,
                endOpenDate : document.getElementsByName("endOpenDate")[0].value,
                deviceVersionType : document.getElementsByName("deviceVersionType")[0].value,
                devVersionTotal : devVersionTotal
            },function(ajax){
                debugger;
                $("#devVersionForm").append(ajax);
            });*/
        }
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

    function setValue(){
        document.getElementById("startTime").value="";
        document.getElementById("endTime").value="";
    }
    // 隐藏页面下面的添加区域
    function showAddPart(tag)
    {
        if(tag)
            $("#addTable").show();
        else
            $("#addTable").hide();
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
        document.getElementsByName("reason")[0].value="";
        document.getElementById("updateId").value="-1";
        document.getElementById("actLabel").innerHTML="添加";
    }

    //编辑设备类型 保存
    function saveEditDeviceType(){
        var rela_dev_type = $("#rela_dev_type_edit").val();
        if(rela_dev_type === "-1" || rela_dev_type === "")
        {
            alert("请选择设备型号！");
            return false;
        }
        var deviceTypeId = $("#updateId").val();
        var gw_type = "1";
        var url = "<s:url value='/itms/resource/deviceTypeInfo!updateDeviceType.action'/>";
        $.post(url,{
            gw_type:gw_type,
            deviceTypeId:deviceTypeId,
            rela_dev_type:rela_dev_type
        },function(ajax){
            alert(ajax);
            if(ajax.indexOf("成功") != -1)
            {
                var form = window.parent.document.getElementById("mainForm");
                form.action = "<s:url value='/itms/resource/deviceTypeInfo!getDevVersionList.action'/>" +
                    "?vendorId=" + $("#vendorId").val() + "&modelId=" + $("#modelId").val() + "&devVersionTotal=" + $("#total").val();
                form.submit();
            }
        });
        showEditDeviceTypePart(false);
    }

    //新增
    function Add() {
        window.open("<s:url value='/itms/resource/deviceTypeInfo!addDevVersionAHLT.action'/>?deviceTypeId=-1","",
            "left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
    }

    function showEditDeviceTypePart(flag){
        if(flag){
            $("#editDeviceTypeTable").show();
        }else {
            $("#editDeviceTypeTable").hide();
        }
    }

    function editDeviceTypeClose() {
        $("#editDeviceTypeTable").hide();
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