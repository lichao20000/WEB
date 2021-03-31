<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>�豸��ѯ</title>
    <link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
    <link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
    <script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
    <SCRIPT LANGUAGE="JavaScript">

      var has_showModalDialog = !!window.showModalDialog;//����һ��ȫ�ֱ����ж��Ƿ���ԭ��showModalDialog����
      if (!has_showModalDialog && !!(window.opener)) {
        window.onbeforeunload = function () {
          window.opener.hasOpenWindow = false;        //�����ر�ʱ����opener�����Ӵ����Ѿ��ر�
        }
      }
      //����window.showModalDialog�����������
      if (window.showModalDialog == undefined) {
        window.showModalDialog = function (url, mixedVar, features) {
          if (window.hasOpenWindow) {
            window.myNewWindow.focus();
          }
          window.hasOpenWindow = true;
          if (mixedVar) var mixedVar = mixedVar;
          //��window.showmodaldialog �� window.open ������һ�������Է�װ��ʱ��������ȥ��ʽ��һ�²���
          if (features) var features = features.replace(/(dialog)|(px)/ig, "").replace(/;/g, ',').replace(/\:/g, "=");
          var left = (window.screen.width - parseInt(features.match(/width[\s]*=[\s]*([\d]+)/i)[1])) / 2;
          var top = (window.screen.height - parseInt(features.match(/height[\s]*=[\s]*([\d]+)/i)[1])) / 2;
          window.myNewWindow = window.open(url, "_blank", features);
        }
      }

      function gwShare_queryField_selected (value) {
        $("input[@name='gwShare_queryField_temp']").val(value.value);
      }

      /*------------------------------------------------------------------------------
      //������:		queryChange
      //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
      //����  :	���ݴ���Ĳ���������ʾ�Ľ���
      //����ֵ:		��������
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      function gwShare_queryDevice () {
        var width = 800;
        var height = 450;
        var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
        var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceListByStb.action"/>?gwShare_queryResultType=" +
          gwShare_queryResultType;
        var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
        url = url + "&gwShare_queryType=" + gwShare_queryType;
        if ("1" == gwShare_queryType) {
          var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
          var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
          if (trim(gwShare_queryParam) == "") {
            alert("�������ѯ������");
            return;
          }
          url = url + "&gwShare_queryField=" + gwShare_queryField;
          url = url + "&gwShare_queryParam=" + gwShare_queryParam;
        } else if ("2" == gwShare_queryType) {
          var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
          var gwShare_onlineStatus = $("select[@name='gwShare_onlineStatus']").val();
          var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
          var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
          var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
          var gwShare_bindType = $("select[@name='gwShare_bindType']").val();
          var gwShare_deviceSerialnumber = $("input[@name='gwShare_deviceSerialnumber']").val();
          var gwShare_matchSQL = $.trim($("textarea[@name='gwShare_matchSQL1']").val());
          var gwShare_platform = $("select[@name='gwShare_platform']").val();
          gwShare_matchSQL = gwShare_matchSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
          url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
          url = url + "&gwShare_cityId=" + gwShare_cityId;
          url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
          url = url + "&gwShare_vendorId=" + gwShare_vendorId;
          url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
          url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
          url = url + "&gwShare_bindType=" + gwShare_bindType;
          url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
          url = url + "&gwShare_platform=" + gwShare_platform;
        } else {
          var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
          if ("" == gwShare_fileName) {
            alert("�����ϴ��ļ���");
            return;
          }
          url = url + "&gwShare_fileName=" + gwShare_fileName;
          //��ҳ���Ǻӱ���ͨ����
          if ($("input[@name='hblt_SpeedTest_flg']").val() == "true") {
            var url = "<s:url value="/gwms/resource/batchHttpTest!saveUpFile.action"/>";

            $.post(url, {
              gwShare_fileName: gwShare_fileName
            }, function (ajax) {
              $("input[@name='fileName_st']").val(ajax);
              alert("������ɣ�");
              return;
            });
            $("input[@name='deviceIds']").val("0");
            return;
          }
        }
        var gwShare_RecordNum = $("input[@name='gwShare_RecordNum']").val();
        url = url + "&gwShare_RecordNum=" + gwShare_RecordNum;
        var gwShare_OrderByUpdateDate = $("input[@name='gwShare_OrderByUpdateDate']").val();
        url = url + "&gwShare_OrderByUpdateDate=" + gwShare_OrderByUpdateDate;
        url = url + "&refresh=" + new Date().getTime();
        var returnVal = window.showModalDialog(url, '', 'dialogWidth=' + width + 'px;dialogHeight=' + height +
          'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
        if (typeof(returnVal) == 'undefined') {
          return;
        } else {
          deviceResult(returnVal);
        }
      }

      /*------------------------------------------------------------------------------
      //������:		��ʼ��������ready��
      //����  :	��
      //����  :	��ʼ�����棨DOM��ʼ��֮��
      //����ֵ:
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      $(function () {
        var queryType = "<s:property value="queryType"/>";
        var queryResultType = "<s:property value="queryResultType"/>";
        var startQuery = "<s:property value="startQuery"/>";
        if ("" != queryType && null != queryType) {
          $("input[@name='gwShare_queryType']").val(queryType);
        }
        if ("" != queryResultType && null != queryResultType) {
          $("input[@name='gwShare_queryResultType']").val(queryResultType);
        }
        gwShare_queryChange(queryType);

        var str = Array($("input[@name='gwShare_queryParam']"),
          $("div[@id='gwShare_msgdiv']"),
          $("input[@name='gwShare_queryField_temp']"));

        //������������������ʱȥ������Ӧƥ��
        //$.autoMatch3("<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceSn.action'/>",str,"#");
      });

      /*------------------------------------------------------------------------------
      //������:		��дreset
      //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3�������ѯ
      //����  :	��ҳ���������
      //����ֵ:		ҳ������
      //˵��  :
      //����  :	Create 2010-4-26 of By qxq
      ------------------------------------------------------------------------------*/
      function gwShare_revalue () {
        var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
        if ("1" == gwShare_queryType) {
          $("input[@name='gwShare_queryField']").get(0).checked = true;
          $("input[@name='gwShare_queryParam']").val("");
        } else if ("2" == gwShare_queryType) {
          $("select[@name='gwShare_cityId']").attr("value", '-1')
          $("select[@name='gwShare_onlineStatus']").attr("value", '-1')
          $("select[@name='gwShare_vendorId']").attr("value", '-1')
          $("select[@name='gwShare_bindType']").attr("value", '-1')
          $("input[@name='gwShare_deviceSerialnumber']").val("");
          gwShare_change_select('deviceModel', '-1');
        }
      }

      /*------------------------------------------------------------------------------
      //������:		queryChange
      //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
      //����  :	���ݴ���Ĳ���������ʾ�Ľ���
      //����ֵ:		��������
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      /* function gwShare_queryChange(change){
          var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
          var gwShare_import_value = $("input[@name='gwShare_import_value']").val();
          switch (change){
              case "2":
                  alert(2);
                  $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
                  $("input[@name='gwShare_queryType']").val("2");
                  $("button[@name='gwShare_jiadan']").css("display","");
                  $("button[@name='gwShare_gaoji']").css("display","none");
                  $("input[@name='gwShare_import']").css("display",gwShare_import_value);
                  $("tr[@id='gwShare_tr11']").css("display","none");
                  $("tr[@id='gwShare_tr12']").css("display","none");
                  $("tr[@id='gwShare_tr21']").css("display","");
                  $("tr[@id='gwShare_tr22']").css("display","");
                  $("tr[@id='gwShare_tr23']").css("display","");
                  $("tr[@id='gwShare_tr24']").css("display","");
                  $("tr[@id='gwShare_tr31']").css("display","none");
                  $("tr[@id='gwShare_tr32']").css("display","none");
                  $("button[@name='gwShare_queryButton']").val(" �� ѯ ");
                  //���������
                  gwShare_change_select("city","-1");
                  gwShare_change_select("vendor","-1");
                  break;
              case "3":
                  alert(3);
                  $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
                  $("input[@name='gwShare_queryType']").val("3");
                  $("input[@name='gwShare_jiadan']").css("display","");
                  $("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
                  $("input[@name='gwShare_import']").css("display","");
                  $("tr[@id='gwShare_tr11']").css("display","none");
                  $("tr[@id='gwShare_tr12']").css("display","none");
                  $("tr[@id='gwShare_tr21']").css("display","none");
                  $("tr[@id='gwShare_tr22']").css("display","none");
                  $("tr[@id='gwShare_tr23']").css("display","none");
                  $("tr[@id='gwShare_tr24']").css("display","none");
                  $("tr[@id='gwShare_tr31']").css("display","");
                  $("tr[@id='gwShare_tr32']").css("display","");
                  $("button[@name='gwShare_queryButton']").val("�����ļ�");
                  break;
              case "1":
                  alert(3);
                  $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
                  $("input[@name='gwShare_queryType']").val("1");
                  $("input[@name='gwShare_jiadan']").css("display","none");
                  $("input[@name='gwShare_gaoji']").css("display","");
                  $("input[@name='gwShare_import']").css("display",gwShare_import_value);
                  $("tr[@id='gwShare_tr11']").css("display","");
                  $("tr[@id='gwShare_tr12']").css("display","");
                  $("tr[@id='gwShare_tr21']").css("display","none");
                  $("tr[@id='gwShare_tr22']").css("display","none");
                  $("tr[@id='gwShare_tr23']").css("display","none");
                  $("tr[@id='gwShare_tr24']").css("display","none");
                  $("tr[@id='gwShare_tr31']").css("display","none");
                  $("tr[@id='gwShare_tr32']").css("display","none");
                  $("button[@name='gwShare_queryButton']").val(" �� ѯ ");
                  break;
              default:
                  break;
          }
      } */
      function gwShare_queryChange (change) {
        var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
        switch (change) {
          case "2":
            $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
            $("input[@name='gwShare_queryType']").val("2");
            $("input[@name='gwShare_jiadan']").css("display", "");
            $("input[@name='gwShare_gaoji']").css("display", "none");
            $("input[@name='gwShare_import']").css("display", "");
            $("tr[@id='gwShare_tr11']").css("display", "none");
            $("tr[@id='gwShare_tr12']").css("display", "none");
            $("tr[@id='gwShare_tr21']").css("display", "");
            $("tr[@id='gwShare_tr22']").css("display", "");
            $("tr[@id='gwShare_tr23']").css("display", "");
            $("tr[@id='gwShare_tr24']").css("display", "");
            $("tr[@id='gwShare_tr31']").css("display", "none");
            $("tr[@id='gwShare_tr32']").css("display", "none");
            $("input[@name='gwShare_queryButton']").val(" �� ѯ ");
            //���������
            gwShare_change_select("city", "-1");
            gwShare_change_select("vendor", "-1");
            break;
          case "3":
            $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
            $("input[@name='gwShare_queryType']").val("3");
            $("input[@name='gwShare_jiadan']").css("display", "");
            $("input[@name='gwShare_gaoji']").css("display", gwShare_gaoji_value);
            $("input[@name='gwShare_import']").css("display", "none");
            $("tr[@id='gwShare_tr11']").css("display", "none");
            $("tr[@id='gwShare_tr12']").css("display", "none");
            $("tr[@id='gwShare_tr21']").css("display", "none");
            $("tr[@id='gwShare_tr22']").css("display", "none");
            $("tr[@id='gwShare_tr23']").css("display", "none");
            $("tr[@id='gwShare_tr24']").css("display", "none");
            $("tr[@id='gwShare_tr31']").css("display", "");
            $("tr[@id='gwShare_tr32']").css("display", "");
            $("input[@name='gwShare_queryButton']").val("�����ļ�");
            break;
          case "1":
            $("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
            $("input[@name='gwShare_queryType']").val("1");
            $("input[@name='gwShare_jiadan']").css("display", "none");
            $("input[@name='gwShare_gaoji']").css("display", gwShare_gaoji_value);
            $("input[@name='gwShare_import']").css("display", "");
            $("tr[@id='gwShare_tr11']").css("display", "");
            $("tr[@id='gwShare_tr12']").css("display", "");
            $("tr[@id='gwShare_tr21']").css("display", "none");
            $("tr[@id='gwShare_tr22']").css("display", "none");
            $("tr[@id='gwShare_tr23']").css("display", "none");
            $("tr[@id='gwShare_tr24']").css("display", "none");
            $("tr[@id='gwShare_tr31']").css("display", "none");
            $("tr[@id='gwShare_tr32']").css("display", "none");
            $("input[@name='gwShare_queryButton']").val(" �� ѯ ");
            break;
          default:
            break;
        }
      }

      /*------------------------------------------------------------------------------
      //������:		gwShare_setGaoji
      //����  :
      //����  :	���ظ߼���ѯ
      //����ֵ:
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      function gwShare_setImport () {
        $("input[@name='gwShare_import_value']").val("");
        $("input[@name='gwShare_import']").css("display", "");
      }

      /*------------------------------------------------------------------------------
      //������:		deviceSelect_change_select
      //����  :	type
                      vendor      �����豸����
                      deviceModel �����豸�ͺ�
                      devicetype  �����豸�汾
      //����  :	����ҳ������̡��ͺż�����
      //����ֵ:
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      function gwShare_change_select (type, selectvalue) {
        switch (type) {
          case "city":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
            $.post(url, {}, function (ajax) {
              gwShare_parseMessage(ajax, $("select[@name='gwShare_cityId']"), selectvalue);
            });
            break;
          case "vendor":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
            $.post(url, {}, function (ajax) {
              gwShare_parseMessage(ajax, $("select[@name='gwShare_vendorId']"), selectvalue);
              $("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
              $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
            });
            break;
          case "deviceModel":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
            var vendorId = $("select[@name='gwShare_vendorId']").val();
            if ("-1" == vendorId) {
              $("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
              $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
              break;
            }
            $.post(url, {
              gwShare_vendorId: vendorId
            }, function (ajax) {
              gwShare_parseMessage(ajax, $("select[@name='gwShare_deviceModelId']"), selectvalue);
              $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
            });
            break;
          case "devicetype":
            var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
            var vendorId = $("select[@name='gwShare_vendorId']").val();
            var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
            if ("-1" == deviceModelId) {
              $("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
              break;
            }
            $.post(url, {
              gwShare_vendorId: vendorId,
              gwShare_deviceModelId: deviceModelId
            }, function (ajax) {
              gwShare_parseMessage(ajax, $("select[@name='gwShare_devicetypeId']"), selectvalue);
            });
            break;
          default:
            alert("δ֪��ѯѡ�");
            break;
        }
      }

      /*------------------------------------------------------------------------------
      //������:		deviceSelect_parseMessage
      //����  :	ajax
                      ������XXX$XXX#XXX$XXX
                  field
                      ��Ҫ���ص�jquery����
      //����  :	����ajax���ز���
      //����ֵ:
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      //������ѯ�豸�ͺŷ���ֵ�ķ���
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

      /*------------------------------------------------------------------------------
      //������:		gwShare_setGaoji
      //����  :
      //����  :	���ظ߼���ѯ
      //����ֵ:
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      function gwShare_setGaoji () {
        $("input[@name='gwShare_gaoji_value']").val("");
        $("button[@name='gwShare_gaoji']").css("display", "");
      }

      /*------------------------------------------------------------------------------
      //������:		trim
      //����  :	str �������ַ���
      //����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
      //����ֵ:		trim��str��
      //˵��  :
      //����  :	Create 2009-12-25 of By qxq
      ------------------------------------------------------------------------------*/
      function trim (str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
      }

      function do_query () {
        $("div[@id='QueryData']").html("");
        if (!do_test()) {
          return;
        }
        setTimeout("gwShare_queryDevice()", 2000);
      }

      //��֤��������ĳ����Ƿ�Ϸ�
      function do_test () {
        //��ȡ��������ݣ�trimһ��
        //var gwShare_queryParam = document.gwShare_selectForm.gwShare_queryParam.value;
        var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
        //$.trim($("input[@name='gwShare_queryParam']").val());
        gwShare_queryParam = $.trim(gwShare_queryParam);

        //�򵥲�ѯ������������  add by zhangchy 2011-09-21 -----begin ---------------
        var title = document.getElementById("gwShare_thTitle").innerHTML;

        if (title == "�� �� �� ѯ") {
          if (0 == gwShare_queryParam.length) {
            alert("�������ѯ������");
            //document.gwShare_selectForm.gwShare_queryParam.focus();
            $("input[@name='gwShare_queryParam']").focus();
            return false;
          }
        } else {
          if (title == "�� �� �� ѯ" || title == "�� �� �� �� �� ѯ") {
            return true;
          }

          // �߼���ѯ���������豸SN��֧��
          var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
          var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
          var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
          var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
          <%-- 	var gwShare_isBatch = <%=isBatch %>; --%>
          var isShowGaoJi = $("input[@name='isShowGJ']").val();

          /* if("1"!=isShowGaoJi){
              if("1"==gwShare_isBatch){
                  if(parseInt(gwShare_devicetypeId) == -1){
                      alert("����ѡ���豸�汾��");
                      return false;
                  }
              }
          } */

          gwShare_spellSQL();
        }
        // --------  add by zhangchy 2011-09-21 ----- end ---------------

        //��ȡѡ�������
        var gwShare_queryFields = document.getElementsByName("gwShare_queryField");

        if (title == "�� �� �� ѯ") {
          if (gwShare_queryFields[0].checked) {
            if (gwShare_queryParam.length < 6 && gwShare_queryParam.length > 0) {
              alert("�������������6λ�豸���кŽ��в�ѯ��");
              document.gwShare_selectForm.gwShare_queryParam.focus();
              return false;
            }
          }
          //"�豸IP"��ѡ��
          else if (gwShare_queryFields[2].checked) {
            if (!reg_verify(gwShare_queryParam)) {
              alert("������Ϸ���IP��ַ��");
              document.gwShare_selectForm.gwShare_queryParam.focus();
              return false;
            }
          }//"�û��ʺ�"��ѡ��
          else if (gwShare_queryFields[1].checked) {
            if (gwShare_queryParam.length < 6 && gwShare_queryParam.length > 0) {
              alert("�������������6λLOID���в�ѯ��");
              document.gwShare_selectForm.gwShare_queryParam.focus();
              return false;
            }
          }
        }
        //"�豸���к�"��ѡ��

//	gwShare_spellSQL();
        //���û���쳣�������ѯ
        return true;
      }

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

      function gwShare_spellSQL () {
        var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!queryDeviceListSQL.action'/>";
        var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
        var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
        var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());

        //var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
        var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
        var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
        var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
        var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
        var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
        var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
        var gwShare_OrderByUpdateDate = $("input[@name='gwShare_OrderByUpdateDate']").val();

        var refresh = new Date().getTime();
        var gw_type = 4;

        var iscqsoft1 = "";
        try {
          if (true == iscqsoft) {
            gwShare_cityId = cqSoftCitys;
            iscqsoft1 = "true";
          }
        }
        catch (e) {
        }
        $.post(url, {
          gwShare_queryResultType: gwShare_queryResultType,
          gwShare_queryType: gwShare_queryType,
          gwShare_cityId: gwShare_cityId,
          //gwShare_nextCityId:gwShare_nextCityId,
          gwShare_onlineStatus: gwShare_onlineStatus,
          gwShare_vendorId: gwShare_vendorId,
          gwShare_deviceModelId: gwShare_deviceModelId,
          gwShare_devicetypeId: gwShare_devicetypeId,
          gwShare_bindType: gwShare_bindType,
          gwShare_deviceSerialnumber: gwShare_deviceSerialnumber,
          refresh: refresh,
          gw_type: gw_type,
          iscqsoft: iscqsoft1,
          gwShare_OrderByUpdateDate: gwShare_OrderByUpdateDate
        }, function (ajax) {
          $("textarea[@name='gwShare_matchSQL1']").val(ajax);
        });
      }


    </SCRIPT>
    <style>
        span {
            position: static;
            border: 0;
        }
    </style>
