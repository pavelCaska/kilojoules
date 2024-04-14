package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.models.MealDTO;
import com.pc.kilojoules.models.MealFormDTO;
import com.pc.kilojoules.services.FoodService;
import com.pc.kilojoules.services.MealFoodService;
import com.pc.kilojoules.services.MealService;
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

import java.util.List;

@Controller
public class MealController {

    private final MealService mealService;
    private final FoodService foodService;
    private final MealFoodService mealFoodService;
    private static final Logger log = LoggerFactory.getLogger(MealController.class);

    @Autowired
    public MealController(MealService mealService, FoodService foodService, MealFoodService mealFoodService) {
        this.mealService = mealService;
        this.foodService = foodService;
        this.mealFoodService = mealFoodService;
    }

    @GetMapping("/meal")
    public String getMealsPaged(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                RedirectAttributes redirectAttributes) {
        page = Math.max(page, 0);

        Page<Meal> mealsPage = mealService.getMealsByPage(page);
        int totalPages = mealsPage.getTotalPages();

        if (page >= totalPages && totalPages != 0) {
            page = totalPages - 1;
            mealsPage = mealService.getMealsByPage(page);
        } else if (totalPages == 0) {
            model.addAttribute("errorMessage", "No records found.");
        }

        List<Meal> meals = mealsPage.getContent();
        List<MealDTO> mealsDTO = mealService.calculateAndReturnMealDtoList(meals);
        model.addAttribute("mealsDTO", mealsDTO);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        redirectAttributes.addAttribute("currentPage", page);
        return "listMeals";
    }
    @GetMapping("/meal/food-search")
    public String searchFoodCreateMeal(@RequestParam(name = "query") String query, RedirectAttributes redirectAttributes) {
        List<Food> foods = foodService.searchFood(query);
        if (!foods.isEmpty()) {
            redirectAttributes.addFlashAttribute("query", query);
            redirectAttributes.addFlashAttribute("foods", foods);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Food not found!");
        }
        return "redirect:/meal/create";
    }
    @GetMapping("/meal/{id}/food-search")
    public String searchFoodEditMeal(@PathVariable("id") Long id, @RequestParam(name = "query") String query, RedirectAttributes redirectAttributes) {
        List<Food> foods = foodService.searchFood(query);
        if (!foods.isEmpty()) {
            redirectAttributes.addFlashAttribute("foods", foods);
            redirectAttributes.addFlashAttribute("query", query);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Food not found!");
        }
        return "redirect:/meal/" + id + "/food/add";
    }
    @GetMapping("/meal/create")
    public String createMealGet(@ModelAttribute("mealFormDTO") MealFormDTO mealFormDTO) {
        return "createMealSearch";
    }

    @GetMapping("/meal/{id}/edit")
    public String editMealGet(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("mealDTO", mealService.calculateAndReturnMealDto(id));
            return "editMeal";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal";
    }

    @GetMapping("/meal/{id}/food/add")
    public String addFoodToMealGet(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("mealDTO", mealService.calculateAndReturnMealDto(id));
            return "editMealSearch";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal";
    }

    @PostMapping("/meal/create")
    public String createMeal(@Valid @ModelAttribute MealFormDTO mealFormDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("foods") List<Long> foods) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed.");
            return "createMealSearch";
        }
        try {
            Meal meal = mealService.createMeal(mealFormDTO, foods);
            redirectAttributes.addFlashAttribute("successMessage", "Meal created successfully.");
            return "redirect:/meal/" + meal.getId() + "/edit";
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal";
    }
    @PostMapping("/meal/{id}/food/add")
    public String addFoodToMealPost(@PathVariable("id") Long id,
                                    @ModelAttribute MealFormDTO mealFormDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam("foods") List<Long> foods) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed.");
            return "redirect:/meal/" + id + "/edit";
        }
        try {
            Meal meal = mealService.addFoodToMeal(id, mealFormDTO, foods);
            redirectAttributes.addFlashAttribute("successMessage", meal.getMealName() + " updated successfully.");

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal/" + id + "/edit";
    }

    @PostMapping("/meal/{id}/edit")
    public String editMealPost(@PathVariable("id") Long id, @RequestParam String mealName,
                           RedirectAttributes redirectAttributes) {

        try {
            Meal meal = mealService.updateMealName(id, mealName);
            redirectAttributes.addFlashAttribute("successMessage", meal.getMealName() + " updated successfully.");
            return "redirect:/meal";

        } catch (RecordNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal/" + id + "/edit";
    }

    @PostMapping("/meal/{id}/delete")
    public String deleteMeal(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Meal meal = mealService.deleteMealById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Meal \"" + meal.getMealName() + "\" was deleted!");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal";
    }

    @PostMapping("/meal/{mealId}/food/{mealFoodId}/delete")
    public String deleteFoodFromMeal(@PathVariable("mealId") Long mealId, @PathVariable("mealFoodId") Long id, RedirectAttributes redirectAttributes) {
        if(!mealFoodService.existsMealFoodByMealIdAndId(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "MealFood record with id " + id + " is not associated with Meal record with id " + mealId);
            return "redirect:/meal";
        }
        try {
            MealFood mf = mealFoodService.deleteMealFoodById(mealId, id);
            redirectAttributes.addFlashAttribute("successMessage", "Food  \"" + mf.getFood().getName() + "\" was deleted.");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal/" + mealId + "/edit";
    }

    @GetMapping("/meal/{mealId}/food/{mealFoodId}/edit")
    public String editFoodFromMealGet(@PathVariable("mealId") Long mealId, @PathVariable("mealFoodId") Long id, Model model, RedirectAttributes redirectAttributes) {
        if(!mealFoodService.existsMealFoodByMealIdAndId(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record with id " + mealId);
            return "redirect:/meal";
        }
        try {
            MealDTO mealDTO = mealService.calculateAndReturnMealDto(mealId);
            MealFood mf = mealFoodService.getMealFoodById(id);
            model.addAttribute("mealDTO", mealDTO);
            model.addAttribute("mealFood", mf);
            model.addAttribute("portions", mf.getFood().getPortions());
            return "editMealFood";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal/" + mealId + "/edit";
    }

    @PostMapping("/meal/{mealId}/food/{mealFoodId}/edit")
    public String editFoodFromMealPost(@PathVariable("mealId") Long mealId, @PathVariable("mealFoodId") Long id,
                                       @Valid @ModelAttribute MealFormDTO mealFormDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(!mealFoodService.existsMealFoodByMealIdAndId(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record with id " + mealId);
            return "redirect:/meal";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/meal/" + mealId + "/food/" + id + "/edit";
        }
        try {
            MealFood mf = mealFoodService.updateMealFood(mealId, id, mealFormDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Food " + mf.getFood().getName() + " updated successfully.");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/meal/" + mealId + "/edit";
    }
}
