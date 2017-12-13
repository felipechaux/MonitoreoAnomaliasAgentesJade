/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.agentes.predicados;

import co.agentes.Señal;
import jade.content.Predicate;

/**
 *
 * @author Felipe Chaux
 * Predicado anomalia identificada
 */
public class AnomaliaIdentificada implements Predicate {

    private Señal señal;

    public Señal getSeñal() {
        return señal;
    }

    public void setSeñal(Señal señal) {
        this.señal = señal;
    }

}
