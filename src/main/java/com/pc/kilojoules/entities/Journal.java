package com.pc.kilojoules.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Consumption date cannot be null")
    private LocalDate consumedAt;

    @Enumerated(EnumType.ORDINAL)
    private MealType mealType;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="journal_food_id", referencedColumnName="id")
    private JournalFood journalFood;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "journal_meal_id", referencedColumnName="id")
    private JournalMeal journalMeal;

}
