package com.dyf.service.impl;

import com.dyf.dao.IBannerIndoDAO;
import com.dyf.entity.BannerInfo;
import com.dyf.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements IBannerService
{
    @Autowired
    private IBannerIndoDAO iBannerIndoDAO;

    @Override
    public BannerInfo findById(String bannerId) {
        return iBannerIndoDAO.findByBannerId(bannerId);
    }

    @Override
    public BannerInfo save(BannerInfo bannerInfo) {
        return iBannerIndoDAO.save(bannerInfo);
    }

    @Override
    public BannerInfo delete(BannerInfo bannerInfo) {
        iBannerIndoDAO.delete(bannerInfo);
        return bannerInfo;
    }

    @Override
    public List<BannerInfo> findAllBanner() {
        return iBannerIndoDAO.findAll();
    }
}
