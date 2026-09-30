package service;

import model.User;
import repository.UserRepository;
import util.PasswordUtil;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * สมัครสมาชิกใหม่ พร้อมตรวจสอบความถูกต้องของข้อมูลก่อนบันทึก
     *
     * @param username ชื่อผู้ใช้ที่ต้องการสมัคร
     * @param password รหัสผ่าน (plain text) ที่ผู้ใช้กรอก
     * @return null ถ้าสมัครสมาชิกสำเร็จ, หรือข้อความ error ถ้าสมัครไม่สำเร็จ เช่น
     *         username ซ้ำ หรือรหัสผ่านสั้นเกินไป
     *
     */
    public AuthResult register(String username, String password) {
        if (username == null || username.trim().isEmpty())
            return AuthResult.fail("Please enter a username");
        if (password == null || password.trim().isEmpty())
            return AuthResult.fail("Please enter a password");

        username = username.trim();
        if (username.contains(","))
            return AuthResult.fail("Username cannot contain ','"); // กัน CSV เพี้ยน
        if (userRepository.existsByUsername(username))
            return AuthResult.fail("Username already exists");

        User user = new User(username, PasswordUtil.hash(password));
        userRepository.save(user);
        return AuthResult.success(user);
    }

    /**
     * ตรวจสอบการเข้าสู่ระบบ (ยืนยันตัวตน)
     * <p>
     * ค้นหาผู้ใช้จาก username แล้วเปรียบเทียบรหัสผ่านที่กรอกกับ hash ที่เก็บไว้
     * ผ่าน {@link PasswordUtil#verify(String, String)}
     *
     * @param username ชื่อผู้ใช้ที่กรอกตอน login
     * @param password รหัสผ่าน (plain text) ที่กรอกตอน login
     * @return {@link AuthResult} สำเร็จ (มี User) หรือล้มเหลวพร้อมข้อความ
     *         (ข้อความเดียวกันทั้งกรณีไม่มี username และรหัสผ่านผิด
     *         เพื่อไม่บอกใบ้ว่า username ไหนมีอยู่จริง)
     */
    public AuthResult login(String username, String password) {
        if (username == null || username.trim().isEmpty())
            return AuthResult.fail("Please enter a username");
        if (password == null || password.isEmpty())
            return AuthResult.fail("Please enter a password");

        User user = userRepository.FindByUsername(username.trim());
        if (user == null || !PasswordUtil.verify(password, user.getHashedPassword())) {
            return AuthResult.fail("Invalid username or password");
        }
        return AuthResult.success(user);
    }
}
