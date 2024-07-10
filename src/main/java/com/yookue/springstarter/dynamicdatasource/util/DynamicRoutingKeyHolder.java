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

package com.yookue.springstarter.dynamicdatasource.util;


import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * Utilities for dynamic datasource routing
 *
 * @author David Hsing
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class DynamicRoutingKeyHolder {
    private static final ThreadLocal<String> keyHolder = new ThreadLocal<>();

    @Nullable
    public static String getRoutingKey() {
        return keyHolder.get();
    }

    public static synchronized void setRoutingKey(@Nullable String key) {
        if (StringUtils.isNotBlank(key)) {
            if (log.isDebugEnabled()) {
                log.debug("Routing to dynamic data source with key '{}'", key);
            }
            keyHolder.set(key);
        } else {
            clearRoutingKey();
        }
    }

    public static synchronized void clearRoutingKey() {
        if (log.isDebugEnabled()) {
            log.debug("Cleaning up the dynamic data source routing key");
        }
        keyHolder.remove();
    }
}
