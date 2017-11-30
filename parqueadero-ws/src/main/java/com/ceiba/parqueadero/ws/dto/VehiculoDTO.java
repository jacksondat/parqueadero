package com.ceiba.parqueadero.ws.dto;

public class VehiculoDTO {
	private String placa;
	private float cilindraje;
	private String tipoVehiculo;
	
	public String getTipoVehiculo() {
		return tipoVehiculo;
	}
	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public float getCilindraje() {
		return cilindraje;
	}
	public void setCilindraje(float cilindraje) {
		this.cilindraje = cilindraje;
	}
}
