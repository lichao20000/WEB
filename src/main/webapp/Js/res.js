var ICPMenuRes = new Array(7);

var SubICPMenuRes = new Array(6);

SubICPMenuRes[0] = new Menu('Operation','网络开通系统','./NetCutover/','网络开通系统','NETCUTOVER_READ');
SubICPMenuRes[1] = new Menu('Operation','ADSL监控分析系统','./AdslCheck/','ADSL监控分析系统','ADSLCHECK_READ');
SubICPMenuRes[2] = new Menu('Operation',strFlautName,'./10000Flaut/',strFlautName,'10000WARN_READ');
SubICPMenuRes[3] = new Menu('Operation','现场安装管理','./UserIsnt/','现场安装管理','USER_INST_READ');
SubICPMenuRes[4] = new Menu('Operation','业务/服务质量管理','./Operation/','业务/服务质量管理','OPERATION_READ');
SubICPMenuRes[5] = new Menu('Operation','能力预报','./Prediction/','能力预报','OPERATION_READ');


ICPMenuRes[0]  = new Menu('Operation','业务管理','','业务支撑系统管理','',SubICPMenuRes);
ICPMenuRes[1]  = new Menu('Performance','考核管理','./assess/','考核管理','PERFORMANCE_READ');
ICPMenuRes[2]  = new Menu('Performance','性能管理','./Performance/','性能管理','PERFORMANCE_READ');
ICPMenuRes[3]  = new Menu('Fault','故障管理','./Warn/','故障管理','WARN_READ');
ICPMenuRes[4]  = new Menu('Resource','资源管理','./Resource/','资源管理系统','RESOURCE_READ');
ICPMenuRes[5]  = new Menu('Report','报表系统','./Report/','报表系统','REPORT_READ');
ICPMenuRes[6]  = new Menu('System','系统管理','./system/','系统管理','');
ICPMenuRes[7]  = new Menu('TopoWeb','WEB拓扑','./webtopo/','WEB拓扑','WEBTOPO1');

//设置主题LOGO
setICPBanner(banner_img,sys_title,"","_top");