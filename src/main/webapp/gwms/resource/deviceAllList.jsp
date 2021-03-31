<%@ include file="../../timelater.jsp" %>
<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>

<%
    String isJs = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<script type="text/javascript">
  var flag = "";
  var gw_type = '<s:property value="gw_type"/>';

  $(function () {
    var isJs = "<%=isJs%>";
    if (isJs == 'sx_lt') {
      initDateForSxlt();
    }
    else {
      initDate();
    }
    var isFirst = '<s:property value="no_query"/>';
    if (isFirst != 'true' && isJs == 'js_dx') {
      $("input[@name='button']").attr("disabled", false);
    }
    <%-- 屏蔽属地查询，还原设备序列号查询
    gwShare_change_select("city","-1");
    --%>
  });

  function initDateForSxlt () {
    //初始化时间改为一个月到现在
    theday = new Date();
    day = theday.getDate();
    startMonth = theday.getMonth();
    month = theday.getMonth() + 1;
    year = theday.getFullYear();
    startYear = year - 1;
    hour = theday.getHours();
    mimute = theday.getMinutes();
    second = theday.getSeconds();

    if (startMonth == 0) {
      $("input[@name='starttime']").val(startYear + "-" + "12" + "-" + day + " " + hour + ":" + mimute + ":" + second);
    } else {
      // 为当前一个月前
      $("input[@name='starttime']").val(year + "-" + startMonth + "-" + day + " " + hour + ":" + mimute + ":" + second);
      $("input[@name='endtime']").val(year + "-" + month + "-" + day + " " + hour + ":" + mimute + ":" + second);
    }
  }

  function gwShare_change_select (type, selectvalue) {
    switch (type) {
      case "city":
        var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
        $.post(url, {}, function (ajax) {
          gwShare_parseMessage(type, ajax, $("select[@name='gwShare_cityId']"), selectvalue);
        });
        break;
      case "vendor":
        var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
        $.post(url, {}, function (ajax) {
          gwShare_parseMessage(type, ajax, $("select[@name='gwShare_vendorId']"), selectvalue);
          $("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
          $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
        });
        break;
      case "deviceModel":
        var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
        var vendorId = $("select[@name='gwShare_vendorId']").val();
        if ("-1" == vendorId) {
          $("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
          $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
          break;
        }
        $.post(url, {
          gwShare_vendorId: vendorId
        }, function (ajax) {
          gwShare_parseMessage(type, ajax, $("select[@name='gwShare_deviceModelId']"), selectvalue);
          $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
        });
        break;
      case "devicetype":
        var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
        var vendorId = $("select[@name='gwShare_vendorId']").val();
        var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
        if ("-1" == deviceModelId) {
          $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
          break;
        }
        $.post(url, {
          gwShare_vendorId: vendorId,
          gwShare_deviceModelId: deviceModelId
        }, function (ajax) {
          gwShare_parseMessage(type, ajax, $("select[@name='gwShare_devicetypeId']"), selectvalue);
        });
        break;
      default:
        alert("未知查询选项！");
        break;
    }
  }

  function gwShare_parseMessage (type, ajax, field, selectvalue) {
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
      if (selectvalue == xValue) {
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

  //初始化时间
  function initDate () {
    //初始化时间  开启 by zhangcong 2011-06-02
    theday = new Date();
    day = theday.getDate();
    month = theday.getMonth() + 1;
    year = theday.getFullYear();
    hour = theday.getHours();
    mimute = theday.getMinutes();
    second = theday.getSeconds();

    flag = '<s:property value="starttime"/>';
    if (null != flag && "" != flag) {
      $("input[@name='starttime']").val('<s:property value="starttime"/>');
      $("input[@name='endtime']").val('<s:property value="endtime"/>');
    } else {
      //modify by zhangcong 开始时间默认为当年的第一天2011-06-02
      $("input[@name='starttime']").val(year + "-1-1 00:00:00");
      $("input[@name='endtime']").val(year + "-" + month + "-" + day + " " + hour + ":" + mimute + ":" + second);
    }
  }

  function ToExcel () {
    var page = "../../gwms/resource/queryDevice!getInfoExcelDevice.action?"
      + "&starttime=" + document.frm.starttime.value
      + "&endtime=" + document.frm.endtime.value
      + "&timeType=" + document.frm.timeType.value
      + "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
      + "&loopback_ip=" + document.frm.loopback_ip.value;
    document.all("childFrm").src = page;
    //window.open(page);
  }

  function do_test () {
    //2个参数必须至少输入一个 add by zhangcong@ 2011-07-05

    //设备序列号
    var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
    //设备IP
    var loopback_ip = $.trim(document.frm.loopback_ip.value);

    //2个参数至少一个
    <%-- 屏蔽按属地查询，还原设备序列号查询
    if(parseInt(gwShare_cityId) == -1){
            alert("至少选择属地！");
            return false;
    }--%>

    //如果输了设备序列号，就必须合法
    if (device_serialnumber.length < 6 && device_serialnumber.length > 0) {
      alert("请至少输入最后6位设备序列号进行查询！");
      document.frm.device_serialnumber.focus();
      return false;
    }

    document.frm.gw_type.value = gw_type;

    $("input[@name='button']").attr("disabled", true);
    frm.submit();

  }

  function DelDevice (device_id) {
    if (!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")) {
      return false;
    }
    var url = "../../Resource/DeviceSave.jsp";
    var pars = "device_id=" + device_id;
    pars += "&gw_type=" + <s:property value="infoType" />;
    pars += "&tt=" + new Date().getTime();
    pars += "&_action=delete";
    var gw_type = <s:property value="infoType" />;
    var deluser;
    if (confirm("是否需要删除设备对应的用户帐号")) {
      deluser = true;
    } else {
      deluser = true;
    }
    var _action = "delete";
    $.post(url, {
      device_id: device_id,
      gw_type: gw_type,
      tt: new Date().getTime(),
      _action: _action,
      deluser: deluser
    }, function (ajax) {
      eval(ajax);
    });
    return true;
  }

  function EditDevice (device_id) {
    var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id;
    window.location.href = strpage;
  }

  function DetailDevice (device_id) {
    var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
    window.open(strpage, "", "left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
  }

  function refresh () {
    window.location.href = window.location.href;
  }

  //reset
  function resetFrm () {
    //document.frm.starttime.value="";
    //document.frm.endtime.value="";
    //初始化时间 modify by zhangcong 2011-06-03
    initDate();
    document.frm.device_serialnumber.value = "";
    document.frm.loopback_ip.value = "";
    document.frm.timeType.value = "-1";
  }

  //-->
</script>

<form name="frm"
      action="<s:url value='/gwms/resource/queryDevice!deviceAll.action"'/>"
      method="POST">

    <input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">

    <table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
        <tr>
            <td height="20"></td>
        </tr>
        <TR>
            <TD>
                <TABLE width="100%" height="30" border="0" cellspacing="0"
                       cellpadding="0" class="green_gargtd">
                    <TR>
                        <TD width="164" align="center" class="title_bigwhite">设备资源</TD>
                        <td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
                                        width="15" height="12"> &nbsp;设备列表,选择时间类型确定所要查询的时间。
                        </td>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <tr>
            <td bgcolor=#999999>
                <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                    <tr>
                        <th colspan="4">设备查询</th>
                    </tr>
                    <TR bgcolor=#ffffff>
                        <td class="column" width='15%' align="right">设备序列号：</td>
                        <td width='35%' align="left">
                            <input name="device_serialnumber" type="text" class='bk'
                                   value="<s:property value='device_serialnumber'/>">
                           <% if(!"nx_lt".equals(isJs)){%>
								 <font color="red">*</font>
								<%} %>
                         </td>
                        <%-- 屏蔽按属地查询，还原设备序列号查询
                        <td class="column" width='15%' align="right">属地：</td>
                        <td width='35%' align="left">
                            <select name="gwShare_cityId" class="bk">
                                <option value="-1">==全部==</option>
                            </select>
                            <font color="red">*</font>
                        </td>
                         --%>
                        <td class="column" width='15%' align="right">设备IP：</td>
                        <td width='35%' align="left"><input name="loopback_ip"
                                                            type="text" class='bk'
                                                            value="<s:property value='loopback_ip'/>">
                            <% if(!"nx_lt".equals(isJs)){%>
								 <font color="red">*</font>
								<%} %>
                          </td>
                    </TR>
                    <TR bgcolor=#ffffff>
                        <td class="column" width='15%' align="right">开始时间</td>
                        <td width='35%' align="left"><input type="text"
                                                            name="starttime" class='bk' readonly
                                                            value="<s:property value='starttime'/>"> <img
                                onclick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                src="<s:url value='/images/search.gif'/>" width="15" height="12"
                                border="0" alt="选择"></td>
                        <td class="column" width='15%' align="right">结束时间</td>
                        <td width='35%' align="left"><input type="text" name="endtime"
                                                            class='bk' readonly value="<s:property value='endtime'/>">
                            <img
                                    onclick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                    src="<s:url value='/images/search.gif'/>" width="15" height="12"
                                    border="0" alt="选择"></td>
                    </TR>
                    <TR bgcolor=#ffffff>
                        <td class="column" width='15%' align="right">查询时间类型</td>
                        <td width='35%' align="left" colspan="3"><select
                                name="timeType" class="bk">
                            <option value="-1"
                                    <s:property value='"-1".equals(timeType)?"selected":""'/>>
                                ==请选择==
                            </option>
                            <option value="1"
                                    <s:property
                                            value='"1".equals(timeType)||(!"2".equals(timeType)&&!"-1".equals(timeType))?"selected":""'/>>
                                ==上报时间==
                            </option>
                            <option value="2"
                                    <s:property value='"2".equals(timeType)?"selected":""'/>>
                                ==绑定时间==
                            </option>
                        </select></td>

                    </TR>

                    <TR>
                        <td class="green_foot" colspan="4" align="right"><input
                                class=jianbian name="button" type="button" onclick="do_test();"
                                value=" 查 询 "> <INPUT class=jianbian TYPE="button"
                                                      value=" 重 置 " onclick="resetFrm()"> <s:if
                                test='#session.isReport=="1"'>
                            <INPUT TYPE="button" value=" 导 出 " class=jianbian
                                   onclick="ToExcel()">
                        </s:if></TD>
                    </TR>

                </TABLE>
            </TD>
        </TR>
        <tr>
            <td bgcolor=#ffffff>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <table width="100%" border=0 cellspacing=1 cellpadding=2
                       bgcolor=#999999 id=userTable>
                    <tr>
                        <s:iterator value="title" status="status">
                            <td class="green_title"><s:property
                                    value="title[#status.index]"/></td>

                        </s:iterator>
                        <td class="green_title" align='center' width="10%">操作</td>
                    </tr>
                    <s:if test="data.size()>0">
                        <s:iterator value="data">
                            <tr bgcolor="#ffffff">
                                <s:if test="infoType == 1">
                                    <td class=column nowrap align="center"><s:property
                                            value="city_name"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="vendor_add"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="device_model"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="softwareversion"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="device"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="loopback_ip"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="complete_time"/></td>
                                    <td class=column nowrap align="center">
                                        <!-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='编辑'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='删除'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> --> <IMG
                                            SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
                                            onclick="DetailDevice('<s:property value="device_id"/>')"
                                            style="cursor: hand"></td>
                                </s:if>
                                <s:else>
                                    <td class=column nowrap align="center"><s:property
                                            value="city_name"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="vendor_add"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="device_model"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="softwareversion"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="device"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="loopback_ip"/></td>
                                    <td class=column nowrap align="center"><s:property
                                            value="complete_time"/></td>
                                    <td class=column nowrap align="center">
                                        <!-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
											ALT='编辑'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='删除'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> --> <IMG
                                            SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
                                            onclick="DetailDevice('<s:property value="device_id"/>')"
                                            style="cursor: hand"></td>
                                </s:else>
                            </tr>
                        </s:iterator>
                    </s:if>
                    <s:else>

                        <tr>
                            <td colspan=8 align=left class=column>系统没有相关的设备信息!</td>
                        </tr>

                    </s:else>

                </table>
            </td>
        </tr>
        <tr>
            <td align="right">
            <ms:inArea areaCode="nx_lt" notInMode="false">
				   [ 统计总数 : <s:property value='total' /> ]&nbsp;
				</ms:inArea>
            <lk:pages
                    url="/gwms/resource/queryDevice!gopageDeviceAll.action" styleClass=""
                    showType="" isGoTo="true" changeNum="true"/></td>
        </tr>
        <tr STYLE="display: none">
            <s:if test="infoType == 1">
                <td>
                    <iframe id="childFrm" src=""></iframe>
                </td>
            </s:if>
            <s:else>
                <td>
                    <iframe id="childFrm" src=""></iframe>
                </td>
            </s:else>
        </tr>
        <TR>
            <TD HEIGHT=20 align="center">
                <div id="_process"></div>
            </TD>
        </TR>
    </table>
</form>

<%@ include file="../foot.jsp" %>
