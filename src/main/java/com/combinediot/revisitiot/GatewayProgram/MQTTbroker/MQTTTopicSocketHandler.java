package com.combinediot.revisitiot.GatewayProgram.MQTTbroker;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.net.Socket;
import java.util.ArrayList;

public class MQTTTopicSocketHandler {
	public static Multimap<String, Socket> topicsMap = ArrayListMultimap.create();
	boolean sameValuesExist;
	
	public void addSocketAndToopic(Socket socket, String topic) {		
		sameValuesExist = false;
		if(!(topicsMap.isEmpty())) {
			topicsMap.forEach((t ,s) -> {
				if(t.equals(topic) && s.equals(socket)) {
					System.err.println("Socket and Topic already exists in the map/list");
					sameValuesExist = true;
				}
			});
			
			if(sameValuesExist == false) {
				topicsMap.put(topic, socket);
			}
		}
		else
		{
			topicsMap.put(topic, socket);
		}
	}
	
	public void showMultimap() {
		System.err.println("-------------------------SOCKET/TOPICS MAP----------------------");
		topicsMap.forEach((k,v) -> System.err.println("Topics: " + k + " sockets: " + v));
		System.err.println("----------------------------------------------------------------");
	}
	
	public void removeSpecificTopicAndSocket(Socket client , String topic) {
		topicsMap.remove(topic, client);	
	}
	
	public ArrayList<Socket> getSocketsOfSpecificTopic(String topic) {
		ArrayList<Socket> so = new ArrayList<Socket>(); 
		
		topicsMap.forEach((t, s) ->{ 
			//System.out.println("TopicList:" + t + "TopicString:" + topic);	
			if(t.equals(topic)) {
				so.add(s);
			}
		});
		
		return so;
	}
}
	//--------------------------------------------------------------------
	
	/*static Map<Socket, String> topics = new HashMap<>();//can't store duplicate keys?
	
	static Map<String, ArrayList<Socket>> listTopic = new HashMap<String,ArrayList<Socket>>();
	List<Socket> arraylistSockets = new ArrayList<Socket>();*/
		
	/*public void AddTopicAndSocket(Socket socket, String topic) {	
		arraylistSockets.add(socket);
		listTopic.put(topic, (ArrayList<Socket>) arraylistSockets);
	}
	
	public void ShowTopicsSockets() {
		System.err.println("-------------------------SOCKET/TOPICS MAP----------------------");
		listTopic.forEach((k,v) -> System.err.println("Topics: " + k + " sockets: " + v));
		System.err.println("----------------------------------------------------------------");
	}
	
	
	public ArrayList<Socket> GetSocketOfSpecificTopic(String topic) {
			
			ArrayList<Socket> so = new ArrayList<Socket>(); 
			
			listTopic.forEach((t, s) ->{ 
				
				//System.out.println("TopicList:" + t + "TopicString:" + topic);	
				if(t.equals(topic)) {
					for(int i = 0; i < s.size(); i++)
					{
						System.out.println("Topic:" + t + " have subscribed clients sockets: " + s);
						so.add(s.get(i));
					}
				}
			});
			
			return so;
		}
	
	
	
	public void addTopicAndSocket(Socket socket, String topic) {
		topics.put(socket, topic);
	}
	
	public void showTopicsAndSockets() {
		System.err.println("-------------------------SOCKET/TOPICS MAP----------------------");
		topics.forEach((k,v) -> System.err.println("Socket: " + k + " topic: " + v));
		System.err.println("----------------------------------------------------------------");
	}
	
	public ArrayList<Socket> getSocket() {
		// TODO Auto-generated method stub
		ArrayList<Socket> so = new ArrayList<Socket>(); 
		
		topics.forEach((s, t) ->{ 
			so.add(s);
		});
		
		return so;
	}
	
	public ArrayList<Socket> getSocketOfSpecificTopic(String topic) {
		
		ArrayList<Socket> so = new ArrayList<Socket>(); 
		
		topics.forEach((s, t) ->{ 
			
			//System.out.println("TopicList:" + t + "TopicString:" + topic);
			
			if(t.equals(topic)) {
				System.out.println("Topic:" + t + " have subscribed clients sockets: " + s);
				so.add(s);
			}
		});
		
		return so;
	}
	
	public void removeSocketOfSpecificTopic(Socket client , String topic) {
		topics.remove(client, topic);
	}
	
	
	
	
	
}*/






	
