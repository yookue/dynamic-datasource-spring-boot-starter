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

package com.yookue.springstarter.dynamicdatasource.advisor;


import javax.annotation.Nonnull;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import com.yookue.springstarter.dynamicdatasource.pointcut.DataSourceDefinitionPointcut;
import lombok.NoArgsConstructor;


/**
 * {@link org.springframework.aop.Advisor} for datasource definition
 *
 * @author David Hsing
 */
@NoArgsConstructor
public class DataSourceDefinitionAdvisor extends AbstractGenericPointcutAdvisor {
    public DataSourceDefinitionAdvisor(@Nonnull MethodInterceptor interceptor) {
        super.setAdvice(interceptor);
    }

    @Nonnull
    @Override
    public Pointcut getPointcut() {
        return new DataSourceDefinitionPointcut();
    }
}
