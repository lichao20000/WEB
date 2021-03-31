<%--
ɽ����ͨ�������ֹ�����
Author: lsr
Version: 1.0.0
Date: 2020-01-10
--%>

<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<html>
<HEAD>
    <title>�ն�ҵ���ֹ������·�</title>
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

      // IE ����assign����
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
        //��ͥ����
        if (gw_type == '1') {
          $("select[name='userType']").val('1');
          $("input[name='cmdId']").val('FROMWEB-0000002');
          $("#stbtab").show();
        }
        if (gw_type == '2')//��ҵ����
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

      //���Ʊ���dns�Ƿ���ʾ
      function showStandDns (wanType, strdns) {
        if ("3" == wanType) {
          $("tr[id='" + strdns + "']").css("display", "block");
        }
        else {
          $("tr[id='" + strdns + "']").css("display", "none");
        }
      }

      // reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
      function reg_verify (addr) {
        //������ʽ
        var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

        if (addr.match(reg)) {
          //IP��ַ��ȷ
          return true;
        } else {
          //IP��ַУ��ʧ��
          return false;
        }
      }

      //���LOID�Ƿ����
      function checkLoid () {
        if ("-1" == $("select[name='userType']").val()) {
          alert("��ѡ���û����͡�");
          return false;
        }
        if ("" == $("input[name='loid']").val().trim()) {
          alert("Ψһ��ʶ����Ϊ�գ�");
          $("input[name='loid']").focus();
          return false;
        }

        //ҵ�񴥷�
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!checkStbLoid.action'/>";
        $.post(url, {
          "userType": $("input[name='userType']").val(),
          "loid": $("input[name='loid']").val().trim()
        }, function (ajax) {
          if ("000" == ajax) {
            alert("Ψһ��ʶ����ʹ�á�");
          }
          else {
            var context = $.parseJSON(ajax);
            var source = $("#net-info-template").html();
            // ��ֵ�������û� ���أ��豸����
            var userInfo = context;
            $("#cityId option[value='" + userInfo.cityId + "']").prop("selected", true);
            $("select[name='deviceType']").val(userInfo.deviceType);
            /*var obj = {
              'cityId':userInfo.cityId,
              'deviceType':userInfo.deviceType
            };*/
            // $('#jirRuBssSheet').fillData(obj);
            // ��ֵ��������ҵ��
            $('#stbSheet').fillData(context);
          }
        });
      }

      //ҵ�������ύ
      function delBusiness () {
        if ("" == $("input[name='loid']").val().trim()) {
          alert("Ψһ��ʶ����Ϊ�ա�");
          $("input[name='loid']").focus();
          return false;
        }
        if ("-1" == $("select[name='cityId']").val()) {
          alert("���ز���Ϊ�ա�");
          $("input[name='cityId']").focus();
          return false;
        }
        if ("-1" == $("select[name='accessStyle']").val()) {
          alert("�ն˽������Ͳ���Ϊ�ա�");
          $("input[name='loid']").focus();
          return false;
        }
        /* if("-1" == $("select[name='deviceType']").val())
        {
            alert("��ѡ���ն����͡�");
            $("select[name='deviceType']").focus();
            return false;
        } */

        var stbUserID = "";
        var stbUserPwd = "";
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData();
        // ��ȡ�������û���Ϣ
        var stbInfoObj = $('#stbSheet').getJsonData();
        stbUserID = stbInfoObj.stbUserID;
        stbUserPwd = stbInfoObj.stbUserPwd;

        if (stbUserID == null || stbUserID == '') {
          alert("�������˺�ֵ����Ϊ�ա�");
          return false;
        }
        if (stbUserPwd == null || stbUserPwd == '') {
          alert("����������ֵ����Ϊ�ա�");
          return false;
        }

        var r = confirm("ȷ��Ҫɾ��������ҵ���� ��");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj, stbInfoObj);
        serverInfoObj.stbServ = '25';
        //ҵ�񴥷�
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!delStbBusiness.action'/>";
        $.post(url, serverInfoObj, function (ajax) {
          alert(ajax);
          $("button[name='delBtn']").attr("disabled", true);
          reset();
          // checkLoid();
        });
        //�һ���ť
        $("button[name='delBtn']").attr("disabled", true);
      }

      //����ҵ���ύ
      function doBusiness () {
        if ("" == $("input[name='loid']").val().trim()) {
          alert("Ψһ��ʶ����Ϊ�ա�");
          $("input[name='loid']").focus();
          return false;
        }
        if ("-1" == $("select[name='cityId']").val()) {
          alert("���ز���Ϊ�ա�");
          $("input[name='cityId']").focus();
          return false;
        }
        if ("-1" == $("select[name='accessStyle']").val()) {
          alert("�ն˽������Ͳ���Ϊ�ա�");
          $("input[name='loid']").focus();
          return false;
        }
        /* if("-1" == $("select[name='deviceType']").val())
        {
            alert("��ѡ���ն����͡�");
            $("select[name='deviceType']").focus();
            return false;
        } */

        var stbUserID = "";
        var stbUserPwd = "";
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData();
        // ��ȡ�������û���Ϣ
        var stbInfoObj = $('#stbSheet').getJsonData();
        stbUserID = stbInfoObj.stbUserID;
        stbUserPwd = stbInfoObj.stbUserPwd;

        if (stbUserID == null || stbUserID == '') {
          alert("�������˺�ֵ����Ϊ�ա�");
          return false;
        }
        if (stbUserPwd == null || stbUserPwd == '') {
          alert("����������ֵ����Ϊ�ա�");
          return false;
        }

        var r = confirm("ȷ��Ҫ�ύ������ҵ���� ��");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj, stbInfoObj);
        serverInfoObj.stbServ = '25';
        //ҵ�񴥷�
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!stbDoBusiness.action'/>";
        $.post(url, serverInfoObj, function (ajax) {
          alert(ajax);
          //�һ���ť
          $("button[name='subBtn']").attr("disabled", false);
          checkLoid();
        });
        //�һ���ť
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
                                        <div align="center" class="title_bigwhite">�ֹ�����</div>
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
                                        �û����Ϲ���
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
                                    <TD width="15%" class=column align="right">��ϵ��:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkman" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
                                    <TD width="35%">
                                        <input type="text" name="dealDate" class=bk value="">
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">Ψһ��ʶ:</TD>
                                    <TD width="35%">
                                        <input type="text" id="loid" name="loid" class=bk value="">&nbsp;

                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <button type="button" name="subButton" onclick="checkLoid()">���Ψһ��ʶ�Ƿ����</button>
                                        <!-- onblur="checkLoid()" -->
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">����:</TD>
                                    <TD width="35%">
                                        <s:select list="cityList" name="cityId"
                                                  headerKey="-1" headerValue="��ѡ������" listKey="city_id"
                                                  listValue="city_name" value="cityId" cssClass="bk"></s:select>
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkPhone" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">��������:</TD>
                                    <TD width="35%">
                                        <input type="text" name="email" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
                                    <TD width="35%">
                                        <input type="text" name="mobile" class=bk value="" maxlength="15">
                                    </TD>
                                    <TD width="15%" class=column align="right">��ַ:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkAddress" size="55" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">֤������:</TD>
                                    <TD width="35%">
                                        <input type="text" name="linkmanCredno" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">�ն�����:</TD>
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
                                        ������ҵ�񹤵�
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
                                    <TD class=column align="right" nowrap width="15%">�������ʺ�:</TD>
                                    <TD width="35%">
                                        <input type="text" name="stbUserID" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">����������:</TD>
                                    <TD width="35%">
                                        <input type="text" name="stbUserPwd" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                </tbody>
                                <TR align="left" id="doBiz">
                                    <TD colspan="4" class=foot align="right" nowrap>
                                        <button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��
                                        </button>
                                        <button type="button" name="subBtn" onclick="delBusiness()">��&nbsp;&nbsp;��
                                        </button>
                                        <button type="button" name="subBtn" onclick="reset()">��&nbsp;&nbsp;��</button>
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