CREATE TABLE task_lock (
  task_name varchar(45) NOT NULL COMMENT '任务名',
  lock_flag int(10) NOT NULL COMMENT '锁定标识 1是已经锁 0是未锁',
  begin_time datetime NOT NULL COMMENT '任务开始时间',
  end_time datetime NULL COMMENT '任务结束时间',
  process_info varchar(100) NULL COMMENT '进程信息',
  PRIMARY KEY (task_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into task_lock (task_name, lock_flag, begin_time, end_time, process_info)
values ('TEST_DB_TRANSACTION', 0, now(), null, CONNECTION_ID());