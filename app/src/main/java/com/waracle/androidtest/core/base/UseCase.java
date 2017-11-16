package com.waracle.androidtest.core.base;

public interface UseCase<D> {
  void execute(Callback<D> callback);

  /**
   * In a normal case scenario, interior layers should not depend on implementation details
   * but since we are using AsyncTask and this is not able to cancel itself, we need a method
   * that allows us to do that. This could be solved using RxJava and subscriptions and/or
   * multiple third party libraries
   */
  void cancel();

  abstract class Callback<D> {
    public abstract void onSuccess(D data);
    public abstract void onError(Exception e);
  }
}
