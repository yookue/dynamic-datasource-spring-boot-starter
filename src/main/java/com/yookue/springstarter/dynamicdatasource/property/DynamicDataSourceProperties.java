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

package com.yookue.springstarter.dynamicdatasource.property;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import com.yookue.springstarter.dynamicdatasource.config.DynamicDataSourceJdbcConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for dynamic datasource
 *
 * @author David Hsing
 */
@ConfigurationProperties(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX)
@Getter
@Setter
@ToString
public class DynamicDataSourceProperties implements Serializable {
    /**
     * Indicates whether to enable this starter or not
     * <p>
     * Default is {@code true}
     */
    private Boolean enabled = true;

    /**
     * Whether to enable jsr250 or not
     */
    private Boolean jsr250Enabled;

    /**
     * The annotations package to scan
     *
     * @see javax.annotation.sql.DataSourceDefinition
     * @see javax.annotation.sql.DataSourceDefinitions
     */
    private String jsr250BasePackage;

    /**
     * The priority order of {@link com.yookue.springstarter.dynamicdatasource.advisor.DataSourceDefinitionAdvisor}
     */
    private Integer definitionAdvisorOrder = Ordered.LOWEST_PRECEDENCE - 1000;

    /**
     * The priority order of {@link com.yookue.springstarter.dynamicdatasource.advisor.DataSourceRoutingAdvisor}
     */
    private Integer routingAdvisorOrder = Ordered.HIGHEST_PRECEDENCE + 1000;

    /**
     * Prefix for {@link javax.sql.DataSource} bean name
     */
    private String beanPrefix = "dynamic";    // $NON-NLS-1$

    /**
     * Suffix for {@link javax.sql.DataSource} bean name
     */
    private String beanSuffix = "RoutingDataSource";    // $NON-NLS-1$

    /**
     * Routing datasource key and jdbc properties
     * <p>
     * The key character must be alphanumeric only
     * <p>
     * Registered datasource bean name is {@code dataSourcePrefix} + key + {@code dataSourceSuffix}
     */
    private final Map<String, DataSourceProperties> datasourceRoute = new LinkedHashMap<>();
}
