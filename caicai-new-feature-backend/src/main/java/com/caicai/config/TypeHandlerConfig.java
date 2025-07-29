package com.caicai.config;

import com.caicai.handler.EnumStatusTypeHandler;
import com.caicai.handler.StringToListTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * TypeHandler配置类
 * 在MyBatis初始化后注册自定义的TypeHandler
 */
@Configuration
@DependsOn("sqlSessionFactory")
public class TypeHandlerConfig implements InitializingBean {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        // 注册自定义的 TypeHandler
        typeHandlerRegistry.register(StringToListTypeHandler.class);
        typeHandlerRegistry.register(EnumStatusTypeHandler.class);
    }
} 