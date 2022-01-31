package ua.com.alevel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import ua.com.alevel.elastic.document.ImageIndex;
import ua.com.alevel.properties.ImageProperties;
import ua.com.alevel.properties.MailProperties;

import javax.annotation.PreDestroy;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({
        MailProperties.class,
        ImageProperties.class
})
public class GameForumsApplication {
    private final ElasticsearchOperations elasticsearchOperations;

    public GameForumsApplication(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public static void main(String[] args) {
        SpringApplication.run(GameForumsApplication.class, args);
    }

    @PreDestroy
    public void resetElastic() {
        elasticsearchOperations.indexOps(ImageIndex.class).delete();
    }
}
