package com.github.zzt93.syncer.config.pipeline.output;

import com.github.zzt93.syncer.config.pipeline.common.ElasticsearchConnection;
import com.github.zzt93.syncer.config.pipeline.common.InvalidConfigException;
import com.github.zzt93.syncer.config.syncer.SyncerOutputMeta;
import com.github.zzt93.syncer.consumer.ack.Ack;
import com.github.zzt93.syncer.consumer.output.channel.elastic.ElasticsearchChannel;

/**
 * @author zzt
 */
public class Elasticsearch implements OutputChannelConfig {

  private ElasticsearchConnection connection;
  private RequestMapping requestMapping = new RequestMapping();
  private PipelineBatch batch = new PipelineBatch();
  private FailureLogConfig failureLog = new FailureLogConfig();

  public FailureLogConfig getFailureLog() {
    return failureLog;
  }

  public void setFailureLog(FailureLogConfig failureLog) {
    this.failureLog = failureLog;
  }

  public ElasticsearchConnection getConnection() {
    return connection;
  }

  public void setConnection(ElasticsearchConnection connection) {
    this.connection = connection;
  }

  public RequestMapping getRequestMapping() {
    return requestMapping;
  }

  public void setRequestMapping(RequestMapping requestMapping) {
    this.requestMapping = requestMapping;
  }


  public PipelineBatch getBatch() {
    return batch;
  }

  public void setBatch(PipelineBatch batch) {
    this.batch = batch;
  }

  @Override
  public ElasticsearchChannel toChannel(Ack ack,
      SyncerOutputMeta outputMeta) throws Exception {
    if (connection.valid()) {
      return new ElasticsearchChannel(this, outputMeta, ack);
    }
    throw new InvalidConfigException("Invalid connection configuration: " + connection);
  }
}
