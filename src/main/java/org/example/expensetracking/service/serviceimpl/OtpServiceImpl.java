package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.repository.OtpRespository;
import org.example.expensetracking.service.OtpService;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRespository otpRespository;

    public OtpServiceImpl(OtpRespository otpRespository) {
        this.otpRespository = otpRespository;
    }
}
