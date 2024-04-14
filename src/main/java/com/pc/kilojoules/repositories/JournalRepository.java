package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    @Query("select j from Journal j where j.consumedAt = ?1 order by j.mealType")
    List<Journal> findByConsumedAtOrderByMealTypeAsc(LocalDate consumedAt);

    List<Journal> findAllByConsumedAt(LocalDate date);

    Optional<Journal> findJournalByIdAndJournalFoodId(Long journalId, Long journalFoodId);
    Optional<Journal> findJournalByIdAndJournalMealId(Long journalId, Long journalMealId);

    @Query("SELECT j FROM Journal j JOIN j.journalMeal jm JOIN jm.journalMealFoods jmf WHERE j.id = :journalId AND jm.id = :mealId AND jmf.id = :foodId")
    Optional<Journal> checkAllEntitiesExistence(@Param("journalId") Long journalId, @Param("mealId") Long mealId, @Param("foodId") Long foodId);

    List<Journal> findAllByConsumedAtBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT j FROM Journal j WHERE j.journalFood IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalFood.kiloJoules DESC LIMIT 10")
    List<Journal> findTop10ByJournalFoodKiloJoulesAndCreatedAtBetweenOrderByJournalFoodKjDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT j FROM Journal j WHERE j.journalMeal IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalMeal.kiloJoules DESC LIMIT 10")
    List<Journal> findTop10ByJournalMealKiloJoulesAndCreatedAtBetweenOrderByJournalMealKjDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT j FROM Journal j WHERE j.journalFood IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalFood.proteins DESC LIMIT 10")
    List<Journal> findTop10ByJournalFoodProteinsAndCreatedAtBetweenOrderByJournalFoodProteinsDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT j FROM Journal j WHERE j.journalMeal IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalMeal.proteins DESC LIMIT 10")
    List<Journal> findTop10ByJournalMealProteinsAndCreatedAtBetweenOrderByJournalMealProteinsDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT j FROM Journal j WHERE j.journalFood IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalFood.carbohydrates DESC LIMIT 10")
    List<Journal> findTop10ByJournalFoodCarbsAndCreatedAtBetweenOrderByJournalFoodCarbsDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT j FROM Journal j WHERE j.journalMeal IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalMeal.carbohydrates DESC LIMIT 10")
    List<Journal> findTop10ByJournalMealCarbsAndCreatedAtBetweenOrderByJournalMealCarbsDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT j FROM Journal j WHERE j.journalFood IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalFood.fiber DESC LIMIT 10")
    List<Journal> findTop10ByJournalFoodFiberAndCreatedAtBetweenOrderByJournalFoodFiberDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT j FROM Journal j WHERE j.journalMeal IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalMeal.fiber DESC LIMIT 10")
    List<Journal> findTop10ByJournalMealFiberAndCreatedAtBetweenOrderByJournalMealFiberDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT j FROM Journal j WHERE j.journalFood IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalFood.fat DESC LIMIT 10")
    List<Journal> findTop10ByJournalFoodFatAndCreatedAtBetweenOrderByJournalFoodFatDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT j FROM Journal j WHERE j.journalMeal IS NOT NULL AND j.consumedAt BETWEEN :startDate AND :endDate ORDER BY j.journalMeal.fat DESC LIMIT 10")
    List<Journal> findTop10ByJournalMealFatAndCreatedAtBetweenOrderByJournalMealFatDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}