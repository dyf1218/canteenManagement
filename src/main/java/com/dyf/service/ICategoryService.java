package com.dyf.service;

import com.dyf.entity.CategoryInfo;

import java.util.List;

public interface ICategoryService
{
    CategoryInfo findByCategoryId(String categoryId);

    List<CategoryInfo> findAllCategory();

    CategoryInfo save(CategoryInfo categoryInfo);

    CategoryInfo delete(CategoryInfo categoryInfo);
}
