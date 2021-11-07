package com.dyf.dao;

import com.dyf.entity.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryInfoDAO extends JpaRepository<CategoryInfo,String>
{
    CategoryInfo findByCategoryId(String categoryId);

    List<CategoryInfo> findAll();

}
