package com.github.norwick.reciperodeo.service;

/**
 * this is from gravatar to hash to their specification
 */

import java.io.*;
import java.security.*;
/**
 * Quickstart MD5 hashing util provided by Gravatar
 */
public class MD5Util {
 /**
  * converts md5 hash to string (unused)
  * @param array array containing md5 hash
  * @return string representation of hash
  */
  public static String hex(byte[] array) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
      sb.append(Integer.toHexString((array[i]
          & 0xFF) | 0x100).substring(1,3));        
      }
      return sb.toString();
  }


 /**
  * converts string to md5 hash
  * @param message message to convert (in this case emails)
  * @return md5 hash representation of string
  */
  public static String md5Hex (String message) {
      try {
      MessageDigest md = 
          MessageDigest.getInstance("MD5");
      return hex (md.digest(message.getBytes("CP1252")));
      } catch (NoSuchAlgorithmException e) {
      } catch (UnsupportedEncodingException e) {
      }
      return null;
  }
}