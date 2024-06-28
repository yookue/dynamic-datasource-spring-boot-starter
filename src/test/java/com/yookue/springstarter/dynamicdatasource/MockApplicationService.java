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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.yookue.springstarter.dynamicdatasource.annotation.DataSourceRouting;
import com.yookue.springstarter.dynamicdatasource.config.DynamicDataSourceJdbcConfiguration;


@Service
class MockApplicationService {
    @Autowired
    @Qualifier(value = DynamicDataSourceJdbcConfiguration.JDBC_TEMPLATE)
    private JdbcTemplate jdbcTemplate;

    @DataSourceRouting(key = "testdb1")
    public String queryDb1() {
        String sql = "select test_name from t_test_table t where t.test_code = 'tab1'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    @DataSourceRouting(key = "testdb2")
    public String queryDb2() {
        String sql = "select test_name from t_test_table t where t.test_code = 'tab2'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    @DataSourceRouting(key = "testdb3")
    public String queryDb3() {
        String sql = "select test_name from t_test_table t where t.test_code = 'tab3'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
