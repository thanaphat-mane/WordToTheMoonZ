package repository;
import java.util.*;

import model.User;
import util.CSVUtil;

public class UserRepository {

    private static final String FILE_PATH = "./src/main/java/data/users.csv";

    public boolean existsByUsername(String username){
        return FindByUsername(username) != null;
    }
    
    public User FindByUsername(String username){
        List<String[]> rows = CSVUtil.readAll(FILE_PATH);
        for (String[] row : rows) {
            if(row.length >= 2 && row[0].equals(username)){
                return new User(row[0], row[1]);
            }
        }
        return null;
    }

    public void save(User user){
        CSVUtil.appendRow(FILE_PATH,new String[]{user.getUsername() , user.getHashedPassword()});
    }
}