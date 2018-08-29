package org.zhongweixian.util;

/**
 * @author caoliang1918@gmail.com
 * <p>
 * data : 2018年5月5日:上午12:22:56
 */
public class SnowFlakeIdGenerator {

    public static final long MAX_WORKER_ID = 15L;
    public static final long SEQUENCE_MASK = 1023L;
    private long twepoch = 1455419300740L;
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowFlakeIdGenerator(long workerId) {
        if (workerId <= MAX_WORKER_ID && workerId >= 0L) {
            this.workerId = workerId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can\'t be greater than %d or less than 0", new Object[]{Long.valueOf(15L)}));
        }
    }

    public SnowFlakeIdGenerator() {
        new SnowFlakeIdGenerator(4L);
    }

    public long generateId() {
        return this.nextId();
    }

    public long generateId(String name) {
        return this.generateId();
    }

    public long generateId(Class<?> clz) {
        return this.generateId();
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 1023L;
            if (this.sequence == 0L) {
                System.out.println("###########1023");
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", new Object[]{Long.valueOf(this.lastTimestamp - timestamp)}));
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = timestamp - this.twepoch << 14 | this.workerId << 10 | this.sequence;
        return nextId;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
            ;
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public void setTwepoch(long twepoch) {
        this.twepoch = twepoch;
    }

}
