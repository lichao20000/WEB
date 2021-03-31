package bio.webtopo.warn.filter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import RemoteDB.AlarmEvent;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-11
 * @category com.linkage.liposs.bio.webtopo.warn.filter
 * 版权：南京联创科技 网管科技部
 * 
 * <p>本次重新设计的实时告警牌将所有javabean和strust配置文件打包，以后如果有新需求改动，都需要在此类中升级版本，
 * 版本定义方法：当前版本是1.0,以后累计加0.1,即：1.1、1.2、1.3 ....... 2.0、2.1等
 * </p>
 * @version 1.0
 *
 */
public class ConstantEventEnv
{
	public final static String Version = "RealTimeEvent 1.0";
	
	public final static Map<String,Integer> LEVEL_MAP = new LinkedHashMap<String, Integer>(6);
	
	public final static Map<Integer,String> NUM_LEVEL_MAP = new HashMap<Integer, String>(6);
	
	public final static Map<String,String> EVENTOID_MAP = new LinkedHashMap<String, String>();
	
	public final static Map<String,Integer> RULE_TYPE_MAP = new LinkedHashMap<String, Integer>();
	
	public static final Map<String, BaseOperation> OPERATOR_MAP = new HashMap<String, BaseOperation>(6);
	
	public static final Map<String, String> CREATE_EVENT_MAP = new LinkedHashMap<String, String>(6);
	
	public static final Map<Integer, String> STATUS_NAME_MAP =  new HashMap<Integer, String>(5);
	
	public static final Map<Integer, String> STATUS_CLEAR_MAP =  new HashMap<Integer, String>(5);
	
	public static Map<String, String> TYPE_NAME_MAP = null;//存放设备类型编号和设备类型之间的对应关系
	
	public final static int LEVEL = 0;
	
	public final static int IP = 1;
	
	public final static int DEVICE_NAME = 2;
	
	public final static int EVENT_OID = 3;
	
	public final static int DEVICE_TYPE = 4;
	
	public final static int CREATOR_NAME = 5;
	
	public final static int CITY = 6;
	/**
	 * 顺序不可调整
	 */
	public final static String[] OPERATORS = { ">=", "<=",">", "<", "=" };
	
	/**
	 * 默认空过滤器(满足过滤规则/true过滤器)
	 */
	public final static BaseFilter TRUEFILTER = new BaseFilter(){

		public boolean accept(AlarmEvent event)
		{
			return true;
		}
		public String toString(){
		    return "true";
		}
	};
	/**
	 * 默认空过滤器(不满足过滤规则/false过滤器)
	 */
	public final static BaseFilter FALSEFILTER = new BaseFilter(){

		public boolean accept(AlarmEvent event)
		{
			return false;
		}
		public String toString(){
		    return "false";
		}
	};

	/**
	 * 默认一般告警过滤器
	 * 用户没有配置过滤器时，使用此对象做过滤，目的是为了减少告警过滤
	 */
	public final static BaseFilter DefaultLevelFilter = new BaseFilter(){

		public boolean accept(AlarmEvent event)
		{
			return event.m_Severity >= 3;
		}
		public String toString(){
		    //return "告警等级>=一般告警";
			return "告警等级>=" + ConstantEventEnv.NUM_LEVEL_MAP.get(3);
		}
	};
	
	/**
	 * 这个过滤器是给WebTopo用的,在WebTopo每次刷新去取新增告警时使用<br>
	 * 只取4,5两个级别的告警
	 * 
	 */
	public final static BaseFilter WebTopoFilter = new BaseFilter()
	{

		public boolean accept(AlarmEvent event)
		{
			return event.m_Severity >= 4;
		}

	};
	
