var ICPMenuRes = new Array(8);

var SubICPMenuRes;

	SubICPMenuRes    = new Array(7);
	SubICPMenuRes[0] = new Menu("Device","����豸��Դ","AddDeviceForm.jsp","�豸��Դ����--����豸��Դ","DEVICE_ADD");
	SubICPMenuRes[1] = new Menu("Device","�豸��Դ�б�","DeviceList.jsp","�豸��Դ����--�豸��Դ�б�","DEVICE_EDIT");
	SubICPMenuRes[2] = new Menu("Device","��ȷ�������豸�б�","DeviceList_topo.jsp","�豸��Դ����--��ȷ�������豸�б�","DEVICE_TOPO_ADD");
	
	//SubICPMenuRes[3] = new Menu("Device","�豸�����嵥��","DeviceType_info.jsp","�豸��Դ����--�豸�����嵥��","DEVICE_TYPE_ALL");
	SubICPMenuRes[3] = new Menu('Device','-','','-','');
	SubICPMenuRes[4] = new Menu("Device","�豸��Դͳ��","DeviceState.jsp","�豸��Դ����--�豸��Դͳ��","DEVICE_RES_SUM");
	SubICPMenuRes[5] = new Menu("Device","��ȷ�������豸ͳ��","DeviceTopoFind.jsp","�豸��Դ����--��ȷ�������豸ͳ��","DEVICE_RES_SUM_TOPOFIND");
	SubICPMenuRes[6] = new Menu("Device","�豸������Ϣ","Devicestru.jsp","�豸��Դ����--�豸������Ϣ","DEVICE_STRUCATE");

