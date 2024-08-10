package com.ckeeper.account.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MailUtilTest {
    @Autowired
    private MailUtil mailUtil;

    @Test
    public void testCacheFunctionality() {
        String email = "test@example.com";
        String authCode = "123456";

        // Save auth code
        mailUtil.saveAuthCode(email, authCode);

        // Retrieve auth code from cache
        String cachedAuthCode = mailUtil.getAuthCode(email);
        assertThat(cachedAuthCode).isEqualTo(authCode);

        // Delete auth code
        mailUtil.deleteAuthCode(email);

        // Verify auth code is deleted
        cachedAuthCode = mailUtil.getAuthCode(email);
        assertThat(cachedAuthCode).isNull();
    }
}
