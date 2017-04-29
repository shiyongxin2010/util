package org.suze.util;
/**
 * 工具类
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2015-11-2 上午9:20:42  by 石冬冬（ddshi@1010111.com）创建
 */
public class TimeOuter {
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 输出时间
	 * Description: 
	 * @Version1.0 2015-11-2 上午9:20:31 by 石冬冬（ddshi@1010111.com）创建
	 * @param spend
	 * @return
	 */
	public static String out(long spend){
		String out = "";
		int hour=(int)spend/(60*60*1000);//时
		int min=(int)spend/(60*1000)%60;//分
		int sec=(int)spend/1000%60; //秒
		int ms=(int)spend%1000;//毫秒
		if(hour>0){out+=hour+"时";}
		if(min>0){out+=min+"分";}
		if(sec>0){out+=sec+"秒";}
		if(ms>0){out+=ms+"毫秒";}
		return out;
	}
	/**
	 * 输出时间
	 * Description: 
	 * @Version1.0 2015-11-2 上午9:23:03 by 石冬冬（ddshi@1010111.com）创建
	 * @param msg
	 * @return
	 */
	public static String format(long spend,String msg){
		if(0==spend){
			return "★★★"+msg+"★★★";
		}else{
			return "★★★"+msg+"，共花费"+out(spend)+"★★★";
		}
	}
	/**
	 * Description:格式化字符串，作为日志识别输出 
	 * @Version1.0 2015-11-26 下午1:30:35 by 石冬冬-Chris Suk（dd.shi02@zuche.com）创建
	 * @param msg
	 * @return
	 */
	public static String format(String msg){
		return "★★★[Heil Hitler]"+msg+"★★★";
	}
}
