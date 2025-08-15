package cn.zzz.demo.config;

import cn.zzz.demo.util.OrderNumberGenerator;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ComplexKeysShardingAlgorithm 复合分片算法
 * HintShardingAlgorithm
 */
public class ShardingAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<?>> {

    private Properties props;


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    private final String logicTableName = "fee";

    /**
     * 复杂分片
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue) {

        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        // 根据orderTime
        Collection<Comparable<?>> orderTimes = columnNameAndShardingValuesMap.get("order_time");
        if (orderTimes != null) {
            Set<String> expectTableNames = orderTimes.stream()
                    .filter(Objects::nonNull)
                    .map(ot -> ((LocalDateTime) ot).format(formatter))
                    .map(this::getTableName)
                    .collect(Collectors.toSet());
            
            return availableTargetNames.stream().filter(expectTableNames::contains).toList();
        }

        // 根据orderTime范围
        Map<String, Range<Comparable<?>>> columnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        if (columnNameAndRangeValuesMap != null) {
            Range<Comparable<?>> orderTimeRange = columnNameAndRangeValuesMap.get("order_time");
            if (orderTimeRange != null) {
                LocalDateTime startTime = null, endTime = null;
                if (orderTimeRange.hasLowerBound()) {
                    startTime = (LocalDateTime) orderTimeRange.lowerEndpoint();
                    BoundType lowerBoundType = orderTimeRange.lowerBoundType();
                }
                if (orderTimeRange.hasUpperBound()) {
                    endTime = (LocalDateTime) orderTimeRange.upperEndpoint();
                    BoundType upperBoundType = orderTimeRange.upperBoundType();
                }
                // todo 这里先写死 跟配置文件里对应
                if (startTime == null) {
                    startTime = LocalDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.MIN);
                }
                if (endTime == null) {
                    endTime = LocalDateTime.of(LocalDate.of(2025, 12, 31), LocalTime.MAX);
                }

                Set<String> expectTableNames = new HashSet<>();
                LocalDateTime curr = startTime;
                while (!curr.isAfter(endTime)) {
                    String yearMonth = curr.format(formatter);
                    expectTableNames.add(getTableName(yearMonth));
                    curr = curr.plusMonths(1);
                }

                return availableTargetNames.stream().filter(expectTableNames::contains).toList();
            }
        }

        // 根据orderNumber
        Collection<Comparable<?>> orderNumbers = columnNameAndShardingValuesMap.get("order_number");
        if (orderNumbers != null) {
            Set<String> expectTableNames = orderNumbers.stream()
                    .filter(Objects::nonNull)
                    .map(on -> OrderNumberGenerator.getYYYYMMFromOrderNumber((String) on))
                    .map(this::getTableName)
                    .collect(Collectors.toSet());

            return availableTargetNames.stream().filter(expectTableNames::contains).toList();
        }


        return null;
    }

    private String getTableName(String yearMonth) {
        return logicTableName + "_" + yearMonth;
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void init(Properties props) {
        this.props = props;
    }
}
