package org.seckill.enums;

public enum SeckillStatEnum {
	success(1,"秒杀成功"),
	end(0,"秒杀结束"),
	repeat_kill(-1,"重复秒杀"),
	inner_error(-2,"系统异常"),
	data_rewrite(-3,"数据篡改");

	private int state;
	
	private String stateInfo;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	private SeckillStatEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public static SeckillStatEnum stateOf(int index){
		for (SeckillStatEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
