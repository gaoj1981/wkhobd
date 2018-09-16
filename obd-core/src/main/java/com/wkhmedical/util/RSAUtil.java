package com.wkhmedical.util;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSAUtil {
	public static final String CHARSET = "UTF-8";
	public static final String RSA_ALGORITHM = "RSA";

	/**
	 * 生成密钥对
	 * 
	 * @param keySize
	 * @return
	 */
	public static KeyPair genKeyPair(int keySize) {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}
		kpg.initialize(keySize);
		KeyPair keyPair = kpg.generateKeyPair();
		return keyPair;
	}

	/**
	 * 获取公钥
	 * 
	 * @param keyPair
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static RSAPublicKey getPublicKey(KeyPair keyPair) throws InvalidKeySpecException {
		PublicKey publicKey = keyPair.getPublic();
		if (publicKey instanceof RSAPublicKey) {
			return (RSAPublicKey) publicKey;
		} else {
			throw new InvalidKeySpecException();
		}
	}

	/**
	 * 获取私钥
	 * 
	 * @param keyPair
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static RSAPrivateKey getPrivateKey(KeyPair keyPair) throws InvalidKeySpecException {
		PrivateKey privateKey = keyPair.getPrivate();
		if (privateKey instanceof RSAPrivateKey) {
			return (RSAPrivateKey) privateKey;
		} else {
			throw new InvalidKeySpecException();
		}
	}

	/**
	 * 获取私钥
	 * 
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(PrivateKey privateKey) throws Exception {
		return Base64.encodeBase64String(privateKey.getEncoded());
	}

	/**
	 * 获取公钥
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(PublicKey publicKey) throws Exception {
		return Base64.encodeBase64String(publicKey.getEncoded());
	}

	/**
	 * 得到公钥
	 * 
	 * @param publicKey
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		return key;
	}

	/**
	 * 得到私钥
	 * 
	 * @param privateKey
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		return key;
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicEncrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),
					publicKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws InvalidParameterSpecException
	 */

	public static String privateDecrypt(String data, RSAPrivateKey privateKey) throws InvalidParameterSpecException {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
					privateKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new InvalidParameterSpecException("解密字符串[" + data + "]时遇到异常");
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 */

	public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),
					privateKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws InvalidParameterSpecException
	 */

	public static String publicDecrypt(String data, RSAPublicKey publicKey) throws InvalidParameterSpecException {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
					publicKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new InvalidParameterSpecException("解密字符串[" + data + "]时遇到异常");
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int offSet = 0;
			byte[] buff;
			int i = 0;
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
			byte[] resultDatas = out.toByteArray();
			return resultDatas;
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
	}

	public static void main(String[] args) throws Exception {
//		KeyPair keyPair = genKeyPair(2048);
//		String privateKey = getPrivateKey(keyPair.getPrivate());
//		String publicKey = getPublicKey(keyPair.getPublic());
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDUGEHKSrKCYyOcNc0q6v2zL31U7oUYaB4ECe2rgMn7IVu3ZwyhvEmSyf/Iy110OCKpGHQ5786GrO3jkMkIplkAbo5TCqahXugxF9TwL0ewz2vthTrmpovUpHOBga7DPM1ewlO3k1fkkc5W2la5ubvVDK+7yM08YD/vlqvSTVap49h1hlBhvrgK/2rDFbJ/Z3WIBnKxkYSl0I2kkgUlTqEnV/d+GtPYjk3Y+R9FPEDBQFDgX2uR4J/R7L0C2iUQTLciVzXAlgZek3vRsRDZ0Eild8oB6RMca6Oc7UgPoh0hrh4H07wckizMEwzZxoquJx9Y1m81rxPy+vTYCj9D1EwlAgMBAAECggEBAIhlORoAw5+6ZdI1f0XUbWboaW9PHOS/QF5UjDW40yvfHf5qFOOGFMKDiSz2K5lr6E5aXZRMtcJCP5ITMFWRtLbkYj7hIJuyTxwTOWtk3HPGUKAlB+YAwPf08hsc7Oi9l00TJ4dtCB8D7XBJZXB3E2ZFjFX1IpdrRnKrsL/D4LHLmphG8GS76o5kxwToG4vh+E7zCDk9GO96LU8xoFmjTAY/p126pDxayfDrq9ttekOfs5bBd8fT4q6Ng0+Lzb9m6gE/hQNeNTS2wuKhHdqYAquN0jrZfdb8C/j3vFA7QoWCt5+4dYrxybyg2SlAfNP6YE74JoJsOGq0NYpwEofvGFkCgYEA+jPcRcDSSycR3iotjpMYR1LBWi8KAsL/hzvcfhR8maK05i5opABj8DedCeo2nO7u0lJfXw/oKCKcL8yz8UXNdUccw9Xnge6kavo8m5ASbyfDyXg+fa+Q8dkj2y5wwwydjtLB8pikSTCl2FTKtk1Zb8Cy78KDeyovHAg+Fiuk3vcCgYEA2QJZs3qkfN0IIhVYuj7cDOd24mAqgvB9GaS9c3aA3nW1cCgLS2P8T1iCTymSEDnPOKm9iLD8f2VzvvPAliXlgCCV5Tj1JMz3H1zLR1t3erBSZ4bJ9Vtl3vyOrkYcYSK/jEoAGpJzUkA2yEQNr+HJRjyM3N3NnOWh5cUcAUv5usMCgYABzFOd8LfSMHKxbQ4swyPeh1q6YjIpOOuLNkPsaAtJM2EnclfwLcyKEmw/gP5pfFIdEth6n2uktjL3KPp2FnFWkW9EfQoP5f4qgqfi2lM3l0qR7RpN3vAkvNHEJn5K+JE9UIhyTOSJXh8jAeD/FGk7I2Bl1rkJLpFuGyUPFbs2mwKBgQC9h3X8cQK0Iv91dRtXHotlocqND9xRG+TGvsDP1RHcOQXL/M1QfPTh/7Vfmjj9ft/i5yyn85uzUFH01kK/YD2+17bSyhogl/rFI3BO1C3Xs6Nk064TYk3ooL+kqYsjswuEZSUOEBY0Ie02n2ks0Y/Ojheg1v87JrttR+ebolVrrQKBgQCukS2JVkmjhUjxYJLwz+tC4JCEY9/e0LM6q0PGfH0GM4sMTBElG9sjo6lxrPVbE2SjxTjDDs88xjPVq0sRLBI4Uu4aog0ypnnWPW4/pVQ/RYt9AwnFxT0HtecUiupfId+JddkslJt/N6Ds1sbY2uUZmeiL7S68heBavoY7KBBoGw==";
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1BhBykqygmMjnDXNKur9sy99VO6FGGgeBAntq4DJ+yFbt2cMobxJksn/yMtddDgiqRh0Oe/Ohqzt45DJCKZZAG6OUwqmoV7oMRfU8C9HsM9r7YU65qaL1KRzgYGuwzzNXsJTt5NX5JHOVtpWubm71Qyvu8jNPGA/75ar0k1WqePYdYZQYb64Cv9qwxWyf2d1iAZysZGEpdCNpJIFJU6hJ1f3fhrT2I5N2PkfRTxAwUBQ4F9rkeCf0ey9AtolEEy3Ilc1wJYGXpN70bEQ2dBIpXfKAekTHGujnO1ID6IdIa4eB9O8HJIszBMM2caKricfWNZvNa8T8vr02Ao/Q9RMJQIDAQAB";
		System.out.println("公钥: \n" + publicKey);
		System.out.println("私钥： \n" + privateKey);

		String str = "{\"id\":\"abcdeabcde\",\"lic\":{\"sn\": \"F1C732521557AED3D2\",\"stats\": {\"bio_used\": 12,\"user_used\": 119},\"v\": 1536933830316},\"stats\":{\"bio_used\":1000,\"user_used\":199},\"st\":"+DateUtil.getTimestamp()+"}";
		System.out.println("\r明文：\r" + str);
		System.out.println("明文大小：\r" + str.getBytes().length);

		String encodedData = publicEncrypt(str, getPublicKey(publicKey));
		System.out.println("\r密文：\r\n" + encodedData);

		String decodedData = privateDecrypt(encodedData, getPrivateKey(privateKey));
		System.out.println("解密后文字: \r\n" + decodedData);
		
		String encodedDataByPrivate = privateEncrypt("RSA加密测试开始：Hello word!   高剑:那这个云端后台给到客户端的授权信息包含什么？是新的公钥和私钥？  羊大爷:我想的可能有点简单，密钥对就固定不变了，比如云端私钥，车上公钥，可以预置多对，但不再动态更新了（或者后续升级版本或加密狗时再更新）羊大爷:所以不知道这样安不安全？高剑:嗯 主要就是密钥安全就行！@Derek 看看是否可行！？ RSA加密测试结束", getPrivateKey(privateKey));
		System.out.println("\r私钥密文：\r\n" + encodedDataByPrivate);
		
		String decodeDataByPublic = publicDecrypt("LvTHA9CoYc8ivOms829jeyCZcDoSACpxVB0R5bjqGmLhX8AJQpguPUcicYeWkL5ZshMCR1Bk72L8V3SrtNI57lOzw45LrkxDbERb4QdIwHuTkghX9IGFqecVv1V2MZhh2hrbGOL7rc+GcmRKESkvQ4Jw3rKZRh67viZ31yEp6xOLoI8NIxwZ77HLHA4CxjffozpQTo2Gxa32G1Es/CPuqk0JBLDpDx0l0cf++D0faWn51LynHlOHrF6Xxfib5A0yKr+UtArjrPCLNoq4vcmxwGoYEmakxdCygl1dxWMX8frdeBI6DU1p8NVfqkWNgFleR5nNqFRzuITq3WOhFkorJA==", getPublicKey(publicKey));
		System.out.println("公钥解密后文字: \r\n" + decodeDataByPublic);
	}

}
