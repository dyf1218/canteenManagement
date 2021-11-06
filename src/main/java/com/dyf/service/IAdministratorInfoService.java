package com.dyf.service;

import com.dyf.dto.AdminDTO;
import com.dyf.entity.AdministratorInfo;
import com.dyf.service.impl.AdministratorInfoImpl;

public interface IAdministratorInfoService
{
    AdminDTO findByAdminId(String adminId);

}
