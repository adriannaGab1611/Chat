package com.example.tpo_05;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class ChatController {
    public Label chatLabel;
    public ListView chatListView;
    public TextField chatTextField;
    public Button sentButton;

    @FXML
    protected void onButtonClick() throws JMSException {
        TextMessage textMessage = Application.ses.createTextMessage();
        textMessage.setText(chatTextField.getText());
        Application.producer.send(textMessage);
    }
    @FXML
    void initialize(){
        try {
            Application.topicSubscriber.setMessageListener(message -> {
                Platform.runLater(() -> {
                    try {
                        String mess = ((TextMessage)message).getText();
                        chatListView.getItems().add(mess);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}