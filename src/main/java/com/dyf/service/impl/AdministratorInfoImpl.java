package com.dyf.service.impl;

import com.dyf.dao.IAdministratorInfoDAO;
import com.dyf.dto.AdminDTO;
import com.dyf.entity.AdministratorInfo;
import com.dyf.enums.ResultEnum;
import com.dyf.exception.SellException;
import com.dyf.service.IAdministratorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorInfoImpl implements IAdministratorInfoService
{
    @Autowired
    private IAdministratorInfoDAO iAdministratorInfoDAO;

    @Override
    public AdminDTO findByAdminId(String adminId) {

        AdministratorInfo administratorInfo = iAdministratorInfoDAO.findByAdminId(adminId);

        /** 查询结果为空的处理 */
        if (administratorInfo == null) {
            throw new SellException(ResultEnum.STUDENT_NOT_EXIST);
        }

        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setAdminId(administratorInfo.getAdminId());
        adminDTO.setAdminPassword(administratorInfo.getAdminPassword());

        return adminDTO;
    }
}
