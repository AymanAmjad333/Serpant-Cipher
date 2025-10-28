package com.example.demo.controller;

import com.example.demo.service.SerpentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SerpentController {

    @Autowired
    private SerpentService serpentService;

    @GetMapping("/generate-key")
    public Map<String, String> generateKey() {
        try {
            SecretKey key = serpentService.generateKey();
            String keyHex = org.bouncycastle.util.encoders.Hex.toHexString(key.getEncoded());
            Map<String, String> response = new HashMap<>();
            response.put("key", keyHex);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    @PostMapping("/encrypt")
    public Map<String, String> encrypt(@RequestParam String key, @RequestParam String password) {
        try {
            SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(org.bouncycastle.util.encoders.Hex.decode(key), "Serpent");
            String ciphertext = serpentService.encrypt(secretKey, password);
            Map<String, String> response = new HashMap<>();
            response.put("ciphertext", ciphertext);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    @PostMapping("/decrypt")
    public Map<String, String> decrypt(@RequestParam String key, @RequestParam String ciphertext) {
        try {
            SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(org.bouncycastle.util.encoders.Hex.decode(key), "Serpent");
            String plaintext = serpentService.decrypt(secretKey, ciphertext);
            Map<String, String> response = new HashMap<>();
            response.put("plaintext", plaintext);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
}
