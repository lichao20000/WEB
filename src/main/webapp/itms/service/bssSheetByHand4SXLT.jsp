<%--
湖北电信手工工单
Author: xiangzl
Version: 1.0.0
Date: 2012-09-14
--%>

<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<html>
<HEAD>
    <title>终端业务下发</title>
    <link href="../../css/css_green.css" rel="stylesheet" type="text/css">
    <link href="../css/listview.css" rel="stylesheet" type="text/css">
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

    <SCRIPT LANGUAGE="JavaScript">
      var loginCityId = "-1";
      String.prototype.replaceAll = function (oldStr, newStr) {
        return this.replace(new RegExp(oldStr, "gm"), newStr);
      }

      function reset () {
        $("input[@name='obj.loid']").val("");
        cleanValue();
      }

      function cleanValue () {
        setTime();
        $("#internetBssSheet").hide();
        $("#iptvBssSheet").hide();
        $("#H248BssSheet").hide();
        $("#sipBssSheet").hide();

        $("select[@name='obj.cityId']").val(loginCityId);
        $("input[@name='obj.linkman']").val("");
        $("input[@name='obj.linkPhone']").val("");
        $("input[@name='obj.email']").val("");
        $("input[@name='obj.mobile']").val("");
        $("input[@name='obj.linkAddress']").val("");
        $("input[@name='obj.linkmanCredno']").val("");
        $("select[@name='obj.deviceType']").val("1");

        $("input[@name='obj.netOltFactory']").val("");
        $("input[@name='obj.netServTypeId']").attr("checked", false);
        $("select[@name='obj.netUsername']").val("-1");
        $("input[@name='obj.netPasswd']").val("");
        $("input[@name='obj.netVlanId']").val("");
        $("input[@name='obj.netSpeed']").val("");
        var checkBoxNet = document.frm.netPort;
        for (var i = 0; i < checkBoxNet.length; i++) {
          checkBoxNet[i].checked = false;
        }
        //$("input[@name='obj.netPort']").val("");
        $("select[@name='obj.netWanType']").val("-1");

        $("input[@name='obj.iptvOltFactory']").val("");
        $("input[@name='obj.iptvServTypeId']").attr("checked", false);
        $("select[@name='obj.iptvWanType']").val("-1");
        $("input[@name='obj.iptvVlanId']").val("");
        $("input[@name='obj.multicastVlan']").val("");
        $("input[@name='obj.iptvUserName']").val("");
        $("input[@name='obj.iptvPasswd']").val("");
        $("input[@name='obj.iptvDestIp']").val("10.0.0.0");
        $("input[@name='obj.iptvDestMark']").val("255.0.0.0");
        var checkBoxObj = document.frm.iptvPort;
        for (var i = 0; i < checkBoxObj.length; i++) {
          checkBoxObj[i].checked = false;
        }
        //$("input[@name='obj.iptvPort']").val("");

        $("input[@name='obj.hvoipOltFactory']").val("");
        $("input[@name='obj.hvoipServTypeId']").attr("checked", false);
        $("select[@name='obj.hvoipRegIdType']").val("-1");
        $("input[@name='obj.hvoipRegId']").val("");
        $("input[@name='obj.hvoipMgcIp']").val("");
        $("input[@name='obj.hvoipMgcPort']").val("2944");
        $("input[@name='obj.hvoipStandMgcIp']").val("");
        $("input[@name='obj.hvoipStandMgcPort']").val("2944");
        $("input[@name='obj.hvoipIpaddress']").val("");
        $("input[@name='obj.hvoipIpmask']").val("");
        $("input[@name='obj.hvoipGateway']").val("");
        $("input[@name='obj.hvoipVlanId']").val("");
        $("input[@name='obj.hvoipEid']").val("");
        $("input[@name='obj.hvoipPhone']").val("");

        $("select[@name='obj.sipVoipPort']").val("-1");
        $("input[@name='obj.sipVoipPhone']").val("");
        $("input[@name='obj.sipVoipUsername']").val("");
        $("input[@name='obj.sipVoipPwd']").val("");
        $("input[@name='obj.sipProxServ']").val("");
        $("input[@name='obj.sipProxPort']").val("5060");
        $("input[@name='obj.sipStandProxServ']").val("");
        $("input[@name='obj.sipStandProxPort']").val("5060");
        $("input[@name='obj.sipRegiServ']").val("");
        $("input[@name='obj.sipRegiPort']").val("5060");
        $("input[@name='obj.sipStandRegiServ']").val("");
        $("input[@name='obj.sipStandRegiPort']").val("5060");
        $("select[@name='obj.sipProtocol']").val("-1");
        $("input[@name='obj.sipIpaddress']").val("");
        $("input[@name='obj.sipIpmask']").val("");
        $("input[@name='obj.sipGateway']").val("");
        $("input[@name='obj.sipVoipUri']").val("");
        $("input[@name='obj.sipOutBoundProxy']").val("");
        $("input[@name='obj.sipOutBoundPort']").val("5060");
        $("input[@name='obj.sipStandOutBoundProxy']").val("");
        $("input[@name='obj.sipStandOutBoundPort']").val("5060");

      }

      function iptvWanTypeChange () {
        var WanType = $("select[@name='obj.iptvWanType']").children('option:selected').val();//这就是selected的值
        if ("1" == WanType) {
          $("input[@name='obj.iptvUserName']").parent().parent().css("display", "none");
          $("input[@name='obj.iptvDestIp']").parent().parent().css("display", "none");
          $("input[@name='iptvPort']").parent().parent().css("display", "block");
          $("input[@name='obj.iptvUserName']").val("");
          $("input[@name='obj.iptvPasswd']").val("");
          $("input[@name='obj.iptvDestIp']").val("10.0.0.0");
          $("input[@name='obj.iptvDestMark']").val("255.0.0.0");
        }
        else if ("2" == WanType) {
          $("input[@name='obj.iptvUserName']").parent().parent().css("display", "block");
          $("input[@name='obj.iptvDestIp']").parent().parent().css("display", "block");
          $("input[@name='iptvPort']").parent().parent().css("display", "none");
          //$("input[@name='obj.iptvPort']").val("");
          var checkBoxBox = document.frm.iptvPort;
          for (var i = 0; i < checkBoxBox.length; i++) {
            checkBoxBox[i].checked = false;
          }
        }
        else {
          $("input[@name='obj.iptvUserName']").parent().parent().css("display", "block");
          $("input[@name='obj.iptvDestIp']").parent().parent().css("display", "block");
          $("input[@name='iptvPort']").parent().parent().css("display", "block");
        }
      }

      $(function () {
        var gw_type = '<s:property value="gw_type" />';
        var loid = '<s:property value="loid" />';
        loginCityId = '<s:property value="cityId" />';
        $("select[@name='obj.cityId']").val(loginCityId);

        //家庭网关
        if (gw_type == '1') {
          $("select[@name='obj.userType']").val('1');
          $("input[@name='obj.cmdId']").val('FROMWEB-0000002');
          $("#siptab").show();
          $("#nettab").show();
          $("#h248tab").show();
          $("#iptvtab").show();
        }
        if (gw_type == '2')//企业网关
        {
          $("select[@name='obj.userType']").val('2');
        }

        if (loid != "") {
          $("input[@name='obj.loid']").val(loid);
          checkLoid();
          return;
        }

        setTime();

        $("input[@name='obj.loid']").blur(function () {
          if ($("input[@name='obj.loid']").val() != "")
            checkLoid();
        })
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
        $("input[@name='obj.dealDate']").val(dstr);
      }

      //静态ip，显示必填字段
      function changeStatic (wanType, strip, strdns) {
        if ("3" == wanType) {
          $("tr[@id='" + strip + "']").css("display", "block");
          $("tr[@id='" + strdns + "']").css("display", "block");
        }
        else {
          $("tr[@id='" + strip + "']").css("display", "none");
          $("tr[@id='" + strdns + "']").css("display", "none");
        }
      }

      //控制备用dns是否显示
      function showStandDns (wanType, strdns) {
        if ("3" == wanType) {
          $("tr[@id='" + strdns + "']").css("display", "block");
        }
        else {
          $("tr[@id='" + strdns + "']").css("display", "none");
        }
      }

      // 用于动态显示三大业务（宽带，IPTV，H248,sip）
      function showSheet (objId, name) {
        if ($("#" + objId).attr("checked")) {
          document.getElementById(name).style.display = "";
        } else {
          document.getElementById(name).style.display = "none";
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

      //如果是ims sip 显示uri 和domain
      function changeIms (protocol) {
        if ("0" == protocol) {
          $("tr[@id='ims']").css("display", "table-row");
          $("tr[@id='ims2']").css("display", "table-row");
          $("tr[@id='ims3']").css("display", "table-row");
        }
        else {
          $("tr[@id='ims']").css("display", "none");
          $("tr[@id='ims2']").css("display", "none");
          $("tr[@id='ims3']").css("display", "none");
        }
      }

      //检查LOID是否存在
      function checkLoid () {
        if ("-1" == $("select[@name='obj.userType']").val()) {
          alert("请选择用户类型。");
          return false;
        }
        if ("" == $("input[@name='obj.loid']").val().trim()) {
          alert("LOID不可为空！");
          $("input[@name='obj.loid']").focus();
          return false;
        }
        cleanValue();

        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!checkLoid.action'/>";
        $.post(url, {
          "obj.userType": $("select[@name='obj.userType']").val(),
          "obj.loid": $("input[@name='obj.loid']").val().trim()
        }, function (ajax) {
          if ("000" == ajax) {
            alert("LOID可以使用。");
          }
          else {
            //alert(ajax);
            $("#netServTypeId").attr("checked", false);
            $("#hvoipServTypeId").attr("checked", false);
            $("#sipServTypeId").attr("checked", false);
            var relt = ajax.split("|");
            $("input[@name='obj.userOperateId']").val(relt[4]);
            //$("input[@name='obj.dealDate']").val();
            //$("select[@name='obj.userType']").val();
            //$("input[@name='obj.loid']").val();
            $("select[@name='obj.cityId']").val(relt[8]);
            $("input[@name='obj.officeId']").val(relt[9]);
            $("input[@name='obj.areaId']").val(relt[10]);
            $("select[@name='obj.accessStyle']").val(relt[11]);
            $("input[@name='obj.linkman']").val(relt[12]);
            $("input[@name='obj.linkPhone']").val(relt[13]);
            $("input[@name='obj.email']").val(relt[14]);
            $("input[@name='obj.mobile']").val(relt[15]);
            $("input[@name='obj.linkAddress']").val(relt[16]);
            $("input[@name='obj.linkmanCredno']").val(relt[17]);
            $("input[@name='obj.customerId']").val(relt[18]);
            $("input[@name='obj.customerAccount']").val(relt[19]);
            //$("input[@name='obj.customerPwd']").val();
            $("select[@name='obj.specId']").val(relt[20]);
            $("select[@name='obj.deviceType']").val(relt[84]);

            if ("22" == relt[21]) {
              $("#netServTypeId").attr("checked", true);
              $("input[@name='obj.netOltFactory']").val(relt[95]);
              $("input[@name='obj.netOperateId']").val(relt[22]);
              $("input[@name='obj.netUsername']").val(relt[23]);
              $("input[@name='obj.netPasswd']").val(relt[24]);
              $("input[@name='obj.netVlanId']").val(relt[25]);
              $("select[@name='obj.netWanType']").val(relt[26]);
              var netPortStrs = relt[83].split(",");
              var checkBoxBox = document.frm.netPort;

              for (var j = 0; j < netPortStrs.length; j++) {
                for (var i = 0; i < checkBoxBox.length; i++) {
                  if (checkBoxBox[i].value == netPortStrs[j]) {
                    checkBoxBox[i].checked = true;
                  }
                }
              }
              $("input[@name='obj.netIpaddress']").val(relt[27]);
              $("input[@name='obj.netIpmask']").val(relt[28]);
              $("input[@name='obj.netGateway']").val(relt[29]);
              $("input[@name='obj.netIpdns']").val(relt[30]);
              $("input[@name='obj.netSpeed']").val(relt[105]);
              //$("input[@name='obj.standNetIpdns']").val(relt[74]);

              showSheet('netServTypeId', 'internetBssSheet');
              if ("3" == relt[26]) {
                changeStatic(relt[26], 'netip', 'netDns');
                //showStandDns(relt[26],'standnetDns');
              }
            }
            if ("15" == relt[31]) {
              $("#hvoipServTypeId").attr("checked", true);
              $("input[@name='obj.hvoipOltFactory']").val(relt[97]);
              $("input[@name='obj.hvoipOperateId']").val(relt[32]);
              $("input[@name='obj.hvoipPhone']").val(relt[33]);
              $("input[@name='obj.hvoipRegId']").val(relt[34]);
              if ("0" == relt[35]) {
                $("select[@name='obj.hvoipRegIdType']").val("IP");
              }
              else if ("1" == relt[35]) {
                $("select[@name='obj.hvoipRegIdType']").val("DomainName");
              }
              else {
                $("select[@name='obj.hvoipRegIdType']").val("-1");
              }
              $("input[@name='obj.hvoipMgcIp']").val(relt[36]);
              $("input[@name='obj.hvoipMgcPort']").val(relt[37]);
              $("input[@name='obj.hvoipStandMgcIp']").val(relt[38]);
              $("input[@name='obj.hvoipStandMgcPort']").val(relt[39]);
              $("select[@name='obj.hvoipPort']").val(relt[40]);
              $("input[@name='obj.hvoipVlanId']").val(relt[41]);
              $("select[@name='obj.hvoipWanType']").val(relt[42]);
              $("input[@name='obj.hvoipIpaddress']").val(relt[43]);
              $("input[@name='obj.hvoipIpmask']").val(relt[44]);
              $("input[@name='obj.hvoipGateway']").val(relt[45]);
              $("input[@name='obj.hvoipIpdns']").val(relt[46]);
              $("input[@name='isH248SendPost']").val("2");
              $("input[@name='obj.hvoipEid']").val(relt[91]);
              showSheet('hvoipServTypeId', 'H248BssSheet');
              if ("3" == relt[42]) {
                changeStatic(relt[42], 'h248IP', 'h248Dns');
              }
            }
            if ("14" == relt[47]) {
              $("#sipServTypeId").attr("checked", true);
              changeIms(relt[65]);
              $("input[@name='obj.sipOperateId']").val(relt[48]);
              $("input[@name='obj.sipVoipPhone']").val(relt[49]);
              $("input[@name='obj.sipVoipUsername']").val(relt[50]);
              $("input[@name='obj.sipVoipPwd']").val(relt[51]);
              $("input[@name='obj.sipProxServ']").val(relt[52]);
              $("input[@name='obj.sipProxPort']").val(relt[53]);
              $("input[@name='obj.sipStandProxServ']").val(relt[54]);
              $("input[@name='obj.sipStandProxPort']").val(relt[55]);
              $("select[@name='obj.sipVoipPort']").val(relt[56]);
              $("input[@name='obj.sipRegiServ']").val(relt[57]);
              $("input[@name='obj.sipRegiPort']").val(relt[58]);
              $("input[@name='obj.sipStandRegiServ']").val(relt[59]);
              $("input[@name='obj.sipStandRegiPort']").val(relt[60]);
              $("input[@name='obj.sipOutBoundProxy']").val(relt[61]);
              $("input[@name='obj.sipOutBoundPort']").val(relt[62]);
              $("input[@name='obj.sipStandOutBoundProxy']").val(relt[63]);
              $("input[@name='obj.sipStandOutBoundPort']").val(relt[64]);
              $("select[@name='obj.sipProtocol']").val(relt[65]);
              $("input[@name='obj.sipVlanId']").val(relt[66]);
              $("select[@name='obj.sipWanType']").val(relt[67]);
              $("input[@name='obj.sipIpaddress']").val(relt[68]);
              $("input[@name='obj.sipIpmask']").val(relt[69]);
              $("input[@name='obj.sipGateway']").val(relt[70]);
              $("input[@name='obj.sipIpdns']").val(relt[71]);
              $("input[@name='obj.sipVoipUri']").val(relt[72]);
              $("input[@name='obj.sipUserAgentDomain']").val(relt[73]);
              $("input[@id='isSipSendPost']").val("2");
              showSheet('sipServTypeId', 'sipBssSheet');
              if ("3" == relt[67]) {
                changeStatic(relt[67], 'sipIP', 'sipDns');
              }
            }
            if ("21" == relt[75]) {
              $("#iptvServTypeId").attr("checked", true);
              $("input[@name='obj.iptvOltFactory']").val(relt[96]);
              $("input[@name='obj.iptvOperateId']").val('1');
              $("select[@name='obj.iptvWanType']").val(relt[86]);
              //$("input[@name='obj.iptvUserName']").val(relt[77]);
              //$("input[@name='obj.iptvPasswd']").val(relt[88]);
              //$("input[@name='obj.iptvPort']").val(relt[78]);
              var iptvPortStrs = relt[78].split(",");
              var checkBoxBox = document.frm.iptvPort;

              for (var j = 0; j < iptvPortStrs.length; j++) {
                for (var i = 0; i < checkBoxBox.length; i++) {
                  if (checkBoxBox[i].value == iptvPortStrs[j]) {
                    checkBoxBox[i].checked = true;
                    continue;
                  }
                }
              }
              $("input[@name='obj.iptvVlanId']").val(relt[79]);
              // $("input[@name='obj.multicastVlan']").val(relt[87]);
              // $("input[@name='obj.iptvDestIp']").val(relt[89]);
              // $("input[@name='obj.iptvDestMark']").val(relt[90]);
              // iptvWanTypeChange();
              showSheet('iptvServTypeId', 'iptvBssSheet');
            }
          }
        });
      }

      //检查customerId是否存在
      function checkCustomerId () {
        if ("" == $("input[@name='obj.customerId']").val()) {
          alert("客户id必须输入。");
          $("input[@name='obj.customerId']").focus();
          return false;
        }
        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!checkCustomerId.action'/>";
        $.post(url, {
          "obj.customerId": $("input[@name='obj.customerId']").val()
        }, function (ajax) {
          if ("-1" == ajax) {
            alert("客户ID已经存在，请重新输入。");
            $("input[@name='obj.customerId']").focus();
          }
          else {
            alert("客户ID可以使用。");
          }
        });
      }

      //业务销户提交
      function delBusiness () {
        var netServTypeId = $("input[@name='obj.netServTypeId'][checked]").val();
        var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
        var iptvServTypeId = $("input[@name='obj.iptvServTypeId'][checked]").val();
        var sipServTypeId = $("input[@name='obj.sipServTypeId'][checked]").val();
        var loid = $("input[@name='obj.loid']").val().trim();
        var cityId = $("select[@name='obj.cityId']").val();

        if ("" == loid) {
          alert("LOID不可为空。");
          $("input[@name='obj.loid']").focus();
          return false;
        }

        if ("-1" == $("select[@name='obj.cityId']").val()) {
          alert("属地不可为空。");
          $("select[@name='obj.cityId']").focus();
          return false;
        }

        if (netServTypeId != "22") {
          netServTypeId = "";
        }
        else {
          var netWanType = $("select[@name='obj.netWanType']").val();
          if ("-1" == netWanType) {
            alert("宽带上网方式不可为空。");
            $("select[@name='obj.netWanType']").focus();
            return false;
          }
        }
        if (iptvServTypeId != "21") {
          iptvServTypeId = "";
        }
        else {
          var WanType = $("select[@name='obj.iptvWanType']").val();
          if ("-1" == WanType) {
            alert("IPTV上网方式不可为空。");
            $("select[@name='obj.iptvWanType']").focus();
            return false;
          }
        }
        if (hvoipServTypeId != "15") {
          hvoipServTypeId = "";
        }
        if (sipServTypeId != "14") {
          sipServTypeId = "";
        }
        /* if((netServTypeId == "" && iptvServTypeId == "" && hvoipServTypeId == "")||(netServTypeId != "" && iptvServTypeId != "" && hvoipServTypeId != "")){
            var r=confirm("确认要删除当前用户所有业务吗 ？");
              if (r==false){
                return ;
            }
        } */
        if (netServTypeId == "" && iptvServTypeId == "" && hvoipServTypeId == "" && sipServTypeId == "") {
          var r = confirm("确认要删除当前用户吗 ？");
          if (r == false) {
            return;
          }
        } else {
          var alertstr = "确认要删除";
          if (netServTypeId != "") {
            alertstr = alertstr + "宽带";
            if (hvoipServTypeId != "" || iptvServTypeId != "" || sipServTypeId !="") {
              alertstr = alertstr + "、";
            }
          }
          if (iptvServTypeId != "") {
            alertstr = alertstr + "IPTV";
            if (hvoipServTypeId != "" || sipServTypeId !="") {
              alertstr = alertstr + "、";
            }
          }
          if (hvoipServTypeId != "") {
            alertstr = alertstr + "H248语音";
            if ( sipServTypeId !="") {
              alertstr = alertstr + "、";
            }
          }
          if (sipServTypeId != "") {
            alertstr = alertstr + "SIP语音";
          }
          var r = confirm(alertstr + "业务吗 ？");
          if (r == false) {
            return;
          }

        }
        var iptvPortStr = "";
        var checkBoxBox = document.frm.iptvPort;
        var portflag = false;
        for (var i = 0; i < checkBoxBox.length; i++) {
          if (checkBoxBox[i].checked) {
            if (portflag) {
              iptvPortStr = iptvPortStr + ",";
            }
            iptvPortStr = iptvPortStr + checkBoxBox[i].value;
            portflag = true;
          }
        }
        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!delBusiness.action'/>";
        $.post(url, {
          "obj.dealDate": $("input[@name='obj.dealDate']").val(),
          "obj.loid": loid,
          "obj.cityId": cityId,

          "obj.netServTypeId": netServTypeId,
          "obj.netWanType": $("select[@name='obj.netWanType']").val(),

          "obj.hvoipServTypeId": hvoipServTypeId,

          "obj.iptvServTypeId": iptvServTypeId,
          "obj.sipServTypeId": sipServTypeId,
          "obj.iptvWanType": $("select[@name='obj.iptvWanType']").val(),
          "obj.iptvPort": iptvPortStr
        }, function (ajax) {
          alert(ajax);
          $("button[@name='delBtn']").attr("disabled", true);
          checkLoid();
        });
        //灰化按钮
        $("button[@name='delBtn']").attr("disabled", true);
      }

      //开户业务提交
      function doBusiness () {
        var loidvalue = "";
        if ("-1" == $("select[@name='obj.userType']").val()) {
          alert("请选择用户类型。");
          return false;
        }

        if ("2" == $("select[@name='obj.userType']").val() && "" == $("input[@name='obj.customerId']").val()) {
          alert("客户ID不可为空。");
          $("input[@name='obj.customerId']").focus();
          return false;
        }

        if ("2" != $("select[@name='obj.accessStyle']").val()) {
          if ("" == $("input[@name='obj.loid']").val().trim()) {
            alert("LOID不可为空。");
            $("input[@name='obj.loid']").focus();
            return false;
          }
          loidvalue = $("input[@name='obj.loid']").val();
        }
        else {
          loidvalue = "bbms" + $("input[@name='obj.customerId']").val();
        }
        if ("-1" == $("select[@name='obj.cityId']").val()) {
          alert("属地不可为空。");
          $("input[@name='obj.loid']").focus();
          return false;
        }
        if ("-1" == $("select[@name='obj.accessStyle']").val()) {
          alert("终端接入类型不可为空。");
          $("input[@name='obj.loid']").focus();
          return false;
        }
        /* if("-1" == $("select[@name='obj.deviceType']").val())
        {
            alert("请选择终端类型。");
            $("select[@name='obj.deviceType']").focus();
            return false;
        } */

        var netServTypeId = $("input[@name='obj.netServTypeId'][checked]").val();
        var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
        var sipServTypeId = $("input[@name='obj.sipServTypeId'][checked]").val();
        var iptvServTypeId = $("input[@name='obj.iptvServTypeId'][checked]").val();
        var netPortStr = "";
        var iptvPortStr = "";

        if ("22" != netServTypeId) {
          netServTypeId = "";
        }
        else {
          /* if("" == $("input[@name='obj.netOltFactory']").val())
          {
              alert("宽带OltFactory不可为空。");
              $("input[@name='obj.netOltFactory']").focus();
              return false;
          } */
          var netWanType = $("select[@name='obj.netWanType']").val();
          if ("-1" == netWanType) {
            alert("宽带上网方式不可为空。");
            $("select[@name='obj.netWanType']").focus();
            return false;
          }
          /* if("" == $("input[@name='obj.netSpeed']").val())
          {
              alert("帐号速率不可为空。");
              $("input[@name='obj.netSpeed']").focus();
              return false;
          } */
          //路由
          else if ("2" == netWanType) {
            if ("-1" == $("input[@name='obj.netUsername']").val()) {
              alert("宽带账号不可为空。");
              $("input[@name='obj.netUsername']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.netPasswd']").val()) {
              alert("宽带密码不可为空。");
              $("input[@name='obj.netPasswd']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.netVlanId']").val()) {
              alert("VLANDID不可为空。");
              $("input[@name='obj.netVlanId']").focus();
              return false;
            }
          }
          //桥接
          else {
            if ("" == $("input[@name='obj.netVlanId']").val()) {
              alert("VLANDID不可为空。");
              $("input[@name='obj.netVlanId']").focus();
              return false;
            }
            if ("-1" == $("input[@name='obj.netUsername']").val()) {
              alert("宽带账号不可为空。");
              $("input[@name='obj.netUsername']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.netPasswd']").val()) {
              alert("宽带密码不可为空。");
              $("input[@name='obj.netPasswd']").focus();
              return false;
            }
            var checkBoxNet = document.frm.netPort;
            var portflag = false;
            for (var i = 0; i < checkBoxNet.length; i++) {
              if (checkBoxNet[i].checked) {
                if (portflag) {
                  netPortStr = netPortStr + ",";
                }
                netPortStr = netPortStr + checkBoxNet[i].value;
                portflag = true;
              }
            }
            if (!portflag) {
              alert("宽带绑定端口不可为空。");
              $("input[@name='netPort']").focus();
              return false;
            }
          }
        }
        if ("21" != iptvServTypeId) {
          iptvServTypeId = "";
        }
        else {
          /* if("" == $("input[@name='obj.iptvOltFactory']").val())
          {
              alert("IPTV OltFactory不可为空。");
              $("input[@name='obj.iptvOltFactory']").focus();
              return false;
          } */
          var WanType = $("select[@name='obj.iptvWanType']").val();
          if ("-1" == WanType) {
            alert("IPTV上网方式不可为空。");
            $("select[@name='obj.iptvWanType']").focus();
            return false;
          }
          if ("" == $("input[@name='obj.iptvVlanId']").val()) {
            alert("IPTV点播不可为空。");
            $("input[@name='obj.iptvVlanId']").focus();
            return false;
          }
          /* if("" == $("input[@name='obj.multicastVlan']").val())
          {
              alert("IPTV组播不可为空。");
              $("input[@name='obj.multicastVlan']").focus();
              return false;
          } */
          //路由
          else if ("2" == WanType) {
            if ("" == $("input[@name='obj.iptvUserName']").val()) {
              alert("IPTV账号不可为空。");
              $("input[@name='obj.iptvUserName']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.iptvPasswd']").val()) {
              alert("IPTV密码不可为空。");
              $("input[@name='obj.iptvPasswd']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.iptvDestIp']").val()) {
              alert("静态路由目的地址不可为空。");
              $("input[@name='obj.iptvDestIp']").focus();
              return false;
            }
            if ("" == $("input[@name='obj.iptvDestMark']").val()) {
              alert("静态路由掩码不可为空。");
              $("input[@name='obj.iptvDestMark']").focus();
              return false;
            }
          }
          //桥接
          else {
            var checkBoxBox = document.frm.iptvPort;
            var portflag = false;
            for (var i = 0; i < checkBoxBox.length; i++) {
              if (checkBoxBox[i].checked) {
                if (portflag) {
                  iptvPortStr = iptvPortStr + ",";
                }
                iptvPortStr = iptvPortStr + checkBoxBox[i].value;
                portflag = true;
              }
            }
            if (!portflag) {
              alert("IPTV绑定端口不可为空。");
              $("input[@name='iptvPort']").focus();
              return false;
            }
          }
        }

        if ("15" != hvoipServTypeId) {
          hvoipServTypeId = "";
        }
        else {
          /* if("" == $("input[@name='obj.hvoipOltFactory']").val())
          {
              alert("voip语音OltFactory不可为空。");
              $("input[@name='obj.hvoipOltFactory']").focus();
              return false;
          } */
          if ("" == $("input[@name='obj.hvoipEid']").val()) {
            alert("EID不可为空。");
            $("input[@name='obj.hvoipEid']").focus();
            return false;
          }
          /*else if(!reg_verify($("input[@name='obj.hvoipEid']").val())){
              alert("EID不是合法IP地址。");
              $("input[@name='obj.hvoipEid']").focus();
              return false;
          }
          if("" == $("input[@name='obj.hvoipPhone']").val())
          {
              alert("电话业务逻辑号不可为空。");
              $("input[@name='obj.hvoipPhone']").focus();
              return false;
          }
          if("" == $("input[@name='obj.hvoipRegId']").val())
          {
              alert("终端表示域名不可为空。");
              $("input[@name='obj.hvoipRegId']").focus();
              return false;
          }
          else if(!reg_verify($("input[@name='obj.hvoipRegId']").val())){
              alert("终端表示域名不是合法IP地址。");
              $("input[@name='obj.hvoipRegId']").focus();
              return false;
          }
          if("" == $("select[@name='obj.hvoipRegIdType']").val())
          {
              alert("终端表示类型不可为空。");
              $("select[@name='obj.hvoipRegIdType']").focus();
              return false;
          }*/
          if ("" == $("input[@name='obj.hvoipMgcIp']").val()) {
            alert("主用MGC地址不可为空。");
            $("input[@name='obj.hvoipMgcIp']").focus();
            return false;
          }
          else if (!reg_verify($("input[@name='obj.hvoipMgcIp']").val())) {
            alert("主用MGC地址不是合法IP地址。");
            $("input[@name='obj.hvoipMgcIp']").focus();
            return false;
          }
          if ("" == $("input[@name='obj.hvoipMgcPort']").val()) {
            alert("主用MGC端口不可为空。");
            $("input[@name='obj.hvoipMgcPort']").focus();
            return false;
          }
          if ("" == $("input[@name='obj.hvoipStandMgcIp']").val()) {
            alert("备用MGC地址不可为空。");
            $("input[@name='obj.hvoipStandMgcIp']").focus();
            return false;
          }
          else if (!reg_verify($("input[@name='obj.hvoipStandMgcIp']").val())) {
            alert("备用MGC地址不是合法IP地址。");
            $("input[@name='obj.hvoipStandMgcIp']").focus();
            return false;
          }
          if ("" == $("input[@name='obj.hvoipStandMgcPort']").val()) {
            alert("备用MGC端口不可为空。");
            $("input[@name='obj.hvoipStandMgcPort']").focus();
            return false;
          }
          /*if("" == $("input[@name='obj.hvoipVlanId']").val())
          {
              alert("VLANDID不可为空");
              $("input[@name='obj.hvoipVlanId']").focus();
              return false;
          }
          if("" == $("input[@name='obj.hvoipIpaddress']").val())
          {
              alert("ont的语音业务地址不可为空。");
              $("input[@name='obj.hvoipIpaddress']").focus();
              return false;
          }
          else if(!reg_verify($("input[@name='obj.hvoipIpaddress']").val())){
              alert("ont的语音业务地址不是合法IP地址。");
              $("input[@name='obj.hvoipIpaddress']").focus();
              return false;
          }
          if("" == $("input[@name='obj.hvoipIpmask']").val())
          {
              alert("ont的语音业务地址掩码不可为空。");
              $("input[@name='obj.hvoipIpmask']").focus();
              return false;
          }
          else if(!reg_verify($("input[@name='obj.hvoipIpmask']").val())){
              alert("ont的语音业务地址掩码不是合法IP地址。");
              $("input[@name='obj.hvoipIpmask']").focus();
              return false;
          }
          if("" == $("input[@name='obj.hvoipGateway']").val())
          {
              alert("ont语音业务网关不可为空。");
              $("input[@name='obj.hvoipGateway']").focus();
              return false;
          }
          else if(!reg_verify($("input[@name='obj.hvoipGateway']").val())){
              alert("ont的语音业务网关不是合法IP地址。");
              $("input[@name='obj.hvoipGateway']").focus();
              return false;
          }*/
        }
        if ("14" != sipServTypeId) {
          sipServTypeId = "";
        }

        var alertstr = "确认要提交";
        if (netServTypeId != "") {
          alertstr = alertstr + "宽带";
          if (hvoipServTypeId != "" || iptvServTypeId != ""  || sipServTypeId != "") {
            alertstr = alertstr + "、";
          }
        }
        if (iptvServTypeId != "") {
          alertstr = alertstr + "IPTV";
          if (hvoipServTypeId != "" || sipServTypeId != "") {
            alertstr = alertstr + "、";
          }
        }
        if (hvoipServTypeId != "") {
          alertstr = alertstr + "H248语音";
          if (sipServTypeId != "") {
            alertstr = alertstr + "、";
          }
        }
        if (sipServTypeId != "") {
          alertstr = alertstr + "SIP语音";
        }
        var r = confirm(alertstr + "业务吗 ？");
        if (r == false) {
          return;
        }
        //业务触发
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!doBusiness.action'/>";
        $.post(url, {
          "obj.deviceType": $("select[@name='obj.deviceType']").val(),
          "obj.cmdId": $("input[@name='obj.cmdId']").val(),
          "obj.authUser": $("input[@name='obj.authUser']").val(),
          "obj.authPwd": $("input[@name='obj.authPwd']").val(),
          "obj.userServTypeId": $("input[@name='obj.userServTypeId']").val(),
          "obj.userOperateId": $("input[@name='obj.userOperateId']").val(),
          "obj.dealDate": $("input[@name='obj.dealDate']").val(),
          "obj.userType": $("select[@name='obj.userType']").val(),
          "obj.loid": loidvalue.trim(),
          "obj.cityId": $("select[@name='obj.cityId']").val(),
          "obj.officeId": $("input[@name='obj.officeId']").val(),
          "obj.areaId": $("input[@name='obj.areaId']").val(),
          "obj.accessStyle": $("select[@name='obj.accessStyle']").val(),
          "obj.linkman": $("input[@name='obj.linkman']").val(),
          "obj.linkPhone": $("input[@name='obj.linkPhone']").val(),
          "obj.email": $("input[@name='obj.email']").val(),
          "obj.mobile": $("input[@name='obj.mobile']").val(),
          "obj.linkAddress": $("input[@name='obj.linkAddress']").val(),
          "obj.linkmanCredno": $("input[@name='obj.linkmanCredno']").val(),
          "obj.customerId": $("input[@name='obj.customerId']").val(),
          "obj.customerAccount": $("input[@name='obj.customerAccount']").val(),
          //"obj.customerPwd":$("input[@name='obj.customerPwd']").val(),
          "obj.specId": $("input[@name='obj.specId']").val(),

          "obj.netServTypeId": netServTypeId,
          "obj.netOperateId": $("input[@name='obj.netOperateId']").val(),
          "obj.netOltFactory": $("input[@name='obj.netOltFactory']").val(),
          "obj.netUsername": $("input[@name='obj.netUsername']").val(),
          "obj.netPasswd": $("input[@name='obj.netPasswd']").val(),
          "obj.netVlanId": $("input[@name='obj.netVlanId']").val(),
          "obj.netWanType": $("select[@name='obj.netWanType']").val(),
          "obj.netIpaddress": $("input[@name='obj.netIpaddress']").val(),
          "obj.netIpmask": $("input[@name='obj.netIpmask']").val(),
          "obj.netGateway": $("input[@name='obj.netGateway']").val(),
          "obj.netIpdns": $("input[@name='obj.netIpdns']").val(),
          "obj.netSpeed": $("input[@name='obj.netSpeed']").val(),
          "obj.netPort": netPortStr,
          //"obj.standNetIpdns":$("input[@name='obj.standNetIpdns']").val(),

          "obj.hvoipServTypeId": hvoipServTypeId,
          "obj.hvoipOperateId": $("input[@name='obj.hvoipOperateId']").val(),
          "obj.hvoipOltFactory": $("input[@name='obj.hvoipOltFactory']").val(),
          "obj.hvoipPhone": $("input[@name='obj.hvoipPhone']").val(),
          "obj.hvoipRegId": $("input[@name='obj.hvoipRegId']").val(),
          "obj.hvoipRegIdType": $("select[@name='obj.hvoipRegIdType']").val(),
          "obj.hvoipMgcIp": $("input[@name='obj.hvoipMgcIp']").val(),
          "obj.hvoipMgcPort": $("input[@name='obj.hvoipMgcPort']").val(),
          "obj.hvoipStandMgcIp": $("input[@name='obj.hvoipStandMgcIp']").val(),
          "obj.hvoipStandMgcPort": $("input[@name='obj.hvoipStandMgcPort']").val(),
          "obj.hvoipPort": $("select[@name='obj.hvoipPort']").val(),
          "obj.hvoipVlanId": $("input[@name='obj.hvoipVlanId']").val(),
          "obj.hvoipWanType": $("select[@name='obj.hvoipWanType']").val(),
          "obj.hvoipIpaddress": $("input[@name='obj.hvoipIpaddress']").val(),
          "obj.hvoipIpmask": $("input[@name='obj.hvoipIpmask']").val(),
          "obj.hvoipGateway": $("input[@name='obj.hvoipGateway']").val(),
          "obj.hvoipIpdns": $("input[@name='obj.hvoipIpdns']").val(),
          "obj.hvoipEid": $("input[@name='obj.hvoipEid']").val(),

          "obj.sipServTypeId": sipServTypeId,
          "obj.sipOperateId": $("input[@name='obj.sipOperateId']").val(),
          "obj.sipVoipPhone": $("input[@name='obj.sipVoipPhone']").val(),
          "obj.sipVoipUsername": $("input[@name='obj.sipVoipUsername']").val(),
          "obj.sipVoipPwd": $("input[@name='obj.sipVoipPwd']").val(),
          "obj.sipProxServ": $("input[@name='obj.sipProxServ']").val(),
          "obj.sipProxPort": $("input[@name='obj.sipProxPort']").val(),
          "obj.sipStandProxServ": $("input[@name='obj.sipStandProxServ']").val(),
          "obj.sipStandProxPort": $("input[@name='obj.sipStandProxPort']").val(),
          "obj.sipVoipPort": $("select[@name='obj.sipVoipPort']").val(),
          "obj.sipRegiServ": $("input[@name='obj.sipRegiServ']").val(),
          "obj.sipRegiPort": $("input[@name='obj.sipRegiPort']").val(),
          "obj.sipStandRegiServ": $("input[@name='obj.sipStandRegiServ']").val(),
          "obj.sipStandRegiPort": $("input[@name='obj.sipStandRegiPort']").val(),
          "obj.sipOutBoundProxy": $("input[@name='obj.sipOutBoundProxy']").val(),
          "obj.sipOutBoundPort": $("input[@name='obj.sipOutBoundPort']").val(),
          "obj.sipStandOutBoundProxy": $("input[@name='obj.sipStandOutBoundProxy']").val(),
          "obj.sipStandOutBoundPort": $("input[@name='obj.sipStandOutBoundPort']").val(),
          "obj.sipProtocol": $("select[@name='obj.sipProtocol']").val(),
          "obj.sipVlanId": $("input[@name='obj.sipVlanId']").val(),
          "obj.sipWanType": $("select[@name='obj.sipWanType']").val(),
          "obj.sipIpaddress": $("input[@name='obj.sipIpaddress']").val(),
          "obj.sipIpmask": $("input[@name='obj.sipIpmask']").val(),
          "obj.sipGateway": $("input[@name='obj.sipGateway']").val(),
          "obj.sipIpdns": $("input[@name='obj.sipIpdns']").val(),
          "obj.sipVoipUri": $("input[@name='obj.sipVoipUri']").val(),
          "obj.sipUserAgentDomain": $("input[@name='obj.sipUserAgentDomain']").val(),

          "obj.iptvServTypeId": iptvServTypeId,
          "obj.iptvOltFactory": $("input[@name='obj.iptvOltFactory']").val(),
          "obj.iptvWanType": $("select[@name='obj.iptvWanType']").val(),
          "obj.iptvVlanId": $("input[@name='obj.iptvVlanId']").val(),
          "obj.multicastVlan": $("input[@name='obj.multicastVlan']").val(),
          "obj.iptvUserName": $("input[@name='obj.iptvUserName']").val(),
          "obj.iptvPasswd": $("input[@name='obj.iptvPasswd']").val(),
          "obj.iptvDestIp": $("input[@name='obj.iptvDestIp']").val(),
          "obj.iptvDestMark": $("input[@name='obj.iptvDestMark']").val(),
          "obj.iptvPort": iptvPortStr

          //"obj.iptvNum"  : $("input[@name='obj.iptvNum']").val()

        }, function (ajax) {
          alert(ajax);
          //灰化按钮
          $("button[@name='subBtn']").attr("disabled", false);
          checkLoid();
        });
        //灰化按钮
        $("button[@name='subBtn']").attr("disabled", true);
      }

      //根据username动态选择
      function changeNetUsername (username) {
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!changeNetUsername.action'/>";
        var loid = $("input[@name='obj.loid']").val();
        var userType = $("select[@name='obj.userType']").val();
        //如果是新增不发送异步请求，获取数据
        var netUsername = $("input[@name='obj.netUsername']").val();
        $.post(url, {
          "obj.loid": loid,
          "obj.netServTypeId": "22",
          "obj.netUsername": netUsername,
          "obj.userType": userType
        }, function (ajax) {
          //显示原始数据
          var relt = ajax.split("|");
          $("#netServTypeId").attr("checked", true);
          $("input[@name='obj.netOperateId']").val("2");
          $("input[@name='obj.netUsername']").val(relt[1]);
          $("input[@name='obj.netPasswd']").val(relt[2]);
          $("input[@name='obj.netVlanId']").val(relt[3]);
          $("select[@name='obj.netWanType']").val(relt[4]);
          var netPortStrs = relt[5].split(",");
          var checkBoxBox = document.frm.netPort;
          for (var j = 0; j < netPortStrs.length; j++) {
            for (var i = 0; i < checkBoxBox.length; i++) {
              if (checkBoxBox[i].value == netPortStrs[j]) {
                checkBoxBox[i].checked = true;
              }
            }
          }
          showSheet('netServTypeId', 'internetBssSheet');
        });
      }

      //端口变更，动态更新此端口的业务信息，如果是新增不更新
      function changeVoipPort (voipPort, voipType) {
        var url = "<s:url value='/itms/service/bssSheetByHand4SXLT!changeVoipPort.action'/>";
        var loid = $("input[@name='obj.loid']").val();
        var userType = $("select[@name='obj.userType']").val();
        //如果是新增不发送异步请求，获取数据
        var sipProtocol = $("select[@name='obj.sipProtocol']").val();
        var sipSendPost = $("#isSipSendPost").val();
        var h248SendPost = $("#isH248SendPost").val();
        if ("sip" == voipType && "2" == sipSendPost) {
          $.post(url, {
            "obj.loid": loid,
            "obj.sipServTypeId": "14",
            "obj.sipVoipPort": voipPort,
            "obj.userType": userType,
            "obj.sipProtocol": sipProtocol
          }, function (ajax) {
            if ("-1" == ajax) {
              //操作类型：1，新增；字段置空置
              $("input[@name='obj.sipOperateId']").val("1");
              $("input[@name='obj.sipVoipPhone']").val("");
              $("input[@name='obj.sipVoipUsername']").val("");
              $("input[@name='obj.sipVoipPwd']").val("");
              $("input[@name='obj.sipProxServ']").val("");
              $("input[@name='obj.sipProxPort']").val("5060");
              $("input[@name='obj.sipStandProxServ']").val("");
              $("input[@name='obj.sipStandProxPort']").val("5060");
              $("input[@name='obj.sipRegiServ']").val("");
              $("input[@name='obj.sipRegiPort']").val("5060");
              $("input[@name='obj.sipStandRegiServ']").val("");
              $("input[@name='obj.sipStandRegiPort']").val("5060");
              $("input[@name='obj.sipOutBoundProxy']").val("");
              $("input[@name='obj.sipOutBoundPort']").val("5060");
              $("input[@name='obj.sipStandOutBoundProxy']").val("");
              $("input[@name='obj.sipStandOutBoundPort']").val("5060");
              $("select[@name='obj.sipProtocol']").val("-1");
              $("input[@name='obj.sipVlanId']").val("47");
              $("select[@name='obj.sipWanType']").val("4");
              $("input[@name='obj.sipIpaddress']").val("");
              $("input[@name='obj.sipIpmask']").val("");
              $("input[@name='obj.sipGateway']").val("");
              $("input[@name='obj.sipIpdns']").val("");
              $("input[@name='obj.sipVoipUri']").val("");
              $("input[@name='obj.sipUserAgentDomain']").val("ah.ctcims.cn");
            }
            else {
              //显示原始数据
              var relt = ajax.split("|");
              changeIms(relt[24]);
              $("input[@name='obj.sipOperateId']").val("2");
              $("input[@name='obj.sipVoipPhone']").val(relt[1]);
              $("input[@name='obj.sipVoipUsername']").val(relt[2]);
              $("input[@name='obj.sipVoipPwd']").val(relt[3]);
              $("input[@name='obj.sipProxServ']").val(relt[4]);
              $("input[@name='obj.sipProxPort']").val(relt[5]);
              $("input[@name='obj.sipStandProxServ']").val(relt[6]);
              $("input[@name='obj.sipStandProxPort']").val(relt[7]);
              $("input[@name='obj.sipRegiServ']").val(relt[8]);
              $("input[@name='obj.sipRegiPort']").val(relt[9]);
              $("input[@name='obj.sipStandRegiServ']").val(relt[10]);
              $("input[@name='obj.sipStandRegiPort']").val(relt[11]);
              $("input[@name='obj.sipOutBoundProxy']").val(relt[12]);
              $("input[@name='obj.sipOutBoundPort']").val(relt[13]);
              $("input[@name='obj.sipStandOutBoundProxy']").val(relt[14]);
              $("input[@name='obj.sipStandOutBoundPort']").val(relt[15]);
              $("input[@name='obj.sipVlanId']").val(relt[16]);
              $("select[@name='obj.sipWanType']").val(relt[17]);
              $("input[@name='obj.sipIpaddress']").val(relt[18]);
              $("input[@name='obj.sipIpmask']").val(relt[19]);
              $("input[@name='obj.sipGateway']").val(relt[20]);
              $("input[@name='obj.sipIpdns']").val(relt[21]);
              $("input[@name='obj.sipVoipUri']").val(relt[22]);
              $("input[@name='obj.sipUserAgentDomain']").val(relt[23]);
              $("select[@name='obj.sipProtocol']").val(relt[24]);
            }
          });
        }
        if ("h248" == voipType && "2" == h248SendPost) {
          $.post(url, {
            "obj.loid": loid,
            "obj.hvoipServTypeId": "14",
            "obj.sipVoipPort": voipPort,
            "obj.userType": userType,
            "obj.sipProtocol": "2"
          }, function (ajax) {
            if ("-1" == ajax) {
              //操作类型：1，新增；字段置空置
              var relt = ajax.split("|");
              $("input[@name='obj.hvoipOperateId']").val("1");
              $("input[@name='obj.hvoipMgcIp']").val("");
              $("input[@name='obj.hvoipMgcPort']").val("");
              $("input[@name='obj.hvoipStandMgcIp']").val("");
              $("input[@name='obj.hvoipStandMgcPort']").val("");
              $("input[@name='obj.hvoipVlanId']").val("");
              $("select[@name='obj.hvoipWanType']").val("-1");
              $("input[@name='obj.hvoipIpaddress']").val("");
              $("input[@name='obj.hvoipIpmask']").val("");
              $("input[@name='obj.hvoipGateway']").val("");

              changeStatic(-1, 'h248IP', 'h248Dns')
            }
            else {
              //显示原始数据
              var relt = ajax.split("|");
              $("input[@name='obj.hvoipOperateId']").val(relt[0]);
              $("input[@name='obj.hvoipMgcIp']").val(relt[4]);
              $("input[@name='obj.hvoipMgcPort']").val(relt[5]);
              $("input[@name='obj.hvoipStandMgcIp']").val(relt[6]);
              $("input[@name='obj.hvoipStandMgcPort']").val(relt[7]);
              $("input[@name='obj.hvoipVlanId']").val(relt[8]);
              $("select[@name='obj.hvoipWanType']").val(relt[9]);
              $("input[@name='obj.hvoipIpaddress']").val(relt[10]);
              $("input[@name='obj.hvoipIpmask']").val(relt[11]);
              $("input[@name='obj.hvoipGateway']").val(relt[12]);

              changeStatic(relt[9], 'h248IP', 'h248Dns');

            }
          });
        }
      }

      function doBusiness1 () {
        var netServTypeId = $("input[@name='obj.netServTypeId'][checked]").val();
        var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
        var sipServTypeId = $("input[@name='obj.sipServTypeId'][checked]").val();
        if ("22" != netServTypeId) {
          netServTypeId = "";
        }
        if ("15" != hvoipServTypeId) {

        }
        if ("14" != sipServTypeId) {
          sipServTypeId = "";
        }

        alert(netServTypeId);
        alert(hvoipServTypeId);
        alert(sipServTypeId);
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
                                    <input type="hidden" id="userServTypeId" name="obj.userServTypeId" value="20">
                                    <input type="hidden" id="userOperateId" name="obj.userOperateId" value="1">
                                    <input type="hidden" name="obj.cmdId" value="FROMWEB-0000001">
                                    <input type="hidden" name="obj.authUser" value="itms">
                                    <input type="hidden" name="obj.authPwd" value="123">
                                    <td colspan="4" class="green_title_left">
                                        用户资料工单
                                    </td>
                                </tr>
                                <tbody id="jirRuBssSheet">
                                <TR bgcolor="#FFFFFF">
                                    <!-- <TD width="15%" class=column align="right">用户类型：</TD>
                                    <TD width="35%" >

                                        <SELECT name="obj.userType" onChange="changeUserType(this.value)" class="bk" disabled>
                                        <OPTION value="-1">--请选择--</OPTION>
                                        <OPTION value="1">家庭网关</OPTION>
                                        <OPTION value="2">企业网关</OPTION>
                                        </SELECT>
                                        <font color="#FF0000">*</font>
                                    </TD> -->
                                    <input type="hidden" name="obj.userType" class=bk value="1">
                                    <TD width="15%" class=column align="right">联系人:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.linkman" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">受理时间：</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.dealDate" class=bk value="">
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">LOID:</TD>
                                    <TD width="35%">
                                        <input type="text" id="loid" name="obj.loid" class=bk value="">&nbsp;

                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <button type="button" name="subButton" onclick="checkLoid()">检测LOID是否存在</button>
                                        <!-- onblur="checkLoid()" -->
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">属地:</TD>
                                    <TD width="35%">
                                        <s:select list="cityList" name="obj.cityId"
                                                  headerKey="-1" headerValue="请选择属地" listKey="city_id"
                                                  listValue="city_name" value="cityId" cssClass="bk"></s:select>
                                        <font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">局向标示:</TD>
                                    <TD width="35%"><input type="text" name="obj.officeId" class=bk value=""></TD>
                                    <TD class=column align="right" nowrap width="15%">区域标示:</TD>
                                    <TD width="35%"><input type="text" name="obj.areaId" class=bk value=""></TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">终端接入类型:</TD>
                                    <TD width="35%">
                                        <SELECT name="obj.accessStyle" class="bk" disabled>
                                            <!-- <OPTION value="-1">--请选择--</OPTION>
                                            <OPTION value="1">ADSL</OPTION>
                                            <OPTION value="2">LAN</OPTION>
                                            <OPTION value="3">EPON</OPTION> -->
                                            <OPTION value="4">GPON</OPTION>
                                        </SELECT>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">联系电话:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.linkPhone" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">电子邮箱:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.email" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">手机:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.mobile" class=bk value="" maxlength="15">
                                    </TD>
                                    <TD width="15%" class=column align="right">地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.linkAddress" size="55" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">证件号码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.linkmanCredno" class=bk value="">
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">终端类型:</TD>
                                    <TD width="35%">
                                        <SELECT name="obj.deviceType" class="bk">
                                            <OPTION value="1">e8-b</OPTION>
                                            <OPTION value="2">e8-c</OPTION>
                                        </SELECT>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR id="egw_cust" bgcolor="#FFFFFF" style="display: none">
                                    <TD id="customerId" class=column align="right" nowrap width="15%">客户ID：</TD>
                                    <TD id="customerIdValue" width="35%">
                                        <input type="text" name="obj.customerId" class=bk value="">
                                        <font color="#FF0000">*</font>
                                        <button type="button" name="subButton" onclick="checkCustomerId()">检测客户ID是否存在
                                        </button>
                                    </TD>
                                    <TD id="customerId" class=column align="right" nowrap width="15%">客户名称：</TD>
                                    <TD id="customerIdValue" width="35%">
                                        <input type="text" name="obj.customerAccount" class=bk value="">
                                    </TD>
                                </TR>
                                </tbody>


                                <tr id="nettab" align="left" style="display:none">
                                    <td colspan="4" class="green_title_left">
                                        <input type="hidden" id="netOperateId" name="obj.netOperateId" value="1"/>
                                        <input type="checkbox" name="obj.netServTypeId" value="22" id="netServTypeId"
                                               onclick="showSheet('netServTypeId','internetBssSheet');"/>
                                        宽带业务工单
                                    </td>
                                </tr>
                                <tbody id="internetBssSheet" style="display:none">
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD width="15%" class=column align="right">OltFactory:</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="obj.netOltFactory" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">宽带帐号或专线接入号:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netUsername" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">宽带密码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netPasswd" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">VLANID:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netVlanId" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">上网方式：</TD>
                                    <TD width="35%">
                                        <SELECT name="obj.netWanType" id="objnetWanType" class="bk">
                                            <option value="-1">==请选择操作类型==</option>
                                            <option value="1">==桥接==</option>
                                            <option value="2">==路由==</option>
                                            <!--<option value="3">==STATIC==</option>-->
                                            <!--<option value="4">==DHCP==</option>-->
                                        </SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">绑定端口:</TD>
                                    <!-- <TD width="35%" colspan="3">
                                        <input type="text" name="obj.netPort" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font> -->
                                    <TD id="netTD1" width="35%" colspan="3">
                                        <input type="checkbox" name="netPort" value="L1" class=bk/>L1&nbsp;
                                        <input type="checkbox" name="netPort" value="L2" class=bk/>L2&nbsp;
                                        <input type="checkbox" name="netPort" value="L3" class=bk/>L3&nbsp;
                                        <input type="checkbox" name="netPort" value="L4" class=bk/>L4&nbsp;
                                        &nbsp;<font color="#FF0000">*
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD width="15%" class=column align="right">帐号速率(M/s):</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="obj.netSpeed" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR id="netip" bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">IP地址：</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netIpaddress" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">掩码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netIpmask" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR id="netDns" bgcolor="#FFFFFF" style="display:none">
                                    <TD class=column align="right" nowrap width="15%">网关:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netGateway" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">DNS:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.netIpdns" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" style="display:none">
                                    <TD width="15%" class=column align="right">用户IP类型:</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="obj.standNetIpdns" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                </tbody>


                                <tr id="iptvtab" align="left" style="display:none">
                                    <td colspan="4" class="green_title_left">
                                        <input type="hidden" id="iptvOperateId" name="obj.iptvOperateId" value="1"/>
                                        <input type="checkbox" name="obj.iptvServTypeId" value="21" id="iptvServTypeId"
                                               onclick="showSheet('iptvServTypeId','iptvBssSheet');"/>
                                        IPTV业务工单
                                    </td>
                                </tr>
                                <tbody id="iptvBssSheet" style="display:none">
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">上网方式：</TD>
                                    <TD width="35%" colspan="3">
                                        <SELECT name="obj.iptvWanType" id="objiptvWanType" class="bk">
                                            <!-- <option value="-1">==请选择操作类型==</option>-->
                                            <option value="1">==桥接==</option>
                                            <!--<option value="3">==STATIC==</option>-->
                                            <!--<option value="4">==DHCP==</option>-->
                                        </SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">VLANID:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.iptvVlanId" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">开通端口:</TD>
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


                                <tr id="h248tab" align="left" style="display:none">
                                    <td colspan="4" class="green_title_left">
                                        <input type="hidden" id="hvoipOperateId" name="obj.hvoipOperateId" value="1"/>
                                        <input type="hidden" name="isH248SendPost" id="isH248SendPost" value="0">
                                        <input type="checkbox" name="obj.hvoipServTypeId" value="15"
                                               id="hvoipServTypeId"
                                               onclick="showSheet('hvoipServTypeId','H248BssSheet');"/>
                                        H248语音业务工单
                                    </td>
                                </tr>
                                <tbody id="H248BssSheet" style="display:none">
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">终端物理标识:</TD>
                                    <TD width="35%">
                                        <select name="obj.hvoipPort" class="bk"
                                                onChange="changeVoipPort(this.value,'h248')">
                                            <option value="-1">==请选择==</option>
                                            <option value="A1">==A1==</option>
                                            <option value="A2">==A2==</option>
                                        </select>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD class=column align="right">EID:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipEid" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">主用MGC地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipMgcIp" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">主用MGC端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipMgcPort" class=bk value="2944">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">备用MGC地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipStandMgcIp" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">备用MGC端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipStandMgcPort" class=bk value="2944">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR id="h248IP" bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">IP地址：</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipIpaddress" class=bk value="">

                                    </TD>
                                    <TD width="15%" class=column align="right">掩码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipIpmask" class=bk value="">

                                    </TD>
                                </TR>
                                <TR id="h248Dns" bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">网关:</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="obj.hvoipGateway" class=bk value="">

                                    </TD>
                                    <!-- <TD width="15%" class=column align="right">DNS:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.hvoipIpdns" class=bk value="" disabled>
                                        &nbsp;<font color="#FF0000"></font>
                                    </TD> -->
                                </TR>
                                <!-- <TR bgcolor="#FFFFFF" >
                                    <TD class=column align="right" nowrap width="15%">上网方式:</TD>
                                    <TD width="35%" colspan="3">
                                        <select name="obj.hvoipWanType" class="bk" onChange="changeStatic(this.value,'h248IP','h248Dns')">
                                        <option value="-1">==请选择操作类型==</option>
                                        <option value="3">==STATIC==</option>
                                        <option value="4">==DHCP==</option>
                                    </select>
                                    &nbsp;&nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR> -->
                                </tbody>

                                <tr id="siptab" align="left">
                                    <td colspan="4" class="green_title_left">
                                        <input type="hidden" id="sipOperateId" name="obj.sipOperateId" value="1"/>
                                        <input type="hidden" name="isSipSendPost" id="isSipSendPost" value="0">
                                        <input type="checkbox" name="obj.sipServTypeId" value="14" id="sipServTypeId"
                                               onclick="showSheet('sipServTypeId','sipBssSheet');"/>
                                        SIP语音业务工单
                                    </td>
                                </tr>
                                <tbody id="sipBssSheet" style="display:none">
                                <tr bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">业务电话号码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipVoipPhone" class=bk value="">&nbsp;
                                        <font color="#FF0000">*</font>
                                    </TD>
                                    <TD class=column align="right" nowrap width="15%">语音端口:</TD>
                                    <TD width="35%">
                                        <select name="obj.sipVoipPort" class="bk"
                                                onChange="changeVoipPort(this.value,'sip')">
                                            <option value="-1">==请选择==</option>
                                            <option value="V1">==V1==</option>
                                            <option value="V2">==V2==</option>
                                        </select>
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </tr>
                                <TR bgcolor="#FFFFFF">
                                    <TD width="15%" class=column align="right">认证账号:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipVoipUsername" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">认证密码</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipVoipPwd" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">主用代理服务器:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipProxServ" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">主用代理服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipProxPort" class=bk value="5060">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">备用代理服务器地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandProxServ" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">备用代理服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandProxPort" class=bk value="5060">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">主用注册服务器:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipRegiServ" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">主用注册服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipRegiPort" class=bk value="5060">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">备用注册服务器地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandRegiServ" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">备用注册服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandRegiPort" class=bk value="5060">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>

                                <TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap>协议类型</TD>
                                    <TD colspan = "3">
                                        <select name="obj.sipProtocol" class="bk" onChange="changeIms(this.value)">
                                            <option value="-1">==请选择协议类型==</option>
                                            <option value="0">==IMS SIP==</option>
                                            <option value="1">==软交换 SIP==</option>
                                        </select>
                                        &nbsp;&nbsp;<font color="#FF0000">* </font>
                                    </TD>
                                </TR>
                                <!--<TR bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">上网方式:</TD>
                                    <TD colspan="3" width="35%" >
                                        <select name="obj.sipWanType" class="bk" onChange="changeStatic(this.value,'sipIP','sipDns')" >
                                        <option value="-1">==请选择操作类型==</option>
                                        <option value="3">==STATIC==</option>
                                        <option value="4">==DHCP==</option>
                                    </select>
                                    &nbsp;&nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR> -->
                                <TR id="sipIP" bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">IP地址：</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipIpaddress" class=bk value="">
                                    </TD>
                                    <TD width="15%" class=column align="right">掩码:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipIpmask" class=bk value="">
                                    </TD>
                                </TR>
                                <TR id="sipDns" bgcolor="#FFFFFF">
                                    <TD class=column align="right" nowrap width="15%">网关:</TD>
                                    <TD width="35%" colspan="3">
                                        <input type="text" name="obj.sipGateway" class=bk value="">
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="ims" style="display: none">
                                    <TD class=column align="right" nowrap>URI</TD>
                                    <TD colspan="3"><INPUT TYPE="text" NAME="obj.sipVoipUri" maxlength=20
                                                           class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="ims2" style="display: none">
                                    <TD class=column align="right" nowrap width="15%">主用绑定服务器:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipOutBoundProxy" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">主用绑定服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipOutBoundPort" class=bk value="5060">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                </TR>
                                <TR bgcolor="#FFFFFF" id="ims3" style="display: none">
                                    <TD class=column align="right" nowrap width="15%">备用绑定服务器地址:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandOutBoundProxy" class=bk value="">
                                        &nbsp;<font color="#FF0000">*</font>
                                    </TD>
                                    <TD width="15%" class=column align="right">备用绑定服务器端口:</TD>
                                    <TD width="35%">
                                        <input type="text" name="obj.sipStandOutBoundPort" class=bk value="5060">
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
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
</FORM>
</body>
</html>
<%@ include file="../../../foot.jsp" %>