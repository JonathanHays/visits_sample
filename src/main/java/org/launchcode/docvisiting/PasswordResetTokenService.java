package org.launchcode.docvisiting;

import org.launchcode.docvisiting.data.PasswordResetTokenRepository;
import org.launchcode.docvisiting.models.PasswordResetToken;
import org.launchcode.docvisiting.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    private static final int TOKEN_LENGTH = 32;

    public PasswordResetToken createToken(User user, String token, int expirationInMinutes) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(calculateExpiryDate(expirationInMinutes));
        return tokenRepository.save(passwordResetToken);
    }

    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        // Get the current date and time
        Calendar cal = Calendar.getInstance();
        // Set the time of the calendar to the current timestamp
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        // Add the specified number of minutes to the current timestamp
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        // Convert the calendar time to a Date object
        Date expiryDate = new Date(cal.getTime().getTime());

        return expiryDate;
    }

    public void deleteToken(PasswordResetToken resetToken) {
        tokenRepository.delete(resetToken);
    }

    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public boolean isTokenUnique(String tokenToCheck) {
        PasswordResetToken existingToken = tokenRepository.findByToken(tokenToCheck);
        return existingToken == null;
    }

    public String generateUniqueToken() {
        String token;
        do {
            token = generateToken();
        } while (!isTokenUnique(token));
        return token;
    }


}
