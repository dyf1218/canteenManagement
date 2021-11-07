package com.dyf.service;

import com.dyf.entity.BannerInfo;

import java.util.List;

public interface IBannerService
{
    BannerInfo findById (String bannerId);

    BannerInfo save(BannerInfo bannerInfo);

    BannerInfo delete(BannerInfo bannerInfo);

    List<BannerInfo> findAllBanner();
}
