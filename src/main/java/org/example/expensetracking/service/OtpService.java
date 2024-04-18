package org.example.expensetracking.service;

import org.example.expensetracking.model.Otp;

import java.util.Date;
import java.util.UUID;

public interface OtpService {
    String generateOTP(int length);
    void saveOtp(Otp otp);
    void updateOtpVerification(String otpCode);
    Otp findOtpByCodeAndNotExpired(String otpCode, Date currentTime);
    UUID findUserIdByOtpCode(String otpCode, Date currentTime);
    boolean userVerified(UUID userId);
}
