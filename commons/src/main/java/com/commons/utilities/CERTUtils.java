package com.commons.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CERTUtils {
	
	private static final Map<String, X509Certificate> trustedRootCAs = new HashMap<>();
	private static Map<String, X509Certificate> trustedSubCAs;
	
	static {
		/* Load the root CA, and trusted Sub CA's*/
		try {
			/* Add to list of trusted root CA's*/
			X509Certificate rootCA = loadPublicX509(Paths.certs+"rootca/rootca.pem");
			trustedRootCAs.put(getCN(rootCA), rootCA);
			trustedSubCAs = new HashMap<>();
		}
		catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean addSubCAIfTrusted(X509Certificate certificate) {
		if(isTrusted(certificate)) {
			if(!trustedSubCAs.containsKey(getCN(certificate))) {
				trustedSubCAs.put(getCN(certificate), certificate);
			}
			return true;
		}
		return false;
	}
	
	public static boolean isTrusted(X509Certificate certificate) {
		if(!isValidToday(certificate))
			return false;
		// Get the issuer that signed this certificate
		String issuerCN = getIssuerCN(certificate);
		// Check if any root CA as issued it
		if(trustedRootCAs.containsKey(issuerCN)) {
			return hasSignedCertificate(certificate, trustedRootCAs.get(issuerCN));
		}
		// Else search for Trusted SubCA's
		if(trustedSubCAs.containsKey(issuerCN)) {
			// We are only going to accept this Sub CA if we find the root CA in out list
			return isTrusted(trustedSubCAs.get(issuerCN));
		}
		return false;
	}

	public static String getIssuerCN(X509Certificate certificate) {
		String issuerName = certificate.getIssuerX500Principal().getName();
		String removedAllBefore = issuerName.substring(issuerName.indexOf("CN="), issuerName.length());
		return removedAllBefore.substring(0, removedAllBefore.indexOf(",")).replace("CN=", "");
	}
	
	public static String getCN(X509Certificate certificate) {
		String subjectName = certificate.getSubjectDN().getName();
		String removedAllBefore = subjectName.substring(subjectName.indexOf("CN="), subjectName.length());
		return removedAllBefore.substring(0, removedAllBefore.indexOf(",")).replace("CN=", "");
	}
	
	private static boolean hasSignedCertificate(X509Certificate signed, X509Certificate signer) {
		try {
			signed.verify(signer.getPublicKey());
		} 
		catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean isValidToday(X509Certificate certificate) {
		try {
			certificate.checkValidity(Calendar.getInstance().getTime());
		} 
		catch (CertificateExpiredException | CertificateNotYetValidException e) {
			return false;
		}
		return true;
	}
	
	public static X509Certificate loadPublicX509(String fileName) throws GeneralSecurityException, IOException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");		
		InputStream in = new ResourcesUtils().getFileFromResourceAsStream(fileName);
		X509Certificate cert = (X509Certificate)cf.generateCertificate(in);
	    return cert;
	}
	
	public static X509Certificate getPublicX509FromSocket(byte[] bytes) throws CertificateException, IOException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");		
		InputStream in = new ByteArrayInputStream(bytes);
		X509Certificate cert = (X509Certificate)cf.generateCertificate(in);
	    return cert;
	}
	
	public static RSAPrivateKey loadRSAPrivateKey(String fileName) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		InputStream in = new ResourcesUtils().getFileFromResourceAsStream(fileName);
		byte[] encodedKey = in.readAllBytes();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKey privKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
		return privKey;
	}
}
