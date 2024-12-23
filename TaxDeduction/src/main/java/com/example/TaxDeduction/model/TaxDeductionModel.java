package com.example.TaxDeduction.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TaxDeductionModel {

    @NotBlank(message = "กรุณากรอกชื่อ")
    private String firstName;

    @NotBlank(message = "กรุณากรอกนามสกุล")
    private String lastName;

    @NotNull(message = "กรุณากรอกรายได้ทั้งปี")
    private BigDecimal annualIncome;

    private BigDecimal spouseDeduction;
    private BigDecimal childDeduction;
    private BigDecimal lifeInsuranceDeduction;
    private BigDecimal providentFundDeduction;
    private BigDecimal socialSecurityDeduction;
    private BigDecimal donationDeduction;
    private boolean hasSpouse;

    public BigDecimal calculateTotalDeductions() {
        BigDecimal totalDeductions = BigDecimal.ZERO;

        // เพิ่มค่าลดหย่อนส่วนตัว 60,000 บาท
        totalDeductions = totalDeductions.add(BigDecimal.valueOf(60000));

        // ถ้ามีคู่สมรส เพิ่มค่าลดหย่อนคู่สมรส 60,000 บาท
        if (hasSpouse) {
            totalDeductions = totalDeductions.add(BigDecimal.valueOf(60000));
        }

        // เพิ่มค่าลดหย่อนอื่นๆ
        if (childDeduction != null) totalDeductions = totalDeductions.add(childDeduction);
        if (lifeInsuranceDeduction != null) totalDeductions = totalDeductions.add(lifeInsuranceDeduction);
        if (providentFundDeduction != null) totalDeductions = totalDeductions.add(providentFundDeduction);
        if (socialSecurityDeduction != null) totalDeductions = totalDeductions.add(socialSecurityDeduction);
        if (donationDeduction != null) totalDeductions = totalDeductions.add(donationDeduction);

        return totalDeductions;
    }

    public BigDecimal calculateTaxableIncome() {
        return annualIncome.subtract(calculateTotalDeductions()).max(BigDecimal.ZERO);
    }

    public BigDecimal calculateIncomeTax() {
        BigDecimal taxableIncome = calculateTaxableIncome();

        // ไม่เกิน 150,000 บาท ยกเว้นภาษี
        if (taxableIncome.compareTo(BigDecimal.valueOf(150000)) <= 0) {
            return BigDecimal.ZERO;
        }
        // 150,001 - 300,000 บาท อัตรา 5%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(300000)) <= 0) {
            return taxableIncome.subtract(BigDecimal.valueOf(150000))
                    .multiply(BigDecimal.valueOf(0.05));
        }
        // 300,001 - 500,000 บาท อัตรา 10%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(500000)) <= 0) {
            return BigDecimal.valueOf(7500) // ภาษีสะสมจากขั้นก่อน (150,000 * 5%)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(300000))
                            .multiply(BigDecimal.valueOf(0.10)));
        }
        // 500,001 - 750,000 บาท อัตรา 15%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(750000)) <= 0) {
            return BigDecimal.valueOf(27500) // ภาษีสะสม (7,500 + 200,000 * 10%)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(500000))
                            .multiply(BigDecimal.valueOf(0.15)));
        }
        // 750,001 - 1,000,000 บาท อัตรา 20%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(1000000)) <= 0) {
            return BigDecimal.valueOf(65000) // ภาษีสะสม
                    .add(taxableIncome.subtract(BigDecimal.valueOf(750000))
                            .multiply(BigDecimal.valueOf(0.20)));
        }
        // 1,000,001 - 2,000,000 บาท อัตรา 25%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(2000000)) <= 0) {
            return BigDecimal.valueOf(115000) // ภาษีสะสม
                    .add(taxableIncome.subtract(BigDecimal.valueOf(1000000))
                            .multiply(BigDecimal.valueOf(0.25)));
        }
        // 2,000,001 - 5,000,000 บาท อัตรา 30%
        else if (taxableIncome.compareTo(BigDecimal.valueOf(5000000)) <= 0) {
            return BigDecimal.valueOf(365000) // ภาษีสะสม
                    .add(taxableIncome.subtract(BigDecimal.valueOf(2000000))
                            .multiply(BigDecimal.valueOf(0.30)));
        }
        // มากกว่า 5,000,000 บาท อัตรา 35%
        else {
            return BigDecimal.valueOf(1265000) // ภาษีสะสม
                    .add(taxableIncome.subtract(BigDecimal.valueOf(5000000))
                            .multiply(BigDecimal.valueOf(0.35)));
        }
    }

    // Getters and Setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public BigDecimal getChildDeduction() {
        return childDeduction;
    }

    public void setChildDeduction(BigDecimal childDeduction) {
        this.childDeduction = childDeduction;
    }

    public BigDecimal getLifeInsuranceDeduction() {
        return lifeInsuranceDeduction;
    }

    public void setLifeInsuranceDeduction(BigDecimal lifeInsuranceDeduction) {
        this.lifeInsuranceDeduction = lifeInsuranceDeduction;
    }

    public BigDecimal getProvidentFundDeduction() {
        return providentFundDeduction;
    }

    public void setProvidentFundDeduction(BigDecimal providentFundDeduction) {
        this.providentFundDeduction = providentFundDeduction;
    }

    public BigDecimal getSocialSecurityDeduction() {
        return socialSecurityDeduction;
    }

    public void setSocialSecurityDeduction(BigDecimal socialSecurityDeduction) {
        this.socialSecurityDeduction = socialSecurityDeduction;
    }

    public BigDecimal getDonationDeduction() {
        return donationDeduction;
    }

    public void setDonationDeduction(BigDecimal donationDeduction) {
        this.donationDeduction = donationDeduction;
    }

    public boolean isHasSpouse() {
        return hasSpouse;
    }

    public void setHasSpouse(boolean hasSpouse) {
        this.hasSpouse = hasSpouse;
    }
}
