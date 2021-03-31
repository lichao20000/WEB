package com.linkage.litms.webtopo.warn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import RemoteDB.AlarmEvent;

import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;

public class DeviceWarnFilter {
    private static DeviceWarnFilter instance = null;
    
    // 存放告警级别的列表
    HashMap LevelMap = new HashMap();

    // 存放告警创建者的名称和编号的映射关系
    HashMap CreatorMap = new HashMap();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String SourceIP = "";

    String SourceName = "";

    String DeviceType = "";

    String EventNo = "";

    int Severity = 0;

    String CreatorType = "";
    
    //默认为flag,即所有告警默认为不满足过滤条件，因而没有被过滤.
    public boolean _warnFlag = false;

    XMLWarnRule xml = null;

    public void setAlarmEvent(AlarmEvent ae, String DeviceType) {
        this.SourceIP = ae.m_SourceIP;
        this.SourceName = ae.m_SourceName;
        // this.DeviceType = Short.toString(ae.m_DeviceType);
        this.DeviceType = DeviceType;
        this.EventNo = ae.m_EventNo;
        this.Severity = ae.m_Severity;
        this.CreatorType = Short.toString(ae.m_CreatorType);
    }

    public boolean SaveXML(String[][] m_strDatas, String file) {
        if (m_strDatas != null) {
            xml.RuleList.clear();
            for (int i = 0; i < m_strDatas.length; i++) {
                WarnRuleBean rule = new WarnRuleBean();
                rule.setPriority(Integer.parseInt(m_strDatas[i][0].trim()));
                rule.setName(m_strDatas[i][1].trim());
                rule.setDesc(m_strDatas[i][2].trim());
                rule.setContent(m_strDatas[i][3].trim());
                rule.setResult(m_strDatas[i][4].trim());
                xml.RuleList.add(Integer.parseInt(m_strDatas[i][0]
                        .trim()), rule);
            }
            try {
                xml.GenerateXMLFile(file, xml.RuleList);
            } catch (Exception ef) {
                ef.printStackTrace();
                return false;
            }
            loadXMLFile(file);
        }
        return true;
    }

    private DeviceWarnFilter() {
        xml = new XMLWarnRule();
    }
    public static DeviceWarnFilter getInstance(String file){
        if(instance == null){
            instance = new DeviceWarnFilter(file);
        }
        return instance;
    }
    /**
     * 私有构造函数
     * @param file
     */
    private DeviceWarnFilter(String file) {
        CreatorMap.put("Trap", "4");
        CreatorMap.put("Syslog", "3");
        CreatorMap.put("性能采集系统", "2");
        CreatorMap.put("流量采集系统", "10");
        CreatorMap.put("网络拓扑管理", "6");
        CreatorMap.put("业务模拟系统", "7");
        CreatorMap.put("Ping检测", "8");
        CreatorMap.put("华为设备端口检测", "9");
        CreatorMap.put("主机系统", "1");
        CreatorMap.put("ARP检测代理", "20");
        CreatorMap.put("VPN检测代理", "80");

        EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
        LevelMap = (HashMap)eventLevelLefDAO.getWarnLevel();
//        LevelMap.put("恢复日志", "0");
//        LevelMap.put("正常日志", "1");
//        LevelMap.put("提示告警", "2");
//        LevelMap.put("一般告警", "3");
//        LevelMap.put("严重告警", "4");
//        LevelMap.put("紧急告警", "5");
        loadXMLFile(file);
    }
    /**
     * 加载XML配置文件
     *
     */
    private void loadXMLFile(String file){
        xml = new XMLWarnRule();
        xml.ReadXMLFile(file);
    }
    /**
     * 
     * @return
     */
    public String[][] getWarnRule() {
        String[][] m_strDatas = null;
        if (xml.RuleList != null) {
            m_strDatas = new String[xml.RuleList.size()][5];
            for (int i = 0; i < xml.RuleList.size(); i++) {
                WarnRuleBean rule = (WarnRuleBean) xml.RuleList.get(i);
                m_strDatas[i][0] = String.valueOf(rule.getPriority());
                m_strDatas[i][1] = rule.getName();
                m_strDatas[i][2] = rule.getDesc();
                m_strDatas[i][3] = rule.getContent();
                m_strDatas[i][4] = rule.getResult();
            }
        }
        return m_strDatas;
    }

