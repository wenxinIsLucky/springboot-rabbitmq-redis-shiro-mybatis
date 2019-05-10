package com.example.yuanye.common.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/*by lucky 2018年1月31日  
 //						   _ooOoo_							              _ooOoo_    
 //						  o8888888o							             o8888888o    
 //						  88" . "88							             88" . "88    
 //						  (| -_- |)							             (| -_- |)    
 //						  O\  =  /O							             O\  =  /O    
 //					   ____/`---'\____						   	      ____/`---'\____    
 //					 .'  \\|     |//  `.						     .   ' \\| |//   `.    
 //					/  \\|||  :  |||//  \						    /  \\|||  :  |||//  \    
 //				   /  _||||| -:- |||||-  \						   /  _||||| -:- |||||-  \    
 //				   |   | \\\  -  /// |   |						   |   | \\\  -  /// |   |    
 //				   | \_|  ''\---/''  |   |						   | \_|  ''\---/''  |   |    
 //				   \  .-\__  `-`  ___/-. /						    \  .-\__ `-` ___/-.  /    
 //			     ___`. .'  /--.--\  `. . __					  	  ___`. .' /--.--\ `. . __    
 //			  ."" '<  `.___\_<|>_/___.'  >'"".				   ."" '< `.___\_<|>_/___.' >'"".    
 //			 | | :  `- \`.;`\ _ /`;.`/ - ` : | |			 | | : `- \`.;`\ _ /`;.`/ - ` : | |    
 //			 \  \ `-.   \_ __\ /__ _/   .-` /  /			   \ \ `-. \_ __\ /__ _/ .-` / /    
 //		 ======`-.____`-.___\_____/___.-`____.-'====== ======`-.____`-.___\_____/___.-`____.-'======    
 //					       	`=---='    
 //
 //		^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  .............................................      
 //			     	  佛祖保佑             永无BUG   									佛祖保佑       永无BUG
 //			佛曰:    
 //				写字楼里写字间，写字间里程序员；    
 //				程序人员写程序，又拿程序换酒钱。    
 //				酒醒只在网上坐，酒醉还来网下眠；    
 //				酒醉酒醒日复日，网上网下年复年。    
 //				但愿老死电脑间，不愿鞠躬老板前；    
 //				奔驰宝马贵者趣，公交自行程序员。    
 //				别人笑我忒疯癫，我笑自己命太贱；    
 //				不见满街漂亮妹，哪个归得程序员？  
 //										-------by 见死不救    
 */

/**
 * 反射取值赋值
 * @author lucky(李文鑫) Email:578581198@qq.com
 * 2018年1月31日-上午11:42:41
 */
public class ReflexUtil {
	
