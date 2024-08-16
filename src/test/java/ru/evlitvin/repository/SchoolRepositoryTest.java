package ru.evlitvin.repository;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import ru.evlitvin.entity.School;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(SchoolRepositoryTest.Config.class)
@ComponentScan(basePackages = "ru.evlitvin.repository")
class SchoolRepositoryTest {

    @Autowired
    private SchoolRepository schoolRepository;

    @BeforeEach
    public void setUp() {
        schoolRepository.deleteAll();
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "ru.evlitvin.repository")
    @PropertySource("classpath:test.properties")
    static class Config {

        private final Environment env;

        Config(Environment env) {
            this.env = env;
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driver")));
            dataSource.setUrl(env.getProperty("jdbc.url"));
            dataSource.setUsername(env.getProperty("jdbc.username"));
            dataSource.setPassword(env.getProperty("jdbc.password"));
            return dataSource;
        }

        @Bean
        public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            em.setPackagesToScan("ru.evlitvin");
            em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            em.setJpaProperties(additionalProperties());
            em.afterPropertiesSet();
            return em.getObject();
        }

        private Properties additionalProperties() {
            Properties properties = new Properties();
            properties.put("hibernate.dialect", env.getProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect"));
            properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql", "true"));
            properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql", "true"));
            properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "update"));
            return properties;
        }

        @Bean
        public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(emf);
            return transactionManager;
        }

        @Bean
        SchoolRepository schoolRepository(EntityManagerFactory emf) {
            return emf.unwrap(SchoolRepository.class);
        }
    }


    @Test
    void givenCorrectSchool_whenSaveSchool_thenSaveSchoolCorrect() {
        School school = new School();
        school.setSchoolName("School # 1");
        school.setAddress("School #1 address");

        schoolRepository.save(school);

        School foundSchool = schoolRepository.findById(school.getId()).orElseThrow();

        assertNotNull(foundSchool);
        assertEquals("School # 1", foundSchool.getSchoolName());
        assertEquals("School #1 address", foundSchool.getAddress());
    }

    @Test
    void givenExistingSchools_whenGetSchoolById_thenReturnSchool() {
        School schoolOne = new School();
        schoolOne.setSchoolName("School # 1");
        schoolOne.setAddress("School #1 address");

        School schoolTwo = new School();
        schoolTwo.setSchoolName("School # 2");
        schoolTwo.setAddress("School # 2 address");

        schoolRepository.save(schoolOne);
        schoolRepository.save(schoolTwo);

        List<School> foundSchools = schoolRepository.findAll();

        assertEquals("School # 1", foundSchools.get(0).getSchoolName());
        assertEquals("School # 2 address", foundSchools.get(1).getAddress());
    }

    @Test
    void givenExistingSchools_whenGetAllSchools_thenReturnListOfSchools() {

        School schoolOne = new School();
        schoolOne.setSchoolName("School # 1");
        schoolOne.setAddress("School # 1 address");

        School schoolTwo = new School();
        schoolTwo.setSchoolName("School # 2");
        schoolTwo.setAddress("School # 2 address");

        schoolRepository.save(schoolOne);
        schoolRepository.save(schoolTwo);

        List<School> schools = schoolRepository.findAll();

        assertNotNull(schools);
        assertEquals(2, schools.size());
    }

    @Test
    void givenSchool_whenDeleteSchool_thenDeleteSchoolCorrect() {
        School schoolOne = new School();
        schoolOne.setSchoolName("School # 1");
        schoolOne.setAddress("School # 1 address");

        School schoolTwo = new School();
        schoolTwo.setSchoolName("School # 2");
        schoolTwo.setAddress("School # 2 address");

        schoolRepository.save(schoolOne);
        schoolRepository.save(schoolTwo);

        List<School> schools = schoolRepository.findAll();
        assertNotNull(schools);
        assertEquals(2, schools.size());

        schoolRepository.deleteById(schoolOne.getId());

        List<School> schoolsAfterDelete = schoolRepository.findAll();
        assertEquals(1, schoolsAfterDelete.size());
    }

    @Test
    void givenSchool_whenUpdateSchool_thenUpdateSchoolCorrect() {
        School schoolOne = new School();
        schoolOne.setSchoolName("School # 1");
        schoolOne.setAddress("School # 1 address");
        schoolRepository.save(schoolOne);

        School foundSchool = schoolRepository.findById(schoolOne.getId()).orElse(null);
        foundSchool.setAddress("School # 1 another address");
        schoolRepository.save(foundSchool);

        assertEquals("School # 1 another address", schoolRepository.findById(schoolOne.getId()).get().getAddress());
    }
}