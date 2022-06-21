package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Image;
public class Proyectil {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private String sentido;
	
	public Proyectil(int x, int y, int ancho, int alto, String sentido) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.sentido = sentido;
	}
	
	public void dibujar(Entorno entorno) { //Depende la direccion del proyectil, carga la respectiva imagen.
		Image imagen = Herramientas.cargarImagen("Imagenes/misilDer.gif");
		if(this.sentido == "arriba") {
			imagen = Herramientas.cargarImagen("Imagenes/misilArriba.gif");
		}
		
		if(this.sentido == "abajo") {
			imagen = Herramientas.cargarImagen("Imagenes/misilAbajo.gif");
		}
		
		if(this.sentido == "derecha") {
			imagen = Herramientas.cargarImagen("Imagenes/misilDer.gif");
		}
		
		if(this.sentido == "izquierda") {
			imagen = Herramientas.cargarImagen("Imagenes/misilIzq.gif");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.3);
		
	}
	public void mover() { // Mueve al proyectil dependiendo el sentido que este tenga.
		if(this.sentido == "arriba" ) 
			this.y = this.y - 4;
		else if(this.sentido == "abajo")
			this.y = this.y + 4;
		else if(this.sentido == "derecha")
			this.x = this.x + 4;
		else
			this.x = this.x - 4;
	}
	public String getSentido() { //Devuelve el sentido del proyectil.
		return this.sentido;
	}
	public int getY() {
		return this.y;
	}
	public int getX() {
		return this.x;
	}
	public int getBordeProyectil() { // Se consigue el borde del proyectil segun la direccion que posea.
		
		if(this.sentido == "izquierda")
			return this.x - this.ancho/2;
		
		else if(this.sentido == "derecha")
			return this.x + this.ancho/2;
		
		else if(this.sentido == "arriba")
			return this.y - this.alto/2;
		
		else
			return this.y + this.alto/2;
		
	}
	
}
