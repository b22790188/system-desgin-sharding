package org.example.systemdesignsharding.service;

import lombok.extern.log4j.Log4j2;
import org.example.systemdesignsharding.dao.UrlDao;
import org.example.systemdesignsharding.model.UrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UrlService {

    @Autowired
    @Qualifier("jdbcTemplate1")
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("jdbcTemplate2")
    private JdbcTemplate jdbcTemplate2;

    @Autowired
    @Qualifier("jdbcTemplate3")
    private JdbcTemplate jdbcTemplate3;

    @Autowired
    private UrlDao urlDao;

    public void testSharding(int number) {
        int shard = Math.abs(number % 3);
        log.info("goes to shard: " + shard);
        switch (shard) {
            case 0:
                urlDao.testConnection(jdbcTemplate1, new UrlData("long1", "short1", 1));
            case 1:
                urlDao.testConnection(jdbcTemplate2, new UrlData("long2", "short2", 10));
            case 2:
                urlDao.testConnection(jdbcTemplate3, new UrlData("long3", "short3", 100));
        }
    }
}
