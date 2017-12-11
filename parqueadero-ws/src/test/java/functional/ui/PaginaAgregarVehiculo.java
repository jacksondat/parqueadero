package functional.ui;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:4200/agregar")
public class PaginaAgregarVehiculo extends PageObject{

	@FindBy(id="placa")
	private WebElementFacade placaInput;
	
	@FindBy(id="tipoVehiculoCarro")
	private WebElementFacade carroRadio;
	
	@FindBy(css=".mensaje .alert-success p")
	private WebElementFacade mensajeConfirmacion;
	
	@FindBy(css="*[class^='btn btn-info btn-lg btn-block']")
	private WebElementFacade agregarVehiculoBoton;
	
	public void clickBotonAgregarVehiculo() {
		agregarVehiculoBoton.click();
	}
	
	public void escribirPlacaVehiculo(String placa) {
		placaInput.sendKeys(placa);
	}

	public void seleccionarVehiculoCarro() {
		carroRadio.click();		
	}

	public String getMensajeConfirmacion() {
		return mensajeConfirmacion.getText();
	}
	
}
