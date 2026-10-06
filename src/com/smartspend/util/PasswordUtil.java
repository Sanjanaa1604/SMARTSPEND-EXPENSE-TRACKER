package com.smartspend.util;

import java.security.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public final class PasswordUtil {
    private PasswordUtil() {}
    public static String hash(String password) {
        try {
            byte[] salt=new byte[16]; SecureRandom.getInstanceStrong().nextBytes(salt);
            PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,120000,256);
            byte[] hash=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt)+":"+Base64.getEncoder().encodeToString(hash);
        } catch(Exception e){throw new IllegalStateException("Password hashing failed",e);}
    }
    public static boolean verify(String password,String stored){
        try{
            String[] p=stored.split(":"); if(p.length!=2)return false;
            byte[] salt=Base64.getDecoder().decode(p[0]); byte[] expected=Base64.getDecoder().decode(p[1]);
            PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,120000,expected.length*8);
            byte[] actual=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(expected,actual);
        }catch(Exception e){return false;}
    }
}
