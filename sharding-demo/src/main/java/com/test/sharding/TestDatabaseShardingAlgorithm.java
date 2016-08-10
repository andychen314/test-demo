package com.test.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

import java.util.Collection;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/7/30.
 */
public class TestDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        System.out.println("*************  doEqualSharding  ***************");

        return availableTargetNames.isEmpty() ? null : availableTargetNames.iterator().next();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        System.out.println("*************  doInSharding  ***************");

        return availableTargetNames;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        System.out.println("*************  doBetweenSharding  ***************");

        return availableTargetNames;
    }
}
