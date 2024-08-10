package com.ckeeper.account.utils;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @CachePut(value = "authCodes", key = "#email")
    public String saveAuthCode(String email, String authCode) {
        return authCode;
    }

    @Cacheable(value = "authCodes", key = "#email")
    public String getAuthCode(String email) {
        return null; // 캐시에 존재하지 않으면 null 반환
    }

    @CacheEvict(value = "authCodes", key = "#email")
    public void deleteAuthCode(String email) {
    }
}
