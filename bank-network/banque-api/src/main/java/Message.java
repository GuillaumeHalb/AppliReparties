import com.ensimag.api.message.EnumMessageType;
import com.ensimag.api.message.IAction;
import com.ensimag.api.message.IMessage;

public class Message implements IMessage {

	private IAction action;
	private long messageId;
	private long originalBankSenderId;
	private long senderId;
	private long destinationBankId;
	private EnumMessageType messageType;
	
	public Message(IAction action, long messageId, long originalBankSenderId,
			long senderId, long destinationBankId, EnumMessageType messageType) {
		this.action = action;
		this.messageId = messageId;
		this.originalBankSenderId = originalBankSenderId;
		this.senderId = senderId;
		this.destinationBankId = destinationBankId;
		this.messageType = messageType;
	}
	
	@Override
	public long getMessageId() {
		return this.messageId;
	}

	@Override
	public IAction getAction() {
		return this.action;
	}

	@Override
	public long getOriginalBankSenderId() {		
		return this.originalBankSenderId;
	}

	@Override
	public long getSenderId() {
		return this.senderId;
	}

	@Override
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	@Override
	public long getDestinationBankId() {
		return this.destinationBankId;
	}

	@Override
	public IMessage clone() { 
		return new Message(action, messageId, originalBankSenderId, senderId, destinationBankId, messageType);
	}

	@Override
	public EnumMessageType getMessageType() {
		return this.messageType;
	}
}
