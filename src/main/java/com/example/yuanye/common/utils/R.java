/*by lucky 2019年5月5日
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

package com.example.yuanye.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
