package com.ming.ls.config;

import com.ming.ls.modules.common.intercept.MyInterceptor;
import com.ming.ls.modules.sys.service.IEmployeeService;
import com.ming.ls.modules.sys.service.impl.EmployeeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    //url拦截器注入
//    @Autowired
//    private MyInterceptor urlInterceptor;
    //过滤路径
    private static List<String> myPathPatterns = new ArrayList<>();
    //排除路径
    private static List<String> myExcludePathPatterns = new ArrayList<>();

    /**
     * 在初始化Servlet服务时（在Servlet构造函数执行之后、init()之前执行），@PostConstruct注解的方法被调用
     */
    @PostConstruct
    void init() {
        System.out.println("Servlet init ... ");
        // 添加匹配的规则， /** 表示匹配所有规则，任意路径
        myPathPatterns.add("/**");
        //添加不拦截路径
        myExcludePathPatterns.add("/image/**");
        myExcludePathPatterns.add("/webjars/**");
        myExcludePathPatterns.add("/static/**");
        myExcludePathPatterns.add("/asserts/**");

    }

    /**
     * 在卸载Servlet服务时（在Servlet的destroy()方法之前执行），@Prdestroy注解的方法被调用
     */
    @PreDestroy
    void destroy() {
        System.out.println("Servlet destory ... ");
    }

    /**
     * 注册配置的拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//         addPathPatterns 用于添加拦截规则
//         excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns(myPathPatterns).excludePathPatterns(myExcludePathPatterns);

    }

    // 下面的方法可以选择性重写
    /**
     * 添加类型转换器和格式化器
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatterForFieldType(LocalDate.class, new USLocalDateFormatter());
    }

    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600 * 24);
    }

    /**
     * 添加静态资源映射--过滤swagger-api
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //过滤swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

        registry.addResourceHandler("/swagger/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger*");

        registry.addResourceHandler("/v2/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v2/api-docs/");

//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/image/**").addResourceLocations("classpath:/image/");

    }

//    /**
//     * 配置消息转换器--这里用的是ali的FastJson
//     * @param converters
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        //1. 定义一个convert转换消息的对象;
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        //2. 添加fastJson的配置信息，比如：是否要格式化返回的json数据;
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullStringAsEmpty,
//                SerializerFeature.DisableCircularReferenceDetect,
//                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteDateUseDateFormat);
//        //3处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        //4.在convert中添加配置信息.
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        //5.将convert添加到converters当中.
//        converters.add(fastJsonHttpMessageConverter);
//    }


    /**
     * 访问页面需要先创建个Controller控制类，再写方法跳转到页面
     * 这里的配置可实现直接访问http://localhost:8080/toLogin就跳转到login.jsp页面了
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/tologin").setViewName("/sys/permission/permission_list");

    }

    /**
     * 开启默认拦截器可用并指定一个默认拦截器DefaultServletHttpRequestHandler，比如在webroot目录下的图片：xx.png，
     * Servelt规范中web根目录（webroot）下的文件可以直接访问的，但DispatcherServlet配置了映射路径是/ ，
     * 几乎把所有的请求都拦截了，从而导致xx.png访问不到，这时注册一个DefaultServletHttpRequestHandler可以解决这个问题。
     * 其实可以理解为DispatcherServlet破坏了Servlet的一个特性（根目录下的文件可以直接访问），DefaultServletHttpRequestHandler
     * 可以帮助回归这个特性的
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
        // 这里可以自己指定默认的拦截器
        configurer.enable("DefaultServletHttpRequestHandler");
    }

//    /**
//     * 在该方法中可以启用内容裁决解析器，configureContentNegotiation()方法是专门用来配置内容裁决参数的
//     * @param configurer
//     */
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        // 表示是否通过请求的Url的扩展名来决定media type
//        configurer.favorPathExtension(true)
//                // 忽略Accept请求头
//                .ignoreAcceptHeader(true)
//                .parameterName("mediaType")
//                // 设置默认的mediaType
//                .defaultContentType(MediaType.TEXT_HTML)
//                // 以.html结尾的请求会被当成MediaType.TEXT_HTML
//                .mediaType("html", MediaType.TEXT_HTML)
//                // 以.json结尾的请求会被当成MediaType.APPLICATION_JSON
//                .mediaType("json", MediaType.APPLICATION_JSON);
//    }
}