	/**
	 * 获取request里面的数据并赋值到对象里面
	 * @author lucky(李文鑫) Email:578581198@qq.com
	 * 2018年1月31日-下午4:18:41
	 * @param obj 对象
	 * @param request
	 * @param param 需要省略的字段名称
	 * @return object类型，再强转类型OK
	 * @throws Exception
	 */
	public static Object getRequestSetClassParams(Object obj,HttpServletRequest request,String...param) throws Exception{
		Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
        	boolean isCz = false;
        	String name = field.getName();
        	for (String p : param) {
				if (name.equals(p)) {
					isCz = true;
					break;
				}
			}
        	if(isCz){
        		continue;
        	}
        	if (!"serialVersionUID".equals(name)) {
        		field.setAccessible(true);
        		String value = request.getParameter(name);
        		String type = field.getGenericType().toString();
        		if (type.equals("int") || type.equals("class java.lang.Integer")) {
        			field.setInt(obj, Integer.parseInt(value!=null?value:"0"));
				}else if (type.equals("class java.lang.Double") || type.equals("double")) {
					field.setDouble(obj, Double.parseDouble(value!=null?value:"0"));
				}else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
					field.setBoolean(obj, Boolean.parseBoolean(value!=null?value:"false"));
				}else if (type.equals("class java.lang.Float") || type.equals("float")) {
					field.setByte(obj, Byte.parseByte(value!=null?value:""));
				}else if (type.equals("class java.lang.Char") || type.equals("char")) {
					field.setChar(obj, value.charAt(0));
				}else if (type.equals("class java.lang.Float") || type.equals("float")) {
					field.setFloat(obj, Float.parseFloat(value!=null?value:"0"));
				}else if (type.equals("class java.lang.Long") || type.equals("long")) {
					field.setLong(obj, Long.parseLong(value!=null?value:"0"));
				}else if (type.equals("class java.lang.Short") || type.equals("short")) {
					field.setShort(obj, Short.parseShort(value!=null?value:"0"));
				}else {
					field.set(obj, request.getParameter(name));
				}
			}
		}
		return obj;
	}
	
	/**
	 * 获取json里面的数据并赋值到对象里面
	 * 
	 * @author lucky(李文鑫) Email:578581198@qq.com 2018年1月31日-下午4:18:41
	 * @param obj
	 *            对象
	 * @param jsonp
	 * @param param
	 *            需要省略的字段名称
	 * @return object类型，再强转类型OK
	 * @throws Exception
	 */
	public static Object getRequestSetClassParams(Object obj,JSONObject json, String... param) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean isCz = false;
			String name = field.getName();
			for (String p : param) {
				if (name.equals(p)) {
					isCz = true;
					break;
				}
			}
			if (isCz) {
				continue;
			}
			try {
				if (!"serialVersionUID".equals(name)) {
					field.setAccessible(true);
					String value = json.getString(name);
					String type = field.getGenericType().toString();
					if (type.equals("int")
							|| type.equals("class java.lang.Integer")) {
						field.setInt(obj,
								Integer.parseInt(value != null ? value : "0"));
					} else if (type.equals("class java.lang.Double")
							|| type.equals("double")) {
						field.setDouble(obj,
								Double.parseDouble(value != null ? value : "0"));
					} else if (type.equals("class java.lang.Boolean")
							|| type.equals("boolean")) {
						field.setBoolean(obj, Boolean
								.parseBoolean(value != null ? value : "false"));
					} else if (type.equals("class java.lang.Float")
							|| type.equals("float")) {
						field.setByte(obj,
								Byte.parseByte(value != null ? value : ""));
					} else if (type.equals("class java.lang.Char")
							|| type.equals("char")) {
						field.setChar(obj, value.charAt(0));
					} else if (type.equals("class java.lang.Float")
							|| type.equals("float")) {
						field.setFloat(obj,
								Float.parseFloat(value != null ? value : "0"));
					} else if (type.equals("class java.lang.Long")
							|| type.equals("long")) {
						field.setLong(obj,
								Long.parseLong(value != null ? value : "0"));
					} else if (type.equals("class java.lang.Short")
							|| type.equals("short")) {
						field.setShort(obj,
								Short.parseShort(value != null ? value : "0"));
					} else {
						field.set(obj, json.getString(name));
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		return obj;
	}
	
	/**
	 * 获取对象的所有字段
	 * @author lucky(李文鑫) Email:578581198@qq.com
	 * 2018年1月31日-上午11:59:24
	 * @param object 对象
	 * @return
	 */
	public static String[] getClassByParams(Object object){
		Field[] fields = object.getClass().getDeclaredFields();
		String[] params = new String[fields.length];
		int i = 0;
        for (Field field : fields) {
        	params[i] = field.getName();
        	i++;
		}
        return params;
	}
	
	/**
	 * 查找该字段是否存在该对象中
	 * @author lucky(李文鑫) Email:578581198@qq.com
	 * 2018年3月13日-下午6:30:39
	 * @param object 对象
	 * @param key 属性
	 * @return true存在 false不存在
	 */
	public static boolean isExistence(Object object,String key){
		String[] param = getClassByParams(object);
		for (String string : param) {
			if (string.equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据属性名获取属性元素，包括各种安全范围和所有父类
	 * @param fieldName 属性名
	 * @param object 对象
	 * @throws Exception 
	 */
	public static Object getClassByValue(String fieldName, Object object) throws Exception {
		Field field = null;
		Class<?> clazz = object.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (Exception e) {
				// 这里甚么都不能抛出去。
				// 如果这里的异常打印或者往外抛，则就不会进入
			}
		}
		field.setAccessible(true);
		return field.get(object);
	}
}
