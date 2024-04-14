package com.pc.kilojoules.controllers;

import com.pc.kilojoules.entities.Journal;
import com.pc.kilojoules.entities.MealType;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class JournalController {

    private final JournalService journalService;
    private final StatisticService statisticService;

    private static final Logger log = LoggerFactory.getLogger(JournalController.class);

    @Autowired
    public JournalController(JournalService journalService, StatisticService statisticService) {
        this.journalService = journalService;
        this.statisticService = statisticService;
    }


    @GetMapping({"/","/journal"})
    public String showJournalForDate(Model model, @RequestParam(name = "date", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        model.addAttribute("entriesB", journalService.getAllJournalEntriesByDateAndMealType(date, MealType.BREAKFAST));
        model.addAttribute("entriesM", journalService.getAllJournalEntriesByDateAndMealType(date, MealType.MID_MORNING_SNACK));
        model.addAttribute("entriesL", journalService.getAllJournalEntriesByDateAndMealType(date, MealType.LUNCH));
        model.addAttribute("entriesA", journalService.getAllJournalEntriesByDateAndMealType(date, MealType.AFTERNOON_SNACK));
        model.addAttribute("entriesD", journalService.getAllJournalEntriesByDateAndMealType(date, MealType.DINNER));
        model.addAttribute("date", date);
        model.addAttribute("previousDate", date.minusDays(1).toString());
        model.addAttribute("nextDate", date.plusDays(1).toString());
        model.addAttribute("dayTotals", statisticService.calculateJournalTotalsByDate(date));
        return "journal";
    }

    @GetMapping("/journal/statistics")
    public String showStatistics(Model model,
                                 @RequestParam(name = "startDate", defaultValue = "#{T(java.time.LocalDate).now().minusDays(2)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam(name = "endDate", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("periodTotals", statisticService.calculateJournalTotalsByPeriod(startDate, endDate));
        model.addAttribute("topKiloJoules", statisticService.getTop10ByKiloJoules(startDate, endDate));
        model.addAttribute("topKiloJoulesCount", statisticService.getTop10ByKiloJoulesCount(startDate, endDate));
        model.addAttribute("topProteins", statisticService.getTop10ByProteins(startDate, endDate));
        model.addAttribute("topProteinsCount", statisticService.getTop10ByProteinsCount(startDate, endDate));
        model.addAttribute("topCarbs", statisticService.getTop10ByCarbohydrates(startDate, endDate));
        model.addAttribute("topCarbsCount", statisticService.getTop10ByCarbohydratesCount(startDate, endDate));
        model.addAttribute("topFiber", statisticService.getTop10ByFiber(startDate, endDate));
        model.addAttribute("topFiberCount", statisticService.getTop10ByFiberCount(startDate, endDate));
        model.addAttribute("topFat", statisticService.getTop10ByFat(startDate, endDate));
        model.addAttribute("topFatCount", statisticService.getTop10ByFatCount(startDate, endDate));

        return "journalStatistics";
    }

    @PostMapping("/journal/{id}/delete")
    public String deleteJournalEntry(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Journal journal = journalService.deleteJournal(id);
            redirectAttributes.addFlashAttribute("successMessage", "Journal entry " + (journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName()) + " deleted successfully");
            return "redirect:/journal";
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataAccessException e) {
            log.error("Database access error:", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Data access error");
        }
        return "redirect:/journal";
    }
}
