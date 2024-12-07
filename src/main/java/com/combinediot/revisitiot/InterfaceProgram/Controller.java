package com.combinediot.revisitiot.InterfaceProgram;


import com.combinediot.revisitiot.sensorProgram.CoAPServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements MqttCallback, Initializable {
	
	@FXML private AnchorPane mainAnchorPane;
	

	@FXML private TextField BrokerURLTextField;
	@FXML private TextField ClientIDtxtField;
	@FXML private TextField keepAliveIntTxtField;
	@FXML private CheckBox setCleanSessionCheckBox;
	@FXML private Button ConnectToBrokerBtn;
	@FXML private TextArea SubsTopicTextArea;
	
	//@FXML private TextField SubsTopicTxtField;
	@FXML private ChoiceBox<String> subscribChoiceBox;
	ObservableList<String> subscribeChoiceBoxList = (ObservableList<String>) FXCollections.observableArrayList(CoAPServer.resourceList);
	
	ObservableList<Integer> QosLvlChoiceBoxList = (ObservableList<Integer>) FXCollections.observableArrayList(0,1,2);
	@FXML private ChoiceBox<Integer> QosLvlChoiceBox;
	
	@FXML private ListView<String> subscribedListView;
	
	
	@FXML private Button SubsTopicBtn;
	@FXML private TextArea SubsTopicMsgTextArea;
	
	ObservableList<String> unSubsChoiceBoxList;
	@FXML private ChoiceBox<String> unsubscribChoiceBox;
	@FXML private TextField UnsubTopicTextfield;
	@FXML private Button unsubsTopicBtn;
	
	
	
	@FXML private Button DisconnectExitBtn;
	@FXML private Button RefreshPublishMsg;
	
	
	private String topic ;//       = "MQTT Examples";
	private String payload; //      = "Message from MqttPublishSample";
	private int qos;//             = 2;
	private String brokerURL;//       = "tcp://localhost:1883";//"tcp://broker.mqttdashboard.com:1883";
	private String clientId ;//    = "MQTTclientID";
	private MemoryPersistence persistence;
	protected boolean retained;
	private Integer keepAlive;
	private Boolean cleanSession;
	
	private MqttClient client;
	MqttConnectOptions connOpts;
	
	
	StringBuilder txt;
	
	public void MQTTconnect(ActionEvent event) {
    	
		System.out.println("Connect btn presssed!!!");
		
		brokerURL = BrokerURLTextField.getText();
		clientId = ClientIDtxtField.getText();
		keepAlive = Integer.parseInt(keepAliveIntTxtField.getText());
		cleanSession = setCleanSessionCheckBox.isSelected();
		//txt = new StringBuilder();
		
		//System.out.println(brokerURL + clientId + keepAlive + cleanSession);
		
		try {
			client = new MqttClient(brokerURL, clientId, new MemoryPersistence());
			client.setCallback(this);
			
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(cleanSession);
			connOpts.setKeepAliveInterval(keepAlive);
			
	        System.out.println("Connecting to broker: "+brokerURL);
	        client.connect();
	        System.out.println("Connected to broker: " + brokerURL + " with clientID: "+ clientId);
	        
	        //enable the buttons for subscribe, unsubscribe and disconnect
			ConnectToBrokerBtn.setDisable(true);
			SubsTopicBtn.setDisable(false);
			unsubsTopicBtn.setDisable(false);
			RefreshPublishMsg.setDisable(false);
			DisconnectExitBtn.setDisable(false);
	        
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
	
	
	public void MQTTsubscribe(ActionEvent event) {
		System.out.println("MQTTsubscribe btn presssed!!!");
		String topic = subscribChoiceBox.getValue().replace("/", "");
		System.out.println(topic);
		//topic = SubsTopicTxtField.getText();
		qos = QosLvlChoiceBox.getValue();
		//ListView<String>subList = new ListView<>();
		//subList.setCellFactory(TextField)
		boolean isAreadySubscibed = false;
		
		try {
			//System.out.println("Subscribing to topic \"" + topic +"\" qos " + qos + subscribedListView.getItems().isEmpty());
		
			if(subscribedListView.getItems().isEmpty() == true)
			{
				client.subscribe(topic, qos);
				subscribedListView.getItems().add(String.valueOf(topic));
				unsubscribChoiceBox.setValue(subscribedListView.getItems().get(0));
				unsubscribChoiceBox.setItems(subscribedListView.getItems());
			}
			else if(subscribedListView.getItems().isEmpty() == false)
			{
				for(int i=0; i < subscribedListView.getItems().size(); i++) {
					if(subscribedListView.getItems().get(i).toString().equals(topic))
					{
						System.err.println(subscribedListView.getItems().get(i) + "is already subscribed");
						isAreadySubscibed = true;
					}
				}
				if(isAreadySubscibed == false) {
					client.subscribe(topic, qos);
					subscribedListView.getItems().add(String.valueOf(topic));
				}
				unsubscribChoiceBox.setValue(subscribedListView.getItems().get(0));
				unsubscribChoiceBox.setItems(subscribedListView.getItems());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}    	
    	//ConnectMQTTbutton.setVisible(false);
        //label.setVisible(true);
    }
	
	public void MQTTunsubscribe(ActionEvent event) {
    	System.out.println("MQTTunsubscribe btn presssed!!!");
    	unsubscribChoiceBox.setItems((ObservableList<String>) subscribedListView.getItems());
    	try {
    		//ConnectMQTTbutton.setVisible(false);
            //label.setVisible(true);
        	System.out.println("Unsubscribe the topic from the MQTT broker");
    		//String testUnsubscribeTopic = UnsubTopicTextfield.getText();
        	String testUnsubscribeTopic = unsubscribChoiceBox.getValue().toString();
        	client.unsubscribe(testUnsubscribeTopic);
        	
        	subscribedListView.getItems().remove(testUnsubscribeTopic);//remove the unsubscribed topic from the subscribed list
        	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
  
    }
	
	public void MQTTdisconnnect(ActionEvent event) {
    	System.out.println("MQTTdisconnnect btn presssed!!!");
		try {		
			
			if(client.isConnected())
			{
				
				//if you are still subscribed to a topic when disconnecting, unsubsribe them automatic
				if(!subscribedListView.getItems().isEmpty())
				{
					ObservableList<String> subslist = subscribedListView.getItems();
					for (String item : subslist)
					 {
						//unsubscribe to all current topics
						client.unsubscribe(item);
					    //System.out.println( (item + "\n") );
					 }
					subscribedListView.getItems().removeAll(subslist);
					//subscribedListView.refresh();
				}
				
				client.disconnect();
				//after disconnecting, make buttons except for connect disabled
				ConnectToBrokerBtn.setDisable(false);
				SubsTopicBtn.setDisable(true);
				unsubsTopicBtn.setDisable(true);
				RefreshPublishMsg.setDisable(true);
				DisconnectExitBtn.setDisable(true);
				
				//Platform.exit();
				//client.close();
				//System.out.println("Closing and Disconnecting from the MQTT Broker!");
			}
			else
			{
				System.out.println("Cannot Diconnect!");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		    	
    	//ConnectMQTTbutton.setVisible(false);
        //label.setVisible(true);
    }
	
	@FXML private void refreshMsgFromBroker(ActionEvent event) {
		SubsTopicMsgTextArea.clear();
	}


	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.err.println("Topic: "+ topic + " Message: " + message);
		SubsTopicMsgTextArea.appendText("Topic: "+ topic + " Message: " + message + "\n");
	}


	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set buttons to enabled or disabled until they have been connected to a broker or not later
		ConnectToBrokerBtn.setDisable(false);
		SubsTopicBtn.setDisable(true);
		unsubsTopicBtn.setDisable(true);
		RefreshPublishMsg.setDisable(true);
		DisconnectExitBtn.setDisable(true);
		
		//Subscribtion alternatives
		subscribChoiceBox.setValue(subscribeChoiceBoxList.get(0).replace("/", ""));
		subscribChoiceBox.setItems(subscribeChoiceBoxList);
		
		//QoSLevel
		QosLvlChoiceBox.setValue(0);
		QosLvlChoiceBox.setItems(QosLvlChoiceBoxList);
		
		//Unsubscribe Choicebox
		//unsubscribChoiceBox.setItems((ObservableList<String>) subscribedListView.getItems());
	}
	
}
