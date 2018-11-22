package com.gojek.simplechat

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable

<<<<<<< HEAD
=======

>>>>>>> 9da200d0e91cc62bef3f98c74f9f712121d44c5b
class RxSchedulersOverrideRule : TestRule {

    private val schedulerInstance = Schedulers.trampoline()

<<<<<<< HEAD
    private val schedulerFunction = io.reactivex.functions.Function<Scheduler, Scheduler> { schedulerInstance }
=======
    private val schedulerFunction = Function<Scheduler, Scheduler> { schedulerInstance }
>>>>>>> 9da200d0e91cc62bef3f98c74f9f712121d44c5b

    private val schedulerFunctionLazy = Function<Callable<Scheduler>, Scheduler> { schedulerInstance }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunctionLazy)

                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler(schedulerFunction)
                RxJavaPlugins.setNewThreadSchedulerHandler(schedulerFunction)
                RxJavaPlugins.setComputationSchedulerHandler(schedulerFunction)

                base.evaluate()

                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}
