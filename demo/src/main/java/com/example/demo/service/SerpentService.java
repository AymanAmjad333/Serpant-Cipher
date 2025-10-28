package com.example.demo.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Security;

@Service
public class SerpentService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("Serpent", "BC");
        keyGen.init(128); // Key size
        return keyGen.generateKey();
    }

    public String encrypt(SecretKey key, String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("Serpent/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Hex.toHexString(encrypted);
    }

    public String decrypt(SecretKey key, String ciphertextHex) throws Exception {
        Cipher cipher = Cipher.getInstance("Serpent/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] ciphertext = Hex.decode(ciphertextHex);
        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted);
    }
}
