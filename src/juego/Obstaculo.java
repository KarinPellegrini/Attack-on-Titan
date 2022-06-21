package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private String tipo;
	private Image imagen;
	
	public Obstaculo(int x, int y, int ancho, int alto, String tipo) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.tipo = tipo;
	}
	
	public void dibujar(Entorno entorno) { //Depende el tipo, carga la imagen correspondiente.
		if(this.tipo == "casa") {
		this.imagen = Herramientas.cargarImagen("Imagenes/casa.png");
		entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 0.3);
		}
		if(this.tipo == "arbol") {
			this.imagen = Herramientas.cargarImagen("Imagenes/arbol.png");
			entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 1);
		}
	}
	
	public int getX() { //Devuelve varias variables de instancia
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getAncho() {
		return this.ancho;
	}
	public int getAlto() {
		return this.alto;
	}
	public String getTipo() {
		return this.tipo;
	}
	
	public int getXLadoDer() { //Devuelve los lados del obstaculo
		return this.x + this.ancho/2;
	}
	public int getXLadoIzq() {
		return this.x - this.ancho/2;
	}
	public int getYLadoArriba() {
		return this.y - this.alto/2;
	}
	public int getYLadoAbajo() {
		return this.y + this.alto/2;
	}
}
