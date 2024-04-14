package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.services.FoodService;
import jakarta.persistence.EntityNotFoundException;
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

@Controller
public class FoodController {

    private final FoodService foodService;

    private static final Logger log = LoggerFactory.getLogger(FoodController.class);


    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping({"/food"})
    public String getFoodsPaged(@RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes redirectAttributes) {
        page = Math.max(page, 0);

        Page<Food> foodsPage = foodService.getFoodsByPage(page);
        int totalPages = foodsPage.getTotalPages();

        if (page >= totalPages && totalPages != 0) {
            page = totalPages - 1;
            foodsPage = foodService.getFoodsByPage(page);
        } else if (totalPages == 0) {
            model.addAttribute("errorMessage", "No records found.");
        }

        model.addAttribute("foods", foodsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        redirectAttributes.addAttribute("currentPage", page);
        return "listFoods";
    }

    @GetMapping("/food/{id}/edit")
    public String editFoodGet(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.getFoodById(id);
            model.addAttribute("food", food);
            return "editFood";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/food";
    }

    @PostMapping("/food/{id}/edit")
    public String updateFood(@PathVariable("id") Long id, @Valid @ModelAttribute("food") Food food, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/food/" + id + "/edit";
        }
        if (!id.equals(food.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID mismatch between path variable and food data.");
            return "redirect:/food/" + id + "/edit"; }

        try {
            Food savedFood = foodService.updateFood(food);
            redirectAttributes.addFlashAttribute("successMessage", "Food \"" + savedFood.getName() + "\" has been updated.");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food not found!");
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/food";
    }

    @GetMapping("/food/create")
    public String createFoodGet(Model model, @ModelAttribute Food food) {
        model.addAttribute("food", food);
        return "createFood";
    }

    @PostMapping("/food/create")
    public String createFoodPost(@Valid @ModelAttribute("food") Food food, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            redirectAttributes.addFlashAttribute("food", food);
            return "redirect:/food/create";
        }

        try {
            Food savedFood = foodService.createFoodWithPortions(food);
            redirectAttributes.addFlashAttribute("successMessage", "Food \"" + savedFood.getName() + "\" added.");
            return "redirect:/food";
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
            return "redirect:/food/create";
        }
    }

    @PostMapping("/food/{id}/delete")
    public String deleteFood(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.deleteFoodById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Food \"" + food.getName() + "\" deleted.");
            return "redirect:/food";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/food";
    }
}
