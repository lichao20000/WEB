var ICPMenuRes = new Array(7);

var SubICPMenuRes = new Array(6);

SubICPMenuRes[0] = new Menu('Operation','���翪ͨϵͳ','./NetCutover/','���翪ͨϵͳ','NETCUTOVER_READ');
SubICPMenuRes[1] = new Menu('Operation','ADSL��ط���ϵͳ','./AdslCheck/','ADSL��ط���ϵͳ','ADSLCHECK_READ');
SubICPMenuRes[2] = new Menu('Operation',strFlautName,'./10000Flaut/',strFlautName,'10000WARN_READ');
SubICPMenuRes[3] = new Menu('Operation','�ֳ���װ����','./UserIsnt/','�ֳ���װ����','USER_INST_READ');
SubICPMenuRes[4] = new Menu('Operation','ҵ��/������������','./Operation/','ҵ��/������������','OPERATION_READ');
SubICPMenuRes[5] = new Menu('Operation','����Ԥ��','./Prediction/','����Ԥ��','OPERATION_READ');


ICPMenuRes[0]  = new Menu('Operation','ҵ�����','','ҵ��֧��ϵͳ����','',SubICPMenuRes);
ICPMenuRes[1]  = new Menu('Performance','���˹���','./assess/','���˹���','PERFORMANCE_READ');
ICPMenuRes[2]  = new Menu('Performance','���ܹ���','./Performance/','���ܹ���','PERFORMANCE_READ');
ICPMenuRes[3]  = new Menu('Fault','���Ϲ���','./Warn/','���Ϲ���','WARN_READ');
ICPMenuRes[4]  = new Menu('Resource','��Դ����','./Resource/','��Դ����ϵͳ','RESOURCE_READ');
ICPMenuRes[5]  = new Menu('Report','����ϵͳ','./Report/','����ϵͳ','REPORT_READ');
ICPMenuRes[6]  = new Menu('System','ϵͳ����','./system/','ϵͳ����','');
ICPMenuRes[7]  = new Menu('TopoWeb','WEB����','./webtopo/','WEB����','WEBTOPO1');

//��������LOGO
setICPBanner(banner_img,sys_title,"","_top");