	/**
	 * 对要编辑的规则的子规则进行分解 设备IP=2.2.2.4 告警级别=严重告警
	 * 
	 * @param content
	 */
	public static String[] formatCotont(String content) {
        	content = content.trim();
        	// 判断是否是告警等级的过滤
        	int pos = -1;
        	String opt = "";
        	String[] opts = ConstantEventEnv.OPERATORS;
        	for (int i = 0; i < opts.length; i++) {
        	    if (content.indexOf(opts[i]) > 0) {
        		pos = content.indexOf(opts[i]);
        		opt = opts[i];
        		break;
        	    }
        	}
        
        	String[] values = new String[3];
        	values[0] = content.substring(0, pos);
        	values[0] = values[0].trim();
        	values[1] = opt;
        	values[1] = values[1].trim();
        	values[2] = content.substring(pos + opt.length());
        	values[2] = values[2].trim();
        	// log.debug("分解：" + Arrays.asList(values));
        	return values;
	}
	/**
	 * 初始化设备型号与型号名称之间的对应关系
	 */
	@SuppressWarnings("unchecked")
	private static void initTypeNameMap() {
        	String mysql = "select device_model_id,device_model from gw_device_model";
        	PrepareSQL psql = new PrepareSQL(mysql);
        	Cursor cursor = DataSetBean.getCursor(psql.getSQL());
        	TYPE_NAME_MAP = new HashMap<String,String>(cursor.getRecordSize());
        	Map map = cursor.getNext();
        	while (map != null) {
        	    TYPE_NAME_MAP.put((String) map.get("device_model_id"), (String) map.get("device_model"));
        	    map = cursor.getNext();
        	}
        	cursor = null;
	}
	/**
	 * 根据型号serial获取对应的型号名称，如果获取不到，则之间返回serial本身
	 * @param serial
	 * @return 
	 */
	public static Object getDeviceModelBySerial(String serial){
	    String device_model = TYPE_NAME_MAP.get(serial);
	    return device_model == null ? "" : device_model;
	}
	static{
    	/** *************** 规则类型 用户创建规则：初始下拉框，供用户选择类型用于创建规则***************** */
    	RULE_TYPE_MAP.put("设备名称", DEVICE_NAME);
    	RULE_TYPE_MAP.put("设备型号", DEVICE_TYPE);
    	RULE_TYPE_MAP.put("设备IP", IP);
    	RULE_TYPE_MAP.put("告警创建者", CREATOR_NAME);
    	RULE_TYPE_MAP.put("事件类型", EVENT_OID);
    	RULE_TYPE_MAP.put("告警等级", LEVEL);
    	RULE_TYPE_MAP.put("属地", CITY);
    	
    
    	/***************** 规则类型 用户创建规则：当用户选择按告警等级过滤时，将此map中key显示在下拉框中******************/
    	LEVEL_MAP.put("严重告警", 5);
    	LEVEL_MAP.put("主要告警", 4);
    	LEVEL_MAP.put("次要告警", 3);
    	LEVEL_MAP.put("警告告警", 2);
    	LEVEL_MAP.put("事件告警", 1);
    	LEVEL_MAP.put("清除告警", 0);
    
    	/*************** 实时告警牌显示告警等级需要从此map中读取 ***************/
    	NUM_LEVEL_MAP.put(5, "严重告警");
    	NUM_LEVEL_MAP.put(4, "主要告警");
    	NUM_LEVEL_MAP.put(3, "次要告警");
    	NUM_LEVEL_MAP.put(2, "警告告警");
    	NUM_LEVEL_MAP.put(1, "事件告警");
    	NUM_LEVEL_MAP.put(0, "清除告警");
    	
    	/*************** 实时告警牌显示告警状态时需要从此map中读取 ***************/
    	STATUS_NAME_MAP.put(1, "未确认");
    	STATUS_NAME_MAP.put(2, "自动确认");
    	STATUS_NAME_MAP.put(3, "手工确认");
    	
    	/*************** 实时告警牌显示清除状态时需要从此map中读取 ***************/
    	STATUS_CLEAR_MAP.put(1, "未清除");
    	STATUS_CLEAR_MAP.put(2, "自动清除");
    	STATUS_CLEAR_MAP.put(3, "手工清除");
    	
    	/***************** 告警事件oid 用户创建规则：当用户选择按告警事件类型规则时，将此map中key显示在下拉框中 ******************/
    	EVENTOID_MAP.put("设备不可达", "1.3.6.1.4.1.10293.100.2.1");
    	EVENTOID_MAP.put("设备可达", "1.3.6.1.4.1.10293.100.2.2");
    	EVENTOID_MAP.put("端口Down", "1.3.6.1.6.3.1.1.5.3");
    	EVENTOID_MAP.put("端口Up", "1.3.6.1.6.3.1.1.5.4");
    	EVENTOID_MAP.put("Snmp采集请求超时", "1.3.6.1.4.1.10293.100.4.1");
    	EVENTOID_MAP.put("Snmp采集请求出现未知错误", "1.3.6.1.4.1.10293.100.4.2");
    	EVENTOID_MAP.put("设备不支持此MIB对象", "1.3.6.1.4.1.10293.100.4.3");
    	EVENTOID_MAP.put("设备告警", "1.3.6.1.4.1.10293.100.5.2");
    	EVENTOID_MAP.put("进程停止运行", "1.3.6.1.4.1.10293.100.8.1");
    	EVENTOID_MAP.put("进程恢复运行", "1.3.6.1.4.1.10293.100.8.2");
    	EVENTOID_MAP.put("用户线路不可达", "1.3.6.1.4.1.10293.100.9.1");
    	EVENTOID_MAP.put("用户线路可达", "1.3.6.1.4.1.10293.100.9.2");
    	EVENTOID_MAP.put("瞬断告警", "1.3.6.1.4.1.10293.100.10.1");
    	EVENTOID_MAP.put("TelnetPing不通", "1.3.6.1.4.1.10293.100.12.1");
    	EVENTOID_MAP.put("TelnetPing通", "1.3.6.1.4.1.10293.100.12.2");
    	EVENTOID_MAP.put("TelnetPing域值超过定义告警", "1.3.6.1.4.1.10293.100.12.3");
    	EVENTOID_MAP.put("TelnetPing域值告警恢复", "1.3.6.1.4.1.10293.100.12.4");
    	//安全网关
    	EVENTOID_MAP.put("病毒事件(安全网关)", "1.3.6.1.4.1.10293.100.18.1");
    	EVENTOID_MAP.put("垃圾邮件(安全网关)", "1.3.6.1.4.1.10293.100.18.2");
    	EVENTOID_MAP.put("过滤事件(安全网关)", "1.3.6.1.4.1.10293.100.18.3");
    	EVENTOID_MAP.put("攻击事件(安全网关)", "1.3.6.1.4.1.10293.100.18.4");
    
    	/***************** 告警创建者 用户创建规则：选择按告警创建者时，将此map种key显示在下拉框中******************/
    	CREATE_EVENT_MAP.put("流量采集", "visualman");
    	CREATE_EVENT_MAP.put("性能采集", "pmee");
    	CREATE_EVENT_MAP.put("主机代理", "hostman");
    	CREATE_EVENT_MAP.put("Ping检测模块", "ipcheck");
    	CREATE_EVENT_MAP.put("Trap告警", "Trap Probe 1");
    	CREATE_EVENT_MAP.put("TelnetPing模块", "telnetping");
    	CREATE_EVENT_MAP.put("业务平台", "buss");
    	CREATE_EVENT_MAP.put("业务模拟", "ServiceAgent");
    	CREATE_EVENT_MAP.put("SDA采集机", "SDAGather");
    	CREATE_EVENT_MAP.put("SysLog日志", "syslog");
    
    	/*******************定义比较操作对象****************/
    	final BaseOperation big = new BigOperation();
    	final BaseOperation small = new SmallOperation();
    	final BaseOperation equal = new EqualOperation();
    	final BaseOperation bigOrEqu = new NotOperation(small) {
    	    public String toString() {
    		return ">=";
    	    }
    	};
    	final BaseOperation smallOrEqu = new NotOperation(big) {
    	    public String toString() {
    		return "<=";
    	    }
    	};
    	final BaseOperation notEqual = new NotOperation(equal) {
    	    public String toString() {
    		return "!=";
    	    }
    	};
    	/************************ 比较操作符与比较操作对象映射关系，过滤模块解析过滤规则内容时需要获取操作符对应的对象 ************************/
    	OPERATOR_MAP.put(">", big);
    	OPERATOR_MAP.put("<", small);
    	OPERATOR_MAP.put("=", equal);
    	OPERATOR_MAP.put(">=", bigOrEqu);
    	OPERATOR_MAP.put("<=", smallOrEqu);
    	OPERATOR_MAP.put("!=", notEqual);
    	
    	initTypeNameMap();
	}
}
