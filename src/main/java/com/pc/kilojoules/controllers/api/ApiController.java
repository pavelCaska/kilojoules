package com.pc.kilojoules.controllers.api;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.models.PortionResponseDTO;
import com.pc.kilojoules.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final FoodService foodService;

    @Autowired
    public ApiController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/getPortions")
    public ResponseEntity<List<PortionResponseDTO>> getPortions(@RequestParam Long foodId) {
        try {
            Food food = foodService.getFoodById(foodId);
            List<PortionResponseDTO> portions = food.getPortions().stream()
                    .map(o -> new PortionResponseDTO(o.getPortionName(), o.getPortionSize(), o.getFood().getId()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(portions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
