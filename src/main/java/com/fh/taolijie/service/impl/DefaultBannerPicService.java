package com.fh.taolijie.service.impl;

import com.fh.taolijie.component.ListResult;
import com.fh.taolijie.dao.mapper.BannerPicModelMapper;
import com.fh.taolijie.domain.BannerPicModel;
import com.fh.taolijie.service.BannerPicService;
import com.fh.taolijie.utils.CollectionUtils;
import com.fh.taolijie.utils.ObjWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wanghongfei on 15-6-16.
 */
@Service
@Transactional(readOnly = true)
public class DefaultBannerPicService implements BannerPicService {
    @Autowired
    BannerPicModelMapper banMapper;

    @Override
    public ListResult<BannerPicModel> getBannerList(int firstResult, int capacity) {
        List<BannerPicModel> banList =  banMapper.getAll(firstResult, CollectionUtils.determineCapacity(capacity));
        int tot = banMapper.countGetAll();

        return new ListResult<>(banList, tot);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean updateBanner(Integer banId, BannerPicModel model) {
        model.setId(banId);

        return banMapper.updateByPrimaryKeySelective(model) <= 0 ? false : true;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteBanner(Integer id) {
        return banMapper.deleteByPrimaryKey(id) <= 0 ? false : true;
    }

    @Override
    @Transactional(readOnly = false)
    public int addBanner(BannerPicModel model) {
        return banMapper.insert(model);
    }

    @Override
    public BannerPicModel findBanner(Integer id) {
        return banMapper.selectByPrimaryKey(id);
    }
}
