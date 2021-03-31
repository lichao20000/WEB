<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp" %>
<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../head.jsp" %>
<%
    String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
      type="text/css">
<SCRIPT LANGUAGE="JavaScript">
  $(function () {
    $("input[@name='gwShare_queryResultType']").val("checkbox");
    gwShare_setGaoji();
    gwShare_setImport();
  });
  var deviceIds = "";
  var returnValthis = "init";
  var max = '<s:property value="maxActive" escapeHtml="false" />';

  function CheckForm () {


    if ($("input[@name='deviceIds']").val() == "") {
      alert("请选择设备！");
      return false;
    }
    var mode = $("#mode").val();
    if (mode == "2") {
      var starttime = $("input[@name='expire_time_start']").val();
      var endtime = $("input[@name='expire_time_end']").val();
      var startdate = $("input[@name='expire_date_start']").val();
      var enddate = $("input[@name='expire_date_end']").val();

      if(enddate<startdate || starttime>endtime){
          alert("触发开始日期/时间小于触发结束日期/时间");
          return false;
      }

      if(startdate!=enddate){
          alert("触发开始日期和结束日期跨天！");
      }
    }
    else{
        $("#type").val($("#softStrategy_type").val());
    }


    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();

    var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
	var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());

	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
	var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
    var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
    var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());

    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
    $("#gwShare_queryTypeHidden").val(gwShare_queryType);
    $("#gwShare_queryFieldHidden").val(gwShare_queryField);
    $("#gwShare_queryParamHidden").val(gwShare_queryParam);
    $("#gwShare_cityIdHidden").val(gwShare_cityId);
    $("#gwShare_onlineStatusHidden").val(gwShare_onlineStatus);
    $("#gwShare_vendorIdHidden").val(gwShare_vendorId);
    $("#gwShare_deviceModelIdHidden").val(gwShare_deviceModelId);
    $("#gwShare_devicetypeIdHidden").val(gwShare_devicetypeId);
    $("#gwShare_bindTypeHidden").val(gwShare_bindType);
    $("#gwShare_deviceSerialnumberHidden").val(gwShare_deviceSerialnumber);
    $("#gwShare_fileNameHidden").val(gwShare_fileName);
    $("#fileName").val(gwShare_fileName);

    //alert("gwShare_queryType="+$("#gwShare_vendorIdHidden").val()+", gwShare_vendorId="+$("#gwShare_vendorIdHidden").val());
    //return false;
    return true;
  }

  function deviceResult (returnVal) {
    returnValthis = returnVal;
    var totalmem=0;
    if(returnVal[0]==0)
    {
      totalmem = returnVal[2].length;
      $("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+totalmem+"</strong></font>");
      for(var i=0;i<totalmem;i++){
          deviceIds = deviceIds + returnVal[2][i][0]+",";
      }
      $("input[@name='deviceIds']").val(deviceIds);
    }
    else
    {
      $("input[@name='task_desc']").val(returnVal[3]);
      if(returnVal[3]!==null&&returnVal[3]!==""){
          totalmem = returnVal[3];
      }else{
          totalmem = returnVal[0];
      }
      $("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+totalmem+"</strong></font>");
      $("input[@name='deviceIds']").val("0");
      $("input[@name='param']").val(returnVal[1]);
    }
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    $("#gwShare_queryType_this").val(gwShare_queryType);
  }

  function change_mode () {
    var mode = $("#mode").val();
    if (mode == "1") {
      $("#softUp").show();
      $("#date").hide();
      $("#time").hide();
    }
    else
    {
      $("#softUp").hide();
      $("#date").show();
      $("#time").show();
    }
  }


</SCRIPT>
<%@ include file="../../toolbar.jsp" %>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
    <TR>
        <TD HEIGHT=20>
            &nbsp;
        </TD>
    </TR>
    <TR>
        <TD>

            <table width="98%" border="0" align="center" cellpadding="0"
                   cellspacing="0" class="text">
                <tr>
                    <td>
                        <table width="100%" height="30" border="0" cellspacing="0"
                               cellpadding="0" class="green_gargtd">
                            <tr>
                                <td width="162" align="center" class="title_bigwhite" nowrap>
                                    批量软件升级
                                </td>
                                <td nowrap>
                                    <img src="<s:url value='/images/attention_2.gif'/>" width="15"
                                         height="12">

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <TR bgcolor="#FFFFFF">
                    <td colspan="4">
                        <jsp:include page="/gwms/share/gwShareDeviceQuery.jsp" flush="false">
                            <jsp:param name="CQ_softUp" value="1"/>
                            <jsp:param name="AHLT_softUp" value="1"/>
                        </jsp:include>
                    </td>
                </TR>
                <TR>
                    <TH colspan="4" align="center">
                        升级策略
                    </TH>
                </TR>
                <tr>
                    <td>
                        <FORM NAME="mainForm" id="mainForm" METHOD="post"
                              ACTION="<s:url value="/gwms/resource/software!batchUp4ahlt.action"/>"
                              onsubmit="return CheckForm()">
                            <input type="hidden" name="deviceIds" value=""/>
                            <input type="hidden" name="param" value=""/>
                            <input type="hidden" name="AHLT_softUp" value="1"/>
                            <input type="hidden" name="gw_type" value="<%=gwType%>"/>
                            <input type="hidden" name="maxActive" id="maxActive"/>
                            <input type="hidden" name="type" id="type"/>
                            <input type="hidden" name="gwShare_queryType_this" id="gwShare_queryType_this"/>
                            <input type="hidden" name="gwShare_queryTypeHidden" id="gwShare_queryTypeHidden"/>
                            <input type="hidden" name="gwShare_queryFieldHidden" id="gwShare_queryFieldHidden"/>
                            <input type="hidden" name="gwShare_queryParamHidden" id="gwShare_queryParamHidden"/>
                            <input type="hidden" name="gwShare_cityIdHidden" id="gwShare_cityIdHidden"/>
                            <input type="hidden" name="gwShare_onlineStatusHidden" id="gwShare_onlineStatusHidden"/>
                            <input type="hidden" name="gwShare_vendorIdHidden" id="gwShare_vendorIdHidden"/>
                            <input type="hidden" name="gwShare_deviceModelIdHidden" id="gwShare_deviceModelIdHidden"/>
                            <input type="hidden" name="gwShare_devicetypeIdHidden" id="gwShare_devicetypeIdHidden"/>
                            <input type="hidden" name="gwShare_bindTypeHidden" id="gwShare_bindTypeHidden"/>
                            <input type="hidden" name="gwShare_deviceSerialnumberHidden" id="gwShare_deviceSerialnumberHidden"/>
                            <input type="hidden" name="gwShare_fileNameHidden" id="gwShare_fileNameHidden"/>
                            <input type="hidden" name="fileName" id="fileName"/>
                            <input type="hidden" name="task_desc" id="task_desc"/>
                            <TABLE width="100%" border=0 cellspacing=0 cellpadding=0
                                   align="center">
                                <TR>
                                    <TD bgcolor=#999999>
                                        <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                                            <tr bgcolor="#FFFFFF">
                                                <td colspan="4">
                                                    <div id="selectedDev">
                                                        请查询设备！
                                                    </div>
                                                </td>
                                            </tr>
                                            <!-- add begin -->
                                            <tr bgcolor="#FFFFFF">
                                                <td align="right" width="15%">任务名称:</td>
                                                <TD width="30%" >
                                                    <input type="text" name='taskName' id="taskName" style="width:25%"/>
                                                </TD>
                                                <td align="right" width="15%">

                                                </td>
                                                <td align="left" width="30%">

                                                </td>
                                            </tr>
                                            <!-- add end -->
                                            <tr bgcolor="#FFFFFF">
                                                <TD align="right" width="15%">
                                                    模式：
                                                </TD>
                                                <TD width="85%" colspan="3">
                                                    <select name="mode" id="mode" class=bk onchange="change_mode()">
                                                        <option value="1" >被动模式</option>
                                                        <option value="2" selected>主动模式</option>
                                                        <!-- <option value="26">STB业务</option> -->
                                                    </select>
                                                </TD>
                                            </tr>
                                            <tr bgcolor="#FFFFFF"  id="softUp" style="display:none">

                                                <TD align="right" width="15%">
                                                    升级策略方式：
                                                </TD>
                                                <TD width="30%">
                                                    <s:property value="softStrategyHTML" escapeHtml="false"/>
                                                </TD>

                                                <td align="right" width="15%">
                                                    软件升级目标版本：
                                                </td>
                                                <td align="left" width="30%">
                                                    根据设备自动匹配
                                                </td>
                                            </tr>
                                            <tr bgcolor="#FFFFFF" id="date">
												<td align="center" width="15%">
													触发开始日期：
												</td>
												<td align="left" width="30%">
													<input type="text"   name="expire_date_start" value="<s:property value='expire_date_start'/>" class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.mainForm.expire_date_start,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>

												<td align="center" width="15%">
													触发结束日期：
												</td>
												<td align="left" width="30%">
													<input type="text"  name="expire_date_end" value="<s:property value='expire_date_end'/>" class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.mainForm.expire_date_end,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>
                                            </tr>
                                            <tr bgcolor="#FFFFFF" id="time">
												<td align="center" width="15%">
													触发开始时间：
												</td>
												<td align="left" width="30%">
													<input type="text"   name="expire_time_start" value="<s:property value='expire_time_start'/>" class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.mainForm.expire_time_start,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>

												<td align="center" width="15%">
													触发结束时间：
												</td>
												<td align="left" width="30%">
													<input type="text"  name="expire_time_end" value="<s:property value='expire_time_end'/>" class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.mainForm.expire_time_end,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>
                                            </tr>
                                            <TR>
                                                <TD colspan="4" align="right" CLASS="green_foot">
                                                    <INPUT TYPE="submit" value=" 执 行 " class=btn>
                                                </TD>
                                            </TR>
                                        </TABLE>
                                    </TD>
                                </TR>
                            </TABLE>
                        </FORM>
                    </td>
                </tr>
            </table>
        </TD>
    </TR>
    <TR>
        <TD HEIGHT=20>
            &nbsp;
            <IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
            <IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
            <IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
        </TD>
    </TR>
</TABLE>
<%@ include file="../foot.jsp" %>
