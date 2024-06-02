package com.github.iamhi.hizone.station.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends
    JpaRepository<TransactionEntity, Integer> {

    List<TransactionEntity> findByOwnerUuid(String ownerUuid);

    List<TransactionEntity> findByOwnerUuidAndCategory(String ownerUuid, String category);

//    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, p.brand, p.madein, p.price) LIKE %?1%")
//    public List<Product> search(String keyword);`
}
