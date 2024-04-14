package com.pc.kilojoules.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "journal_foods")
public class JournalFood implements JournalEntry, JournalFoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", message = "Amount must be greater than or equal to zero")
    private BigDecimal quantity; // denominator or divisor

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

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @OneToMany(mappedBy="journalFood", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<JournalFoodPortion> portions;

}
