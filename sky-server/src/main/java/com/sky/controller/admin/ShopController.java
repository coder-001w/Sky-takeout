package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺管理")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 修改店铺状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "修改店铺状态")
    public Result setStatus(@PathVariable("status") Integer status){
        log.info("设置店铺状态为:{}",status == 1 ? "营业中" : "休息中");
        redisTemplate.opsForValue().set(SHOP_STATUS,status);
        return Result.success();
    }

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "获取店铺状态")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("获取店铺状态:{}",status == 1 ? "营业中" : "休息中");
        return Result.success(status);
    }
}
