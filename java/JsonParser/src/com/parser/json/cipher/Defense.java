package com.parser.json.cipher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Defense {
	private String mEncodeString = "shLab!17KctIanay";
	private Cipher mCipher = null; 
	private Key mAesKey = null; 
	
	private static Defense encode = null;
	
	public Defense() 
			throws NoSuchAlgorithmException, NoSuchPaddingException{
		mCipher = Cipher.getInstance("AES");
		mAesKey = new SecretKeySpec(mEncodeString.getBytes(), "AES");
	}
	
	public String encryptData(String sData) 
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		mCipher.init(Cipher.ENCRYPT_MODE, mAesKey);
		byte[] encrypted = mCipher.doFinal(sData.getBytes());
		return encodeIntoBase64(encrypted);
	}
	
	
	public String decryptData(String encrypted) 
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException{
		mCipher.init(Cipher.DECRYPT_MODE, mAesKey);
		String decrypted = new String(mCipher.doFinal(decodeIntoBase64(encrypted)));
		return decrypted;
	}
	
	public byte[] decodeIntoBase64(String  encryptedString) throws UnsupportedEncodingException{
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] bArray = encryptedString.getBytes("UTF-8");
		return decoder.decode(bArray);
	}
	
	public String encodeIntoBase64(byte[] bData) throws UnsupportedEncodingException{
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] finalBytes = encoder.encode(bData);
		
		String encryptedString = new String(finalBytes, "UTF-8");
		return encryptedString;
	}
	
	public static Defense getInstance() 
			throws NoSuchAlgorithmException, NoSuchPaddingException{
		if(encode == null)
			encode = new Defense();
		return encode;
	}
}
