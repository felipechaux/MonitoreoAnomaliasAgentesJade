/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.agentes.acciones;

import co.agentes.Señal;
import jade.content.AgentAction;

/**
 *
 * @author Felipe Chaux
 * Accion Notificar
 */
public class Notificar implements AgentAction {

    private Señal señal;

    public Señal getSeñal() {
        return señal;
    }

    public void setSeñal(Señal señal) {
        this.señal = señal;
    }

}
