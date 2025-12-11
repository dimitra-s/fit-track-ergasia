package gr.hua.dit.fittrack.core.service.impl.dto;

public record LoginResult(
        boolean success,
        String reason,      // μήνυμα λάθους αν αποτύχει
        String accessToken, // το JWT
        String tokenType,   // π.χ. "Bearer"
        long expiresIn,     // σε δευτερόλεπτα π.χ. 3600
        String role,        // USER ή TRAINER
        Long userId
) {

    public static LoginResult success(String accessToken,
                                      long expiresIn,
                                      String role,
                                      Long userId) {
        return new LoginResult(true, null, accessToken, "Bearer", expiresIn, role, userId);
    }

    public static LoginResult fail(String reason) {
        return new LoginResult(false, reason, null, null, 0, null, null);
    }
}
