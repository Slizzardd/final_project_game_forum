package ua.com.alevel.config.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ua.com.alevel.properties.MailProperties;

import java.util.Properties;

@ComponentScan("ua.com.alevel.config")
@Configuration
public class MailConfig {

//    @Value("${mail.name}")
//    private String ourMailName;
//
//    @Value("${mail.password}")
//    private String ourMailPassword;

    private final MailProperties mailProperties;

    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public MailSender mailConfiguration() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHostName());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getName());
        mailSender.setPassword(mailProperties.getPassword());

        Properties properties = new Properties();

        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
