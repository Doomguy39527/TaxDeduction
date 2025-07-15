package com.example.TaxDeduction.controller;

import com.example.TaxDeduction.entity.TaxDeductionEntity;
import com.example.TaxDeduction.model.TaxDeductionModel;
import com.example.TaxDeduction.service.TaxDeductionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class TaxDeductionController {
    @Autowired
    private TaxDeductionService taxDeductionService; // Inject TaxDeductionService

    // แสดงหน้าแรกพร้อมฟอร์มคำนวณภาษี
    @GetMapping("/")
    public String showCalculator(Model model) {
        model.addAttribute("taxDeductionModel", new TaxDeductionModel()); // สร้างโมเดลใหม่สำหรับฟอร์ม
        return "index"; // แสดงหน้า index.html
    }

    // ประมวลผลการคำนวณภาษี
    @PostMapping("/calculate")
    public String calculateTax(@Valid @ModelAttribute("taxDeductionModel") TaxDeductionModel request,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "index"; // หากมีข้อผิดพลาดในการตรวจสอบข้อมูล ให้กลับไปที่หน้าเดิม
        }

        // คำนวณและบันทึกผลลัพธ์
        TaxDeductionEntity result = taxDeductionService.calculateAndSaveTaxDeduction(request);

        // เพิ่มผลลัพธ์ลงใน Model เพื่อแสดงในหน้าเว็บ
        model.addAttribute("result", request); // เพิ่มผลลัพธ์ลงใน Model
        model.addAttribute("totalIncome", request.calculateTotalIncome()); // รายได้รวม
        model.addAttribute("fixedExpenses", request.calculateFixedExpenses()); // ค่าใช้จ่ายเหมา
        model.addAttribute("totalDeductions", request.calculateTotalDeductions()); // ค่าลดหย่อนทั้งหมด
        model.addAttribute("netIncome", request.calculateNetIncome()); // รายได้สุทธิ
        model.addAttribute("taxableIncome", request.calculateTaxableIncome()); // เงินได้ที่ต้องเสียภาษี
        model.addAttribute("incomeTax", request.calculateIncomeTax()); // ภาษีที่ต้องชำระ
        model.addAttribute("parentSupportDeduction", request.getParentSupportDeduction()); // ค่าลดหย่อนบิดา-มารดา

        return "index"; // แสดงผลลัพธ์ในหน้า index.html
    }
}