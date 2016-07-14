package sg.ncl.common.jpa;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Christopher Zhong
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJpaRepositories
@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan
@Import(JpaAutoConfiguration.class)
public @interface UseJpa {

    /**
     * Type-safe alternative to {@link EnableJpaRepositories#basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
     * each package that serves no purpose other than being referenced by this attribute.
     */
    @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackageClasses")
    Class<?>[] jpaBasePackageClasses() default {};

    /**
     * Type-safe alternative to {@link EntityScan#basePackages()} for specifying the packages to
     * scan for annotated entities. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return classes from the base packages to scan
     */
    @AliasFor(annotation = EntityScan.class, attribute = "basePackageClasses")
    Class<?>[] entityBasePackageClasses() default {};

}
