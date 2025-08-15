package cn.zzz.demo.model.vo;

import cn.zzz.demo.model.entity.Fee;
import cn.zzz.demo.model.entity.FeeItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeeVO extends Fee {
    private List<FeeItem> items;

}