</head>
<form name="gwShare_selectForm" action="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>"
      target="dataForm">
    <input type="hidden" name="gwShare_queryType" value="1"/>
    <input type="hidden" name="gwShare_queryResultType" value="checkbox"/>
    <input type="hidden" name="gwShare_queryField_temp" value="deviceSn"/>
    <input type="hidden" name="gwShare_gaoji_value" value="none"/>
    <input type="hidden" name="gwShare_import_value" value="none"/>
    <input type="hidden" name="gwShare_OrderByUpdateDate" value="1"/><!-- 1������¼�����ϱ�ʱ������0������ -->
    <input type="hidden" name="gwShare_RecordNum" value="3"/><!-- Ĭ��3����¼��0Ϊ��ѯȫ�����������ô�С������50 -->

    <TABLE width="100%" class="querytable" align="center">
        <tr>
            <td colspan="4" class="title_1" id="gwShare_thTitle">�� �� �� ѯ</td>
        </tr>
        <tr id="gwShare_tr11" STYLE="display:">
            <td colspan="4" align="center" width="100%">
                <div align="center">
                    <input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
                    <br/>
                    <div id="gwShare_msgdiv" STYLE="display:none"></div>
                </div>
            </td>
        </tr>
        <tr id="gwShare_tr12" STYLE="display:">
            <td colspan="4" align="center" width="100%">
                <div align="center">
                    <input type="radio" class=jianbian name="gwShare_queryField" value="deviceSn" checked
                           onclick="gwShare_queryField_selected(this)"/> �豸���к� &nbsp;&nbsp;
                    <input type="radio" class=jianbian name="gwShare_queryField" value="custaccount"
                           onclick="gwShare_queryField_selected(this)"/> ҵ���˺� &nbsp;&nbsp;
                    <input type="radio" class=jianbian name="gwShare_queryField" value="deviceIp"
                           onclick="gwShare_queryField_selected(this)"/> �豸IP &nbsp;&nbsp;
                    <!-- <input type="radio" class=jianbian name="gwShare_queryField" value="all" onclick="gwShare_queryField_selected(this)"/> ȫ �� &nbsp;&nbsp; -->
                </div>
            </td>
        </tr>
        <TR id="gwShare_tr21" STYLE="display:none">
            <TD align="right" class="title_2" width="15%">�� ��</TD>
            <TD align="left" width="35%">
                <select name="gwShare_cityId" class="bk">
                    <option value="-1">==��ѡ��==</option>
                </select>
            </TD>
            <ms:inArea areaCode="hn_lt" notInMode="true">
                <TD align="right" class="title_2" width="15%">����״̬</TD>
                <TD width="35%">
                    <select name="gwShare_onlineStatus" class="bk">
                        <option value="-1">==��ѡ��==</option>
                        <option value="0">����</option>
                        <option value="1">����</option>
                    </select>
                </TD>
            </ms:inArea>
            <ms:inArea areaCode="hn_lt" notInMode="false">
                <TD align="right" class="title_2" width="15%">�豸���к�</TD>
                <TD width="35%">
                    <input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="40" class="bk"/>
                    <font color="red">��ģ��ƥ��</font>
                </TD>
            </ms:inArea>
        </TR>
        <TR id="gwShare_tr22" STYLE="display:none">
            <TD align="right" class="title_2" width="15%">�� ��</TD>
            <TD width="35%">
                <select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
                    <option value="-1">==��ѡ��==</option>
                </select>
            </TD>
            <TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
            <TD align="left" width="35%">
                <select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
                    <option value="-1">����ѡ����</option>
                </select>
            </TD>
        </TR>
        <TR id="gwShare_tr23" STYLE="display:none">
            <TD align="right" class="title_2" width="15%">�豸�汾</TD>
            <TD width="35%">
                <select name="gwShare_devicetypeId" class="bk"">
                <option value="-1">����ѡ���豸�ͺ�</option>
                </select>
            </TD>
            <TD align="right" class="title_2" width="15%">�Ƿ��</TD>
            <TD width="35%">
                <select name="gwShare_bindType" class="bk">
                    <option value="-1">==��ѡ��==</option>
                    <option value="0">δ��</option>
                    <option value="1">�Ѱ�</option>
                </select>
            </TD>
        </TR>
        <ms:inArea areaCode="hn_lt" notInMode="true">
            <TR id="gwShare_tr24" STYLE="display:none">
                <TD align="right" class="title_2" width="15%">�豸���к�</TD>
                <TD width="35%">
                    <input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="40" class="bk"/>
                    <font color="red">��ģ��ƥ��</font>
                </TD>
                <TD align="right" class="title_2" width="15%">ƽ̨</TD>
                <TD width="35%">
                    <select name="gwShare_platform" class="bk">
                        <option value="-1">==��ѡ��==</option>
                        <option value="0">3.0����ƽ̨</option>
                        <option value="1">2.0����ƽ̨</option>
                        <option value="2">2.0��Ϊƽ̨</option>
                        <option value="3">3.0��Ϊƽ̨</option>
                        <option value="4">�ῴ</option>
                    </select>
                </TD>
            </TR>
            <TR bgcolor="#FFFFFF" id="gwShare_tr25" STYLE="display:none">
                <TD align="right" class=column width="15%">�����ɵ�SQL</TD>
                <TD width="35%" colspan="3">
					    <textarea name="gwShare_matchSQL1" cols="100" rows="4" class=bk readonly="readonly">
					    </textarea>
                    <font color="red">*�����ɵ�SQL</font>
                </TD>
            </TR>
        </ms:inArea>
        <tr id="gwShare_tr31" bgcolor="#FFFFFF" style="display:none">
            <td align="right" width="15%">�ύ�ļ�</td>
            <td colspan="3" width="85%">
                <div id="importUsername">
                    <iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
                            src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
                    </iframe>
                    <input type="hidden" name=gwShare_fileName value=""/>
                </div>
            </td>
        </tr>
        <!--  height="30" -->
        <tr id="gwShare_tr32" style="display:none">
            <td CLASS="green_foot" align="right">ע������</td>
            <td colspan="3" CLASS="green_foot">
                1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��
                <br>
                2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���
                <br>
                3���ļ�ֻ��һ�С�
                <br>
                4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
            </td>
        </tr>
        <tr>
            <td colspan="4" align="right" class="foot" width="100%">
                <div align="right">
                    <input type="button" onclick="javascript:do_query();"
                           name="gwShare_queryButton" style="CURSOR:hand" value="�� ѯ"></input>
                    <input type="button" onclick="javascript:gwShare_queryChange('1');"
                           name="gwShare_jiadan" style="CURSOR:hand;display:none" value="�򵥲�ѯ"></input>
                    <input type="button" onclick="javascript:gwShare_queryChange('2');"
                           name="gwShare_gaoji" style="CURSOR:hand" value="�߼���ѯ"></input>
                    <input type="button" onclick="javascript:gwShare_queryChange('3');"
                           name="gwShare_import" style="CURSOR:hand" value="�����ѯ"></input>

                    <!-- <input type="button" class=jianbian style="CURSOR:hand" style="display: none"
                    onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="�����ѯ" /> -->

                    <input type="button" onclick="javascript:gwShare_revalue();"
                           name="gwShare_reButto" style="CURSOR:hand" value="�� ��"></input>
                </div>
            </td>
        </tr>
    </TABLE>
</form>