<%--
山西联通机顶盒手工工单
Author: lsr
Version: 1.0.0
Date: 2020-01-10
--%>

<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<html>
<HEAD>
    <title>终端业务手工工单下发</title>
    <link href="../../css/css_green.css" rel="stylesheet" type="text/css">
    <link href="../../css/easyform.css" rel="stylesheet" type="text/css">
    <link href="../../css/listview.css" rel="stylesheet" type="text/css">
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery-1.11.3.min.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.serializeExtend-1.0.1.js"></SCRIPT>

    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

    <SCRIPT LANGUAGE="JavaScript">
      var loginCityId = "-1";
      String.prototype.replaceAll = function (oldStr, newStr) {
        return this.replace(new RegExp(oldStr, "gm"), newStr);
      }

      // IE 兼容assign方法
      if (typeof Object.assign != 'function') {
        Object.assign = function (target) {
          'use strict';
          if (target == null) {
            throw new TypeError('Cannot convert undefined or null to object');
          }

          target = Object(target);
          for (var index = 1; index < arguments.length; index++) {
            var source = arguments[index];
            if (source != null) {
              for (var key in source) {
                if (Object.prototype.hasOwnProperty.call(source, key)) {
                  target[key] = source[key];
                }
              }
            }
          }
          return target;
        };
      }

      function reset () {
        $("input[name='loid']").val("");
        cleanValue();
      }

      function cleanValue () {
        setTime();
        $("select[name='cityId']").val(loginCityId);
        $("input[name='linkman']").val("");
        $("input[name='linkPhone']").val("");
        $("input[name='email']").val("");
        $("input[name='mobile']").val("");
        $("input[name='linkAddress']").val("");
        $("input[name='linkmanCredno']").val("");
        $("select[name='deviceType']").val("2");

        $("input[name='stbUserID']").val("");
        $("input[name='stbUserPwd']").val("");

      }

      $(function () {
        var gw_type = '<s:property value="gw_type" />';
        var loid = '<s:property value="loid" />';
        loginCityId = '<s:property value="cityId" />';
        $("#cityId option[value='00']").prop("selected", true);
        //家庭网关
        if (gw_type == '1') {
          $("select[name='userType']").val('1');
          $("input[name='cmdId']").val('FROMWEB-0000002');
          $("#stbtab").show();
        }
        if (gw_type == '2')//企业网关
        {
          $("select[name='userType']").val('2');
        }
        setTime();
      });

      function setTime () {
        var dstr = "";
        var d = new Date();

        dstr += d.getFullYear();
        if (d.getMonth() + 1 < 10) {
          dstr += '0';
          dstr += d.getMonth() + 1;
        }
        else {
          dstr += d.getMonth() + 1;
        }

        if (d.getDate() < 10) {
          dstr += '0';
          dstr += d.getDate();
        }
        else {
          dstr += d.getDate();
        }

        if (d.getHours() < 10) {
          dstr += '0';
          dstr += d.getHours();
        }
        else {
          dstr += d.getHours();
        }

        if (d.getMinutes() < 10) {
          dstr += '0';
          dstr += d.getMinutes();
        }
        else {
          dstr += d.getMinutes();
        }

        dstr += "00";
        $("input[name='dealDate']").val(dstr);
      }

      //控制备用dns是否显示
      function showStandDns (wanType, strdns) {
        if ("3" == wanType) {
          $("tr[id='" + strdns + "']").css("display", "block");
        }
        else {
          $("tr[id='" + strdns + "']").css("display", "none");
        }
      }

      // reg_verify - 完全用正则表达式来判断一个字符串是否是合法的IP地址，
      function reg_verify (addr) {
        //正则表达式
        var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

        if (addr.match(reg)) {
          //IP地址正确
          return true;
        } else {
          //IP地址校验失败
          return false;
        }
      }

      //检查LOID是否存在
      function checkLoid () {
        if ("-1" == $("select[name='userType']").val()) {
          alert("请选择用户类型。");
          return false;
        }
        if ("" == $("input[name='loid']").val().trim()) {
          alert("唯一标识不可为空！");
          $("input[name='loid']").focus();
          return false;
        }

        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!checkStbLoid.action'/>";
        $.post(url, {
          "userType": $("input[name='userType']").val(),
          "loid": $("input[name='loid']").val().trim()
        }, function (ajax) {
          if ("000" == ajax) {
            alert("唯一标识可以使用。");
          }
          else {
            var context = $.parseJSON(ajax);
            var source = $("#net-info-template").html();
            // 赋值给资料用户 属地，设备类型
            var userInfo = context;
            $("#cityId option[value='" + userInfo.cityId + "']").prop("selected", true);
            $("select[name='deviceType']").val(userInfo.deviceType);
            /*var obj = {
              'cityId':userInfo.cityId,
              'deviceType':userInfo.deviceType
            };*/
            // $('#jirRuBssSheet').fillData(obj);
            // 赋值给机顶盒业务
            $('#stbSheet').fillData(context);
          }
        });
      }

      //业务销户提交
      function delBusiness () {
        if ("" == $("input[name='loid']").val().trim()) {
          alert("唯一标识不可为空。");
          $("input[name='loid']").focus();
          return false;
        }
        if ("-1" == $("select[name='cityId']").val()) {
          alert("属地不可为空。");
          $("input[name='cityId']").focus();
          return false;
        }
        if ("-1" == $("select[name='accessStyle']").val()) {
          alert("终端接入类型不可为空。");
          $("input[name='loid']").focus();
          return false;
        }
        /* if("-1" == $("select[name='deviceType']").val())
        {
            alert("请选择终端类型。");
            $("select[name='deviceType']").focus();
            return false;
        } */

        var stbUserID = "";
        var stbUserPwd = "";
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData();
        // 获取机顶盒用户信息
        var stbInfoObj = $('#stbSheet').getJsonData();
        stbUserID = stbInfoObj.stbUserID;
        stbUserPwd = stbInfoObj.stbUserPwd;

        if (stbUserID == null || stbUserID == '') {
          alert("机顶盒账号值不能为空。");
          return false;
        }
        if (stbUserPwd == null || stbUserPwd == '') {
          alert("机顶盒密码值不能为空。");
          return false;
        }

        var r = confirm("确定要删除机顶盒业务吗 ？");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj, stbInfoObj);
        serverInfoObj.stbServ = '25';
        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!delStbBusiness.action'/>";
        $.post(url, serverInfoObj, function (ajax) {
          alert(ajax);
          $("button[name='delBtn']").attr("disabled", true);
          reset();
          // checkLoid();
        });
        //灰化按钮
        $("button[name='delBtn']").attr("disabled", true);
      }

      //开户业务提交
      function doBusiness () {
        if ("" == $("input[name='loid']").val().trim()) {
          alert("唯一标识不可为空。");
          $("input[name='loid']").focus();
          return false;
        }
        if ("-1" == $("select[name='cityId']").val()) {
          alert("属地不可为空。");
          $("input[name='cityId']").focus();
          return false;
        }
        if ("-1" == $("select[name='accessStyle']").val()) {
          alert("终端接入类型不可为空。");
          $("input[name='loid']").focus();
          return false;
        }
        /* if("-1" == $("select[name='deviceType']").val())
        {
            alert("请选择终端类型。");
            $("select[name='deviceType']").focus();
            return false;
        } */

        var stbUserID = "";
        var stbUserPwd = "";
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData();
        // 获取机顶盒用户信息
        var stbInfoObj = $('#stbSheet').getJsonData();
        stbUserID = stbInfoObj.stbUserID;
        stbUserPwd = stbInfoObj.stbUserPwd;

        if (stbUserID == null || stbUserID == '') {
          alert("机顶盒账号值不能为空。");
          return false;
        }
        if (stbUserPwd == null || stbUserPwd == '') {
          alert("机顶盒密码值不能为空。");
          return false;
        }

        var r = confirm("确定要提交机顶盒业务吗 ？");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj, stbInfoObj);
        serverInfoObj.stbServ = '25';
        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!stbDoBusiness.action'/>";
        $.post(url, serverInfoObj, function (ajax) {
          alert(ajax);
          //灰化按钮
          $("button[name='subBtn']").attr("disabled", false);
          checkLoid();
        });
        //灰化按钮
        $("button[name='subBtn']").attr("disabled", true);
      }

    </script>
