package cgresearch.apps.hlsvis.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Container for functionality to communicate with a RabbitMQ queue.
 * 
 * @author Philipp Jenke
 *
 */
public class RabbitMqCommunication {

	/**
	 * Name of the queue to communicate with
	 */
	private final String queueName;

	/**
	 * Floag for a valid connection.
	 */
	private boolean isConnected = false;

	/**
	 * Connunication channel - only valid after successful connect.
	 */
	private Channel channel = null;

	/**
	 * Current connection, only valid after successfuly connect.
	 */
	private Connection connection = null;

	/**
	 * RabbitMQ user name
	 */
	private final String login;

	/**
	 * RabbitMQ password
	 */
	private final String password;

	/**
	 * Host of the rabbit mq server
	 */
	private final String host;

	private List<IMessageCallback> receiverList = new ArrayList<IMessageCallback>();

	/**
	 * Constructor.
	 */
	public RabbitMqCommunication(String queueName, String host, String login,
			String password) {
		this.login = login;
		this.host = host;
		this.password = password;
		this.queueName = queueName;
	}

	/**
	 * Connect to the queue.
	 */
	public void connect() {
		if (isConnected) {
			Logger.getInstance().message(
					"Cannot connect, already connected - disconnect first!");
			return;
		}

		isConnected = false;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(login);
		factory.setPassword(password);

		channel = null;
		connection = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException e) {
			Logger.getInstance().exception("", e);
			isConnected = false;
			return;
		}
		isConnected = true;

	}

	/**
	 * Send a message to the queue. Only valid after successful connect.
	 */
	public void sendMessage(String message) {
		if (!isConnected) {
			Logger.getInstance().message("Cannot send message, connect first!");
			return;
		}

		try {
			channel.queueDeclare(queueName, false, false, false, null);
			channel.basicPublish("", queueName, null, message.getBytes());

		} catch (IOException e) {
			Logger.getInstance()
					.exception("Failed to send message to queue", e);
		}

	}

	/**
	 * Read a message from the queue. Blocks until a message arrives. Return
	 * null on error.
	 */
	public String readMessage() {

		if (!isConnected) {
			Logger.getInstance().message("Cannot read message, connect first!");
			return null;
		}

		try {
			channel.queueDeclare(queueName, false, false, false, null);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			return message;
		} catch (IOException e) {
			Logger.getInstance().exception("Failed to read from queue.", e);
			return null;
		} catch (ConsumerCancelledException e) {
			Logger.getInstance().exception("Failed to read from queue.", e);
			return null;
		} catch (InterruptedException e) {
			Logger.getInstance().exception("Failed to read from queue.", e);
			return null;
		}

	}

	/**
	 * Close the current connection.
	 */
	public void disconnect() {
		if (!isConnected) {
			Logger.getInstance().message("Cannot send message, connect first!");
			return;
		}
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			Logger.getInstance().exception("Failed to disconnect.", e);
		}
		isConnected = false;
	}

	/**
	 * Register an additional receiver for messages.
	 */
	public void registerMessageReceiver(IMessageCallback receiver) {
		receiverList.add(receiver);
	}

	/**
	 * Wait for messages from a queue. Implemented in its own thread.
	 */
	public void waitForMessages() {

		if (!isConnected) {
			Logger.getInstance().message("Cannot read message, connect first!");
			return;
		}

		final QueueingConsumer consumer;
		try {
			channel.queueDeclare(queueName, false, false, false, null);
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			Logger.getInstance().exception("Failed to read from queue.", e);
			return;
		} catch (ConsumerCancelledException e) {
			Logger.getInstance().exception("Failed to read from queue.", e);
			return;
		}

		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					QueueingConsumer.Delivery delivery;
					try {
						delivery = consumer.nextDelivery();
					} catch (ShutdownSignalException e) {
						Logger.getInstance().exception(
								"Failed to read from queue.", e);
						return;
					} catch (ConsumerCancelledException e) {
						Logger.getInstance().exception(
								"Failed to read from queue.", e);
						return;
					} catch (InterruptedException e) {
						Logger.getInstance().exception(
								"Failed to read from queue.", e);
						return;
					}
					String message = new String(delivery.getBody());
					for (IMessageCallback empfaenger : receiverList) {
						empfaenger.messageReceived(message);
					}
				}
			}
		};
		t.start();
	}

	/**
	 * Program entry point (Testing)
	 */
	public static void main(String[] args) {
		RabbitMqCommunication comm = new RabbitMqCommunication("testQueue",
				"127.0.0.1", "hls", "hls");
		new ConsoleLogger();
		comm.connect();
		String message = "Welcome to the world of tomorrow.";
		comm.sendMessage(message);
		Logger.getInstance().message("Send: " + message);
		String receivedMessage = comm.readMessage();
		Logger.getInstance().message("Received: " + receivedMessage);
		comm.disconnect();
	}
}
