package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.models.MealDTO;
import com.pc.kilojoules.models.MealFormDTO;
import com.pc.kilojoules.services.FoodService;
import com.pc.kilojoules.services.MealFoodService;
import com.pc.kilojoules.services.MealService;
import com.pc.kilojoules.services.PortionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MealController {

    private final MealService mealService;
    private final FoodService foodService;
    private final MealFoodService mealFoodService;
    private final PortionService portionService;

    public MealController(MealService mealService, FoodService foodService, MealFoodService mealFoodService, PortionService portionService) {
        this.mealService = mealService;
        this.foodService = foodService;
        this.mealFoodService = mealFoodService;
        this.portionService = portionService;
    }

    @GetMapping("/meal/create")
    public String createMealGet(Model model, @ModelAttribute ("mealFormDTO") MealFormDTO mealFormDTO) {
        return "createMealSearch";
    }

    @GetMapping("/meal/{id}/edit")
    public String editMealGet(@PathVariable("id") Long id, Model model) {
        MealDTO mealDTO = mealService.calculateAndReturnMealDto(id);
        model.addAttribute("mealDTO", mealDTO);
        return "editMealSearch";
    }

    @GetMapping("/meal/foodSearch")
    public String searchFoodCreateMeal(@RequestParam(name = "query") String query, RedirectAttributes redirectAttributes) {
            List<Food> foods = foodService.searchFood(query);
            List<Portion> portions = new ArrayList<>();
            if(!foods.isEmpty()) {
                if (foods.size() == 1) {
                    portions = portionService.findPortionsByFood(foods.get(0));
                    redirectAttributes.addFlashAttribute("query", query);
                    redirectAttributes.addFlashAttribute("foods", foods);
                    redirectAttributes.addFlashAttribute("portions", portions);
                    return "redirect:/meal/create";
                } else {
                    redirectAttributes.addFlashAttribute("query", query);
                    redirectAttributes.addFlashAttribute("foods", foods);
                    redirectAttributes.addFlashAttribute("portions", portions);
                    return "redirect:/meal/create";
                }
            }
        redirectAttributes.addFlashAttribute("errorMessage", "Hledaná potravina nebyla nenalezana.");
        return "redirect:/meal/create";
    }

    @GetMapping("/meal/{id}/foodSearch")
    public String searchFoodEditMeal(@PathVariable("id") Long id, @RequestParam(name = "query") String query, RedirectAttributes redirectAttributes) {
        List<Food> foods = foodService.searchFood(query);
        if(!foods.isEmpty()) {
            redirectAttributes.addFlashAttribute("foods", foods);
            redirectAttributes.addFlashAttribute("query", query);
            return "redirect:/meal/" + id + "/edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Hledaná potravina nebyla nalezena!");
            return "redirect:/meal/" + id + "/edit";
        }
    }

    @PostMapping("/meal/create")
    public String saveMeal(@Valid @ModelAttribute MealFormDTO mealFormDTO,
                           BindingResult bindingResult,
                           @RequestParam("foods") List<Long> foods) {

        if (bindingResult.hasErrors()) {
            return "createMealSearch";
        }

        Meal meal = Meal.builder()
                .mealName(mealFormDTO.getMealName())
                .build();
        meal = mealService.save(meal);

        final Meal savedMeal = meal;
        Long mealId = savedMeal.getId();
        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());

        Set<MealFood> mealFoods = foods.stream()
                .map(foodId -> {
                    MealFood mf = new MealFood();
                    mf.setMeal(savedMeal);
                    mf.setQuantity(savedQuantity);
                    Food food = foodService.findById(foodId);
                    mf.setFood(food);
                    return mealFoodService.save(mf);
                })
                .collect(Collectors.toSet());

        meal.setMealFoods(mealFoods);
        return "redirect:/meal/" + mealId + "/edit";
    }

    @PostMapping("/meal/{id}/edit")
    public String editMeal(@PathVariable("id") Long id, @ModelAttribute MealFormDTO mealFormDTO,
                           BindingResult bindingResult,
                           @RequestParam("foods") List<Long> foods) {

        if (bindingResult.hasErrors()) {
            return "redirect:/meal/" + id + "/edit";
        }

        Meal meal = mealService.findMealById(id);
        meal.setMealName(mealFormDTO.getMealName());

        final Meal savedMeal = meal;
        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());

        Set<MealFood> mealFoods = foods.stream()
                .map(foodId -> {
                    MealFood mf = new MealFood();
                    mf.setMeal(savedMeal);
                    mf.setQuantity(savedQuantity);
                    Food food = foodService.findById(foodId);
                    mf.setFood(food);
                    return mealFoodService.save(mf);
                })
                .collect(Collectors.toSet());

        meal.setMealFoods(mealFoods);

        return "redirect:/meal/" + id + "/edit";
    }

    @PostMapping("/meal/{id}/delete")
    public String deleteMeal(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Meal meal = mealService.findMealById(id);
            String mealName = meal.getMealName();
            mealService.deleteMealById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Jídlo \"" + mealName + "\" bylo smazáno!");
            return "redirect:/meal/list";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Jídlo nenalezeno!");
            return "redirect:/meal/list";
        }
    }

    @PostMapping("/meal/{mealId}/food/{id}/delete")
    public String deleteFoodFromMeal(@PathVariable("mealId") Long mealId, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Food food = foodService.findById(id);
            String foodName = food.getName();
            mealFoodService.deleteMealFoodByMealIdAndFoodId(mealId, id);
            redirectAttributes.addFlashAttribute("successMessage", "Potravina  \"" + foodName + "\" byla smazána!");
            return "redirect:/meal/" + mealId + "/edit";

        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Potravina nenalezena!");
            return "redirect:/meal/" + mealId + "/edit";
        }
    }

    @GetMapping("/meal/list")
    public String listMeals(Model model) {
        List<MealDTO> mealsDTO = mealService.calculateAndReturnMealDtoList();
        model.addAttribute("mealsDTO", mealsDTO);
        return "listMeals";
    }
}
