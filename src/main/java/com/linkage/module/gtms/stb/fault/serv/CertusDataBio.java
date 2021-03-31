package com.linkage.module.gtms.stb.fault.serv;

import java.util.Map;

public interface CertusDataBio
{
	/**
	 * <pre>
	 * 请求机顶盒信息
	 *  说明：该方法与requestSTB方法实现相同，该方法虽传入mac地址参数，却未使用。参见requestSTB方法
	 * </pre>
	 * @param serviceUser
	 * @param sn
	 * @param mac
	 * @param cityId
	 * @return
	 * @see #requestSTB
	 */
	Map<String,String> requestSTBInfo(String serviceUser,String sn,String mac,String cityId);
	
	/**
	 * <pre>
	 * 请求机顶盒信息
	 * 说明：该方法与requestSTBInfo方法实现一致。在解决用户视屏质量参数不符合赛特斯格式要求时，
	 *   requestSTBInfo方法未传递mac地址，而且该方法被很多方法调用，加上业务不熟，
	 *   故新增该方法，防止引起其他bug。如果其他地方调用同样需要mac地址，可直接调用该方法。
	 * </pre>
	 * @param serviceUser
	 * @param sn
	 * @param mac
	 * @param cityId
	 * @return 
	 */
	Map<String,String> requestSTB(String serviceUser,String sn,String mac,String cityId);
}
