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
import ru.evlitvin.entity.Pupil;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(PupilRepositoryTest.Config.class)
@ComponentScan(basePackages = "ru.evlitvin.repository")
class PupilRepositoryTest {

    @Autowired
    private PupilRepository pupilRepository;

    @BeforeEach
    public void setUp() {
        pupilRepository.deleteAll();
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
        PupilRepository pupilRepository(EntityManagerFactory emf) {
            return emf.unwrap(PupilRepository.class);
        }
    }


    @Test
    void givenCorrectPupil_whenSavePupil_thenSavePupilCorrect() {
        Pupil pupil = new Pupil();
        pupil.setFirstName("Petr");
        pupil.setLastName("Petrov");

        pupilRepository.save(pupil);

        Pupil foundPupil = pupilRepository.findById(pupil.getId()).orElseThrow();

        assertNotNull(foundPupil);
        assertEquals("Petr", foundPupil.getFirstName());
        assertEquals("Petrov", foundPupil.getLastName());
    }

    @Test
    void givenExistingPupils_whenGetPupilById_thenReturnPupil() {
        Pupil pupilOne = new Pupil();
        pupilOne.setFirstName("Petr");
        pupilOne.setLastName("Petrov");

        Pupil pupilTwo = new Pupil();
        pupilTwo.setFirstName("Maria");
        pupilTwo.setLastName("Kuznecova");

        pupilRepository.save(pupilOne);
        pupilRepository.save(pupilTwo);

        List<Pupil> foundPupils = pupilRepository.findAll();

        assertEquals("Petr", foundPupils.get(0).getFirstName());
        assertEquals("Kuznecova", foundPupils.get(1).getLastName());
    }

    @Test
    void givenExistingPupils_whenGetAllPupils_thenReturnListOfPupils() {
        Pupil pupilOne = new Pupil();
        pupilOne.setFirstName("Petr");
        pupilOne.setLastName("Petrov");

        Pupil pupilTwo = new Pupil();
        pupilTwo.setFirstName("Maria");
        pupilTwo.setLastName("Kuznecova");

        pupilRepository.save(pupilOne);
        pupilRepository.save(pupilTwo);

        List<Pupil> foundPupils = pupilRepository.findAll();

        assertNotNull(foundPupils);
        assertEquals(2, foundPupils.size());
    }

    @Test
    void givenPupil_whenDeletePupil_thenDeletePupilCorrect() {
        Pupil pupilOne = new Pupil();
        pupilOne.setFirstName("Petr");
        pupilOne.setLastName("Petrov");

        Pupil pupilTwo = new Pupil();
        pupilTwo.setFirstName("Maria");
        pupilTwo.setLastName("Kuznecova");

        pupilRepository.save(pupilOne);
        pupilRepository.save(pupilTwo);

        List<Pupil> pupils = pupilRepository.findAll();
        assertNotNull(pupils);
        assertEquals(2, pupils.size());

        pupilRepository.deleteById(pupilOne.getId());

        List<Pupil> pupilsAfterDelete = pupilRepository.findAll();
        assertEquals(1, pupilsAfterDelete.size());
    }

    @Test
    void givenPupil_whenUpdatePupil_thenUpdatePupilCorrect() {
        Pupil pupilOne = new Pupil();
        pupilOne.setFirstName("Petr");
        pupilOne.setLastName("Petrov");

        pupilRepository.save(pupilOne);

        Pupil foundPupil = pupilRepository.findById(pupilOne.getId()).orElse(null);
        foundPupil.setFirstName("Mark");

        pupilRepository.save(foundPupil);

        assertEquals("Mark", pupilRepository.findById(pupilOne.getId()).get().getFirstName());
    }
}