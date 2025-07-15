package com.example.TaxDeduction.repository;

import com.example.TaxDeduction.entity.TaxDeductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxDeductionRepository extends JpaRepository<TaxDeductionEntity, Long> {
    // Basic CRUD operations are automatically provided by JpaRepository
    // You can add custom query methods here if needed
}