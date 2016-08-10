package com.test.sharding.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishustory.common.utils.StringUtil;
import com.test.sharding.entities.TestBean;
import com.test.sharding.entities.TestBeanExample;
import com.test.sharding.mapper.TestBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 测试 Service
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/7/29.
 */
@Service
public class TestBeanService {

    @Resource
    private TestBeanMapper testBeanMapper;

    private Logger logger = LoggerFactory.getLogger(TestBeanService.class);

    /**
     * 增加
     *
     * @param testBean 测试信息类
     */
    @Transactional
    public void insert(TestBean testBean) {
        logger.debug("TestBean insert : " + JSON.toJSONString(testBean));
        if (testBean != null) {
            testBeanMapper.insertSelective(testBean);
        } else {
            logger.error("TestBean is null");
            throw new RuntimeException("TestBean is null");
        }
    }

    /**
     * 修改 更新
     *
     * @param testBean 测试信息类
     */
    @Transactional
    public void update(TestBean testBean) {
        logger.debug("TestBean update : " + JSON.toJSONString(testBean));
        if (testBean != null) {
            testBeanMapper.updateByPrimaryKeySelective(testBean);
        } else {
            logger.error("TestBean is null");
            throw new RuntimeException("TestBean is null");
        }
    }

    public TestBean getById(int id) {
        logger.debug("getById : ID = " + id);
        return testBeanMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     *
     * @param param     参数
     * @param pageIndex 页数
     * @param pageSize  查询行数
     * @return 分页查询结果
     */
    public PageInfo<TestBean> getPageList(Map<String, String> param, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize, true);
        return new PageInfo<>(getList(param));
    }


    /**
     * 按条件全部查询
     *
     * @param param 参数
     * @return 查询结果
     */
    public List<TestBean> getList(Map<String, String> param) {
        TestBeanExample example = new TestBeanExample();
        TestBeanExample.Criteria criteria = example.createCriteria();
        if (null != param) {
            if (StringUtil.isNotEmpty(param.get("name"))) {
                criteria.andTestNameLike("%" + param.get("name").trim() + "%");
            }
            example.setOrderByClause(" sort ");
        }
        return testBeanMapper.selectByExample(example);
    }
    /**
     * 按条件全部查询
     *
     * @param name
     * @return 查询结果
     */
    public List<TestBean> getByName(String name) {
        TestBeanExample example = new TestBeanExample();
        example.createCriteria().andTestNameLike("%"+name+"%");
        example.setOrderByClause(" id desc");
        return testBeanMapper.selectByExample(example);
    }
    @Transactional
    public void deletes(List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            TestBeanExample example = new TestBeanExample();
            example.createCriteria().andIdIn(ids);
            testBeanMapper.deleteByExample(example);
        }
    }

    public List<TestBean> getByIds(List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            TestBeanExample example = new TestBeanExample();
            example.createCriteria().andIdIn(ids);
            return testBeanMapper.selectByExample(example);
        } else {
            return new ArrayList<>();
        }
    }
}
