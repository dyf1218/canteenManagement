package com.dyf.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CategoryInfo {
    private static final long serialVersionUID = 1L;

    @Id
    private String categoryId;

    private Integer categoryNum;

    private String categoryName;

    private Integer categoryStatus;

}
