package mqtt;

import gnu.io.SerialPort;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import org.apache.commons.io.IOUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.JSONArray;
import org.json.JSONObject;

public class MQTT {

    private MqttClient client;
    private MqttConnectOptions conOpt;

    String brokerUrl = "tcp://mqtt.thingspeak.com:1883";
    String clientId = "ExamplePublish";
    String channel = "channels/438939/publish/DBOEJJCM6P78O4L9";
    //String channel2 = "channels/438939/subscribe/fields/field1/PLFHAHYRI5FFIDZM";
    int qos = 0;

    public MQTT() throws MqttException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(true);
            //conOpt.
            // Construct an MQTT blocking mode client
            client = new MqttClient(brokerUrl, MqttClient.generateClientId(), dataStore);

            // Set this wrapper as the callback handler
            client.setCallback(new MqttCallback() {

                @Override
                public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
                    // TODO Auto-generated method stub
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    System.out.println("Time:\t" + time
                            + "  Topic:\t" + arg0
                            + "  Message:\t" + new String(arg1.getPayload())
                            + "  QoS:\t" + arg1.getQos());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {
                    // TODO Auto-generated method stub

                    try {
                        
                        
                        System.out.println(arg0.getMessageId() + " " + arg0.getMessage());
                        System.out.println();
                    } catch (MqttException e) {
                        // TODO Auto-generated catch block

                    }
                }

                @Override
                public void connectionLost(Throwable arg0) {
                    // TODO Auto-generated method stub

                    System.out.println("Connection to " + brokerUrl + " lost!");

                    System.out.println("Reconnecting..");
                    try {
                        client.connect(conOpt);
                        //System.out.println("Connected1");
                    } catch (MqttException e) {
                        // TODO Auto-generated catch block

                    }
                    //	System.exit(1);
                    //client.
                }
            });
            if (client.isConnected()) {

                System.out.println("was already Connected");
                client.disconnect();
            }
            client.connect(conOpt);
            //System.out.println("Connected2");

        } catch (MqttException e) {
            System.out.println("Unable to set up client: " + e.toString());
            System.exit(1);
        }

    }

    public void publish(String data) throws MqttPersistenceException, MqttException {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("Publishing at: " + time + " to topic \"" + channel + "\" qos " + qos);

        // Create and configure a message
        MqttMessage message = new MqttMessage(data.getBytes());
        //System.out.println("Message");
        //System.out.println(message);
        message.setQos(qos);
        message.setRetained(false);
        //System.out.println(message);
        client.publish(channel, message);

        // Disconnect the client
    }

    public void subscribe(String topicName) throws MqttException {
        client.connect();
        System.out.println("Connected to " + brokerUrl + " with client ID " + client.getClientId());

        // Subscribe to the topic
        System.out.println("Subscribing to topic \"" + topicName + "\" qos " + qos);
        client.subscribe(topicName, qos);

        // Block until Enter is pressed
        System.out.println("Press <Enter> to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            //If we can't read we'll just exit
        }
        client.disconnect();
        System.out.println("Disconnected");
    }

    public void close() throws MqttException {
        client.disconnect();
        System.out.println("Disconnected");
    }

    public static void main(String[] args) throws MqttException, InterruptedException, URISyntaxException {
        
        
       
            
        /*MQTT mqtt = new MQTT();
        String i = "8";
        mqtt.publish("field1=" + i + "&field2=" + i + "&field3=" + i + "&field4=" + i + "&field5=" + i + "&field6=" + i + "&field7=" + i+ "&field8=" + i);*/
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
                      
                       
        
    }//end of main 

}
