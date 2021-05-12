package com.samlspring.sapp.agents;

import org.springframework.stereotype.Service;
import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class AuthenticationProviderService {
    SecureRandom secureRandom = null; //threadsafe

    public byte[] generateNewToken() {

        try {
            secureRandom = SecureRandom.getInstance("DRBG", DrbgParameters.instantiation(256, DrbgParameters.Capability.RESEED_ONLY, null));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] randomBytes = new byte[53];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }
}
