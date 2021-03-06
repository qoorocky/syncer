package com.github.zzt93.syncer.producer.output;

import com.github.zzt93.syncer.common.data.SyncData;
import com.github.zzt93.syncer.consumer.InputSource;

/**
 * @author zzt
 */
public class LocalOutputSink implements OutputSink {

  private InputSource inputSource;

  public LocalOutputSink(InputSource inputSource) {
    this.inputSource = inputSource;
  }

  @Override
  public boolean output(SyncData data) {
    return inputSource.input(data);
  }

  @Override
  public boolean output(SyncData[] data) {
    return inputSource.input(data);
  }

  @Override
  public InputSource remote() {
    return inputSource;
  }


  @Override
  public String toString() {
    return "LocalOutputSink{" +
        "inputSource=" + inputSource +
        '}';
  }
}
