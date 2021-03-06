package com.dyf.controller;

import com.dyf.dto.ChosenFoodDTO;
import com.dyf.dto.OrderDTO;
import com.dyf.dto.StudentDTO;
import com.dyf.entity.CategoryInfo;
import com.dyf.entity.FoodInfo;
import com.dyf.entity.OrderDetail;
import com.dyf.entity.StudentInfo;
import com.dyf.enums.ResultEnum;
import com.dyf.exception.SellException;
import com.dyf.form.CategoryForm;
import com.dyf.form.OrderForm;
import com.dyf.service.ICategoryService;
import com.dyf.service.IFoodInfoService;
import com.dyf.service.IOrderService;
import com.dyf.service.IStudentService;
import com.dyf.utils.ResultVOUtil;
import com.dyf.vo.CategoryInfoVO;
import com.dyf.vo.FoodInfoVO;
import com.dyf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dyf.enums.ResultEnum.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sell")
public class SellController {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IFoodInfoService iFoodInfoService;

    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private ICategoryService iCategoryService;

    // http://127.0.0.1:8080/canteen/sell/foodList
    @CrossOrigin
    @GetMapping("/foodList")
    public ResultVO list() {

        List<FoodInfo> foodInfoList = iFoodInfoService.findUpAll();

        List<FoodInfoVO> foodInfoVOList = new ArrayList<FoodInfoVO>();

        for (FoodInfo foodInfo : foodInfoList) {
            FoodInfoVO foodInfoVO = new FoodInfoVO();

            BeanUtils.copyProperties(foodInfo, foodInfoVO);
            foodInfoVOList.add(foodInfoVO);
        }

        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), foodInfoVOList);
    }

    // http://127.0.0.1:8080/canteen/sell/pay
    @PostMapping(value = "/pay", produces = "application/json")
    public ResultVO pay(@RequestBody OrderForm orderForm) {
        /* ?????????????????????id??????????????????????????? */
        StudentDTO studentDTO = iStudentService.findByStudentId(orderForm.getStudentId());
        StudentInfo studentInfo = iStudentService.findByStudentIdUsedByAdmin(orderForm.getStudentId());

        BigDecimal foodPrice;
        BigDecimal totalPrice = BigDecimal.ZERO;

        /* ?????????????????? */
        for (ChosenFoodDTO chosenFoodDTO : orderForm.getChosenFoodDTOList()) {
            /* ???????????????????????? */
            if (chosenFoodDTO.getFoodId() == null) {
                throw new SellException(ResultEnum.FOOD_NOT_EXIST);
            }

            /* ????????????id?????????????????? */
            foodPrice = iFoodInfoService.findById(chosenFoodDTO.getFoodId()).getFoodPrice();

            totalPrice = foodPrice.multiply(new BigDecimal(chosenFoodDTO.getFoodQuantity())).add(totalPrice);
        }

        /* ???????????????????????? */
        String password = orderForm.getPassword();

        if (!password.equals(studentInfo.getPassword())) {
            return ResultVOUtil.fail(PASSWORD_MISMATCH.getCode(), PASSWORD_MISMATCH.getMessage());
        }

        /* ?????????????????????????????? */
        if (studentDTO.getBalance().compareTo(totalPrice) < 0)     //????????????
        {
            return ResultVOUtil.fail(BALANCE_NOT_EFFICIENT.getCode(), BALANCE_NOT_EFFICIENT.getMessage(), studentDTO);
        }

        /* ???????????? */
        OrderDTO orderDTO = new OrderDTO();

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

        for (ChosenFoodDTO chosenFoodDTO : orderForm.getChosenFoodDTOList()) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setFoodId(chosenFoodDTO.getFoodId());
            orderDetail.setQuantity(chosenFoodDTO.getFoodQuantity());
            orderDetailList.add(orderDetail);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        orderDTO.setStudentId(orderForm.getStudentId());

        iOrderService.create(orderDTO);
        return ResultVOUtil.success(PAY_SUCCESS.getMessage(), iStudentService.pay(totalPrice, orderForm.getStudentId()));        //?????????????????????????????????????????????????????????DTO???????????????id?????????????????????

    }


    @PostMapping(value = "/editFood", produces = "application/json")
    public ResultVO editFood(FoodInfo foodInfo) {
        return ResultVOUtil.success(EDIT_SUCCESS.getMessage(), iFoodInfoService.edit(foodInfo));
    }

    @PostMapping(value = "/category/get", produces = "application/json")
    public ResultVO getCategory() {
        /*????????????????????????category???????????????*/
        CategoryInfoVO categoryInfoVO = new CategoryInfoVO();
        categoryInfoVO.setCategoryInfoList(iCategoryService.findAllCategory());

        return ResultVOUtil.success(categoryInfoVO);
    }

}
