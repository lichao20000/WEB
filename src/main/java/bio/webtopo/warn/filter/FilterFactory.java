package bio.webtopo.warn.filter;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn 版权：南京联创科技 网管科技部
 * 
 */
public class FilterFactory
{
	/**
	 * 工厂方法
	 * 
	 * @param rule
	 * @return
	 */
	public static BaseFilter buildFilter(RuleDetail rule)
	{
		loadType(rule);
		BaseFilter filter = null;
		switch (rule.getType())
			{
			case ConstantEventEnv.LEVEL:
				rule.setIntValue(ConstantEventEnv.LEVEL_MAP.get(rule.getValue()));
				filter = new LevelFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP
						.get(rule.getOperation()));
				break;
			case ConstantEventEnv.CREATOR_NAME:
				rule.setValue(ConstantEventEnv.CREATE_EVENT_MAP.get(rule.getValue()));
				filter = new CreatorNameFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
			case ConstantEventEnv.EVENT_OID:
				rule.setValue(ConstantEventEnv.EVENTOID_MAP.get(rule.getValue()));
				filter = new EventOidFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
			case ConstantEventEnv.DEVICE_TYPE:
				filter = new DeviceTypeFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
			case ConstantEventEnv.IP:
				filter = new DeviceIPFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
			case ConstantEventEnv.DEVICE_NAME:
				filter = new DeviceNameFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
			case ConstantEventEnv.CITY:
				filter = new CityFilter(rule.getValue(), ConstantEventEnv.OPERATOR_MAP.get(rule.getOperation()));
				break;
				
			
			default:
				return null;
			}
		return filter;
	}
	/**
	 * 根据rule中key获取用户配置规则时的类型（如：设备IP，告警创建者）
	 * @param rule
	 * @param map
	 */
	private static void loadType(RuleDetail rule){
		String key = rule.getKey();
		rule.setType(ConstantEventEnv.RULE_TYPE_MAP.get(key));
	}
}
