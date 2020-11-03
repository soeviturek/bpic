package com.bpic.web.controller.system;

import com.bpic.common.config.BpicConfig;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.News;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.file.FileUploadUtils;
import com.bpic.mobile.weixin.BASE64DecodedMultipartFile;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuditImageVo;
import com.bpic.system.service.ISysDictDataService;
import com.bpic.system.service.ISysNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/web/news")
public class NewsController extends BaseController {

    private final static Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private ISysNewsService sysNewsService;

    @Value("${bpic.imageUrl}")
    private String imageUrl;

    @GetMapping("/selectNews")
    public AjaxResult selectNews(){
        List<News> newsList = sysNewsService.selectNews();
        return AjaxResult.success(newsList);
    }

    @PostMapping("/updateNews")
    public AjaxResult updateNews(@RequestBody News news){
        logger.info("更新咨询接口start=====================,{}",news);
        try {
            sysNewsService.updateNews(news);
        }catch (Exception e){
            logger.error("更新错误=====================================，{}",e.getMessage());
        }
        logger.info("更新咨询接口end=================================");
       return AjaxResult.success("更新成功");
    }

    /**
     * 更新图片上传
     */
    @RequestMapping("/uploadImage")
    public AjaxResult upload(HttpServletRequest request,
                             @RequestParam("file1")String file1,@RequestParam("address") String address) {
        String contentType = request.getHeader("Content-Type");
        logger.info("===========请求uploadImage开始================");
        logger.info("文件上传接口的contentType:{}", contentType);
        String filePath = BpicConfig.getUploadPath();
        logger.info("文件存储系统路径为:{}", filePath);
        String url ="";
        try {
            String fileNames = FileUploadUtils.upload(filePath, BASE64DecodedMultipartFile.base64ToMultipart(file1));
            logger.info("上传完成后返回的文件名:{}", fileNames);
             url = imageUrl + fileNames;
            logger.info("上传完成保存到服务器的全路径是:{}", url);
        } catch (Exception e) {
            logger.info("图片上传失败,{}", e.getMessage());
            return new AjaxResult(500, "图片上传失败");
        }
        News news=new News();
        news.setTitle(url);
        news.setContent(address);
        try {
            sysNewsService.updateBanner(news);
        }catch (Exception e){
            logger.error("更新banner图错误==================,{}",e.getMessage());
            return AjaxResult.error("更新banner图失败");
        }
       return AjaxResult.success("更新成功");
    }

}
