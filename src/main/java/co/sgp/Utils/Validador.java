package co.sgp.Utils;

import co.sgp.Models.Pedido.Estado;
import co.sgp.Models.Pedido.Prioridad;

public class Validador {
    public static boolean esEstadoValido(String estado) {
        if(estado != null){
            for (Estado estadoEnum : Estado.values()) {
                if (estadoEnum.name().equals(estado.trim().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    public static boolean esTextoValido(String texto) {
        if (texto == null || texto.isBlank()) {
            return false;
        }
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
    public static boolean esNumeroPositivo(int numero) {
        return numero > 0;
    }

    public static boolean esPrioridadValida(String prioridad) {
        if (prioridad == null) {
            return false;
        }
        for (Prioridad prioridadEnum : Prioridad.values()) {
            if (prioridadEnum.name().equals(prioridad.trim().toUpperCase())) {
                return true;
            }
        }
        return false;

    }

    public static boolean esNitvalido(Integer numero) {
        if (numero == null){
            return false;
        }
        int longitudNumeroDocumento = numero.toString().length();
        return longitudNumeroDocumento >= 6 && longitudNumeroDocumento <= 11;
    }
}
