package com.github.iamhi.hizone.station.gateway.wellbeing;

import com.github.iamhi.hizone.station.core.wellbeing.WeightLogService;
import com.github.iamhi.hizone.station.core.wellbeing.dto.WeightLogDto;
import com.github.iamhi.hizone.station.gateway.wellbeing.responses.WeightLogResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ui/wellbeing/weightlog")
public record UIWeightLogController(
    WeightLogService weightLogService
) {

    @GetMapping
    public String index(Model model) {
        List<WeightLogResponse> weightLogs = weightLogService.getWeightLogs()
            .stream().map(WeightLogController::toResponse).toList();

        model.addAttribute("weightLogs", weightLogs);

        return "wellbeing/weightlog/index";
    }

    @GetMapping("/{uuid}")
    public String logDetails(Model model, @PathVariable String uuid) {
        Optional<WeightLogDto> optionalWeightLogDto = weightLogService.readWeightLog(uuid);

        if (optionalWeightLogDto.isPresent()) {
            WeightLogResponse weightLog = WeightLogController.toResponse(optionalWeightLogDto.get());

            model.addAttribute("weightLog", weightLog);

            return "wellbeing/weightlog/details";
        } else {
            return index(model);
        }
    }
}
