package com.github.zzt93.syncer.config.pipeline.output;

import com.github.zzt93.syncer.config.pipeline.common.InvalidConfigException;
import com.github.zzt93.syncer.config.pipeline.common.MysqlConnection;
import com.github.zzt93.syncer.config.syncer.SyncerOutputMeta;
import com.github.zzt93.syncer.consumer.ack.Ack;
import com.github.zzt93.syncer.consumer.output.channel.OutputChannel;
import com.github.zzt93.syncer.consumer.output.channel.jdbc.MysqlChannel;

/**
 * @author zzt
 */
public class Mysql implements OutputChannelConfig {

  private MysqlConnection connection;
  private RowMapping rowMapping;
  private PipelineBatch batch = new PipelineBatch();
  private FailureLogConfig failureLog = new FailureLogConfig();

  public FailureLogConfig getFailureLog() {
    return failureLog;
  }

  public void setFailureLog(FailureLogConfig failureLog) {
    this.failureLog = failureLog;
  }

  public MysqlConnection getConnection() {
    return connection;
  }

  public void setConnection(
      MysqlConnection connection) {
    this.connection = connection;
  }

  public PipelineBatch getBatch() {
    return batch;
  }

  public void setBatch(PipelineBatch batch) {
    this.batch = batch;
  }

  public RowMapping getRowMapping() {
    return rowMapping;
  }

  public void setRowMapping(RowMapping rowMapping) {
    this.rowMapping = rowMapping;
  }

  @Override
  public OutputChannel toChannel(Ack ack, SyncerOutputMeta outputMeta) throws Exception {
    if (connection.valid()) {
      return new MysqlChannel(this, outputMeta, ack);
    }
    throw new InvalidConfigException("Invalid connection configuration: " + connection);
  }

}
