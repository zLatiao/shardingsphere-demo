package cn.zzz.demo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("fee_item")
@Data
public class FeeItem {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号（分片键）
     */
    private String orderNumber;

    /**
     * 订单时间（分片键）
     */
    private LocalDateTime orderTime;

    /**
     * 费用类型（如：运费、税费、优惠等）
     */
    private String type;

    /**
     * 费用项目名称
     */
    private String name;

    /**
     * 费用金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private Integer updateUser;

    /**
     * 乐观锁
     */
    private Integer version;
}