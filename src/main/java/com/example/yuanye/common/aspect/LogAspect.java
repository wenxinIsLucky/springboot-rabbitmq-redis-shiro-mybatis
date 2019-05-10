package com.example.yuanye.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yuanye.common.annotation.SysLog;
import com.example.yuanye.common.utils.DateUtils;
import com.example.yuanye.common.utils.IpUtil;
import com.example.yuanye.modules.sys.entity.Log;
import com.example.yuanye.modules.sys.entity.Manager;
import com.example.yuanye.modules.sys.entity.SysLogEntity;
import com.example.yuanye.modules.sys.entity.SysUserEntity;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NamedThreadLocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


/*by lucky 2017  
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
 * 日志拦截保存到数据库
 * @author lucky(李文鑫) Email:578581198@qq.com
 * 2017年12月18日-下午2:17:55
 */
@Configuration
@Aspect
@SuppressWarnings({"rawtypes","unchecked"})
public class LogAspect {
	private static ThreadLocal<Long> startLocal = new NamedThreadLocal("ThreadLocal startTime");
	private static ThreadLocal<SysLogEntity> logLocal = new NamedThreadLocal("ThreadLocal Log");
	
	@Autowired(required=false)
	HttpServletRequest request;
	@Autowired
    AmqpTemplate amqpTemplate;
	
	@Pointcut(value = "@annotation(com.example.yuanye.common.annotation.SysLog)")
	public void controllerAspect(){}
	
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		//前置通知
		//开始时间
        long startTime = System.currentTimeMillis();
        startLocal.set(startTime);
        
        HttpSession session = this.request.getSession();
        String openid = (String)session.getAttribute("wechatUser");
		SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        SysLogEntity log = new SysLogEntity();
        log.setTitle(getTitleValue(joinPoint));
        log.setMethod(this.request.getMethod());
        log.setIp(IpUtil.getIpAddress(this.request));
		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = new Gson().toJson(args[0]);
			log.setParams(params);
		}catch (Exception e){
		}
        log.setUrl(this.request.getRequestURI());
		log.setCreateDate(DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.setType("info");
        if (sysUserEntity != null) {
        	log.setUid(sysUserEntity.getUsername());
		}else {
			log.setUid(openid);
		}
        logLocal.set(log);
	}
	
	@AfterReturning(pointcut="controllerAspect()",returning="rvt")
	public void doAfterReturning(JoinPoint joinPoint,Object rvt){
		//返回参数通知
		SysLogEntity log = logLocal.get();
		if (!Objects.nonNull(log)) {
			log = new SysLogEntity();
		}
		if (Objects.nonNull(rvt)) {
			log.setResult(rvt.toString());
		}
		//结束时间
		long endTime = System.currentTimeMillis();
		log.setTimeout((endTime - startLocal.get())+"");
		logLocal.remove();
		//这里采用的线程池来写入
		//ThreadUtils.startCreateLog(log);
		//这里采用的是中间件消息队列类写入
		JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("log", log);
		amqpTemplate.convertAndSend("logQueue", json.toJSONString());
	}
	
	/*@After("controllerAspect()")
	public void doAfter(JoinPoint joinPoint) {
		//后置通知
	}*/
	
	@AfterThrowing(pointcut="controllerAspect()", throwing="e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		//异常通知
		SysLogEntity log = logLocal.get();
		if (!Objects.nonNull(log)) {
			log = new SysLogEntity();
		}
		log.setType("error");
		log.setException(e.toString());
		//结束时间
		long endTime = System.currentTimeMillis();
		log.setTimeout((endTime - startLocal.get())+"");
		logLocal.remove();
		//这里采用的线程池来写入
		//ThreadUtils.startCreateLog(log);
		//这里采用的是中间件消息队列类写入
		JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("log", log);
		amqpTemplate.convertAndSend("logQueue", json.toJSONString());
	}
	
	/**
	 * 获取自定义注解内容
	 * @author lucky(李文鑫) Email:578581198@qq.com
	 * 2019年3月5日-下午12:18:21
	 * @param joinPoint
	 * @return
	 */
	private static String getTitleValue(JoinPoint joinPoint){
		try {
			MethodSignature signature = (MethodSignature)joinPoint.getSignature();
			Method method = signature.getMethod();
			SysLog sysLog = method.getAnnotation(SysLog.class);
			if (!Objects.nonNull(sysLog)) {
				return "Lucky.plum";
			}
			return sysLog.value();
		} catch (Exception e) {
			return "Lucky.Exception";
		}
	}
	
}
