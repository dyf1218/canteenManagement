package com.dyf.service;

import com.dyf.entity.CategoryInfo;

public interface ICategoryService
{
    CategoryInfo findByCategoryId(String categoryId);
}
