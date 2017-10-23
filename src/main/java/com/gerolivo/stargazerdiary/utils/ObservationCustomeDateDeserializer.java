package com.gerolivo.stargazerdiary.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObservationCustomeDateDeserializer extends JsonSerializer<Date> {

    @Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
	    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	    jsonGenerator.writeString(dateFormatter.format(date));
	}
}