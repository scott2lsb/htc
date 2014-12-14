package com.haitaocheng.constants;
import com.google.common.eventbus.EventBus;


public class HTCEventBus {
	private static EventBus eventBus;
	
	private HTCEventBus() {
		
	}
	public static EventBus getEventBus() {
		 if (eventBus == null) {
			 eventBus = new EventBus();
		}
		return eventBus;
	}
	
}
