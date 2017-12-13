
package co.agentes;
/**
 *
 * @author Felipe Chaux
 * Creacion de agentes
 */
public class Main {   
     public static void main(String[] args) throws InterruptedException { 
       String[]args1={"-gui",""};
       String[]args2={"-container","AgenteReceptorArduino:co.agentes.AgenteReceptorArduino;AgenteControl:co.agentes.AgenteControl"};
       jade.Boot.main(args1);
       jade.Boot.main(args2);
     
    }
    
}
