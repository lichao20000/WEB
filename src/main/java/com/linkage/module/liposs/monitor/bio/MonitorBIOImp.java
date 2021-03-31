package com.linkage.module.liposs.monitor.bio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnit;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.liposs.monitor.dao.MonitorDAO;
import com.linkage.system.utils.DateTimeUtil;
import com.linkage.system.utils.StringUtils;
import com.linkage.system.utils.chart.ChartTools;
import com.linkage.system.utils.chart.ChartUtil;
import com.linkage.system.utils.database.DBUtil;

@SuppressWarnings({"rawtypes","deprecation","unchecked"})
public class MonitorBIOImp implements MonitorBIO
{
	private static final Logger LOG = Logger.getLogger(MonitorBIOImp.class);
    private MonitorDAO mdao = null;
    /** 画图类*/
    private ChartUtil chart = null;
	/**dao层用到的表名列表*/
    private List<String> tableNames = null;
    /**dao层用到的统计开始时间,单位秒*/
    private long startTime = 0L;
    /**dao层用到的统计结束时间，单位秒*/
    private long endTime = 0L;
    /**返回给action的json*/
    private String json = null;
    /**时序图的标题*/
    String title = null;
    /**时序图X轴时间显示格式*/
    String pattern = null;
    /**时序图X轴单位的刻度*/
    int unit = 0;
    /**时序图两个刻度之间相差的数量*/
    int cnt = 0;

    private int rptType = 0;
    private String rptTime = "";
    private String phaseStart = "";
    private String phaseEnd = "";


    @Override
    public String getAllMonitorHostListJson()
    {
    	LOG.debug("BIO-获取监控点");
        List<Map> monitorHostList = mdao.getAllMonitorHostList();
        if(monitorHostList != null && !monitorHostList.isEmpty())
        {
            StringBuilder json = new StringBuilder(1000);
            for(Map value :monitorHostList)
            {
            	json.append(value.get("hostid")+"#"+value.get("hostdesc")+",");
            }
            LOG.debug("BIO-获取监控点完毕");
            return json.deleteCharAt(json.length()-1).toString();
        }else
        {
        	LOG.debug("BIO-获取监控点完毕");
            return "{}";
        }
    }

    @Override
    public String getMonitorTypeListByHostIDJson(String host_id)
    {
    	LOG.debug("BIO-获取监控类型");
        List<Map> monitorTypeList = mdao.getMonitorTypeListByHostID(host_id);
        if(monitorTypeList != null && !monitorTypeList.isEmpty())
        {
            StringBuilder json = new StringBuilder(1000);
            for(Map value :monitorTypeList)
            {
            	json.append(value.get("typeid")+"#"+value.get("typedesc")+",");
            }
            LOG.debug("BIO-获取监控类型完毕");
            return json.deleteCharAt(json.length()-1).toString();
        }else
        {
        	LOG.debug("BIO-获取监控类型完毕");
            return "{}";
        }
    }

    @Override
    public String paintMRTG(int rptType, String rptTime, String monitor_host,
            String monitor_type, HttpServletRequest request, String phaseStart,
            String phaseEnd,int width,int height,String tableWidth,String tableStyle)
    {
    	LOG.warn("BIO-画图");
        this.rptType = rptType;
        this.rptTime = rptTime;
        this.phaseStart = phaseStart;
        this.phaseEnd = phaseEnd;

        Map monitorHostAndTypeMap = mdao.getMonitorHostAndTypeByHostIDAndTypeID(monitor_host,monitor_type);

        //先查询所有数据并且处理统计结果
        Map<String,Object> returnDataMap = getAllData(monitorHostAndTypeMap);

        if (returnDataMap != null && !returnDataMap.isEmpty() && !returnDataMap.containsKey("error"))
        {
            long startTime2 = new DateTimeUtil(this.phaseStart).getDateTime().getTime()/1000L;
            int timePeroid = Integer.parseInt(monitorHostAndTypeMap.get("typeperoid") + "");//粒度，CPU为2秒，工单为300秒，内存、TCP等为1秒
            String typedesc = (String)monitorHostAndTypeMap.get("typedesc");//Y轴标题为监控类型描述
            double maxValue = (Double)returnDataMap.get("maxValue");
            double minValue = (Double)returnDataMap.get("minValue");
            double avgValue = (Double)returnDataMap.get("avgValue");

            int timePeroidNum = (Integer)returnDataMap.get("timePeroidNum");

            //查询出来的数据列
            double[] allData = (double[])returnDataMap.get("allData");

            String[] lineNames = new String[2];
            lineNames[0] = typedesc;
            lineNames[1] = "阈值";

            String xName = "time";
            String yName = "value";

            //曲线图要展现的数据，两条，一条为数值，另外一条为阈值
            List<Map>[] allMRTGLines = new ArrayList[2];
            for(int i = 0 ;i < 2;i++)
            {
                List<Map> listMap = new ArrayList<Map>();
                allMRTGLines[i] = listMap;
            }

            // 行数据扫描
            for (int row = 0; row < timePeroidNum; row++)
            {
                // 得到时间
                long collecttime = startTime2 + timePeroid * row;

                // 得到监控值
                Map valueMap = new HashMap();
                valueMap.put(xName, collecttime);
                valueMap.put(yName, allData[row]);
                allMRTGLines[0].add(valueMap);

                // 得到阈值
                Map valveMap = new HashMap();
                valveMap.put(xName, collecttime);
                valveMap.put(yName,Double.parseDouble(monitorHostAndTypeMap.get("typevalve")+""));
                allMRTGLines[1].add(valveMap);
            }

            //组装图
            String imgUrl = ChartTools.getChartWithContextUrl(getPortMRTG(
                    allMRTGLines,lineNames, typedesc, this.rptType, this.rptTime, this.phaseStart, this.phaseEnd),
                    width, height, request);

            boolean isQueue = false;
            String currValue = "-";
            String queueValue = "-";
            String displayType =  LipossGlobals.getLipossProperty("alarmMonitor.displayType");
            if(!StringUtil.IsEmpty(displayType) && "2".equals(displayType)){
            	isQueue = true;
            	Map monitorQueueMap = getQueueValue(monitor_host,monitor_type);
            	currValue = StringUtil.getStringValue(monitorQueueMap.get("currActive"));
            	queueValue = StringUtil.getStringValue(monitorQueueMap.get("currWait"));
            }

            String gatherDataStr = doMRTGHTML(imgUrl,maxValue,minValue,avgValue,monitorHostAndTypeMap,tableWidth,tableStyle,isQueue,currValue,queueValue);
            LOG.debug("BIO-画图完毕");
            return gatherDataStr;
        }else{
            return (String)returnDataMap.get("error");
        }
    }

