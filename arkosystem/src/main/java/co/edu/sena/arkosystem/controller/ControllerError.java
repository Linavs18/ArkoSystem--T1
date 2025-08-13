package co.edu.sena.arkosystem.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para el manejo de errores HTTP.
 * <p>
 * Esta clase implementa la interfaz {@link ErrorController} de Spring Boot para
 * interceptar y gestionar las páginas de error de la aplicación, como 403, 404, y 500.
 * Proporciona mensajes de error amigables y específicos basados en el código de estado HTTP.
 * </p>
 */
@Controller
public class ControllerError implements ErrorController {

    /**
     * Maneja las solicitudes de errores.
     * <p>
     * Este método se encarga de procesar las solicitudes a la ruta "/error" y de
     * determinar la causa del error (por ejemplo, 403, 404, 500) a partir de los
     * atributos de la solicitud. En base al código de estado, se personalizan el
     * título, mensaje y descripción del error para ser mostrados en la vista
     * correspondiente.
     * </p>
     *
     * @param request El objeto {@link HttpServletRequest} que contiene la información de la solicitud.
     * @param model El objeto {@link Model} para agregar los datos del error a la vista.
     * @return El nombre de la plantilla de la vista de error apropiada.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String errorMessage;
        String errorTitle;
        String errorDescription;

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

            switch (statusCode) {
                case 403:
                    errorTitle = "Acceso Denegado";
                    errorMessage = "Lo sentimos, no tienes permiso para acceder a esta página.";
                    errorDescription = "Verifica que tengas los permisos necesarios o contacta al administrador.";
                    break;
                case 404:
                    errorTitle = "Página No Encontrada";
                    errorMessage = "Lo sentimos, la página que buscas no existe.";
                    errorDescription = "La URL solicitada no fue encontrada en el servidor.";
                    break;
                case 500:
                    errorTitle = "Error del Servidor";
                    errorMessage = "Ha ocurrido un error interno en el servidor.";
                    errorDescription = message != null ? message.toString() : httpStatus.getReasonPhrase();
                    break;
                default:
                    errorTitle = "Error " + statusCode;
                    errorMessage = "Ha ocurrido un error inesperado.";
                    errorDescription = message != null ? message.toString() : httpStatus.getReasonPhrase();
            }

            model.addAttribute("errorTitle", errorTitle);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("errorDescription", errorDescription);
            model.addAttribute("statusCode", statusCode);

            // Determinar qué plantilla usar basado en el código de error
            if (statusCode == 403) {
                return "Errors/403";
            } else if (statusCode == 404) {
                return "Errors/404";
            }
        }

        // Error por defecto si no se puede determinar el código de estado
        model.addAttribute("errorTitle", "Error Inesperado");
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado.");
        model.addAttribute("errorDescription", "Por favor, intenta nuevamente o contacta al soporte técnico.");

        return "Errors/error";
    }
}