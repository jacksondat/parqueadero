package com.ceiba.parqueadero.ws.model;

public abstract class Vehiculo {
	protected String placa;
	protected float cilindraje;

	public float getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(float cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getPlaca() {
		return this.placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
}
