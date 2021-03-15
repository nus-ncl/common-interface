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
        boolean IMPORT_SQL = true;
        boolean RUN_JAVA = true;
        String mysqlUrl = "jdbc:mysql://localhost:3306";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
        System.out.println("Connection Established...");
        ScriptRunner scriptRunner = new ScriptRunner(con);

        String sql_dir = "/home/hkwany/IdeaProjects/services-in-one/src/main/resources/db/migration/";
        String[] sql_files = {"V1_0__initial_schema.sql", "V1_2__create_tables_for_data_repo.sql",
                "V1_3__create_tables_for_image_repo.sql",
                "V1_5__add_table_for_password_reset_request.sql",
                "V1_8__add_table_for_data_downloads.sql",
                "V1_9__add_table_for_data_access_request.sql",
                "V1_10__modify_description_for_teams_table.sql",
                "V1_11__add_table_for_team_quotas.sql",
                "V1_12__create_tables_for_catalogue.sql",
                "V1_14__create_table_for_data_licenses.sql",
                "V1_16__alter_table_for_data_resources_scanning.sql",
                "V1_17__create_tables_for_public_user_downloads.sql",
                "V1_20__alter_table_for_teams.sql",
                "V1_21__create_tables_for_project_monthly_usage.sql",
                "V1_22__create_table_for_nodes_usage_reservations.sql",
                "V1_23__alter_table_for_project_usage.sql"};
        if(IMPORT_SQL) {
            for (int i = 1; i < sql_files.length; i++) {
                System.out.println("Import SQL Starting......");
                Reader reader = new BufferedReader(new FileReader(sql_dir + sql_files[i]));
                scriptRunner.runScript(reader);
                System.out.println("Import SQL Finished......");

            }
            System.out.println("Import SQL All Finished......");
        }
        if (RUN_JAVA) {
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

