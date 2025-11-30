package com.muratcan.yeldan.couriertrackingapplication.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muratcan.yeldan.couriertrackingapplication.constant.Constants;
import com.muratcan.yeldan.couriertrackingapplication.entity.Store;
import com.muratcan.yeldan.couriertrackingapplication.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreDataLoader {

    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("stores.json");

            List<Store> jsonStores = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );

            Set<String> jsonNames = jsonStores.stream()
                    .map(Store::getName)
                    .collect(Collectors.toSet());

            List<Store> existingStores = storeRepository.findAll();

            List<Store> toDeleteList = existingStores.stream()
                    .filter(store -> !jsonNames.contains(store.getName()))
                    .toList();

            if (!toDeleteList.isEmpty()) {
                String[] namesToDelete = toDeleteList.stream()
                        .map(Store::getName)
                        .toArray(String[]::new);

                redisTemplate.opsForGeo().remove(Constants.STORES_GEO_KEY, namesToDelete);

                storeRepository.deleteAll(toDeleteList);

                log.info("Deleted {} stores from DB and Redis", toDeleteList.size());
            }

            storeRepository.saveAll(jsonStores);
            log.info("Upserted {} stores to DB", jsonStores.size());

            for (Store store : jsonStores) {
                redisTemplate.opsForGeo().add(
                        Constants.STORES_GEO_KEY,
                        new Point(store.getLng(), store.getLat()),
                        store.getName()
                );
            }

            log.info("Synced {} stores to Redis GEO index", jsonStores.size());

        } catch (Exception e) {
            log.error("Failed to load/sync stores", e);
            throw new IllegalStateException("Application cannot start without stores", e);
        }
    }
}
