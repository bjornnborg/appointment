package bjornn.borg.appointment.controller.serializer;

import java.io.IOException;

import bjornn.borg.appointment.model.entity.StylistTimeSlot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StylistTimeSlotSerializer extends JsonSerializer<StylistTimeSlot> {

	@Override
	public void serialize(StylistTimeSlot stylistTimeSlot, JsonGenerator json, SerializerProvider provider) throws IOException, JsonProcessingException {
		json.writeStartObject();
		json.writeNumberField("id", stylistTimeSlot.getId());
		// json.writeEmbeddedObject(stylistTimeSlot.getStylist());
		json.writeEmbeddedObject(stylistTimeSlot.getTimeSlot());
		json.writeEndObject();
		
	}


}
