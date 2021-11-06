package com.dyf.dto;

import lombok.Data;

@Data
public class CategoryDTO
{
    private static final long serialVersionUID = 1L;

    private String categoryId;

    private Integer categoryNum;

    private String categoryName;

    private Integer ifShow;
}
