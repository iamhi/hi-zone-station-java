package com.github.iamhi.hizone.station.core.transaction;

import java.util.List;

public interface TransactionService {

    TransactionDto createTransaction(String description, String category, Long value);

    List<TransactionDto> getTransactions(TransactionSearchBundle searchBundle);
}
