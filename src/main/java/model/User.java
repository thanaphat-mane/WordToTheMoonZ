package model;

public class User {
    private String username;
    private String hashedPassword;

    /**
     * สร้าง User object
     *
     * @param username        ชื่อผู้ใช้
     * @param hashedPassword  รหัสผ่านที่ผ่านการ hash แล้ว(ห้ามเป็น plain text)
     */
    public User(String username,String hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    /**
     *
     * @return ชื่อผู้ใช้ของ user นี้
     */
    public String getUsername(){
        return username;
    }


    /**
     *
     * @return รหัสผ่านที่ถูก hash แล้ว (ไม่ใช่รหัสผ่านจริงที่ผู้ใช้กรอก)
     */
    public String getHashedPassword(){
        return hashedPassword;
    }
}
