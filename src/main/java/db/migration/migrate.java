package db.migration;
import db.migration.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class migrate {
    public static void main(String[] args) throws Exception {
        V1_4__change_content_format_to_text_email obj = new V1_4__change_content_format_to_text_email();
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        obj.migrate(jdbcTemplate);
    }
}
