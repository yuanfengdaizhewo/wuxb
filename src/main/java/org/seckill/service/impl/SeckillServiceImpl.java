package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.ScekillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String slat = "sjfiaifij#$%^&)(*";
	
	@Resource
	private ScekillDao scekillDao;
	
	@Resource
	private SuccessKilledDao successKilledDao;
	@Override
	
	
	public List<Seckill> getSeckillList() {
		return scekillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return scekillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = scekillDao.queryById(seckillId);
		if (seckill==null) {
			return new Exposer(false, seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		
		if (nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	
	private String getMD5(long seckillId){
		String base = seckillId+"/"+slat;
		return DigestUtils.md5DigestAsHex(base.getBytes());
	}

	
	
	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
		try {
			if (md5 == null || !md5.equals(getMD5(seckillId))) {
				throw new SeckillException("seckill data rewrite");
			}
			
			Date nowTime = new Date();
			int count = scekillDao.reduceNumber(seckillId, nowTime);
			if (count<=0) {
				throw new SeckillCloseException("seckill is closed");
			}else{
				int insertCount = successKilledDao.insertSuceessKilled(seckillId, userPhone);
				if (insertCount<=0) {
					throw new RepeatKillException("seckill repeated");
				}else{
					SuccessKilled successKilled = successKilledDao.queryByIdWithScekill(seckillId);
					return new SeckillExecution(seckillId, SeckillStatEnum.success,successKilled);
				}
			}
		}catch(SeckillCloseException e1){
			throw e1;
		}catch(RepeatKillException e2){
			throw e2;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new SeckillException(e.getMessage());
		}
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		Date killTime = new Date();
		HashMap<String, Object> map = new HashMap<>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
			scekillDao.killByProcedure(map);
			Integer result = MapUtils.getInteger(map, "result",-2);
			if (result == 1) {
				SuccessKilled sk = successKilledDao.queryByIdWithScekill(seckillId);
				return new SeckillExecution(seckillId, SeckillStatEnum.success,sk);
			}else{
				return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillExecution(seckillId, SeckillStatEnum.inner_error);
		}
	}

}
