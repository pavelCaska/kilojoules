package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Builder.Default
    @NotNull
    @DecimalMin(value = "100.0", message = "Quantity must be 100")
    private BigDecimal quantity = new BigDecimal("100"); // denominator or divisor

    @NotNull
    @DecimalMin(value = "0.0", message = "KiloJoules must be greater than or equal to zero")
    private BigDecimal kiloJoules;

    @NotNull
    @DecimalMin(value = "0.0", message = "Proteins must be greater than or equal to zero")
    private BigDecimal proteins;

    @NotNull
    @DecimalMin(value = "0.0", message = "Carbohydrates must be greater than or equal to zero")
    private BigDecimal carbohydrates;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "Fiber must be greater than or equal to zero")
    private BigDecimal fiber = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "Sugar must be greater than or equal to zero")
    private BigDecimal sugar = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0", message = "Fat must be greater than or equal to zero")
    private BigDecimal fat;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "SAFA must be greater than or equal to zero")
    private BigDecimal safa = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "TFA must be greater than or equal to zero")
    private BigDecimal tfa = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "Cholesterol must be greater than or equal to zero")
    private BigDecimal cholesterol = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "Sodium must be greater than or equal to zero")
    private BigDecimal sodium = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "Calcium must be greater than or equal to zero")
    private BigDecimal calcium = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @DecimalMin(value = "0.0", message = "PHE must be greater than or equal to zero")
    private BigDecimal phe = BigDecimal.ZERO;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;


    @Builder.Default
    @OneToMany(mappedBy="food", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Portion> portions = new ArrayList<>();
}
