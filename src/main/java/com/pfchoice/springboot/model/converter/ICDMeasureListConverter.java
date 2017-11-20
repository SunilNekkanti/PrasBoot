package com.pfchoice.springboot.model.converter;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pfchoice.springboot.repositories.ICDMeasureRepository;

@SuppressWarnings("rawtypes")
@Component
@Converter(autoApply = true)
public class ICDMeasureListConverter implements AttributeConverter<List, String> {

	private static final String SEPARATOR = ",";

	private static ICDMeasureRepository icdMeasureRepository;
	
	@Autowired
	private ICDMeasureRepository icdMeasureRepository1;


	@PostConstruct
	public void init() {
		ICDMeasureListConverter.icdMeasureRepository = icdMeasureRepository1;
	}

	@Override
	public String convertToDatabaseColumn(List icdMeasureList) {
		return String.join(",", icdMeasureList.toString());
	}

	@Override
	public List convertToEntityAttribute(final String diagnosis) {
		if(diagnosis != null){
		String diagnoses = diagnosis.replace(" ", "");
		String[] icdCodes = diagnoses.split(SEPARATOR);

		return icdMeasureRepository.findByCodes(icdCodes);
		}else{
			return null;
		}
	}
}