package com.samlspring.sapp.agents;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenAgent {

    private final SecureRandom secureRandom = new SecureRandom(); //threadsafe

    public byte[] generateNewToken() {
        byte[] randomBytes = new byte[53];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }
}
