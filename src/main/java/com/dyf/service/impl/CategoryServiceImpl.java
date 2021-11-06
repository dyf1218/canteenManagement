package com.dyf.service.impl;

import com.dyf.entity.CategoryInfo;
import com.dyf.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceImpl implements ICategoryService
{
    @Autowired
    ICategoryService iCategoryService;

    @Override
    public CategoryInfo findByCategoryId(String categoryId) {
        return findByCategoryId(categoryId);
    }
}
