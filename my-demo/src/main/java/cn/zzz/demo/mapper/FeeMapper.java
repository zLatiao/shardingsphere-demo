package cn.zzz.demo.mapper;

import cn.zzz.demo.model.entity.Fee;
import cn.zzz.demo.model.vo.FeeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface FeeMapper extends BaseMapper<Fee> {
    @Select("select ")
    FeeVO selectFeeVOByOrderNumber(String orderNumber);
}
