package com.github.zzt93.syncer.consumer.ack;

import com.github.zzt93.syncer.common.data.SyncDataExclusion;
import com.github.zzt93.syncer.common.exception.FailureException;
import com.github.zzt93.syncer.common.util.FileUtil;
import com.github.zzt93.syncer.config.pipeline.output.FailureLogConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zzt
 */
public class FailureLog<T> {

  private final Logger logger = LoggerFactory.getLogger(FailureLog.class);
  private final Type type;
  private final AtomicInteger itemCount = new AtomicInteger(0);
  private final BufferedWriter writer;
  private static final Gson gson = new GsonBuilder()
      .setExclusionStrategies(new SyncDataExclusion())
      .create();
  private final int limit;

  public FailureLog(Path path, FailureLogConfig limit, TypeToken token)
      throws FileNotFoundException {
    this.limit = limit.getCountLimit();
    type = token.getType();
    FileUtil.createFile(path, (e) -> logger.error("Fail to create failure log file [{}]", path, e));
    writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(path.toFile(), true)));
  }

  private List<T> recover(Path path) {
    List<T> res = new LinkedList<>();
    // TODO 18/2/1 fromJson: set root object of StandardEvaluationContext & type locator
    return res;
  }

  public boolean log(T data) {
    // TODO 18/2/5 item timestamp
    itemCount.incrementAndGet();
    try {
      String json = gson.toJson(data, type);
      writer.append(json);
      writer.newLine();
    } catch (IOException e) {
      logger.error("Fail to convert to json {}", data);
    }
    if (itemCount.get() > limit) {
      throw new FailureException("Too many failed items, abort and need human influence");
    }
    return true;
  }

}
