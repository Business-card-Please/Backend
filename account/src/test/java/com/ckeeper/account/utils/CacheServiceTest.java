package com.ckeeper.account.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CacheServiceTest {
    @Autowired
    private CacheService cacheService;

    @Test
    public void testCacheFunctionality() {
        String email = "test@example.com";
        String authCode = "123456";

        // Save auth code
        cacheService.saveAuthCode(email, authCode);

        // Retrieve auth code from cache
        String cachedAuthCode = cacheService.getAuthCode(email);
        assertThat(cachedAuthCode).isEqualTo(authCode);

        // Delete auth code
        cacheService.deleteAuthCode(email);

        // Verify auth code is deleted
        cachedAuthCode = cacheService.getAuthCode(email);
        assertThat(cachedAuthCode).isNull();
    }
}
