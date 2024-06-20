package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class GenericByteMessage extends GenericMessage {
	
	private byte[] data;
	
	public GenericByteMessage(Type<? extends GenericByteMessage> type) {
		super(type);
	}

	public GenericByteMessage(byte[] data, Type<GenericByteMessage> type) {
		super(type);
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBytes(data);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		data = new byte[buf.readableBytes()];
		buf.readBytes(data);
	}

	@Override
	public void process(IPayloadContext ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}
	
}
