package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "journal_mealfoods")
public class JournalMealFood implements JournalFoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_meal_id", nullable = false)
    private JournalMeal journalMeal;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0", message = "KiloJoules must be greater than or equal to zero")
    private BigDecimal kiloJoules;

    @NotNull
    @DecimalMin(value = "0.0", message = "Proteins must be greater than or equal to zero")
    private BigDecimal proteins;

    @NotNull
    @DecimalMin(value = "0.0", message = "Carbohydrates must be greater than or equal to zero")
    private BigDecimal carbohydrates;

    @DecimalMin(value = "0.0", message = "Fiber must be greater than or equal to zero")
    private BigDecimal fiber;

    @DecimalMin(value = "0.0", message = "Sugar must be greater than or equal to zero")
    private BigDecimal sugar;

    @NotNull
    @DecimalMin(value = "0.0", message = "Fat must be greater than or equal to zero")
    private BigDecimal fat;

    @DecimalMin(value = "0.0", message = "SAFA must be greater than or equal to zero")
    private BigDecimal safa;

    @DecimalMin(value = "0.0", message = "TFA must be greater than or equal to zero")
    private BigDecimal tfa;

    @DecimalMin(value = "0.0", message = "Cholesterol must be greater than or equal to zero")
    private BigDecimal cholesterol;

    @DecimalMin(value = "0.0", message = "Sodium must be greater than or equal to zero")
    private BigDecimal sodium;

    @DecimalMin(value = "0.0", message = "Calcium must be greater than or equal to zero")
    private BigDecimal calcium;

    @DecimalMin(value = "0.0", message = "PHE must be greater than or equal to zero")
    private BigDecimal phe;

    @OneToMany(mappedBy="journalMealFood", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<JournalMealFoodPortion> portions;
}
