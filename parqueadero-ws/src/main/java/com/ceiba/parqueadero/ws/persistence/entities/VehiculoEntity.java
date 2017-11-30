package com.ceiba.parqueadero.ws.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "tbl_vehiculos")
public class VehiculoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String placa;
	
	@Column
	private float cilindraje;
	
	@Column(nullable = false)
	private Date fechaIngreso;
	
	@Column
	private Date fechaSalida;
	
	@OneToOne
	@JoinColumn(name="idTipoVehiculo")
	private TipoVehiculoEntity tipoVehiculo;
	
	
	public TipoVehiculoEntity getTipoVehiculo() {
		return tipoVehiculo;
	}
	public void setTipoVehiculo(TipoVehiculoEntity tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	public float getCilindraje() {
		return cilindraje;
	}
	public void setCilindraje(float cilindraje) {
		this.cilindraje = cilindraje;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}	
	
}
