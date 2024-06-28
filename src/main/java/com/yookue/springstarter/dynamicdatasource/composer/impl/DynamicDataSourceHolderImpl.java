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

package com.yookue.springstarter.dynamicdatasource.composer.impl;


import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.yookue.commonplexus.javaseutil.util.MapPlainWraps;
import com.yookue.springstarter.dynamicdatasource.composer.DynamicDataSourceHolder;
import com.yookue.springstarter.dynamicdatasource.datasource.DynamicRoutingDataSource;
import com.yookue.springstarter.dynamicdatasource.util.DynamicRoutingKeyHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Composer implementation for dynamic datasource holder
 *
 * @author David Hsing
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class DynamicDataSourceHolderImpl implements DynamicDataSourceHolder {
    private static final ThreadLocal<Map<Object, Object>> dataSourceHolder = new ThreadLocal<>();

    @Setter
    private DynamicRoutingDataSource dynamicDataSource;

    @Override
    public synchronized void addDataSource(@Nullable String key, @Nullable DataSource dataSource) {
        if (StringUtils.isBlank(key) || dataSource == null) {
            return;
        }
        Map<Object, Object> mapSources = dataSourceHolder.get();
        mapSources = ObjectUtils.defaultIfNull(mapSources, new LinkedHashMap<>(1));
        mapSources.put(key, dataSource);
        dataSourceHolder.set(mapSources);
        dynamicDataSource.setTargetDataSources(dataSourceHolder.get());
        dynamicDataSource.afterPropertiesSet();
    }

    @Override
    public synchronized void removeDataSource(@Nullable String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        if (StringUtils.equalsIgnoreCase(key, DynamicRoutingKeyHolder.getRoutingKey())) {
            if (log.isDebugEnabled()) {
                log.debug(key, "Data source with routing key '{}' is in using currently, will clear it first.");
            }
            DynamicRoutingKeyHolder.clearRoutingKey();
        }
        Map<Object, Object> mapSources = dataSourceHolder.get();
        if (mapSources != null) {
            mapSources.remove(key);
        }
        dataSourceHolder.set(mapSources);
        dynamicDataSource.setTargetDataSources(dataSourceHolder.get());
        dynamicDataSource.afterPropertiesSet();
    }

    @Override
    public synchronized void clearDataSource() {
        DynamicRoutingKeyHolder.clearRoutingKey();
        Map<Object, Object> mapSources = dataSourceHolder.get();
        if (mapSources != null) {
            mapSources.clear();
        }
        dataSourceHolder.set(mapSources);
        dynamicDataSource.setTargetDataSources(dataSourceHolder.get());
        dynamicDataSource.afterPropertiesSet();
    }

    @Override
    public boolean containsDataSource(@Nullable String key) {
        return StringUtils.isNotBlank(key) && MapPlainWraps.containsKey(dataSourceHolder.get(), key);
    }
}
