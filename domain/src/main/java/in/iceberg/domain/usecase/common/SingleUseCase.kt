package `in`.iceberg.domain.usecase.common

import `in`.iceberg.common.ApplicationExceptions
import `in`.iceberg.domain.executor.PostExecutionThread
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, P>(private val postExecutionThread: PostExecutionThread?) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @NonNull
    abstract fun buildUseCaseObservable(params: P? = null): Single<T>

    fun execute(observer: DisposableSingleObserver<T>?, params: P? = null) {
        requireNotNull(observer) { ApplicationExceptions.NO_NULL_OBSERVER }

        requireNotNull(postExecutionThread) { ApplicationExceptions.NO_NULL_EXECUTION_THREAD }

        requireNotNull(postExecutionThread.scheduler) { ApplicationExceptions.NO_NULL_SCHEDULER }

        val single =
            this.buildUseCaseObservable(params).subscribeOn(Schedulers.io()).observeOn(postExecutionThread.scheduler)
        addDisposable(single.subscribeWith(observer))
    }

    fun disposeAll() {
        this.compositeDisposable.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        this.compositeDisposable.add(disposable)
    }
}