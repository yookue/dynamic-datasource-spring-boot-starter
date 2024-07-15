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

package com.yookue.springstarter.dynamicdatasource.composer;


import javax.sql.DataSource;
import jakarta.annotation.Nullable;


/**
 * Composer interface for dynamic datasource holder
 *
 * @author David Hsing
 */
@SuppressWarnings("unused")
public interface DynamicDataSourceHolder {
    void addDataSource(@Nullable String key, @Nullable DataSource dataSource);

    void removeDataSource(@Nullable String key);

    void clearDataSource();

    boolean containsDataSource(@Nullable String key);
}
