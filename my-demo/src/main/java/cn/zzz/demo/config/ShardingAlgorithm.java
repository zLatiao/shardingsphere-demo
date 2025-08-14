package cn.zzz.demo.config;

import cn.zzz.demo.util.OrderNumberGenerator;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ShardingAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<?>>, HintShardingAlgorithm<String> {

    private Properties props;

    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<String> hintShardingValue) {
        return null;
    }

    /**
     * 复杂分片
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue) {
        var yearMonths = availableTargetNames.stream()
                .map(name -> YearMonth.of(2000 + Integer.parseInt(name.substring(name.length() - 4, name.length() - 2)), Integer.parseInt(name.substring(name.length() - 2))))
                .collect(Collectors.toCollection(TreeSet::new));

        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        if (columnNameAndShardingValuesMap.get("order_time") != null) {
            List<String> result = new ArrayList<>();
            Collection<Comparable<?>> orderTimes = columnNameAndShardingValuesMap.get("order_time");
            for (Comparable<?> ot : orderTimes) {
                if (ot == null) {
                    continue;
                }
                LocalDateTime orderTime = (LocalDateTime) ot;
                String format = orderTime.format(DateTimeFormatter.ofPattern("yyMM"));
                if (!format.isEmpty()) {
                    result.addAll(availableTargetNames.stream().filter(s -> s.endsWith(format)).toList());
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        if (columnNameAndShardingValuesMap.get("order_number") != null) {
            List<String> result = new ArrayList<>();
            Collection<Comparable<?>> orderNumbers = columnNameAndShardingValuesMap.get("order_number");
            for (Comparable<?> on : orderNumbers) {
                if (on == null) {
                    continue;
                }
                String orderNumber = (String) on;
                String yymm = OrderNumberGenerator.getYYMMFromOrderNumber(orderNumber);
                if (!yymm.isEmpty()) {
                    result.addAll(availableTargetNames.stream().filter(s -> s.endsWith(yymm)).toList());
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }

        Map<String, Range<Comparable<?>>> columnNameAndRangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
        if (columnNameAndRangeValuesMap != null) {
            Range<Comparable<?>> orderTimeRange = columnNameAndRangeValuesMap.get("order_time");
            if (orderTimeRange != null) {
                // TODO 2025/8/14
                if (orderTimeRange.hasLowerBound()) {
                    LocalDateTime lowerEndpoint = (LocalDateTime) orderTimeRange.lowerEndpoint();
                    BoundType lowerBoundType = orderTimeRange.lowerBoundType();
                }
                if (orderTimeRange.hasUpperBound()) {
                    LocalDateTime upperEndpoint = (LocalDateTime) orderTimeRange.upperEndpoint();
                    BoundType upperBoundType = orderTimeRange.upperBoundType();
                }
            }
        }

        return null;
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
