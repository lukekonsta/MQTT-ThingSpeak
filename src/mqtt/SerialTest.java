/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Loukas
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.Environment;

public class SerialTest implements SerialPortEventListener {

    SerialPort serialPort;
    int counter = 0;
    String one, two, three, four, five, six, seven, eight;
    String[] totals = new String[8];

    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyACM0", // Raspberry Pi
        "/dev/ttyUSB0", // Linux
        "COM5", // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 115200;

    public void initialize() {
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();

                if (inputLine.equals("H#") || inputLine.equals("-------------------------------------------------------")) {

                    //
                } else {

                    //System.out.println(inputLine);

                    if (counter == 0) {

                        System.out.println("Temperature");
                        totals[counter] = inputLine;
                        one = inputLine;
                        System.out.println(one);

                    } else if (counter == 1) {

                        System.out.println("Humidity");
                        totals[counter] = inputLine;
                        two = inputLine;
                        System.out.println(two);

                    } else if (counter == 2) {

                        System.out.println("Pressure");
                        totals[counter] = inputLine;
                        three = inputLine;
                        System.out.println(three);

                    } else if (counter == 3) {

                        System.out.println("Watermark");
                        totals[counter] = inputLine;
                        four = inputLine;
                        System.out.println(four);

                    } else if (counter == 4) {
                        System.out.println("Leaf");
                        totals[counter] = inputLine;
                        five = inputLine;
                        System.out.println(five);

                    } else if (counter == 5) {

                        System.out.println("Anemometer");
                        totals[counter] = inputLine;
                        six = inputLine;
                        System.out.println(six);

                    } else if (counter == 6) {

                        System.out.println("Vane");
                        totals[counter] = inputLine;
                        seven = inputLine;
                        System.out.println(seven);

                    } else if (counter == 7) {

                        System.out.println("Pluviometer");
                        totals[counter] = inputLine;
                        eight = inputLine;
                        System.out.println(eight);
                    }

                    counter += 1;

                    if (counter > 7) {
                        MQTT mqtt = new MQTT();
                        mqtt.publish("field1=" + one + "&field2=" + two + "&field3=" + three + "&field4=" + four + "&field5=" + five + "&field6=" + six + "&field7=" + seven + "&field8=" + eight);

                        counter = 0;
                    }

                }

                FileWriter fw = null;
                PrintWriter pw = null;
                try {
                    fw = new FileWriter("weatherdata.txt", true);
                    pw = new PrintWriter(fw);

                    pw.write(inputLine + System.lineSeparator());
                    pw.close();
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(ex.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    /*public static void main(String[] args) throws Exception {
        SerialTest main = new SerialTest();
        main.initialize();
        Thread t = new Thread() {
            @Override
            public void run() {
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ie) {
                }
            }
        };
        t.start();
        System.out.println("Started");
    }*/
}
