import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import javax.swing.SwingUtilities;

import com.mycompany.wordtype.*;

import service.AuthResult;
import service.AuthService;

public class Main {

    // ไฟล์ฟอนต์ที่วางไว้ใน src/main/resources/fonts/
    private static final String[] FONT_FILES = {
        "RobotoMono-Regular.ttf",
        "RobotoMono-Medium.ttf",
        "RobotoMono-Bold.ttf",
        "SpaceGrotesk-Medium.ttf"
    };

    /** ลงทะเบียนฟอนต์จากในโปรเจกต์ ให้ new Font("ชื่อฟอนต์", ...) หาเจอ โดยไม่ต้องติดตั้งฟอนต์ในเครื่อง */
    private static void loadFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (String file : FONT_FILES) {
            try (InputStream in = Main.class.getResourceAsStream("/fonts/" + file)) {
                if (in == null) {
                    System.err.println("Font file not found: /fonts/" + file);
                    continue;
                }
                Font font = Font.createFont(Font.TRUETYPE_FONT, in);
                ge.registerFont(font);
                System.out.println("Loaded font: " + font.getFontName() + " (family: " + font.getFamily() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        private static void seedTestUsers() {
        String[][] accounts = {
            {"KuyKranuiTan", "1234"},
            {"user1", "1234"},
            {"user2", "1234"}
        };
        AuthService auth = new AuthService();
        for (String[] acc : accounts) {
            AuthResult r = auth.register(acc[0], acc[1]);
            System.out.println("[seed] " + acc[0] + ": " + (r.isSuccess() ? "created" : r.getMessage()));
        }
    }

    public static void main(String[] args) {
        loadFonts(); // ต้องเรียกก่อนสร้างหน้า login
        seedTestUsers();
        SwingUtilities.invokeLater(() -> {
            com.formdev.flatlaf.FlatLightLaf.setup(); // ตั้ง Look and Feel ครั้งเดียว ก่อนสร้างหน้าใด ๆ
            new Mainframe().setVisible(true);
        });
    }
}