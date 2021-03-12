package db.migration;
import db.migration.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;

public class migrate {
    public static void main(String[] args) throws Exception {
        String mysqlUrl="jdbc:mysql://localhost:3306";
        Connection con = DriverManager.getConnection(mysqlUrl,"root","root");
        System.out.println("Connection Established...");
        ScriptRunner scriptRunner = new ScriptRunner(con);

        File dir = new File("/home/localuser/IdeaProjects/services-in-one/src/main/resources/db/migration");
        File[] files = dir.listFiles();
        if (files != null){
            for (File child : files){
                System.out.println("Starting......");
                System.out.println(child.getAbsolutePath());
                Reader reader = new BufferedReader(new FileReader(child.getAbsolutePath()));
                scriptRunner.runScript(reader);
                System.out.println("Finished......");
            }
            System.out.println("All Finished......");
        }
        
//        V1_4__change_content_format_to_text_email obj = new V1_4__change_content_format_to_text_email();
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
//        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//        obj.migrate(jdbcTemplate);
    }
}
