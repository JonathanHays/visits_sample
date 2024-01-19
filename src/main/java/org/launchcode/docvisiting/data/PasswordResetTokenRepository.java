package org.launchcode.docvisiting.data;

import org.launchcode.docvisiting.models.PasswordResetToken;
import org.launchcode.docvisiting.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByExpiryDateLessThan(Date now);

    PasswordResetToken findByUser(User user);
}
