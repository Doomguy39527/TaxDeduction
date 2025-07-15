package com.example.TaxDeduction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_deduction_calculator")
public class TaxDeductionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID หลักของ Entity

    @Column(nullable = false)
    private BigDecimal employmentIncome; // รายได้จากเงินเดือน

    @Column(nullable = false)
    private BigDecimal businessIncome; // รายได้จากธุรกิจ

    @Column(nullable = false)
    private BigDecimal investmentIncome; // รายได้จากการลงทุน

    @Column(nullable = false)
    private boolean hasSpouse; // มีคู่สมรสหรือไม่

    @Column(nullable = false)
    private Integer parentSupportCount; // จำนวนบิดา-มารดาที่อุปการะ

    @Column
    private Integer childDeduction; // จำนวนบุตรที่ลดหย่อน

    @Column
    private BigDecimal lifeInsuranceDeduction; // ค่าลดหย่อนเบี้ยประกันชีวิต

    @Column
    private BigDecimal providentFundDeduction; // ค่าลดหย่อนกองทุนสำรองเลี้ยงชีพ

    @Column
    private BigDecimal socialSecurityDeduction; // ค่าลดหย่อนประกันสังคม

    @Column
    private BigDecimal donationDeduction; // ค่าลดหย่อนเงินบริจาค

    @Column(nullable = false)
    private BigDecimal fixedExpenses; // ค่าใช้จ่ายเหมา

    @Column(nullable = false)
    private BigDecimal totalDeductions; // ค่าลดหย่อนทั้งหมด

    @Column(nullable = false)
    private BigDecimal netIncome; // รายได้สุทธิ

    @Column(nullable = false)
    private BigDecimal taxableIncome; // เงินได้ที่ต้องเสียภาษี

    @Column(nullable = false)
    private BigDecimal incomeTax; // ภาษีที่ต้องชำระ

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime calculationDate = LocalDateTime.now(); // วันที่คำนวณ

    @Column(nullable = false)
    private Integer age; // อายุของผู้เสียภาษี

    // Getter และ Setter สำหรับทุกฟิลด์
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getEmploymentIncome() { return employmentIncome; }
    public void setEmploymentIncome(BigDecimal employmentIncome) { this.employmentIncome = employmentIncome; }
    public BigDecimal getBusinessIncome() { return businessIncome; }
    public void setBusinessIncome(BigDecimal businessIncome) { this.businessIncome = businessIncome; }
    public BigDecimal getInvestmentIncome() { return investmentIncome; }
    public void setInvestmentIncome(BigDecimal investmentIncome) { this.investmentIncome = investmentIncome; }
    public boolean isHasSpouse() { return hasSpouse; }
    public void setHasSpouse(boolean hasSpouse) { this.hasSpouse = hasSpouse; }
    public Integer getParentSupportCount() { return parentSupportCount; }
    public void setParentSupportCount(Integer parentSupportCount) { this.parentSupportCount = parentSupportCount; }
    public Integer getChildDeduction() { return childDeduction; }
    public void setChildDeduction(Integer childDeduction) { this.childDeduction = childDeduction; }
    public BigDecimal getLifeInsuranceDeduction() { return lifeInsuranceDeduction; }
    public void setLifeInsuranceDeduction(BigDecimal lifeInsuranceDeduction) { this.lifeInsuranceDeduction = lifeInsuranceDeduction; }
    public BigDecimal getProvidentFundDeduction() { return providentFundDeduction; }
    public void setProvidentFundDeduction(BigDecimal providentFundDeduction) { this.providentFundDeduction = providentFundDeduction; }
    public BigDecimal getSocialSecurityDeduction() { return socialSecurityDeduction; }
    public void setSocialSecurityDeduction(BigDecimal socialSecurityDeduction) { this.socialSecurityDeduction = socialSecurityDeduction; }
    public BigDecimal getDonationDeduction() { return donationDeduction; }
    public void setDonationDeduction(BigDecimal donationDeduction) { this.donationDeduction = donationDeduction; }
    public BigDecimal getFixedExpenses() { return fixedExpenses; }
    public void setFixedExpenses(BigDecimal fixedExpenses) { this.fixedExpenses = fixedExpenses; }
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }
    public BigDecimal getNetIncome() { return netIncome; }
    public void setNetIncome(BigDecimal netIncome) { this.netIncome = netIncome; }
    public BigDecimal getTaxableIncome() { return taxableIncome; }
    public void setTaxableIncome(BigDecimal taxableIncome) { this.taxableIncome = taxableIncome; }
    public BigDecimal getIncomeTax() { return incomeTax; }
    public void setIncomeTax(BigDecimal incomeTax) { this.incomeTax = incomeTax; }
    public LocalDateTime getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDateTime calculationDate) { this.calculationDate = calculationDate; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}