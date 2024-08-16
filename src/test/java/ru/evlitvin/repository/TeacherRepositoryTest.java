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
import ru.evlitvin.entity.Teacher;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TeacherRepositoryTest.Config.class)
@ComponentScan(basePackages = "ru.evlitvin.repository")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void setUp() {
        teacherRepository.deleteAll();
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
        TeacherRepository teacherRepository(EntityManagerFactory emf) {
            return emf.unwrap(TeacherRepository.class);
        }
    }


    @Test
    void givenCorrectTeacher_whenSaveTeacher_thenSaveTeacherCorrect() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");

        teacherRepository.save(teacher);

        Teacher foundTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();

        assertNotNull(foundTeacher);
        assertEquals("Ivan", foundTeacher.getFirstName());
        assertEquals("Ivanov", foundTeacher.getLastName());
    }

    @Test
    void givenExistingTeachers_whenGetTeacherById_thenReturnTeacher() {
        Teacher teacherOne = new Teacher();
        teacherOne.setFirstName("Ivan");
        teacherOne.setLastName("Ivanov");

        Teacher teacherTwo = new Teacher();
        teacherTwo.setFirstName("Petr");
        teacherTwo.setLastName("Petrov");

        teacherRepository.save(teacherOne);
        teacherRepository.save(teacherTwo);

        List<Teacher> foundTeachers = teacherRepository.findAll();

        assertEquals("Ivan", foundTeachers.get(0).getFirstName());
        assertEquals("Petrov", foundTeachers.get(1).getLastName());
    }

    @Test
    void givenExistingTeachers_whenGetAllTeachers_thenReturnListOfTeachers() {
        Teacher teacherOne = new Teacher();
        teacherOne.setFirstName("Ivan");
        teacherOne.setLastName("Ivanov");

        Teacher teacherTwo = new Teacher();
        teacherTwo.setFirstName("Petr");
        teacherTwo.setLastName("Petrov");

        teacherRepository.save(teacherOne);
        teacherRepository.save(teacherTwo);

        List<Teacher> teachers = teacherRepository.findAll();

        assertNotNull(teachers);
        assertEquals(2, teachers.size());
    }

    @Test
    void givenTeacher_whenDeleteTeacher_thenDeleteTeacherCorrect() {
        Teacher teacherOne = new Teacher();
        teacherOne.setFirstName("Ivan");
        teacherOne.setLastName("Ivanov");

        Teacher teacherTwo = new Teacher();
        teacherTwo.setFirstName("Petr");
        teacherTwo.setLastName("Petrov");

        teacherRepository.save(teacherOne);
        teacherRepository.save(teacherTwo);

        List<Teacher> teachers = teacherRepository.findAll();
        assertNotNull(teachers);
        assertEquals(2, teachers.size());

        teacherRepository.deleteById(teacherOne.getId());

        List<Teacher> teachersAfterDelete = teacherRepository.findAll();
        assertEquals(1, teachersAfterDelete.size());
    }

    @Test
    void givenTeacher_whenUpdateTeacher_thenUpdateTeacherCorrect() {
        Teacher teacherOne = new Teacher();
        teacherOne.setFirstName("Ivan");
        teacherOne.setLastName("Ivanov");

        teacherRepository.save(teacherOne);

        Teacher foundTeacher = teacherRepository.findById(teacherOne.getId()).orElse(null);
        foundTeacher.setLastName("Petrov");
        teacherRepository.save(foundTeacher);

        assertEquals("Petrov", teacherRepository.findById(teacherOne.getId()).get().getLastName());
    }
}