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

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置
 *
 * @author
 */
@Configuration
/*@EnableSwagger2*/
public class SwaggerConfig{

    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(409).message("业务逻辑异常").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(422).message("参数校验异常").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(503).message("Hystrix异常").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求成功").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(4004).message("请重新登录").responseModel(new ModelRef("ApiError")).build());

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .globalResponseMessage(RequestMethod.GET, responseMessageList)
            .globalResponseMessage(RequestMethod.POST, responseMessageList)
            .globalResponseMessage(RequestMethod.OPTIONS, responseMessageList)
            .select()
            //加了ApiOperation注解的类，生成接口文档
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            //包下的类，生成接口文档
            //.apis(RequestHandlerSelectors.basePackage("com.example.yuanye.modules.job.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Spring boot + redis + mysql + mybatis + shiro + rabbitmq")
            .description("测试文档")
            .termsOfServiceUrl("http://192.168.0.104/")
            .description("统一请求域名:http://yyhq.yywluo.cn")
            .version("1.0.0")
            .build();
    }

}