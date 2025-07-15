package com.example.TaxDeduction.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDeductionModel {
    // ค่าลดหย่อนหลัก
    private static final BigDecimal PERSONAL_DEDUCTION = BigDecimal.valueOf(60000); // ค่าลดหย่อนส่วนตัว
    private static final BigDecimal SPOUSE_DEDUCTION = BigDecimal.valueOf(60000); // ค่าลดหย่อนคู่สมรส
    private static final BigDecimal PARENT_SUPPORT_DEDUCTION = BigDecimal.valueOf(30000); // ค่าลดหย่อนดูแลพ่อแม่ต่อคน
    private static final BigDecimal FIRST_CHILD_DEDUCTION = BigDecimal.valueOf(30000); // บุตรคนแรก
    private static final BigDecimal SECOND_CHILD_DEDUCTION = BigDecimal.valueOf(60000); // บุตรคนที่สอง
    private static final BigDecimal OTHER_CHILD_DEDUCTION = BigDecimal.valueOf(30000); // บุตรคนที่สามขึ้นไป
    private static final BigDecimal SENIOR_CITIZEN_DEDUCTION = BigDecimal.valueOf(190000); // ค่าลดหย่อนผู้สูงอายุ

    // รายได้ของผู้เสียภาษี
    @NotNull(message = "กรุณากรอกรายได้จากการจ้างงาน")
    private BigDecimal employmentIncome; // เงินเดือน
    @NotNull(message = "กรุณากรอกรายได้จากธุรกิจหรืออาชีพอิสระ")
    private BigDecimal businessIncome; // รายได้จากธุรกิจ
    @NotNull(message = "กรุณากรอกรายได้จากการลงทุน")
    private BigDecimal investmentIncome; // รายได้จากการลงทุน

    private boolean hasSpouse; // มีคู่สมรสหรือไม่

    @Min(value = 0, message = "จำนวนผู้ที่อุปการะต้องไม่น้อยกว่า 0")
    @Max(value = 4, message = "จำนวนผู้ที่อุปการะต้องไม่เกิน 4")
    private Integer parentSupportCount = 0; // จำนวนพ่อแม่ที่อุปการะ

    @Min(value = 0, message = "จำนวนบุตรต้องไม่น้อยกว่า 0")
    private Integer childDeduction = null; // จำนวนบุตรที่ลดหย่อน

    private BigDecimal lifeInsuranceDeduction; // ค่าลดหย่อนเบี้ยประกันชีวิต
    private BigDecimal providentFundDeduction; // ค่าลดหย่อนกองทุนสำรองเลี้ยงชีพ
    private BigDecimal socialSecurityDeduction; // ค่าลดหย่อนประกันสังคม
    private List<BigDecimal> donations = new ArrayList<>(); // เงินบริจาค

    @Min(value = 0, message = "อายุต้องไม่น้อยกว่า 0")
    private Integer age; // อายุของผู้เสียภาษี

    //  คำนวณรายได้รวม
    public BigDecimal calculateTotalIncome() {
        BigDecimal total = BigDecimal.ZERO;
        if (employmentIncome != null) total = total.add(employmentIncome);
        if (businessIncome != null) total = total.add(businessIncome);
        if (investmentIncome != null) total = total.add(investmentIncome);
        return total;
    }

    //  คำนวณค่าใช้จ่ายเหมา (50% ของรายได้แต่ไม่เกิน 100,000 บาท)
    public BigDecimal calculateFixedExpenses() {
        BigDecimal totalIncome = calculateTotalIncome();
        BigDecimal fixedExpenses = totalIncome.multiply(BigDecimal.valueOf(0.5));
        return fixedExpenses.min(BigDecimal.valueOf(100000));
    }

    //  คำนวณค่าลดหย่อนทั้งหมด
    public BigDecimal calculateTotalDeductions() {
        BigDecimal totalDeductions = PERSONAL_DEDUCTION; // ค่าลดหย่อนส่วนตัว 60,000 บาท

        if (hasSpouse) {
            totalDeductions = totalDeductions.add(SPOUSE_DEDUCTION); // คู่สมรส
        }

        if (parentSupportCount != null) {
            totalDeductions = totalDeductions.add(
                    PARENT_SUPPORT_DEDUCTION.multiply(BigDecimal.valueOf(parentSupportCount)) // พ่อแม่ที่อุปการะ
            );
        }

        // ค่าลดหย่อนสำหรับบุตร
        if (childDeduction != null) {
            if (childDeduction >= 1) totalDeductions = totalDeductions.add(FIRST_CHILD_DEDUCTION);
            if (childDeduction >= 2) totalDeductions = totalDeductions.add(SECOND_CHILD_DEDUCTION);
            if (childDeduction > 2) {
                totalDeductions = totalDeductions.add(
                        OTHER_CHILD_DEDUCTION.multiply(BigDecimal.valueOf(childDeduction - 2))
                );
            }
        }

        //  ค่าลดหย่อนผู้สูงอายุ (ใหม่)
        if (age != null && age >= 65) {
            totalDeductions = totalDeductions.add(SENIOR_CITIZEN_DEDUCTION);
        }

        if (lifeInsuranceDeduction != null) {
            BigDecimal maxLifeInsuranceDeduction = BigDecimal.valueOf(100000);
            totalDeductions = totalDeductions.add(lifeInsuranceDeduction.min(maxLifeInsuranceDeduction));
        }

        if (providentFundDeduction != null) totalDeductions = totalDeductions.add(providentFundDeduction);
        if (socialSecurityDeduction != null) totalDeductions = totalDeductions.add(socialSecurityDeduction);

        return totalDeductions;
    }

    //  คำนวณเงินได้สุทธิ
    public BigDecimal calculateNetIncome() {
        BigDecimal totalIncome = calculateTotalIncome();
        BigDecimal fixedExpenses = calculateFixedExpenses();
        BigDecimal deductions = calculateTotalDeductions();

        return totalIncome
                .subtract(fixedExpenses)
                .subtract(deductions)
                .max(BigDecimal.ZERO);
    }

    //  คำนวณภาษีที่ต้องจ่าย (แบบขั้นบันได)
    public BigDecimal calculateIncomeTax() {
        BigDecimal taxableIncome = calculateNetIncome(); // ใช้เงินได้สุทธิ
        BigDecimal tax = BigDecimal.ZERO;

        if (taxableIncome.compareTo(BigDecimal.valueOf(150000)) <= 0) return BigDecimal.ZERO;

        if (taxableIncome.compareTo(BigDecimal.valueOf(150000)) > 0) {
            BigDecimal taxableAmount = taxableIncome.min(BigDecimal.valueOf(300000)).subtract(BigDecimal.valueOf(150000));
            tax = tax.add(taxableAmount.multiply(BigDecimal.valueOf(0.05))); // 5%
        }
        if (taxableIncome.compareTo(BigDecimal.valueOf(300000)) > 0) {
            BigDecimal taxableAmount = taxableIncome.min(BigDecimal.valueOf(500000)).subtract(BigDecimal.valueOf(300000));
            tax = tax.add(taxableAmount.multiply(BigDecimal.valueOf(0.10))); // 10%
        }
        if (taxableIncome.compareTo(BigDecimal.valueOf(500000)) > 0) {
            BigDecimal taxableAmount = taxableIncome.min(BigDecimal.valueOf(750000)).subtract(BigDecimal.valueOf(500000));
            tax = tax.add(taxableAmount.multiply(BigDecimal.valueOf(0.15))); // 15%
        }

        return tax;
    }
}
