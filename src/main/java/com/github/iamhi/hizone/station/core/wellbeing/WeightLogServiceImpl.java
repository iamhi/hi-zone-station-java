package com.github.iamhi.hizone.station.core.wellbeing;

import com.github.iamhi.hizone.station.core.user.MemberCache;
import com.github.iamhi.hizone.station.core.utils.FileService;
import com.github.iamhi.hizone.station.core.wellbeing.dto.WeightLogDto;
import com.github.iamhi.hizone.station.data.wellbeing.WeightLogEntity;
import com.github.iamhi.hizone.station.data.wellbeing.WeightLogRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public record WeightLogServiceImpl(
    MemberCache memberCache,
    WeightLogRepository repository,
    FileService fileService
) implements WeightLogService {

    @Override
    public List<WeightLogDto> getWeightLogs() {
        return repository.findByOwnerUuidOrderByCreatedAtDesc(memberCache.getUuid())
            .stream().map(this::fromEntity).toList();
    }

    @Override
    public WeightLogDto createWeightLog(String description, byte[] imageContent, String originalFileName) {
        WeightLogEntity weightLogEntity = generateEntity();
        String fileExtension = FilenameUtils.getExtension(originalFileName);

        weightLogEntity.setOwnerUuid(memberCache.getUuid());
        weightLogEntity.setDescription(description);

        if (imageContent != null && imageContent.length > 0) {
            String imagePath = fileService.writeFile(
                FILE_PREFIX + UUID.randomUUID() + "." + fileExtension, APPLICATION_NAME,
                true,
                imageContent);

            weightLogEntity.setImagePath(imagePath);
        }

        return fromEntity(repository.save(weightLogEntity));
    }

    @Override
    public Optional<WeightLogDto> readWeightLog(String uuid) {
        return repository.findByUuidAndOwnerUuid(uuid, memberCache.getUuid()).map(this::fromEntity);
    }

    @Override
    public Optional<WeightLogDto> deleteWeightLog(String uuid) {
        Optional<WeightLogEntity> optionalWeightLogEntity = repository.findByUuidAndOwnerUuid(uuid, memberCache.getUuid());

        if (optionalWeightLogEntity.isPresent()) {
            repository.delete(optionalWeightLogEntity.get());

            return optionalWeightLogEntity.map(this::fromEntity);
        }

        return Optional.empty();
    }

    @Override
    public byte[] getImageWeightLog(String uuid) {
        return
            repository.findByUuidAndOwnerUuid(uuid, memberCache.getUuid())
                .map(weightLog -> fileService.readFileContent(weightLog.getImagePath(), APPLICATION_NAME, true))
                .orElse(new byte[0]);
    }

    private WeightLogEntity generateEntity() {
        WeightLogEntity entity = new WeightLogEntity();

        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        return entity;
    }

    private WeightLogDto fromEntity(WeightLogEntity entity) {
        return new WeightLogDto(
            entity.getUuid(),
            entity.getOwnerUuid(),
            entity.getDescription(),
            StringUtils.isNotBlank(entity.getImagePath()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
