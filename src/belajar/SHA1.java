/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belajar;

/**
 *
 * @author arprast
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;


public class SHA1 {
    
    public static void main(String[] args) throws Exception {
    byte[] input = "1234567".getBytes();

    MessageDigest hash = MessageDigest.getInstance("SHA1");

    ByteArrayInputStream bIn = new ByteArrayInputStream(input);
    DigestInputStream dIn = new DigestInputStream(bIn, hash);
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();

    int ch;
    while ((ch = dIn.read()) >= 0) {
      bOut.write(ch);
    }
    byte[] newInput = bOut.toByteArray();
    System.out.println("in digest : " + new String(dIn.getMessageDigest().digest()));

    DigestOutputStream dOut = new DigestOutputStream( new ByteArrayOutputStream(), hash);
    dOut.write(newInput);
    dOut.close();
    System.out.println("out digest: " + new String(dOut.getMessageDigest().digest()));
  }
    
  public static byte[] encrypt(String x) throws Exception {
    java.security.MessageDigest d = null;
    d = java.security.MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(x.getBytes());
    return d.digest();
  }
}
