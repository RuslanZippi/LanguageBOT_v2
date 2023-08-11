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
//    public void insertData() {
//        jdbcTemplate.execute("INSERT INTO user (id,user_id) VALUES(1, '111')");
//        //jdbcTemplate.execute("INSERT INTO public.user (id,user_id) VALUES(2, '222')");
//        //jdbcTemplate.execute("INSERT INTO public.user (id,user_id) VALUES(3, '333')");
//    }
}
