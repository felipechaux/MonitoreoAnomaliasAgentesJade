/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.agentes.acciones;

import co.agentes.Señal;
import co.agentes.db.PersistenciaDB;
import jade.content.AgentAction;

/**
 *
 * @author Felipe Chaux
 * Accion guardar
 */
public class Guardar implements AgentAction {

    private Señal señal;

    public Señal getSeñal() {
        return señal;
    }

    public void setSeñal(Señal señal) {
        this.señal = señal;
    }
    
    public void GuardarN(Señal registro){
      ///  System.out.println("alojando "+registro.getEntrada());
        PersistenciaDB con = new PersistenciaDB();
        con.insertN(registro);
        
    }
     public void GuardarANM(Señal registro){
      ///  System.out.println("alojando "+registro.getEntrada());
        PersistenciaDB con = new PersistenciaDB();
        con.insertANM(registro);
        
    }
    

   

}
