package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "journal_meals")
public class JournalMeal implements JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String mealName;

    @Column(nullable = false)
    private Boolean saved = false;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @Column
    private BigDecimal quantity = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "KiloJoules must be greater than or equal to zero")
    private BigDecimal kiloJoules = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Proteins must be greater than or equal to zero")
    private BigDecimal proteins = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Carbohydrates must be greater than or equal to zero")
    private BigDecimal carbohydrates = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Fiber must be greater than or equal to zero")
    private BigDecimal fiber = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Sugar must be greater than or equal to zero")
    private BigDecimal sugar = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Fat must be greater than or equal to zero")
    private BigDecimal fat = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "SAFA must be greater than or equal to zero")
    private BigDecimal safa = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "TFA must be greater than or equal to zero")
    private BigDecimal tfa = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Cholesterol must be greater than or equal to zero")
    private BigDecimal cholesterol = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Sodium must be greater than or equal to zero")
    private BigDecimal sodium = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Calcium must be greater than or equal to zero")
    private BigDecimal calcium = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "PHE must be greater than or equal to zero")
    private BigDecimal phe = BigDecimal.ZERO;

    @OneToMany(mappedBy = "journalMeal", cascade = CascadeType.ALL)
    private Set<JournalMealFood> journalMealFoods;

}
