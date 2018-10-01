package com.nloops.students.data.mvp;

/**
 * This is the base interface of our View structure.
 *
 * @param <T> implemented Presenter.
 */
public interface BaseView<T> {

  void setPresenter(T presenter);
}
