/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author a
 */
public class SHA {
//
//    //para testes
//    public static void main(String[] args) throws Exception {
//        new SHA();
//    }

    public SHA() throws Exception {
        System.out.println("hex: " + generateHash("1"));
    }

    public static String generateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hex.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hex.toString();
    }
}
