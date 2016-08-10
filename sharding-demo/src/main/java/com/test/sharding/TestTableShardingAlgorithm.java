package com.test.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/7/30.
 */
public class TestTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {

    private static Logger logger= org.slf4j.LoggerFactory.getLogger(TestTableShardingAlgorithm.class);

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        logger.debug("****** doEqualSharding ******");
        for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue() % availableTargetNames.size() + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        logger.debug("****** doInSharding ******");
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (Integer value : shardingValue.getValues()) {
            result.addAll(availableTargetNames.stream().filter(tableName -> tableName.endsWith(value % availableTargetNames.size() + "")).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        logger.debug("****** doBetweenSharding ******");
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Integer> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {

            for (String each : availableTargetNames) {
                if (each.endsWith(i % availableTargetNames.size() + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
