package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Journal;
import com.pc.kilojoules.entities.JournalMeal;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.models.JournalMealFormDTO;
import com.pc.kilojoules.services.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class JournalMealController {

    private final MealService mealService;
    private final JournalService journalService;
    private final JournalMealService journalMealService;
    private static final Logger log = LoggerFactory.getLogger(JournalMealController.class);

    @Autowired
    public JournalMealController(MealService mealService, JournalService journalService, JournalMealService journalMealService) {
        this.mealService = mealService;
        this.journalService = journalService;
        this.journalMealService = journalMealService;
    }

    @PostMapping("/journal/meal/{mealId}/add")
    public String addMealToJournalPost(@PathVariable("mealId") Long id,
                                       @ModelAttribute("formDTO") JournalMealFormDTO formDTO,
                                       Model model, RedirectAttributes redirectAttributes) {
        try {
            Meal meal = mealService.getMealById(id);
            JournalMeal journalMeal = journalMealService.convertMealToUnsavedJournalMeal(meal);
            model.addAttribute("jm", journalMeal);
            formDTO.setDate(LocalDate.now());
            formDTO.setMealName(journalMeal.getMealName());
            model.addAttribute("formDTO", formDTO);

            return "addMealToJournal";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal/search";
    }
    @GetMapping("/journal/meal/{journalMealId}/edit")
    public String editNewMealToJournalGet(@PathVariable("journalMealId") Long id,
                                          @ModelAttribute("formDTO") JournalMealFormDTO formDTO,
                                          Model model, RedirectAttributes redirectAttributes) {
        try {
            JournalMeal jm = journalMealService.getJournalMealById(id);
            formDTO.setDate(LocalDate.now());
            formDTO.setMealName(jm.getMealName());
            model.addAttribute("jm", jm);
            return "addMealToJournal";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal/search";
    }

    @PostMapping("/journal/meal/{journalMealId}/edit")
    public String editNewMealToJournalPost(@PathVariable("journalMealId") Long id, RedirectAttributes redirectAttributes,
                                           @Valid @ModelAttribute("formDTO") JournalMealFormDTO formDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/meal/" + id + "/edit";
        }
        try {
            Journal journal = journalService.createJournalAndJournalMeal(id, formDTO);
            redirectAttributes.addFlashAttribute("successMessage", journal.getJournalMeal().getMealName() + " added successfully.");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @GetMapping("/journal/{journalId}/meal/{journalMealId}/edit")
    public String editJournalMealGet(@PathVariable("journalId") Long journalId, @PathVariable("journalMealId") Long id,
                                     @ModelAttribute("formDTO") JournalMealFormDTO formDTO,
                                     Model model, RedirectAttributes redirectAttributes) {
        if(!journalService.existsJournalByIdAndJournalMealId(journalId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Meal record with id " + id + " is not associated with Journal record with id " + journalId);
            return "redirect:/journal";
        }
        try {
            Journal entry = journalService.getJournalEntryById(journalId);
            model.addAttribute("entry", entry);
            model.addAttribute("jm", entry.getJournalMeal());
            formDTO.setDate(entry.getConsumedAt());
            formDTO.setMealType(entry.getMealType().toString());
            formDTO.setMealName(entry.getJournalMeal().getMealName());
            model.addAttribute("formDTO", formDTO);

            return "editJournalMeal";

        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }

    @PostMapping("/journal/{journalId}/meal/{journalMealId}/edit")
    public String editJournalMealPost(@PathVariable("journalId") Long journalId, @PathVariable("journalMealId") Long id,
                                      @Valid @ModelAttribute("formDTO") JournalMealFormDTO formDTO, BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if(!journalService.existsJournalByIdAndJournalMealId(journalId, id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Meal with id " + id + " is not associated with Journal with id " + journalId);
            return "redirect:/journal";
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed!");
            return "redirect:/journal/" + journalId + "meal/" + id + "/edit";
        }

        try {
            Journal journal = journalService.updateJournalWithMeal(journalId, formDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Meal " + journal.getJournalMeal().getMealName() + " updated successfully.");
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }
}
