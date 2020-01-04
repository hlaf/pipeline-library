package com.emt.ioc

import com.emt.IStepExecutor

interface IContext {
    IStepExecutor getStepExecutor()
}
