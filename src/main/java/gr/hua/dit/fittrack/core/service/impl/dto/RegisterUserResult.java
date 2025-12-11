package gr.hua.dit.fittrack.core.service.impl.dto;

public record RegisterUserResult(
        boolean created,
        String reason,
        UserView userView
) {

    public static RegisterUserResult success(UserView userView) {
        return new RegisterUserResult(true, null, userView);
    }

    public static RegisterUserResult fail(String reason) {
        return new RegisterUserResult(false, reason, null);
    }
}
