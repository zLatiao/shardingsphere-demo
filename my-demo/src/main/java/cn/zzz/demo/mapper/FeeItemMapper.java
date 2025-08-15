package cn.zzz.demo.mapper;

import cn.zzz.demo.model.entity.FeeItem;
import cn.zzz.demo.model.vo.FeeItemVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FeeItemMapper extends BaseMapper<FeeItem> {
    @Select("""
            select t2.*, t1.order_name 
            from fee t1 
            join fee_item t2 on t1.order_number = t2.order_number 
            where t1.order_number = #{orderNumber}
            """)
    List<FeeItemVO> selectFeeItemVOByOrderNumber(@Param("orderNumber") String orderNumber);
}