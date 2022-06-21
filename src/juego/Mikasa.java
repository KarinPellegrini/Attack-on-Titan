package juego;
import java.awt.Image;
import entorno.Herramientas;
import entorno.Entorno;

public class Mikasa {
	private int x;
	private int y;
	private int alto;
	private int ancho;
	private String sentido;
	private boolean pocionActiva;
	private Image imagen;
	
	public Mikasa(int x, int y, int ancho, int alto, String sentido, boolean pocionActiva, String imagen) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.sentido = sentido;
		this.pocionActiva = pocionActiva;
		this.imagen = Herramientas.cargarImagen("Imagenes/mikasaQuietaDer.png");
	}
	
	public void dibujarMovimiento(Entorno entorno) { //Dibuja a mikasa en movimiento (Solo derecha o izquierda)
		
		if(sentido == "derecha") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaDerMov.gif");
		}
		if(sentido == "izquierda") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaIzqMov.gif");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.7);
		
	}
	public void dibujarQuieta(Entorno entorno) { //Dibuja a Mikasa quieta
		
		if(sentido == "derecha") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaQuietaDer.png");
		}
		if(sentido == "izquierda") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaQuietaIzq.png");
		}
		if(sentido == "abajo") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaAbajo.png");
		}
		if(sentido == "arriba") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaArriba.png");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.7);
		
	}

	public void dibujarDisparando(Entorno entorno) { //Dibuja a Mikasa disparando
		if(this.sentido == "derecha") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaDisparoDer.png");
		}
		else{
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaDisparoIzq.png");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 0.7);
	
	}
	public void dibujarDisparandoKyojin(Entorno entorno) { //Dibuja a Mikasa disparando transformada
		if(this.sentido == "derecha") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaDisparoKyojinDer.png");
		}
		else{
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaDisparoKyojinIzq.png");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 1);
	
	}
	public void dibujarTransformada(Entorno entorno) { //Dibuja a Mikasa en movimiento (transformada)
		if(this.sentido == "izquierda") {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaKyojinIzqMov.gif");
		}
		
		else {
			this.imagen = Herramientas.cargarImagen("Imagenes/mikasaKyojinDerMov.gif");
		}
		
		entorno.dibujarImagen(imagen, this.x, this.y, 0, 1);
	}
	
	public void moverArriba() //Mueve al personaje y cambia la direccion de Mikasa.
	{
		this.y = this.y - 3;
		this.sentido = "arriba";
	}
	
	public void moverAbajo() //Mueve al personaje y cambia la direccion de Mikasa.
	{
		this.y = this.y + 3;
		this.sentido = "abajo";
	}
	
	public void moverDerecha() //Mueve al personaje y cambia la direccion de Mikasa.
	{
		this.x = this.x + 3;
		this.sentido = "derecha";
	}
	
	public void moverIzquierda() //Mueve al personaje y cambia la direccion de Mikasa.
	{
		this.x = this.x - 3;
		this.sentido = "izquierda";
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
	public String getSentido(){ //Devuelve el sentido de Mikasa.
		return this.sentido;
	}
	public boolean getEstado() { //Devuelve el estado en relacion al suero de Mikasa.
		return this.pocionActiva;
	}
	
	public void cambiarAlto(int alto) { //Permite cambiar el alto de Mikasa.
		this.alto = alto;
	}
	public void cambiarAncho(int ancho) { //Permite cambiar el ancho de Mikasa.
		this.ancho = ancho;
	}
	public void cambiarEstado(boolean nuevoEstado) { //Permite cambiar el estado de Mikasa.
		this.pocionActiva = nuevoEstado;
	}
	
	public Proyectil disparar(){ // Depende la direccion del personaje va a tener cierto x y cierto y, ya que sino podria salir el proyectil desde fuera del personaje.
		if(this.sentido == "derecha") {
			Proyectil p1 = new Proyectil(this.x + this.ancho, this.y, 80, 10, this.sentido);
			return p1;
			}
		else if(this.sentido == "arriba") {
			Proyectil p1 = new Proyectil(this.x, this.y - this.alto, 10,80, this.sentido);
			return p1;
			}
		else if(this.sentido == "izquierda") {
			Proyectil p1 = new Proyectil(this.x - this.ancho, this.y, 80, 10, this.sentido);
			return p1;
		}
		else {
			Proyectil p1 = new Proyectil(this.x, this.y + this.alto, 10,80, this.sentido);
			return p1;
		}
	}
	
	public int getXLadoDer() { //Devuelve los lados de Mikasa
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
