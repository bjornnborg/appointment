package bjornn.borg.appointment.controller.serializer;

import bjornn.borg.appointment.model.entity.StylistTimeSlot;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomModule extends SimpleModule {
	
	private static final long serialVersionUID = -4233338770785351291L;

	public CustomModule() {
		super("custom-json-module", Version.unknownVersion());
		addSerializer(StylistTimeSlot.class, new StylistTimeSlotSerializer());
	}

}
