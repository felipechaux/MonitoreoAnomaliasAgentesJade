package co.agentes;

import co.agentes.predicados.AnomaliaDetectada;
import co.agentes.predicados.AnomaliaNoDetectada;
import co.agentes.acciones.Guardar;
import co.agentes.acciones.Ignorar;
import co.agentes.acciones.Notificar;
import co.agentes.predicados.AnomaliaIdentificada;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

/**
 *
 * @author Felipe Chaux Ontologia señal
 */
public class SeñalOntologia extends Ontology {

    // Nombre de la ontología
    public static final String ONTOLOGY_NAME = "ontología de señales";
    // Vocabulario de la ontología 
    public static final String SEÑAL = "Señal";

    public static final String SEÑAL_ENTRADA = "entrada";

    public static final String ANOMALIA_DETECTADA = "Anomalia";
    public static final String ANOMALIA_DETECTADA_SEÑAL = "señal";
    public static final String ANOMALIA_NO_DETECTADA = "SinAnomalia";
    public static final String ANOMALIA_NO_DETECTADA_SEÑAL = "señal";
    public static final String ANOMALIA_IDENTIFICADA = "AnomaliaIdentificada!!";
    public static final String ANOMALIA_IDENTIFICADA_SEÑAL = "señal";

    public static final String GUARDAR = "Guardar";
    public static final String GUARDAR_SEÑAL = "señal";
    public static final String IGNORAR = "Ignorar";
    public static final String IGNORAR_SEÑAL = "señal";
    public static final String NOTIFICAR = "Notificar!!";
    public static final String NOTIFICAR_SEÑAL = "señal";

    // Instancia de la ontología (que será única)
    private static Ontology instancia = new SeñalOntologia();

    // Método para acceder a la instancia de la ontología
    public static Ontology getInstance() {
        return instancia;
    }

    private SeñalOntologia() {
        //  se extiende la ontología básica
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            // Adicion de los elementos
            add(new ConceptSchema(SEÑAL), Señal.class);
            add(new PredicateSchema(ANOMALIA_DETECTADA), AnomaliaDetectada.class);
            add(new PredicateSchema(ANOMALIA_NO_DETECTADA), AnomaliaNoDetectada.class);
            add(new PredicateSchema(ANOMALIA_IDENTIFICADA), AnomaliaIdentificada.class);
            add(new AgentActionSchema(GUARDAR), Guardar.class);
            add(new AgentActionSchema(IGNORAR), Ignorar.class);
             add(new AgentActionSchema(NOTIFICAR), Notificar.class);

            // Estructura del esquema para el concepto SEÑAL
            ConceptSchema cs = (ConceptSchema) getSchema(SEÑAL);
            cs.add(SEÑAL_ENTRADA, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            // Estructura del esquema para el predicado ANOMALIA DETECTADA
            PredicateSchema ps = (PredicateSchema) getSchema(ANOMALIA_DETECTADA);
            ps.add(ANOMALIA_DETECTADA_SEÑAL, (ConceptSchema) getSchema(SEÑAL));
            // Estructura del esquema para el predicado ANOMALIA NO DETECTADA
            PredicateSchema ps1 = (PredicateSchema) getSchema(ANOMALIA_NO_DETECTADA);
            ps1.add(ANOMALIA_NO_DETECTADA_SEÑAL, (ConceptSchema) getSchema(SEÑAL));
            // Estructura del esquema para el predicado ANOMALIA IDENTIFICADA
            PredicateSchema ps2 = (PredicateSchema) getSchema(ANOMALIA_IDENTIFICADA);
            ps2.add(ANOMALIA_IDENTIFICADA_SEÑAL, (ConceptSchema) getSchema(SEÑAL));

            // Estructura del esquema para la acción GUARDAR
            AgentActionSchema as = (AgentActionSchema) getSchema(GUARDAR);
            as.add(GUARDAR_SEÑAL, (ConceptSchema) getSchema(SEÑAL));
            // Estructura del esquema para la acción IGNORAR
            AgentActionSchema as1 = (AgentActionSchema) getSchema(IGNORAR);
            as1.add(IGNORAR_SEÑAL, (ConceptSchema) getSchema(SEÑAL));
            // Estructura del esquema para la acción NOTIFICAR
            AgentActionSchema as2 = (AgentActionSchema) getSchema(NOTIFICAR);
            as2.add(NOTIFICAR_SEÑAL, (ConceptSchema) getSchema(SEÑAL));
            

        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

}
