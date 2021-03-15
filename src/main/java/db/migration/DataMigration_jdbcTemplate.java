package db.migration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
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
        if (sql_files != null){
            for (File child : sql_files){
                System.out.println("Import SQL Starting......");
                System.out.println(child.getAbsolutePath());
                Reader reader = new BufferedReader(new FileReader(child.getAbsolutePath()));
                scriptRunner.runScript(reader);
                System.out.println("Import SQL Finished......");
            }
            System.out.println("Import SQL All Finished......");
        }
        if (java_files != null) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
            JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
            System.out.println("Run Java Starting......");
            V1_1__initial_data obj0 = new V1_1__initial_data();
            obj0.migrate(jdbcTemplate);

            V1_4__change_content_format_to_text_email obj1 = new V1_4__change_content_format_to_text_email();
            obj1.migrate(jdbcTemplate);

            V1_6__change_email_error_message_to_text obj2 = new V1_6__change_email_error_message_to_text();
            obj2.migrate(jdbcTemplate);

            V1_7__add_current_os_to_images_table obj3 = new V1_7__add_current_os_to_images_table();
            obj3.migrate(jdbcTemplate);

            V1_13__set_category_for_existing_datasets obj4 = new V1_13__set_category_for_existing_datasets();
            obj4.migrate(jdbcTemplate);

            V1_15__insert_data_licenses obj5 = new V1_15__insert_data_licenses();
            obj5.migrate(jdbcTemplate);

            V1_18__change_data_license_link obj6 = new V1_18__change_data_license_link();
            obj6.migrate(jdbcTemplate);

            V1_19__change_experiment_max_duration obj7 = new V1_19__change_experiment_max_duration();
            obj7.migrate(jdbcTemplate);

            System.out.println("Run Java Finished......");

            System.out.println("Run Java All Finished......");
        }
    }
}

