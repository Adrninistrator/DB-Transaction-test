package test.db.entity;

import java.util.Date;

public class TaskLock {
    private String taskName;

    private Integer lockFlag;

    private Date beginTime;

    private Date endTime;

    private Date dbCurrentDate;

    private String processInfo;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public Integer getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Integer lockFlag) {
        this.lockFlag = lockFlag;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getDbCurrentDate() {
        return dbCurrentDate;
    }

    public void setDbCurrentDate(Date dbCurrentDate) {
        this.dbCurrentDate = dbCurrentDate;
    }

    public String getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(String processInfo) {
        this.processInfo = processInfo;
    }

    @Override
    public String toString() {
        return "TaskLock{" +
                "taskName='" + taskName + '\'' +
                ", lockFlag=" + lockFlag +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", dbCurrentDate=" + dbCurrentDate +
                ", processInfo='" + processInfo + '\'' +
                '}';
    }
}