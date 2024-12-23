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
        return "index";
    }

    @PostMapping("/calculate")
    public String calculateTaxDeduction(
            @Valid @ModelAttribute("taxDeductionRequest") TaxDeductionModel request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        BigDecimal totalDeductions = request.calculateTotalDeductions()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxableIncome = request.calculateTaxableIncome()
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal incomeTax = request.calculateIncomeTax()
                .setScale(2, RoundingMode.HALF_UP);

        model.addAttribute("firstName", request.getFirstName());
        model.addAttribute("lastName", request.getLastName());
        model.addAttribute("totalDeductions", totalDeductions);
        model.addAttribute("taxableIncome", taxableIncome);
        model.addAttribute("incomeTax", incomeTax);

        return "index";
    }
}