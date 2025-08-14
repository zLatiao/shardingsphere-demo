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
        Fee entity = new Fee();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setOrderTime(LocalDateTime.now());
        feeMapper.insert(entity);
//        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yymm"));

    }

    @Test
    public void insertBatch() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 1);
        LocalDate currDate = startDate;

        List<Fee> list = new ArrayList<>();

        while (currDate.isBefore(endDate)) {
            Fee entity = new Fee();
            entity.setOrderNumber(OrderNumberGenerator.generateOrderNumber(currDate));
            entity.setOrderTime(LocalDateTime.of(currDate, LocalTime.MIN));
            list.add(entity);
            currDate = currDate.plusMonths(1);
        }

        feeMapper.insert(list);
    }


    @Test
    public void selectByOrderNumber() {
        Fee fee = feeMapper.selectOne(Wrappers.<Fee>lambdaQuery().eq(Fee::getOrderNumber, "NO25012000000000000001"));
    }

    @Test
    public void selectByOrderNumbers() {
        List<Fee> fees = feeMapper.selectList(Wrappers.<Fee>lambdaQuery().in(Fee::getOrderNumber, "NO250800f72211-3674-49a3-b400-6dc4c8e3a5e4", "NO25052ad885c2-4e73-40f3-89df-48dd7cfda2a2"));
        System.out.println();
    }

    @Test
    public void selectByGTOrderTime() {
        List<Fee> fees = feeMapper.selectList(Wrappers.<Fee>lambdaQuery().ge(Fee::getOrderTime, LocalDateTime.of(2025, 1, 1, 0, 0, 0)));
        System.out.println();
    }
}
