package co.agentes;

import co.agentes.predicados.AnomaliaDetectada;
import co.agentes.predicados.AnomaliaNoDetectada;
import co.agentes.acciones.Guardar;
import co.agentes.acciones.Ignorar;
import co.agentes.acciones.Notificar;
import co.agentes.predicados.AnomaliaIdentificada;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Felipe Chaux Agente Control
 */
public class AgenteControl extends Agent {

    private Codec codec = new SLCodec();
    private Ontology ontologia = SeñalOntologia.getInstance();
    int anomalias = 0;

    // comportamiento que permite recibir un mensaje y contestarlo
    SequentialBehaviour seq = new SequentialBehaviour();

    class rem extends SequentialBehaviour {

    }

    class RespuestaComportamiento extends SimpleBehaviour {

        private boolean finished = false;

        public RespuestaComportamiento(Agent a) {
            super(a);
        }

        public void action() {

            System.out.println("\nEsperando señal arduino  del ReceptorArduino....");

            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchLanguage(codec.getName()),
                    MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage msg = blockingReceive(mt);

            try {

                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                        System.out.println("Mensaje No entendido recibido");
                    } else if (msg.getPerformative() == ACLMessage.INFORM) {

                        ContentElement ce = getContentManager().extractContent(msg);
                        // System.out.print("lo que extrajo el comprador :"+ce);
                        if (ce instanceof AnomaliaDetectada) {
                            //  INFORM con contenido correcto
                            AnomaliaDetectada ad = (AnomaliaDetectada) ce;
                            Señal sig = ad.getSeñal();
                            System.out.println("Mensaje recibido:");
                            System.out.println("Entrada: " + sig.getEntrada());

                            //Guardar anomalia en BD
                            Guardar gd = new Guardar();
                            gd.setSeñal(sig);
                            gd.GuardarANM(sig);
                            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                            msg2.setLanguage(codec.getName());
                            msg2.setOntology(ontologia.getName());
                            msg2.setSender(getAID());
                            msg2.addReceiver(msg.getSender());
                            getContentManager().fillContent(msg2, gd);
                            send(msg2);
                            System.out.println("Anomalia Alojada en BD");
                           
                            //anomalias++;
                            //System.out.println("Anomalias indentificadas "+anomalias);
                        } else if (ce instanceof AnomaliaNoDetectada) {

                            // INFORM con contenido correcto
                            AnomaliaNoDetectada an = (AnomaliaNoDetectada) ce;
                            Señal sig = an.getSeñal();
             
                            System.out.println("Mensaje recibido:");
                            System.out.println("Entrada: " + sig.getEntrada());
                            System.out.println("Entorno...");
                            Ignorar iA = new Ignorar();
                            iA.setSeñal(sig);
                            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                            msg2.setLanguage(codec.getName());
                            msg2.setOntology(ontologia.getName());
                            msg2.setSender(getAID());
                            msg2.addReceiver(msg.getSender());
                            getContentManager().fillContent(msg2, iA);
                            send(msg2);
                        

                        }else if (ce instanceof AnomaliaIdentificada){
                            
                            AnomaliaIdentificada an = (AnomaliaIdentificada) ce;
                            Señal sig = an.getSeñal();
             
                            System.out.println("Anomalia Identificada!!!!");
                            //System.out.println("Entrada: " + sig.getEntrada());
                            //Guardar Notificar 
                            Notificar n = new Notificar();
                            n.setSeñal(sig);
                            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                            msg2.setLanguage(codec.getName());
                            msg2.setOntology(ontologia.getName());
                            msg2.setSender(getAID());
                            msg2.addReceiver(msg.getSender());
                            getContentManager().fillContent(msg2, n);
                            send(msg2);
                            System.out.println(msg2);
                            
                        }
                        
                        else {
                            // INFORM con contenido incorrecto
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                            reply.setContent("( NO ENTIENDO!!)");
                            send(reply);
                        }

                    } else {
                        //performativa incorrecta
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        reply.setContent("( NO ENTIENDO!!)" + ACLMessage.getPerformative(msg.getPerformative()) + ")( expected (inform)))");
                        send(reply);
                    }
                } else {
                    //System.out.println("No message received");
                }

            } catch (jade.content.lang.Codec.CodecException ce) {
                System.out.println(ce);
            } catch (jade.content.onto.OntologyException oe) {
                System.out.println(oe);
            }
        }

        public boolean done() {
            return finished;
        }

    }

    protected void setup() {

        System.out.println(getName() + " preparado");
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        RespuestaComportamiento PingBehaviour;
        PingBehaviour = new RespuestaComportamiento(this);
        addBehaviour(PingBehaviour);
    }

    protected void takeDown() {
        System.out.println("Destruccion de agente ");
    }

}
