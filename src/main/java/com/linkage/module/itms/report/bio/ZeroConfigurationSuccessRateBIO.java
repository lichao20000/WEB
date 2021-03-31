package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.dao.ZeroConfigurationSuccessRateDAO;

public class ZeroConfigurationSuccessRateBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigurationSuccessRateBIO.class);

	private ZeroConfigurationSuccessRateDAO dao;

	
	/**
	 * 绑定率统计   页面列表显示
	 * 
	 * 
	 */
	public ArrayList<HashMap<String,String>> getZeroConfigurationSuccessRateByCityid(String starttime1,
			String cityId,String isExcel) {

		logger.debug("getZeroConfigurationSuccessRateByCityid({},{},{},{})",
				new Object[] { starttime1, cityId });
		if ("1".equals(isExcel))
		{
			// 调用存储过程
//			long result = dao.executeProcedure(starttime1, cityId);
		}
		
		ArrayList<HashMap<String,String>> list = dao.getResultByCityId(starttime1,cityId);
		// 按本地网统计
//		Map cityMap = CityDAO.getCityIdCityNameMap();
//
//		// 根据属地cityId获取下一层属地ID(不包含自己)
//		ArrayList<String> cityList = CityDAO
//				.getNextCityIdsByCityPidCore(cityId);
//		Collections.sort(cityList);
//		Map demap = dao.getOperOkNum(cityId); // 统计已绑定数
//		Map nomap = dao.getOperUnOkNum(cityId); // 统计未绑定数
//
//		long detotal = 0;
//		
//		long nototal = 0;
//		
//		long adetotal = 0;
//		
//		long anototal = 0;
//
//		ArrayList<String> tlist = null;
//
//		// 选择的属地
//		Map<String, Object> amap = new HashMap();
//		amap.put("city_id", cityId); // 属地ID
//		amap.put("city_name", cityMap.get(cityId)); // 属地名称
//		detotal = 0;
//		nototal = 0;
//		adetotal = 0;
//		anototal = 0;
//		tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
//		for (int j = 0; j < tlist.size(); j++) {
//			detotal = detotal
//					+ StringUtil.getLongValue(demap.get(tlist.get(j)));
//			nototal = nototal
//					+ StringUtil.getLongValue(nomap.get(tlist.get(j)));
//		}
//		adetotal+= detotal;
//		anototal+= nototal;
//		amap.put("detotal", detotal); // 新装工单
//		amap.put("nototal", nototal); // 失败工单
//		amap.put("percent", percent(detotal, nototal)); // 零配置开通成功率
//		list.add(amap);
//		for (int i = 0; i < cityList.size(); i++) {
//			Map<String, Object> tmap = new HashMap<String, Object>();
//			// 已部署数
//			detotal = 0;
//			// 未部署数
//			nototal = 0;
//			String city_id = cityList.get(i);
//			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
//			tmap.put("city_id", city_id);
//			tmap.put("city_name", cityMap.get(city_id));
//			for (int j = 0; j < tlist.size(); j++) {
//				detotal = detotal
//						+ StringUtil.getLongValue(demap.get(tlist.get(j)));
//				nototal = nototal
//						+ StringUtil.getLongValue(nomap.get(tlist.get(j)));
//			}
//			adetotal+= detotal;
//			anototal+= nototal;
//			String percent = percent(detotal, nototal);
//			tmap.put("detotal", detotal); // 新装工单
//			tmap.put("nototal", nototal); // 失败工单
//			tmap.put("percent", percent); // 零配置开通成功率
//			list.add(tmap);
//			tlist = null;
//		}
//		String apercent = percent(adetotal, anototal);
//		amap.put("all", "总计"); // 已绑定
//		amap.put("adetotal", adetotal); // 已绑定
//		amap.put("anototal", anototal); // 未绑定
//		amap.put("apercent", apercent); // 绑定率
//		cityMap = null;
//		cityList = null;
//		tlist = null;
		return list;
	}

	public String percent(long p1, long p2) {
		double p3;
		if (p1 == 0 && p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / (p1 + p2);
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public ZeroConfigurationSuccessRateDAO getDao() {
		return dao;
	}

	public void setDao(ZeroConfigurationSuccessRateDAO dao) {
		this.dao = dao;
	}

}
