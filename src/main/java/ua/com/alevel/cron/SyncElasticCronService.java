package ua.com.alevel.cron;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.alevel.elastic.document.ImageIndex;
import ua.com.alevel.elastic.repository.ImageIndexRepository;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.repository.image.ImageRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SyncElasticCronService {
    //todo fix this in imageService
    private final ElasticsearchOperations elasticsearchOperations;
    private final ImageIndexRepository imageIndexRepository;
    private final ImageRepository imageRepository;

    public SyncElasticCronService(ElasticsearchOperations elasticsearchOperations, ImageIndexRepository imageIndexRepository, ImageRepository imageRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.imageIndexRepository = imageIndexRepository;
        this.imageRepository = imageRepository;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void sycToSupplier() {
        elasticsearchOperations.indexOps(ImageIndex.class).refresh();
        imageIndexRepository.deleteAll();
        imageIndexRepository.saveAll(prepareDataset());
    }

    private Collection<ImageIndex> prepareDataset() {
        List<Image> images = imageRepository.findAll();
        List<ImageIndex> imageIndices = new ArrayList<>();
        images.forEach(image -> {
            ImageIndex imageIndex = new ImageIndex();
            imageIndex.setNameGame(image.getNameGame());
            imageIndices.add(imageIndex);
        });
        return imageIndices;
    }
}
