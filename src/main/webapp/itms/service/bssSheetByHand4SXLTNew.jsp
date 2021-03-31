<%--
ɽ����ͨ�ֹ�����
Author: lsr
Version: 1.0.0
Date: 2019-12-16
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
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/handlebars.min-v4.5.3.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.serializeExtend-1.0.1.js"></SCRIPT>

    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

    <script id="net-info-template" type="text/x-handlebars-template">
        {{#each netInfoList}}
        <tr id="nettab-{{@key}}" align="left">
            <td colspan="4" class="green_title_left">
                <input type="hidden" class="netOperateId" name="netOperateId" value="1"/>
                <input type="checkbox" name="netServTypeId" value="22" id="netServTypeId-{{@key}}" data-id ="{{@key}}"
                       onclick="showSheet('netServTypeId-{{@key}}','internetBssSheet-{{@key}}','net');"/>
                ���ҵ�񹤵�
            </td>
        </tr>
        <tbody id="internetBssSheet-{{@key}}" style="display:none" >
        <TR bgcolor="#FFFFFF" >
            <TD width="15%" class=column align="right">OltFactory:</TD>
            <TD width="35%" colspan="3">
                <input type="text" name="netOltFactory" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
				<input type="hidden" name="oldNetOltFactory">
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
            <TD width="35%">
                <input type="text" name="netUsername" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
				<input type="hidden" name="oldNetUsername">
            </TD>
            <TD width="15%" class=column align="right">�������:</TD>
            <TD width="35%">
                <input type="text" name="netPasswd" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">VLANID:</TD>
            <TD width="35%">
                <input type="text" name="netVlanId" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
				<input type="hidden" name="oldNetVlanId">
            </TD>
            <TD width="15%" class=column align="right">������ʽ��</TD>
            <TD width="35%">
                <SELECT name="netWanType" class="objnetWanType" class="bk" onchange="change_select()">
                    <option value>==��ѡ���������==</option>
                    <option value="1">==�Ž�==</option>
                    <option value="2">==·��==</option>
                    <option value="3">==ȫ·��==</option>
                </SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD width="15%" class=column align="right">�󶨶˿�:</TD>
            <TD class="netTD1" width="35%" colspan="3">
                <input type="checkbox" name="netPort" value="L1" class=bk/>L1&nbsp;
                <input type="checkbox" name="netPort" value="L2" class=bk/>L2&nbsp;
                <input type="checkbox" name="netPort" value="L3" class=bk/>L3&nbsp;
                <input type="checkbox" name="netPort" value="L4" class=bk/>L4&nbsp;
                &nbsp;<font color="#FF0000">*</font>
				<input type="hidden" name="oldNetPort">
            </TD>
        </TR>
        </tbody>
        {{/each}}
        {{#each iptvInfoList}}
        <tr id="iptvtab-{{@key}}" align="left" >
            <td colspan="4" class="green_title_left">
                <input type="hidden" id="iptvOperateId-{{@key}}" name="iptvOperateId" value="1"/>
                <input type="checkbox" name="iptvServTypeId" value="21" id="iptvServTypeId-{{@key}}" data-id ="{{@key}}"
                       onclick="showSheet('iptvServTypeId-{{@key}}','iptvBssSheet-{{@key}}','iptv');"/>
                IPTVҵ�񹤵�
            </td>
        </tr>
        <tbody id="iptvBssSheet-{{@key}}" style="display:none" >
        <TR bgcolor="#FFFFFF">
            <TD width="15%" class=column align="right">������ʽ��</TD>
            <TD width="35%" >
                <SELECT name="iptvWanType" id="objiptvWanType" class="bk">
                    <option value="1">==�Ž�==</option>
                </SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">OltFactory:</TD>
            <TD width="35%" colspan="3">
                <input type="text" name="iptvOltFactory" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">VLANID:</TD>
            <TD width="35%">
                <input type="text" name="iptvVlanId" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">��ͨ�˿�:</TD>
            <TD id="iptvTD1" width="35%">
                <input type="checkbox" name="iptvPort" value="L1" class=bk/>L1&nbsp;
                <input type="checkbox" name="iptvPort" value="L2" class=bk/>L2&nbsp;
                <input type="checkbox" name="iptvPort" value="L3" class=bk/>L3&nbsp;
                <input type="checkbox" name="iptvPort" value="L4" class=bk/>L4&nbsp;
                &nbsp;<font color="#FF0000">*
            </font>
            </TD>
        </TR>
        </tbody>
        {{/each}}
        {{#each h248VoipInfoList}}
        <tr id="h248tab-{{@key}}" align="left" >
            <td colspan="4" class="green_title_left">
                <input type="hidden" id="hvoipOperateId-{{@key}}" name="hvoipOperateId" value="1"/>
                <input type="hidden" name="isH248SendPost" id="isH248SendPost-{{@key}}" value="0">
                <input type="checkbox" name="hvoipServTypeId" value="15" data-id ="{{@key}}"
                       id="hvoipServTypeId-{{@key}}"
                       onclick="showSheet('hvoipServTypeId-{{@key}}','H248BssSheet-{{@key}}','h248');"/>
                H248����ҵ�񹤵�
            </td>
        </tr>
        <tbody id="H248BssSheet-{{@key}}" style = "display:none">
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">�����˿�:</TD>
            <TD width="35%">
                <input type="checkbox" name="hvoipPort" value="A1" class=bk/>A1&nbsp;
                <input type="checkbox" name="hvoipPort" value="A2" class=bk/>A2&nbsp;
                <input type="checkbox" name="hvoipPort" value="A3" class=bk/>A3&nbsp;
                <input type="checkbox" name="hvoipPort" value="A4" class=bk/>A4&nbsp;
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD class=column align="right">EID:</TD>
            <TD width="35%">
                <input type="text" name="hvoipEid" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
            <TD width="35%">
                <input type="text" name="hvoipMgcIp" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">����MGC�˿�:</TD>
            <TD width="35%">
                <input type="text" name="hvoipMgcPort" class=bk value="2944">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
            <TD width="35%">
                <input type="text" name="hvoipStandMgcIp" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">����MGC�˿�:</TD>
            <TD width="35%">
                <input type="text" name="hvoipStandMgcPort" class=bk value="2944">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR id="h248IP" bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
            <TD width="35%">
                <input type="text" name="hvoipIpaddress" class=bk value="">

            </TD>
            <TD width="15%" class=column align="right">����:</TD>
            <TD width="35%">
                <input type="text" name="hvoipIpmask" class=bk value="">

            </TD>
        </TR>
        <TR id="h248Dns" bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����:</TD>
            <TD width="35%" colspan="3">
                <input type="text" name="hvoipGateway" class=bk value="">

            </TD>
        </TR>
        </tbody>
        {{/each}}
        {{#each sipVoipInfoList}}
        <tr id="siptab-{{@key}}" align="left">
            <td colspan="4" class="green_title_left">
                <input type="hidden" id="sipOperateId-{{@key}}" name="sipOperateId" value="1"/>
                <input type="hidden" name="isSipSendPost" id="isSipSendPost-{{@key}}" value="0">
                <input type="checkbox" name="sipServTypeId" value="14" id="sipServTypeId-{{@key}}" data-id ="{{@key}}"
                       onclick="showSheet('sipServTypeId-{{@key}}','sipBssSheet-{{@key}}','sip');"/>
                SIP����ҵ�񹤵�
            </td>
        </tr>
        <tbody id="sipBssSheet-{{@key}}" style = "display:none">
        <tr bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
            <TD width="35%">
                <input type="text" name="sipVoipPhone" class=bk value="">&nbsp;
                <font color="#FF0000">*</font>
            </TD>
            <TD class=column align="right" nowrap width="15%">�����˿�:</TD>
            <TD width="35%">
                <input type="checkbox" name="sipVoipPort" value="V1" class=bk/>V1&nbsp;
                <input type="checkbox" name="sipVoipPort" value="V2" class=bk/>V2&nbsp;
                <input type="checkbox" name="sipVoipPort" value="V3" class=bk/>V3&nbsp;
                <input type="checkbox" name="sipVoipPort" value="V4" class=bk/>V4&nbsp;
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </tr>
        <TR bgcolor="#FFFFFF">
            <TD width="15%" class=column align="right">��֤�˺�:</TD>
            <TD width="35%">
                <input type="text" name="sipVoipUsername" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">��֤����</TD>
            <TD width="35%">
                <input type="text" name="sipVoipPwd" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">���ô��������:</TD>
            <TD width="35%">
                <input type="text" name="sipProxServ" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">���ô���������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipProxPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">���ô����������ַ:</TD>
            <TD width="35%">
                <input type="text" name="sipStandProxServ" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">���ô���������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipStandProxPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����ע�������:</TD>
            <TD width="35%">
                <input type="text" name="sipRegiServ" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">����ע��������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipRegiPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����ע���������ַ:</TD>
            <TD width="35%">
                <input type="text" name="sipStandRegiServ" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">����ע��������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipStandRegiPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>

        <TR bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap>Э������</TD>
            <TD colspan = "3">
                <select name="sipProtocol" class="bk" onChange="changeIms(this.value)">
                    <option value="-1">==��ѡ��Э������==</option>
                    <option value="0">==IMS SIP==</option>
                    <option value="1">==���� SIP==</option>
                </select>
                &nbsp;&nbsp;<font color="#FF0000">* </font>
            </TD>
        </TR>
        <TR id="sipIP" bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
            <TD width="35%">
                <input type="text" name="sipIpaddress" class=bk value="">
            </TD>
            <TD width="15%" class=column align="right">����:</TD>
            <TD width="35%">
                <input type="text" name="sipIpmask" class=bk value="">
            </TD>
        </TR>
        <TR id="sipDns" bgcolor="#FFFFFF">
            <TD class=column align="right" nowrap width="15%">����:</TD>
            <TD width="35%" colspan="3">
                <input type="text" name="sipGateway" class=bk value="">
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF" id="ims" style="display: none">
            <TD class=column align="right" nowrap>URI</TD>
            <TD colspan="3"><INPUT TYPE="text" NAME="sipVoipUri" maxlength=20
                                   class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
        </TR>
        <TR bgcolor="#FFFFFF" id="ims2" style="display: none">
            <TD class=column align="right" nowrap width="15%">���ð󶨷�����:</TD>
            <TD width="35%">
                <input type="text" name="sipOutBoundProxy" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">���ð󶨷������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipOutBoundPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        <TR bgcolor="#FFFFFF" id="ims3" style="display: none">
            <TD class=column align="right" nowrap width="15%">���ð󶨷�������ַ:</TD>
            <TD width="35%">
                <input type="text" name="sipStandOutBoundProxy" class=bk value="">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
            <TD width="15%" class=column align="right">���ð󶨷������˿�:</TD>
            <TD width="35%">
                <input type="text" name="sipStandOutBoundPort" class=bk value="5060">
                &nbsp;<font color="#FF0000">*</font>
            </TD>
        </TR>
        </tbody>
        {{/each}}
        <TR align="left" id="doBiz">
            <TD colspan="4" class=foot align="right" nowrap>
                <button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��
                </button>
                <button type="button" name="subBtn" onclick="delBusiness()">��&nbsp;&nbsp;��
                </button>
                <button type="button" name="subBtn" onclick="reset()">��&nbsp;&nbsp;��</button>
            </TD>
        </TR>
    </script>
    <SCRIPT LANGUAGE="JavaScript">
      var loginCityId = "-1";
      String.prototype.replaceAll = function (oldStr, newStr) {
        return this.replace(new RegExp(oldStr, "gm"), newStr);
      }

      // IE ����assign����
      if (typeof Object.assign != 'function') {
        Object.assign = function(target) {
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

      
      //��Ϊȫ·�� vlanidĬ��Ϊ2
      function change_select(){
    	var netOltFactory=  $("input[name='netOltFactory']").val();
    	var netWanType=  $("select[name='netWanType']").val();
    	if(netOltFactory==null || netOltFactory==''){
    		alert("������дOltFactory��");
    	}
    	if('HW'==netOltFactory.toUpperCase() || 'HUAWEI'==netOltFactory.toUpperCase()){
    		if(netWanType==3){
    			$("input[name='netVlanId']").val("2");
    		}
    	}
	}	

      
      
      
      function cleanValue () {
        setTime();
        $("#internetBssSheet").hide();
        $("#iptvBssSheet").hide();
        $("#H248BssSheet").hide();
        $("#sipBssSheet").hide();

        $("select[name='cityId']").val(loginCityId);
        $("input[name='linkman']").val("");
        $("input[name='linkPhone']").val("");
        $("input[name='email']").val("");
        $("input[name='mobile']").val("");
        $("input[name='linkAddress']").val("");
        $("input[name='linkmanCredno']").val("");
        $("select[name='deviceType']").val("2");

        $("input[name='netOltFactory']").val("");
        $("input[name='netServTypeId']").attr("checked", false);
        $("select[name='netUsername']").val("-1");
        $("input[name='netPasswd']").val("");
        $("input[name='netVlanId']").val("");
        $("input[name='netSpeed']").val("");
        var checkBoxNet = document.frm.netPort;
        for (var i = 0; i < checkBoxNet.length; i++) {
          checkBoxNet[i].checked = false;
        }
        //$("input[name='netPort']").val("");
        $("select[name='netWanType']").val("-1");

        $("input[name='iptvOltFactory']").val("");
        $("input[name='iptvServTypeId']").attr("checked", false);
        $("select[name='iptvWanType']").val("-1");
        $("input[name='iptvVlanId']").val("");
        $("input[name='multicastVlan']").val("");
        $("input[name='iptvUserName']").val("");
        $("input[name='iptvPasswd']").val("");
        $("input[name='iptvDestIp']").val("10.0.0.0");
        $("input[name='iptvDestMark']").val("255.0.0.0");
        var checkBoxObj = document.frm.iptvPort;
        for (var i = 0; i < checkBoxObj.length; i++) {
          checkBoxObj[i].checked = false;
        }
        //$("input[name='iptvPort']").val("");

        $("input[name='hvoipOltFactory']").val("");
        $("input[name='hvoipServTypeId']").attr("checked", false);
        $("select[name='hvoipRegIdType']").val("-1");
        $("input[name='hvoipRegId']").val("");
        $("input[name='hvoipMgcIp']").val("");
        $("input[name='hvoipMgcPort']").val("2944");
        $("input[name='hvoipStandMgcIp']").val("");
        $("input[name='hvoipStandMgcPort']").val("2944");
        $("input[name='hvoipIpaddress']").val("");
        $("input[name='hvoipIpmask']").val("");
        $("input[name='hvoipGateway']").val("");
        $("input[name='hvoipVlanId']").val("");
        $("input[name='hvoipEid']").val("");
        $("input[name='hvoipPhone']").val("");

        $("select[name='sipVoipPort']").val("-1");
        $("input[name='sipVoipPhone']").val("");
        $("input[name='sipVoipUsername']").val("");
        $("input[name='sipVoipPwd']").val("");
        $("input[name='sipProxServ']").val("");
        $("input[name='sipProxPort']").val("5060");
        $("input[name='sipStandProxServ']").val("");
        $("input[name='sipStandProxPort']").val("5060");
        $("input[name='sipRegiServ']").val("");
        $("input[name='sipRegiPort']").val("5060");
        $("input[name='sipStandRegiServ']").val("");
        $("input[name='sipStandRegiPort']").val("5060");
        $("select[name='sipProtocol']").val("-1");
        $("input[name='sipIpaddress']").val("");
        $("input[name='sipIpmask']").val("");
        $("input[name='sipGateway']").val("");
        $("input[name='sipVoipUri']").val("");
        $("input[name='sipOutBoundProxy']").val("");
        $("input[name='sipOutBoundPort']").val("5060");
        $("input[name='sipStandOutBoundProxy']").val("");
        $("input[name='sipStandOutBoundPort']").val("5060");

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
          $("#siptab").show();
          $("#nettab").show();
          $("#h248tab").show();
          $("#iptvtab").show();
        }
        if (gw_type == '2')//��ҵ����
        {
          $("select[name='userType']").val('2');
        }
        setTime();
        initForm();
      });

      function initForm () {
        // ��ʼ��ÿ������һ��ֵ
        var source = $("#net-info-template").html();
        var template = Handlebars.compile(source);
        var context = {
          "netInfoList":['1'],
          "iptvInfoList":['1'],
          "h248VoipInfoList":['1'],
          "sipVoipInfoList":['1']

        };
        var html1 = template(context);
        document.getElementById('info-list').innerHTML= html1;
      }
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

      // ���ڶ�̬��ʾ�Ĵ�ҵ�񣨿����IPTV��H248,sip��ÿ��ҵ��ֻ��ѡ��һ��
      function showSheet (objId, id,type) {
        if ($("#" + objId).prop("checked")) {
          if(type == 'net'){
            $("input[name='netServTypeId']").prop("checked",false);
          }
          if(type == 'iptv'){
            $("input[name='iptvServTypeId']").prop("checked",false);
          }
          if(type == 'h248'){
            $("input[name='hvoipServTypeId']").prop("checked",false);
            $("input[name='sipServTypeId']").prop("checked",false);
          }
          if(type == 'sip'){
            $("input[name='sipServTypeId']").prop("checked",false);
            $("input[name='hvoipServTypeId']").prop("checked",false);
          }
          $("#" + objId).prop("checked",true);
          document.getElementById(id).style.display = "";
        } else {
          document.getElementById(id).style.display = "none";
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

      //�����ims sip ��ʾuri ��domain
      function changeIms (protocol) {
        if ("0" == protocol) {
          $("tr[id='ims']").css("display", "table-row");
          $("tr[id='ims2']").css("display", "table-row");
          $("tr[id='ims3']").css("display", "table-row");
        }
        else {
          $("tr[id='ims']").css("display", "none");
          $("tr[id='ims2']").css("display", "none");
          $("tr[id='ims3']").css("display", "none");
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
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!checkLoidNew.action'/>";
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
            var template = Handlebars.compile(source);
            /*var context = {
              "netInfoList":['1','2','3'],
              "iptvInfoList":['1','2','3'],
              "h248VoipInfoList":['1','2','3'],
              "sipVoipInfoList":['1','2','3']

            };*/
            var html1 = template(context);
            document.getElementById('info-list').innerHTML= html1;
            // ��ֵ�������û� ���أ��豸����
            var userInfo = context.userInfoObj;
            $("#cityId option[value='"+userInfo.cityId+"']").prop("selected", true);
            $("select[name='deviceType']").val(userInfo.deviceType);
            /*var obj = {
              'cityId':userInfo.cityId,
              'deviceType':userInfo.deviceType
            };*/
            // $('#jirRuBssSheet').fillData(obj);
            // ��ֵ�����ҵ��
            var netInfoList = context.netInfoList;
            if(netInfoList != null){
              for(i = 0;i<netInfoList.length;i++){
                $('#internetBssSheet-'+i).fillData(netInfoList[i]);
                $('#internetBssSheet-'+i).css('display','');
              }
            }
            // ��ֵ��iptvҵ��
            var iptvInfoList = context.iptvInfoList;
            if(iptvInfoList != null){
              for(i = 0;i<iptvInfoList.length;i++){
                $('#iptvBssSheet-'+i).fillData(iptvInfoList[i]);
                $('#iptvBssSheet-'+i).css('display','');
              }
            }
            // ��ֵ��sipҵ��
            var sipVoipInfoList = context.sipVoipInfoList;
            if(sipVoipInfoList != null){
              for(i = 0;i<sipVoipInfoList.length;i++){
                $('#sipBssSheet-'+i).fillData(sipVoipInfoList[i]);
                $('#sipBssSheet-'+i).css('display','');
              }
            }
            // ��ֵ��h248ҵ��
            var h248VoipInfoList = context.h248VoipInfoList;
            if(h248VoipInfoList != null){
              for(i = 0;i<h248VoipInfoList.length;i++){
                $('#H248BssSheet-'+i).fillData(h248VoipInfoList[i]);
                $('#H248BssSheet-'+i).css('display','');

              }
            }
          }
        });
      }

      //ҵ�������ύ
      function delBusiness () {
        var netPortStr = "";
        var iptvPortStr = "";
        var netServTypeId = "";
        var hvoipServTypeId ="";
        var sipServTypeId = "";
        var iptvServTypeId = "";
        var cmdId = $("input[name='cmdId']").val();
        var netServTypeIdArrays = new Array();
        var hvoipServTypeIdArrays = new Array();
        var sipServTypeIdArrays = new Array();
        var iptvServTypeIdArrays = new Array();
        $('input[name="netServTypeId"]:checked').each(function(){
          netServTypeIdArrays.push($(this).attr("data-id"));
          netServTypeId = $(this).val();
        });
        $('input[name="hvoipServTypeId"]:checked').each(function(){
          hvoipServTypeIdArrays.push($(this).attr("data-id"));
          hvoipServTypeId = $(this).val();
        });
        $('input[name="sipServTypeId"]:checked').each(function(){
          sipServTypeIdArrays.push($(this).attr("data-id"));
          sipServTypeId = $(this).val();
        });
        $('input[name="iptvServTypeId"]:checked').each(function(){
          iptvServTypeIdArrays.push($(this).attr("data-id"));
          iptvServTypeId = $(this).val();
        });
        if((netServTypeIdArrays ==null || netServTypeIdArrays.length <= 0 ) && (hvoipServTypeIdArrays ==null || hvoipServTypeIdArrays.length <= 0) && (sipServTypeIdArrays ==null || sipServTypeIdArrays.length <= 0) && (iptvServTypeIdArrays ==null || iptvServTypeIdArrays.length <= 0)){
          alert("�빴ѡһ��ҵ�����͡�");
          return false;
        }
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData()
        if ("22" != netServTypeId) {
          netServTypeId = "";
        }
        else {
          // ��ȡ����û���Ϣ
          for(i = 0;i<netServTypeIdArrays.length;i++){
            var netInfoObj = $('#internetBssSheet-'+netServTypeIdArrays[i]).getJsonData();
          }
          
          if (netInfoObj.netVlanId==null || "" == netInfoObj.netVlanId) {
              alert("���VlanId����Ϊ�ա�");
              $("input[name='netVlanId']").focus();
              return false;
            }
        }

        if ("21" != iptvServTypeId) {
          iptvServTypeId = "";
        }
        else {
          // ��ȡIPTV�û���Ϣ
          for(i = 0;i<iptvServTypeIdArrays.length;i++){
            var iptvInfoObj = $('#iptvBssSheet-'+iptvServTypeIdArrays[i]).getJsonData();
          }
          var WanType = iptvInfoObj.iptvWanType;
          if ("-1" == WanType) {
            alert("IPTV������ʽ����Ϊ�ա�");
            $("select[name='iptvWanType']").focus();
            return false;
          }
          if ("" == iptvInfoObj.iptvVlanId) {
            alert("iptvVlanId����Ϊ�ա�");
            $("input[name='iptvVlanId']").focus();
            return false;
          }

          //�Ž�
          if ('' == iptvInfoObj.iptvPort) {
            alert("IPTV�󶨶˿ڲ���Ϊ�ա�");
            $("input[name='iptvPort']").focus();
            return false;
          }
        }

        if ("15" != hvoipServTypeId) {
          hvoipServTypeId = "";
        }
        else {
          // ��ȡH248��Ϣ
          for(i = 0;i<hvoipServTypeIdArrays.length;i++){
            var h248InfoObj = $('#H248BssSheet-'+hvoipServTypeIdArrays[i]).getJsonData();

          }
        }
        if ("14" != sipServTypeId) {
          sipServTypeId = "";
        }else{
          // ��ȡsip��Ϣ
          for(i = 0;i<sipServTypeIdArrays.length;i++){
            var sipInfoObj = $('#sipBssSheet-'+sipServTypeIdArrays[i]).getJsonData();
          }
        }

        var alertstr = "ȷ��Ҫɾ��";
        if (netServTypeId != "") {
          alertstr = alertstr + "���";
          if (hvoipServTypeId != "" || iptvServTypeId != ""  || sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (iptvServTypeId != "") {
          alertstr = alertstr + "IPTV";
          if (hvoipServTypeId != "" || sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (hvoipServTypeId != "") {
          alertstr = alertstr + "H248����";
          if (sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (sipServTypeId != "") {
          alertstr = alertstr + "SIP����";
        }
        var r = confirm(alertstr + "ҵ���� ��");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj,netInfoObj, iptvInfoObj,h248InfoObj,sipInfoObj);
        serverInfoObj.netServTypeId = netServTypeId;
        serverInfoObj.hvoipServTypeId = hvoipServTypeId;
        serverInfoObj.sipServTypeId = sipServTypeId;
        serverInfoObj.iptvServTypeId = iptvServTypeId;
        serverInfoObj.cmdId=cmdId;
        //ҵ�񴥷�
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!delBusiness.action'/>";
        $.post(url, serverInfoObj, function (ajax) {
          alert(ajax);
          $("button[name='delBtn']").attr("disabled", true);
          checkLoid();
        });
        //�һ���ť
        $("button[name='delBtn']").attr("disabled", true);
      }

      //����ҵ���ύ
      function doBusiness () {
        var loidvalue = "";
        if ("-1" == $("input[name='userType']").val()) {
          alert("��ѡ���û����͡�");
          return false;
        }

        if ("2" == $("input[name='userType']").val() && "" == $("input[name='customerId']").val()) {
          alert("�ͻ�ID����Ϊ�ա�");
          $("input[name='customerId']").focus();
          return false;
        }

        if ("2" != $("select[name='accessStyle']").val()) {
          if ("" == $("input[name='loid']").val().trim()) {
            alert("Ψһ��ʶ����Ϊ�ա�");
            $("input[name='loid']").focus();
            return false;
          }
          loidvalue = $("input[name='loid']").val();
        }
        else {
          loidvalue = "bbms" + $("input[name='customerId']").val();
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

        var netPortStr = "";
        var iptvPortStr = "";
        var netServTypeId = "";
        var hvoipServTypeId ="";
        var sipServTypeId = "";
        var iptvServTypeId = "";
        var cmdId = $("input[name='cmdId']").val();
        var netServTypeIdArrays = new Array();
        var hvoipServTypeIdArrays = new Array();
        var sipServTypeIdArrays = new Array();
        var iptvServTypeIdArrays = new Array();
        $('input[name="netServTypeId"]:checked').each(function(){
          netServTypeIdArrays.push($(this).attr("data-id"));
          netServTypeId = $(this).val();
        });
        $('input[name="hvoipServTypeId"]:checked').each(function(){
          hvoipServTypeIdArrays.push($(this).attr("data-id"));
          hvoipServTypeId = $(this).val();
        });
        $('input[name="sipServTypeId"]:checked').each(function(){
          sipServTypeIdArrays.push($(this).attr("data-id"));
          sipServTypeId = $(this).val();
        });
        $('input[name="iptvServTypeId"]:checked').each(function(){
          iptvServTypeIdArrays.push($(this).attr("data-id"));
          iptvServTypeId = $(this).val();
        });
        if((netServTypeIdArrays ==null || netServTypeIdArrays.length <= 0 ) && (hvoipServTypeIdArrays ==null || hvoipServTypeIdArrays.length <= 0) && (sipServTypeIdArrays ==null || sipServTypeIdArrays.length <= 0) && (iptvServTypeIdArrays ==null || iptvServTypeIdArrays.length <= 0)){
          alert("�빴ѡһ��ҵ�����͡�");
          return false;
        }
        var serverInfoObj = {};
        var userInfoObj = $('#jirRuBssSheet').getJsonData()
        if ("22" != netServTypeId) {
          netServTypeId = "";
        }
        else {
          // ��ȡ����û���Ϣ
          for(i = 0;i<netServTypeIdArrays.length;i++){
            var netInfoObj = $('#internetBssSheet-'+netServTypeIdArrays[i]).getJsonData();
          }
          netPortStr = netInfoObj.netPort;
          var netOltFactory = netInfoObj.netOltFactory;
          var netWanType = netInfoObj.netWanType;
          var netUsername = netInfoObj.netUsername;
          var netPasswd = netInfoObj.netPasswd;
          
          if('1'==netWanType || '2'==netWanType){
        	  if(netPortStr == null || netPortStr == ''){
       			  alert("�Žӡ�·��ģʽʱ����˿�ֵ����Ϊ�ա�");
 	              return false;
        	  }
          }
          
          if('2'==netWanType || '3'==netWanType){
        	  if(netUsername == null || netUsername == ''){
       			  alert("·�ɡ�ȫ·��ģʽʱ����˺Ų���Ϊ�ա�");
 	              return false;
        	  }
        	  if(netPasswd == null || netPasswd == ''){
       			  alert("·�ɡ�ȫ·��ģʽʱ������벻��Ϊ�ա�");
 	              return false;
        	  }
          }
          
          if('HW'!=netOltFactory.toUpperCase() && 'HUAWEI'!=netOltFactory.toUpperCase()){
        	  if (netInfoObj.netVlanId == null ||"" == netInfoObj.netVlanId) {
	              alert("���VlanId����Ϊ�ա�");
	              $("input[name='netVlanId']").focus();
	              return false;
	            }
          }
          var netPortArays = netPortStr.split(',');
          if(netPortArays.length > 1){
            alert("����˿�ֵֻ��ѡһ����");
            return false;
          }
          
          var oldNetOltFactory = $("input[name='oldNetOltFactory']").val();
          var oldNetPort = $("input[name='oldNetPort']").val();
          //�ж�olt�Ƿ������޸�,����olt��Ϊ��Ϊolt����vlanid��Ϊ��
       	  if(netOltFactory.toUpperCase()!=oldNetOltFactory.toUpperCase()){
       		  if('HW'==netOltFactory.toUpperCase() || 'HUAWEI'==netOltFactory.toUpperCase()){
	       		  $("input[name='netVlanId']").val("");
	       			netInfoObj.netVlanId="";
       		  }
       	  }else{
       		  //oltδ�޸ģ�����ǻ�Ϊolt�������޸��˰󶨿ڣ���vlanid��Ϊ��
       		  if('HW'==netOltFactory.toUpperCase() || 'HUAWEI'==netOltFactory.toUpperCase()){
       			  if(netPortStr!=oldNetPort){
       				$("input[name='netVlanId']").val("");
       				netInfoObj.netVlanId="";
       			  }
       		  }
       	  }
          
        }

        if ("21" != iptvServTypeId) {
          iptvServTypeId = "";
        }
        else {
          // ��ȡIPTV�û���Ϣ
          for(i = 0;i<iptvServTypeIdArrays.length;i++){
            var iptvInfoObj = $('#iptvBssSheet-'+iptvServTypeIdArrays[i]).getJsonData();
          }
          var WanType = iptvInfoObj.iptvWanType;
          if ("-1" == WanType) {
            alert("IPTV������ʽ����Ϊ�ա�");
            $("select[name='iptvWanType']").focus();
            return false;
          }
          if ("" == iptvInfoObj.iptvVlanId) {
            alert("iptvVlanId����Ϊ�ա�");
            $("input[name='iptvVlanId']").focus();
            return false;
          }

          //�Ž�
          if ('' == iptvInfoObj.iptvPort) {
            alert("IPTV�󶨶˿ڲ���Ϊ�ա�");
            $("input[name='iptvPort']").focus();
            return false;
          }
        }

        if ("15" != hvoipServTypeId) {
          hvoipServTypeId = "";
        }
        else {
          // ��ȡH248��Ϣ
          for(i = 0;i<hvoipServTypeIdArrays.length;i++){
            var h248InfoObj = $('#H248BssSheet-'+hvoipServTypeIdArrays[i]).getJsonData();

          }
          if ("" == $("input[name='hvoipEid']").val()) {
            alert("EID����Ϊ�ա�");
            $("input[name='hvoipEid']").focus();
            return false;
          }
          /*else if(!reg_verify($("input[name='hvoipEid']").val())){
              alert("EID���ǺϷ�IP��ַ��");
              $("input[name='hvoipEid']").focus();
              return false;
          }
          if("" == $("input[name='hvoipPhone']").val())
          {
              alert("�绰ҵ���߼��Ų���Ϊ�ա�");
              $("input[name='hvoipPhone']").focus();
              return false;
          }
          if("" == $("input[name='hvoipRegId']").val())
          {
              alert("�ն˱�ʾ��������Ϊ�ա�");
              $("input[name='hvoipRegId']").focus();
              return false;
          }
          else if(!reg_verify($("input[name='hvoipRegId']").val())){
              alert("�ն˱�ʾ�������ǺϷ�IP��ַ��");
              $("input[name='hvoipRegId']").focus();
              return false;
          }
          if("" == $("select[name='hvoipRegIdType']").val())
          {
              alert("�ն˱�ʾ���Ͳ���Ϊ�ա�");
              $("select[name='hvoipRegIdType']").focus();
              return false;
          }*/
          if ("" == $("input[name='hvoipMgcIp']").val()) {
            alert("����MGC��ַ����Ϊ�ա�");
            $("input[name='hvoipMgcIp']").focus();
            return false;
          }
          else if (!reg_verify($("input[name='hvoipMgcIp']").val())) {
            alert("����MGC��ַ���ǺϷ�IP��ַ��");
            $("input[name='hvoipMgcIp']").focus();
            return false;
          }
          if ("" == $("input[name='hvoipMgcPort']").val()) {
            alert("����MGC�˿ڲ���Ϊ�ա�");
            $("input[name='hvoipMgcPort']").focus();
            return false;
          }
          if ("" == $("input[name='hvoipStandMgcIp']").val()) {
            alert("����MGC��ַ����Ϊ�ա�");
            $("input[name='hvoipStandMgcIp']").focus();
            return false;
          }
          else if (!reg_verify($("input[name='hvoipStandMgcIp']").val())) {
            alert("����MGC��ַ���ǺϷ�IP��ַ��");
            $("input[name='hvoipStandMgcIp']").focus();
            return false;
          }
          if ("" == $("input[name='hvoipStandMgcPort']").val()) {
            alert("����MGC�˿ڲ���Ϊ�ա�");
            $("input[name='hvoipStandMgcPort']").focus();
            return false;
          }
          /*if("" == $("input[name='hvoipVlanId']").val())
          {
              alert("VLANDID����Ϊ��");
              $("input[name='hvoipVlanId']").focus();
              return false;
          }
          if("" == $("input[name='hvoipIpaddress']").val())
          {
              alert("ont������ҵ���ַ����Ϊ�ա�");
              $("input[name='hvoipIpaddress']").focus();
              return false;
          }
          else if(!reg_verify($("input[name='hvoipIpaddress']").val())){
              alert("ont������ҵ���ַ���ǺϷ�IP��ַ��");
              $("input[name='hvoipIpaddress']").focus();
              return false;
          }
          if("" == $("input[name='hvoipIpmask']").val())
          {
              alert("ont������ҵ���ַ���벻��Ϊ�ա�");
              $("input[name='hvoipIpmask']").focus();
              return false;
          }
          else if(!reg_verify($("input[name='hvoipIpmask']").val())){
              alert("ont������ҵ���ַ���벻�ǺϷ�IP��ַ��");
              $("input[name='hvoipIpmask']").focus();
              return false;
          }
          if("" == $("input[name='hvoipGateway']").val())
          {
              alert("ont����ҵ�����ز���Ϊ�ա�");
              $("input[name='hvoipGateway']").focus();
              return false;
          }
          else if(!reg_verify($("input[name='hvoipGateway']").val())){
              alert("ont������ҵ�����ز��ǺϷ�IP��ַ��");
              $("input[name='hvoipGateway']").focus();
              return false;
          }*/
        }
        if ("14" != sipServTypeId) {
          sipServTypeId = "";
        }else{
          // ��ȡsip��Ϣ
          for(i = 0;i<sipServTypeIdArrays.length;i++){
            var sipInfoObj = $('#sipBssSheet-'+sipServTypeIdArrays[i]).getJsonData();
            debugger
          }
        }

        var alertstr = "ȷ��Ҫ�ύ";
        if (netServTypeId != "") {
          alertstr = alertstr + "���";
          if (hvoipServTypeId != "" || iptvServTypeId != ""  || sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (iptvServTypeId != "") {
          alertstr = alertstr + "IPTV";
          if (hvoipServTypeId != "" || sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (hvoipServTypeId != "") {
          alertstr = alertstr + "H248����";
          if (sipServTypeId != "") {
            alertstr = alertstr + "��";
          }
        }
        if (sipServTypeId != "") {
          alertstr = alertstr + "SIP����";
        }
        var r = confirm(alertstr + "ҵ���� ��");
        if (r == false) {
          return;
        }
        serverInfoObj = Object.assign(userInfoObj,netInfoObj, iptvInfoObj,h248InfoObj,sipInfoObj);
        serverInfoObj.netServTypeId = netServTypeId;
        serverInfoObj.hvoipServTypeId = hvoipServTypeId;
        serverInfoObj.sipServTypeId = sipServTypeId;
        serverInfoObj.iptvServTypeId = iptvServTypeId;
        serverInfoObj.cmdId=cmdId;
        //ҵ�񴥷�
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!doBusiness.action'/>";
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
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
                                    <TD width="35%"><input type="text" name="officeId" class=bk value=""></TD>
                                    <TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
                                    <TD width="35%"><input type="text" name="areaId" class=bk value=""></TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">�ն˽�������:</TD>
                                    <TD width="35%">
                                        <SELECT name="accessStyle" class="bk" disabled>
                                            <!-- <OPTION value="-1">--��ѡ��--</OPTION>
                                            <OPTION value="1">ADSL</OPTION>
                                            <OPTION value="2">LAN</OPTION>
                                            <OPTION value="3">EPON</OPTION> -->
                                            <OPTION value="4">GPON</OPTION>
                                        </SELECT>
                                        &nbsp;<font color="#FF0000">*</font>
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
                                            <OPTION value="2" selected = 'true'>e8-c</OPTION>
                                        </SELECT>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                </tbody>



                            </TABLE>
                        </TD>

                    </TR>
                    <tr >
                        <TD colspan="4" bgcolor="#999999">
                            <TABLE id = "info-list" border=0 cellspacing=1 cellpadding=2 width="100%">
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