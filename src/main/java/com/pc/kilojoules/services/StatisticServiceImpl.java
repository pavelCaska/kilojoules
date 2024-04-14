package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Journal;
import com.pc.kilojoules.entities.JournalEntry;
import com.pc.kilojoules.models.JournalTotalsDTO;
import com.pc.kilojoules.models.TopTenDTO;
import com.pc.kilojoules.repositories.JournalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final JournalRepository journalRepository;

    public StatisticServiceImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public JournalTotalsDTO calculateJournalTotalsByDate(LocalDate date) {
        JournalTotalsDTO journalTotalsDTO = new JournalTotalsDTO();
        List<Journal> entries = journalRepository.findAllByConsumedAt(date);
        calculateTotals(journalTotalsDTO, entries);
        return journalTotalsDTO;
    }
    @Override
    public JournalTotalsDTO calculateJournalTotalsByPeriod(LocalDate startDate, LocalDate endDate) {
        JournalTotalsDTO journalTotalsDTO = new JournalTotalsDTO();
        List<Journal> entries = journalRepository.findAllByConsumedAtBetween(startDate, endDate);
        calculateTotals(journalTotalsDTO, entries);
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        journalTotalsDTO.setAvgQuantity(journalTotalsDTO.getTotalQuantity().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));
        journalTotalsDTO.setAvgKiloJoules(journalTotalsDTO.getTotalKiloJoules().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));
        journalTotalsDTO.setAvgProteins(journalTotalsDTO.getTotalProteins().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));
        journalTotalsDTO.setAvgCarbohydrates(journalTotalsDTO.getTotalCarbohydrates().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));
        journalTotalsDTO.setAvgFiber(journalTotalsDTO.getTotalFiber().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));
        journalTotalsDTO.setAvgFat(journalTotalsDTO.getTotalFat().divide(BigDecimal.valueOf(numOfDays), RoundingMode.HALF_UP));

        return journalTotalsDTO;
    }

    private void calculateTotals(JournalTotalsDTO journalTotalsDTO, List<Journal> entries){
        entries.forEach(entry -> {
            if (entry.getJournalFood() != null) {
                updateTotalsWithJournalItem(journalTotalsDTO, entry.getJournalFood());
            }
            if (entry.getJournalMeal() != null) {
                updateTotalsWithJournalItem(journalTotalsDTO, entry.getJournalMeal());
            }
        });
    }

    private void updateTotalsWithJournalItem(JournalTotalsDTO journalTotalsDTO, JournalEntry item){
        journalTotalsDTO.setTotalQuantity(journalTotalsDTO.getTotalQuantity().add(item.getQuantity()));
        journalTotalsDTO.setTotalKiloJoules(journalTotalsDTO.getTotalKiloJoules().add(item.getKiloJoules()));
        journalTotalsDTO.setTotalProteins(journalTotalsDTO.getTotalProteins().add(item.getProteins()));
        journalTotalsDTO.setTotalCarbohydrates(journalTotalsDTO.getTotalCarbohydrates().add(item.getProteins()));
        journalTotalsDTO.setTotalFiber(journalTotalsDTO.getTotalFiber().add(item.getFiber()));
        journalTotalsDTO.setTotalFat(journalTotalsDTO.getTotalFat().add(item.getFat()));
    }

    @Override
    public List<TopTenDTO> getTop10ByKiloJoules(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodKiloJoulesAndCreatedAtBetweenOrderByJournalFoodKjDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealKiloJoulesAndCreatedAtBetweenOrderByJournalMealKjDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        return combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setKiloJoules(journal.getJournalFood() != null ? journal.getJournalFood().getKiloJoules() : journal.getJournalMeal().getKiloJoules());
                    return item;
                })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> item.getKiloJoules().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getKiloJoules, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByKiloJoulesCount(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodKiloJoulesAndCreatedAtBetweenOrderByJournalFoodKjDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealKiloJoulesAndCreatedAtBetweenOrderByJournalMealKjDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);
        Map<String, TopTenDTO> itemMap = combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setKiloJoules(journal.getJournalFood() != null ? journal.getJournalFood().getKiloJoules() : journal.getJournalMeal().getKiloJoules());
                    item.setCount(1);
                    return item;
                })
                .collect(Collectors.toMap(
                        TopTenDTO::getName, // key
                        dto -> dto, // value
                        (dto1, dto2) -> { // merge function
                            dto1.setKiloJoules(dto1.getKiloJoules().add(dto2.getKiloJoules()));
                            dto1.setCount(dto1.getCount() + 1);
                            return dto1;
                        }));

        return itemMap.values().stream()
                .filter(item -> item.getKiloJoules().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getKiloJoules, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
    @Override
    public List<TopTenDTO> getTop10ByProteins(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodProteinsAndCreatedAtBetweenOrderByJournalFoodProteinsDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealProteinsAndCreatedAtBetweenOrderByJournalMealProteinsDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        return combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setProteins(journal.getJournalFood() != null ? journal.getJournalFood().getProteins() : journal.getJournalMeal().getProteins());
                    return item;
                })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> item.getProteins().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getProteins, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
    @Override
    public List<TopTenDTO> getTop10ByProteinsCount(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodProteinsAndCreatedAtBetweenOrderByJournalFoodProteinsDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealProteinsAndCreatedAtBetweenOrderByJournalMealProteinsDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);
        Map<String, TopTenDTO> itemMap = combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setProteins(journal.getJournalFood() != null ? journal.getJournalFood().getProteins() : journal.getJournalMeal().getProteins());
                    item.setCount(1);
                    return item;
                })
                .collect(Collectors.toMap(
                        TopTenDTO::getName,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setProteins(dto1.getProteins().add(dto2.getProteins()));
                            dto1.setCount(dto1.getCount() + 1);
                            return dto1;
                        }));
        return itemMap.values().stream()
                .filter(item -> item.getProteins().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getProteins, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByCarbohydrates(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodCarbsAndCreatedAtBetweenOrderByJournalFoodCarbsDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealCarbsAndCreatedAtBetweenOrderByJournalMealCarbsDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        return combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setCarbohydrates(journal.getJournalFood() != null ? journal.getJournalFood().getCarbohydrates() : journal.getJournalMeal().getCarbohydrates());
                    return item;
                })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> item.getCarbohydrates().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getCarbohydrates, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByCarbohydratesCount(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodCarbsAndCreatedAtBetweenOrderByJournalFoodCarbsDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealCarbsAndCreatedAtBetweenOrderByJournalMealCarbsDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);
        Map<String, TopTenDTO> itemMap = combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setCarbohydrates(journal.getJournalFood() != null ? journal.getJournalFood().getCarbohydrates() : journal.getJournalMeal().getCarbohydrates());
                    item.setCount(1);
                    return item;
                })
                .collect(Collectors.toMap(
                        TopTenDTO::getName,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setCarbohydrates(dto1.getCarbohydrates().add(dto2.getCarbohydrates()));
                            dto1.setCount(dto1.getCount() + 1);
                            return dto1;
                        }));
        return itemMap.values().stream()
                .filter(item -> item.getCarbohydrates().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getCarbohydrates, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByFiber(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodFiberAndCreatedAtBetweenOrderByJournalFoodFiberDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealFiberAndCreatedAtBetweenOrderByJournalMealFiberDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        return combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setFiber(journal.getJournalFood() != null ? journal.getJournalFood().getFiber() : journal.getJournalMeal().getFiber());
                    return item;
                })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> item.getFiber().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getFiber, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByFiberCount(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodFiberAndCreatedAtBetweenOrderByJournalFoodFiberDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealFiberAndCreatedAtBetweenOrderByJournalMealFiberDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);
        Map<String, TopTenDTO> itemMap = combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setFiber(journal.getJournalFood() != null ? journal.getJournalFood().getFiber() : journal.getJournalMeal().getFiber());
                    item.setCount(1);
                    return item;
                })
                .collect(Collectors.toMap(
                        TopTenDTO::getName,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setFiber(dto1.getFiber().add(dto2.getFiber()));
                            dto1.setCount(dto1.getCount() + 1);
                            return dto1;
                        }));
        return itemMap.values().stream()
                .filter(item -> item.getFiber().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getFiber, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTenDTO> getTop10ByFat(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodFatAndCreatedAtBetweenOrderByJournalFoodFatDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealFatAndCreatedAtBetweenOrderByJournalMealFatDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        return combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setFat(journal.getJournalFood() != null ? journal.getJournalFood().getFat() : journal.getJournalMeal().getFat());
                    return item;
                })
                .collect(Collectors.toList())
                .stream()
                .filter(item -> item.getFat().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getFat, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }


    @Override
    public List<TopTenDTO> getTop10ByFatCount(LocalDate startDate, LocalDate endDate) {
        List<Journal> combinedList = new ArrayList<>();
        List<Journal> foodList = journalRepository.findTop10ByJournalFoodFatAndCreatedAtBetweenOrderByJournalFoodFatDesc(startDate, endDate);
        List<Journal> mealList = journalRepository.findTop10ByJournalMealFatAndCreatedAtBetweenOrderByJournalMealFatDesc(startDate, endDate);
        combinedList.addAll(foodList);
        combinedList.addAll(mealList);

        Map<String, TopTenDTO> itemMap = combinedList.stream()
                .map(journal -> {
                    TopTenDTO item = new TopTenDTO();
                    item.setFoodId(journal.getJournalFood() != null ? journal.getJournalFood().getId() : null);
                    item.setMealId(journal.getJournalMeal() != null ? journal.getJournalMeal().getId() : null);
                    item.setName(journal.getJournalFood() != null ? journal.getJournalFood().getName() : journal.getJournalMeal().getMealName());
                    item.setFat(journal.getJournalFood() != null ? journal.getJournalFood().getFat() : journal.getJournalMeal().getFat());
                    item.setCount(1);
                    return item;
                })
                .collect(Collectors.toMap(
                        TopTenDTO::getName,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setFat(dto1.getFat().add(dto2.getFat()));
                            dto1.setCount(dto1.getCount() + 1);
                            return dto1;
                        }));
        return itemMap.values().stream()
                .filter(item -> item.getFat().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(TopTenDTO::getFat, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
