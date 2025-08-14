package cn.zzz.demo;
import cn.zzz.demo.mapper.FeeMapper;
import cn.zzz.demo.model.entity.Fee;
import cn.zzz.demo.util.OrderNumberGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class FeeTest {
    @Autowired
    private FeeMapper feeMapper;

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

    Fee createTestFee(LocalDateTime time) {
        Fee fee = new Fee();
        fee.setOrderNumber(OrderNumberGenerator.generateOrderNumber(time));
        fee.setOrderTime(time);
        return fee;
    }
}
