package com.commons.utilities;

import com.commons.enums.EndPoint;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class Console {
    public final static String system = "system";

    public static void input(String message) {
        System.out.print("<"+Console.system+"> " + message+": ");
    }

    public static void show(String message) {
        log("<"+Console.system+"> " + message);
    }

    public static void show(EndPoint endPoint, byte[] bytes) {
        log("<"+ endPoint.toString().toLowerCase()+"> "+new String(bytes, StandardCharsets.UTF_8));
    }

    public static void showCertificateIssuerAndCN(String certificateTitle, X509Certificate certificate) {
        show(certificateTitle + " subject: ["+CERTUtils.getCN(certificate)+"]," + " issuer: ["+CERTUtils.getIssuerCN(certificate)+"]");
    }

    public static void showStationToStationLogs(byte[] mergedOrdered, byte[] signed, byte[] signedReceived) throws NoSuchAlgorithmException {
        show(" > station-to-station protocol (sent data)");
        show(" >	    diffie-hellmean exchange: " + Utils.bytesToHex(Utils.SHA256(mergedOrdered)));
        show(" >	    signature  : " + Utils.bytesToHex(Utils.SHA256(signed)));
        show(" > station-to-station protocol (received data)");
        show(" >     signature  : " + Utils.bytesToHex(Utils.SHA256(signedReceived)));
    }

    private static void log(String message) {
        System.out.println(message);
    }
}