    private Map<String,Object> getAllData(Map monitorHostAndTypeMap)
    {
        Map<String,Object> returnDataMap = new HashMap<String,Object>();
        //将格式化日期转换为Cal
        DateTimeUtil dt = new DateTimeUtil(rptTime);
        Calendar cal = null;

        //粒度周期
        int timePeroid = Integer.parseInt(monitorHostAndTypeMap.get("typeperoid") + "");

        String dateStrForShow = "yyyy-MM-dd HH:mm";
        String dateStr2ForFotmat = "yyyy-MM-dd";
        //设定参数
        switch (rptType)
        {
            case 1://日
                phaseStart = getTimeStr(new DateTimeUtil(rptTime).getLongTime(),new SimpleDateFormat(dateStr2ForFotmat));
                phaseEnd = phaseStart;//重置页面展示时间
                break;
            case 2://周
                long sT = new DateTimeUtil(dt.getFirstDayOfWeek("CN")+" 00:00:00").getLongTime();
                phaseStart = getTimeStr(sT,new SimpleDateFormat(dateStr2ForFotmat));
                phaseEnd = getTimeStr(sT + 7 * 24 * 60 * 60 - 1,new SimpleDateFormat(dateStr2ForFotmat));//重置页面展示时间
                dateStrForShow = "yyyy-MM-dd HH";
                break;
            case 3://月
                cal = Calendar.getInstance();
                cal.setTime(new Date(dt.getLongTime()*1000L));
                cal.add(Calendar.MONTH, 1);
                phaseStart = getTimeStr(dt.getLongTime(),new SimpleDateFormat(dateStr2ForFotmat));
                phaseEnd = getTimeStr(cal.getTimeInMillis()/1000L-1,new SimpleDateFormat(dateStr2ForFotmat));
                dateStrForShow = "yyyy-MM-dd";
                break;
//            case 4://年报表使用小时粒度，其他均使用5分钟粒度
//                timePeroid = 300*12*24;
//                dateStrForShow = "yyyy-MM";
//                cal = Calendar.getInstance();
//                cal.setTime(new Date(dt.getLongTime()*1000L));
//                cal.add(Calendar.YEAR, 1);
//                phaseStart = getTimeStr(dt.getLongTime(),new SimpleDateFormat(dateStr2ForFotmat));
//                phaseEnd = getTimeStr(cal.getTimeInMillis()/1000L-1,new SimpleDateFormat(dateStr2ForFotmat));
//                break;
        }

        Date startTime = new DateTimeUtil(phaseStart).getDateTime();
        Date endTime = new DateTimeUtil(phaseEnd).getDateTime();

        endTime.setMinutes(59);
        endTime.setSeconds(59);

        if(rptType != 6){
        	//除小时报表外，均为23点
            endTime.setHours(23);
        }

        // 5分钟粒度数量，需要+1
        int timePeroidNum = (int) Math.ceil(
        		((endTime.getTime()/1000L - startTime.getTime()/1000L) / (long) timePeroid)) + 1;

        long startTime2 = startTime.getTime()/1000L;

        LOG.warn("timePeroidNum=" + timePeroidNum);

        //存储数据
        double[] allData = initAllData(timePeroidNum);

        // 查询阶段内表的数量，哪些表存在 tab_monitor_data_2014_11_29
        Map<String, Object> queryConditions = new HashMap<String, Object>();
        queryConditions.put("startTime", startTime);
        queryConditions.put("endTime", endTime);
        queryConditions.put("rptType", rptType);
        queryConditions.put("rptTime", rptTime);
        List<String> tableList = getExistTables(queryConditions);

        List<Double> valueDataList = new ArrayList<Double>();
        double maxValue = -1;
        double minValue = 100000;
        double countValue = 0.0;
        double avgValue = 0.0;
        // 拼装SQL
        StringBuilder whereSql = new StringBuilder();
        // 加一下时间范围
        whereSql.append(" where gathertime >= ").append(startTime.getTime()/1000L)
                .append(" and gathertime < ").append(endTime.getTime()/1000L);
        //添加hostID和TypeID
        whereSql.append(" and hostid = ").append(monitorHostAndTypeMap.get("hostid"));
        whereSql.append(" and typeid = ").append(monitorHostAndTypeMap.get("typeid"));

        // 循环查询单天的数据
        for (String tableName : tableList)
        {
            // 查询数据
            queryConditions.clear();
            queryConditions.put("whereSql", whereSql.toString());
            queryConditions.put("tableName", tableName);

            List<Map> resultData = mdao.queryDataByConditions(queryConditions);

            if (resultData != null && !resultData.isEmpty())
            {
                for (Map oneData : resultData)
                {
                    long gathertime = Long.parseLong(oneData.get("gathertime") + "");

                    // 计算填充的时间行位置，以粒度为一行 ---5分钟(0对应0~299,1对应300~599)
                    int index = (int) ((gathertime - startTime2) / (long) timePeroid);

                    double monitorvalue = Double.parseDouble(
                    		oneData.get("monitorvalue") == null ? "-1" : oneData.get("monitorvalue") + "");

                    // 嵌入allData中
                    if (allData[index] == -1 && monitorvalue != -1)
                    {
                        // 填入对应格--端口数据
                        allData[index] = monitorvalue;
                        valueDataList.add(monitorvalue);

                        //取最大最小值
                        if(monitorvalue > maxValue){
                            maxValue = monitorvalue;
                        }
                        if(monitorvalue < minValue){
                            minValue = monitorvalue;
                        }
                        countValue += monitorvalue;
                    }
                }
                avgValue = countValue / valueDataList.size();
            }
        }

        // 对于实际上没有中断的地方进行填补   //待定，是否需要填充中断数据
        //fillEmptyData(allData, deviceIfNum, timePeroidNum);

        returnDataMap.put("timePeroidNum", timePeroidNum);
        returnDataMap.put("allData", allData);
        returnDataMap.put("maxValue", maxValue);
        returnDataMap.put("minValue", minValue);
        returnDataMap.put("avgValue", avgValue);
        returnDataMap.put("startTime2", startTime2);
        returnDataMap.put("dateStrForShow", dateStrForShow);
        return returnDataMap;
    }

