package com.waracle.androidtest.data.remote;

import com.waracle.androidtest.core.base.UseCase;
import com.waracle.androidtest.core.model.Cake;
import com.waracle.androidtest.data.remote.base.BaseRemoteDataSource;
import java.util.List;

public interface RemoteCakeDataSource extends BaseRemoteDataSource {
  void getCakes(UseCase.Callback<List<Cake>> cakesCallback);
}
