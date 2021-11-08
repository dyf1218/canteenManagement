package com.dyf.controller;

import com.dyf.dto.AdminDTO;
import com.dyf.entity.BannerInfo;
import com.dyf.entity.CategoryInfo;
import com.dyf.entity.FoodInfo;
import com.dyf.entity.StudentInfo;
import com.dyf.enums.ResultEnum;
import com.dyf.exception.SellException;
import com.dyf.form.BannerForm;
import com.dyf.form.CategoryForm;
import com.dyf.form.FoodForm;
import com.dyf.form.StudentForm;
import com.dyf.service.*;
import com.dyf.utils.KeyUtil;
import com.dyf.utils.ResultVOUtil;
import com.dyf.utils.TokenProcessor;
import com.dyf.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.dyf.constant.Constant.*;
import static com.dyf.enums.ResultEnum.*;
import static com.dyf.enums.ResultEnum.QUERY_FAIL;
import static com.dyf.enums.ResultEnum.QUERY_SUCCESS;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private IFoodInfoService iFoodInfoService;

    @Autowired
    private IAdministratorInfoService iAdministratorInfoService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IBannerService iBannerService;

    // http://127.0.0.1:8080/canteen/admin/getFoodList
    @GetMapping("/getFoodList")
    public ResultVO getFoodList() {
        List<FoodInfo> foodInfoList = iFoodInfoService.findAll();
        List<FoodInfoVO> foodInfoVOList = new ArrayList<>();

        for (FoodInfo foodInfo : foodInfoList) {
            FoodInfoVO foodInfoVO = new FoodInfoVO();

            BeanUtils.copyProperties(foodInfo, foodInfoVO);

            foodInfoVOList.add(foodInfoVO);
        }

        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), foodInfoVOList);
    }

    // http://127.0.0.1:8080/canteen/admin/addFood
    @PostMapping("/addFood")
    public ResultVO addFood(@RequestBody FoodForm foodForm) {

        FoodInfo foodInfo = new FoodInfo();

//        foodInfo.setFoodId(KeyUtil.genUniqueKey());
//        foodInfo.setFoodPrice(foodForm.getFoodPrice());
//        foodInfo.setFoodName(foodForm.getFoodName());
        foodForm.setFoodId(KeyUtil.genUniqueFoodKey());

        BeanUtils.copyProperties(foodForm, foodInfo);

        iFoodInfoService.save(foodInfo);

        return ResultVOUtil.success(ADD_SUCCESS.getMessage(), foodForm);
    }

    @PostMapping("/editFood")
    public ResultVO editFood(@RequestBody FoodForm foodForm) {
        FoodInfo foodInfo = iFoodInfoService.findById(foodForm.getFoodId());

        if (foodInfo == null) {
            throw new SellException(ResultEnum.FOOD_NOT_EXIST);
            //return ResultVOUtil.editFail();
        }

        BeanUtils.copyProperties(foodForm, foodInfo);

        iFoodInfoService.save(foodInfo);

        return ResultVOUtil.success(EDIT_SUCCESS.getMessage(), foodForm);

    }

    @PostMapping(value = "/getFoodListByName", produces = "application/json")
    public ResultVO getFoodListByName(@RequestParam String foodName) {

        List<FoodInfo> foodInfoList = iFoodInfoService.findByFoodName(foodName);
        if (foodInfoList.isEmpty()) {
            return ResultVOUtil.fail(QUERY_FAIL.getCode(), QUERY_FAIL.getMessage());
        }
        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), foodInfoList);
    }

    @GetMapping("/getStudentList")
    public ResultVO getStudentList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<StudentInfo> studentInfoPage = iStudentService.getStudentPage(pageRequest);

        List<StudentInfoVO> studentInfoVOList = new ArrayList<StudentInfoVO>();

