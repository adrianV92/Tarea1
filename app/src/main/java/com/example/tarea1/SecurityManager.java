package com.example.tarea1;

import android.hardware.fingerprint.FingerprintManager;


public class SecurityManager {

    private static SecurityManager securityManager;
    private FingerprintManager fingerprintManager;
    boolean hasFingerprintSecurity=false;

    private SecurityManager(FingerprintManager fingerprintManager){
        this.fingerprintManager=fingerprintManager;
    }

    public static SecurityManager initSecurityManager(FingerprintManager fingerprintManager){
        if (securityManager==null)
            securityManager= new SecurityManager(fingerprintManager);
         return securityManager;
    }

    public boolean prepareSecurityFinger() {
        return fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
    }

    public FingerprintManager getFingerprintManager() {
        return fingerprintManager;
    }

}
