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
