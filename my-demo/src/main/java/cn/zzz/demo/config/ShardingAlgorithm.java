package cn.zzz.demo.config;

import cn.zzz.demo.util.OrderNumberGenerator;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.time.LocalDateTime;
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
    
    /**
     * 复杂分片
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue) {

        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        Collection<Comparable<?>> orderTimes = columnNameAndShardingValuesMap.get("order_time");
        if (orderTimes != null) {
            Set<String> expectTableNames = orderTimes.stream()
                    .filter(Objects::nonNull)
                    .map(ot -> ((LocalDateTime) ot).format(formatter))
                    .map(s -> "fee_" + s)
                    .collect(Collectors.toSet());
            
            return availableTargetNames.stream().filter(expectTableNames::contains).toList();
        }

        Collection<Comparable<?>> orderNumbers = columnNameAndShardingValuesMap.get("order_number");
        if (orderNumbers != null) {
            Set<String> expectTableNames = orderNumbers.stream()
                    .filter(Objects::nonNull)
                    .map(on -> OrderNumberGenerator.getYYYYMMFromOrderNumber((String) on))
                    .map(s -> "fee_" + s)
                    .collect(Collectors.toSet());

            return availableTargetNames.stream().filter(expectTableNames::contains).toList();
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
