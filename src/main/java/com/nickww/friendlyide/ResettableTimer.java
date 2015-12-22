package com.nickww.friendlyide;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ResettableTimer
{
	private ScheduledFuture<?> future;
	private Supplier<ScheduledFuture<?>> scheduler;
	
	public ResettableTimer(int timeValue, TimeUnit timeUnit, Runnable r)
	{
		scheduler = () -> ((ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1)).schedule(r, timeValue, timeUnit);
		reset();
	}
	
	public void reset()
	{
		if(future != null)
			future.cancel(false);
		future = scheduler.get();
	}
}
