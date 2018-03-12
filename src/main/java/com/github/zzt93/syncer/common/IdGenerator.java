package com.github.zzt93.syncer.common;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.zzt93.syncer.producer.input.mongo.DocTimestamp;
import com.github.zzt93.syncer.producer.input.mysql.connect.BinlogInfo;
import org.bson.BsonTimestamp;
import org.bson.Document;

/**
 * @author zzt
 */
public class IdGenerator {

  public static final String EID = "eid";
  private static final String SEP  = "/";

  public enum Offset {
    DUP(1000), CLONE(2000);

    private final int offset;

    Offset(int offset) {
      this.offset = offset;
    }

    public int getOffset() {
      return offset;
    }
  }

  /**
   * <a href="https://github.com/shyiko/mysql-binlog-connector-java/issues/200">binlog table map</a>
   */
  public static String fromEvent(Event[] event, String binlogFileName) {
    EventHeaderV4 tableMap = event[0].getHeader();
    EventHeaderV4 second = event[1].getHeader();
    // have to remember table map event for restart
    // have to add following data event for unique id
    return second.getServerId() + SEP + binlogFileName + SEP + tableMap
        .getPosition() + SEP + second.getPosition() + SEP + second.getEventType();
  }

  public static String fromEventId(String eventId, int ordinal) {
    return eventId + SEP + ordinal;
  }

  public static String fromEventId(String eventId, int ordinal, int offset) {
    return eventId + SEP + ordinal + SEP + offset;
  }

  public static BinlogInfo fromDataId(String dataId) {
    String[] split = dataId.split(SEP);
    if (split.length == 6 || split.length == 7) {
      return new BinlogInfo(split[1], Long.parseLong(split[2]));
    }
    throw new IllegalArgumentException(dataId);
  }

  public static String fromDocument(Document document) {
    BsonTimestamp timestamp = (BsonTimestamp) document.get("ts");
    return timestamp.getTime() + SEP + timestamp.getInc();
  }

  public static DocTimestamp fromMongoDataId(String dataId) {
    String[] split = dataId.split(SEP);
    if (split.length == 3) {
      return new DocTimestamp(new BsonTimestamp(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
    }
    throw new IllegalArgumentException(dataId);
  }

}
