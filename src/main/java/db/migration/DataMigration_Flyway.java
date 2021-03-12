package db.migration;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class DataMigration_Flyway {
    public void main(String[] args) throws Exception {
        V1_1__initial_data initialData = new V1_1__initial_data();
        JdbcTemplate database = new JdbcTemplate(createDataSource());
        Flyway flyway = new Flyway();
        flyway.migrate();
        initialData.migrate(database);

//        String mysqlUrl = "jdbc:mysql://localhost:3306";
//        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
//        System.out.println("Connection Established...");
//        ScriptRunner scriptRunner = new ScriptRunner(con);
//
//        File dir = new File("/home/localuser/IdeaProjects/services-in-one/src/main/resources/db/migration");
//        File[] files = dir.listFiles();
//        if (files != null) {
//            for (File child : files) {
//                System.out.println("Starting......");
//                System.out.println(child.getAbsolutePath());
//                Reader reader = new BufferedReader(new FileReader(child.getAbsolutePath()));
//                scriptRunner.runScript(reader);
//                System.out.println("Finished......");
//            }
//            System.out.println("All Finished......");
//        }

//        V1_4__change_content_format_to_text_email obj = new V1_4__change_content_format_to_text_email();
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
//        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//        obj.migrate(jdbcTemplate);
    }

    private SimpleDriverDataSource createDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setPassword("root");
        return simpleDriverDataSource;
    }
}
