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

package com.yookue.springstarter.dynamicdatasource.pointcut;


import java.lang.reflect.Method;
import javax.sql.DataSource;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import com.yookue.springstarter.dynamicdatasource.annotation.DataSourceDefinition;


/**
 * {@link org.springframework.aop.Pointcut} for datasource definition
 *
 * @author David Hsing
 */
public class DataSourceDefinitionPointcut extends StaticMethodMatcherPointcut {
    @Override
    public boolean matches(@Nonnull Method method, @Nullable Class<?> targetClazz) {
        return ClassUtils.isAssignable(DataSource.class, method.getReturnType()) && AnnotatedElementUtils.hasAnnotation(method, DataSourceDefinition.class);
    }
}
