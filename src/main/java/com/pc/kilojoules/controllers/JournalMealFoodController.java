package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Journal;
import com.pc.kilojoules.entities.JournalMeal;
import com.pc.kilojoules.entities.JournalMealFood;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.models.JournalMealFoodFormDTO;
import com.pc.kilojoules.models.UnsavedMealFormDTO;
import com.pc.kilojoules.services.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JournalMealFoodController {

    private final JournalService journalService;
    private final JournalMealService journalMealService;
    private final JournalMealFoodService journalMealFoodService;
    private static final Logger log = LoggerFactory.getLogger(JournalMealFoodController.class);

    @Autowired
    public JournalMealFoodController(JournalService journalService, JournalMealService journalMealService, JournalMealFoodService journalMealFoodService) {
        this.journalService = journalService;
        this.journalMealService = journalMealService;
        this.journalMealFoodService = journalMealFoodService;
    }


    @GetMapping("/journal/{journalId}/meal/{journalMealId}/food/add")
    public String addJournalMealFoodGet(@PathVariable("journalId") Long journalId, @PathVariable("journalMealId") Long id,
                                        Model model, RedirectAttributes redirectAttributes,
                                        @ModelAttribute("formDTO") UnsavedMealFormDTO formDTO) {
        if(!journalService.existsJournalByIdAndJournalMealId(journalId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Meal record with id " + id + " is not associated with Journal record with id " + journalId);
            return "redirect:/journal";
        }
        try {
            Journal entry = journalService.getJournalEntryById(journalId);
            model.addAttribute("entry", entry);
            return "addJournalMealFood";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @PostMapping("/journal/{journalId}/meal/{journalMealId}/food/add")
    public String addJournalMealFoodPost(@PathVariable("journalId") Long journalId, @PathVariable("journalMealId") Long mealId,
                                         RedirectAttributes redirectAttributes,
                                         @Valid @ModelAttribute("formDTO") UnsavedMealFormDTO formDTO, BindingResult bindingResult,
                                         @RequestParam(name = "foods") Long id) {
        if(!journalService.existsJournalByIdAndJournalMealId(journalId, mealId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Meal record with id " + mealId + " is not associated with Journal record with id " + journalId);
            return "redirect:/journal";
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/" + journalId + "/meal/" + mealId + "/edit";
        }
        try {
            String foodName = journalService.addFoodToJournalMeal(journalId, id, formDTO.getQuantity(), formDTO.getPortionSize());
            redirectAttributes.addFlashAttribute("successMessage", "Food " + foodName + " added successfully");
            return "redirect:/journal/" + journalId + "/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @GetMapping("/journal/meal/{mealId}/food/add")
    public String addFoodToNewMealToJournalGet(@PathVariable("mealId") Long id, Model model,
                                               @ModelAttribute("formDTO") UnsavedMealFormDTO formDTO,
                                               RedirectAttributes redirectAttributes) {
        try {
            JournalMeal journalMeal = journalMealService.getJournalMealById(id);
            model.addAttribute("jm", journalMeal);

            return "addJmfToNewMealToJournal";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal/search";
    }


    @PostMapping("/journal/meal/{mealId}/food/add")
    public String addFoodToNewMealToJournalPost(@PathVariable("mealId") Long mealId,
                                                RedirectAttributes redirectAttributes,
                                                @Valid @ModelAttribute("formDTO") UnsavedMealFormDTO formDTO, BindingResult bindingResult,
                                                @RequestParam(name = "foods") Long id) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/meal/" + mealId + "/edit";
        }
        try {
            String foodName = journalMealService.addFoodToUnsavedJournalMeal(mealId, id, formDTO.getQuantity(), formDTO.getPortionSize());
            redirectAttributes.addFlashAttribute("successMessage", "Food " + foodName + " added successfully");

            return "redirect:/journal/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @GetMapping("/journal/{journalId}/meal/{journalMealId}/food/{journalMealFoodId}/edit")
    public String editJournalMealFoodGet(@PathVariable("journalId") Long journalId,
                                         @PathVariable("journalMealId") Long mealId,
                                         @PathVariable("journalMealFoodId") Long id,
                                         @ModelAttribute("formDTO") JournalMealFoodFormDTO formDTO,
                                         Model model, RedirectAttributes redirectAttributes) {
        if(!journalService.doAllEntitiesExist(journalId, mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId + " and Journal record with id " + journalId);
            return "redirect:/journal";
        }
        try {
            Journal entry = journalService.getJournalEntryById(journalId);
            JournalMealFood jmf = journalMealFoodService.getJournalMealFoodById(id);
            model.addAttribute("entry", entry);
            model.addAttribute("jmf", jmf);
            formDTO.setFoodName(jmf.getName());
            model.addAttribute("formDTO", formDTO);

            return "editJournalMealFood";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @PostMapping("/journal/{journalId}/meal/{journalMealId}/food/{journalMealFoodId}/edit")
    public String editJournalMealFoodPost(@PathVariable("journalId") Long journalId, @PathVariable("journalMealId") Long mealId, @PathVariable("journalMealFoodId") Long id,
                                          RedirectAttributes redirectAttributes,
                                          @Valid @ModelAttribute("formDTO") JournalMealFoodFormDTO formDTO, BindingResult bindingResult) {
        if(!journalService.doAllEntitiesExist(journalId, mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId + " and Journal record with id " + journalId);
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/" + journalId + "/meal/" + mealId + "/food/" + id + "/edit";
        }
        try {
            String foodName = journalService.updateJournalMealWithJmf(journalId, id, formDTO.getQuantity(), formDTO.getPortionSize(), formDTO.getFoodName());
            redirectAttributes.addFlashAttribute("successMessage", "Food " + foodName + " changed successfully");
            return "redirect:/journal/" + journalId + "/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @PostMapping("/journal/{journalId}/meal/{journalMealId}/food/{journalMealFoodId}/delete")
    public String deleteJournalMealFood(@PathVariable("journalId") Long journalId,
                                        @PathVariable("journalMealId") Long mealId,
                                        @PathVariable("journalMealFoodId") Long id,
                                        RedirectAttributes redirectAttributes) {
        if(!journalService.doAllEntitiesExist(journalId, mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId + " and Journal record with id " + journalId);
        }
        try {
            String foodName = journalService.deleteJmfByIdAndJournalId(id, journalId);
            redirectAttributes.addFlashAttribute("successMessage", "Food " + foodName + " deleted successfully");

            return "redirect:/journal/" + journalId + "/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @PostMapping("/journal/meal/{journalMealId}/food/{journalMealFoodId}/delete")
    public String deleteJournalMealFoodForNewMeal(@PathVariable("journalMealId") Long mealId,
                                                  @PathVariable("journalMealFoodId") Long id,
                                                  RedirectAttributes redirectAttributes) {
        if(!journalMealService.doJournalMealAndJmfExist(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId);
            return "redirect:/journal/meal/" + mealId + "/edit";
        }
        try {
            JournalMealFood jmf = journalMealFoodService.deleteJmfByIdAndMealId(id, mealId);
            redirectAttributes.addFlashAttribute("successMessage", "Food " + jmf.getName() + " deleted successfully");

            return "redirect:/journal/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }
    @GetMapping("/journal/meal/{journalMealId}/food/{journalMealFoodId}/edit")
    public String editJournalMealFoodForUnsavedMealGet(@PathVariable("journalMealId") Long mealId,
                                                  @PathVariable("journalMealFoodId") Long id,
                                                  Model model,
                                                  @ModelAttribute JournalMealFoodFormDTO formDTO,
                                                  RedirectAttributes redirectAttributes) {
        if(!journalMealService.doJournalMealAndJmfExist(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId);
            return "redirect:/journal/meal/" + mealId + "/edit";
        }
        try {
            JournalMeal journalMeal = journalMealService.getJournalMealById(mealId);
            JournalMealFood journalMealFood = journalMealFoodService.getJournalMealFoodById(id);
            formDTO.setFoodName(journalMealFood.getName());
            model.addAttribute("jm", journalMeal);
            model.addAttribute("jmf", journalMealFood);
            model.addAttribute("formDTO", formDTO);

            return "editJmfToNewMealToJournal";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }
    @PostMapping("/journal/meal/{journalMealId}/food/{journalMealFoodId}/edit")
    public String editJournalMealFoodForUnsavedMealPost(@PathVariable("journalMealId") Long mealId,
                                                  @PathVariable("journalMealFoodId") Long id,
                                                  @ModelAttribute JournalMealFoodFormDTO formDTO, BindingResult bindingResult,
                                                  RedirectAttributes redirectAttributes) {
        if(!journalMealService.doJournalMealAndJmfExist(mealId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Food record with id " + id + " is not associated with Meal record " + mealId);
            return "redirect:/journal/meal/" + mealId + "/edit";
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/meal/" + mealId + "/food/" + id + "/edit";
        }

        try {
            String foodName = journalMealService.updateUnsavedJournalMealWithJmf(mealId, id, formDTO.getQuantity(), formDTO.getPortionSize(), formDTO.getFoodName());
            redirectAttributes.addFlashAttribute("successMessage", "Food " + foodName + " updated successfully.");
            return "redirect:/journal/meal/" + mealId + "/edit";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }
}
