package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface ScekillDao {
	

	/**
	 * 减少库存数量
	 * <p>Title: reduceNumber</p>
	 * <p>Description: </p>
	 * @param scekillId
	 * @param killTime
	 * @return 受影响的行数 以此来判断是否执行成功
	 */
	int reduceNumber(@Param("seckillId")long scekillId,@Param("killTime")Date killTime);
	
	/**
	 * 通过ID查询商品详情
	 * <p>Title: queryById</p>
	 * <p>Description: </p>
	 * @param scekillId
	 * @return 秒杀商品详情
	 */
	Seckill queryById(Long scekillId);
	
	/**
	 * 查询所有商品详情
	 * <p>Title: queryAll</p>
	 * <p>Description: </p>
	 * @param offet
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offet") int offet,@Param("limit")int limit);
	
	void killByProcedure(Map<String, Object> paramMap);
}
