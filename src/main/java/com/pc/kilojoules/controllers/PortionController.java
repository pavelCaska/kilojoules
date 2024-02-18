package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.services.FoodService;
import com.pc.kilojoules.services.PortionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
public class PortionController {

    private final PortionService portionService;
    private final FoodService foodService;

    @Autowired
    public PortionController(PortionService portionService, FoodService foodService) {
        this.portionService = portionService;
        this.foodService = foodService;
    }

    @GetMapping("/food/{id}/portion")
    public String createPortion(@PathVariable("id") Long id, Model model, @ModelAttribute("portion") Portion portion, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.findById(id);
            model.addAttribute("food", food);
            model.addAttribute("portions", portionService.findPortionsByFood(food));
            return "editPortion";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/";
        }
    }

    @PostMapping("/food/{id}/portion")
    public String savePortion(@PathVariable("id") Long id, @Valid @ModelAttribute("portion") Portion portion, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "Validace selhala! Formulář byl resetován!");
            return "redirect:/food/" + id + "/portion";
        }
        try {
            Food food = foodService.findById(id);
            Portion newPortion = Portion.builder()
                    .portionName(portion.getPortionName())
                    .portionSize(portion.getPortionSize())
                    .food(food)
                    .build();
            Portion savedPortion = portionService.save(newPortion);
            redirectAttributes.addFlashAttribute("successMessage", "Portion " + savedPortion.getId() + " saved successfully!");
            return "redirect:/food/" + id + "/portion";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/food/" + id + "/portion";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Duplicitní název porce!");
            return "redirect:/food/" + id + "/portion";
        }
    }

    @PostMapping("/food/{foodId}/portion/{id}/delete")
    public String deletePortion(@PathVariable Long foodId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Portion portion = portionService.deletePortionById(id);
            String portionName = portion.getPortionName();
            redirectAttributes.addFlashAttribute("successMessage", "Porce \"" + portionName + "\" byla smazána!");
            return "redirect:/food/" + foodId + "/portion";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Porce nenalezena!");
            return "redirect:/food/" + foodId + "/portion";
        }
    }
}
