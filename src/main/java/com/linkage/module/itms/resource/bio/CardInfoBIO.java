/**
 * 
 */
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.itms.resource.dao.CardInfoDAO;
import com.linkage.module.itms.resource.obj.CardStatusOBJ;

/**
 * @author chenjie(67371)
 * @date 2011-5-7
 */
public class CardInfoBIO {
	
	// 日志记录	
	private static Logger logger = LoggerFactory.getLogger(CardInfoBIO.class);
	
	private CardInfoDAO dao;
	
	private int maxPage_splitPage;
	
	/** 卡状态采集节点 **/
	public static final int GATHER_CARDSMANAGE = 46;

	public List<Map> queryCard(String username, String card_serialnumber, String online_status, 
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryCard({},{},{})", new Object[]{username, card_serialnumber, online_status});
		maxPage_splitPage =  getCardCount(username, card_serialnumber, online_status, curPage_splitPage, num_splitPage);
		return dao.queryCard(username, card_serialnumber, online_status, curPage_splitPage, num_splitPage);
	}

	public int getCardCount(String username, String card_serialnumber, String online_status, 
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getCardCount({},{},{})", new Object[]{username, card_serialnumber, online_status});
		return dao.getCardCount(username, card_serialnumber, online_status, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 根据用户ID查询绑定的设备ID
	 * @return
	 */
	/**
	public String getDeviceIdByUserId(String user_id)
	{
		logger.debug("getDeviceIdByUserId({})", new Object[]{user_id});
		return dao.getDeviceIdByUserId(user_id);
	}
	**/

	public CardInfoDAO getDao() {
		return dao;
	}

	public void setDao(CardInfoDAO dao) {
		this.dao = dao;
	}

	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public CardStatusOBJ getCardStauts(String device_id) {
		// 采集
		int result = new SuperGatherCorba(LipossGlobals.getGw_Type(device_id)).getCpeParams(device_id, GATHER_CARDSMANAGE);
		CardStatusOBJ cardObj = new CardStatusOBJ();
		cardObj.setDeviceId(device_id);
		cardObj.setResultCode(result);
		cardObj.setResultStr(Global.G_Fault_Map.get(result).getFaultDesc());
		// 采集成功
		if(result == 1)
		{
			Map objMap = dao.queryCardStatus(device_id);
			if(objMap != null)
			{
				cardObj.setCardNo((String)objMap.get("card_no"));
				cardObj.setStatus(StringUtil.getStringValue(objMap.get("status")));
				cardObj.setCardStatus(StringUtil.getStringValue(objMap.get("card_status")));
			}
			// 采集返回成功，但是表中没有数据
			else
			{
				cardObj.setResultCode(-9);
				cardObj.setResultStr(Global.G_Fault_Map.get(-9).getFaultDesc());
			}
			return cardObj;
		}
		else
		{
			// 设备不在线
			if(result == -1)
			{
				cardObj.setCardStatus("0");
			}
			return cardObj;
		}
	}
}
