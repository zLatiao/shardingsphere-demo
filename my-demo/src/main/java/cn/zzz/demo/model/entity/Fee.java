package cn.zzz.demo.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("fee")
@Data
public class Fee {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单时间
     */
    private LocalDateTime orderTime;

    /**
     * 费用数量
     */
    private Integer feeCnt;

    /**
     * 总费用
     */
    private BigDecimal feeSum;

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