    /**
     * 获取当前存活队列和等待队列的值
     */
	private Map<String,String> getQueueValue(String host_id,String type_id)
	{
		Map<String,String> queueValueMap = new HashMap<String, String>();
    	long currTime = System.currentTimeMillis();
    	// 查询当前存活线程的值
    	DateTimeUtil dateTimeUtil = new DateTimeUtil(currTime);
    	String tabMonitorData = "tab_monitor_data_" + dateTimeUtil.getYear() + "_" +
    			(dateTimeUtil.getMonth() < 10 ? ("0" + dateTimeUtil.getMonth()) : dateTimeUtil.getMonth()) + "_" +
    			(dateTimeUtil.getDay() < 10 ? ("0" + dateTimeUtil.getDay()) : dateTimeUtil.getDay());
    	Map monitorDataMap = mdao.getCurrActiveCount(host_id, type_id, tabMonitorData);
    	String currActive = "-";
    	if(null != monitorDataMap && !monitorDataMap.isEmpty()){
    		long activeGatherTime = StringUtil.getLongValue(monitorDataMap.get("gathertime"));
    		if((currTime/1000 - activeGatherTime <= StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("alarmMonitor.monitorSpace")))){
    			currActive = StringUtil.getStringValue(monitorDataMap.get("monitorvalue"));
    		}
    	}
    	queueValueMap.put("currActive", currActive);

