package com.github.zzt93.syncer.consumer.input;

import com.github.zzt93.syncer.common.data.SyncData;
import com.github.zzt93.syncer.common.data.SyncInitMeta;
import com.github.zzt93.syncer.config.pipeline.common.Connection;
import com.github.zzt93.syncer.config.pipeline.input.Schema;
import com.github.zzt93.syncer.consumer.InputSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

/**
 * @author zzt
 */
public abstract class LocalInputSource implements InputSource {

  private final BlockingDeque<SyncData> filterInput;

  private final Set<Schema> schemas;
  private final Connection connection;
  private final SyncInitMeta syncInitMeta;
  private final String clientId;

  public LocalInputSource(
      String clientId, Connection connection, Set<Schema> schemas,
      SyncInitMeta syncInitMeta,
      BlockingDeque<SyncData> input) {
    this.schemas = schemas;
    this.connection = connection;
    this.syncInitMeta = syncInitMeta;
    this.clientId = clientId;
    this.filterInput = input;
  }

  @Override
  public Connection getRemoteConnection() {
    return connection;
  }

  @Override
  public abstract SyncInitMeta getSyncInitMeta();

  @Override
  public Set<Schema> getSchemas() {
    return schemas;
  }

  @Override
  public String clientId() {
    return clientId;
  }

  @Override
  public boolean input(SyncData data) {
    data.setSourceIdentifier(connection.connectionIdentifier());
    return filterInput.add(data);
  }

  @Override
  public boolean input(SyncData[] data) {
    List<SyncData> res = new ArrayList<>(data.length);
    for (SyncData datum : data) {
      res.add(datum.setSourceIdentifier(connection.connectionIdentifier()));
    }
    return filterInput.addAll(res);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocalInputSource that = (LocalInputSource) o;

    return clientId.equals(that.clientId);
  }

  @Override
  public int hashCode() {
    return clientId.hashCode();
  }

  @Override
  public String toString() {
    return "LocalInputSource{" +
        "schemas=" + schemas +
        ", connection=" + connection +
        ", syncInitMeta=" + syncInitMeta +
        ", clientId='" + clientId + '\'' +
        '}';
  }
}
