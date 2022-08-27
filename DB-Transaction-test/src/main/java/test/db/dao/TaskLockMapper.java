package test.db.dao;

import test.db.entity.TaskLock;

public interface TaskLockMapper {
    int deleteByPrimaryKey(String taskName);

    int insert(TaskLock record);

    int insertSelective(TaskLock record);

    TaskLock selectByPrimaryKey(String taskName);

    int updateByPrimaryKeySelective(TaskLock record);

    int updateByPrimaryKey(TaskLock record);

    //
    TaskLock selectForUpdate(String taskName);

    int updateLockFlagNoCheck(TaskLock record);
}