    	// 查询当前队列的值
    	String tabMonitorOther = "tab_monitor_other_" + dateTimeUtil.getYear() + "_" +
    			(dateTimeUtil.getMonth() < 10 ? ("0" + dateTimeUtil.getMonth()) : dateTimeUtil.getMonth()) + "_" +
    			(dateTimeUtil.getDay() < 10 ? ("0" + dateTimeUtil.getDay()) : dateTimeUtil.getDay());
    	Map monitorOtherMap = mdao.getCurrWaitCount(host_id, type_id, tabMonitorOther);
    	String currWait = "-";
    	if(null != monitorOtherMap && !monitorOtherMap.isEmpty()){
    		long waitGatherTime = StringUtil.getLongValue(monitorOtherMap.get("gathertime"));
    		if(currTime/1000 - waitGatherTime <=5){
    			currWait = StringUtil.getStringValue(StringUtil.getIntegerValue(monitorOtherMap.get("monitorvalue")));
    		}
    	}
    	queueValueMap.put("currWait", currWait);
    	return queueValueMap;
    }

    /**
     * [初始化一个指定大小的double数组，初始为-1，用来区别0和空]
     */
    private double[] initAllData(int timePeroidNum)
    {
        double[] allData = new double[timePeroidNum];
        for(int r = 0;r <timePeroidNum ;r ++){
            allData[r] = -1;//这个地方需要谨慎对待，如果是-1，则不计算到结果里面去，如果是0，则计算到结果里面去，待定
        }
        return allData;
    }

    /**
     * @param imgURL 每个图的图片URL
     * @param gatherData  端口个数+2 行，多个指标列+第一列，前一个参数是列的个数，后一个参数是行的个数，与allData保持一致
     */
    private String doMRTGHTML(String imgURL,double maxValue,double minValue,double avgValue,Map monitorHostAndTypeMap,String width,String style,boolean isQueue,String currValue,String queueValue)
    {
        StringBuilder sb = new StringBuilder();

        String classStyle = "title_2";

        classStyle = "even";
        // 最后合并图
        sb.append("<table id=\"bodyList\" class=\"listtable\" ");
        sb.append(width);
        sb.append(style);
        sb.append(">");
        sb.append("<tr><td class=\"").append(classStyle)
                .append("\">监控点名称</td><td>")
                .append("<font style=\"font-weight:bold;\">")
                .append(monitorHostAndTypeMap.get("hostdesc") + "")
                .append("</font>");
        sb.append("</td><td class=\"").append(classStyle)
                .append("\">监控类型名称</td><td>")
                .append("<font style=\"font-weight:bold;\">")
                .append(monitorHostAndTypeMap.get("typedesc") + "")
                .append("</font>")
                .append("</td><td class=\"").append(classStyle)
                .append("\">阈值</td><td>")
                .append("<font style=\"font-weight:bold;\">")
                .append(monitorHostAndTypeMap.get("typevalve") + "")
                .append("</font>")
                .append("</td></tr>");

        sb.append("<tr><td colspan=\"6\" style=\"text-align:center;\">");
        sb.append("<img src=\"").append(imgURL).append("\"/>");
        sb.append("</td></tr>");

        // 线程队列
        if(isQueue){
        	// 当前值
        	sb.append("<tr><td class=\"").append(classStyle)
	            .append("\" align=\"right\">当前值</td><td>").append(cssValue(currValue,StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
	            .append("</td>");
		    // 等待队列值
		    sb.append("<td class=\"").append(classStyle)
		            .append("\" align=\"right\">等待队列值</td><td>").append(cssValue(queueValue,StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
		            .append("</td>");
		    // 阈值
		    sb.append("<td class=\"").append(classStyle)
		            .append("\" align=\"right\">阈值</td><td>").append(cssValue(StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve")),StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
		            .append("</td></tr>");
        }

        // 增加极值统计数据
        // 最大
        sb.append("<tr><td class=\"").append(classStyle)
                .append("\" align=\"right\">最大值</td><td>").append(cssValue(StringUtil.getStringValue(maxValue),StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
                .append("</td>");
        // 最小
        sb.append("<td class=\"").append(classStyle)
                .append("\" align=\"right\">最小值</td><td>").append(cssValue(StringUtil.getStringValue(minValue),StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
                .append("</td>");
        // 平均
        sb.append("<td class=\"").append(classStyle)
                .append("\" align=\"right\">平均值</td><td>").append(cssValue(StringUtil.getStringValue(avgValue),StringUtil.getStringValue(monitorHostAndTypeMap.get("typevalve"))))
                .append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }

    /**
     * 当前值对比阀值，返回指定样式的值
     * @param preValue 当前值
     * @param typevalve 阀值
     */
    private String cssValue(String preValue,String typevalve)
    {
    	if("-".equals(preValue)){
    		return "<font color=\"red\" style=\"font-weight:bold;\">" + preValue + "</font>";
    	}

    	if(StringUtil.getDoubleValue(preValue) > StringUtil.getDoubleValue(typevalve)){
    		return "<font color=\"red\" style=\"font-weight:bold;\">" + StringUtils.formatNumber(preValue,2) + "</font>";
    	}

    	return "<font color=\"green\" style=\"font-weight:bold;\">" + StringUtils.formatNumber(preValue,2) + "</font>";
    }

    private JFreeChart getPortMRTG(List<Map>[] allMRTGLines,String[] lineNames, String yAName, int reportType, String rptTime, String phaseStart, String phaseEnd)
    {
      String xAName = "时间";
      String xName = "time";
      String yName = "value";

      initParams(reportType, rptTime, phaseStart, phaseEnd);

      //曲线的名字和Y轴的名字保持一致
      chart.setXAxisUnit(unit,cnt,pattern);
      return chart.createSTP(title,xAName,yAName,lineNames,allMRTGLines,xName,yName,0);
    }

    public List<String> getExistTables(Map<String,Object> queryConditions)
    {
        Date startTime = (Date)queryConditions.get("startTime");
        Date endTime = (Date)queryConditions.get("endTime");
        int rptType = (Integer)queryConditions.get("rptType");
        String rptTime = (String)queryConditions.get("rptTime");

        return DBUtil.getExistTables(getAllTableStr(startTime,endTime,rptType,rptTime));
    }

    private List<String> getAllTableStr(Date startTime2, Date endTime2,int rptType,String rptTime)
    {
        List<String> allStr = new ArrayList<String>();

        // 今天
        Calendar ca = Calendar.getInstance();
        ca.setTime(startTime2);

        int year = 0;
        int month = 0;
        int day = 0;

        do{
            year = ca.get(Calendar.YEAR);
            month = ca.get(Calendar.MONTH) + 1;
            day = ca.get(Calendar.DAY_OF_MONTH);

            // 默认使用原始表
            allStr.add("tab_monitor_data_" + year + "_" + (month < 10 ? "0":"") + month + "_" + (day < 10 ? "0":"") + day);

            if (year == (endTime2.getYear() + 1900)
                    && month == (endTime2.getMonth() + 1)
                    && day == endTime2.getDate())
            {
                break;
            }

            // 获取后一天
            ca.add(Calendar.DAY_OF_YEAR, 1);
        }while (true);

        if (rptType != 6){
        	// 除小时报表外，均为23点
            endTime2.setHours(23);
        }

        startTime = startTime2.getTime() / 1000L;
        endTime = endTime2.getTime() / 1000L;
        LOG.warn("allStr==" + allStr);

        return allStr;
    }

  private void initParams(int reportType, String rptTime, String phaseStart, String phaseEnd)
    {
      int secPerHour = 3600;
      int secPerDay = 24 * secPerHour;
      this.pattern = "dd";
      this.unit = DateTickUnit.DAY;//2是日，3是小时
      this.cnt = 1;

      switch (reportType)
      {
	      case 1:
	            this.startTime = new DateTimeUtil(phaseStart).getLongTime();
	            this.endTime = (new DateTimeUtil(phaseEnd).getLongTime() + secPerDay);
	        this.title = "日报表";
	        break;
	      case 2:
	            this.startTime = new DateTimeUtil(phaseStart).getLongTime();
	            this.endTime = (new DateTimeUtil(phaseEnd).getLongTime() + secPerDay);
	        this.title = "周报表";
	        break;
	      case 3:
	        this.startTime = new DateTimeUtil(phaseStart + " 00:00:00").getLongTime();
	        this.endTime = (new DateTimeUtil(phaseEnd + " 00:00:00").getLongTime() + secPerDay);
	        this.title = "月报表";
	        break;
	//      case 4:
	//        this.startTime = new DateTimeUtil(rptTime.split("-")[0] + "-01-01").getLongTime();
	//        this.endTime = (new DateTimeUtil(rptTime.split("-")[0] + "-12-31").getLongTime() + secPerDay);
	//        this.title = "年报表";
	//        this.pattern = "M";
	//        this.unit = DateTickUnit.MONTH;
	//        break;
	      case 5:
	        this.startTime = new DateTimeUtil(phaseStart).getLongTime();
	        this.endTime = (new DateTimeUtil(phaseEnd).getLongTime() + secPerDay);
	        this.title = "阶段报表";
	        break;
	      case 6:
	        this.startTime = new DateTimeUtil(phaseStart).getLongTime();
	        this.endTime = (new DateTimeUtil(phaseEnd).getLongTime() + secPerHour);
	        this.title = "小时报表";
	        this.pattern = "HH";
	        this.unit = DateTickUnit.HOUR;
	        break;
	      }

      title = (title + "(" + new DateTimeUtil(startTime * 1000L).getLongDate() + "到" +
        new DateTimeUtil(endTime * 1000L).getLongDate() + ")");
    }

  	/**
  	 * 综合监控
  	 */
	@Override
	public String paintMRTGAll(int rptType, String rptTime,String monitor_host, String monitor_type,
			HttpServletRequest request, String phaseStart, String phaseEnd,int width,int height,String tableWidth,String tableStyle)
	{
		String monitorResultAll = "";
		// 查询当前主机的所有监控类型
		List<Map> monitorTypeList = mdao.getMonitorTypeListByHostID(monitor_host);

		if(null != monitorTypeList && !monitorTypeList.isEmpty() && null != monitorTypeList.get(0)){

			List<String> monitorTypeNewList = new ArrayList<String>();
			String cpuType = "-";
			String memType = "-";
			String hardType = "-";
			for(Map monitorType : monitorTypeList){
				if (StringUtil.getStringValue(monitorType.get("typeid")).equals(StringUtil.getStringValue(LipossGlobals.getLipossProperty("alarmMonitor.cpuType")))){
					cpuType = StringUtil.getStringValue(monitorType.get("typeid"));
				} else if(StringUtil.getStringValue(monitorType.get("typeid")).equals(StringUtil.getStringValue(LipossGlobals.getLipossProperty("alarmMonitor.memType")))){
					memType = StringUtil.getStringValue(monitorType.get("typeid"));
				} else if(StringUtil.getStringValue(monitorType.get("typeid")).equals(StringUtil.getStringValue(LipossGlobals.getLipossProperty("alarmMonitor.hardType")))){
					hardType = StringUtil.getStringValue(monitorType.get("typeid"));
				} else{
					monitorTypeNewList.add(StringUtil.getStringValue(monitorType.get("typeid")));
				}
			}
			if(!"-".equals(cpuType)){
				monitorTypeNewList.add(cpuType);
			}
			if(!"-".equals(memType)){
				monitorTypeNewList.add(memType);
			}
			if(!"-".equals(hardType)){
				monitorTypeNewList.add(hardType);
			}

			for(String monitorTypeNew : monitorTypeNewList){
				if(!StringUtil.getStringValue(monitorTypeNew).equals(monitor_type)){
					monitorResultAll += paintMRTG(rptType, rptTime, monitor_host,monitorTypeNew, request, phaseStart, phaseEnd,width,height,tableWidth,tableStyle);
				}
			}
		}
		LOG.warn("monitorResultAll:" + monitorResultAll);
		return monitorResultAll;
	}

	/**
	 * 获取所有主机信息
	 */
	public List<Map> getAllMonitorHostList()
	{
		return mdao.getAllMonitorHostList();
	}

	/**
	 * 获取该主机当前进程的状态
	 */
	public List<Map<String,String>> getHostProgressList(String hostId)
	{
		// 根据主机ID获取该主机名称
		List<Map> hostMonitorList = mdao.getAllMonitorHostList();
		String hostName = "-";
		if(null != hostMonitorList && !hostMonitorList.isEmpty()){
			for(Map hostMonitor : hostMonitorList){
				if(StringUtil.getStringValue(hostMonitor.get("hostid")).equals(hostId)){
					hostName = StringUtil.getStringValue(hostMonitor.get("hostname"));
				}
			}
		}

		// 查询该主机监控的进程类型
		List<Map> hostProgressType = mdao.getHostProgressType(hostId);
		if(null == hostProgressType || hostProgressType.isEmpty()){
			LOG.warn("该主机监控的进程为空");
			return null;
		}

		long currTime = System.currentTimeMillis();
    	// 查询当前主机，当前监控类型
    	DateTimeUtil dateTimeUtil = new DateTimeUtil(currTime);
    	String tabMonitorProgress = "tab_monitor_pro_" + dateTimeUtil.getYear() + "_" +
    			(dateTimeUtil.getMonth() < 10 ? ("0" + dateTimeUtil.getMonth()) : dateTimeUtil.getMonth()) + "_" +
    			(dateTimeUtil.getDay() < 10 ? ("0" + dateTimeUtil.getDay()) : dateTimeUtil.getDay());
    	LOG.warn("tabMonitorProgress=" + tabMonitorProgress);
    	List<Map<String,String>> currProgressList = new ArrayList<Map<String,String>>();
		for(Map hostProgress : hostProgressType)
		{
			Map<String,String> currProgressMap = new HashMap<String, String>();
			currProgressMap.put("hostid", hostId);
			currProgressMap.put("hostname", hostName);
			currProgressMap.put("progressid", StringUtil.getStringValue(hostProgress.get("progressid")));
			currProgressMap.put("progresstype", StringUtil.getStringValue(hostProgress.get("progresstype")));
			currProgressMap.put("progressDesc", "未知");
			Map progressMap = mdao.getCurrHostProgress(hostId,StringUtil.getStringValue(hostProgress.get("progressid")),tabMonitorProgress);
			if(null != progressMap && !progressMap.isEmpty()){
				long gathertime = StringUtil.getLongValue(progressMap.get("gathertime"));
				if((currTime/1000 - gathertime <= StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("alarmMonitor.monitorSpace")))){
	    			 int monitorValue = StringUtil.getIntegerValue(progressMap.get("monitorvalue"))	;
	    			 if(monitorValue >= 2){
	    				 currProgressMap.put("monitorValue", StringUtil.getStringValue(monitorValue));
	    				 currProgressMap.put("progressDesc", "UP");
	    			 }else{
	    				 currProgressMap.put("monitorValue", StringUtil.getStringValue(monitorValue));
	    				 currProgressMap.put("progressDesc", "DOWN");
	    			 }
	    		}
			}
			currProgressList.add(currProgressMap);
		}
		return currProgressList;
	}

	/**
	 * 获取当前小时的值
	 */
	public int getHourValue(long currTime)
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(currTime);
		return dateTimeUtil.getHour();
	}

	/**
	 * 监控平台，获取监控列表
	 */
	public List<Map<String,String>> getHostMonitorList(long currTime)
	{
		List<Map> monitorHostList = getAllMonitorHostList();
		if(null == monitorHostList || monitorHostList.isEmpty()){
			LOG.error("监控主机列表为空，请检测数据是否正确！");
			return null;
		}
		List<Map<String,String>> monitorList = new ArrayList<Map<String,String>>();
		for(Map monitorHost : monitorHostList){
			monitorList.add(getMonitorMap(StringUtil.getStringValue(monitorHost.get("hostid")),StringUtil.getStringValue(monitorHost.get("hostname")),currTime));

		}
		return monitorList;
	}

	private Map<String,String> getMonitorMap(String hostId,String hostName,long currTime)
	{
		Map<String,String> monitorMap = new HashMap<String, String>();
		monitorMap.put("hostid", hostId);
		monitorMap.put("hostname",hostName);
		monitorMap.put("cpu", "-");
		monitorMap.put("mem", "-");
		monitorMap.put("hard", "-");
		monitorMap.put("ntp", "未知");
		monitorMap.put("serv", "正常");
		monitorMap.put("progress", "正常");
		getMonitorByHostid(hostId,monitorMap,currTime);
		return monitorMap;
	}

	private void getMonitorByHostid(String hostid,Map<String,String> monitorMap,long currTime)
	{
    	DateTimeUtil dateTimeUtil = new DateTimeUtil(currTime);

		//根据主机ID获取该主机监控的所有类型
		List<Map> monitorTypeList = mdao.getMonitorTypeListByHostID(hostid);
		if(null != monitorTypeList && !monitorTypeList.isEmpty() && null != monitorTypeList.get(0))
		{
	    	String tabMonitorData = "tab_monitor_data_" + dateTimeUtil.getYear() + "_" +
	    			(dateTimeUtil.getMonth() < 10 ? ("0" + dateTimeUtil.getMonth()) : dateTimeUtil.getMonth()) + "_" +
	    			(dateTimeUtil.getDay() < 10 ? ("0" + dateTimeUtil.getDay()) : dateTimeUtil.getDay());
			for(Map monitorType : monitorTypeList)
			{
				if(StringUtil.getStringValue(monitorType.get("typeid")).equals(LipossGlobals.getLipossProperty("alarmMonitor.allType"))){
					continue;
				}
				String currValue = getMonitorServValue(hostid,StringUtil.getStringValue(monitorType.get("typeid")),tabMonitorData,currTime);
				if(StringUtil.getStringValue(monitorType.get("typeid")).equals(LipossGlobals.getLipossProperty("alarmMonitor.cpuType"))){
					monitorMap.put("cpu", currValue);
				}
				if(StringUtil.getStringValue(monitorType.get("typeid")).equals(LipossGlobals.getLipossProperty("alarmMonitor.memType"))){
					monitorMap.put("mem", currValue);
				}
				if(StringUtil.getStringValue(monitorType.get("typeid")).equals(LipossGlobals.getLipossProperty("alarmMonitor.hardType"))){
					monitorMap.put("hard", currValue);
				}
				if("-".equals(currValue)){
					monitorMap.put("serv", "未知");
				}

				if("正常".equals(monitorMap.get("serv"))
						&& (StringUtil.getDoubleValue(currValue) > StringUtil.getDoubleValue(monitorType.get("typevalve"))))
				{
					monitorMap.put("serv", "告警");
				}
			}
		}else{
			monitorMap.put("serv", "正常");
		}
		monitorTypeList=null;

		// 根据主机ID获取该主机的所有进程
		List<Map> monitorProgressList = mdao.getHostProgressType(hostid);
		if(null != monitorProgressList && !monitorProgressList.isEmpty() && null != monitorProgressList.get(0))
		{
			String tabMonitorPro = "tab_monitor_pro_" + dateTimeUtil.getYear() + "_" +
	    			(dateTimeUtil.getMonth() < 10 ? ("0" + dateTimeUtil.getMonth()) : dateTimeUtil.getMonth()) + "_" +
	    			(dateTimeUtil.getDay() < 10 ? ("0" + dateTimeUtil.getDay()) : dateTimeUtil.getDay());
			LOG.warn("tabMonitorPro=" + tabMonitorPro);
			for(Map monitorProgress : monitorProgressList){
				String progressState = getMonitorProgressValue(hostid,
											StringUtil.getStringValue(monitorProgress.get("progressid")),
											tabMonitorPro,currTime);
				if("-".equals(progressState)){
					monitorMap.put("progress", "未知");
					break;
				}
				if("异常".equals(progressState)){
					monitorMap.put("progress", "异常");
				}
			}
		}else{
			monitorMap.put("progress", "正常");
		}
		monitorProgressList=null;

		// 根据主机ID获取该主机的时间
		Map ipTimeMap = mdao.getHostIpTime("16");
		if(null != ipTimeMap && !ipTimeMap.isEmpty())
		{
			Map monitorIpTimeMap = mdao.getHostIpTime(hostid);

			if(null != monitorIpTimeMap && !monitorIpTimeMap.isEmpty())
			{
				long time=StringUtil.getLongValue(ipTimeMap,"host_time");
				long ipTime=StringUtil.getLongValue(monitorIpTimeMap,"host_time");
				//时间差30秒内都为正常
				if(time-ipTime>30 || ipTime-time>30){
					monitorMap.put("ntp", "异常");
				}else{
					monitorMap.put("ntp", "正常");
				}
			}
			monitorIpTimeMap=null;
		}
		ipTimeMap=null;
	}

	private String getMonitorServValue(String hostid, String typeid, String tabMonitorData,long currTime)
	{
		Map monitorDataMap = mdao.getCurrActiveCount(hostid,typeid,tabMonitorData);
    	LOG.warn("monitorDataMap=" + monitorDataMap);
    	String currActive = "-";
    	if(null != monitorDataMap && !monitorDataMap.isEmpty()){
    		long activeGatherTime = StringUtil.getLongValue(monitorDataMap.get("gathertime"));
    		if((currTime/1000 - activeGatherTime <= StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("alarmMonitor.monitorSpace")))){
    			currActive = StringUtil.getStringValue(StringUtil.getDoubleValue(monitorDataMap.get("monitorvalue")));
    		}
    	}
    	return currActive;
	}

	private String getMonitorProgressValue(String hostid, String progressid, String tabMonitorPro,long currTime)
	{
    	Map progressMap = mdao.getCurrHostProgress(hostid,progressid,tabMonitorPro);
    	String currActive = "-";
		if(null != progressMap && !progressMap.isEmpty()){
			long gathertime = StringUtil.getLongValue(progressMap.get("gathertime"));
			if((currTime/1000 - gathertime <= StringUtil.getIntegerValue(LipossGlobals.getLipossProperty("alarmMonitor.monitorSpace"))))
			{
    			 int monitorValue = StringUtil.getIntegerValue(progressMap.get("monitorvalue"))	;
    			 if(monitorValue >= 2){
    				 currActive = "正常";
    			 }else{
    				 currActive = "异常";
    			 }
    		}
		}
    	return currActive;
	}

	/**
	 * 获取进程历史信息分页
	 */
	public List<Map> getProHistoryList(String hostId,String progressType,String start,String end,int curPageSplitPage,int numSplitPage)
	{
		//根据主机ID和进程ID获取主机名称和进程名称
		String  monitorName = "";
		Map hostMap = mdao.getHostName(hostId);
		if(null != hostMap && !hostMap.isEmpty()){
			monitorName = StringUtil.getStringValue(hostMap.get("hostname"));
		}
		String progressName = "";
		Map progressMap = mdao.getHostProgressType(hostId,progressType);
		if(null != progressMap && !progressMap.isEmpty()){
			progressName = StringUtil.getStringValue(progressMap.get("progresstype"));
		}

		List<String> tableNameList = getDayTaleName(start,end);
		List<Map> progressHistoryList = mdao.getProgressHistoryList(hostId,progressType,start,end,tableNameList,curPageSplitPage,numSplitPage);
		if(null != progressHistoryList && !progressHistoryList.isEmpty()){
			for(Map progressHistory : progressHistoryList)
			{
				progressHistory.put("monitor_name", monitorName);
				progressHistory.put("progress_name", progressName);
				progressHistory.put("gathertime", new DateTimeUtil(StringUtil.getLongValue(progressHistory.get("gathertime"))*1000).getLongDate());
				int monitorValue = StringUtil.getIntegerValue(progressHistory.get("monitorvalue"));
				if(monitorValue >= 2){
					progressHistory.put("progressDesc", "UP");
				 }else{
					 progressHistory.put("progressDesc", "DOWN");
				 }
			}
		}
		return 	progressHistoryList;
	}

	/**
	 * 获取进程历史信息总数
	 */
	public int getProHistoryCount(String hostId,String progressType,String start,String end)
	{
		List<String> tableNameList = getDayTaleName(start,end);
		return mdao.getProgressHistoryCount(hostId,progressType,start,end,tableNameList);
	}

	private List<String> getDayTaleName(String start,String end)
	{
		// 天表集合
		List<String> tableNameList = new ArrayList<String>();

		DateTimeUtil dateTimeUtil1 = new DateTimeUtil(start);
		DateTimeUtil dateTimeUtil2 = new DateTimeUtil(end);
		int diffDay1 = (int) ((dateTimeUtil2.getLongTime() - dateTimeUtil1.getLongTime())/60/24);

		if(dateTimeUtil1.getDate().compareTo(dateTimeUtil2.getDate()) == 0){
			String tabMonitorPro = "tab_monitor_pro_" + dateTimeUtil1.getYear() + "_" +
	    			(dateTimeUtil1.getMonth() < 10 ? ("0" + dateTimeUtil1.getMonth()) : dateTimeUtil1.getMonth()) + "_" +
	    			(dateTimeUtil1.getDay() < 10 ? ("0" + dateTimeUtil1.getDay()) : dateTimeUtil1.getDay());
			tableNameList.add(tabMonitorPro);
		}else if(dateTimeUtil1.getLastDayOfMonth().compareTo(dateTimeUtil2.getDate()) < 0){
			DateTimeUtil dateTimeUtil3 = new DateTimeUtil(dateTimeUtil1.getLastDayOfMonth());
			int diffDay2 = dateTimeUtil3.getDay() -  dateTimeUtil1.getDay();
			for(int index=0;index<=diffDay2;index++){
				String tabMonitorPro = "tab_monitor_pro_" + dateTimeUtil1.getYear() + "_" +
		    			(dateTimeUtil1.getMonth() < 10 ? ("0" + dateTimeUtil1.getMonth()) : dateTimeUtil1.getMonth()) + "_" +
		    			((dateTimeUtil1.getDay() + index) < 10 ? ("0" + (dateTimeUtil1.getDay() + index)) : (dateTimeUtil1.getDay() + index));
				tableNameList.add(tabMonitorPro);
			}

			for(int index=diffDay1 - diffDay2 - 1;index>=0;index--){
				String tabMonitorPro = "tab_monitor_pro_" + dateTimeUtil2.getYear() + "_" +
		    			(dateTimeUtil2.getMonth() < 10 ? ("0" + dateTimeUtil2.getMonth()) : dateTimeUtil2.getMonth()) + "_" +
		    			((dateTimeUtil2.getDay() - index) < 10 ? ("0" + (dateTimeUtil2.getDay() - index)) : (dateTimeUtil2.getDay() - index));
				tableNameList.add(tabMonitorPro);
			}

		} else{
			for(int index=0;index<=diffDay1;index++){
				String tabMonitorPro = "tab_monitor_pro_" + dateTimeUtil1.getYear() + "_" +
		    			(dateTimeUtil1.getMonth() < 10 ? ("0" + dateTimeUtil1.getMonth()) : dateTimeUtil1.getMonth()) + "_" +
		    			((dateTimeUtil1.getDay() + index) < 10 ? ("0" + (dateTimeUtil1.getDay() + index)) : (dateTimeUtil1.getDay() + index));
				tableNameList.add(tabMonitorPro);
			}
		}
		LOG.warn("tableNameList=" + tableNameList);
		return tableNameList;
	}

	/**
	 * 获取主机的所有进程
	 */
	public String getHostProgressType(String hostid)
	{
		List<Map> monitorProgressList = mdao.getHostProgressType(hostid);
		String hostProgressType = "";
		if(null != monitorProgressList && !monitorProgressList.isEmpty() && null != monitorProgressList.get(0)){
			for(int index=0;index<monitorProgressList.size();index++){
				hostProgressType += StringUtil.getStringValue(monitorProgressList.get(index).get("progressid"));
				hostProgressType += "#";
				hostProgressType += StringUtil.getStringValue(monitorProgressList.get(index).get("progresstype"));
				if(index != monitorProgressList.size() - 1){
					hostProgressType += ",";
				}
			}
		}
		return hostProgressType;
	}

    /**
     * String yyyy-MM-dd HH:mm 类型的时间
     */
    private String getTimeStr(long startTime,SimpleDateFormat formatter)
    {
//        return new DateTimeUtil(startTime).getDatePatternStr("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime*1000L);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取最新关键日志
     */
    public List<Map<String, String>> getHostLog(String monitor_host,String progress_type)
    {
		List<Map> logList=mdao.getHostIpLog(monitor_host, progress_type);

    	if(logList==null || logList.isEmpty()){
    		return null;
    	}
    	LOG.warn("getHostLog logList:"+logList.size());

    	List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	for(Map map:logList){
    		Map<String,String> m=new HashMap<String,String>();
    		m.put("log",StringUtil.getStringValue(map,"log"));
    		list.add(m);
    	}
    	return list;
    }




    public ChartUtil getChart(){
		return chart;
	}

	public void setChart(ChartUtil chart){
		this.chart = chart;
	}

    public MonitorDAO getMdao(){
        return mdao;
    }

    public void setMdao(MonitorDAO mdao){
        this.mdao = mdao;
    }

    public List<String> getTableNames(){
        return tableNames;
    }

    public void setTableNames(List<String> tableNames){
        this.tableNames = tableNames;
    }

    public long getStartTime(){
        return startTime;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public long getEndTime(){
        return endTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
    }

    public String getJson(){
        return json;
    }

    public void setJson(String json){
        this.json = json;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getPattern(){
        return pattern;
    }

    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    public int getUnit(){
        return unit;
    }

    public void setUnit(int unit){
        this.unit = unit;
    }

    public int getCnt(){
        return cnt;
    }

    public void setCnt(int cnt){
        this.cnt = cnt;
    }
}
