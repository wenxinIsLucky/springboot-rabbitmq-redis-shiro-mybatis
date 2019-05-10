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

package com.example.yuanye.common.config;

import com.example.yuanye.modules.sys.shiro.ShiroTag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker配置
 *
 * @author
 */
@Configuration
public class FreemarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(ShiroTag shiroTag){
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:/templates");
        Map<String, Object> variables = new HashMap<>(1);
        variables.put("shiro", shiroTag);
        configurer.setFreemarkerVariables(variables);

        Properties settings = new Properties();
        settings.setProperty("default_encoding", "utf-8");
        settings.setProperty("number_format", "0.##");
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

}
