package org.example.expensetracking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    private UUID otpId;
    private int otpCode;
    private Timestamp issuedAt;
    private Duration expiration;
    private boolean verify;
    private UUID userId;
}
