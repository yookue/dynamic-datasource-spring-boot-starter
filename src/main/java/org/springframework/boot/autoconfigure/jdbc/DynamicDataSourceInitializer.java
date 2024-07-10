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

package org.springframework.boot.autoconfigure.jdbc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyNameException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import com.yookue.commonplexus.javaseutil.constant.CharVariantConst;
import com.yookue.commonplexus.javaseutil.util.CollectionPlainWraps;
import com.yookue.commonplexus.javaseutil.util.RegexUtilsWraps;
import com.yookue.commonplexus.javaseutil.util.StringUtilsWraps;
import com.yookue.commonplexus.springutil.support.ClassPathScanningSimpleComponentProvider;
import com.yookue.commonplexus.springutil.util.BeanFactoryWraps;
import com.yookue.commonplexus.springutil.util.ClassUtilsWraps;
import com.yookue.springstarter.datasourcebuilder.composer.DataSourceBuilder;
import com.yookue.springstarter.datasourcebuilder.constant.DataSourcePoolConst;
import com.yookue.springstarter.datasourcebuilder.enumeration.DataSourcePoolType;
import com.yookue.springstarter.datasourcebuilder.property.C3p0DataSourceProperties;
import com.yookue.springstarter.datasourcebuilder.property.Dbcp2DataSourceProperties;
import com.yookue.springstarter.datasourcebuilder.property.DruidDataSourceProperties;
import com.yookue.springstarter.datasourcebuilder.property.HikariDataSourceProperties;
import com.yookue.springstarter.datasourcebuilder.property.OracleUcpDataSourceProperties;
import com.yookue.springstarter.datasourcebuilder.property.TomcatDataSourceProperties;
import com.yookue.springstarter.dynamicdatasource.composer.DynamicDataSourceHolder;
import com.yookue.springstarter.dynamicdatasource.config.DynamicDataSourceJdbcConfiguration;
import com.yookue.springstarter.dynamicdatasource.property.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Dynamic datasource initializer
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.dynamic-datasource", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = {DataSource.class, JdbcOperations.class})
@ConditionalOnBean(value = {DataSourceBuilder.class, DynamicDataSourceHolder.class})
@AutoConfigureAfter(value = DynamicDataSourceJdbcConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class DynamicDataSourceInitializer implements BeanFactoryAware, InitializingBean {
    private final DynamicDataSourceProperties sourceProperties;
    private final DynamicDataSourceHolder sourceHolder;
    private final DataSourceBuilder sourceBuilder;

    @Setter
    protected BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() {
        configureDataSource();
    }

    protected void configureDataSource() {
        if (CollectionUtils.isEmpty(sourceProperties.getDatasourceRoute())) {
            return;
        }
        for (Map.Entry<String, DataSourceProperties> entry : sourceProperties.getDatasourceRoute().entrySet()) {
            if (StringUtils.isBlank(entry.getKey()) || ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            String legalKey = RegexUtilsWraps.reserveAlphanumeric(entry.getKey());
            if (StringUtils.isBlank(legalKey)) {
                if (log.isWarnEnabled()) {
                    log.warn("Dynamic datasource route key '{}' is illegal, must be alphanumeric", entry.getKey());
                }
                throw new InvalidConfigurationPropertyNameException(DynamicDataSourceJdbcConfiguration.ROUTE_PREFIX + "." + entry.getKey(), null);
            }
            if (RegexUtilsWraps.startsWithNumeric(legalKey)) {
                if (log.isWarnEnabled()) {
                    log.warn("Dynamic datasource route key '{}' is illegal, must starts with alphabets", entry.getKey());
                }
                throw new InvalidConfigurationPropertyNameException(DynamicDataSourceJdbcConfiguration.ROUTE_PREFIX + "." + entry.getKey(), null);
            }
            if (StringUtils.isNotBlank(sourceProperties.getBeanPrefix())) {
                legalKey = StringUtils.capitalize(legalKey);
            }
            String beanName = String.format("%s%s%s", ObjectUtils.getDisplayString(sourceProperties.getBeanPrefix()), legalKey, ObjectUtils.getDisplayString(sourceProperties.getBeanSuffix()));    // $NON-NLS-1$
            if (BeanFactoryWraps.containsBeanDefinition(beanFactory, beanName)) {
                if (log.isWarnEnabled()) {
                    log.warn("Bean name '{}' already exists, ignored dynamic datasource route with key '{}'", beanName, entry.getKey());
                }
            } else {
                DataSource dataSource;
                try {
                    if (StringUtils.isNotBlank(entry.getValue().getJndiName())) {
                        dataSource = sourceBuilder.dataSource(entry.getValue());
                    } else {
                        DataSourceProperties subProperties = determineDataSourceProperties(entry.getKey(), entry.getValue());
                        dataSource = sourceBuilder.dataSource(subProperties);
                    }
                    if (dataSource != null) {
                        BeanFactoryWraps.registerSingletonBean(beanFactory, beanName, dataSource);
                        sourceHolder.addDataSource(entry.getKey(), dataSource);
                    } else {
                        if (log.isWarnEnabled()) {
                            log.warn("Dynamic datasource route key '{}' can not be created", entry.getKey());
                        }
                    }
                } catch (Exception ex) {
                    String message = String.format("Dynamic datasource route key '%s' can not be initialized", entry.getKey());    // $NON-NLS-1$
                    log.error(message, ex);
                }
            }
        }
        // Scan jsr250 annotations
        if (sourceHolder != null && BooleanUtils.isTrue(sourceProperties.getJsr250Enabled()) && StringUtils.isNotBlank(sourceProperties.getJsr250BasePackage())) {
            ClassPathScanningSimpleComponentProvider provider = new ClassPathScanningSimpleComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(DataSourceDefinition.class, true, true));
            provider.addIncludeFilter(new AnnotationTypeFilter(DataSourceDefinitions.class, true, true));
            Set<BeanDefinition> components = provider.findCandidateComponents(sourceProperties.getJsr250BasePackage());
            if (CollectionUtils.isEmpty(components)) {
                return;
            }
            Set<String> classNames = components.stream().map(BeanDefinition::getBeanClassName).collect(Collectors.toSet());
            List<DataSourceDefinition> definitions = new ArrayList<>();
            for (String className : classNames) {
                Class<?> clazz = ClassUtilsWraps.forNameQuietly(className);
                if (clazz == null) {
                    continue;
                }
                Optional.ofNullable(AnnotationUtils.getAnnotation(clazz, DataSourceDefinition.class)).ifPresent(definitions::add);
                Optional.ofNullable(AnnotationUtils.getAnnotation(clazz, DataSourceDefinitions.class)).ifPresent(element -> CollectionPlainWraps.addAll(definitions, element.value()));
            }
            // Prepare the data source definitions
            if (!CollectionUtils.isEmpty(definitions)) {
                for (DataSourceDefinition definition : definitions) {
                    Optional.ofNullable(determineDataSource(definition)).ifPresent(element -> sourceHolder.addDataSource(definition.name(), element));
                }
            }
        }
    }

    @Nonnull
    protected DataSourceProperties determineDataSourceProperties(@Nonnull String key, @Nonnull DataSourceProperties properties) {
        Class<? extends DataSource> poolType = properties.getType();
        if (poolType == null) {
            if (log.isWarnEnabled()) {
                log.warn("Dynamic datasource route key '{}' missing 'type' property, using generic type instead", key);
            }
            return properties;
        }
        String propertyName = StringUtils.join(DynamicDataSourceJdbcConfiguration.ROUTE_PREFIX, CharVariantConst.DOT, key);
        DataSourceProperties result = null;
        String typeName = ClassUtils.getQualifiedName(poolType);
        switch (typeName) {
            case DataSourcePoolConst.C3P0:
                result = sourceBuilder.dataSourceProperties(propertyName, C3p0DataSourceProperties.class);
                break;
            case DataSourcePoolConst.DBCP2:
                result = sourceBuilder.dataSourceProperties(propertyName, Dbcp2DataSourceProperties.class);
                break;
            case DataSourcePoolConst.DRUID:
                result = sourceBuilder.dataSourceProperties(propertyName, DruidDataSourceProperties.class);
                break;
            case DataSourcePoolConst.HIKARI:
                result = sourceBuilder.dataSourceProperties(propertyName, HikariDataSourceProperties.class);
                break;
            case DataSourcePoolConst.ORACLE_UCP:
                result = sourceBuilder.dataSourceProperties(propertyName, OracleUcpDataSourceProperties.class);
                break;
            case DataSourcePoolConst.TOMCAT:
                result = sourceBuilder.dataSourceProperties(propertyName, TomcatDataSourceProperties.class);
                break;
            default:
                if (log.isWarnEnabled()) {
                    log.warn("Dynamic datasource route key '{}' has an unsupported 'type' property '{}', using generic type instead", key, typeName);
                }
                break;
        }
        return (result == null) ? properties : result;
    }

    @Nullable
    protected DataSource determineDataSource(@Nonnull DataSourceDefinition definition) {
        if (StringUtils.isBlank(definition.name())) {
            return null;
        }
        DataSourceProperties properties = new DataSourceProperties();
        properties.setName(definition.name());
        properties.setDriverClassName(definition.className());
        properties.setUrl(definition.url());
        properties.setUsername(definition.user());
        properties.setPassword(definition.password());
        if (ArrayUtils.isNotEmpty(definition.properties())) {
            Map<String, String> props = new LinkedHashMap<>(ArrayUtils.getLength(definition.properties()));
            Arrays.stream(definition.properties()).map(element -> StringUtilsWraps.splitBy(element, CharVariantConst.EQUALS)).filter(element -> ArrayUtils.getLength(element) == 2).forEach(element -> props.put(element[0], element[1]));
            properties.getXa().setProperties(props);
        }
        for (DataSourcePoolType poolType : DataSourcePoolType.values()) {
            if (ClassUtilsWraps.isPresent(poolType.getValue())) {
                properties.setType(ClassUtilsWraps.forNameAsQuietly(poolType.getValue(), DataSource.class));
                break;
            }
        }
        return sourceBuilder.dataSource(properties);
    }
}
