package util;

import java.io.*;
import java.util.*;

import repository.UserRepository;

/**
 * คลาสเครื่องมือ (Utility) สำหรับอ่านและเขียนไฟล์ CSV แบบทั่วไป
 * <p>
 * ไม่ผูกติดกับข้อมูลชนิดใดชนิดหนึ่งโดยเฉพาะ ทำงานกับ CSV แบบดิบ (String[] ต่อแถว)
 * เพื่อให้คลาสระดับ Repository (เช่น {@link UserRepository}) เรียกใช้ซ้ำได้
 * โดยไม่ต้องเขียนโค้ดอ่าน/เขียนไฟล์เองในแต่ละ Repository
 */
public class CSVUtil {
    /**
     * อ่านไฟล์ CSV ทั้งหมด
     *
     * @param filePath พาธของไฟล์ csv ที่ต้องการอ่าน
     * @return รายการของแต่ละแถว โดยแต่ละแถวถูกแยกคอลัมน์ด้วยเครื่องหมายจุลภาคเป็น String[]
     *         หากไฟล์ยังไม่มีอยู่จริง จะคืนค่าเป็น list ว่างแทนการโยน exception
     */

    public static List<String[]> readAll(String filePath) {
        List<String[]> rows = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists())
            return rows;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * เพิ่มแถวข้อมูลใหม่ต่อท้ายไฟล์ (append)
     * <p>
     * หากโฟลเดอร์ปลายทางยังไม่มีอยู่ จะถูกสร้างให้อัตโนมัติ
     *
     * @param filePath พาธของไฟล์ csv ที่ต้องการเขียน
     * @param values   ค่าของแต่ละคอลัมน์ในแถวที่จะเพิ่ม (จะถูกเชื่อมด้วยจุลภาค)
     */
    public static void appendRow(String filePath, String[] values) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(String.join(",", values));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * เขียนทับไฟล์ทั้งหมดด้วยข้อมูลชุดใหม่
     * <p>
     * ใช้ในกรณีที่ต้องการแก้ไข (update) หรือลบ (delete) บางแถวออกจากไฟล์เดิม
     * เพราะไฟล์ CSV ไม่รองรับการแก้ไขแถวเดียวโดยตรง
     *
     * @param filePath พาธของไฟล์ csv ที่ต้องการเขียนทับ
     * @param rows     ข้อมูลทั้งหมดที่จะเขียนแทนที่เนื้อหาเดิมของไฟล์
     */
    public static void writeAll(String filePath, List<String[]> rows) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
