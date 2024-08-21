package org.example.systemdesignsharding.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UrlDao {

    public Integer saveUrlInfo(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String longUrl, String shortUrl, int ttl) {
        String sql = "INSERT INTO info (long_url, short_url,ttl) VALUES (:longUrl, :shortUrl , :ttl)";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        map.put("shortUrl", shortUrl);
        map.put("ttl", ttl);
        try {
            namedParameterJdbcTemplate.update(sql, map);
            return 1;
        } catch (DataIntegrityViolationException e) {
            return 0;
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    public String findLongUrl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String longUrl) {
        String sql = "SELECT short_url FROM info WHERE long_url=:longUrl";
        Map<String, Object> map = new HashMap<>();
        map.put("longUrl", longUrl);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String findLongUrlByShortUrl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String shortUrl) {
        String sql = "SELECT long_url FROM info WHERE short_url=:shortUrl";
        Map<String, Object> map = new HashMap<>();
        map.put("shortUrl", shortUrl);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


//    public Integer testConnection(JdbcTemplate jdbcTemplate, shortenRequest shortenRequest) {
//        String sql = "INSERT INTO info(long_url, short_url, ttl) values (?,?,?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, shortenRequest.getLong_url());
//            ps.setString(2, shortenRequest.getShort_url());
//            ps.setInt(3, shortenRequest.getTtl());
//            return ps;
//        }, keyHolder);
//        return keyHolder.getKey().intValue();
//    }
}
