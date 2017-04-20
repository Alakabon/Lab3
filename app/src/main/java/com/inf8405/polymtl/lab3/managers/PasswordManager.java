package com.inf8405.polymtl.lab3.managers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Handles the very basic crypto for the password
 * Taken pretty much as is from the following stackoverflow thread
 * http://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat
 */
public class PasswordManager {
    private final static String key = "A2BF98261C48C"; // 128 bit WEP key
    
    public static String encryptPassword(String plainPassword) {
        
        return plainPassword;
        /* Temporary out since decrypt isnt working
        String encryptedPassword = "";
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(plainPassword.getBytes());
            encryptedPassword = new String(encrypted);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPassword;
        */
    }
    
    public static String decryptPassword(String encryptedPassword){
        return encryptedPassword;
        
        /* This should work but doesnt...
        String plainPassword="";
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted=cipher.doFinal(encryptedPassword.getBytes());
            plainPassword = new String(decrypted);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainPassword;
        */
    }

}
