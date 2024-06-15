package com.github.iamhi.hizone.station.core.wellbeing;

import com.github.iamhi.hizone.station.core.wellbeing.dto.WeightLogDto;

import java.util.List;
import java.util.Optional;

public interface WeightLogService {

    String APPLICATION_NAME = "wellbeing/weightlog";

    String FILE_PREFIX = "weight_log";

    List<WeightLogDto> getWeightLogs();

    WeightLogDto createWeightLog(String description, byte[] imageContent, String originalFileName);

    Optional<WeightLogDto> readWeightLog(String uuid);

    Optional<WeightLogDto> deleteWeightLog(String uuid);

    byte[] getImageWeightLog(String uuid);
}
