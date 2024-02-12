package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", message = "Amount must be greater than or equal to zero")
    private BigDecimal amount; // denominator or divisor

    @NotNull
    @DecimalMin(value = "0.0", message = "KiloJoules must be greater than or equal to zero")
    private BigDecimal kiloJoules;

    @NotNull
    @DecimalMin(value = "0.0", message = "Proteins must be greater than or equal to zero")
    private BigDecimal proteins;

    @NotNull
    @DecimalMin(value = "0.0", message = "Carbohydrates must be greater than or equal to zero")
    private BigDecimal carbohydrates;

//    @NotNull
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

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "The 'createdAt' date must be in the past or today")
    private Date createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @OneToMany(mappedBy="food", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Portion> portions;
}
