package com.dyf.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String adminId;

    private String adminPassword;
}
