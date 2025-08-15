package cn.zzz.demo;

import cn.zzz.demo.mapper.FeeItemMapper;
import cn.zzz.demo.mapper.FeeMapper;
import cn.zzz.demo.model.entity.Fee;
import cn.zzz.demo.model.entity.FeeItem;
import cn.zzz.demo.model.vo.FeeItemVO;
import cn.zzz.demo.model.vo.FeeVO;
import cn.zzz.demo.util.OrderNumberGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class FeeTest {
    @Autowired
    private FeeMapper feeMapper;
    @Autowired
    private FeeItemMapper feeItemMapper;

    @Test
    public void test() {
        Fee fee = createTestFee(LocalDateTime.now());
        feeMapper.insert(fee);
    }

    @Test
    public void insertBatch() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 1);
        LocalDate currDate = startDate;

        List<Fee> list = new ArrayList<>();

        while (currDate.isBefore(endDate)) {
            Fee fee = createTestFee(LocalDateTime.of(currDate, LocalTime.MIN));
            list.add(fee);
            currDate = currDate.plusWeeks(2);
        }

        feeMapper.insert(list);
    }


    @Test
    public void selectByOrderNumber() {
        Fee fee = feeMapper.selectOne(Wrappers.<Fee>lambdaQuery().eq(Fee::getOrderNumber, "NO2025010de8be50-5cc4-4120-99ed-60b87eca77e6"));
    }
    @Test
    public void selectByOrderNumbers() {
        List<Fee> fees = feeMapper.selectList(Wrappers.<Fee>lambdaQuery().in(Fee::getOrderNumber, "NO2025010de8be50-5cc4-4120-99ed-60b87eca77e6", "NO202512e1e52219-875b-4870-a194-45433447f3c8"));
        System.out.println();
    }

    @Test
    public void selectByGTOrderTime() {
        List<Fee> fees = feeMapper.selectList(Wrappers.<Fee>lambdaQuery().ge(Fee::getOrderTime, LocalDateTime.of(2025, 1, 1, 0, 0, 0)));
        System.out.println();
    }


    @Test
    public void insertFeeAndItem() {
        LocalDateTime now = LocalDateTime.now();
        String orderNumber = OrderNumberGenerator.generateOrderNumber(now);

        FeeItem testFeeItem = createTestFeeItem(orderNumber, now);
        Fee testFee = createTestFee(testFeeItem);

        feeMapper.insert(testFee);
        feeItemMapper.insert(testFeeItem);
    }

    @Test
    public void selectFeeVOByOrderNumber() {
        String orderNumber = "NO2025086dab49a4-ca36-4c6b-91be-903965caac56";
        Fee fee = feeMapper.selectOne(Wrappers.<Fee>lambdaQuery().eq(Fee::getOrderNumber, orderNumber));
        FeeVO feeVO = new FeeVO();
        BeanUtils.copyProperties(fee, feeVO);

        List<FeeItem> feeItems = feeItemMapper.selectList(Wrappers.<FeeItem>lambdaQuery().eq(FeeItem::getOrderNumber, orderNumber));
        feeVO.setItems(feeItems);

        System.out.println();
    }

    @Test
    public void selectFeeItemVOByOrderNumber() {
        String orderNumber = "NO2025086dab49a4-ca36-4c6b-91be-903965caac56";

        List<FeeItemVO> feeItemVOS = feeItemMapper.selectFeeItemVOByOrderNumber(orderNumber);
        System.out.println();
    }

    Fee createTestFee(LocalDateTime time) {
        Fee fee = new Fee();
        fee.setOrderNumber(OrderNumberGenerator.generateOrderNumber(time));
        fee.setOrderTime(time);
        return fee;
    }

    Fee createTestFee(FeeItem feeItem) {
        Fee fee = new Fee();
        fee.setOrderNumber(feeItem.getOrderNumber());
        fee.setOrderTime(feeItem.getOrderTime());
        fee.setOrderNumber("name" + UUID.randomUUID());
        fee.setFeeCnt(1);
        fee.setFeeSum(feeItem.getAmount());
        return fee;
    }


    FeeItem createTestFeeItem(String orderNumber, LocalDateTime time) {
        FeeItem item = new FeeItem();
        item.setOrderNumber(OrderNumberGenerator.generateOrderNumber(time));
        item.setOrderTime(time);
        item.setType(randomType());
        item.setName(item.getType() + "-" + UUID.randomUUID());
        item.setAmount(BigDecimal.valueOf(new Random().nextLong(100)).add(BigDecimal.valueOf(new Random().nextDouble())));
        return item;
    }

    String randomType() {
        // 费用类型（如：运费、税费、优惠等）
        String[] types = {"运费", "税费", "优惠"};
        return types[new Random().nextInt(types.length)];
    }
}
