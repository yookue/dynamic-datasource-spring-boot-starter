/*
 * Copyright (c) 2020 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.dynamicdatasource.interceptor;


import java.lang.reflect.Method;
import javax.sql.DataSource;
import jakarta.annotation.Nonnull;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import com.yookue.commonplexus.javaseutil.util.RegexUtilsWraps;
import com.yookue.commonplexus.springutil.exception.InvalidBeanNameException;
import com.yookue.springstarter.dynamicdatasource.annotation.DataSourceDefinition;
import com.yookue.springstarter.dynamicdatasource.composer.DynamicDataSourceHolder;
import lombok.Setter;


/**
 * {@link org.aopalliance.intercept.Interceptor} for datasource definition
 *
 * @author David Hsing
 */
@Setter
public class DataSourceDefinitionInterceptor implements MethodInterceptor {
    private DynamicDataSourceHolder dynamicHelper;

    public DataSourceDefinitionInterceptor(@Nonnull DynamicDataSourceHolder helper) {
        dynamicHelper = helper;
    }

    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (!(result instanceof DataSource )) {
            return result;
        }
        Method method = invocation.getMethod();
        if (AnnotationUtils.findAnnotation(method, Bean.class) != null || AnnotationUtils.findAnnotation(method, Component.class) != null) {
            DataSourceDefinition annotation = AnnotationUtils.findAnnotation(method, DataSourceDefinition.class);
            if (annotation != null) {
                String legalKey = RegexUtilsWraps.reserveAlphanumeric(annotation.key());
                if (StringUtils.isBlank(legalKey)) {
                    throw new InvalidBeanNameException(annotation.key(), "must be alphanumeric");
                }
                if (RegexUtilsWraps.startsWithNumeric(legalKey)) {
                    throw new InvalidBeanNameException(annotation.key(), "must starts with alphabets");
                }
                if (!dynamicHelper.containsDataSource(legalKey)) {
                    dynamicHelper.addDataSource(legalKey, (DataSource) result);
                }
            }
        }
        return result;
    }
}