</HEAD>
<body>
<FORM NAME="frm" action="" method="post">
    <TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
        <TR>
            <TD HEIGHT="20">&nbsp;</TD>
        </TR>
        <TR>
            <TD>
                <TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
                    <TR>
                        <TD colspan="4">
                            <table width="100%" height="30" border="0" cellspacing="0" cellpadding="0"
                                   class="green_gargtd">
                                <tr>
                                    <td width="162">
                                        <div align="center" class="title_bigwhite">手工工单</div>
                                    </td>
                                    <td><img src="../../images/attention_2.gif" width="15" height="12"></td>
                                </tr>
                            </table>
                        </TD>
                    </TR>

                    <TR>
                        <TD colspan="4" bgcolor="#999999">
                            <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                                <tr align="left">

                                    <td colspan="4" class="green_title_left">
                                        用户资料工单
                                    </td>
                                </tr>
                                <tbody id="jirRuBssSheet">
                                <TR bgcolor="#FFFFFF">
                                    <input type="hidden" id="userServTypeId" name="userServTypeId" value="20">
                                    <input type="hidden" id="userOperateId" name="userOperateId" value="1">
                                    <input type="hidden" name="cmdId" value="FROMWEB-0000001">
                                    <input type="hidden" name="authUser" value="itms">
                                    <input type="hidden" name="authPwd" value="123">
                                    <input type="hidden" name="userType" class=bk value="1">
                                    <TD width="15%" class=column align="right">联系人:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkman" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">受理时间：</TD>
                                    <TD width="35%">
                                        <input type="text" name="dealDate" class=bk value="">
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">唯一标识:</TD>
                                    <TD width="35%">
                                        <input type="text" id="loid" name="loid" class=bk value="">&nbsp;

                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <button type="button" name="subButton" onclick="checkLoid()">检测唯一标识是否存在</button>
                                        <!-- onblur="checkLoid()" -->
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">属地:</TD>
                                    <TD width="35%">
                                        <s:select list="cityList" name="cityId"
                                                  headerKey="-1" headerValue="请选择属地" listKey="city_id"
                                                  listValue="city_name" value="cityId" cssClass="bk"></s:select>
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">联系电话:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkPhone" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">电子邮箱:</TD>
                                    <TD width="35%">
                                        <input type="text" name="email" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">手机:</TD>
                                    <TD width="35%">
                                        <input type="text" name="mobile" class=bk value="" maxlength="15">
                                    </TD>
                                    <TD width="15%" class=column align="right">地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkAddress" size="55" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">证件号码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkmanCredno" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">终端类型:</TD>
                                    <TD width="35%">
                                        <SELECT name="deviceType" class="bk">
                                            <OPTION value="1">e8-b</OPTION>
                                            <OPTION value="2" selected='true'>e8-c</OPTION>
                                        </SELECT>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                </tbody>
                            </TABLE>
                        </TD>

                    </TR>
                    <tr>
                        <TD colspan="4" bgcolor="#999999">
                            <TABLE id="info-list" border=0 cellspacing=1 cellpadding=2 width="100%">
                                <tr id="stbtab" align="left">
                                    <td colspan="4" class="green_title_left">
                                        <input type="hidden" class="netOperateId" name="netOperateId" value="1"/>
                                        机顶盒业务工单
                                    </td>
                                </tr>
                                <tbody id="stbSheet">
                                <!-- <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">OltFactory:</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="netOltFactory" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR> -->
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">机顶盒帐号:</TD>
                                    <TD width="35%">
                                        <input type="text" name="stbUserID" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">机顶盒密码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="stbUserPwd" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                </tbody>
                                <TR align="left" id="doBiz">
                                    <TD colspan="4" class=foot align="right" nowrap>
                                        <button type="button" name="subBtn" onclick="doBusiness()">开&nbsp;&nbsp;户
                                        </button>
                                        <button type="button" name="subBtn" onclick="delBusiness()">销&nbsp;&nbsp;户
                                        </button>
                                        <button type="button" name="subBtn" onclick="reset()">重&nbsp;&nbsp;置</button>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </tr>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
</FORM>
</body>
</html>
<%@include file="../../../foot.jsp" %>