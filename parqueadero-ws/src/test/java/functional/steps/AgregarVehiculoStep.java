package functional.steps;

import static org.assertj.core.api.Assertions.assertThat;

import functional.ui.PaginaAgregarVehiculo;
import functional.ui.PaginaInicio;
import net.thucydides.core.annotations.Step;

public class AgregarVehiculoStep {

	PaginaInicio paginaInicio;
	
	PaginaAgregarVehiculo paginaAgregarVehiculo;
	
	@Step("Given un usuario en la pagina de inicio")
	public void abrirPaginaInicio() {
		paginaInicio.open();
	}

	@Step("When click en el boton Agregar Vehiculo")
	public void ingresarPaginAgregarVehiculo() {
		paginaInicio.clickBotonAgregarVehiculo();
	}
	
	@Step("And click en el boton Agregar Vehiculo")
	public void clickBotonAgregarVehiculo() {
		paginaAgregarVehiculo.clickBotonAgregarVehiculo();
	}
	
	@Step("And escribe la placa del vehiculo")
	public void agregarPlaca(String placa) {
		paginaAgregarVehiculo.escribirPlacaVehiculo(placa);
	}
	
	@Step("And seleccionar el tipo de vehiculo Carro")
	public void seleccionarVehiculoCarro() {
		paginaAgregarVehiculo.seleccionarVehiculoCarro();
	}

	@Step("Then El titulo debe ser")
	public void tituloDebeSer(String titulo) {
		assertThat(paginaAgregarVehiculo.getTitle()).containsIgnoringCase(titulo);
	}

	@Step("Then el mensaje de confirmacion debe ser")
	public void confirmacionDeberiaSer(String mensaje) {
		assertThat(paginaAgregarVehiculo.getMensajeConfirmacion()).containsIgnoringCase(mensaje);
	}
}
