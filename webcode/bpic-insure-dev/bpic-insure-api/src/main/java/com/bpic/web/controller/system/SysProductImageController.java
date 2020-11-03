package com.bpic.web.controller.system;

import com.bpic.common.config.BpicConfig;
import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.News;
import com.bpic.common.core.domain.entity.ProductImage;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.DateUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.file.FileUploadUtils;
import com.bpic.common.utils.file.FtpUtils;
import com.bpic.common.utils.uuid.IdUtils;
import com.bpic.mobile.weixin.BASE64DecodedMultipartFile;
import com.bpic.system.service.ISysProductImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.bpic.common.utils.file.FileUploadUtils.extractFilename;
import static com.bpic.common.utils.file.FileUploadUtils.getExtension;

@RestController
@RequestMapping("/web/system/productImage")
public class SysProductImageController extends BaseController {
    private final static Logger log = LoggerFactory.getLogger(SysProductImageController.class);

    @Autowired
    private ISysProductImageService sysProductImageService;

    @Value("${bpic.images}")
    private String images;

    /**
     * 查询产品图片接口
     *
     * @param map
     * @return
     */
    @PostMapping("/selectProductImageList")
    public TableDataInfo selectProductImageList(@RequestBody Map<String, String> map) {
        log.info("查询产品图片信息接口start==================================,{}", map);
        startPage();
        List<ProductImage> productImageList = sysProductImageService.selectByConditions(map);
        log.info("查询产品图片接口end============================");
        return getDataTable(productImageList);
    }

    @PostMapping("/uploadImage")
    public AjaxResult uploadImage(HttpServletRequest request,
                                  @RequestParam("file") MultipartFile file){
        log.info("修改图片信息接口start=================================,request:{},file:{}", request, file);
        String contentType = request.getHeader("Content-Type");
        log.info("===========请求uploadImage开始================");
        log.info("文件上传接口的contentType:{}", contentType);
        // 获取上传文件路径
        String filePath = BpicConfig.getUploadPath();
        log.info("文件存储系统路径为:{}", filePath);
        String url = "";
        try {
            InputStream inputStream = file.getInputStream();
//            String originalFilename = file.getOriginalFilename();
            filePath = filePath+"/"+DateUtils.datePath();
            String extension = getExtension(file);
            String fileName= IdUtils.fastUUID() + "." + extension;
            log.info("路径============,{},{}",filePath,fileName);
            FtpUtils.uploadFile(inputStream, filePath, fileName);
            FtpUtils.close();
            log.info("上传完成后返回的文件名:{}", fileName);
            url = images + DateUtils.datePath()+"/"+fileName;
            log.info("上传完成保存到服务器的全路径是:{}", url);
            //将返回的全路径放进map
        } catch (Exception e) {
            logger.info("图片上传失败,{}", e.getMessage());
            return new AjaxResult(500, "图片上传失败");
        }
        return AjaxResult.success(url);
    }


    @PostMapping("/insertOrUpdateProductImage")
    public AjaxResult updateProductImage(@RequestBody ProductImage productImage) {
        try {
            log.info("记录productImage的id值=======================，id:{}",productImage.getProductCode());
            if(StringUtils.isEmpty(productImage.getImage())){
                return new AjaxResult(HttpStatus.BAD_REQUEST,"上传图片不能为空!!!");
            }
            if(StringUtils.isEmpty(productImage.getProductCode())){
                return new AjaxResult(HttpStatus.BAD_REQUEST,"产品代码不能为空!!!");
            }
            if (sysProductImageService.selectByProductCode(productImage.getProductCode())==null) {
                sysProductImageService.insertProductImage(productImage);
            }else {
                sysProductImageService.updateProductImage(productImage.getProductCode(), productImage.getImage());
            }
        } catch (Exception e) {
            log.error("更新图片失败=========================,{}", e.getMessage());
            return AjaxResult.error("更新图片失败");
        }
        return AjaxResult.success();
    }


    /**
     * 更新图片上传
     */
    @RequestMapping("/customizedUploadImage")
    public AjaxResult upload(HttpServletRequest request,
                             @RequestParam("file1") MultipartFile file1) {
        //String file1
        //request对象，获取请求头content type
        String contentType = request.getHeader("Content-Type");
        logger.info("===========请求uploadImage开始================");
        logger.info("文件上传接口的contentType:{}", contentType);

        //获取上传到服务器的路径,getter方法
        String filePath = "/Test";
        logger.info("文件存储系统路径为:{}", filePath);
        String url ="";
        try {
            //获取上传完成后的文件名   参数为file path和一个file
            String fileNames = FileUploadUtils.upload(filePath, file1);
//            String fileNames = FileUploadUtils.upload(filePath, file1);


            logger.info("上传完成后返回的文件名:{}", fileNames);
            //url = ${bpic.imageUrl}
//            url = imageUrl + fileNames;
//            logger.info("上传完成保存到服务器的全路径是:{}", url);
        } catch (Exception e) {
            logger.info("图片上传失败,{}", e.getMessage());
            return new AjaxResult(500, "图片上传失败");
        }
        return new AjaxResult(200,"图片上传成功");
    }

}
