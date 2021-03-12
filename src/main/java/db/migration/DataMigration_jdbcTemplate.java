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

public class DataMigration_jdbcTemplate {
    public static void main(String[] args) throws Exception {
        String mysqlUrl = "jdbc:mysql://localhost:3306";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
        System.out.println("Connection Established...");
        ScriptRunner scriptRunner = new ScriptRunner(con);

        File sql_dir = new File("/home/localuser/IdeaProjects/services-in-one/src/main/resources/db/migration");
        File java_dir = new File("/home/localuser/IdeaProjects/services-in-one/src/main/java/db/migration");
        File[] sql_files = sql_dir.listFiles();
        File[] java_files = java_dir.listFiles();
//        if (sql_files != null){
//            for (File child : sql_files){
//                System.out.println("Import SQL Starting......");
//                System.out.println(child.getAbsolutePath());
//                Reader reader = new BufferedReader(new FileReader(child.getAbsolutePath()));
//                scriptRunner.runScript(reader);
//                System.out.println("Import SQL Finished......");
//            }
//            System.out.println("Import SQL All Finished......");
//        }
        if (java_files != null) {
            for (File child : java_files) {
                System.out.println("Run Java Starting......");
                System.out.println(child.getAbsoluteFile());
//                V1_4__change_content_format_to_text_email obj = new V1_4__change_content_format_to_text_email();
//                ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
//                JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//                obj.migrate(jdbcTemplate);
                System.out.println("Run Java Finished......");
            }
            System.out.println("Run Java All Finished......");
        }
    }
}

