package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.Otp;
import org.example.expensetracking.repository.OtpRepository;
import org.example.expensetracking.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private OtpRepository otpRepository;
    public String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Override
    public void saveOtp(Otp otp) {
        otpRepository.saveOtp(otp);
    }

    @Override
    public void updateOtpVerification(String otpCode) {
        otpRepository.updateOtpVerification(otpCode);
    }

    @Override
    public Otp findOtpByCodeAndNotExpired(String otpCode, Date currentTime) {
        return otpRepository.findOtpByCodeAndNotExpired(otpCode, currentTime);
    }

    @Override
    public UUID findUserIdByOtpCode(String otpCode, Date currentTime) {
        return otpRepository.findUserIdByOtpCode(otpCode, currentTime);
    }

    @Override
    public boolean userVerified(UUID userId) {
        return otpRepository.userVerified(userId);
    }
}
