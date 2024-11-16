package com.hospital.core.dto.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HotServiceResponse {
    private String description;
    private String poster;
    private String name;
    private String slug;
    private BigDecimal price;
    private Long pointReward;
    private Long quantityUsed;
}
