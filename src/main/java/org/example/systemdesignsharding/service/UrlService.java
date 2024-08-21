package org.example.systemdesignsharding.service;

import lombok.extern.log4j.Log4j2;
import org.example.systemdesignsharding.dao.UrlDao;
import org.example.systemdesignsharding.model.shortenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Log4j2
public class UrlService {

    @Autowired
    @Qualifier("namedParameterJdbcTemplate1")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate1;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate2")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate2;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate3")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate3;

    @Autowired
    private UrlDao urlDao;


    private static final String BASE_URL = "http://localhost:8080/";
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int KEY_LENGTH = 7;


    //長網址轉短網址主要邏輯
    public String shortenUrl(shortenRequest shortenRequest) {
        String shortKey = generateShortUrl(shortenRequest.getLongUrl());
        String shortUrl = BASE_URL + shortKey;
        // asciiSum used to help sharding
        int asciiSum = convertAndSumAscii(shortKey);
        NamedParameterJdbcTemplate shard = getShardedTemplate(asciiSum);

        boolean state = true;

        try {
            //確認資料庫是否已有該長網址轉換之結果

            String longUrl = shortenRequest.getLongUrl();

            String findLongUrlResult = urlDao.findLongUrl(shard, longUrl);
            if (findLongUrlResult != null) {
                return findLongUrlResult;
            }
            //確保插入的數據不會重複
            while (state) {
                if (urlDao.saveUrlInfo(shard, shortenRequest.getLongUrl(), shortUrl, shortenRequest.getTtl()) == 1) {
                    state = false;
                }
            }


            return shortUrl;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    public String getLongUrl(String shortKey) {
        // 兩種結果 - 1.longUrl(成功取得 url) 2.null(沒有該url)

        // calculate short key
        int asciiSum = convertAndSumAscii(shortKey);
        NamedParameterJdbcTemplate shardTemplate = getShardedTemplate(asciiSum);

        return urlDao.findLongUrlByShortUrl(shardTemplate, BASE_URL + shortKey);
    }

    private String generateShortUrl(String longUrl) {
        long asciiValue = getAsciiValue(longUrl);
        String base62Encoded = encodeBase62(asciiValue);
        return getRandomSubstring(base62Encoded);
    }

    private long getAsciiValue(String input) {
        byte[] bytes = input.getBytes();
        long asciiValue = 0;
        for (byte b : bytes) {
            asciiValue = (asciiValue * 256) + (b & 0xFF);
        }
        return asciiValue;
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    private String getRandomSubstring(String input) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++) {
            sb.append(input.charAt(random.nextInt(input.length())));
        }
        return sb.toString();
    }


    //todo: sum up ascii code of shorten url function
    public int convertAndSumAscii(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int asciiValue = (int) c;
            sum += asciiValue;
        }
        return sum;
    }


    public NamedParameterJdbcTemplate getShardedTemplate(int number) {
        int shard = Math.abs(number % 3);

        log.info("goes to shard: " + (shard + 1));

        switch (shard) {
            case 0:
                return namedParameterJdbcTemplate1;
            case 1:
                return namedParameterJdbcTemplate2;
            case 2:
                return namedParameterJdbcTemplate3;
        }

        log.error("Wrong input!");
        return null;
    }
}