ICPMenuRes[0]  = new Menu("Device","�豸��Դ����","","�ۺ�����--��Դ����ϵͳ--�豸��Դ����","DEVICE_RES_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(6);
	SubICPMenuRes[0] = new Menu("Customer","���Lan�ͻ���Դ","AddLanCustomerForm.jsp","�ͻ���Դ����--Lan�ͻ���Դ","RES_CUS_LAN_ALL");
	SubICPMenuRes[1] = new Menu("Customer","���ADSL�ͻ���Դ","AddRadiusCustomerForm.jsp","�ͻ���Դ����--ADSL�ͻ���Դ","RES_CUS_ADSL_ALL");
	SubICPMenuRes[2] = new Menu("Customer","��Ӵ�ͻ���Դ","AddUserInfoForm.jsp","�ͻ���Դ����--��ͻ���Դ","RES_LARGE_ALL");
	SubICPMenuRes[3] = new Menu("Customer","Lan�ͻ���Դ�б�","LanCustomerList.jsp","�ͻ���Դ����--Lan�ͻ���Դ","RES_CUS_LAN_ALL");
	SubICPMenuRes[4] = new Menu("Customer","ADSL�ͻ���Դ�б�","RadiusCustomerList.jsp","�ͻ���Դ����--ADSL�ͻ���Դ","RES_CUS_ADSL_ALL");
	SubICPMenuRes[5] = new Menu("Customer","��ͻ���Դ�б�","UserInfoList.jsp","�ͻ���Դ����--��ͻ���Դ","RES_LARGE_ALL");

ICPMenuRes[1]  = new Menu("Customer","�ͻ���Դ����","","�ۺ�����--��Դ����ϵͳ--�ͻ���Դ����","RES_CUSTOMER_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu("Version","���ذ�����汾","DeviceResourceSoftwareList.jsp","�汾��Դ����--����汾","RES_VERSION_READ");
	SubICPMenuRes[1] = new Menu("Version","Ӳ���汾","DeviceResourceHardwareList.jsp","�汾��Դ����--Ӳ���汾","RES_VERSION_READ");
	SubICPMenuRes[2] = new Menu("Version","�汾ͳ�ƺ��ۺϲ�ѯ","VersionAllSearchForm.jsp","�汾��Դ����--�汾ͳ�ƺ��ۺϲ�ѯ","RES_VERSION_READ");
	SubICPMenuRes[3] = new Menu("Version","�汾ͳ�ƺ͵ȼ���ѯ","VersionLevelSearchForm.jsp","�汾��Դ����--�汾ͳ�ƺ͵ȼ���ѯ","RES_VERSION_READ");
ICPMenuRes[2]  = new Menu("Version","�汾��Դ����","","�ۺ�����--��Դ����ϵͳ--�汾��Դ����","RES_VERSION_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(9);
	SubICPMenuRes[0] = new Menu("Base","������Դ","AreaList.jsp","������Դ����--������Դ","RES_BASE_AREA_ALL");
	SubICPMenuRes[1] = new Menu("Base","������Դ","CityList.jsp","������Դ����--������Դ","RES_BASE_CITY_ALL");
	SubICPMenuRes[2] = new Menu("Base","������Դ","OfficeList.jsp","������Դ����--������Դ","RES_BASE_OFFICE_ALL");
	SubICPMenuRes[3] = new Menu("Base","С����Դ","ZoneList.jsp","������Դ����--С����Դ","RES_BASE_ZONE_ALL");
	SubICPMenuRes[4] = new Menu("Base","��Դ�������ñ�","ResTypeToDeviceModelList.jsp","������Դ����--��Դ������Դ","RES_BASE_TYPE_ALL");
	SubICPMenuRes[5] = new Menu('Base','-','','-','');
	SubICPMenuRes[6] = new Menu("Base","�豸���","ResourceTypeList.jsp","������Դ����--�豸���","RES_BASE_RES_ALL");
	SubICPMenuRes[7] = new Menu("Base","�豸����","VendorList.jsp","������Դ����--�豸����","RES_BASE_FACTORY_ALL");
	SubICPMenuRes[8] = new Menu("Base","�豸�ͺ�","DeviceType_info.jsp","������Դ����--�豸�ͺ�","RES_BASE_FACTORY_ALL");	
ICPMenuRes[3]  = new Menu("Base","������Դ����","","�ۺ�����--��Դ����ϵͳ--������Դ����","RES_BASE_READ",SubICPMenuRes);

	//SubICPMenuRes    = new Array(1);
	//SubICPMenuRes[0] = new Menu("IPADDRESS","��ַ��;����","../ipmanage/ip_addr_usecreate_form.jsp","IP��ַ����-��ַ��;����","RES_BASE_AREA_ALL");
ICPMenuRes[4]  = new Menu("IPADDRESS","IP��ַ����","../ipmanage/","�ۺ�����--��Դ����ϵͳ--IP��ַ����","IP_ADDR_MANAGE");

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu("Bak","���ñ����豸","bak_config_dev_form.jsp","�豸���ñ���--���ñ����豸","RES_BAK_CONFIG_DEV");
	SubICPMenuRes[1] = new Menu("Bak","���ñ��ݷ�����","bak_config_serv_form.jsp","�豸���ñ���--���ñ��ݷ�����","RES_BAK_CONFIG_SERV");
	SubICPMenuRes[2] = new Menu("Bak","��ʾ�豸����","bak_view_form.jsp","�豸���ñ���--��ʾ�豸����","RES_BAK_VIEW");
	SubICPMenuRes[3] = new Menu("Bak","�Ƚ��豸����","bak_compare_form.jsp","�豸���ñ���--�Ƚ��豸����","RES_BAK_COMPARE");
ICPMenuRes[5]  = new Menu("Bak","�豸���ñ���","","�ۺ�����--��Դ����ϵͳ--�豸���ñ���","RES_BAK_READ",SubICPMenuRes);

ICPMenuRes[6]  = new Menu("Whois","Whois����","","�ۺ�����--��Դ����ϵͳ--Whois����","WHOIS_READ",SubICPMenuRes);

   SubICPMenuRes    = new Array(1);
	SubICPMenuRes[0] = new Menu("Circuit","��·����","circuit_define.jsp","��·����","RES_BASE_READ");
ICPMenuRes[7]  = new Menu("Circuit","��·��Դ","","�ۺ�����--��·��Դ","RES_BASE_READ",SubICPMenuRes);
//��������LOGO
setICPBanner("../images/resource_banner.jpg","��Դ����ϵͳ","index.jsp","_top");

//��������Banner
//setADSBanner(IMGStr,ALTStr,LINKStr,TARGETStr);

//����ICPMenu��ɫ
//setICPMenuColor("#003399","#FFFFFF","#FF0000");

//���ù�˾LOGO
setLogo("images/logo.gif","�����Ƽ��ɷ����޹�˾","http://www.lianchuang.com/","_blank");

isFrameWork = true;