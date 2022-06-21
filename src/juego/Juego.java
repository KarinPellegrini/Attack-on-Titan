package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.util.Random;
import entorno.InterfaceJuego;
import entorno.Entorno;
import entorno.Herramientas;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y metodos propios de cada grupo
	private Mikasa mika;
	private Kyojin[] enemigos;
	private Proyectil proyectil;
	private Suero[] sueros;
	private Obstaculo[] obstaculos;
	private Image fondo;
	private int contadorDeTicks;
	private int puntos;
	private int kyojinesEliminados;
	private int vidas;
	private int tiempoDeInvulnerabilidad;
	private boolean pantallaInicioActiva;
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo 7 - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		
		// Carga valores a ciertas variables de instancia.
		this.fondo = Herramientas.cargarImagen("Imagenes/fondo.png");
		this.contadorDeTicks = 0;
		this.puntos = 0;
		this.kyojinesEliminados = 0;
		this.vidas = 3;
		this.tiempoDeInvulnerabilidad = 0;
		this.pantallaInicioActiva = true;
		
		//Mikasa
		this.mika = new Mikasa(400,300,40,70,"derecha", false,"mikasaMov1");
		
		//Obstaculos
		this.obstaculos = new Obstaculo[4];
		// Crea 4 obstaculos para comenzar el juego distribuidos en 4 partes de la pantalla (arribaIzq, arribaDer, abajoIzq, abajoDer).
		this.obstaculos[0] = new Obstaculo(200, 175, 40, 70, "arbol"); //Crea un obstaculo de tipo arbol
		this.obstaculos[1] = new Obstaculo(200, 425, 70, 70, "casa"); //Crea un obstaculo de tipo casa
		this.obstaculos[2] = new Obstaculo(600, 425, 40, 70, "arbol"); //Crea un obstaculo de tipo arbol
		this.obstaculos[3] = new Obstaculo(600, 175, 70, 70, "casa"); //Crea un obstaculo de tipo casa
		
		//Enemigos
		
		this.enemigos = new Kyojin[4];
		
		for (int i = 0; i < 4; i++) { //Crea 4 Kyojines para comenzar el juego.
			int derOIzq = this.randomPos(); //Numero random entre 0 y 1 para definir derecha o izquierda.
			int supOInf = this.randomPos(); //Numero random entre 0 y 1 para definir superior o inferior.
			int yKyojin;
			int xKyojin;
			
			if(derOIzq == 0) {
				xKyojin = this.darXDer();
			}
			else {
				xKyojin = this.darXIzq();
			}
			if(supOInf == 0) {
				yKyojin = this.darYSup();
			}
			else {
				yKyojin = this.darYInf();
			}
			this.enemigos[i] = new Kyojin(xKyojin, yKyojin, 60, 140); //Recibe un X y un Y random para darle una posicion al Kyojin, los valores ancho y alto son fijos.
			boolean colisionMikasaTitanGeneral = this.colisionMikasaKyojinAbajo(i) || this.colisionMikasaKyojinArriba(i) || this.colisionMikasaKyojinDer(i) || this.colisionMikasaKyojinIzq(i);
			boolean colisionKyojinObstaculoGeneral = this.colisionKyojinObstaculoAbajo(i) || this.colisionKyojinObstaculoArriba(i) || this.colisionKyojinObstaculoDer(i) || this.colisionKyojinObstaculoIzq(i); 
			if(colisionKyojinObstaculoGeneral || colisionMikasaTitanGeneral || this.colisionKyojinKyojinGeneral(i)) { //Si hay colisiones al momento de inicializar al titan, se lo borra y se resta 1 a la variable "i" (para crear otro).
				this.enemigos[i] = null; 
				i--;
			}
			
		}
		
		//Sueros
		this.sueros = new Suero[3];
		boolean seCreo = false;
		while(seCreo != true) {
			seCreo = true;
			int derOIzq = this.randomPos();
			int supOInf = this.randomPos();
			int xSuero;
			int ySuero;
			if(derOIzq == 0) {
				xSuero = this.darXDer();
			}
			else {
				xSuero = this.darXIzq();
			}
			if(supOInf == 0) {
				ySuero = this.darYSup();
			}
			else {
				ySuero = this.darYInf();
			}
			this.sueros[0] = new Suero(xSuero,ySuero,20,35);
				if(this.colisionSueroKyojin(0) || this.colisionSueroObstaculo(0) || this.tocoSuero(0)) {
				seCreo = false; //Recibe un X y un Y random para darle una posicion al suero. (Crea solo uno para comenzar el juego)
				}
		}
		
		
		
		// Inicia el juego!
		this.entorno.iniciar();
		
	}

	public void tick()
	{
		// Procesamiento de un instante de tiempo
		if(this.pantallaInicioActiva == true) { //Mientras la pantallaDeInicioSeaActiva, se ejecuta este bloque y no ejecuta las funcionalidades principales.
			Image oprimaJugar = Herramientas.cargarImagen("Imagenes/jugar.png");
			Image pantCarga = Herramientas.cargarImagen("Imagenes/pantCarga.png");
			this.entorno.dibujarImagen(this.fondo,400,300,0,1);
			this.mika.dibujarQuieta(entorno);
			
			for (int i = 0; i < enemigos.length; i++) {
				if(this.enemigos[i] != null) {
					this.enemigos[i].dibujarDerecha(entorno);
				}
			}
			for (int i = 0; i < this.obstaculos.length; i++) {
				this.obstaculos[i].dibujar(entorno);
			}
			this.entorno.dibujarImagen(pantCarga, 410, 60, 0, 1); //Dibuja una pequeña pantalla de carga
			this.entorno.dibujarImagen(oprimaJugar, 410, 540, 0, 1); //Dibuja un cuadro con instrucciones para comenzar el juego.
			if(this.entorno.sePresiono(this.entorno.TECLA_ENTER)) { //Si se oprime enter, comienza el juego.
				this.pantallaInicioActiva = false;
				Herramientas.play("Sonido/musica.wav");
			}
		}
		else {
		
		
		//Corrobora si termino el juego, sino, ejecuta el resto del tick.
		if(this.terminoJuego()) {
			this.entorno.dibujarImagen(this.fondo, 400, 300, 0, 1); //Si termino, dibuja el fondo, posibles enemigos restantes, obstaculos y un cuadro correspondiente al resultado obtenido.
			for (int i = 0; i < enemigos.length; i++) {
				if(this.enemigos[i] != null) {
					this.enemigos[i].dibujarDerecha(entorno);
				}
			}
			for (int i = 0; i < this.obstaculos.length; i++) {
				this.obstaculos[i].dibujar(entorno);
			}
			if(this.seGano()) {
			this.entorno.dibujarRectangulo(410, 300, 600, 300, 0, Color.WHITE);
			this.entorno.dibujarRectangulo(410, 300, 590, 290, 0, Color.BLACK);
			
			this.entorno.cambiarFont(Font.SERIF, 60, Color.GREEN);
			
			this.entorno.escribirTexto("GANASTE", 280, 250);
			
			this.entorno.cambiarFont(Font.SERIF, 25, Color.GREEN);
			
			this.entorno.escribirTexto("Puntaje final: " + this.puntos, 190, 310);
			this.entorno.escribirTexto("Titanes eliminados: " + this.kyojinesEliminados, 190, 350);
			this.entorno.escribirTexto("Oprima la tecla 'End/Fin' para cerrar el juego.", 190, 390);
			}
			else {
			this.entorno.dibujarRectangulo(410, 300, 600, 300, 0, Color.WHITE);
			this.entorno.dibujarRectangulo(410, 300, 590, 290, 0, Color.BLACK);
			
			this.entorno.cambiarFont(Font.SERIF, 60, Color.RED);
			
			this.entorno.escribirTexto("PERDISTE", 280, 250);
			
			this.entorno.cambiarFont(Font.SERIF, 25, Color.RED);
			
			this.entorno.escribirTexto("Puntaje final: " + this.puntos, 190, 310);
			this.entorno.escribirTexto("Titanes eliminados: " + this.kyojinesEliminados, 190, 350);
			this.entorno.escribirTexto("Oprima la tecla 'End/Fin' para cerrar el juego.", 190, 390);	
			}
			if(this.entorno.sePresiono(this.entorno.TECLA_FIN)) { //Si se presiona la tecla end/fin, se cierra la pantalla.
				System.exit(0);
			}
		}
		else {
		
		//Dibujar fondo
		this.entorno.dibujarImagen(this.fondo, 400, 300, 0, 1);
		
		//Dibuja las vidas restantes de mikasa.
		Image corazones;
		if(this.vidas == 3) {
			corazones = Herramientas.cargarImagen("Imagenes/tresCorazones.png");
			this.entorno.dibujarImagen(corazones, 50, 25, 0, 1);
		}
		else if(this.vidas == 2) {
			corazones = Herramientas.cargarImagen("Imagenes/dosCorazones.png");
			this.entorno.dibujarImagen(corazones, 50, 25, 0, 1);
		}
		else {
			corazones = Herramientas.cargarImagen("Imagenes/unCorazon.png");
			this.entorno.dibujarImagen(corazones, 50, 25, 0, 1);
		}
			
		//Contador de ticks y actualizador de ticks de invulnerabilidad (Si mikasa perdio una vida recientemente.)
		this.contadorDeTicks++;
		
		if(this.tiempoDeInvulnerabilidad > 0){ //Disminuye en cada tick el tiempo restante de invulnerabilidad
			this.tiempoDeInvulnerabilidad--;
			Image invulnerabilidad = Herramientas.cargarImagen("Imagenes/invulnerabilidad2.png");
			if(this.tiempoDeInvulnerabilidad < 50) { //Si falta la mitad del tiempo de invulnerabilidad, carga la segunda imagen (contiene un 1, indicando los "segundos" restantes de la misma.)
				invulnerabilidad = Herramientas.cargarImagen("Imagenes/invulnerabilidad1.png");
			}
			this.entorno.dibujarImagen(invulnerabilidad, 750, 40, 0, 0.3); //Dibuja el tiempo de invulnerabilidad restante
		}
	
		// Dibujar puntaje y kyojines eliminados
		this.entorno.cambiarFont(Font.SERIF, 14, Color.WHITE);
		this.entorno.escribirTexto("PUNTAJE: " + this.puntos, 10, 60);
		this.entorno.escribirTexto("KYOJINES ELIMINADOS: " + this.kyojinesEliminados, 620, 590);
		
		//Mikasa
		
		boolean condicionMoverArriba = this.entorno.estaPresionada('w' ) && this.colisionMikasaObstaculoArriba() != true && this.mika.getY() - this.mika.getAlto()/2 > 0;
		boolean condicionMoverAbajo = this.entorno.estaPresionada('s') && this.colisionMikasaObstaculoAbajo() != true && this.mika.getY() + this.mika.getAlto()/2 < 600;
		boolean condicionMoverDerecha = this.entorno.estaPresionada('d') && this.colisionMikasaObstaculoDer() != true && this.mika.getX() + this.mika.getAncho()/2 < 800;
		boolean condicionMoverIzquierda = this.entorno.estaPresionada('a') && this.colisionMikasaObstaculoIzq() != true && this.mika.getX() - this.mika.getAncho()/2 >0;
		
		boolean fotoActiva = false; // Este booleano comprueba que no se este dibujando ninguna otra foto de movimiento, esto se hace para no solapar dos imagenes de movimiento.
		
		if(condicionMoverIzquierda) {//Movimiento del personaje segun la tecla apretada.
			this.mika.moverIzquierda();
			if(this.proyectil == null && fotoActiva == false) { // Se comprueba que no haya proyectil activo ya que cuando lo esta tiene su propia imagen.
				if(this.mika.getEstado() == false)
				{
					this.mika.dibujarMovimiento(entorno);
				}
				else  // Si esta transformada, se usa el metodo dibujarTransformada
				{
					this.mika.dibujarTransformada(entorno);
				}
				fotoActiva = true;
			}
		}
		if(condicionMoverDerecha) {
			this.mika.moverDerecha();
			if(this.proyectil == null && fotoActiva == false) {
				if(this.mika.getEstado() == false)
				{
					this.mika.dibujarMovimiento(entorno);
				}
				else 
				{
					this.mika.dibujarTransformada(entorno);
				}
				fotoActiva = true;
			}
		}
		
		if(condicionMoverArriba) { 
			this.mika.moverArriba();
			if(this.proyectil == null && fotoActiva == false) {
				if(this.mika.getEstado() == false)
				{
					this.mika.dibujarMovimiento(entorno);
				}
				else 
				{
					this.mika.dibujarTransformada(entorno);
				}
				fotoActiva = true;
			}
		}
		
		if(condicionMoverAbajo) {
			this.mika.moverAbajo();
			if(this.proyectil == null && fotoActiva == false) {
				if(this.mika.getEstado() == false)
				{
					this.mika.dibujarMovimiento(entorno);
				}
				else 
				{
					this.mika.dibujarTransformada(entorno);
				}
				fotoActiva = true;
			}
		}
		
		// Si no se esta presionando ninguna tecla, se dibuja a Mikasa con una foto quieta. (segun su orientacion tambien)
		if(!condicionMoverArriba && !condicionMoverAbajo && !condicionMoverDerecha && !condicionMoverIzquierda && this.proyectil == null) {
			if(this.mika.getEstado() == false) {
				this.mika.dibujarQuieta(entorno);
			}
			else { // Si esta transformada, se usa el metodo dibujarTransformada
				this.mika.dibujarTransformada(entorno);
			}
		}
		
		if (this.mika.getEstado() == true) { //Comprobacion del estado de mikasa (si tomo el suero)
			this.mika.cambiarAlto(138); //Si lo tomo se cambia la altura, y luego el ancho, al de un titan
			this.mika.cambiarAncho(40);
		}
		
		for (int i = 0; i < enemigos.length; i++) {
			if(this.enemigos[i] != null) {
				boolean colisionMikasaTitanGeneral = this.colisionMikasaKyojinAbajo(i) || this.colisionMikasaKyojinArriba(i) || this.colisionMikasaKyojinDer(i) || this.colisionMikasaKyojinIzq(i);
				if(colisionMikasaTitanGeneral && this.mika.getEstado()) { /*Si un titan choca a Mikasa, y esta se encuentra en el estado de pocion,
																			se la devuelve a su altura y ancho base, y se cambia el estado de pocion a false.*/
					this.mika.cambiarAlto(70);
					this.mika.cambiarAncho(40);
					this.mika.cambiarEstado(false);
					this.enemigos[i] = null;				//Ademas, el titan muere y se actualiza el puntaje y contador de enemigos eliminados.
					this.puntos = this.puntos + 10;
					this.kyojinesEliminados++;
				}
				else if(colisionMikasaTitanGeneral && this.mika.getEstado() == false && this.tiempoDeInvulnerabilidad == 0) { //Comprobacion colision sin pocion activa
					this.vidas--; //Le quita una vida.
					this.tiempoDeInvulnerabilidad = 100; // Otorga un margen de invulnerabilidad para permitirle a mikasa escapar del enemigo (100 ticks).
				}
			}
		}
		
		
		
		//Proyectil
		
		if(this.entorno.sePresiono(entorno.TECLA_ESPACIO) && this.proyectil == null) { //Si se presiono espacio y no hay proyectil, se le asigna a this.proyectil el creado por la clase de Mikasa.
			this.proyectil = this.mika.disparar();	
		}
		
		if(this.proyectil != null) { //Mientras no sea null (haya proyectil), este es dibujado y se mueve segun su metodo propio.
			this.proyectil.dibujar(entorno);
			this.proyectil.mover();
		}
		
		for (int i = 0; i < obstaculos.length; i++) { { 
				if(this.deteccionProyectilErrado(i)) { //Si el proyectil fallo y choco con un objeto u obstaculo, se elimina.
					this.proyectil = null;
				}
			}
		}
		
		if(this.proyectil != null) { //Dibuja a Mikasa disparando (mientras el proyectil este activo) segun su estado.
			if(this.mika.getEstado() == false) 
			{
				this.mika.dibujarDisparando(entorno);
			}
			else {
				this.mika.dibujarDisparandoKyojin(entorno);
			}
		}
		
		//Sueros
		
		for (int i = 0; i < this.sueros.length; i++) {
			if(this.sueros[i] != null) { //Mientras que el suero no sea nulo, lo dibuja y chequea si Mikasa lo toco.
				this.sueros[i].dibujar(entorno);
				if(this.tocoSuero(i)) {
					this.mika.cambiarEstado(true);
					this.sueros[i] = null;
				}
			}
		}
		
		boolean seCreoEnTick = false; //Booleano para saber si se creo un suero en este tick.
		for (int i = 0; i < this.sueros.length; i++) {
			if(this.sueros[i] == null && seCreoEnTick == false && this.contadorDeTicks % 500 == 0) {
				seCreoEnTick = true;
				int derOIzq = this.randomPos();
				int supOInf = this.randomPos();
				int xSuero;
				int ySuero;
				if(derOIzq == 0) {
					xSuero = this.darXDer();
				}
				else {
					xSuero = this.darXIzq();
				}
				if(supOInf == 0) {
					ySuero = this.darYSup();
				}
				else {
					ySuero = this.darYInf();
				}
				this.sueros[i] = new Suero(xSuero,ySuero,20,35);
				if(this.colisionSueroKyojin(i) || this.colisionSueroObstaculo(i) || this.tocoSuero(i)) { //Si se detecta colision (superpuesto con algun objeto) al crearlo, se borra y se crea otro.
					this.sueros[i] = null;
					seCreoEnTick = false;
					i--;
				}
			}
		}
				

		//Enemigos
		for (int i = 0; i < enemigos.length; i++) {
			if(this.enemigos[i] != null) { //Si el enemigo no es nulo, lo dibuja.
				
				if(this.mika.getX() > this.enemigos[i].getX()) // Si Mikasa esta mas hacia la derecha (x mayor que el x del titan), dibuja al kyojin mirando hacia la derecha.
				{ 
					this.enemigos[i].dibujarDerecha(entorno);
				}
				
				else  										   // Si el X de Mikasa es menor que el X del titan, lo dibuja mirando hacia la izquierda.
				{ 
					this.enemigos[i].dibujarIzquierda(entorno);
				}
				
				if (enemigos[i] != null && this.proyectil != null && this.impactoProyectilEnemigo(i)) { // Si el Kyojin es impactado por un proyectil, es borrado igual que el proyectil.
					this.enemigos[i] = null;
					this.proyectil = null;
					this.puntos = this.puntos + 10; 
					this.kyojinesEliminados++;
				}
			}
		}
		
		int contKyojinesCreadosTick = 0; //Booleano que cuenta kyojines creados en el tick
		for (int i = 0; i < this.enemigos.length; i++) { //Crea Kyojines cada cierto tiempo y si no se crearon mas de 2 en el tick
			if(this.enemigos[i] == null && this.contadorDeTicks % 200 == 0 && contKyojinesCreadosTick < 2) {
				contKyojinesCreadosTick++;
				int derOIzq = this.randomPos();
				int supOInf = this.randomPos();
				int yKyojin;
				int xKyojin;
				
				if(derOIzq == 0) {
					xKyojin = this.darXDer();
				}
				else {
					xKyojin = this.darXIzq();
				}
				if(supOInf == 0) {
					yKyojin = this.darYSup();
				}
				else {
					yKyojin = this.darYInf();
				}
				
				this.enemigos[i] = new Kyojin(xKyojin, yKyojin, 60, 140); //Recibe un X y un Y random para darle una posicion al Kyojin, los valores ancho y alto son fijos.
				
				boolean colisionMikasaTitanGeneral = this.colisionMikasaKyojinAbajo(i) || this.colisionMikasaKyojinArriba(i) || this.colisionMikasaKyojinDer(i) || this.colisionMikasaKyojinIzq(i);
				boolean colisionKyojinObstaculoGeneral = this.colisionKyojinObstaculoAbajo(i) || this.colisionKyojinObstaculoArriba(i) || this.colisionKyojinObstaculoDer(i) || this.colisionKyojinObstaculoIzq(i);
				
				if(colisionKyojinObstaculoGeneral || colisionMikasaTitanGeneral || this.colisionKyojinKyojinGeneral(i)) { //Si se detecta colision al crearlo, se borra para crear otro.
					this.enemigos[i] = null;
					i--;
					contKyojinesCreadosTick--;
				}
			
			}
		}
		
		//Movimiento del Kyojin
		//Evitar que el Kyojin se salga de los margenes
		for (int i = 0; i < enemigos.length; i++) {
			if(this.enemigos[i] != null) {
				boolean chocaBordeDer = this.enemigos[i].getX() + this.enemigos[i].getAncho()/2 > 800;
				boolean chocaBordeIzq = this.enemigos[i].getX() - this.enemigos[i].getAncho()/2 < 0;
				boolean chocaBordeArriba = this.enemigos[i].getY() - this.enemigos[i].getAlto()/2 < 0;
				boolean chocaBordeAbajo = this.enemigos[i].getY() + this.enemigos[i].getAlto()/2 > 600;
				
				if(chocaBordeDer) {
					this.enemigos[i].moverIzqChoque();
				}
				
				if(chocaBordeIzq) {
					this.enemigos[i].moverDerChoque();
				}
				
				if(chocaBordeArriba) {
					this.enemigos[i].moverAbajoChoque();
				}
				
				if(chocaBordeAbajo) {
					this.enemigos[i].moverArribaChoque();
				}
			}
		}
		
		
		//Cuando choca con otro titan.
		for (int i = 0; i < this.enemigos.length; i++) { //Comparo un Kyojin
			if(this.enemigos[i] != null) {
				for (int j = 0; j < this.enemigos.length; j++) { //Con todos los demas. (Menos el mismo, se corrobora en el metodo).
					boolean colisionKyojinGeneral = (this.colisionKyojinKyojinDer(i,j) || this.colisionKyojinKyojinIzq(i,j) || this.colisionKyojinKyojinArriba(i,j) || this.colisionKyojinKyojinArriba(i,j));
					if(colisionKyojinGeneral) {  //Si hay colision entre Kyojines, se corrobora cual ocurrio, y se mueve a los kyojines en consecuencia.
						if(colisionKyojinKyojinDer(i,j) && !colisionKyojinObstaculoIzq(i) && !colisionKyojinObstaculoDer(j)) {
							this.enemigos[i].moverIzqChoque();
							this.enemigos[j].moverDerChoque();
						}
						else {
							this.enemigos[i].moverArribaChoque();
							this.enemigos[j].moverAbajoChoque();
						}
						if(colisionKyojinKyojinIzq(i,j) && !colisionKyojinObstaculoDer(i) && !colisionKyojinObstaculoIzq(j)) {
							this.enemigos[i].moverDerChoque();
							this.enemigos[j].moverIzqChoque();
						}
						else {
							this.enemigos[i].moverAbajoChoque();
							this.enemigos[j].moverArribaChoque();
						}
						if(colisionKyojinKyojinArriba(i,j) && !colisionKyojinObstaculoAbajo(i) && !colisionKyojinObstaculoAbajo(j)) {
							this.enemigos[i].moverAbajoChoque();
							this.enemigos[j].moverArribaChoque();
						}
						else {
							this.enemigos[i].moverDerChoque();
							this.enemigos[i].moverIzqChoque();
						}
						if(colisionKyojinKyojinAbajo(i,j) && !colisionKyojinObstaculoArriba(i) && !colisionKyojinObstaculoAbajo(j)) {
							this.enemigos[i].moverArribaChoque();
							this.enemigos[j].moverAbajoChoque();
						}
						else {
							this.enemigos[i].moverAbajoChoque();
							this.enemigos[j].moverArribaChoque();
						}
					}
				}
			}
		}
		
		//Movimiento sin colisiones y con colisiones con obstaculos.
		
		for (int i = 0; i < enemigos.length; i++) { //Mover kyojines. //El que estoy viendo
			if (this.enemigos[i]!=null) { // Si no es nulo, lo deja moverse.
				boolean colisionGeneralObstaculo = this.colisionKyojinObstaculoAbajo(i) || this.colisionKyojinObstaculoArriba(i) || this.colisionKyojinObstaculoDer(i) || this.colisionKyojinObstaculoIzq(i);
				boolean puedeMoverDer = (!this.colisionKyojinObstaculoDer(i) && this.enemigos[i].getX() + this.enemigos[i].getAncho()/2 < 800 && !(colisionGeneralObstaculo && this.enemigos[i].getY() > this.mika.getY()) && this.enemigos[i].getX() < this.mika.getX());
				boolean puedeMoverIzq = (!this.colisionKyojinObstaculoIzq(i) && this.enemigos[i].getX() - this.enemigos[i].getAncho()/2 >0 && !(colisionGeneralObstaculo && this.enemigos[i].getY() < this.mika.getY()) && this.enemigos[i].getX() > this.mika.getX());
				boolean puedeMoverArriba = (!this.colisionKyojinObstaculoArriba(i) && this.enemigos[i].getY() - this.enemigos[i].getAlto()/2 > 0 && !(colisionGeneralObstaculo && this.enemigos[i].getX() > this.mika.getX()) && this.enemigos[i].getY() > this.mika.getY());
				boolean puedeMoverAbajo = (!this.colisionKyojinObstaculoAbajo(i) && this.enemigos[i].getY() + this.enemigos[i].getAlto()/2 < 600 && !(colisionGeneralObstaculo && this.enemigos[i].getX() < this.mika.getX()) && this.enemigos[i].getY() < this.mika.getY());
				
				boolean colisionaObstaculoAbajo = this.colisionKyojinObstaculoAbajo(i) && this.enemigos[i].getY() < this.mika.getY();
				boolean colisionaObstaculoArriba = this.colisionKyojinObstaculoArriba(i) && this.enemigos[i].getY() > this.mika.getY(); 
				boolean colisionaObstaculoDer = this.colisionKyojinObstaculoDer(i) && this.enemigos[i].getX() < this.mika.getX();
				boolean colisionaObstaculoIzq = this.colisionKyojinObstaculoIzq(i) && this.enemigos[i].getX() > this.mika.getX();
				
				
				//Sin colisiones
				
				if(puedeMoverDer) {
					this.enemigos[i].moverDer(this.mika.getX());
				}
				if(puedeMoverIzq) {
					this.enemigos[i].moverIzq(this.mika.getX());
				}
				if(puedeMoverArriba) {
					this.enemigos[i].moverArriba(this.mika.getY());
				}
				if(puedeMoverAbajo) {
					this.enemigos[i].moverAbajo(this.mika.getY());
				}
				
				
				//Colision con obstaculos
				
				if(colisionaObstaculoAbajo) {
					this.enemigos[i].moverIzqChoque();
				}
				if(colisionaObstaculoArriba) {
					this.enemigos[i].moverDerChoque();
				}
				if(colisionaObstaculoDer) {
					this.enemigos[i].moverAbajoChoque();
				}
				if(colisionaObstaculoIzq) {
					this.enemigos[i].moverArribaChoque();
				}
			}
		}
		
		
		
		//Obstaculos
		for (int i = 0; i < obstaculos.length; i++) {// Dibuja los obstaculos.
				this.obstaculos[i].dibujar(entorno);
		}
		
			}
		}
	}
		
	
	public int darXIzq() {
		Random x = new Random();
		int posX = x.nextInt(370-80) + 80; //Numero aleatorio en X desde 80 y 370
		return posX;
	}
	public int darXDer() {
		Random x = new Random();
		int posX = x.nextInt(720-430) + 430; //Numero aleatorio en X desde 430 y 720
		return posX;
	}
	public int darYSup() {
		Random y = new Random();
		int posY = y.nextInt(260-100) + 100; //Numero aleatorio entre 100 y 260
		return posY;
	}
	public int darYInf() {
		Random y = new Random();
		int posY = y.nextInt(470-340) + 340 ; //Numero aleatorio entre 340 y 470
		return posY;
	}
	
	public int randomPos() { //Da un valor aleatorio (0 o 1)
		Random x = new Random();
		int valorRandom = x.nextInt(2);
		return valorRandom;
	}
	
	public boolean seGano() { //Determina si se gano.
		for (int i = 0; i < this.enemigos.length; i++) {
			if(this.enemigos[i] != null) {
				return false;
			}
		}
		return true;
	}
	public boolean sePerdio() { //Determina si se perdio.
		if(this.vidas == 0) {
			this.mika = null;
			return true;
		}
		return false;
	}
	
	public boolean terminoJuego() { //Determina si finalizo el juego.
		if(seGano() || sePerdio())
			return true;
		return false;
	}
	
	// Colisiones mikasa y kyojines.
	public boolean colisionMikasaKyojinDer(int indice) {
		int xLadoDer = this.mika.getXLadoDer(); //Lados necesarios para detectar colision de Mikasa.
		int yLadoArriba = this.mika.getYLadoArriba();
		
		int xLadoDerKyojin = this.enemigos[indice].getXLadoDer(); //Definicion de los lados de los Kyojines
		int xLadoIzqKyojin = this.enemigos[indice].getXLadoIzq();
		int yLadoArribaKyojin = this.enemigos[indice].getYLadoArriba();
		int yLadoAbajoKyojin = this.enemigos[indice].getYLadoAbajo();
		
		return(xLadoDer > xLadoIzqKyojin && xLadoDer < xLadoDerKyojin && yLadoArriba < yLadoAbajoKyojin && yLadoArriba > yLadoArribaKyojin);
	}
	
	public boolean colisionMikasaKyojinIzq(int indice) {
		int xLadoIzq = this.mika.getXLadoIzq(); //Lados necesarios para detectar colision de Mikasa.
		int yLadoArriba = this.mika.getYLadoArriba();
		
		int xLadoDerKyojin = this.enemigos[indice].getXLadoDer(); //Definicion de los lados de los Kyojines
		int xLadoIzqKyojin = this.enemigos[indice].getXLadoIzq();
		int yLadoArribaKyojin = this.enemigos[indice].getYLadoArriba();
		int yLadoAbajoKyojin = this.enemigos[indice].getYLadoAbajo();
		
		return(xLadoIzq < xLadoDerKyojin && xLadoIzq > xLadoIzqKyojin && yLadoArriba < yLadoAbajoKyojin && yLadoArriba > yLadoArribaKyojin);
	}
	
	public boolean colisionMikasaKyojinArriba(int indice) {
		int xLadoDer = this.mika.getXLadoDer(); //Lados necesarios para detectar colision de Mikasa.
		int yLadoAbajo = this.mika.getYLadoAbajo();
		
		int xLadoDerKyojin = this.enemigos[indice].getXLadoDer(); //Definicion de los lados de los Kyojines
		int xLadoIzqKyojin = this.enemigos[indice].getXLadoIzq();
		int yLadoArribaKyojin = this.enemigos[indice].getYLadoArriba();
		int yLadoAbajoKyojin = this.enemigos[indice].getYLadoAbajo();
		
		return(xLadoDer > xLadoIzqKyojin && xLadoDer < xLadoDerKyojin && yLadoAbajo > yLadoArribaKyojin && yLadoAbajo < yLadoAbajoKyojin);
	}
	
	public boolean colisionMikasaKyojinAbajo(int indice) {
		int xLadoIzq = this.mika.getXLadoIzq(); //Lados necesarios para detectar colision de Mikasa.
		int yLadoAbajo = this.mika.getYLadoAbajo();
		
		int xLadoDerKyojin = this.enemigos[indice].getXLadoDer(); //Definicion de los lados de los Kyojines
		int xLadoIzqKyojin = this.enemigos[indice].getXLadoIzq();
		int yLadoArribaKyojin = this.enemigos[indice].getYLadoArriba();
		int yLadoAbajoKyojin = this.enemigos[indice].getYLadoAbajo();
		
		return(xLadoIzq < xLadoDerKyojin && xLadoIzq > xLadoIzqKyojin && yLadoAbajo > yLadoArribaKyojin && yLadoAbajo < yLadoAbajoKyojin);
	}
	
	//Colisiones de mikasa y obstaculos
	public boolean colisionMikasaObstaculoDer() {
		for (int i = 0; i < obstaculos.length; i++) {
			int xLadoDer = this.mika.getXLadoDer(); //Lados necesarios para detectar colision de Mikasa.
			int yLadoArriba = this.mika.getYLadoArriba();
			int yLadoAbajo = this.mika.getYLadoAbajo();
			
			int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
			int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
			int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
			int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
			
			if(xLadoDer+4 >= xLadoIzqObstaculo && xLadoDer <= xLadoDerObstaculo && ((yLadoArriba >= yLadoAbajoObstaculo && yLadoAbajo <= yLadoAbajoObstaculo) || (yLadoArriba <= yLadoAbajoObstaculo && yLadoAbajo >= yLadoArribaObstaculo))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean colisionMikasaObstaculoIzq() {
		for (int i = 0; i < obstaculos.length; i++) {
			int xLadoIzq = this.mika.getXLadoIzq(); //Lados necesarios para detectar colision de Mikasa.
			int yLadoArriba = this.mika.getYLadoArriba();
			int yLadoAbajo = this.mika.getYLadoAbajo();
			
			int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
			int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
			int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
			int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
			
			if(xLadoIzq-4 <= xLadoDerObstaculo && xLadoIzq >= xLadoIzqObstaculo && ((yLadoArriba >= yLadoAbajoObstaculo && yLadoAbajo <= yLadoAbajoObstaculo) || (yLadoArriba <= yLadoAbajoObstaculo && yLadoAbajo >= yLadoArribaObstaculo))) {
				return true;
			}
		}
		return false;
	}
	public boolean colisionMikasaObstaculoArriba() {
		for (int i = 0; i < obstaculos.length; i++) {
			int xLadoDer = this.mika.getXLadoDer(); //Lados necesarios para detectar colision de Mikasa.
			int xLadoIzq = this.mika.getXLadoIzq();
			int yLadoArriba = this.mika.getYLadoArriba();
			
			int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
			int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
			int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
			int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
			
			if(yLadoArriba-4 <= yLadoAbajoObstaculo && yLadoArriba >= yLadoArribaObstaculo && ((xLadoDer >= xLadoIzqObstaculo && xLadoIzq <= xLadoDerObstaculo) || (xLadoIzq <= xLadoDerObstaculo && xLadoDer >= xLadoIzqObstaculo))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean colisionMikasaObstaculoAbajo() {
		for (int i = 0; i < obstaculos.length; i++) {
			int xLadoDer = this.mika.getXLadoDer(); //Lados necesarios para detectar colision de Mikasa.
			int xLadoIzq = this.mika.getXLadoIzq();
			int yLadoAbajo = this.mika.getYLadoAbajo();
			
			int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
			int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
			int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
			int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
			
			if(yLadoAbajo+4 >= yLadoArribaObstaculo && yLadoAbajo <= yLadoAbajoObstaculo && ((xLadoDer >= xLadoIzqObstaculo && xLadoIzq <= xLadoDerObstaculo) || (xLadoIzq <= xLadoDerObstaculo && xLadoDer >= xLadoIzqObstaculo))) {
				return true;
			}
		}
		return false;
	}
	
	//Colisiones Kyojines con obstaculos
	public boolean colisionKyojinObstaculoDer(int indice) {
		for (int i = 0; i < obstaculos.length; i++) {
			if (this.enemigos[indice] == null) {
				return false;
			}
			else {
				int xLadoDer = this.enemigos[indice].getXLadoDer(); //Definicion de los lados necesarios del Kyojin
				int yLadoArriba = this.enemigos[indice].getYLadoArriba();
				int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
		
				int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
				int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
				int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
				int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
				
				if(xLadoDer+4 > xLadoIzqObstaculo && xLadoDer < xLadoDerObstaculo && ((yLadoArriba > yLadoAbajoObstaculo && yLadoAbajo < yLadoAbajoObstaculo) || (yLadoArriba < yLadoAbajoObstaculo && yLadoAbajo > yLadoArribaObstaculo))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean colisionKyojinObstaculoIzq(int indice) {
		for (int i = 0; i < obstaculos.length; i++) {
			if (this.enemigos[indice] == null) {
				return false;
			}
			else {
				int xLadoIzq = this.enemigos[indice].getXLadoIzq(); //Definicion de los lados necesarios del Kyojin
				int yLadoArriba = this.enemigos[indice].getYLadoArriba();
				int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
				
				int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
				int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
				int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
				int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
				
				if(xLadoIzq-4 < xLadoDerObstaculo && xLadoIzq > xLadoIzqObstaculo && ((yLadoArriba > yLadoAbajoObstaculo && yLadoAbajo < yLadoAbajoObstaculo) || (yLadoArriba < yLadoAbajoObstaculo && yLadoAbajo > yLadoArribaObstaculo))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean colisionKyojinObstaculoArriba(int indice) {
		for (int i = 0; i < obstaculos.length; i++) {
			if (this.enemigos[indice] == null) {
				return false;
			}
			else {
				int xLadoDer = this.enemigos[indice].getXLadoDer(); //Definicion de los lados necesarios del Kyojin
				int xLadoIzq = this.enemigos[indice].getXLadoIzq();
				int yLadoArriba = this.enemigos[indice].getYLadoArriba();
				
				int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
				int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
				int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
				int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
				
				if(yLadoArriba-4 < yLadoAbajoObstaculo && yLadoArriba > yLadoArribaObstaculo && ((xLadoDer > xLadoIzqObstaculo && xLadoIzq < xLadoDerObstaculo) || (xLadoIzq < xLadoDerObstaculo && xLadoDer > xLadoIzqObstaculo))) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean colisionKyojinObstaculoAbajo(int indice) {
		for (int i = 0; i < obstaculos.length; i++) {
			if (this.enemigos[indice] == null) {
				return false;
			}
			else {
				int xLadoDer = this.enemigos[indice].getXLadoDer(); //Definicion de los lados necesarios del Kyojin
				int xLadoIzq = this.enemigos[indice].getXLadoIzq();
				int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
				
				int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer(); //Definicion de los lados de los obstaculos
				int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
				int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
				int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
				
				if(yLadoAbajo+4 > yLadoArribaObstaculo && yLadoAbajo < yLadoAbajoObstaculo && ((xLadoDer > xLadoIzqObstaculo && xLadoIzq < xLadoDerObstaculo) || (xLadoIzq < xLadoDerObstaculo && xLadoDer > xLadoIzqObstaculo))) {
					return true;
				}
			}
		}
		return false;
	}
	//Colisiones entre kyojines
	public boolean colisionKyojinKyojinDer(int indice, int indiceSegundo) {
		if (indiceSegundo == indice || this.enemigos[indiceSegundo] == null) {
			return false;
		}
		else {
			int xLadoDer = this.enemigos[indice].getXLadoDer();  //Definicion de los lados necesarios del primer Kyojin
			int yLadoArriba = this.enemigos[indice].getYLadoArriba();
			int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
		
			int xLadoDerSegundo = this.enemigos[indiceSegundo].getXLadoDer(); //Definicion de los lados necesarios del segundo Kyojin
			int xLadoIzqSegundo = this.enemigos[indiceSegundo].getXLadoIzq();
			int yLadoArribaSegundo = this.enemigos[indiceSegundo].getYLadoArriba();
			int yLadoAbajoSegundo = this.enemigos[indiceSegundo].getYLadoAbajo();
				
			return(xLadoDer+4 > xLadoIzqSegundo && xLadoDer < xLadoDerSegundo && ((yLadoArriba > yLadoAbajoSegundo && yLadoAbajo < yLadoAbajoSegundo) || (yLadoArriba < yLadoAbajoSegundo && yLadoAbajo > yLadoArribaSegundo)));
		}
	}
	
	public boolean colisionKyojinKyojinIzq(int indice, int indiceSegundo) {
		if (indiceSegundo == indice || this.enemigos[indiceSegundo] == null) {
			return false;
		}
		else {
			int xLadoIzq = this.enemigos[indice].getXLadoIzq(); //Definicion de los lados necesarios del primer Kyojin
			int yLadoArriba = this.enemigos[indice].getYLadoArriba();
			int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
				
			int xLadoDerSegundo = this.enemigos[indiceSegundo].getXLadoDer(); //Definicion de los lados necesarios del segundo Kyojin
			int xLadoIzqSegundo = this.enemigos[indiceSegundo].getXLadoIzq();
			int yLadoArribaSegundo = this.enemigos[indiceSegundo].getYLadoArriba();
			int yLadoAbajoSegundo = this.enemigos[indiceSegundo].getYLadoAbajo();
				
			return(xLadoIzq-4 < xLadoDerSegundo && xLadoIzq > xLadoIzqSegundo && ((yLadoArriba > yLadoAbajoSegundo && yLadoAbajo < yLadoAbajoSegundo) || (yLadoArriba < yLadoAbajoSegundo && yLadoAbajo > yLadoArribaSegundo)));
		}
	}
	
	public boolean colisionKyojinKyojinArriba(int indice, int indiceSegundo) {
		if (indiceSegundo == indice || this.enemigos[indiceSegundo] == null) {
			return false;
		}
		else {
			int xLadoDer = this.enemigos[indice].getXLadoDer(); //Definicion de los lados necesarios del primer Kyojin
			int xLadoIzq = this.enemigos[indice].getXLadoIzq();
			int yLadoArriba = this.enemigos[indice].getYLadoArriba();
				
			int xLadoDerSegundo = this.enemigos[indiceSegundo].getXLadoDer(); //Definicion de los lados necesarios del segundo Kyojin
			int xLadoIzqSegundo = this.enemigos[indiceSegundo].getXLadoIzq();
			int yLadoArribaSegundo = this.enemigos[indiceSegundo].getYLadoArriba();
			int yLadoAbajoSegundo = this.enemigos[indiceSegundo].getYLadoAbajo();
				
			return(yLadoArriba-4 < yLadoAbajoSegundo && yLadoArriba > yLadoArribaSegundo && ((xLadoDer > xLadoIzqSegundo && xLadoIzq < xLadoDerSegundo) || (xLadoIzq < xLadoDerSegundo && xLadoDer > xLadoIzqSegundo)));
		}
	}

	public boolean colisionKyojinKyojinAbajo(int indice, int indiceSegundo) {
		if (indiceSegundo == indice || this.enemigos[indiceSegundo] == null) {
			return false;
		}
		else {
			int xLadoDer = this.enemigos[indice].getXLadoDer(); //Definicion de los lados necesarios del primer Kyojin
			int xLadoIzq = this.enemigos[indice].getXLadoIzq();
			int yLadoAbajo = this.enemigos[indice].getYLadoAbajo();
				
			int xLadoDerSegundo = this.enemigos[indiceSegundo].getXLadoDer(); //Definicion de los lados necesarios del segundo Kyojin
			int xLadoIzqSegundo = this.enemigos[indiceSegundo].getXLadoIzq();
			int yLadoArribaSegundo = this.enemigos[indiceSegundo].getYLadoArriba();
			int yLadoAbajoSegundo = this.enemigos[indiceSegundo].getYLadoAbajo();
				
			return(yLadoAbajo+4 > yLadoArribaSegundo && yLadoAbajo < yLadoAbajoSegundo && ((xLadoDer > xLadoIzqSegundo && xLadoIzq < xLadoDerSegundo) || (xLadoIzq < xLadoDerSegundo && xLadoDer > yLadoAbajoSegundo)));
		}
	}
	
	public boolean colisionKyojinKyojinGeneral(int indice) { //Agrupacion de las funciones especificas de colision de kyojines con kyojines solo con un indice.
		for (int i = 0; i < this.enemigos.length; i++) {
			if(colisionKyojinKyojinDer(indice,i) || colisionKyojinKyojinIzq(indice,i) || colisionKyojinKyojinArriba(indice,i) || colisionKyojinKyojinAbajo(indice,i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean impactoProyectilEnemigo(int indice) { // La punta del proyectil (depende la direccion/sentido) solo impacta con la cara visible del enemigo, es decir, el lado contrario al sentido del proyectil
		
		int bordeProyectil = proyectil.getBordeProyectil();
		int xProyectil = proyectil.getX();
		int yProyectil = proyectil.getY();
		
		int xLadoDerKyojin = this.enemigos[indice].getXLadoDer();
		int xLadoIzqKyojin = this.enemigos[indice].getXLadoIzq();
		int yLadoArribaKyojin = this.enemigos[indice].getYLadoArriba();
		int yLadoAbajoKyojin = this.enemigos[indice].getYLadoAbajo();
		
		if(proyectil.getSentido() == "arriba") { //Corrobora impacto proyectil con enemigo segun el sentido del mismo.
			return(bordeProyectil < yLadoAbajoKyojin && bordeProyectil > yLadoArribaKyojin && xProyectil > xLadoIzqKyojin && xProyectil < xLadoDerKyojin);
		}
		
		if(proyectil.getSentido() == "abajo") {
			return(bordeProyectil > yLadoArribaKyojin && bordeProyectil < yLadoAbajoKyojin && xProyectil > xLadoIzqKyojin && xProyectil < xLadoDerKyojin);
		}	
		
		if(proyectil.getSentido() == "derecha") {
			return(bordeProyectil > xLadoIzqKyojin && bordeProyectil < xLadoDerKyojin && yProyectil > yLadoArribaKyojin && yProyectil < yLadoAbajoKyojin);
		}
		
		if(proyectil.getSentido() == "izquierda") {
			return(bordeProyectil < xLadoDerKyojin && bordeProyectil > xLadoIzqKyojin && yProyectil > yLadoArribaKyojin && yProyectil < yLadoAbajoKyojin);
		}
		return false;
	}
	
	public boolean impactoProyectilObstaculo(int indice) { // La punta del proyectil (depende la direccion/sentido) solo impacta con la cara visible del obstaculo, es decir, el lado contrario al sentido del proyectil
		
			int bordeProyectil = proyectil.getBordeProyectil();
			int xProyectil = proyectil.getX();
			int yProyectil = proyectil.getY();
			
			int xLadoDerObstaculo = this.obstaculos[indice].getXLadoDer();
			int xLadoIzqObstaculo = this.obstaculos[indice].getXLadoIzq();
			int yLadoArribaObstaculo = this.obstaculos[indice].getYLadoArriba();
			int yLadoAbajoObstaculo = this.obstaculos[indice].getYLadoAbajo();
		
		if(proyectil.getSentido() == "arriba") {
				return(bordeProyectil < yLadoAbajoObstaculo && bordeProyectil > yLadoArribaObstaculo && xProyectil > xLadoIzqObstaculo && xProyectil < xLadoDerObstaculo);
			}
		
		if(proyectil.getSentido() == "abajo") {
				return(bordeProyectil > yLadoArribaObstaculo &&	bordeProyectil < yLadoAbajoObstaculo && xProyectil > xLadoIzqObstaculo && xProyectil < xLadoDerObstaculo);
			}	
		
		if(proyectil.getSentido() == "derecha") {
				return(bordeProyectil > xLadoIzqObstaculo && bordeProyectil < xLadoDerObstaculo && yProyectil > yLadoArribaObstaculo && yProyectil < yLadoAbajoObstaculo);
			}
		
		if(proyectil.getSentido() == "izquierda"){
				return(bordeProyectil < xLadoDerObstaculo && bordeProyectil > xLadoIzqObstaculo && yProyectil > yLadoArribaObstaculo && yProyectil < yLadoAbajoObstaculo);
			}
		
		return false;
	}
	
	public boolean deteccionProyectilErrado(int indice) { //Recibe un indice de un obstaculo para corroborar colisiones en el metodo impactoProyectilObstaculo
		if(this.proyectil != null) {
			if(this.proyectil.getSentido() == "derecha") { // Si el proyectil toca un borde de la pantalla o se corrobora en el metodo impactoProyectilObstaculo una colision segun la direccion del proyectil, se devuelve true.
				return(this.proyectil.getBordeProyectil() >=800 || (this.obstaculos[indice] != null && this.impactoProyectilObstaculo(indice)));
			}
			if(this.proyectil.getSentido() == "izquierda") { // Si el proyectil toca un borde de la pantalla o se corrobora en el metodo impactoProyectilObstaculo una colision segun la direccion del proyectil, se devuelve true.
				return(this.proyectil.getBordeProyectil() <= 0 || (this.obstaculos[indice] != null && this.impactoProyectilObstaculo(indice)));
			}
			if(this.proyectil.getSentido() == "arriba") { // Si el proyectil toca un borde de la pantalla o se corrobora en el metodo impactoProyectilObstaculo una colision segun la direccion del proyectil, se devuelve true.
				return(this.proyectil.getBordeProyectil() <= 0 || (this.obstaculos[indice] != null && this.impactoProyectilObstaculo(indice)));
			}
			if(this.proyectil.getSentido() == "abajo") { // Si el proyectil toca un borde de la pantalla o se corrobora en el metodo impactoProyectilObstaculo una colision segun la direccion del proyectil, se devuelve true.
				return(this.proyectil.getBordeProyectil() >= 600 || (this.obstaculos[indice] != null && this.impactoProyectilObstaculo(indice)));
			}
		}
		return false;
	}
	
	public boolean tocoSuero(int indice) {
		for (int i = 0; i < sueros.length; i++) {
			int xLadoDer = this.mika.getXLadoDer();
			int xLadoIzq = this.mika.getXLadoIzq();
			int yLadoArriba = this.mika.getYLadoArriba();
			int yLadoAbajo = this.mika.getYLadoAbajo();
				
			int xLadoDerSuero = this.sueros[indice].getXLadoDer();
			int xLadoIzqSuero = this.sueros[indice].getXLadoIzq();
			int yLadoArribaSuero = this.sueros[indice].getYLadoArriba();
			int yLadoAbajoSuero = this.sueros[indice].getYLadoAbajo();
				
			if(xLadoIzq <= xLadoDerSuero && xLadoIzq >= xLadoIzqSuero && ((yLadoArriba >= yLadoAbajoSuero && yLadoAbajo <= yLadoAbajoSuero) || (yLadoArriba <= yLadoAbajoSuero && yLadoAbajo >= yLadoArribaSuero))) {
				return true;
			}
			if(xLadoDer >= xLadoIzqSuero && xLadoDer <= xLadoDerSuero && ((yLadoArriba >= yLadoAbajoSuero && yLadoAbajo <= yLadoAbajoSuero) || (yLadoArriba <= yLadoAbajoSuero && yLadoAbajo >= yLadoArribaSuero))) {
				return true;
			}
			if(yLadoArriba <= yLadoAbajoSuero && yLadoArriba >= yLadoArribaSuero && ((xLadoDer >= xLadoIzqSuero && xLadoIzq <= xLadoDerSuero) || (xLadoIzq <= xLadoDerSuero && xLadoDer >= xLadoIzqSuero))) {
				return true;
			}
			if(yLadoAbajo >= yLadoArribaSuero && yLadoAbajo <= yLadoAbajoSuero && ((xLadoDer >= xLadoIzqSuero && xLadoIzq <= xLadoDerSuero) || (xLadoIzq <= xLadoDerSuero && xLadoDer >= xLadoIzqSuero))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean colisionSueroObstaculo(int indice) {
		for (int i = 0; i < this.obstaculos.length; i++) {
				int xLadoDer = this.sueros[indice].getXLadoDer();
				int xLadoIzq = this.sueros[indice].getXLadoIzq();
				int yLadoArriba = this.sueros[indice].getYLadoArriba();
				int yLadoAbajo = this.sueros[indice].getYLadoAbajo();
				
				int xLadoDerObstaculo = this.obstaculos[i].getXLadoDer();
				int xLadoIzqObstaculo = this.obstaculos[i].getXLadoIzq();
				int yLadoArribaObstaculo = this.obstaculos[i].getYLadoArriba();
				int yLadoAbajoObstaculo = this.obstaculos[i].getYLadoAbajo();
				
				if(xLadoIzq <= xLadoDerObstaculo && xLadoIzq >= xLadoIzqObstaculo && ((yLadoArriba >= yLadoAbajoObstaculo && yLadoAbajo <= yLadoAbajoObstaculo) || (yLadoArriba <= yLadoAbajoObstaculo && yLadoAbajo >= yLadoArribaObstaculo))) {
					return true;
				}
				if(xLadoDer >= xLadoIzqObstaculo && xLadoDer <= xLadoDerObstaculo && ((yLadoArriba >= yLadoAbajoObstaculo && yLadoAbajo <= yLadoAbajoObstaculo) || (yLadoArriba <= yLadoAbajoObstaculo && yLadoAbajo >= yLadoArribaObstaculo))) {
					return true;
				}
				if(yLadoArriba <= yLadoAbajoObstaculo && yLadoArriba >= yLadoArribaObstaculo && ((xLadoDer >= xLadoIzqObstaculo && xLadoIzq <= xLadoDerObstaculo) || (xLadoIzq <= xLadoDerObstaculo && xLadoDer >= xLadoIzqObstaculo))) {
					return true;
				}
				if(yLadoAbajo >= yLadoArribaObstaculo && yLadoAbajo <= yLadoAbajoObstaculo && ((xLadoDer >= xLadoIzqObstaculo && xLadoIzq <= xLadoDerObstaculo) || (xLadoIzq <= xLadoDerObstaculo && xLadoDer >= xLadoIzqObstaculo))) {
					return true;
				}
		}
		return false;
	}
	
	
	public boolean colisionSueroKyojin(int indice) {
		for (int i = 0; i < this.enemigos.length; i++) {
			if(this.enemigos[i]==null) {
				return false;
			}
			else {
				
				int xLadoDer = this.sueros[indice].getXLadoDer();
				int xLadoIzq = this.sueros[indice].getXLadoIzq();
				int yLadoArriba = this.sueros[indice].getYLadoArriba();
				int yLadoAbajo = this.sueros[indice].getYLadoAbajo();
				
				int xLadoDerKyojin = this.enemigos[i].getXLadoDer();
				int xLadoIzqKyojin = this.enemigos[i].getXLadoIzq();
				int yLadoArribaKyojin = this.enemigos[i].getYLadoArriba();
				int yLadoAbajoKyojin = this.enemigos[i].getYLadoAbajo();
				
				if(xLadoIzq <= xLadoDerKyojin && xLadoIzq >= xLadoIzqKyojin && ((yLadoArriba >= yLadoAbajoKyojin && yLadoAbajo <= yLadoAbajoKyojin) || (yLadoArriba <= yLadoAbajoKyojin && yLadoAbajo >= yLadoArribaKyojin))) {
					return true;
				}
				if(xLadoDer >= xLadoIzqKyojin && xLadoDer <= xLadoDerKyojin && ((yLadoArriba >= yLadoAbajoKyojin && yLadoAbajo <= yLadoAbajoKyojin) || (yLadoArriba <= yLadoAbajoKyojin && yLadoAbajo >= yLadoArribaKyojin))) {
					return true;
				}
				if(yLadoArriba <= yLadoAbajoKyojin && yLadoArriba >= yLadoArribaKyojin && ((xLadoDer >= xLadoIzqKyojin && xLadoIzq <= xLadoDerKyojin) || (xLadoIzq <= xLadoDerKyojin && xLadoDer >= xLadoIzqKyojin))) {
					return true;
				}
				if(yLadoAbajo >= yLadoArribaKyojin && yLadoAbajo <= yLadoAbajoKyojin && ((xLadoDer >= xLadoIzqKyojin && xLadoIzq <= xLadoDerKyojin) || (xLadoIzq <= xLadoDerKyojin && xLadoDer >= xLadoIzqKyojin))) {
					return true;
				}
			}
		}
		return false;
	}
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
