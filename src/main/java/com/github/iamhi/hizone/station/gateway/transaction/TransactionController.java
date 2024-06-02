package com.github.iamhi.hizone.station.gateway.transaction;

import com.github.iamhi.hizone.station.core.transaction.TransactionDto;
import com.github.iamhi.hizone.station.core.transaction.TransactionSearchBundle;
import com.github.iamhi.hizone.station.core.transaction.TransactionService;
import com.github.iamhi.hizone.station.gateway.transaction.requests.TransactionCreateRequest;
import com.github.iamhi.hizone.station.gateway.transaction.responses.TransactionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/transaction")
@RestController
public record TransactionController(
   TransactionService transactionService
) {

    @PostMapping
    TransactionResponse createTransaction(@RequestBody TransactionCreateRequest request) {
        return toResponse(transactionService.createTransaction(
            request.description(),
            request.category(),
            request.value()
        ));
    }

    @GetMapping
    List<TransactionResponse> getTransactions(@RequestParam(required = false) String category) {
        TransactionSearchBundle searchBundle = new TransactionSearchBundle(category);

        return transactionService.getTransactions(searchBundle).stream().map(this::toResponse).toList();
    }

    private TransactionResponse toResponse(TransactionDto dto) {
        return new TransactionResponse(
            dto.uuid(),
            dto.description(),
            dto.category(),
            dto.value(),
            dto.createdAt()
        );
    }
}
