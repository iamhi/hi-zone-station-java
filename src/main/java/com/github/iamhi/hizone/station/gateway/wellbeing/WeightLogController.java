package com.github.iamhi.hizone.station.gateway.wellbeing;

import com.github.iamhi.hizone.station.core.wellbeing.WeightLogService;
import com.github.iamhi.hizone.station.core.wellbeing.dto.WeightLogDto;
import com.github.iamhi.hizone.station.gateway.wellbeing.requests.CreateWeightLogRequest;
import com.github.iamhi.hizone.station.gateway.wellbeing.responses.WeightLogResponse;
import jdk.jfr.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/wellbeing/weightlog")
@RestController
public record WeightLogController(
    WeightLogService weightLogService
) {

    static WeightLogResponse toResponse(WeightLogDto dto) {
        return new WeightLogResponse(
            dto.uuid(),
            dto.description(),
            getImageUrl(dto.uuid()),
            dto.createdAt(),
            dto.createdAt()
        );
    }

    private static String getImageUrl(String uuid) {
        return "/hi-zone-api/station/wellbeing/weightlog/" + uuid + "/image";
    }

    @GetMapping
    public List<WeightLogResponse> getWeightLogs() {
        return weightLogService.getWeightLogs()
            .stream().map(WeightLogController::toResponse)
            .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Optional<WeightLogResponse> addWeightLog(
        @ModelAttribute CreateWeightLogRequest request
    ) {
        try {
            return Optional.of(
                toResponse(weightLogService.createWeightLog(
                    request.description(),
                    request.image().getBytes(),
                    request.image().getOriginalFilename())));
        } catch (IOException e) {
            return Optional.of(
                toResponse(weightLogService.createWeightLog(
                    request.description(),
                    null,
                    StringUtils.EMPTY)));
        }
    }

    @DeleteMapping("/{uuid}")
    public Optional<WeightLogResponse> removeWeightLog(@PathVariable String uuid) {
        return weightLogService.deleteWeightLog(uuid).map(WeightLogController::toResponse);
    }

    @GetMapping(value = "/{uuid}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] readImageWeightLog(@PathVariable String uuid) {
        return weightLogService.getImageWeightLog(uuid);
    }
}
