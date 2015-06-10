package com.fh.taolijie.dao.mapper.v2;

import com.fh.taolijie.domain.v2.TaoliIvyModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaoliIvyModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int insert(TaoliIvyModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int insertSelective(TaoliIvyModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    TaoliIvyModel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TaoliIvyModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(TaoliIvyModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table taoli_ivy
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TaoliIvyModel record);

    List<TaoliIvyModel> findBy(TaoliIvyModel model);

    List<TaoliIvyModel> searchBy(TaoliIvyModel model);
}