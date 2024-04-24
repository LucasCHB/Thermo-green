package control;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

public class SMSSender {
    
    private Controller myController;
    private VonageClient client;

    public SMSSender(Controller aController) {
    	this.myController = aController;
    	
    	this.client = VonageClient.builder().apiKey("23b2778c").apiSecret("gDJj7VDjytNPedBk").build();
    }
    
    public void sendSMS(String aPhoneNum, String aMessage) {
    	TextMessage message = new TextMessage("Vonage APIs", "33" + aPhoneNum.substring(1, 10), aMessage);

    	SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

    	if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
    	    System.out.println("Message sent successfully.");
    	} else {
    	    System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
    	}
    }
}
