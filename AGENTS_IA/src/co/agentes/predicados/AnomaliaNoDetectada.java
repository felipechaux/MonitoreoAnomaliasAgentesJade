
package co.agentes.predicados;
import co.agentes.Señal;
import jade.content.Predicate;
/**
 * @author Felipe Chaux
 * Predicado anomalia no detectada
 */
public class AnomaliaNoDetectada implements Predicate {
    private Señal señal;

    public Señal getSeñal() {
        return señal;
    }

    public void setSeñal(Señal señal) {
        this.señal = señal;
    }
}
