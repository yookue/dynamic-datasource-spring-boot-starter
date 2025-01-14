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


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.yookue.commonplexus.javaseutil.util.StackTraceWraps;
import lombok.extern.slf4j.Slf4j;


@SpringBootTest(classes = MockApplicationInitializer.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(value = MockApplicationConfiguration.class)
@Slf4j
@SuppressWarnings("LoggingSimilarMessage")
class MockApplicationTest {
    @Autowired
    private MockApplicationService service;

    @Test
    void queryDb1() {
        String methodName = StackTraceWraps.getExecutingMethodName();
        String result = service.queryDb1();
        log.info("{}: Query result data is '{}'", methodName, result);
        Assertions.assertNotNull(result);
    }

    @Test
    void queryDb2() {
        String methodName = StackTraceWraps.getExecutingMethodName();
        String result = service.queryDb2();
        log.info("{}: Query result data is '{}'", methodName, result);
        Assertions.assertNotNull(result);
    }

    @Test
    void queryDb3() {
        String methodName = StackTraceWraps.getExecutingMethodName();
        String result = service.queryDb3();
        log.info("{}: Query result data is '{}'", methodName, result);
        Assertions.assertNotNull(result);
    }
}
