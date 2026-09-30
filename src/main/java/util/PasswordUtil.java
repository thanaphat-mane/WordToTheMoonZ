package util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * คลาสเครื่องมือ (Utility) สำหรับเข้ารหัสผ่านและตรวจสอบรหัสผ่าน
 * <p>
 * ใช้อัลกอริทึม SHA-256 เพื่อไม่ให้ต้องเก็บรหัสผ่านจริงเป็น plain text
 * ลงในไฟล์ users.csv การเก็บเป็น hash หมายความว่าต่อให้ไฟล์ csv รั่วไหล
 * ก็ไม่สามารถอ่านรหัสผ่านจริงกลับมาได้โดยตรง
 */
public class PasswordUtil {

    /**
     * เข้ารหัสผ่านด้วยอัลกอริทึม SHA-256
     *
     * @param plainPassword รหัสผ่านต้นฉบับที่ผู้ใช้กรอกเข้ามา (plain text)
     * @return ค่า hash ของรหัสผ่านในรูปแบบ hexadecimal string (ความยาวคงที่ 64 ตัวอักษร)
     */
    public static String hash(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ตรวจสอบว่ารหัสผ่านที่ผู้ใช้กรอก ตรงกับค่า hash ที่เก็บไว้ในระบบหรือไม่
     * <p>
     * ทำงานโดย hash รหัสผ่านที่กรอกเข้ามาใหม่อีกครั้ง แล้วเทียบกับ hash ที่เก็บไว้
     * (ไม่มีการถอดรหัส hash กลับเป็น plain text เพราะ SHA-256 เป็น one-way hash)
     *
     * @param plainPassword  รหัสผ่านที่ผู้ใช้กรอกเข้ามาตอน login (plain text)
     * @param hashedPassword ค่า hash ที่เก็บไว้ในระบบ (เช่น จากไฟล์ users.csv)
     * @return true ถ้ารหัสผ่านตรงกัน, false ถ้าไม่ตรงกัน
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        return hash(plainPassword).equals(hashedPassword);
    }
}