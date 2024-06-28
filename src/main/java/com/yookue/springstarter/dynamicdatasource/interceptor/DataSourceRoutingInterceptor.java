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
import javax.annotation.Nonnull;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import com.yookue.commonplexus.javaseutil.util.RegexUtilsWraps;
import com.yookue.commonplexus.springutil.exception.InvalidBeanNameException;
import com.yookue.springstarter.dynamicdatasource.annotation.DataSourceRouting;
import com.yookue.springstarter.dynamicdatasource.util.DynamicRoutingKeyHolder;
import lombok.extern.slf4j.Slf4j;


/**
 * {@link org.springframework.aop.Pointcut} for datasource routing
 *
 * @author David Hsing
 */
@Slf4j
public class DataSourceRoutingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        DataSourceRouting annotation = AnnotationUtils.findAnnotation(method, DataSourceRouting.class);
        if (annotation == null) {
            return invocation.proceed();
        }
        String legalKey = RegexUtilsWraps.reserveAlphanumeric(annotation.key());
        if (StringUtils.isBlank(legalKey)) {
            throw new InvalidBeanNameException(annotation.key(), "must be alphanumeric");
        }
        if (RegexUtilsWraps.startsWithNumeric(legalKey)) {
            throw new InvalidBeanNameException(annotation.key(), "must starts with alphabets");
        }
        if (log.isDebugEnabled()) {
            log.debug("Method '{}.{}' is routing to dynamic data source with key '{}'", method.getDeclaringClass(), method.getName(), annotation.key());
        }
        DynamicRoutingKeyHolder.setRoutingKey(legalKey);
        Object result = invocation.proceed();
        DynamicRoutingKeyHolder.clearRoutingKey();
        return result;
    }
}
