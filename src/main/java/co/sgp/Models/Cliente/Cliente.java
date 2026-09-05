package co.sgp.Models.Cliente;


public class Cliente {
    private  final Integer NIT;
    private final String nombre;
    public Cliente(Integer NIT, String nombre) {
        this.NIT = NIT;
        this.nombre = nombre;
    }
    public Integer getNIT() {
        return NIT;
    }
    public String getNombre() {
        return nombre;
    }
}
