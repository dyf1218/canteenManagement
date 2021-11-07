package com.dyf.form;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Id;

@Data
public class BannerForm
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
