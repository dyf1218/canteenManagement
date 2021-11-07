package com.dyf.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class BannerInfo
{
    private static final long serialVersionUID = 1L;

    @Id
    private String bannerId;

    @Generated
    private Integer bannerNum;

    private String bannerName;

    private String bannerIcon;

    private Integer bannerStatus;
}
