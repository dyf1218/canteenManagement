package com.dyf.service.impl;

import com.dyf.entity.CategoryInfo;
import com.dyf.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService
{
    @Autowired
    ICategoryService iCategoryService;

    @Override
    public CategoryInfo findByCategoryId(String categoryId) {
        return findByCategoryId(categoryId);
    }

    @Override
    public List<CategoryInfo> findAllCategory() {
        return null;
    }
}
