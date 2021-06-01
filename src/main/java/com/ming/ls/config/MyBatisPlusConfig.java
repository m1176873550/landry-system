package com.ming.ls.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.ming.ls.modules.common.handler.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 *  手动配置，可在application里注解，使用默认配置，通过
 * @author : 明杰
 * @date : 2020/1/9 15:33

 * @return : null
 */
@Configuration
@MapperScan("com.ming.ls.modules.sys.dao")
@EnableTransactionManagement
public class MyBatisPlusConfig {

    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
//    /**
//     * 相当于 @MapperScan("com.ming.ls.modules.sys.dao")，也可在***Application.java中用
//     * @return
//     */
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
//        mapperScannerConfigurer.setBasePackage("com.ming.ls.modules.sys.dao*");
//        return mapperScannerConfigurer;
//    }

//    @Bean
//    public GlobalConfig globalConfig() {
//        //数据库配置
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
//
//        // 默认为UUID
//        dbConfig.setIdType(IdType.ID_WORKER_STR);
//
//        //全局配置
//        GlobalConfig globalConfig = new GlobalConfig();
//        //全局配置
//        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
//        globalConfig.setDbConfig(dbConfig);
//        return globalConfig;
//    }
}
