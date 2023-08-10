package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application  //implements CommandLineRunner
{
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void run(String... args) throws Exception {
//        String sql = "INSERT INTO book (id,title, author) VALUES ("
//                + "'1','Nam Ha Minh', 'nam@codejava.net')";
//
//        int rows = jdbcTemplate.update(sql);
//        if (rows > 0) {
//            System.out.println("A new row has been inserted.");
//        }
//    }
}
