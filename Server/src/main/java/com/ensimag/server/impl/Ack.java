import com.ensimag.api.message.IAck;

public class Ack implements IAck {

	private static final long serialVersionUID = -6056893412666221878L;
	
	public long ackSenderId;
	public long ackMessageId;
	
	public Ack(long ackSenderId, long ackMessageId) {
		this.ackSenderId = ackSenderId;
		this.ackMessageId = ackMessageId;
	}
	
	@Override
	public long getAckSenderId() {
		return this.ackSenderId;
	}

	@Override
	public long getAckMessageId() {
		return this.ackMessageId;
	}
}
