
package com.linkage.module.gwms.util;

public class IpUtil {

	/**
	 * 获取输入value的二进制序列，高位补零 eg. value:123 ,return:01111011
	 * 
	 * @param value
	 * @return
	 */
	public static String getValueBinarySeq(int value) {
		// 中间拼接
		StringBuffer _s = new StringBuffer();
		// 计算有效位
		while (value > 0) {
			_s.append(value % 2);
			value = value / 2;
		}
		// 高位补零
		while (_s.length() < 8) {
			_s.append("0");
		}
		// 反转输出
		return _s.reverse().toString();
	}

	/**
	 * 根据输入的二进制序列，输出十进制标准值 eg. value:01111011 return:123
	 * 
	 * @param value
	 * @return
	 */
	public static int getValueStandardPrint(String value) {
		// 数组化
		char[] _c = value.toCharArray();
		int _value = 0;
		// 计算
		for (int m = 0; m < _c.length; m++) {
			if ('1' == _c[m]) {
				_value = _value + (int) Math.pow(2, 7 - m);
			}
		}
		// 返回
		return _value;
	}

	/**
	 * 根据输入的掩码位数，输出掩码的二进制表示形式,中间以.隔开 eg. subnetMask=24,
	 * return:11111111.11111111.11111111.00000000
	 * 
	 * @param subnetMask
	 * @return
	 */
	public static String getSubnetMaskBinarySeq(int subnetMask) {
		// 检查掩码位数是否有效
		if (subnetMask < 0 || subnetMask > 32) {
			throw new NumberFormatException("subnetMask too large or small");
		}
		// 组建掩码长度数组
		String[] _subnetMask = new String[4];
		// 掩码高位处理
		for (int i = 0; i < subnetMask / 8; i++) {
			_subnetMask[i] = "11111111";
		}
		// 掩码中间位处理
		if (subnetMask % 8 > 0) {
			StringBuffer _temp = new StringBuffer();
			for (int i = 0; i < subnetMask % 8; i++) {
				_temp.append("1");
			}
			for (int i = 0; i < (8 - subnetMask % 8); i++) {
				_temp.append("0");
			}
			_subnetMask[subnetMask / 8] = _temp.toString();
		} else {
			_subnetMask[subnetMask / 8] = "00000000";
		}
		// 掩码地位补零
		for (int i = (1 + subnetMask / 8); i < 4; i++) {
			_subnetMask[i] = "00000000";
		}
		// 格式化掩码并返回
		return _subnetMask[0] + "." + _subnetMask[1] + "." + _subnetMask[2] + "."
				+ _subnetMask[3];
	}

	/**
	 * 根据一个地址ipAddr，及子网掩码数subnetMask获得子网地址 eg. ipAddr：123.168.0.0 subnetMask：22
	 * retrun：123.168.0.0
	 * 
	 * @param ipAddr
	 * @param subnetMask
	 * @return
	 */
	public static String getFlowerSubAddr(String ipAddr, int subnetMask) {
		String[] ipArray = ipAddr.split("\\.");
		// 校验地址合法性
		if (ipArray.length != 4) {
			throw new ArrayIndexOutOfBoundsException("ipAddr is illegal");
		}
		// 检查掩码位数是否有效
		if (subnetMask < 0 || subnetMask > 32) {
			throw new NumberFormatException("subnetMask too large or small");
		}
		// 将IP地址转化成二进制序列
		StringBuffer _s = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (i != 0) {
				_s.append(".");
			}
			_s.append(getValueBinarySeq(Integer.parseInt(ipArray[i])));
		}
		// 进行子网地址计算
		char[] ipAddrBinarySeq = _s.toString().toCharArray();
		char[] subnetMaskBinarySeq = getSubnetMaskBinarySeq(subnetMask).toCharArray();
		char[] _subAddr = new char[ipAddrBinarySeq.length];
		for (int i = 0; i < ipAddrBinarySeq.length; i++) {
			if ('.' == ipAddrBinarySeq[i]) {
				_subAddr[i] = '.';
			} else if (('1' == ipAddrBinarySeq[i]) && ('1' == subnetMaskBinarySeq[i])) {
				_subAddr[i] = '1';
			} else {
				_subAddr[i] = '0';
			}
		}
		ipArray = String.valueOf(_subAddr).split("\\.");
		// 将子网地址转成十进制的标准输出
		StringBuffer _subAddrSb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (i != 0) {
				_subAddrSb.append(".");
			}
			_subAddrSb.append(getValueStandardPrint(ipArray[i]));
		}
		return _subAddrSb.toString();
	}

	/**
	 * 根据一个子网地址，及子网掩码数subnetMask获得子网广播地址 eg. subAddr：123.168.0.0 subnetMask：22
	 * retrun：123.168.3.255
	 * 
	 * @param ipAddr
	 * @param subnetMask
	 * @return
	 */
	public static String getCeilSubAddr(String subAddr, int subnetMask) {
		String[] ipArray = subAddr.split("\\.");
		// 校验地址合法性
		if (ipArray.length != 4) {
			throw new ArrayIndexOutOfBoundsException("ipAddr is illegal");
		}
		// 检查掩码位数是否有效
		if (subnetMask < 0 || subnetMask > 32) {
			throw new NumberFormatException("subnetMask too large or small");
		}
		// 将IP地址转化成二进制序列
		StringBuffer _s = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (i != 0) {
				_s.append(".");
			}
			_s.append(getValueBinarySeq(Integer.parseInt(ipArray[i])));
		}
		// 进行子网广播地址计算
		char[] ipAddrBinarySeq = _s.toString().toCharArray();
		char[] subnetMaskBinarySeq = getSubnetMaskBinarySeq(subnetMask).toCharArray();
		char[] _subAddr = new char[ipAddrBinarySeq.length];
		for (int i = 0; i < ipAddrBinarySeq.length; i++) {
			if ('.' == ipAddrBinarySeq[i]) {
				_subAddr[i] = '.';
			} else if (subnetMaskBinarySeq[i] == ipAddrBinarySeq[i]) {
				_subAddr[i] = '1';
			} else {
				_subAddr[i] = '0';
			}
		}
		ipArray = String.valueOf(_subAddr).split("\\.");
		// 将子网广播地址转成十进制的标准输出
		StringBuffer _subAddrSb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (i != 0) {
				_subAddrSb.append(".");
			}
			_subAddrSb.append(getValueStandardPrint(ipArray[i]));
		}
		return _subAddrSb.toString();
	}

	/**
	 * 根据输入的IP地址整理成long型 eg. ipAddr:192.168.0.1 return:192168000001
	 * 
	 * @param ipAddr
	 * @return
	 */
	public static long getFullAddr(String ipAddr) {
		String[] ipArray = ipAddr.split("\\.");
		// 校验地址合法性
		if (ipArray.length != 4) {
			throw new ArrayIndexOutOfBoundsException("ipAddr is illegal");
		}
		// 数值转换，位数不足，高位补零
		StringBuffer _s = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (1 == ipArray[i].length()) {
				_s.append("00").append(ipArray[i]);
			} else if (2 == ipArray[i].length()) {
				_s.append("0").append(ipArray[i]);
			} else {
				_s.append(ipArray[i]);
			}
		}
		// 转换成Long型返回
		return Long.parseLong(_s.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(IpUtil.getValueStandardPrint(IpUtil.getValueBinarySeq(168)));
		// System.out.println(IpUtil.getCeilSubAddr(IpUtil.getFlowerSubAddr("123.168.0.0",22),22));
		System.out.println(IpUtil.getFullAddr("192.168.0.1"));
	}
}
