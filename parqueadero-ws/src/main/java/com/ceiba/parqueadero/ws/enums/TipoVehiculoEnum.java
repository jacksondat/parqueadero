package com.ceiba.parqueadero.ws.enums;

import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;

public enum TipoVehiculoEnum {
	CARRO("carro"), MOTO("moto");
	
	private String value;
	
	TipoVehiculoEnum(String value){
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }
	
	public static TipoVehiculoEnum getTipoVehiculo(String tipoVehiculo) throws TipoVehiculoException{
		TipoVehiculoEnum tipoVehiculoResponse = null;
		for (TipoVehiculoEnum tipoVehiculoIter : TipoVehiculoEnum.values()) {
			if(tipoVehiculoIter.getValue() == tipoVehiculo){
				tipoVehiculoResponse = tipoVehiculoIter;
				break;
			}
		}
		
		if(tipoVehiculoResponse != null){
			return tipoVehiculoResponse;
		}else{
			throw new TipoVehiculoException("No existen Tipos de Vehiculos con el nombre "+tipoVehiculo);
		}
	}
}
