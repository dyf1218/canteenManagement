package com.dyf.form;

import com.dyf.entity.CategoryInfo;
import lombok.Data;

import java.util.List;

@Data
public class CategoryForm {

    private static final long serialVersionUID = 1L;

    private String categoryId;

    private Integer categoryNum;

    private String categoryName;

    private Integer categoryStatus;
}
