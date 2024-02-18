package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.services.FoodService;
import com.pc.kilojoules.services.PortionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class FoodController {

    private final FoodService foodService;
    private final PortionService portionService;
    private static final Logger log = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    public FoodController(FoodService foodService, PortionService portionService) {
        this.foodService = foodService;
        this.portionService = portionService;
    }

    @GetMapping({"/", "/listFoods"})
    public String getFoodsPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            page = Math.max(page, 0);

            Page<Food> foodsPage = foodService.getFoodsByPage(page);
            int totalPages = foodsPage.getTotalPages();

            if (page >= totalPages) {
                page = totalPages - 1;
                foodsPage = foodService.getFoodsByPage(page);
            }

            model.addAttribute("foods", foodsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            redirectAttributes.addAttribute("currentPage", page);
            return "listFoods";
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            model.addAttribute("errorMessage", "Error accessing data. Please try again later.");
            return "listFoods";
        } catch (Exception e) {
            log.error("Unexpected error:", e);
            model.addAttribute("errorMessage", "Something went wrong!");
            return "listFoods";
        }
    }

    @GetMapping("/food/{id}/edit")
    public String editFood(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.findById(id);
            model.addAttribute("food", food);
            return "editFood";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/";
        }
    }

    @PostMapping("/food/{id}/update")
    public String updateFood(@PathVariable("id") Long id, @Valid @ModelAttribute("food") Food food, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "Validace selhala! Formulář byl resetován!");
            return "redirect:/food/{id}/edit";
        }

        try {
            foodService.update(food);
            String foodName = food.getName();
            redirectAttributes.addFlashAttribute("successMessage", "Potravina \"" + foodName + "\" byla aktualizována!");
            return "redirect:/";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/";
        }
    }

    @GetMapping("/food/create")
    public String createFoodGet(Model model, @ModelAttribute Food food, RedirectAttributes redirectAttributes) {
        model.addAttribute("food", food);
        return "createFood";
    }

    @PostMapping("/food/create")
    public String createFoodPost(@Valid @ModelAttribute("food") Food food, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "Validace selhala! Formulář byl resetován!");
            return "redirect:/food/create";
        }

        try {
            Food savedFood = foodService.create(food);

            List<Portion> portionList = new ArrayList<>();
            Portion portion100 = new Portion("100 g", BigDecimal.valueOf(100), savedFood);
            Portion portion1 = new Portion("1 g", BigDecimal.valueOf(1), savedFood);
            portionList.add(portion100);
            portionList.add(portion1);

            savedFood.setPortions(portionList);
            Food updatedFood = foodService.update(savedFood);
            String foodName = updatedFood.getName();
            redirectAttributes.addFlashAttribute("successMessage", "Potravina \"" + foodName + "\" byla přidána!");
            return "redirect:/";
        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chyba při práci s databází.");
            return "redirect:/food/create";
        }
    }

    @PostMapping ("/food/{id}/delete")
    public String deleteFood(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.deleteFoodById(id);
            String foodName = food.getName();
            redirectAttributes.addFlashAttribute("successMessage", "Potravina \"" + foodName + "\" byla smazána!");
            return "redirect:/";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/";
        }
    }
}
