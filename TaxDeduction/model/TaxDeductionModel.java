package com.example.TaxDeduction.model;

// File must be named TaxDeductionRequest.java

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxDeductionModel {
    // Personal information
    @NotNull(message = "รายได้ประจำปีต้องไม่เป็นค่าว่าง")
    @Min(value = 0, message = "รายได้ประจำปีต้องเป็นตัวเลขบวก")
    private BigDecimal annualIncome;

    // Deduction categories
    @Min(value = 0, message = "เบี้ยประกันชีวิตต้องเป็นตัวเลขบวก")
    private BigDecimal lifeInsuranceDeduction;

    @Min(value = 0, message = "เงินสะสม กบข. ต้องเป็นตัวเลขบวก")
    private BigDecimal governmentPensionFundDeduction;

    @Min(value = 0, message = "เงินบริจาคต้องเป็นตัวเลขบวก")
    private BigDecimal donationDeduction;

    // Additional deductions for different categories
    @Min(value = 0, message = "ค่าลดหย่อนบุตรต้องเป็นตัวเลขบวก")
    private Integer numberOfChildren;

    @Min(value = 0, message = "ค่าลดหย่อนพ่อแม่ต้องเป็นตัวเลขบวก")
    private Integer numberOfParentDependents;

    // Personal tax deduction constants
    private static final BigDecimal PERSONAL_DEDUCTION = BigDecimal.valueOf(60000);
    private static final BigDecimal CHILD_DEDUCTION = BigDecimal.valueOf(30000);
    private static final BigDecimal PARENT_DEDUCTION = BigDecimal.valueOf(30000);
    private static final BigDecimal MAX_DONATION_DEDUCTION = BigDecimal.valueOf(100000);

    // Method to calculate total tax deductions
    public BigDecimal calculateTotalDeductions() {
        BigDecimal totalDeductions = PERSONAL_DEDUCTION;

        // Life insurance deduction (max 100,000 baht)
        BigDecimal lifeInsurance = lifeInsuranceDeduction == null ? BigDecimal.ZERO :
                lifeInsuranceDeduction.min(BigDecimal.valueOf(100000));
        totalDeductions = totalDeductions.add(lifeInsurance);

        // Government Pension Fund deduction
        BigDecimal gpfDeduction = governmentPensionFundDeduction == null ? BigDecimal.ZERO :
                governmentPensionFundDeduction.min(BigDecimal.valueOf(500000));
        totalDeductions = totalDeductions.add(gpfDeduction);

        // Donation deduction (max 100,000 baht)
        BigDecimal donationDeduct = donationDeduction == null ? BigDecimal.ZERO :
                donationDeduction.min(MAX_DONATION_DEDUCTION);
        totalDeductions = totalDeductions.add(donationDeduct);

        // Children deduction
        int childCount = numberOfChildren == null ? 0 : numberOfChildren;
        BigDecimal childDeduction = CHILD_DEDUCTION.multiply(BigDecimal.valueOf(Math.min(childCount, 3)));
        totalDeductions = totalDeductions.add(childDeduction);

        // Parents deduction
        int parentCount = numberOfParentDependents == null ? 0 : numberOfParentDependents;
        BigDecimal parentDeduction = PARENT_DEDUCTION.multiply(BigDecimal.valueOf(Math.min(parentCount, 2)));
        totalDeductions = totalDeductions.add(parentDeduction);

        return totalDeductions;
    }

    // Method to calculate taxable income
    public BigDecimal calculateTaxableIncome() {
        BigDecimal totalDeductions = calculateTotalDeductions();
        return annualIncome.subtract(totalDeductions).max(BigDecimal.ZERO);
    }

    // Method to calculate income tax based on Thai tax brackets
    public BigDecimal calculateIncomeTax() {
        BigDecimal taxableIncome = calculateTaxableIncome();

        // Thai income tax brackets for 2024 (simplified)
        if (taxableIncome.compareTo(BigDecimal.valueOf(300000)) <= 0) {
            return BigDecimal.ZERO; // No tax for income up to 300,000
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(500000)) <= 0) {
            // 5% tax on income between 300,001 and 500,000
            return taxableIncome.subtract(BigDecimal.valueOf(300000))
                    .multiply(BigDecimal.valueOf(0.05));
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(1000000)) <= 0) {
            // 10% tax on income between 500,001 and 1,000,000
            return BigDecimal.valueOf(10000)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(500000))
                            .multiply(BigDecimal.valueOf(0.10)));
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(4000000)) <= 0) {
            // 20% tax on income between 1,000,001 and 4,000,000
            return BigDecimal.valueOf(60000)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(1000000))
                            .multiply(BigDecimal.valueOf(0.20)));
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(5000000)) <= 0) {
            // 30% tax on income between 4,000,001 and 5,000,000
            return BigDecimal.valueOf(460000)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(4000000))
                            .multiply(BigDecimal.valueOf(0.30)));
        } else {
            // 35% tax on income above 5,000,000
            return BigDecimal.valueOf(760000)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(5000000))
                            .multiply(BigDecimal.valueOf(0.35)));
        }
    }
}