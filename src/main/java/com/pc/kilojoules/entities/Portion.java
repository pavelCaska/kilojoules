package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "portions")
public class Portion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank
    @Size(max = 55)
    private String portionName;

    @NonNull
    @NotNull
    @DecimalMin(value = "0.0", message = "Size must be greater than or equal to zero")
    private BigDecimal portionSize;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

}
