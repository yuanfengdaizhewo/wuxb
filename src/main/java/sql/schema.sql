--数据库初始化脚本

--创建数据库
create DATABASE seckill;
--使用数据库
use seckill;
--创建秒杀库存表
create table seckill(
'seckill_id' bigint NOT NULL AUTO_INCREMENT COMMENT'商品库ID',
'name' varchar(120) NOT NULL COMMENT'商品的名称',
'number' int NOT NULL COMMENT'商品的数量',
'start_time' timestamp NOT NULL COMMENT'秒杀开始时间',
'ent_time' timestamp NOT NULL COMMENT'秒杀结束时间',
'create_time' timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'商品创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT'秒杀库存表'

insert into 
	seckill(name,number,start_time,end_time)
values
	('1000元秒杀ipad',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
	('500元秒杀ipad2',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
	('300元秒杀iphone',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
	('100元秒杀红米note',400,'2015-11-01 00:00:00','2015-11-02 00:00:00');
	


--秒杀成功明细表
create table success_killed(
`seckill_id` bigint NOT NULL COMMENT'秒杀id',
`user_phone` bigint NOT NULL COMMENT'用户电话'
`state` tinyint NOT NULL DEFAULT -1 COMMENT'秒杀状态  默认值为-1：无效, 0：有效 ,1：已付款';
`create_time` timestamp NOT NULL COMMENT'创建时间',
PRIMARY KEY (`seckill_id`,`user_phone`),
KEY idx_create_time(`create_time`)
)ENGING=InnoDB DEFAULT CHARSET=utf8 COMMENT'秒杀成功明细表';