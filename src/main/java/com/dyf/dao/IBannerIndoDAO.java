package com.dyf.dao;

import com.dyf.entity.BannerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBannerIndoDAO extends JpaRepository<BannerInfo,String>
{
    BannerInfo findByBannerId(String bannerId);

    List<BannerInfo> findAll();
}
