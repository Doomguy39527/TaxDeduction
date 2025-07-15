package com.example.TaxDeduction.service;

import com.example.TaxDeduction.entity.TaxDeductionEntity;
import com.example.TaxDeduction.model.TaxDeductionModel;
import com.example.TaxDeduction.repository.TaxDeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaxDeductionService {

    @Autowired
    private TaxDeductionRepository taxDeductionRepository; // Inject TaxDeductionRepository

    @Transactional
    public TaxDeductionEntity calculateAndSaveTaxDeduction(TaxDeductionModel model) {
        // ตรวจสอบค่าลดหย่อนก่อนคำนวณ
        validateDeductionLimits(model);

        TaxDeductionEntity entity = new TaxDeductionEntity();

        // เซ็ตข้อมูลรายได้
        entity.setEmploymentIncome(getValueOrZero(model.getEmploymentIncome()));
        entity.setBusinessIncome(getValueOrZero(model.getBusinessIncome()));
        entity.setInvestmentIncome(getValueOrZero(model.getInvestmentIncome()));

        // เซ็ตอายุ
        entity.setAge(model.getAge());

        // เซ็ตข้อมูลลดหย่อน
        entity.setHasSpouse(model.isHasSpouse());
        entity.setParentSupportCount(model.getParentSupportCount());
        entity.setChildDeduction(model.getChildDeduction());
        entity.setLifeInsuranceDeduction(model.getLifeInsuranceDeduction());
        entity.setProvidentFundDeduction(model.getProvidentFundDeduction());
        entity.setSocialSecurityDeduction(model.getSocialSecurityDeduction());
        entity.setDonationDeduction(model.getDonationDeduction());

        // คำนวณและเซ็ตค่าที่ได้
        entity.setFixedExpenses(model.calculateFixedExpenses()); // ค่าใช้จ่ายเหมา
        entity.setTotalDeductions(model.calculateTotalDeductions()); // ค่าลดหย่อนทั้งหมด
        entity.setNetIncome(model.calculateNetIncome()); // รายได้สุทธิ
        entity.setTaxableIncome(model.calculateTaxableIncome()); // เงินได้ที่ต้องเสียภาษี
        entity.setIncomeTax(model.calculateIncomeTax()); // ภาษีที่ต้องชำระ

        // เซ็ตวันที่คำนวณ
        entity.setCalculationDate(LocalDateTime.now());

        // บันทึกลงฐานข้อมูล
        return taxDeductionRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<TaxDeductionEntity> getAllCalculations() {
        return taxDeductionRepository.findAll(); // ดึงข้อมูลทั้งหมดจากฐานข้อมูล
    }

    @Transactional(readOnly = true)
    public Optional<TaxDeductionEntity> getCalculationById(Long id) {
        return taxDeductionRepository.findById(id); // ดึงข้อมูลโดยใช้ ID
    }

    @Transactional
    public void deleteCalculation(Long id) {
        taxDeductionRepository.deleteById(id); // ลบข้อมูลโดยใช้ ID
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalTaxPaid() {
        return taxDeductionRepository.findAll().stream()
                .map(TaxDeductionEntity::getIncomeTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // คำนวณภาษีรวมทั้งหมด
    }

    // ตรวจสอบค่าลดหย่อน
    private void validateDeductionLimits(TaxDeductionModel model) {
        // ตรวจสอบเบี้ยประกันชีวิต (ไม่เกิน 100,000 บาท)
        if (model.getLifeInsuranceDeduction() != null &&
                model.getLifeInsuranceDeduction().compareTo(new BigDecimal("100000")) > 0) {
            model.setLifeInsuranceDeduction(new BigDecimal("100000"));
        }

        // ตรวจสอบกองทุนสำรองเลี้ยงชีพ (ไม่เกิน 500,000 บาท)
        if (model.getProvidentFundDeduction() != null &&
                model.getProvidentFundDeduction().compareTo(new BigDecimal("500000")) > 0) {
            model.setProvidentFundDeduction(new BigDecimal("500000"));
        }

        // ตรวจสอบประกันสังคม (ไม่เกิน 9,000 บาท)
        if (model.getSocialSecurityDeduction() != null &&
                model.getSocialSecurityDeduction().compareTo(new BigDecimal("9000")) > 0) {
            model.setSocialSecurityDeduction(new BigDecimal("9000"));
        }

        // ตรวจสอบเงินบริจาค (ไม่เกิน 10% ของรายได้หลังหักลดหย่อน)
        BigDecimal maxDonation = model.calculateTotalIncome()
                .subtract(model.calculateTotalDeductions())
                .multiply(new BigDecimal("0.10"));

        if (model.getDonationDeduction() != null &&
                model.getDonationDeduction().compareTo(maxDonation) > 0) {
            model.setDonationDeduction(maxDonation);
        }
    }

    // ตรวจสอบค่า null และคืนค่าเป็น 0 หากเป็น null
    private BigDecimal getValueOrZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}