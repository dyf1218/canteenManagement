package com.dyf.service.impl;

import com.dyf.dao.ICategoryInfoDAO;
import com.dyf.entity.CategoryInfo;
import com.dyf.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService
{
    @Autowired
    ICategoryInfoDAO iCategoryInfoDAO;

    @Override
    public CategoryInfo findByCategoryId(String categoryId) {
        return iCategoryInfoDAO.findByCategoryId(categoryId);
    }

    @Override
    public List<CategoryInfo> findAllCategory() {
        return iCategoryInfoDAO.findAll();
    }

    @Override
    public CategoryInfo save(CategoryInfo categoryInfo) {
        return iCategoryInfoDAO.save(categoryInfo);
    }

    @Override
    public CategoryInfo delete(CategoryInfo categoryInfo) {

        iCategoryInfoDAO.delete(categoryInfo);
        return categoryInfo;
    }
}
