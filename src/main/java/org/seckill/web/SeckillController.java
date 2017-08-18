package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping("/list" )
	public String list(Model model){
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}
	
	@RequestMapping("/{seckillId}/detail")
	public String detail(@PathVariable("seckillId") Long seckillId,Model model){
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill ==null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	@RequestMapping(value="/{seckillId}/exposer",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execution(@PathVariable("seckillId") Long seckillId,
													@PathVariable("md5") String md5,
													@CookieValue(value="killPhone",required= false)Long phone){
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		SeckillExecution result;
		try {
			result = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, result);
		} catch (SeckillCloseException e) {
			result = new SeckillExecution(seckillId, SeckillStatEnum.end);
			return new SeckillResult<SeckillExecution>(true, result);
		} catch (RepeatKillException e) {
			result = new SeckillExecution(seckillId, SeckillStatEnum.repeat_kill);
			return new SeckillResult<SeckillExecution>(true, result);
		} catch (SeckillException e) {
			logger.error(e.getMessage(),e);
			result = new SeckillExecution(seckillId, SeckillStatEnum.inner_error);
			return new SeckillResult<SeckillExecution>(true, result);
		}
	}
	
	@RequestMapping(value="/time/now",method=RequestMethod.GET,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public  SeckillResult<Long> time(){
		Date date = new Date();
		return new SeckillResult<Long>(true, date.getTime());
	}
}
