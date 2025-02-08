package com.springcomics.api.service.auth;

import com.springcomics.api.domain.member.OtpToken;
import com.springcomics.api.domain.member.User;
import com.springcomics.api.exception.AlreadyExistsEmailException;
import com.springcomics.api.exception.InvalidOtp;
import com.springcomics.api.repository.OtpTokenRepository;
import com.springcomics.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    private static final int OTP_EXPIRATION_MINUTES = 3;

    @Transactional
    public void generateOtp(String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        otpTokenRepository.deleteByEmail(email);

        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpToken otpToken = OtpToken.create(email, otp);
        otpTokenRepository.save(otpToken);
        emailService.sendOtp(email, otp);
    }

    @Transactional
    public boolean verifyOtp(String email, String otp) {
        OtpToken otpToken = otpTokenRepository.findByEmail(email).orElseThrow(InvalidOtp::new);

        if (otpToken.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(OTP_EXPIRATION_MINUTES))) {
            return false;
        }

        otpToken.isVerifiedOtpUser();
        otpTokenRepository.save(otpToken);

        return otpToken.getOtp().equals(otp);

    }

}