//        for(StudentInfo studentInfo : studentInfoPage){
//            StudentInfoVO studentInfoVO = new StudentInfoVO();
//
//            BeanUtils.copyProperties(studentInfo, studentInfoVO);
//
//            studentInfoVOList.add(studentInfoVO);
//        }

        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), studentInfoPage);
    }

    @PostMapping(value = "/addStudent", produces = "application/json")
    public ResultVO addStudent(@RequestBody StudentForm studentForm) {
        if (studentForm == null) {
            return ResultVOUtil.fail(ADD_FAIL.getCode(), ADD_FAIL.getMessage());
        }
        iStudentService.getStudentList();

        for (StudentInfo studentInfo: iStudentService.getStudentList())
        {
            if (studentInfo.getStudentId() == studentForm.getStudentId()){
                return ResultVOUtil.fail(DUPLICATE_STUDENT_ID.getCode(),DUPLICATE_STUDENT_ID.getMessage());
            };
        }

        StudentInfo studentInfo = new StudentInfo();

        BeanUtils.copyProperties(studentForm, studentInfo);

        return ResultVOUtil.success(ADD_SUCCESS.getMessage(), iStudentService.addStudent(studentInfo));

    }


    @PostMapping(value = "/editStudent", produces = "application/json")
    public ResultVO editStudent(@RequestBody StudentForm studentForm) {
        if (studentForm == null) {
            return ResultVOUtil.fail(EDIT_FAIL.getCode(), EDIT_FAIL.getMessage());
        }


        StudentInfo studentInfo = iStudentService.findByStudentIdUsedByAdmin(studentForm.getStudentId());

        if (studentInfo == null) {
            throw new SellException(STUDENT_NOT_EXIST);
        }

        if (studentForm.getBalance() != null) studentInfo.setBalance(studentForm.getBalance());
        if (studentForm.getStudentName() != null) studentInfo.setStudentName(studentForm.getStudentName());
        if (studentForm.getPassword() != null) studentInfo.setPassword(studentForm.getPassword());

        return ResultVOUtil.success(EDIT_SUCCESS.getMessage(), iStudentService.editStudent(studentInfo));
    }

    @PostMapping(value = "/stuDeposit", produces = "application/json")
    public ResultVO stuDeposit(String studentId, BigDecimal amount) {
        StudentInfo studentInfo = iStudentService.findByStudentIdUsedByAdmin(studentId);

        if (studentInfo == null) {
            throw new SellException(STUDENT_NOT_EXIST);
        }

        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            return ResultVOUtil.fail(DEPOSIT_FAIL.getCode(), DEPOSIT_FAIL.getMessage());
        }

        iStudentService.stuDeposit(studentInfo, amount);

        return ResultVOUtil.success(0, DEPOSIT_SUCCESS.getMessage());
    }

    @PostMapping(value = "/deleteStudent", produces = "application/json")
    public ResultVO deleteStudent(String studentId) {
        try {
            StudentInfo studentInfo = iStudentService.findByStudentIdUsedByAdmin(studentId);
            iStudentService.deleteStudent(studentInfo);
        } catch (SellException sellException) {
            log.error(STUDENT_NOT_EXIST.getMessage());
            return ResultVOUtil.fail(STUDENT_NOT_EXIST.getCode(), STUDENT_NOT_EXIST.getMessage());
        }

        return ResultVOUtil.success(SUCCESS, DELETE_SUCCESS.getMessage());
    }

    @PostMapping(value = "/findStudentByName", produces = "application/json")
    public ResultVO findStudentByName(
            @RequestParam String studentName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<StudentInfo> studentInfoList = iStudentService.findByStudentName(studentName, pageable);

        if (studentInfoList.isEmpty()) {
            return ResultVOUtil.fail(QUERY_FAIL.getCode(), QUERY_FAIL.getMessage());
        }

        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), studentInfoList);
    }

    @PostMapping(value = "/findStudentById", produces = "application/json")
    public ResultVO findStudentById(
            @RequestParam String studentId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<StudentInfo> studentInfoPage = iStudentService.findByStudentId(studentId, pageRequest);

        if (studentInfoPage.isEmpty()) {
            return ResultVOUtil.fail(QUERY_FAIL.getCode(), QUERY_FAIL.getMessage());
        }

        return ResultVOUtil.success(QUERY_SUCCESS.getMessage(), studentInfoPage);
    }

    /**
     * 登录逻辑
     *
     * @param adminId 用户id(学号)
     * @param password  密码
     * @return JSON
     */

    @PostMapping(value = "/login")
    public ResultVO login(
            @RequestParam(value = "adminId") String adminId,
            @RequestParam(value = "password") String password) {

        String token = TokenProcessor.getInstance().makeToken();

        String info = LOGIN_LOGIC;
        log.info(info +
                ": userid = " +
                adminId +
                ", password = " +
                password);

        AdminDTO admin = new AdminDTO();
        try{
            admin = iAdministratorInfoService.findByAdminId(adminId);
        }catch (Exception e){
            return ResultVOUtil.fail(ADMIN_NOT_EXIST.getCode(), ADMIN_NOT_EXIST.getMessage());
        }

        if (admin != null) {
            log.info(admin.toString());
            if (Objects.equals(password, admin.getAdminPassword())) {
                info = LOGIN_SUCCESS.getMessage();
                log.info(info);
                return ResultVOUtil.success(info, admin, token);
            } else {
                info = PASSWORD_MISMATCH.getMessage();
                log.info(info);
                return ResultVOUtil.fail(PASSWORD_MISMATCH.getCode(), info);
            }
        } else {
            info = ADMIN_NOT_EXIST.getMessage();
            log.info(info);
        }

        return ResultVOUtil.fail(ADMIN_NOT_EXIST.getCode(), info);
    }


    @PostMapping(value = "/category/add", produces = "application/json")
    public ResultVO addCategory(@RequestBody CategoryForm categoryForm)
    {
        CategoryInfo categoryInfo = new CategoryInfo();

        categoryInfo.setCategoryId(KeyUtil.genUniqueCategoryKey());

        BeanUtils.copyProperties(categoryForm,categoryInfo);

        iCategoryService.save(categoryInfo);

        return ResultVOUtil.success(ADD_SUCCESS.getCode(),ADD_SUCCESS.getMessage());

    }

    @PostMapping(value = "/category/edit", produces = "application/json")
    public ResultVO editCategory(@RequestBody CategoryForm categoryForm)
    {
        CategoryInfo categoryInfo = iCategoryService.findByCategoryId(categoryForm.getCategoryId());

        if(categoryInfo == null){
            return ResultVOUtil.fail(CATEGORY_NOT_EXIST.getCode(), CATEGORY_NOT_EXIST.getMessage());
        }

        BeanUtils.copyProperties(categoryForm, categoryInfo);

        iCategoryService.save(categoryInfo);

        return ResultVOUtil.success(EDIT_SUCCESS);
    }

    @PostMapping(value = "/category/get", produces = "application/json")
    public ResultVO getCategory()
    {
        /*将类别列表封装入category再传给前端*/
        CategoryInfoVO categoryInfoVO = new CategoryInfoVO();
        categoryInfoVO.setCategoryInfoList(iCategoryService.findAllCategory());

        return ResultVOUtil.success(categoryInfoVO);
    }

    @PostMapping(value = "/category/delete", produces = "application/json")
    public ResultVO deleteCategory(String categoryId)
    {
        CategoryInfo categoryInfo = iCategoryService.findByCategoryId(categoryId);

        if(categoryInfo == null){
            return ResultVOUtil.fail(CATEGORY_NOT_EXIST.getCode(),CATEGORY_NOT_EXIST.getMessage());
        }

        FoodInfo foodInfo = iFoodInfoService.findByFoodCategory(categoryId);

        if (foodInfo == null){
            iCategoryService.delete(categoryInfo);
        }else {
            return ResultVOUtil.fail(DELETE_FAIL.getCode(), DELETE_FAIL.getMessage());
        }
        return ResultVOUtil.success(DELETE_SUCCESS);
    }

    @PostMapping(value = "/banner/add", produces = "application/json")
    public ResultVO addBanner(@RequestBody BannerForm bannerForm)
    {
        BannerInfo bannerInfo = new BannerInfo();

        bannerInfo.setBannerId(KeyUtil.genUniqueBannerKey());

        BeanUtils.copyProperties(bannerForm,bannerInfo);

        iBannerService.save(bannerInfo);

        return ResultVOUtil.success(ADD_SUCCESS.getCode(),ADD_SUCCESS.getMessage());

    }

    @PostMapping(value = "/banner/delete", produces = "application/json")
    public ResultVO deleteBanner(String bannerId)
    {
        BannerInfo bannerInfo = iBannerService.findById(bannerId);

        if(bannerInfo == null){
            return ResultVOUtil.fail(BANNER_NOT_EXIST.getCode(),BANNER_NOT_EXIST.getMessage());
        }

        iBannerService.delete(bannerInfo);
        return ResultVOUtil.success(DELETE_SUCCESS);

    }

    @PostMapping(value = "/banner/edit", produces = "application/json")
    public ResultVO editBanner(@RequestBody BannerForm bannerForm)
    {
        BannerInfo bannerInfo = iBannerService.findById(bannerForm.getBannerId());

        if(bannerInfo == null){
            return ResultVOUtil.fail(BANNER_NOT_EXIST.getCode(), BANNER_NOT_EXIST.getMessage());
        }

        BeanUtils.copyProperties(bannerForm,bannerInfo);

        iBannerService.save(bannerInfo);

        return ResultVOUtil.success(EDIT_SUCCESS);

    }

    @PostMapping(value = "/banner/get", produces = "application/json")
    public ResultVO getBanner()
    {
        /*将类别列表封装后再传给前端*/
        BannerInfoVO bannerInfoVO = new BannerInfoVO();
        bannerInfoVO.setBannerInfoList(iBannerService.findAllBanner());

        return ResultVOUtil.success(bannerInfoVO);
    }

}
