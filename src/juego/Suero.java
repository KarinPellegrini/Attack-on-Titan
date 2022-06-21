package juego;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Suero {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	
	public Suero(int x, int y, int ancho, int alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
	}
	
	public void dibujar(Entorno entorno) {
		Image imagen = Herramientas.cargarImagen("Imagenes/pocion.png");
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.5);
		
	}
	public int getX() { //Devuelve la posicion X.
		return this.x;
	}
	public int getY() {	//Devuelve la posicion Y.
		return this.y;
	}
	public int getAncho() {	//Devuelve el valor del ancho.
		return this.ancho;
	}
	public int getAlto() {	//Devuelve el valor del alto.
		return this.alto;
	}
	
	public int getXLadoDer() { //Devuelve los lados del suero
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
