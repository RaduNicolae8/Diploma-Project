package com.diploma.project.dto;

import com.diploma.project.model.Category;
import com.diploma.project.model.MarketplaceUser;
import com.diploma.project.model.ServiceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MarketplaceServiceDTO {
    private Long id;
    private Long price;
    private String name;
    private String description;
    private Boolean isGeneratedBySeller;
    private Timestamp dateOfCreation;
    private Category category;
    private ServiceType serviceType;
    private MarketplaceUser user;
}
