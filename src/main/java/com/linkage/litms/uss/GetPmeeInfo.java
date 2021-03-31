package com.linkage.litms.uss;

import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.StringUtil;

public class GetPmeeInfo extends AllTrTds {

	public GetPmeeInfo() {
		UssLog.log("-------------------------GetPmeeInfo-------------------------------------");
	}
	
	public String getPmeeInfo(String customerID, String username, long startTime, long endTime) {

		String html = "";
		String device_id = com.linkage.litms.uss.DBUtil.getDeviceID(username);
		
		//获得头信息
		//html += CommonMtd.getTitleTable("客户相关信息");
		
		DateTimeUtil dtu = new DateTimeUtil(startTime * 1000);
		int month = dtu.getMonth();
		int year = dtu.getYear();

		String curDate = dtu.getDate();
		boolean cpuData = true;
		boolean memData = true;
		
		if (null == device_id) {
			html += tr(td("该业务用户没有对应的设备", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		long time0_1 = startTime + 2 * 60 * 60;
		long time1_2 = time0_1 + 2 * 60 * 60;
		long time2_3 = time1_2 + 2 * 60 * 60;
		long time3_4 = time2_3 + 2 * 60 * 60;
		long time4_5 = time3_4 + 2 * 60 * 60;
		long time5_6 = time4_5 + 2 * 60 * 60;
		long time6_7 = time5_6 + 2 * 60 * 60;
		long time7_8 = time6_7 + 2 * 60 * 60;
		long time8_9 = time7_8 + 2 * 60 * 60;
		long time9_10 = time8_9 + 2 * 60 * 60;
		long time10_11 = time9_10 + 2 * 60 * 60;
		long time11_12 = time10_11 + 2 * 60 * 60;

		double cpu_value0_1 = 0;
		double cpu_value1_2 = 0;
		double cpu_value2_3 = 0;
		double cpu_value3_4 = 0;
		double cpu_value4_5 = 0;
		double cpu_value5_6 = 0;
		double cpu_value6_7 = 0;
		double cpu_value7_8 = 0;
		double cpu_value8_9 = 0;
		double cpu_value9_10 = 0;
		double cpu_value10_11 = 0;
		double cpu_value11_12 = 0;

		int cpu_times_1 = 0;
		int cpu_times_2 = 0;
		int cpu_times_3 = 0;
		int cpu_times_4 = 0;
		int cpu_times_5 = 0;
		int cpu_times_6 = 0;
		int cpu_times_7 = 0;
		int cpu_times_8 = 0;
		int cpu_times_9 = 0;
		int cpu_times_10 = 0;
		int cpu_times_11 = 0;
		int cpu_times_12 = 0;
		
		double mem_value0_1 = 0;
		double mem_value1_2 = 0;
		double mem_value2_3 = 0;
		double mem_value3_4 = 0;
		double mem_value4_5 = 0;
		double mem_value5_6 = 0;
		double mem_value6_7 = 0;
		double mem_value7_8 = 0;
		double mem_value8_9 = 0;
		double mem_value9_10 = 0;
		double mem_value10_11 = 0;
		double mem_value11_12 = 0;

		int mem_times_1 = 0;
		int mem_times_2 = 0;
		int mem_times_3 = 0;
		int mem_times_4 = 0;
		int mem_times_5 = 0;
		int mem_times_6 = 0;
		int mem_times_7 = 0;
		int mem_times_8 = 0;
		int mem_times_9 = 0;
		int mem_times_10 = 0;
		int mem_times_11 = 0;
		int mem_times_12 = 0;
		
		long tempTime = 0;
		double tempValue = 0;

		//检查表名是否存在
		String chkTableSQL = "select name from sysobjects where name = 'pm_raw_"+ year + "_" + month+"'";
		if (DBUtil.GetDB() == Global.DB_ORACLE)
		{// oracle
			chkTableSQL = "select table_name as name from user_tables where table_name='"
					+ StringUtil.getUpperCase("pm_raw_"+ year + "_" + month) + "'";
		}
		else if (DBUtil.GetDB() == Global.DB_SYBASE)
		{// sybase
			chkTableSQL = "select name from sysobjects where name = 'pm_raw_"+ year + "_" + month+"'";
		}
		PrepareSQL psql = new PrepareSQL(chkTableSQL);
		psql.getSQL();
		Map<String, String> chkTableMap = DataSetBean.getRecord(chkTableSQL);

		UssLog.log("[GetPmeeInfo-getPmeeInfo]------chkTableSQL------:" + chkTableSQL);
		
		if (null == chkTableMap) {
			html += tr(td("该用户暂时没有性能信息", "", "10", ""));
			return retHTML(html, username, curDate, customerID);
		}
		
		String cpuSQL = "select a.device_id,c.* from pm_map_instance a,pm_expression b, pm_raw_"
				+ year + "_" + month + " c " + " where a.device_id='" + device_id
				+ "' and a.expressionid=b.expressionid and "
				+ " b.class1=1 and c.id=a.id and c.gathertime>=" + startTime
				+ " and c.gathertime<=" + endTime + " order by c.gathertime";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			cpuSQL = "select a.device_id, c.gathertime, c.value from pm_map_instance a,pm_expression b, pm_raw_"
					+ year + "_" + month + " c " + " where a.device_id='" + device_id
					+ "' and a.expressionid=b.expressionid and "
					+ " b.class1=1 and c.id=a.id and c.gathertime>=" + startTime
					+ " and c.gathertime<=" + endTime + " order by c.gathertime";
		}
		psql = new PrepareSQL(cpuSQL);
		psql.getSQL();
		UssLog.log("[GetPmeeInfo-getPmeeInfo]------cpuSQL------:" + cpuSQL);
		
		Cursor cursor = DataSetBean.getCursor(cpuSQL);
		Map<String, String> cpuMap = cursor.getNext();
		if (null == cpuMap) {
			cpuData = false;
		} else {

			while (cpuMap != null) {
				tempTime = Long.parseLong(cpuMap.get("gathertime"));
				tempValue = Double.parseDouble(cpuMap.get("value"));
				
				if (tempTime >= startTime && tempTime < time0_1) {
					
					cpu_value0_1 += tempValue;
					if (tempValue != 0) {
						cpu_times_1++;
					}
				}
				if (tempTime >= time0_1 && tempTime < time1_2) {
					
					cpu_value1_2 += tempValue;
					if (tempValue != 0) {
						cpu_times_2++;
					}
				}
				if (tempTime >= time1_2 && tempTime < time2_3) {
					
					cpu_value2_3 += tempValue;
					if (tempValue != 0) {
						cpu_times_3++;
					}
				}
				if (tempTime >= time2_3 && tempTime < time3_4) {
					
					cpu_value3_4 += tempValue;
					if (tempValue != 0) {
						cpu_times_4++;
					}
				}
				if (tempTime >= time3_4 && tempTime < time4_5) {
					
					cpu_value4_5 += tempValue;
					if (tempValue != 0) {
						cpu_times_5++;
					}
				}
				if (tempTime >= time4_5 && tempTime < time5_6) {
					
					cpu_value5_6 += tempValue;
					if (tempValue != 0) {
						cpu_times_6++;
					}
				}
				if (tempTime >= time5_6 && tempTime < time6_7) {
					
					cpu_value6_7 += tempValue;
					if (tempValue != 0) {
						cpu_times_7++;
					}
				}
				if (tempTime >= time6_7 && tempTime < time7_8) {
					
					cpu_value7_8 += tempValue;
					if (tempValue != 0) {
						cpu_times_8++;
					}
				}
				if (tempTime >= time7_8 && tempTime < time8_9) {
					
					cpu_value8_9 += tempValue;
					if (tempValue != 0) {
						cpu_times_9++;
					}
				}
				if (tempTime >= time8_9 && tempTime < time9_10) {
					
					cpu_value9_10 += tempValue;
					if (tempValue != 0) {
						cpu_times_10++;
					}
				}
				if (tempTime >= time9_10 && tempTime < time10_11) {
					cpu_value10_11 += tempValue;
					if (tempValue != 0) {
						cpu_times_11++;
					}
				}
				if (tempTime >= time10_11 && tempTime < time11_12) {
					cpu_value11_12 += tempValue;
					if (tempValue != 0) {
						cpu_times_12++;
					}
				}

				cpuMap = cursor.getNext();
			}
		}

		/******************************************************************************/
		String memSQL = "select a.device_id,c.* from pm_map_instance a,pm_expression b, pm_raw_"
				+ year + "_" + month + " c " + " where a.device_id='" + device_id
				+ "' and a.expressionid=b.expressionid and "
				+ " b.class1=2 and c.id=a.id and c.gathertime>=" + startTime
				+ " and c.gathertime<=" + endTime + " order by c.gathertime";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			memSQL = "select a.device_id, c.gathertime, c.value from pm_map_instance a,pm_expression b, pm_raw_"
					+ year + "_" + month + " c " + " where a.device_id='" + device_id
					+ "' and a.expressionid=b.expressionid and "
					+ " b.class1=2 and c.id=a.id and c.gathertime>=" + startTime
					+ " and c.gathertime<=" + endTime + " order by c.gathertime";
		}
		psql = new PrepareSQL(memSQL);
		psql.getSQL();
		UssLog.log("[GetPmeeInfo-getPmeeInfo]------memSQL------:" + memSQL);
		
		Cursor cursor_mem = DataSetBean.getCursor(memSQL);
		Map<String, String> memMap = cursor_mem.getNext();
		if (null == memMap) {
			memData = false;
		} else {
			while (memMap != null) {
				tempTime = Long.parseLong(memMap.get("gathertime"));
				tempValue = Double.parseDouble(memMap.get("value"));

				if (tempTime >= startTime && tempTime < time0_1) {
					mem_value0_1 += tempValue;
					if (tempValue != 0) {
						mem_times_1++;
					}
				}
				if (tempTime >= time0_1 && tempTime < time1_2) {
					mem_value1_2 += tempValue;
					if (tempValue != 0) {
						mem_times_2++;
					}
				}
				if (tempTime >= time1_2 && tempTime < time2_3) {
					mem_value2_3 += tempValue;
					if (tempValue != 0) {
						mem_times_3++;
					}
				}
				if (tempTime >= time2_3 && tempTime < time3_4) {
					
					mem_value3_4 += tempValue;
					if (tempValue != 0) {
						mem_times_4++;
					}
				}
				if (tempTime >= time3_4 && tempTime < time4_5) {
					
					mem_value4_5 += tempValue;
					if (tempValue != 0) {
						mem_times_5++;
					}
				}
				if (tempTime >= time4_5 && tempTime < time5_6) {
					
					mem_value5_6 += tempValue;
					if (tempValue != 0) {
						mem_times_6++;
					}
				}
				if (tempTime >= time5_6 && tempTime < time6_7) {
					
					mem_value6_7 += tempValue;
					if (tempValue != 0) {
						mem_times_7++;
					}
				}
				if (tempTime >= time6_7 && tempTime < time7_8) {
					
					mem_value7_8 += tempValue;
					if (tempValue != 0) {
						mem_times_8++;
					}
				}
				if (tempTime >= time7_8 && tempTime < time8_9) {
					
					mem_value8_9 += tempValue;
					if (tempValue != 0) {
						mem_times_9++;
					}
				}
				if (tempTime >= time8_9 && tempTime < time9_10) {
					
					mem_value9_10 += tempValue;
					if (tempValue != 0) {
						mem_times_10++;
					}
				}
				if (tempTime >= time9_10 && tempTime < time10_11) {
					
					mem_value10_11 += tempValue;
					if (tempValue != 0) {
						mem_times_11++;
					}
				}
				if (tempTime >= time10_11 && tempTime < time11_12) {
					
					mem_value11_12 += tempValue;
					if (tempValue != 0) {
						mem_times_12++;
					}
				}

				memMap = cursor_mem.getNext();
			}
		}
		
//		logger.debug("mem_times_6---------------------:" + mem_times_6);
//		logger.debug("mem_times_7---------------------:" + mem_times_7);
//		logger.debug("mem_times_8---------------------:" + mem_times_8);
//		
//		logger.debug("mem_value5_6---------------------:" + mem_value5_6);
//		logger.debug("mem_value6_7---------------------:" + mem_value6_7);
//		logger.debug("mem_value7_8---------------------:" + mem_value7_8);
		
		html += th(td("时间"), td("CPU平均使用率(%)"), td("内存平均使用率(%)"));
		if (!cpuData && !memData) {
			html += tr(td("该用户没有性能数据", "", "10", ""));
		} else {
			if (cpu_times_1 == 0 && mem_times_1 != 0) {
				cpu_times_1 = mem_times_1;
			} else if (cpu_times_1 != 0 && mem_times_1 == 0) {
				mem_times_1 = cpu_times_1;
			}
			if (cpu_times_2 == 0 && mem_times_2 != 0) {
				cpu_times_2 = mem_times_2;
			} else if (cpu_times_2 != 0 && mem_times_2 == 0) {
				mem_times_2 = cpu_times_2;
			}
			if (cpu_times_3 == 0 && mem_times_3 != 0) {
				cpu_times_3 = mem_times_3;
			} else if (cpu_times_3 != 0 && mem_times_3 == 0) {
				mem_times_3 = cpu_times_3;
			}
			if (cpu_times_4 == 0 && mem_times_4 != 0) {
				cpu_times_4 = mem_times_4;
			} else if (cpu_times_4 != 0 && mem_times_4 == 0) {
				mem_times_4 = cpu_times_4;
			}
			if (cpu_times_5 == 0 && mem_times_5 != 0) {
				cpu_times_5 = mem_times_5;
			} else if (cpu_times_5 != 0 && mem_times_5 == 0) {
				mem_times_5 = cpu_times_5;
			}
			if (cpu_times_6 == 0 && mem_times_6 != 0) {
				cpu_times_6 = mem_times_6;
			} else if (cpu_times_6 != 0 && mem_times_6 == 0) {
				mem_times_6 = cpu_times_6;
			}
			if (cpu_times_7 == 0 && mem_times_7 != 0) {
				cpu_times_7 = mem_times_7;
			} else if (cpu_times_7 != 0 && mem_times_7 == 0) {
				mem_times_7 = cpu_times_7;
			}
			if (cpu_times_8 == 0 && mem_times_8 != 0) {
				cpu_times_8 = mem_times_8;
			} else if (cpu_times_8 != 0 && mem_times_8 == 0) {
				mem_times_8 = cpu_times_8;
			}
			if (cpu_times_9 == 0 && mem_times_9 != 0) {
				cpu_times_9 = mem_times_9;
			} else if (cpu_times_9 != 0 && mem_times_9 == 0) {
				mem_times_9 = cpu_times_9;
			}
			if (cpu_times_10 == 0 && mem_times_10 != 0) {
				cpu_times_10 = mem_times_10;
			} else if (cpu_times_10 != 0 && mem_times_10 == 0) {
				mem_times_10 = cpu_times_10;
			}
			if (cpu_times_11 == 0 && mem_times_11 != 0) {
				cpu_times_11 = mem_times_11;
			} else if (cpu_times_11 != 0 && mem_times_11 == 0) {
				mem_times_11 = cpu_times_11;
			}
			if (cpu_times_12 == 0 && mem_times_12 != 0) {
				cpu_times_12 = mem_times_12;
			} else if (cpu_times_12 != 0 && mem_times_12 == 0) {
				mem_times_12 = cpu_times_12;
			}
			
			if (cpu_times_1 == 0) {
				html += tr(td("0点-2点"), td(cpu_value0_1 + ""), td(mem_value0_1 + ""));
			} else {
				html += tr(td("0点-2点"), td(String.format("%.2f", cpu_value0_1 / cpu_times_1)),
						td(String.format("%.2f", mem_value0_1 / mem_times_1)));
			}

			if (cpu_times_2 == 0) {
				html += tr(td("2点-4点"), td(cpu_value1_2 + ""), td(mem_value1_2 + ""));
			} else {
				html += tr(td("2点-4点"), td(String.format("%.2f", cpu_value1_2 / cpu_times_2)),
						td(String.format("%.2f", mem_value1_2 / mem_times_2)));
			}

			if (cpu_times_3 == 0) {
				html += tr(td("4点-6点"), td(cpu_value2_3 + ""), td(mem_value2_3 + ""));
			} else {
				html += tr(td("4点-6点"), td(String.format("%.2f", cpu_value2_3 / cpu_times_3)),
						td(String.format("%.2f", mem_value2_3 / mem_times_3)));
			}

			if (cpu_times_4 == 0) {
				html += tr(td("6点-8点"), td(cpu_value3_4 + ""), td(mem_value3_4 + ""));
			} else {
				html += tr(td("6点-8点"), td(String.format("%.2f", cpu_value3_4 / cpu_times_4)),
						td(String.format("%.2f", mem_value3_4 / mem_times_4)));
			}

			if (cpu_times_5 == 0) {
				html += tr(td("8点-10点"), td(cpu_value4_5 + ""), td(mem_value4_5 + ""));
			} else {
				html += tr(td("8点-10点"), td(String.format("%.2f", cpu_value4_5 / cpu_times_5)),
						td(String.format("%.2f", mem_value4_5 / mem_times_5)));
			}

			if (cpu_times_6 == 0) {
				html += tr(td("10点-12点"), td(cpu_value5_6 + ""), td(mem_value5_6 + ""));
			} else {
				html += tr(td("10点-12点"), td(String.format("%.2f", cpu_value5_6 / cpu_times_6)),
						td(String.format("%.2f", mem_value5_6 / mem_times_6)));
			}

			if (cpu_times_7 == 0) {
				html += tr(td("12点-14点"), td(cpu_value6_7 + ""), td(mem_value6_7 + ""));
			} else {
				html += tr(td("12点-14点"), td(String.format("%.2f", cpu_value6_7 / cpu_times_7)),
						td(String.format("%.2f", (mem_value6_7 / mem_times_7))));
			}
			
			if (cpu_times_8 == 0) {
				html += tr(td("14点-16点"), td(cpu_value7_8 + ""), td(mem_value7_8 + ""));
			} else {
				html += tr(td("14点-16点"), td(String.format("%.2f", cpu_value7_8 / cpu_times_8)),
						td(String.format("%.2f", mem_value7_8 / mem_times_8)));
			}

			if (cpu_times_9 == 0) {
				html += tr(td("16点-18点"), td(cpu_value8_9 + ""), td(mem_value8_9 + ""));
			} else {
				html += tr(td("16点-18点"), td(String.format("%.2f", cpu_value8_9 / cpu_times_9)),
						td(String.format("%.2f", mem_value8_9 / mem_times_9)));
			}

			if (cpu_times_10 == 0) {
				html += tr(td("18点-20点"), td(cpu_value9_10 + ""), td(mem_value9_10 + ""));
			} else {
				html += tr(td("18点-20点"), td(String.format("%.2f", cpu_value9_10 / cpu_times_10)),
						td(String.format("%.2f", mem_value9_10 / mem_times_10)));
			}

			if (cpu_times_11 == 0) {
				html += tr(td("20点-22点"), td(cpu_value10_11 + ""), td(mem_value10_11 + ""));
			} else {
				html += tr(td("20点-22点"), td(String.format("%.2f", cpu_value10_11 / cpu_times_11)),
						td(String.format("%.2f", mem_value10_11 / mem_times_11)));
			}

			if (cpu_times_12 == 0) {
				html += tr(td("22点-24点"), td(cpu_value11_12 + ""), td(mem_value11_12 + ""));
			} else {
				html += tr(td("22点-24点"), td(String.format("%.2f", cpu_value11_12 / cpu_times_12)),
						td(String.format("%.2f", mem_value11_12 / mem_times_12)));
			}
		}
		
		return retHTML(html, username, curDate, customerID);
	}

	
	/**
	 * 最终返回的HTML
	 * 
	 * @param html
	 * @param username
	 * @param curDate
	 * @return
	 */
	private String retHTML(String html, String username, String curDate, String customerID) {
		html += tr(td("<input class='bottom1' type='button' value=' 返 回 ' onclick='doReturn("+customerID+")'>","right", "10", "","tdd_A"));
		
		String returnhtml = BuildHTML.getComplete("用户<font color='red'>“"+username+"”</font>的性能数据（<font color='red'>"+curDate+"</font>）", 10, html);
		return returnhtml;
	}
}
