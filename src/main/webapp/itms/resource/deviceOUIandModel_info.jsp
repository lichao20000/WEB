<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>OUI��ѯ�޸�</title>
    <%
        /**
         * OUI��ѯ�޸�ҳ��
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
        if (type == "1") {
          $('.change-device-type').show();
          if (deviceType == "1") {
            $('.stb-device').hide();
            $('.device').show();
            $("select[@name='vendorName']").find("option[value='-1']").attr("selected", true);
            $("select[@name='stb_vendor']").find("option[value='-1']").attr("selected", true);
            $("select[@name='deviceModel']").find("option[value='-1']").attr("selected", true);
            $("select[@name='stb_device_model']").find("option[value='-1']").attr("selected", true);
          } else if(deviceType == "4") {
            $('.stb-device').show();
            $('.device').hide();
          }
          document.all("actLabel").innerHTML = "���";
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
            vendorName = '--'+vendorName+'--';
            deviceModel = '--'+deviceModel+'--';
            // $("select[name='vendorName'] option:contains('"+vendorName+"')").attr('selected', true);
            // $("select[name='deviceModel'] option:contains('"+deviceModel+"')").attr('selected', true);
            $("select[name='vendorName']").find("option[value='-1']").attr("selected", true);
            $("select[name='device_model_id']").find("option[value='-1']").attr("selected", true);
          } else if(deviceType == "4") {
            $('.stb-device').show();
            $('.device').hide();
            vendorName = '==' +vendorName + '==';
            deviceModel = '=='+deviceModel+'==';
            // $("select[name='stb_vendor'] option:contains('"+vendorName+"')").attr('selected', true);
            // $("select[name='stb_device_model'] option:contains('"+deviceModel+"')").attr('selected', true);
            $("select[name='stb_vendor']").find("option[value='-1']").attr("selected", true);
            $("select[name='stb_device_model']").find("option[value='-1']").attr("selected", true);
          }
          document.all("actLabel").innerHTML = "�༭";
          $("input[@name='type']").val(type);
          $("input[@name='id']").val(id);
          $("select[@name='vendorName']").val();
          $("input[@name='ouiId']").val(ouiId);
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
        var ouiId = $("input[@name='ouiId']").val();
        if (ouiId == null || $.trim(ouiId) == "") {
          alert("OUI����Ϊ�գ�");
          $("#save").attr('disabled', false);
          return false;
        }
        var url = "";
        var deviceType = "";
        if ("1" == type) {
          url = "<s:url value='/itms/resource/deviceOUIInfoACT!addOUI.action'/>";
          deviceType = $("select[@name='device-type']").find("option:selected").val();
        } else if ("2" == type) {
          url = "<s:url value='/itms/resource/deviceOUIInfoACT!editOUI.action'/>";
          deviceType = deviceGwType;
        }
        if (deviceType == "1") {
          vendorName = $("select[@name='vendorName']").find("option:selected").text().slice(2,-2);
          vendorNameValue = $("select[@name='vendorName']").find("option:selected").val();
          deviceModel = $("select[@name='device_model_id']").find("option:selected").text().slice(2,-2);
          deviceModelValue = $("select[@name='device_model_id']").find("option:selected").val();
        }else{
          vendorName = $("select[@name='stb_vendor']").find("option:selected").text().slice(2,-2);
          vendorNameValue = $("select[@name='stb_vendor']").find("option:selected").val();
          deviceModel = $("select[@name='stb_device_model']").find("option:selected").text().slice(2,-2);
          deviceModelValue = $("select[@name='stb_device_model']").find("option:selected").val();
        }
        if (vendorNameValue == null || $.trim(vendorNameValue) == "" || vendorNameValue == -1) {
          alert("���̲���Ϊ�գ�");
          $("#save").attr('disabled', false);
          return false;
        }
        var ouiDesc = $("input[@name='ouiDesc']").val();
        if (ouiDesc == null || $.trim(ouiDesc) == "") {
          alert("OUI��������Ϊ�գ�");
          $("#save").attr('disabled', false);
          return false;
        }

        console.log(deviceModelValue + ':' + deviceModel);
        if (deviceModelValue == null || $.trim(deviceModelValue) == "" || deviceModelValue == -1) {
          alert("�ͺŲ���Ϊ�գ�");
          $("#save").attr('disabled', false);
          return false;
        }
        var remark = $("input[@name='remark']").val();

        $.post(url, {
          id: id,
          vendorName: vendorName,
          ouiId: ouiId,
          ouiDesc: ouiDesc,
          remark: remark,
          deviceModel: deviceModel,
          deviceType: deviceType
        }, function (ajax) {
          alert(ajax);
          var form = window.document.getElementById("form");
          form.action = "<s:url value="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action"/>";
          form.submit();
          $("#save").attr('disabled', false);
          $("table[@id='addedit']").hide();
        });
      }

      function gwShare_change_select_stb (type, selectvalue) {
        switch (type) {
          case "stbVendor":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
            $.post(url, {}, function (ajax) {
              gwShare_parseMessage(ajax, $("select[@name='stb_vendor']"), selectvalue);
              //$("select[@name='vendor']").html("<option value='-1'>==����ѡ���豸����==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
            });
            break;
          case "stbDeviceModel":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
            var vendorId = $("select[@name='stb_vendor']").val();
            if ("-1" == vendorId) {
              $("select[@name='stb_device_model']").html("<option value='-1'>==����ѡ���豸����==</option>");
              //$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
              break;
            }
            $.post(url, {
              gwShare_vendorId: vendorId
            }, function (ajax) {
              //$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
              gwShare_parseMessage(ajax, $("select[@name='stb_device_model']"), selectvalue);
            });
            break;
          default:
            alert("δ֪��ѯѡ�");
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

        option = "<option value='-1' selected>==��ѡ��==</option>";
        field.append(option);
        for (var i = 0; i < lineData.length; i++) {
          var oneElement = lineData[i].split("$");
          var xValue = oneElement[0];
          var xText = oneElement[1];
          if (selectvalue == xValue) {
            flag = false;
            //����ÿ��value��text��ǵ�ֵ����һ��option����
            option = "<option value='" + xValue + "' selected>==" + xText + "==</option>";
          } else {
            //����ÿ��value��text��ǵ�ֵ����һ��option����
            option = "<option value='" + xValue + "'>==" + xText + "==</option>";
          }
          try {
            field.append(option);
          } catch (e) {
            alert("�豸�ͺż���ʧ�ܣ�");
          }
        }
        if (flag) {
          field.attr("value", "-1");
        }
      }

      //** iframe�Զ���Ӧҳ�� **//
      //������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
      //�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
      //����iframe��ID
      var iframeids = ["dataForm"]

      //����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
      var iframehide = "yes"

      function dyniframesize () {
        var dyniframe = new Array()
        for (i = 0; i < iframeids.length; i++) {
          if (document.getElementById) {
            //�Զ�����iframe�߶�
            dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
            if (dyniframe[i] && !window.opera) {
              dyniframe[i].style.display = "block"
              //����û����������NetScape
              if (dyniframe[i].contentDocument
                && dyniframe[i].contentDocument.body.offsetHeight)
                dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
              //����û����������IE
              else if (dyniframe[i].Document
                && dyniframe[i].Document.body.scrollHeight)
                dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
            }
          }
          //�����趨�Ĳ���������֧��iframe�����������ʾ����
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
        gwShare_change_select_stb('stbVendor', '-1');
        changeType()
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
    </script>
</head>
<body>
<form id="form" name="selectForm" method="post"
      action="<s:url value='/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action'/>"
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
                            OUI��Ϣ����
                        </td>
                        <td><img src="<s:url value="/images/attention_2.gif"/>"
                                 width="15" height="12"/> �豸OUI��Ϣ���
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table class="querytable">

                    <TR>
                        <th colspan="4">�豸OUI��ѯ</th>
                    </TR>
                    <TR>
                        <TD class="column" width='15%' align="right">OUI</TD>
                        <TD width="35%">
                            <input type="text" name="oui" id="oui" value="" maxlength=50 class=bk>
                        </TD>
                        <TD class=column width="15%" align='right'>����</TD>
                        <TD width="35%">
                            <s:select list="vendorMap" name="vendor_name" id="vendor_name"
                                      headerKey="0" headerValue="��ѡ����" listKey="vendor_name"
                                      listValue="vendor_name" cssClass="bk">
                            </s:select>
                        </TD>
                    </TR>

                    <TR>
                        <td colspan="4" align="right" class=foot>

                            <s:if test='%{userName=="admin" || userName=="szxadmin"}'>
                                <button onclick="addeditOUI('','','','','','1','')">&nbsp;��&nbsp;&nbsp;��&nbsp;</button>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                            </s:if>
                            <button onclick="query()">&nbsp;��&nbsp;&nbsp;ѯ&nbsp;</button>
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
                        <th colspan="4"><SPAN id="actLabel">���</SPAN>OUI��Ϣ
                            <input type="hidden" name="type" id="type" value="1"/>
                            <input type="hidden" name="id" id="id" value=""/>
                        </th>
                    </tr>
                    <tr class = "change-device-type">
                        <td class="column" align="center">
                            �豸���ͣ�
                        </td>
                        <td>
                            <select name="device-type" class="bk" onchange="changeType()">
                                <option value="1" selected="">==��è�豸==</option>
                                <option value="4">==�������豸==</option>
                            </select>
                        </td>
                    </tr>
                    <TR class="device">
                        <TD class="column" align="center" width="15%">��è����</TD>
                        <TD>
                            <%=str_VendorList%>
                            <font color="red">*</font>
                        </TD>
                        <TD class="column" align="center" width="15%">��è�ͺ�</TD>
                        <TD>
                            <div id="div_device_model_id">
                                <select name="deviceModel" class="bk">
                                    <option value="-1">
                                        --����ѡ����--
                                    </option>
                                </select>
                            </div>
                            <font color="red">*</font>
                        </TD>
                    </TR>
                    <TR bgcolor="#FFFFFF" class="stb-device">
                        <TD align="right" class=column width="15%">�����г���</TD>
                        <TD align="left" width="35%"><select name="stb_vendor" class="bk"
                                                             onchange="gwShare_change_select_stb('stbDeviceModel','-1')">
                        </select>
                            <font color="red">*</font>
                        </TD>
                        <TD align="right" class=column width="15%">�������ͺ�</TD>
                        <TD width="35%"><select name="stb_device_model" class="bk">
                            <option value="-1">==��ѡ����==</option>
                        </select>
                            <font color="red">*</font>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="column" align="center" width="15%">OUI</TD>
                        <TD width="35%"><input type="text" name="ouiId"
                                               id="ouiId" class="bk" value="" size="40" maxlength="20"/>
                            <font color="red">*</font>
                        </TD>
                        <TD class="column" align="center" width="15%">����</TD>
                        <TD width="35%"><input type="text" name="ouiDesc"
                                               id="ouiDesc" class="bk" value="" size="40" maxlength="20">
                            <font color="red">*</font>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="column" align="center" width="15%">��ע</TD>
                        <TD width="35%" colspan="3"><input type="text" name="remark"
                                                           id="remark" class="bk" value="" size="40" maxlength="20"/>
                        </TD>

                    </TR>
                    <tr>
                        <td colspan="4" class="foot" align="right">
                            <div class="right">
                                <button onclick="ExecMod()" id="save">����</button>
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