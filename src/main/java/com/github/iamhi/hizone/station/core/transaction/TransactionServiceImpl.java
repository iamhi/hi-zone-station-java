package com.github.iamhi.hizone.station.core.transaction;

import com.github.iamhi.hizone.station.core.user.MemberCache;
import com.github.iamhi.hizone.station.data.TransactionEntity;
import com.github.iamhi.hizone.station.data.TransactionRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class TransactionServiceImpl implements TransactionService {

    private final MemberCache memberCache;

    private final TransactionRepository repository;

    @Override
    public TransactionDto createTransaction(String description, String category, Long value) {
        TransactionEntity transactionEntity = generateEntity();

        transactionEntity.setDescription(description);
        transactionEntity.setValue(value);
        transactionEntity.setCategory(category);

        return toDto(repository.save(transactionEntity));
    }

    @Override
    public List<TransactionDto> getTransactions(TransactionSearchBundle searchBundle) {
        if (StringUtils.isBlank(searchBundle.category())) {
            return repository.findByOwnerUuid(memberCache.getUuid())
                .stream().map(this::toDto).collect(Collectors.toList());
        }
        return repository.findByOwnerUuidAndCategory(memberCache.getUuid(), searchBundle.category())
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    private TransactionEntity generateEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setUuid(UUID.randomUUID().toString());
        transactionEntity.setCreatedAt(Instant.now());
        transactionEntity.setOwnerUuid(memberCache.getUuid());

        return transactionEntity;
    }

    private TransactionDto toDto(TransactionEntity entity) {
        return new TransactionDto(
            entity.getUuid(),
            entity.getDescription(),
            entity.getCategory(),
            entity.getValue(),
            entity.getCreatedAt()
        );
    }
}