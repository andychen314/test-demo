package com.test.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/2.
 */
public class MultipleKeysModuloTableShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue<?>> shardingValues) {
        Set<Integer> orderIdValueSet = getShardingValue(shardingValues, "order_id");
        Set<Integer> userIdValueSet = getShardingValue(shardingValues, "user_id");

        List<String> result = new ArrayList<>();

        Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValueSet, orderIdValueSet);
        for (List<Integer> value : valueResult) {
            String suffix = Joiner.on("").join(value.get(0) % 2, value.get(1) % 2);
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(suffix)) {
                    result.add(tableName);
                }
            }

        }
        return result;
    }

    private Set<Integer> getShardingValue(final Collection<ShardingValue<?>> shardingValues, final String shardingKey) {
        Set<Integer> valueSet = new HashSet<>();
        ShardingValue<Integer> shardingValue = null;
        for (ShardingValue<?> each : shardingValues) {
            if (each.getColumnName().equals(shardingKey)) {
                shardingValue = (ShardingValue<Integer>) each;
                break;
            }
        }
        if (null == shardingValue) {
            return valueSet;
        }
        switch (shardingValue.getType()) {
            case SINGLE:
                valueSet.add(shardingValue.getValue());
                break;
            case LIST:
                valueSet.addAll(shardingValue.getValues());
                break;
            case RANGE:
                for (Integer i = shardingValue.getValueRange().lowerEndpoint(); i <= shardingValue.getValueRange().upperEndpoint(); i++) {
                    valueSet.add(i);
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return valueSet;
    }
}
