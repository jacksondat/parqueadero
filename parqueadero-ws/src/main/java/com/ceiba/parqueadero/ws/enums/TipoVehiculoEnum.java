package com.ceiba.parqueadero.ws.enums;

public enum TipoVehiculoEnum {
	CARRO("carro"), MOTO("moto");
	
	private String value;
	
	TipoVehiculoEnum(String value){
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }

}
