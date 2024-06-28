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

package com.yookue.springstarter.dynamicdatasource.datasource;


import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import com.yookue.springstarter.dynamicdatasource.util.DynamicRoutingKeyHolder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * {@link javax.sql.DataSource} for dynamic routing
 *
 * @author David Hsing
 * @reference "http://www.cnblogs.com/weknow619/p/6415900.html"
 * @reference "https://www.xncoding.com/2017/07/10/spring/sb-multisource.html"
 * @reference "https://www.oschina.net/p/dynamic-datasource-spring-boot-starter"
 */
@NoArgsConstructor
@Slf4j
@SuppressWarnings({"unused", "JavadocDeclaration", "JavadocLinkAsPlainText"})
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    public DynamicRoutingDataSource(@Nonnull Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        if (log.isDebugEnabled()) {
            log.debug("Determined the current data source's routing key is '{}'", DynamicRoutingKeyHolder.getRoutingKey());
        }
        return DynamicRoutingKeyHolder.getRoutingKey();
    }
}
