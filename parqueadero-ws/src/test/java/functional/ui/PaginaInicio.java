package functional.ui;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://localhost:4200")
public class PaginaInicio extends PageObject{
	
	@FindBy(css="*[class^='btn btn-info btn-lg btn-block']")
	private WebElementFacade agregarVehiculoBoton;
	
	public void clickBotonAgregarVehiculo() {
		agregarVehiculoBoton.click();
	}
}
