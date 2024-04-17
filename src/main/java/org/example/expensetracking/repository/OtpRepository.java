package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.*;
import org.example.expensetracking.model.Otp;

import java.util.Date;
import java.util.UUID;

@Mapper
public interface OtpRepository {
    @Insert("""
        INSERT INTO otps (otp_code, issued_at, expiration, user_id) 
        VALUES (#{otpCode}, #{issuedAt}, #{expiration}, #{userId, jdbcType=OTHER}) 
    """)
    void saveOtp(Otp otp);

    @Update("""
        UPDATE otps 
        SET verify = true 
        WHERE otp_code = #{otpCode}
    """)
    void updateOtpVerification(@Param("otpCode") String otpCode);

    @Select("""
        SELECT * 
        FROM otps 
        WHERE otp_code = #{otpCode} 
        AND expiration > #{currentTime}
    """)
    Otp findOtpByCodeAndNotExpired(@Param("otpCode") String otpCode, @Param("currentTime") Date currentTime);
}
