/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redick.banner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Redick01
 */
@Slf4j
public class LogHelperBanner implements InitializingBean {

    private static final String[] BANNER = {" _                        _    _      _                 \n" ,
            "| |                      | |  | |    | |                \n" ,
            "| |     ___   __ _ ______| |__| | ___| |_ __   ___ _ __ \n" ,
            "| |    / _ \\ / _` |______|  __  |/ _ \\ | '_ \\ / _ \\ '__|\n" ,
            "| |___| (_) | (_| |      | |  | |  __/ | |_) |  __/ |   \n" ,
            "|______\\___/ \\__, |      |_|  |_|\\___|_| .__/ \\___|_|   \n" ,
            "              __/ |                    | |              \n" ,
            "             |___/                     |_|              \n"};

    private static final String LOG_HELPER = " :: Log-Helper :: ";

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("                                         " + LOG_HELPER);
        for (String s : BANNER) {
            log.info(s);
        }
    }
}
