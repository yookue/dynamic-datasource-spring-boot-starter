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

package com.yookue.springstarter.dynamicdatasource;


import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yookue.springstarter.dynamicdatasource.annotation.DataSourceDefinition;


@TestConfiguration(proxyBeanMethods = false)
class MockApplicationConfiguration {
    @Bean
    @DataSourceDefinition(key = "testdb3")
    public DataSource testdb3DataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDataSourceName("127.0.0.1/3306/test_db3");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test_db3?characterEncoding=UTF-8&useUnicode=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=GMT%2b8&zeroDateTimeBehavior=convertToNull");
        dataSource.setUser("root");
        dataSource.setPassword("mysql");
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
}
