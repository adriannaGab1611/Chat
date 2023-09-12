package com.example.tpo_05;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Hashtable;

public class Application extends javafx.application.Application {

    public static Session ses;
    public static Connection con;
    public static MessageProducer producer;
    public static TopicSubscriber topicSubscriber;

    public static void main(String[] args) {

        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        env.put("topic.mytopic", "mytopic");

        try {
            Context ctx = new InitialContext(env);
            ConnectionFactory fact = (ConnectionFactory) ctx.lookup("ConnectionFactory");
            String admDestName = "mytopic";
            Topic dest = (Topic) ctx.lookup(admDestName);
            con = fact.createConnection();
            con.setClientID(args[0]);
            ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = ses.createProducer(dest);
            topicSubscriber = ses.createDurableSubscriber(dest, args[0]);
            con.start();
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("chatGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        ChatController controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        con.close();
    }
}