package com.github.iamhi.hizone.station.gateway.wellbeing.requests;

import org.springframework.web.multipart.MultipartFile;

public record CreateWeightLogRequest(
    String description,
    MultipartFile image
) {
}
