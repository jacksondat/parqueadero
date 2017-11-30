package com.ceiba.parqueadero.ws.services;

import com.ceiba.parqueadero.ws.enums.ParqueaderoCompany;
import com.ceiba.parqueadero.ws.services.impl.ParqueaderoCeibaServiceImpl;

public class ParqueaderoServiceFactory {

	public ParqueaderoService createParqueaderoService(ParqueaderoCompany parqueaderoCompany) {
		ParqueaderoService parqueaderoService = null;
		
		if(parqueaderoCompany.compareTo(ParqueaderoCompany.CEIBA) == 0){
			parqueaderoService = new ParqueaderoCeibaServiceImpl();
		}
		
		return parqueaderoService;
	}

}
