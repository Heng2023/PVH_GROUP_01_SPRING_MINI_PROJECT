package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    public String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
