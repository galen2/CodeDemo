package encryption;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/**
 * 功能：获取公钥、获取私钥、获取证书序列号、签名、签名验证
 * 概念描述：
 * 1、一个证书库可以包含多个证书；
 * 2、证书库有密码、别名，同样每一个证书也有密码、别名。
 * 3、可以从证书库导出某个别名的证书；
 * 4、导出整数中仅仅包含公钥相关信息，而私钥只能从证书库中获取
 * 5、证书可最终用X509Certificate对象来表示，因此，此对象可以获取到证书所有信息
 */
public class SignCert {
	private static char[] storepass = "100200".toCharArray();// 证书库密码
	private static char[] cakeypass = "200100".toCharArray();// 证书密码
	private static String cerFileName = "D:/Program Files/jdk1.7.0_15/bin/abnerCA.cer";// 证书路径
	private static String storeFileName = "D:\\Program Files\\jdk1.7.0_15\\bin\\abnerCALib";// 证书库路径
	private static String aliasName = "abnerCA";// 被签证书一在证书库中的alias别名
	private static String aliasName2 = "abnerCA2";// 被签证书2在证书库中的alias别名
	private static  KeyStore ks = null;
	static {
		initAbnerCALib();
	}
	public static void initAbnerCALib()  {
		// 装载证书库
		try {
			FileInputStream in = new FileInputStream(storeFileName);
			ks = KeyStore.getInstance("JKS");// JKS为证书库的类型
			ks.load(in, storepass);
		} catch (Exception e) {
		}
	}

	
	/**
	 * 获取私钥：从证书库
	 */
	public static void createPrivateKey() {
		PrivateKey privateKey = null;
		try {
			privateKey = (PrivateKey) ks.getKey(aliasName, cakeypass);
			System.out.println("privateKey:\n" + Base64.encodeBase64String(privateKey.getEncoded()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取私钥：利用KeyFactory从文本串里面提取
	 */
	public static PrivateKey createPrivateKeyToBase64(String privateKey) {
		try {
			String ALGORITHM = "RSA";
			//PKCS8EncodedKeySpec 只能提取私钥
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			return priKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取公钥：从证书库中
	 */
	public static void createPublicKeyFromStore() {
		try {
			Certificate cert = ks.getCertificate("abnerCa");
			PublicKey pubkey = cert.getPublicKey();
			byte[] encodedKey = pubkey.getEncoded();
			System.out.println(Base64.encodeBase64(encodedKey));
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取公钥：从证书文件中
	 */
	public static void createPublicKey() {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");// X.509是使用最多的一种数字证书标准
			FileInputStream in = new FileInputStream(cerFileName);

			// 第一种方式
			/*
			 * java.security.cert.Certificate cert = cf.generateCertificate(in);
			 * X509CertImpl cimp2 = new X509CertImpl(cert.getEncoded());
			 * PublicKey publicKey = cimp2.getPublicKey();
			 * System.out.println("certId:\n"+cimp2.getSerialNumber());
			 * System.out.println("publicKey:\n"+Base64.encode(publicKey.
			 * getEncoded()));
			 */

			// 第二种方式
			X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
			System.out.println("certId:\n" + cert.getSerialNumber());
			System.out.println("publicKey:\n" + Base64.encodeBase64String(cert.getPublicKey().getEncoded()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取公钥：base64编码之后字符串中
	 */
	public static PublicKey createPublicKeyToBase64(String publickey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decodeBase64(publickey);
			// X509EncodedKeySpec 只能用于提取公钥
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			return pubKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 证书序列号：每一个证书只有一个唯一序列号
	 */
	public static void getSignCertId() {
		try {
			Enumeration<String> aliasenum = ks.aliases();
			while (true) {
				String keyAlias = null;
				if (aliasenum.hasMoreElements()) {
					keyAlias = aliasenum.nextElement();
				}
				if (keyAlias == null)
					break;
				X509Certificate cert = (X509Certificate) ks.getCertificate(keyAlias);
				String certId = cert.getSerialNumber().toString();
				System.out.println("certId:" + certId);// 1833294118
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 直接从具体证书文件中获取
	 */
	public static void getSignCertIdFromCerficate() {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");// X.509是使用最多的一种数字证书标准
			FileInputStream in = new FileInputStream(cerFileName);
			X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
			System.out.println("certId:\n" + cert.getSerialNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void encryptionAnddecryptionTest() {
		String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHnonNnRzs+ExLNOz4aRxh+v5cIxdH1iymaRKkTq2tDc27ahwaQkZpzYvkwZ9He442RMj8pehxkx6VlDDCIB6mIqp8Ghu0xNIOzeLmG4zZbnV456ML9BlvWpWVrR1qB3HoQSY7OSK3SkcgYknuCwqVDyTHB8HErL0MdHlHVZo/HwIDAQAB";
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIeeic2dHOz4TEs07PhpHGH6/lwjF0fWLKZpEqROra0NzbtqHBpCRmnNi+TBn0d7jjZEyPyl6HGTHpWUMMIgHqYiqnwaG7TE0g7N4uYbjNludXjnowv0GW9alZWtHWoHcehBJjs5IrdKRyBiSe4LCpUPJMcHwcSsvQx0eUdVmj8fAgMBAAECgYBOLiKamXvXT7wLjtMc0Ns/0IVZcBE3pEvRErlgZsrP/CkhZSYWLSaST5/Zm5TKjHuK5VRH8QxyjLEQ4YDKJ+ICXI/ymBen4buVxfNJjZJjI+Q7RIFsWnviVCJwE3iNfOUJjTMVzCq7gRO2YQ6Qv6OMvFqbLzQxcDP+WRQK3hOigQJBAPbYMRL17KuMM7dE7cjsNk24nusUxuxEdG8TLw2MVV/3pCz/tSMmzBb/07lUiIlAthICMJLVDDd1eT0gH7rYUXECQQCMpkEbg8eMFxqxE8TDu+0pTpHiV1m9KHSzC8OT/SqEfvcrL0/2s74EzMqwkYg2b8Ybk+MDmxrs4ZB4rjVVAFGPAkADAXLnhjMRi619h8tVbPrkS2Ez/5bEfbjlOViTU6geeQd4vMxZ1zkY9ph/YzYeZblR0tEAmLODYVzOj7uTLNshAkB67C6I2pJCyEqGqm1UV+D9MfLj6029uSbM+KIUq7VGGSTx9CahyRNwZH9c88QlN2jf308PbraIgtbd8fsgb+fXAkEAnyyKLg4I4keY6HAsVC2hY87SAaK+GKRYv5hxIrvm3UfvKNTSdE6s/c186+CXnddJSdx4DELZmcp01Z3EfrEVjQ==";
		String before = "123456";
		try {
			byte[] plainText = before.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, createPublicKeyToBase64(publickey));
			// 用公钥进行加密，返回一个字节流
			byte[] cipherText = cipher.doFinal(plainText);

			cipher.init(Cipher.DECRYPT_MODE, createPrivateKeyToBase64(privateKey));
			// 用私钥进行解密，返回一个字节流
			byte[] newPlainText = cipher.doFinal(cipherText);
			System.out.println(new String(newPlainText, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 数字签名:私钥签名
	 */
	public static void Signature() {
		try {
			String content = "hello RSA";
			String ALGORITHM = "RSA";
			String SIGN_ALGORITHMS = "MD5WithRSA";
			String DEFAULT_CHARSET = "UTF-8";
			String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIeeic2dHOz4TEs07PhpHGH6/lwjF0fWLKZpEqROra0NzbtqHBpCRmnNi+TBn0d7jjZEyPyl6HGTHpWUMMIgHqYiqnwaG7TE0g7N4uYbjNludXjnowv0GW9alZWtHWoHcehBJjs5IrdKRyBiSe4LCpUPJMcHwcSsvQx0eUdVmj8fAgMBAAECgYBOLiKamXvXT7wLjtMc0Ns/0IVZcBE3pEvRErlgZsrP/CkhZSYWLSaST5/Zm5TKjHuK5VRH8QxyjLEQ4YDKJ+ICXI/ymBen4buVxfNJjZJjI+Q7RIFsWnviVCJwE3iNfOUJjTMVzCq7gRO2YQ6Qv6OMvFqbLzQxcDP+WRQK3hOigQJBAPbYMRL17KuMM7dE7cjsNk24nusUxuxEdG8TLw2MVV/3pCz/tSMmzBb/07lUiIlAthICMJLVDDd1eT0gH7rYUXECQQCMpkEbg8eMFxqxE8TDu+0pTpHiV1m9KHSzC8OT/SqEfvcrL0/2s74EzMqwkYg2b8Ybk+MDmxrs4ZB4rjVVAFGPAkADAXLnhjMRi619h8tVbPrkS2Ez/5bEfbjlOViTU6geeQd4vMxZ1zkY9ph/YzYeZblR0tEAmLODYVzOj7uTLNshAkB67C6I2pJCyEqGqm1UV+D9MfLj6029uSbM+KIUq7VGGSTx9CahyRNwZH9c88QlN2jf308PbraIgtbd8fsgb+fXAkEAnyyKLg4I4keY6HAsVC2hY87SAaK+GKRYv5hxIrvm3UfvKNTSdE6s/c186+CXnddJSdx4DELZmcp01Z3EfrEVjQ==";
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));
			byte[] signed = signature.sign();
			System.out.println(Base64.encodeBase64String(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验签名：公钥进行校验
	 */
	public static void verifySignature() {
		try {
			String content = "hello RSA";
			String SIGN_ALGORITHMS = "MD5WithRSA";
			String sign = "J2PLsEJBMjev7VIzOvAMZBbyGevVdki4HQ4d3g5BjzLGWFlOspTx0TGsqUAmFKzC64UzBppo8wrzXhbihVvTayJcQm7kPidTvqFa7DZ/tiCsrX5B6B39Yo3U+pCCNrL9yB+UOtxz8U/66W1tsccbyDYisE4hRDb/ugVckx7ySiQ=";
			String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHnonNnRzs+ExLNOz4aRxh+v5cIxdH1iymaRKkTq2tDc27ahwaQkZpzYvkwZ9He442RMj8pehxkx6VlDDCIB6mIqp8Ghu0xNIOzeLmG4zZbnV456ML9BlvWpWVrR1qB3HoQSY7OSK3SkcgYknuCwqVDyTHB8HErL0MdHlHVZo/HwIDAQAB";
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decodeBase64(publickey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));
			boolean bverify = signature.verify(Base64.decodeBase64(sign));
			System.out.println(bverify);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		try {
//			createPrivateKey();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
