package co.agentes;

import co.agentes.db.PersistenciaDB;
import co.agentes.predicados.AnomaliaDetectada;
import co.agentes.predicados.AnomaliaIdentificada;
import co.agentes.predicados.AnomaliaNoDetectada;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author Felipe Chaux Agente receptor de arduino
 */
public class AgenteReceptorArduino extends Agent implements SerialPortEventListener {

    private Codec codec = new SLCodec();
    private Ontology ontologia = SeñalOntologia.getInstance();
    static int cont = 0;

    SerialPort serialPort;
    public BufferedReader input;
    public OutputStream output;
    public static final int TIME_OUT = 0;
    public final String PUERTO = "COM3";
    public static final int DATA_RATE = 9600;
    String inputLine;
    public static boolean comp;

    File drAudio1 = new File("sounds/AnomaliaIdentificada.wav");
    File drAudio2 = new File("sounds/Anomalia.wav");

    //long tini;
    public void inicializarConexionArduino() {

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //busqueda de una instancia de puerto serie como se establece en PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier actualPuertoID = (CommPortIdentifier) portEnum.nextElement();
            if (PUERTO.equals(actualPuertoID.getName())) {
                portId = actualPuertoID;
                break;
            }
        }
        if (portId == null) {
            // System.out.println("No se pudo conectar al puerto COM");
            return;
        }

        try {

            //se abre el puerto serie y se usa el nombre de clase para el appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            //establecer parametros de puerto
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // se abren las transmisiones 
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            //output = serialPort.getOutputStream();
            // se agregan los eventos que escucharam
            serialPort.addEventListener((SerialPortEventListener) this);
            serialPort.notifyOnDataAvailable(true);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void serialEvent(SerialPortEvent oEvent) {
        //si hay datos disponibles
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                inputLine = input.readLine();
                EntitySerial s = new EntitySerial();
                s.setInput(inputLine);

                // System.out.println("Arduino " + inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

    }

    class EnviarMensajeBehaviour extends SimpleBehaviour {

        private boolean finished = false;

        public EnviarMensajeBehaviour(Agent a) {
            super(a);

        }

        public void action() {

            try {
                drAudio1 = new File("sounds/Anomalia.wav");
                drAudio2 = new File("sounds/AnomaliaIdentificada.wav");
                Sound sonidos = new Sound();
                EntitySerial s = new EntitySerial();
                // System.out.println("datos ARDUINOreceptor : "+s.getInput());
      
                String respuesta = "AgenteControl";
                AID r = new AID();
                r.setLocalName(respuesta);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());
                //System.out.println("\n Ingresar señal");

                respuesta = s.getInput();
                ///////////////////////////////
                Señal sig = new Señal();
                sig.setEntrada(respuesta);

                //AnomaliaDetectada a = new AnomaliaDetectada();
                //a.setSeñal(sig);
                PersistenciaDB pr = new PersistenciaDB();

                // System.out.println("resultado1 " + pr.consultaN(sig));
                if (comp == true && pr.consultaN(sig) != null) {
                    // System.out.println("ya se puede hacer alfo");
                    if (pr.consultaN(sig) == false) {

                        System.out.println("anomalias!!");

                        if (pr.consultaANM(sig) == false && pr.consultaANM(sig) != null) {
                            System.out.println("no existe anomalia -- alojar");
                            sonidos.Play(drAudio1);

                            AnomaliaDetectada a2 = new AnomaliaDetectada();
                            a2.setSeñal(sig);
                            getContentManager().fillContent(msg, a2);

                        } else {
                            System.out.println(" ----anomalia identificada");
                            sonidos.Play(drAudio2);

                            AnomaliaIdentificada ai = new AnomaliaIdentificada();
                            ai.setSeñal(sig);
                            getContentManager().fillContent(msg, ai);

                        }

                    } else {

                        System.out.println("no anomalias");

                        AnomaliaNoDetectada a = new AnomaliaNoDetectada();
                        a.setSeñal(sig);
                        getContentManager().fillContent(msg, a);

                    }

                    send(msg);
                } else {
                    // System.out.println("aun nada");
                }

            } /*catch (java.io.IOException io) {
                System.out.println(io);
            }*/ catch (jade.content.lang.Codec.CodecException ce) {
                System.out.println(ce);
            } catch (jade.content.onto.OntologyException oe) {
                System.out.println("agente receptor excepcion:" + oe);
            } catch (Exception e) {
                System.out.println("\n\n... Terminando ...");
                finished = true;
            }
        }

        public boolean done() {

            //return finished;
            return finished;
        }

    }

    class PrimerBehavior extends TickerBehaviour {

        //int minticks;
        EntitySerial s = new EntitySerial();
        PersistenciaDB pr = new PersistenciaDB();
        Señal sig = new Señal();

        public PrimerBehavior(Agent a, long intervalo) {
            super(a, intervalo);

        }

        public void reset() {
            super.reset();
            //minticks = 0;
            System.out.println("reseteo!");
        }

        @Override
        //////////////////////////////////10 segundos de percepcion
        protected void onTick() {

            // long tfin = System.currentTimeMillis() - tini;
            int nticks = getTickCount(); // obtenemos el numero de ticks desde el último reset
            //  minticks++;
            if (nticks == 10) {
                System.out.println("aprendizaje terminado");
                comp = true;
                stop();
            } else {

                sig.setEntrada(s.getInput());
                pr.insertN(sig);
                System.out.println("aprendizaje.." + s.getInput());

            }

        }

    }

    protected void setup() {

        System.out.println(getName() + " preparado");

        inicializarConexionArduino();

        //tini = System.currentTimeMillis();
        addBehaviour(new PrimerBehavior(this, 100));

        // 
        /**
         * Registro en el DF
         */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("AgenteReceptorArduino");
        sd.setName(getName());
        sd.setOwnership("ARNOIA");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " error al registrar en el facilitador de directorio . Razon: " + e.getMessage());
            doDelete();
        }

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

        EnviarMensajeBehaviour EnviarBehaviour = new EnviarMensajeBehaviour(this);
        addBehaviour(EnviarBehaviour);
        //MiComportamiento m = new MiComportamiento(this);
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
