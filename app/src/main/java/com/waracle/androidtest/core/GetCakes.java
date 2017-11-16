package com.waracle.androidtest.core;

import com.waracle.androidtest.core.base.UseCase;
import com.waracle.androidtest.core.model.Cake;
import com.waracle.androidtest.data.remote.RemoteCakeDataSource;
import java.util.List;

public class GetCakes implements UseCase<List<Cake>> {

  private final RemoteCakeDataSource remoteCakeDataSource;

  public GetCakes(RemoteCakeDataSource remoteCakeDataSource) {
    this.remoteCakeDataSource = remoteCakeDataSource;
  }

  @Override
  public void execute(Callback<List<Cake>> callback) {
    remoteCakeDataSource.getCakes(callback);
  }

  public void cancel() {
    remoteCakeDataSource.cancelDataRequest();
  }
}
