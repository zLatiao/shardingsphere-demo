package cn.zzz.demo.model.vo;

import cn.zzz.demo.model.entity.FeeItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeeItemVO extends FeeItem {
    private String orderName;
}
