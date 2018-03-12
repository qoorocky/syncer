package com.github.zzt93.syncer.producer.input.mysql.connect;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.network.SSLMode;
import org.junit.Test;

/**
 * @author zzt
 */
public class MysqlMasterConnectorTest {

  @Test
  public void listEvent() throws Exception {
    BinaryLogClient client = new BinaryLogClient("", 3310,
        "root", "");
    client.registerLifecycleListener(new LogLifecycleListener());
    client.setEventDeserializer(SyncDeserializer.defaultDeserialzer());
    client.setServerId(1234);
    client.setSSLMode(SSLMode.DISABLED);
    client.setBinlogFilename("mysql-bin.001124");
    client.setBinlogPosition(51582531);
    client.registerEventListener(event -> {
      EventType eventType = event.getHeader().getEventType();
      if (eventType == EventType.WRITE_ROWS || eventType == EventType.TABLE_MAP){
        System.out.println(event);
      }
    });
    client.connect();
  }
}