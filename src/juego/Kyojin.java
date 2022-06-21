package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;


public class Kyojin {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	
	
	public Kyojin(int x, int y, int ancho, int alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		
	}
	
	public void dibujarIzquierda(Entorno entorno) {
		Image imagen;
		imagen = Herramientas.cargarImagen("Imagenes/kyojinMovIzq.gif");
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 1);
	}
	
	public void dibujarDerecha(Entorno entorno) {
		Image imagen;
		imagen = Herramientas.cargarImagen("Imagenes/kyojinMovDer.gif");
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 1);
	}
	
	public void moverIzqChoque() { //Metodos para mover al titan si se encuentra colisionando con un obstaculo u otro titan.
			this.x = this.x - 2;
	}
	
	public void moverDerChoque() { 
		this.x = this.x + 2;
	}
	
	public void moverArribaChoque() {
		this.y = this.y - 2;
	}
	
	public void moverAbajoChoque() { 
		this.y = this.y + 2;
	}
	
	public void moverIzq(int x) { //Metodos para mover al titan libremente.
		if(this.x > x) {
			this.x = this.x -1;
		}
	}

	public void moverDer(int x) {
		if(this.x < x) {
			this.x = this.x +1;
		}
	}
	
	public void moverArriba(int y) {
		if(this.y > y) {
			this.y = this.y-1;
		}
	}
	
	public void moverAbajo(int y) {
		if(this.y < y) {
			this.y = this.y +1;
		}
	}
	public int getX() { //Devuelve la posicion X.
		return this.x;
	}
	
	public int getY() { //Devuelve la posicion Y.
		return this.y;
	}
	public int getAncho() { //Devuelve el valor del ancho.
		return this.ancho;
	}
	public int getAlto() { //Devuelve el valor del alto.
		return this.alto;
	}
	
	public int getXLadoDer() { //Devuelve los lados del Kyojin
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
