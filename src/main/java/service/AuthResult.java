package service;

import model.*;

public class AuthResult {
    private final User user;
    private final String message;

    private AuthResult(User user, String message) {
        this.user = user;
        this.message = message;
    }


    public static AuthResult success(User user) {
        return new AuthResult(user, null);
    }

    public static AuthResult fail(String message) {
        return new AuthResult(null, message);
    }

    /**
     *
     * @return true ถ้าสำเร็จ
     */
    public boolean isSuccess() {
        return user != null;
    }

    /**
     *
     * @return user ที่ login/สมัครสำเร็จ หรือ null ถ้าล้มเหลว
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @return ข้อความสาเหตุที่ล้มเหลว หรือ null ถ้าสำเร็จ
     */
    public String getMessage() {
        return message;
    }
}
