package com.bpic.web.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bpic.common.core.vo.DictVo;
import com.bpic.common.utils.reponse.Response;
import com.bpic.system.mapper.SysDictDataMapper;
import com.bpic.system.mapper.SysDictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.bpic.common.annotation.Log;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysDictData;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.enums.BusinessType;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.poi.ExcelUtil;
import com.bpic.system.service.ISysDictDataService;
import com.bpic.system.service.ISysDictTypeService;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/web/system/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public AjaxResult export(SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode)
    {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @PostMapping(value = "/type/findByCodes")
    public Response<Map<String,List<DictVo>>> findByCodes(@RequestParam String codes)
    {
        Response<Map<String, List<DictVo>>> response = new Response<Map<String, List<DictVo>>>(Response.SUCCESS,
                "dictionary_success_bycode");
        // 解析codes(,分隔)
        Date start0 = new Date();
        logger.info("[findByCodes方法start]+[]+入参[{}]", codes);
        try {
          response= dictTypeService.findByCodes(codes);
            Date end0 = new Date();
            logger.info("[findByCodes方法end]+[]+[方法耗时：{}秒]+[返回值：{}]", (end0.getTime() - start0.getTime()) / 1000,
                    response);
        } catch (Exception e) {
            logger.error("[findByCodes方法]+[]+[捕获异常]", e);
        }
        return response;
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/findByCode")
    public Response<DictVo> findByCode(@RequestParam String code){
        Response<DictVo> response = new Response<DictVo>(Response.SUCCESS,
                "dictionary_success_bycode");
     try{

        DictVo dict= sysDictDataMapper.selectDictVo(code);
        response.setData(dict);
        } catch (Exception e) {
            logger.error("[findByCodes方法]+[]+[捕获异常]", e);
        }
        return response;
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes)
    {
        return toAjax(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
