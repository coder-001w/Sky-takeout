package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id更新购物车商品数量
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);

    @Insert("insert into shopping_cart(user_id, dish_id, setmeal_id, name, amount, image, create_time, number, dish_flavor) " +
            "values(#{userId}, #{dishId}, #{setmealId}, #{name}, #{amount}, #{image}, #{createTime}, #{number}, #{dishFlavor})")
    void insert(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{currentId}")
    List<ShoppingCart> getShoppingCartListById(Long currentId);

    @Delete("delete from shopping_cart where user_id = #{currentId}")
    void cleanShoppingCart(Long currentId);

    @Delete("delete from shopping_cart where id = #{id} ")
    void deleteById(Long id);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}