    /**
     * 对每个告警处理规则进行处理，如果满足该条子规则，则返回true，如果不满足则返回false
     * 
     * @param args
     * @return
     */
    public boolean doWarn(String[] args) {
        boolean flag = false;
        // 依次判断每一项条件
        if (args[0].trim().equals("设备IP") && args[1].trim().equals("=")) {
            if (SourceIP.equals(args[2].trim())) {
                flag = true;
            }
        } else if (args[0].trim().equals("设备IP") && args[1].trim().equals("!=")) {
            if (!SourceIP.equals(args[2].trim())) {
                flag = true;
            }
        } else if (args[0].trim().equals("设备名称") && args[1].trim().equals("=")) {
            if (SourceName.equals(args[2].trim())) {
                flag = true;
            }
        } else if (args[0].trim().equals("设备名称") && args[1].trim().equals("!=")) {
            if (!SourceName.equals(args[2].trim())) {
                flag = true;
            }
        } else if (args[0].trim().equals("设备类型") && args[1].trim().equals("=")) {
            String tmp = args[2].trim();
            if(tmp.indexOf("/") != -1){
                tmp = tmp.substring(0,tmp.indexOf("/"));
            }
            //logger.debug("设备类型:AlarmEvent=" + DeviceType.trim() + "?" + args[2].substring(0, args[2].trim().indexOf("/")));
            if (DeviceType.trim().equals(tmp)) {
                //logger.debug("设备类型被过滤:AlarmEvent=" + DeviceType.trim() + "=" + args[2].substring(0, args[2].trim().indexOf("/")));
                flag = true;
            }
        } else if (args[0].trim().equals("设备类型") && args[1].trim().equals("!=")) {
            String tmp = args[2].trim();
            if(tmp.indexOf("/") != -1){
                tmp = tmp.substring(0,tmp.indexOf("/"));
            }
            //logger.debug("设备类型:AlarmEvent=" + DeviceType.trim() + "?" + args[2].substring(0, args[2].trim().indexOf("/")));
            //if (!DeviceType.trim().equals(args[2].substring(0, args[2].trim().indexOf("/")))) {
            if (!DeviceType.trim().equals(tmp)) {
                //logger.debug("设备类型被过滤:AlarmEvent=" + DeviceType + "=" + args[2].substring(0, args[2].trim().indexOf("/")));
                flag = true;
            }// 
        } else if (args[0].trim().equals("告警创建者") && args[1].trim().equals("=")) {
            // logger.debug("进入告警创建者状态=,当前的告警创建者是:" + CreatorType);
             //logger.debug("进入告警创建者状态=,与之比较的告警创建者是:" +
             //CreatorMap.get(args[2].trim()));
            if (CreatorType.equals(CreatorMap.get(args[2].trim()))) {
                //logger.debug("告警创建者被过滤:AlarmEvent=" + CreatorType + " " + CreatorMap.get(args[2].trim()));
                flag = true;
            }
        } else if (args[0].trim().equals("告警创建者") && args[1].trim().equals("!=")) {
            if (!CreatorType.equals(CreatorMap.get(args[2].trim()))) {
                //logger.debug("告警创建者被过滤:AlarmEvent=" + CreatorType + " " + CreatorMap.get(args[2].trim()));
                flag = true;
            }
        } else if (args[0].trim().equals("告警事件编号")
                && args[1].trim().equals("=")) {
            if (EventNo.equals(args[2].trim())) {
                //logger.debug("告警事件编号:" + EventNo + " " + args[2].trim());
                flag = true;
            }
        } else if (args[0].trim().equals("告警事件编号")
                && args[1].trim().equals("!=")) {
            if (!EventNo.equals(args[2].trim())) {
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals(">=")) {
            // logger.debug("进入告警级别判断状态>＝,当前的告警级别是:" + Severity);
            // logger.debug("进入告警级别判断状态>＝,要判断的级别是:" +
            // Integer.parseInt( (String) LevelMap.get(args[2])));
            if (Severity >= Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                // logger.debug("经过判断，符合比较要求");
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals("<=")) {
            // logger.debug("进入告警级别判断状态<=,当前的告警级别是:"+Severity);
            // logger.debug("进入告警级别判断状态<=,要判断的级别是:"+Integer.parseInt((String)
            // LevelMap.get(args[2])));
            if (Severity <= Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                // logger.debug("进入告警级别判断状态<=,当前的告警级别是:"+Severity);
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals("!=")) {
            if (Severity != Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals(">")) {
            // logger.debug("进入告警级别判断状态>,当前的告警级别是:" + Severity);
            // logger.debug("进入告警级别判断状态>,要判断的级别是:" +
            // Integer.parseInt( (String) LevelMap.get(args[2].trim())));
            if (Severity > Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                // logger.debug("经过判断，符合比较要求");
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals("<")) {
            // logger.debug("进入告警级别判断状态<,当前的告警级别是:" + Severity);
            if (Severity < Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                flag = true;
            }
        } else if (args[0].trim().equals("告警级别") && args[1].trim().equals("=")) {
            // logger.debug("进入告警级别判断状态=,当前的告警级别是:" + Severity);
            if (Severity == Integer.parseInt((String) LevelMap.get(args[2]
                    .trim()))) {
                flag = true;
            }
        } else {
            //logger.debug("没有找到相匹配的条件");

        }
        _warnFlag = flag;
        return flag;
    }

    /**
     * 对要编辑的规则的子规则进行分解
     * 
     * @param content
     */
    public String[] formatCotont(String content, String[] opts) {
        // 判断是否是告警等级的过滤
        int pos = -1;
        String opt = "";
        for (int i = 0; i < opts.length; i++) {
            if (content.indexOf(opts[i]) >= 0) {
                pos = content.indexOf(opts[i]);
                opt = opts[i];
                break;
            }
        }

        String[] values = new String[3];
        values[0] = content.substring(0, pos);
        values[1] = opt;
        values[2] = content.substring(pos + opt.length());
        return values;
    }
 
    /**
     * 告警过滤 BY HEMC
     * 
     * @param alarmList
     * @return
     */
    /*
    public boolean doFilterTest() {
        boolean flag = true;
        // 判断规则处理是否成功
        // boolean isdo = false;
        // 对告警按规则进行处理
        for (int i = 0; i < xml.RuleList.size(); i++) {
            WarnRuleBean rule = (WarnRuleBean) xml.RuleList.get(i);
            if (rule != null) {
                String ruleName = rule.getName();
                String ruleResult = rule.getResult();
                ArrayList orList = (ArrayList) xml.Name_ContentORMap
                        .get(ruleName);

                // 判断该条规则处理是否已经结束
                boolean isOver = false;
                // 当获取到或条件的时候，进行处理
                if (orList != null && orList.size() > 0) {
                    // logger.debug("进行或条件处理");
                    for (int j = 0; j < orList.size(); j++) {
                        // 获取到的告警列表
                        String content = (String) orList.get(j);
                        String[] values = null;
                        if (content.indexOf("告警级别") >= 0) {
                            String[] opts = { ">=", "<=", "!=", ">", "<", "=" };
                            values = formatCotont(content, opts);
                        } else {
                            String[] opts = { "!=", "=" };
                            values = formatCotont(content, opts);
                        }
                        if (values != null) {
                            // 对告警进行处理,如果告警处理认为通过的话，则结束or循环，否则继续
                            if (doWarn(values)) {
                                isOver = true;
                                break;
                            }
                        }
                    }
                }
                // 当告警处理已经认为结束时,则判断下一步骤是什么，并进行相应处理
                if (isOver) {
                    if (ruleResult.equals("立即过滤")) {
                        flag = false;
                        break;
                    } else if (ruleResult.equals("继续过滤")) {
                        //logger.debug("下一条规则过滤");
                        continue;
                    }
                }
                // 进行并运算处理
                else {
                    ArrayList andList = (ArrayList) xml.Name_ContentANDMap
                            .get(ruleName);
                    if (andList != null && andList.size() > 0) {
                        // logger.debug("进行与条件过滤");
                        // 对与告警进行处理
                        isOver = true;
                        // logger.debug("====================sdfsdfsd"+andList.size());
                        int j = 0;
                        for (j = 0; j < andList.size(); j++) {
                            String content = (String) andList.get(j);
                            // logger.debug(content);
                            String[] values = null;
                            if (content.indexOf("告警级别") >= 0) {
                                String[] opts = { ">=", "<=", "!=", ">", "<",
                                        "=" };
                                values = formatCotont(content, opts);
                            } else {
                                String[] opts = { "!=", "=" };
                                values = formatCotont(content, opts);
                            }
                            // logger.debug(values[2]);
                            if (values != null) {
                                // 对告警进行处理,如果告警处理认为不符合一个子规则，就退出该循环，只有所有的子规则都通过才认为通过
                                if (!doWarn(values)) {
                                    isOver = false;

                                    break;
                                } else {
                                    continue;
                                }
                            }
                        }
                        // 当所有子规则都验证通过了，则该项规则起作用;否则用下一规则去验证该告警
                        if (isOver) {
                            if (ruleResult.equals("立即过滤")) {
                                flag = false;
                                break;
                            } else if (ruleResult.equals("继续过滤")) {
                                //logger.debug("下一条规则过滤");
                                continue;
                            }
                        }
                        // 当验证没有通过的时候，用相反的条件去过滤
                        else {
                            if (ruleResult.equals("立即过滤")) {
                                flag = true;
                                break;
                            } else if (ruleResult.equals("继续过滤")) {
                                //logger.debug("下一条规则过滤");
                                flag = false;
                                break;
                            }
                        }
                    }
                    // 如果没有与运算符，则对或运算再进行处理
                    else {
                        if (!isOver) {
                            if (ruleResult.equals("立即过滤")) {
                                flag = true;
                                break;
                            } else if (ruleResult.equals("继续过滤")) {
                                // 如果不符合规则，则直接跳出
                                //logger.debug("下一条规则过滤");
                                flag = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return _warnFlag;
    }
     */
    /**
     * 处理告警过滤
     */
    public boolean doFilter() {
        boolean _flag = false;
        for (int i = 0; i < xml.RuleList.size(); i++) {
            WarnRuleBean rule = (WarnRuleBean) xml.RuleList.get(i);
            if (rule != null) {
                String ruleName = rule.getName();
                String ruleResult = rule.getResult();
                ArrayList orList = (ArrayList) xml.Name_ContentORMap.get(ruleName);
                // 当获取到或条件的时候，进行处理
                ////////////////////////////////or//////////////////////////////////////////
                if (orList != null && orList.size() > 0) {
                    for (int j = 0; j < orList.size(); j++) {
                        // 获取到的告警列表
                        String content = (String) orList.get(j);
                        String[] values = null;
                        if (content.indexOf("告警级别") >= 0) {
                            String[] opts = { ">=", "<=", "!=", ">", "<", "=" };
                            values = formatCotont(content, opts);
                        } else {
                            String[] opts = { "!=", "=" };
                            values = formatCotont(content, opts);
                        }
                        if (values != null) {
                            //如：设备类型!＝SE800 or 告警等级!＝一般告警
                            if (doWarn(values))
                                //当满足过滤条件,立即返回true,以后的过滤规则不需要再过滤
                                return true;
                            else{
                                //当or条件中任何一个都不满足时
                                _flag = false;
                            }
                        }
                    }
                }
                ////////////////////////////////and//////////////////////////////////////////
                ArrayList andList = (ArrayList) xml.Name_ContentANDMap.get(ruleName);
                if (andList != null && andList.size() > 0) {
                    // 对与告警进行处理
                    int j = 0;
                    for (j = 0; j < andList.size(); j++) {
                        String content = (String) andList.get(j);
                        String[] values = null;
                        if (content.indexOf("告警级别") >= 0) {
                            String[] opts = { ">=", "<=", "!=", ">", "<", "=" };
                            values = formatCotont(content, opts);
                        } else {
                            String[] opts = { "!=", "=" };
                            values = formatCotont(content, opts);
                        }
                        if (values != null)
                            // 对告警进行处理,如果告警处理认为不符合一个子规则，就退出该循环，只有所有的子规则都通过才认为通过
                            //如：设备类型!＝SE800 and 告警等级!＝严重告警
                            //设备类型:{Cisco 3550} 严重告警   [满足,不满足]false (未过滤)
                            //设备类型:{Cisco 3550} 一般告警   [满足,满足]true (被过滤)
                            //设备类型:{SE800} 严重告警  [不满足,]false
                            //设备类型:{SE800} 一般告警  [不满足,]false
                            if (doWarn(values))
                                //对告警进行处理,如果告警处理认为不符合一个子规则,就返回false
                                //如果and中某条满足,就设置flag为true;
                                _flag = true;
                            else{
                                //当and中的条件都满足时，则为true
                                //如果其中一条不满足,则直接返回false
                                return false;
                            }
                    }
                }
                // 如果没有与运算符，则对或运算再进行处理
                if (ruleResult.equals("立即过滤")) {
                    //前面已对_flag赋值了，这里直接返回flag值
                    //logger.debug("立即过滤flag=" + _flag);
                    return _flag;
                } else if (ruleResult.equals("继续过滤")) {
                    //当最后一条规则也是 继续过滤 时，即下面没有过滤规则了。
                    continue;
                }
            }
        }
        //返回flag值
        return _flag;
    }
}