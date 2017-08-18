package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

	/**
	 * 增加数据	
	 * <p>Title: insertSuceessKilled</p>
	 * <p>Description: </p>
	 * @param scekillId
	 * @param userPhone
	 * @return
	 */
	int insertSuceessKilled(@Param("seckillId")Long seckillId,@Param("userPhone")Long userPhone);
	
	/**
	 * 通过scekillId来查询表
	 * <p>Title: queryByIdWithScekill</p>
	 * <p>Description: </p>
	 * @param scekillId
	 * @return
	 */
	SuccessKilled queryByIdWithScekill(Long scekillId);
	
	
}
