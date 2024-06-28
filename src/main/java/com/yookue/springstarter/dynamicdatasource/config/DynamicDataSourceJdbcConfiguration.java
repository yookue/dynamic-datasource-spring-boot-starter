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

package com.yookue.springstarter.dynamicdatasource.config;


import java.util.LinkedHashMap;
import javax.annotation.Nonnull;
import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;
import com.yookue.springstarter.datasourcebuilder.composer.DataSourceBuilder;
import com.yookue.springstarter.datasourcebuilder.config.DataSourceBuilderConfiguration;
import com.yookue.springstarter.dynamicdatasource.composer.DynamicDataSourceHolder;
import com.yookue.springstarter.dynamicdatasource.composer.impl.DynamicDataSourceHolderImpl;
import com.yookue.springstarter.dynamicdatasource.datasource.DynamicRoutingDataSource;
import com.yookue.springstarter.dynamicdatasource.property.DynamicDataSourceProperties;


/**
 * Configuration for JDBC of dynamic datasource
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = {DataSource.class, JdbcOperations.class, DataSourceBuilder.class})
@ConditionalOnBean(value = DataSourceBuilder.class)
@AutoConfigureAfter(value = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceBuilderConfiguration.class})
@AutoConfigureOrder(value = Ordered.LOWEST_PRECEDENCE - 2000)
@EnableConfigurationProperties(value = DynamicDataSourceProperties.class)
public class DynamicDataSourceJdbcConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.dynamic-datasource";    // $NON-NLS-1$
    public static final String DATA_SOURCE = "dynamicDataSource";    // $NON-NLS-1$
    public static final String JDBC_TEMPLATE = "dynamicJdbcTemplate";    // $NON-NLS-1$
    public static final String TRANSACTION_MANAGER = "dynamicTransactionManager";    // $NON-NLS-1$
    public static final String DATA_SOURCE_HELPER = "dynamicDataSourceHelper";    // $NON-NLS-1$
    public static final String ROUTE_PREFIX = PROPERTIES_PREFIX + ".datasource-route";    // $NON-NLS-1$

    @Bean(name = DATA_SOURCE)
    @ConditionalOnMissingBean(name = DATA_SOURCE)
    public DynamicRoutingDataSource dataSource() {
        return new DynamicRoutingDataSource(new LinkedHashMap<>(0));
    }

    @Bean(name = JDBC_TEMPLATE)
    @ConditionalOnBean(name = DATA_SOURCE)
    @ConditionalOnMissingBean(name = JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier(value = DATA_SOURCE) @Nonnull DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = TRANSACTION_MANAGER)
    @ConditionalOnProperty(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX, name = "jdbc-transaction", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(name = DATA_SOURCE)
    @ConditionalOnMissingBean(name = TRANSACTION_MANAGER)
    public TransactionManager transactionManager(@Nonnull DataSourceBuilder builder, @Qualifier(value = DATA_SOURCE) @Nonnull DataSource dataSource, @Nonnull ObjectProvider<TransactionManagerCustomizers> customizers) {
        return builder.jdbcTransactionManager(dataSource, customizers);
    }

    @Bean(name = DATA_SOURCE_HELPER)
    @ConditionalOnBean(name = DATA_SOURCE)
    @ConditionalOnMissingBean(name = DATA_SOURCE_HELPER)
    public DynamicDataSourceHolder dataSourceHelper(@Qualifier(value = DATA_SOURCE) @Nonnull DynamicRoutingDataSource dataSource) {
        return new DynamicDataSourceHolderImpl(dataSource);
    }
}
