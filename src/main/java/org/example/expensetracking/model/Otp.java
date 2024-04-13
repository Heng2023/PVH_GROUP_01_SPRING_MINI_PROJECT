package org.example.expensetracking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    private UUID otpId;
    private String otpCode;
    private Date issuedAt;
    private Date expiration;
    private String verify;
    private UUID userId;
}
