/***************************************************************************************
 *             				���ܡ�������������JS����
 ***************************************************************************************
 *@author:benyp(5260) E-mail:benyp@lianchuang.com
 *@since:2008-10-10
 *@version 1.0
 *@remark:��ҪΪ���������ṩͳһ��JS����
 ************************************�޸ļ�¼*******************************************
 *  ID      ʱ��     �޸���        �޸�����          ��ע
 *-------------------------------------------------------------------------------------
 *  1   2008-10-13  benyp      �޸Ķ�float���ж�  ��Ҫ����jQeuryCheckForm-linkage.js   
 *-------------------------------------------------------------------------------------
 *  2   2008-10-14  benyp      ���Ӳ�ѯ�豸  
 *-------------------------------------------------------------------------------------
 *  3   2008-10-14  benyp      ����Ҫ�������޸Ĳɼ�ʱ������ͬʱ�ڲ�ѯ���ý��ʱ���ɼ�ʱ������ʾ
 *                             ���ӵķ�����$.MO(),$.showT(),$.changeT()
 *-------------------------------------------------------------------------------------
 *  4   2008-10-30  benyp      ������������ط���
 ***************************************************************************************
 */
jQuery.extend({
	//*******************************************����*************************************
	checkFluxWarn:function(){
		var data="";
		//�˿�������������ֵ
		if($("input[@name='ifinoctetsbps_max']").attr("readonly") != true){
			data=$("input[@name='ifinoctetsbps_max']").val();
			if($.trim(data)==""){
				alert("�˿�������������ֵ����Ϊ�գ�");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿�������������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿�������������ֵ����Խ�磡");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinoctetsbps_max']").val("-1");
		}
		//�˿�������������ֵ
		if($("input[@name='ifoutoctetsbps_max']").attr("readonly") != true){
			data=$("input[@name='ifoutoctetsbps_max']").val();
			if($.trim(data)==""){
				alert("�˿�������������ֵ����Ϊ�գ�");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿�������������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿�������������ֵ����Խ�磡");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutoctetsbps_max']").val("-1");
		}
		//�˿����붪������ֵ
		if($("input[@name='ifindiscardspps_max']").attr("readonly") != true){
			data=$("input[@name='ifindiscardspps_max']").val();
			if($.trim(data)==""){
				alert("�˿����붪������ֵ����Ϊ�գ�");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿����붪������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿����붪������ֵ����Խ�磡");
				$("input[@name='ifindiscardspps_max']").focus();
				$("input[@name='ifindiscardspps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifindiscardspps_max']").val("-1");
		}
		//�˿�������������ֵ
		if($("input[@name='ifoutdiscardspps_max']").attr("readonly") != true){
			data=$("input[@name='ifoutdiscardspps_max']").val();
			if($.trim(data)==""){
				alert("�˿�������������ֵ����Ϊ�գ�");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿�������������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿�������������ֵ����Խ�磡");
				$("input[@name='ifoutdiscardspps_max']").focus();
				$("input[@name='ifoutdiscardspps_max']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutdiscardspps_max']").val("-1");
		}
		//�˿�����������ֵ
		if($("input[@name='ifinerrorspps_max']").attr("readonly") != true){
			data=$("input[@name='ifinerrorspps_max']").val();
			if($.trim(data)==""){
				alert("�˿����붪������ֵ����Ϊ�գ�");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿����붪������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿����붪������ֵ����Խ�磡");
				$("input[@name='ifinerrorspps_max']").focus();
				$("input[@name='ifinerrorspps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinerrorspps_max']").val("-1");
		}
		//�˿������������ֵ
		if($("input[@name='ifouterrorspps_max']").attr("readonly") != true){
			data=$("input[@name='ifouterrorspps_max']").val();
			if($.trim(data)==""){
				alert("�˿������������ֵ����Ϊ�գ�");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�˿������������ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�˿������������ֵ����Խ�磡");
				$("input[@name='ifouterrorspps_max']").focus();
				$("input[@name='ifouterrorspps_max']").select();
				return false;
			}	
		}else{
			$("input[@name='ifouterrorspps_max']").val("-1");
		}
		//������ֵ�Ĵ���
		if($.trim($("input[@name='warningnum']").val())==""){
			alert("������ֵ�Ĵ�������Ϊ��!");
			$("input[@name='warningnum']").focus();
			$("input[@name='warningnum']").select();
			return false;
		}else if(!$.checkNum($("input[@name='warningnum']").val(),'int')){
			alert("������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ��");
			$("input[@name='warningnum']").focus();
			$("input[@name='warningnum']").select();
			return false;
		}
		//����ר��
		if($("input[@name='ifinoctetsbps_min']").length>0){
			//�˿�������������ֵ
			if($("input[@name='ifinoctetsbps_min']").attr("readonly")!=true){
				data=$("input[@name='ifinoctetsbps_min']").val();
				if($.trim(data)==""){
					alert("�˿�������������ֵ����Ϊ�գ�");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("�˿�������������ֵֻ��Ϊ���ָ�ʽ!");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("�˿�������������ֵ����Խ�磡");
					$("input[@name='ifinoctetsbps_min']").focus();
					$("input[@name='ifinoctetsbps_min']").select();
					return false;
				}	
			}else{
				$("input[@name='ifinoctetsbps_min']").val("-1");
			}
			//�˿�������������ֵ��(%)
			if($("input[@name='ifoutoctetsbps_min']").attr("readonly")!=true){
				data=$("input[@name='ifoutoctetsbps_min']").val();
				if($.trim(data)==""){
					alert("�˿�������������ֵ����Ϊ�գ�");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("�˿�������������ֵֻ��Ϊ���ָ�ʽ!");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("�˿�������������ֵ����Խ�磡");
					$("input[@name='ifoutoctetsbps_min']").focus();
					$("input[@name='ifoutoctetsbps_min']").select();
					return false;
				}	
			}else{
				$("input[@name='ifoutoctetsbps_min']").val("-1");
			}
			//������ֵ���Ĵ��������澯��
			if($.trim($("input[@name='warningnum_min']").val())==""){
				alert("������ֵ�Ĵ�������Ϊ��!");
				$("input[@name='warningnum_min']").focus();
				$("input[@name='warningnum_min']").select();
				return false;
			}else if(!$.checkNum($("input[@name='warningnum_min']").val(),'int')){
				alert("������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ��");
				$("input[@name='warningnum_min']").focus();
				$("input[@name='warningnum_min']").select();
				return false;
			}
			//��̬��ֵ��(%)
			if($("input[@name='overper_min']").attr("readonly")!=true){
				data=$("input[@name='overper_min']").val();
				if($.trim(data)==""){
					alert("��̬��ֵ������Ϊ�գ�");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}else if(!$.checkNum(data,'float')){
					alert("��̬��ֵ��ֻ��Ϊ���ָ�ʽ!");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}else if(data<0 || data>100){
					alert("��̬��ֵ������Խ�磡");
					$("input[@name='overper_min']").focus();
					$("input[@name='overper_min']").select();
					return false;
				}	
			}else{
				$("input[@name='overper_min']").val("-1");
			}
			//������̬��ֵ������(���澯)
			if($.trim($("input[@name='overnum_min']").val())==""){
				alert("������̬��ֵ����������Ϊ��!");
				$("input[@name='overnum_min']").focus();
				$("input[@name='overnum_min']").select();
				return false;
			}else if(!$.checkNum($("input[@name='overnum_min']").val(),'int')){
				alert("������̬��ֵ������ֻ��Ϊ���ָ�ʽ��");
				$("input[@name='overnum_min']").focus();
				$("input[@name='overnum_min']").select();
				return false;
			}
		}
		//������̬��ֵ�İٷֱ�
		if($("input[@name='overper']").attr("readonly")!=true){
			data=$("input[@name='overper']").val();
			if($.trim(data)==""){
				alert("������̬��ֵ�İٷֱȲ���Ϊ�գ�");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("������̬��ֵ�İٷֱ�ֻ��Ϊ���ָ�ʽ!");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				return false;
			}else if(data<0 || data>100){
					alert("������̬��ֵ�İٷֱ�����Խ�磡");
					$("input[@name='overper']").focus();
					$("input[@name='overper']").select();
					return false;
			}	
		}else{
			$("input[@name='overper']").val("-1");
		}
		//������̬��ֵһ�Ĵ���(���澯)
		if($("input[@name='overnum']").attr("readonly")!=true){
			if($.trim($("input[@name='overnum']").val())==""){
				alert("������̬��ֵһ�Ĵ�������Ϊ�գ�");
				$("input[@name='overnum']").focus();
				$("input[@name='overnum']").select();
				return false;
			}else if(!$.checkNum($("input[@name='overnum']").val(),'int')){
				alert("������̬��ֵһ�Ĵ���ֻ��Ϊ���ָ�ʽ!");
				$("input[@name='overnum']").focus();
				$("input[@name='overnum']").select();
				return false;
			}	
		}else{
			$("input[@name='overnum']").val("-1");
		}
		//���ɶ�̬��ֵһ������(��)
		if($("input[@name='com_day']").attr("readonly")!=true){
			if($.trim($("input[@name='com_day']").val())==""){
				alert("���ɶ�̬��ֵһ����������Ϊ�գ�");
				$("input[@name='com_day']").focus();
				$("input[@name='com_day']").select();
				return false;
			}else if(!$.checkNum($("input[@name='com_day']").val(),'int')){
				alert("���ɶ�̬��ֵһ������ֻ��Ϊ���ָ�ʽ!");
				$("input[@name='com_day']").focus();
				$("input[@name='com_day']").select();
				return false;
			}	
		}else{
			$("input[@name='com_day']").val("-1");
		}
		//�������ʱ仯����ֵ
		if($("input[@name='ifinoctets']").attr("readonly")!=true){
			data=$("input[@name='ifinoctets']").val();
			if($.trim(data)==""){
				alert("�������ʱ仯����ֵ����Ϊ�գ�");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�������ʱ仯����ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�������ʱ仯����ֵ����Խ�磡");
				$("input[@name='ifinoctets']").focus();
				$("input[@name='ifinoctets']").select();
				return false;
			}	
		}else{
			$("input[@name='ifinoctets']").val("-1");
		}
		//�������ʱ仯����ֵ
		if($("input[@name='ifoutoctets']").attr("readonly")!=true){
			data=$("input[@name='ifoutoctets']").val();
			if($.trim(data)==""){
				alert("�������ʱ仯����ֵ����Ϊ�գ�");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}else if(!$.checkNum(data,'float')){
				alert("�������ʱ仯����ֵֻ��Ϊ���ָ�ʽ!");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}else if(data<0 || data>100){
				alert("�������ʱ仯����ֵ����Խ�磡");
				$("input[@name='ifoutoctets']").focus();
				$("input[@name='ifoutoctets']").select();
				return false;
			}		
		}else{
			$("input[@name='ifoutoctets']").val("-1");
		}
		return true;
	},
	//��ʼ���澯ҳ��
	initWarn:function(){
		//�˿�������������ֵһ�Ƚϲ�����
		$("select[@name='ifinoct_maxtype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifinoctetsbps_max']").attr("readonly",true);
				$("input[@name='ifinoctetsbps_max']").attr("class","onread");
			}else{
				$("input[@name='ifinoctetsbps_max']").attr("readonly",false);
				$("input[@name='ifinoctetsbps_max']").attr("class","");
				$("input[@name='ifinoctetsbps_max']").focus();
				$("input[@name='ifinoctetsbps_max']").select();
			}
		});
		//�˿�������������ֵһ�Ƚϲ�����
		$("select[@name='ifoutoct_maxtype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifoutoctetsbps_max']").attr("readonly",true);
				$("input[@name='ifoutoctetsbps_max']").attr("class","onread");
			}else{
				$("input[@name='ifoutoctetsbps_max']").attr("readonly",false);
				$("input[@name='ifoutoctetsbps_max']").attr("class","");
				$("input[@name='ifoutoctetsbps_max']").focus();
				$("input[@name='ifoutoctetsbps_max']").select();
			}
		});
		//�˿�������������ֵ���Ƚϲ�����
		$("select[@name='ifinoct_mintype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifinoctetsbps_min']").attr("readonly",true);
				$("input[@name='ifinoctetsbps_min']").attr("class","onread");
			}else{
				$("input[@name='ifinoctetsbps_min']").attr("readonly",false);
				$("input[@name='ifinoctetsbps_min']").attr("class","");
				$("input[@name='ifinoctetsbps_min']").focus();
				$("input[@name='ifinoctetsbps_min']").select();
			}
		});
		//�˿�������������ֵ���Ƚϲ�����
		$("select[@name='ifoutoct_mintype']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='ifoutoctetsbps_min']").attr("readonly",true);
				$("input[@name='ifoutoctetsbps_min']").attr("class","onread");
			}else{
				$("input[@name='ifoutoctetsbps_min']").attr("readonly",false);
				$("input[@name='ifoutoctetsbps_min']").attr("class","");
				$("input[@name='ifoutoctetsbps_min']").focus();
				$("input[@name='ifoutoctetsbps_min']").select();
			}
		});
		//��̬��ֵһ������
		$("select[@name='overmax']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='overper']").attr("readonly",true);
				$("input[@name='overper']").attr("class","onread");
				$("input[@name='overnum']").attr("readonly",true);
				$("input[@name='overnum']").attr("class","onread");
				$("select[@name='overlevel']").attr("disabled",true);				
				$("select[@name='reinoverlevel']").attr("disabled",true);
			}else{
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='overnum']").attr("class","");
				$("select[@name='overlevel']").attr("disabled",false);				
				$("select[@name='reinoverlevel']").attr("disabled",false);
			}
		});
		//��̬��ֵ��������
		$("select[@name='overmin']").change(function(){
			if($(this).val()=="0"){
				$("input[@name='overper_min']").attr("readonly",true);
				$("input[@name='overper_min']").attr("class","onread");
				$("input[@name='overnum_min']").attr("readonly",true);
				$("input[@name='overnum_min']").attr("class","onread");
				$("select[@name='overlevel_min']").attr("disabled",true);				
				$("select[@name='reinoverlevel_min']").attr("disabled",true);
			}else{
				$("input[@name='overper_min']").attr("readonly",false);
				$("input[@name='overper_min']").attr("class","");
				$("input[@name='overper_min']").focus();
				$("input[@name='overper_min']").select();
				$("input[@name='overnum_min']").attr("readonly",false);
				$("input[@name='overnum_min']").attr("class","");
				$("select[@name='overlevel_min']").attr("disabled",false);				
				$("select[@name='reinoverlevel_min']").attr("disabled",false);
			}
		});
		//�˿����������������ֵ
		$("#c_inmax").click(function(){
			$.targetClick($(this),"ifinoctetsbps_max");
		});
		//�˿�����������������ֵ
		$("#c_outmax").click(function(){
			$.targetClick($(this),"ifoutoctetsbps_max");
		});
		//�˿����붪������ֵ
		$("#c_disinmax").click(function(){
			$.targetClick($(this),"ifindiscardspps_max");
		});
		//�˿�������������ֵ
		$("#c_disoutmax").click(function(){
			$.targetClick($(this),"ifoutdiscardspps_max");
		});
		//�˿�����������ֵ
		$("#c_errinmax").click(function(){
			$.targetClick($(this),"ifinerrorspps_max");
		});
		//�˿������������ֵ
		$("#c_erroutmax").click(function(){
			$.targetClick($(this),"ifouterrorspps_max");
		});
		//���ö�̬��ֵ�澯
		$("#c_usedynamic").click(function(){
			if($(this).attr("checked")==true){
				//������̬��ֵ�İٷֱ�
				$("input[@name='overper']").attr("readonly",false);
				$("input[@name='overper']").attr("class","");
				$("input[@name='overper']").focus();
				$("input[@name='overper']").select();
				//�����ٷֱȴ���(���澯)
				$("input[@name='overnum']").attr("readonly",false);
				$("input[@name='overnum']").attr("class","");
				//������̬��ֵ�澯ʱ�ĸ澯����
				$("select[@name='overlevel']").attr("disabled",false);
				//��̬��ֵ�澯�ָ�����
				$("select[@name='reinoverlevel']").attr("disabled",false);
				//���ɶ�̬��ֵһ������(��)
				$("input[@name='com_day']").attr("readonly",false);
				$("input[@name='com_day']").attr("class","");
			}else{
				//������̬��ֵ�İٷֱ�
				$("input[@name='overper']").attr("readonly",true);
				$("input[@name='overper']").attr("class","onread");
				//�����ٷֱȴ���(���澯)
				$("input[@name='overnum']").attr("readonly",true);
				$("input[@name='overnum']").attr("class","onread");
				//������̬��ֵ�澯ʱ�ĸ澯����
				$("select[@name='overlevel']").attr("disabled",true);
				//��̬��ֵ�澯�ָ�����
				$("select[@name='reinoverlevel']").attr("disabled",true);
				//���ɶ�̬��ֵһ������(��)
				$("input[@name='com_day']").attr("readonly",true);
				$("input[@name='com_day']").attr("class","onread");
			}
		});
		//������������ͻ��澯����
		$("#c_intbflag").click(function(){
			if($(this).attr("checked")==true){
				$("input[@name='intbflag']").val(1)
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifinoctets']").attr("readonly",false);
				$("input[@name='ifinoctets']").attr("class","");
				$("input[@name='ifinoctets']").select();
				$("input[@name='ifinoctets']").focus();
				//��������ͻ��澯������
				$("select[@name='inoperation']").attr("disabled",false);
				//��������ͻ��澯����
				$("select[@name='inwarninglevel']").attr("disabled",false);
				//�������ʻָ�ͻ��澯����
				$("select[@name='inreinstatelevel']").attr("disabled",false);
			}else{
				$("input[@name='intbflag']").val(0)
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifinoctets']").attr("readonly",true);
				$("input[@name='ifinoctets']").attr("class","onread");
				//��������ͻ��澯������
				$("select[@name='inoperation']").attr("disabled",true);
				//��������ͻ��澯����
				$("select[@name='inwarninglevel']").attr("disabled",true);
				//�������ʻָ�ͻ��澯����
				$("select[@name='inreinstatelevel']").attr("disabled",true);
			}
		});
		//������������ͻ��澯����
		$("#c_outtbflag").click(function(){
			if($(this).attr("checked")==true){
				$("input[@name='outtbflag']").val(1);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifoutoctets']").attr("readonly",false);
				$("input[@name='ifoutoctets']").attr("class","");
				$("input[@name='ifoutoctets']").select();
				$("input[@name='ifoutoctets']").focus();
				//��������ͻ��澯������
				$("select[@name='outoperation']").attr("disabled",false);
				//��������ͻ��澯����
				$("select[@name='outwarninglevel']").attr("disabled",false);
				//�������ʻָ�ͻ��澯����
				$("select[@name='outreinstatelevel']").attr("disabled",false);
			}else{
				$("input[@name='outtbflag']").val(0);
				//�������ʱ仯����ֵ(%)
				$("input[@name='ifoutoctets']").attr("readonly",true);
				$("input[@name='ifoutoctets']").attr("class","onread");
				//��������ͻ��澯������
				$("select[@name='outoperation']").attr("disabled",true);
				//��������ͻ��澯����
				$("select[@name='outwarninglevel']").attr("disabled",true);
				//�������ʻָ�ͻ��澯����
				$("select[@name='outreinstatelevel']").attr("disabled",true);
			}
		});
	},
	//input�����¼�
	targetClick:function(target,name){
		if(target.attr("checked")==true){
			$("input[@name='"+name+"']").attr("readonly",false);
			$("input[@name='"+name+"']").attr("class","");
			$("input[@name='"+name+"']").select();
			$("input[@name='"+name+"']").focus();
		}else{
			$("input[@name='"+name+"']").attr("readonly",true);
			$("input[@name='"+name+"']").attr("class","onread");
		}
	},
	//***********************************************����**********************************
	//Input OnMouseOut�¼�
	MO:function(interval,target){
		if(interval==target.val()){
			target.toggle();
			target.parent().find("label").toggle();
		}
	},
	//�л�Label��Input
	showT:function(target){
		target.toggle();
		target.parent().find("input").toggle();
	},
	//��ʾ�޸�ʱ��
	changeT:function(interval,expressionid,device_id,target){
		var lab=target.parent().find("label");
		if(window.confirm("ȷ��Ҫ�޸Ĳɼ�ʱ������")){
			$.post(
				"./configPmee!ChangeInterval.action",
				{
					interval:target.val(),
					expressionid:expressionid,
					device_id:device_id
				},
				function(data){
					if(data=="true"){
						alert("�޸ļ��ʱ��ɹ���");
						target.toggle();
						lab.html(target.val());
						lab.toggle();					
					}else{
						alert("�޸ļ��ʱ��ʧ�ܣ������ԣ�");
					}
				}
			);
		}else{
			target.toggle();
			target.val(interval);
			lab.toggle();
		}
	},
	//�����豸���ơ��豸IP��ȡ�豸
	SelDevByName:function(pmeeflg, gw_type){
		if($.trim($("input[@name='device_name']").val())=="" && $.trim($("input[@name='loopback_ip']").val())==""){
			alert("�������豸���ƻ��豸IP��");
			$("input[@name='device_name']").focus();
			return false;
		}
		$("tbody[@name='tbody']").html("<tr><td colspan='6' class='even'>���ڻ�ȡ�豸����ȴ�</td></tr>");
		$.post(
			"./configPmee!getDevByNameIP.action",
			{
				device_name:$.cc($("input[@name='device_name']")),
				loopback_ip:$("input[@name='loopback_ip']").val(),
				gw_type:gw_type,
				pmeeflg:pmeeflg
			},
			function(data){
				$("tbody[@name='tbody']").html(data);
				$("#chkall").click(function(){
					var chk=$(this).attr("checked");
					chk=typeof(chk)=="undefined"?false:chk;
					$("input[@name='chk']").attr("checked",chk);
				});
			}
		);
	},
	//�����豸���̡��豸�ͺŲ�ѯ�豸
	SelDevByModel:function(pmeeflg, gw_type){
		if($("select[@name='device_model']").val()==""){
			alert("��ѡ���̺��ͺ�!");
			$("select[@name='device_model']").focus();
			return false;
		}
		
		$("#chkall").attr("checked",false);
		$("tbody[@name='tbody']").html("<tr><td colspan='6' class='even'>���ڻ�ȡ�豸����ȴ�</td></tr>");
		$.post(
			"./configPmee!getDevByModel.action",
			{
				city_id:$("select[@name='city_id']").val(),
				vendor_id:$("select[@name='vendor_id']").val(),
				device_model:$("select[@name='device_model']").val(),
				gw_type:gw_type,
				pmeeflg:pmeeflg
			},
			function(data){
				$("tbody[@name='tbody']").html(data);
				$("#chkall").click(function(){
					var chk=$(this).attr("checked");
					chk=typeof(chk)=="undefined"?false:chk;
					$("input[@name='chk']").attr("checked",chk);
				});
			}
		);
	},
	//��ʼ���澯��ʾ type:show tr_fix��ʾ ��������ʾ
	init:function(type){
		if(type=="hide"){
			$("tr[@name='tr_fix']").hide();
			$("tr[@name='tr_show']").hide();
		}else{
			$("tr[@name='tr_fix']").show();
		}		
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
	},
	//��ʾ������tab type:�ڼ���tab
	showHide:function(type){
		//ȫ������
		$("tr[@name='tr_fix']").hide();
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
		//classȫΪout
		$("td[@name='td_fix']").attr("class","mouseout");
		$("td[@name='td_active']").attr("class","mouseout");
		$("td[@name='td_sudden']").attr("class","mouseout");
		//mouseoutȫΪout
		$("td[@name='td_fix']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		$("td[@name='td_active']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		$("td[@name='td_sudden']").mouseout(function(){
			$(this).attr("class","mouseout");
		});
		//����tab
		switch(type){
			case 1:
				$("td[@name='td_fix']").attr("class","mouseon");
				$("td[@name='td_fix']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_fix']").show();
				break;
			case 2:
				$("td[@name='td_active']").attr("class","mouseon");
				$("td[@name='td_active']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_active']").show();
				break;
			case 3:
				$("td[@name='td_sudden']").attr("class","mouseon");
				$("td[@name='td_sudden']").mouseout(function(){
					$(this).attr("class","mouseon");
				});
				$("tr[@name='tr_sudden']").show();
				break;
		}
	},
	//����Tab��ʾ��Ӧ�ĸ澯������
	showChage:function(){
		if($("td[@name='td_fix']").attr("class")=="mouseon"){
			$("tr[@name='tr_fix']").show();
		}else if($("td[@name='td_active']").attr("class")=="mouseon"){
			$("tr[@name='tr_active']").show();
		}else{
			$("tr[@name='tr_sudden']").show();
		}
	},
	//���澯����
	CheckWarn:function(){
		if($("select[@name='mintype']").val()>0 && $("input[@name='minthres']").val()==""){
			alert("�����˹̶���ֵһ���̶���ֵһ����Ϊ�գ�������!");
			$("input[@name='minthres']").focus();
			return false;
		}else if($("input[@name='minthres']").val()!="" && !$.checkNum($("input[@name='minthres']").val(),'float')){
			alert("�̶���ֵֻ��Ϊ���֣����������룡");
			$("input[@name='minthres']").focus();
			$("input[@name='minthres']").select();
			return false;
		}else if($("input[@name='mincount']").val()==""){
			alert("����������ֵ�Ĵ�������Ϊ�գ����������룡");
			$("input[@name='mincount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mincount']").val(),'int')){
			alert("����������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='mincount']").focus();
			$("input[@name='mincount']").select();
			return false;
		}else if($("select[@name='maxtype']").val()>0 && $("input[@name='maxthres']").val()==""){
			alert("�̶���ֵ����Ϊ�գ������룡");
			$("input[@name='maxthres']").focus();
			return false;
		}else if($("input[@name='maxthres']").val()!="" && !$.checkNum($("input[@name='maxthres']").val(),'float')){
			alert("�̶���ֵֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='maxthres']").focus();
			$("input[@name='maxthres']").select();
			return false;
		}else if($("input[@name='maxcount']").val()==""){
			alert("����������ֵ�Ĵ�������Ϊ�գ������룡");
			$("input[@name='maxcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='maxcount']").val(),'int')){
			alert("����������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='maxcount']").focus();
			$("input[@name='maxcount']").select();
			return false;
		}else if($("input[@name='beforeday']").val()==""){
			alert("���������ݻ�׼ֵ��");
			$("input[@name='beforeday']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='beforeday']").val(),'int')){
			alert("���ݻ�׼ֵֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='beforeday']").focus();
			$("input[@name='beforeday']").select();
			return false;
		}else if($("select[@name='dynatype']").val()>0 && $("input[@name='dynathres']").val()==""){
			alert("��ֵ�ٷֱȲ���Ϊ�գ������룡");
			$("input[@name='dynathres']").focus();
			return false;
		}else if($("input[@name='dynathres']").val()!="" && !$.checkNum($("input[@name='dynathres']").val(),'float')){
			alert("��ֵ�ٷֱ�ֻ��Ϊ���ָ�ʽ������������");
			$("input[@name='dynathres']").focus();
			$("input[@name='dynathres']").select();
			return false;
		}else if($("select[@name='mutationtype']").val()>0 && $("input[@name='mutationthres']").val()==""){
			alert("ͻ�䷧ֵ�����ٷֱȲ���Ϊ�գ�������!");
			$("input[@name='mutationthres']").focus();
			return false;
		}else if($("input[@name='mutationthres']").val()!="" && !$.checkNum($("input[@name='mutationthres']").val(),'float')){
			alert("�ٷֱ�ֻ��Ϊ���ָ�ʽ������������!");
			$("input[@name='mutationthres']").focus();
			$("input[@name='mutationthres']").select();
			return false;
		}else if($("input[@name='mutationcount']").val()==""){
			alert("�ﵽ��ֵ�ٷֱȴ�������Ϊ�գ�������!");
			$("input[@name='mutationcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mutationcount']").val(),'int')){
			alert("����ֻ��Ϊ���ָ�ʽ������������");
			$("input[@name='mutationcount']").focus();
			$("input[@name='mutationcount']").select();
			return false;
		}else{
			return true;
		}
	}
});