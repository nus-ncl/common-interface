package sg.ncl.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Christopher Zhong
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(sg.ncl.service.user.App.class, args);
//        UserCredentialsRepository repository = context.getBean(UserCredentialsRepository.class);
//        UserCredentialsEntity credentials = new UserCredentialsEntity();
//        credentials.setUsername("abc");
//        credentials.setPassword("def");
//        final UserEntity user = new UserEntity();
//        user.setApplicationDate(ZonedDateTime.now());
//        credentials.setUser(user);
//        final UserDetailsEntity personalDetails = new UserDetailsEntity();
//        personalDetails.setFirstName("first");
//        personalDetails.setLastName("last");
//        personalDetails.setEmail("email");
//        personalDetails.setPhone("phone");
//        user.setUserDetails(personalDetails);
//        final AddressEntity addressEntity = new AddressEntity();
//        personalDetails.setAddress(addressEntity);
//        addressEntity.setAddress1("address1");
//        addressEntity.setCountry("country");
//        addressEntity.setZipCode("zipcode");
//        repository.save(credentials);
//        final Iterable<UserCredentialsEntity> all = repository.findAll();
//        for (UserCredentialsEntity c : all) {
//            System.out.println("all: " + c);
//        }
//        credentials = repository.findByUsername("abc");
//        System.out.println("findByUsername: " + credentials);

//        final UserCredentialsEntity abc = repository.findByUsername("abc");
//        System.out.println(">>>: " + abc);
//        final UserCredentialsEntity def = repository.findByUsername("def");
//        System.out.println(">>>: " +def);

    }

}
