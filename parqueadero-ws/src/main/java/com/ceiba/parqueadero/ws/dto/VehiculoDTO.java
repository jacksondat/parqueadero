package com.ceiba.parqueadero.ws.dto;

import java.util.Date;

public class VehiculoDTO {
	private String placa;
	private float cilindraje;
	private String tipoVehiculo;
	private double valor;
	private Date fechaIngreso;
	
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
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
}
