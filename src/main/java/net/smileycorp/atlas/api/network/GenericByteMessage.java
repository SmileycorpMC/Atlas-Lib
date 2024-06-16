package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class GenericByteMessage implements NetworkMessage {

	public GenericByteMessage() {}

	private byte[] data;

	public GenericByteMessage(byte[] data) {
		this.data=data;
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
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return null;
	}
	
}
