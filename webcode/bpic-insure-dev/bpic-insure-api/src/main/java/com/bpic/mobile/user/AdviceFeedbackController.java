package com.bpic.mobile.user;

import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.domain.AdviceFeedback;
import com.bpic.system.service.AdviceFeedbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 意见反馈
 * xyy
 * 2020/9/7
 */
@RestController
@RequestMapping("/api/adviceFeedback")
public class AdviceFeedbackController extends BaseController {

    @Resource
    AdviceFeedbackService adviceFeedbackService;

    /**
     * 提交意见审核
     * @param adviceFeedback
     * @return AjaxResult
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody AdviceFeedback adviceFeedback){
        if(adviceFeedback == null){
            return new AjaxResult(400,"意见反馈对象不能为空");
        }
        adviceFeedbackService.save(adviceFeedback);
        return new AjaxResult(200,"意见反馈提交成功");
    }

    /**
     * 查询单个意见反馈
     * @param cellphone
     * @return AjaxResult
     */
    @GetMapping("/queryOne")
    public AjaxResult queryOne(@RequestParam("cellphone") String cellphone){
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"联系方式不能为空");
        }
       return new AjaxResult(200,"查询成功",adviceFeedbackService.queryOne(cellphone));
    }

    /**
     * 根据意见类型查询意见列表
     * @return AjaxResult
     */
    @GetMapping("/queryList")
    public AjaxResult queryList(@RequestParam("advice_type")String advice_type){
        startPage();
        List<AdviceFeedback> adviceFeedbacks = adviceFeedbackService.queryList(advice_type);
        TableDataInfo dataTable = getDataTable(adviceFeedbacks);
        return new AjaxResult(200,"查询成功",dataTable);
    }
}
