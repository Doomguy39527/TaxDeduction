package com.example.TaxDeduction.controller;

import com.example.TaxDeduction.model.TaxDeductionModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class TaxDeductionController {

    @GetMapping("/")
    public String showTaxCalculationForm(Model model) {
        model.addAttribute("taxDeductionRequest", new TaxDeductionModel());
        return "index"; // เปลี่ยนชื่อเป็น index.html
    }

    @PostMapping("/calculate")
    public String calculateTaxDeduction(
            @Valid @ModelAttribute("taxDeductionRequest") TaxDeductionModel request,
            BindingResult bindingResult,
            Model model) {

        // ถ้ามี validation error กลับไปที่ฟอร์ม
        if (bindingResult.hasErrors()) {
            return "index"; // เปลี่ยนชื่อเป็น index.html
        }

        // คำนวณ total deductions
        BigDecimal totalDeductions = request.calculateTotalDeductions()
                .setScale(2, RoundingMode.HALF_UP);

        // คำนวณ taxable income
        BigDecimal taxableIncome = request.calculateTaxableIncome()
                .setScale(2, RoundingMode.HALF_UP);

        // คำนวณ income tax
        BigDecimal incomeTax = request.calculateIncomeTax()
                .setScale(2, RoundingMode.HALF_UP);

        // ส่งผลลัพธ์กลับไปยัง view
        model.addAttribute("totalDeductions", totalDeductions);
        model.addAttribute("taxableIncome", taxableIncome);
        model.addAttribute("incomeTax", incomeTax);

        return "index"; // เปลี่ยนชื่อเป็น index.html
    }
}
