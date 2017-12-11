package functional.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import functional.steps.AgregarVehiculoStep;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class AgregarVehiculoTest {

	@Steps
    AgregarVehiculoStep agregarVehiculoStep;

    @Managed(driver="chrome")
    WebDriver browser;

    @Test
    public void deberiaAgregarUnVehiculo() {
        // Given
    	agregarVehiculoStep.abrirPaginaInicio();

        // When
    	agregarVehiculoStep.ingresarPaginAgregarVehiculo();
    	agregarVehiculoStep.agregarPlaca("JKM64D");
    	agregarVehiculoStep.seleccionarVehiculoCarro();
    	agregarVehiculoStep.clickBotonAgregarVehiculo();

        // Then
    	agregarVehiculoStep.confirmacionDeberiaSer("El vehículo con placa JKM64D fue ingresado exitósamente.");
    }
	
}
