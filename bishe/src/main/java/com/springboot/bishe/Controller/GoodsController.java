package com.springboot.bishe.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.bishe.common.AppFileUtils;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Goods;
import com.springboot.bishe.domain.Provider;
import com.springboot.bishe.service.GoodsService;
import com.springboot.bishe.service.ProviderService;
import com.springboot.bishe.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: GoodsController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/4/17 19:48
 * @author: 浪漫
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ProviderService providerService;

    /**
     * 查询所有的商品
     */
    @RequestMapping("loadAllGoods")
    public DataGridView loadAllGoods(GoodsVo goodsVo) {
        IPage<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());
        this.goodsService.page(page, queryWrapper);
        List<Goods> records = page.getRecords();
        for (Goods goods : records) {
            Provider provider = this.providerService.getById(goods.getProviderid());
            if(null!=provider) {
                goods.setProvidername(provider.getProvidername());
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 添加
     */
    @RequestMapping("addGoods")
    public DataGridView addGoods(GoodsVo goodsVo) {
        try {
            if(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().endsWith("_temp")) {
                String newName= AppFileUtils.renameFile(goodsVo.getGoodsimg());
                goodsVo.setGoodsimg(newName);
            }
            this.goodsService.save(goodsVo);
            return DataGridView.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateGoods")
    public DataGridView updateGoods(GoodsVo goodsVo) {
        try {
            //说明是不默认图片
            if(!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))) {
                if(goodsVo.getGoodsimg().endsWith("_temp")) {
                    String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
                    goodsVo.setGoodsimg(newName);
                    //删除原先的图片
                    String oldPath=this.goodsService.getById(goodsVo.getId()).getGoodsimg();
                    AppFileUtils.removeFileByPath(oldPath);
                }
            }
            this.goodsService.updateById(goodsVo);
            return DataGridView.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("修改失败");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteGoods")
    public DataGridView deleteGoods(Integer id,String goodsimg) {
        try {
            //删除原文件
            AppFileUtils.removeFileByPath(goodsimg);
            this.goodsService.removeById(id);
            return DataGridView.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("删除失败");
        }
    }

    /**
     * 加载所有可用的供应商
     */
    @RequestMapping("loadAllGoodsForSelect")
    public DataGridView loadAllGoodsForSelect() {
        QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Goods> list = this.goodsService.list(queryWrapper);
        for (Goods goods : list) {
            Provider provider = this.providerService.getById(goods.getProviderid());
            if(null!=provider) {
                goods.setProvidername(provider.getProvidername());
            }
        }
        return new DataGridView(list);
    }

    /**
     *根据供应商ID查询商品信息
     */
    @RequestMapping("loadGoodsByProviderId")
    public DataGridView loadGoodsByProviderId(Integer providerid) {
        QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        queryWrapper.eq(providerid!=null, "providerid", providerid);
        List<Goods> list = this.goodsService.list(queryWrapper);
        for (Goods goods : list) {
            Provider provider = this.providerService.getById(goods.getProviderid());
            if(null!=provider) {
                goods.setProvidername(provider.getProvidername());
            }
        }
        return new DataGridView(list);
    }
}
